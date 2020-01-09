package biz.belcorp.consultoras.feature.search.single

import android.util.Log
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.consultoras.domain.interactor.*
import biz.belcorp.consultoras.domain.util.OfferTypes.getOfferTypeForAnalytics
import biz.belcorp.consultoras.exception.ErrorFactory
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.SearchCUVCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@PerActivity
class SearchProductPresenter @Inject
internal constructor(
    private val userUseCase: UserUseCase
    , private val orderUseCase: OrderUseCase
    , private val loginModelDataMapper: LoginModelDataMapper
    , private val accountUseCase: AccountUseCase
    , private val origenPedidoUseCase: OrigenPedidoUseCase
    , private val origenMarcacionUseCase: OrigenMarcacionUseCase
    , private val festivalUseCase: FestivalUseCase)
    : Presenter<SearchProductView> {

    private var searchProductView: SearchProductView? = null

    override fun attachView(view: SearchProductView) {
        searchProductView = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.userUseCase.dispose()
        this.orderUseCase.dispose()
        this.origenMarcacionUseCase.dispose()
        this.origenPedidoUseCase.dispose()
        this.searchProductView = null
    }

    /** functions */

    fun data() {
        userUseCase[GetUser()]
        getConfigFest()
    }

    fun getImageEnabled(){
        GlobalScope.launch{
            orderUseCase.getImageDialogEnabled()?.let{
                searchProductView?.setImageEnabled(it)
            }
        }
    }

    fun buscarCUV(cuv: String) {
        searchProductView?.showLoading()
        orderUseCase.searchCUV(cuv, CUVObserver())
    }

    fun trackBackPressed() {
        userUseCase[UserBackPressedObserver()]
    }

    fun getValorOrigen(palanca : String, codigo : String, product: ProductCUV){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = origenPedidoUseCase.getValor(palanca, codigo)
                GlobalScope.launch(Dispatchers.Main) {
                    searchProductView?.updateOriginValue(result.toString(), product)
                }

            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    searchProductView?.hideLoading()
                    when(e) {
                        is VersionException -> searchProductView?.onVersionError(e.isRequiredUpdate, e.url)
                        else -> searchProductView?.onError(ErrorFactory.create(e))
                    }
                }

            }
        }

    }

    fun getConfigFest() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                userUseCase.getUser()?.let { user ->
                    user.campaing?.toInt()?.let { campaing ->
                        festivalUseCase.getConfiguracion(campaing)?.let { config ->
                            searchProductView?.setConfigFest(config)
                        }
                    }
                }
            } catch (e: Exception) {}
        }
    }

    fun insertHomologado(product: ProductCUV, identifier: String){
        searchProductView?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result =  orderUseCase.insertarPedido(product, identifier,
                    false, -1, product.clienteId ?: 0)
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        searchProductView?.hideLoading()
                        when(it.code) {
                            SearchCUVCode.ERROR_PRODUCTO_FESTIVAL_ALCANZADO ->{
                                searchProductView?.onFestivalAwardReached(it.message)
                            }
                            SearchCUVCode.OK -> {
                                searchProductView?.onProductAdded(it.message)
                            }
                            SearchCUVCode.ERROR_CANTIDAD_EXCEDIDA -> {
                                searchProductView?.errorAddingProduct(it.message!!)
                            }
                            SearchCUVCode.ERROR_PRODUCTO_DUOPERFECTO_LIMITE -> {
                                searchProductView?.showToolTipError(it.message)
                            }
                            SearchCUVCode.ERROR_PRODUCTO_CANTIDAD_LIMITE -> {
                                searchProductView?.showErrorExcedido(it.message!!)
                            }
                            SearchCUVCode.ERROR_PRODUCTO_ALCANCE_PREMIO_FEST -> {
                                searchProductView?.onProductNotAdded(it.message)
                            }
                            else -> {
                                searchProductView?.showCUVWithError(it.message!!)
                            }
                        }
                    }
                }
            }catch (e: Exception){
                GlobalScope.launch(Dispatchers.Main) {
                    searchProductView?.hideLoading()
                    when(e){
                        is VersionException -> searchProductView?.onVersionError(e.isRequiredUpdate, e.url)
                        else -> searchProductView?.onError(ErrorFactory.create(e))
                    }
                }

            }
        }
    }

    fun getRelatedConfig(code: String) {
        accountUseCase.getConfig(code, RelatedConfigObserver())
    }

    fun getRelatedOffers(product: ProductCUV, minimoResultados: Int?, maximoResultados: Int?, caracteresDescripcion: Int?) {

        orderUseCase.getRelatedOffers(product.cuv!!,
            product.codigoProducto!!,
            minimoResultados,
            maximoResultados,
            caracteresDescripcion,
            RelatedOfferObserver(product, ""))
    }

    fun getNameListTracker(ubication: String, section: String, isSuggest: Boolean){
        GlobalScope.launch {
            try {
                val nameList = origenMarcacionUseCase.getValorLista(ubication, section)
                GlobalScope.launch(Dispatchers.Main) {
                    if (isSuggest) {
                        searchProductView?.setNameListTrackerSuggest(nameList)
                    } else {
                        searchProductView?.setNameListTracker(nameList)
                    }
                }
            }
            catch (e: Exception) {}
        }
    }

    /** observers */

    private inner class GetUser : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            t?.let {
                searchProductView?.setData(it)
            }
        }
    }

    private inner class RelatedConfigObserver : BaseObserver<Collection<UserConfigData?>?>() {

        override fun onNext(t: Collection<UserConfigData?>?) {
            t?.let {
                searchProductView?.setRelatedConfig(it.toList())
            }
        }
    }

    private inner class UserBackPressedObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            searchProductView?.trackBackPressed(loginModelDataMapper.transform(t))
        }
    }

    private inner class SuggestProductObserver(var code: String?, var mensaje: String?) : BaseObserver<Collection<ProductCUV?>?>() {

        override fun onNext(t: Collection<ProductCUV?>?) {
            t?.let {
                if (code == SearchCUVCode.ERROR_PRODUCTO_AGOTADO && t.isEmpty())
                    searchProductView?.showCUVWithError(mensaje)
                else
                    searchProductView?.showAlternative(SearchProductFragment.TYPE_SUGGEST_PRODUCT, mensaje, it, null)
            }
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)

            if (searchProductView == null) return

            searchProductView?.hideLoading()
            if (exception is VersionException) {
                searchProductView?.onVersionError(exception.isRequiredUpdate, exception.url)
            } else {
                searchProductView?.onError(ErrorFactory.create(exception))
            }
        }
    }

    private inner class RelatedOfferObserver(var product: ProductCUV, var mensaje: String?) : BaseObserver<RelatedOfferResponse?>() {

        override fun onNext(t: RelatedOfferResponse?) {

            searchProductView?.showCUV(product, "")

            t?.let { response ->

                response.productos?.let {
                    searchProductView?.showAlternative(SearchProductFragment.TYPE_RELATED_OFFER, mensaje, it, response.total
                        ?: 0)
                }
            }
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)

            if (searchProductView == null) return

            searchProductView?.showCUV(product, "")
            searchProductView?.hideLoading()

            if (exception is VersionException) {
                searchProductView?.onVersionError(exception.isRequiredUpdate, exception.url)
            } else {
                searchProductView?.onError(ErrorFactory.create(exception))
            }
        }
    }

    private inner class CUVObserver : BaseObserver<BasicDto<ProductCUV>?>() {

        override fun onNext(t: BasicDto<ProductCUV>?) {
            t?.let {
                when {
                    t.code == SearchCUVCode.OK -> {

                        it.data?.let { product ->
                            if (product.tieneOfertasRelacionadas == true) {
                                searchProductView?.getRelatedOffers(product)
                            } else {
                                searchProductView?.showCUV(product, "")
                            }
                        }
                    }
                    t.code == SearchCUVCode.ERROR_PRODUCTO_OFERTAREVISTA_ESIKA
                        || t.code == SearchCUVCode.ERROR_PRODUCTO_OFERTAREVISTA_LBEL
                    -> searchProductView?.showCUV(it.data!!, t.message ?: "")
                    t.code == SearchCUVCode.ERROR_PRODUCTO_NOEXISTE -> searchProductView?.productNotFound()
                    t.code == SearchCUVCode.ERROR_PRODUCTO_SUGERIDO ||
                        t.code == SearchCUVCode.ERROR_PRODUCTO_AGOTADO -> {
                        it.data?.let { d ->
                            orderUseCase.getSuggestedReplacement(d.cuv!!, SuggestProductObserver(t.code, t.message))
                        }
                    }
                    else -> {
                        searchProductView?.showCUVWithError(it)
                    }
                }
            }
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)

            if (searchProductView == null) return

            searchProductView?.hideLoading()
            if (exception is VersionException) {
                searchProductView?.onVersionError(exception.isRequiredUpdate, exception.url)
            } else {
                searchProductView?.onError(ErrorFactory.create(exception))
            }
        }

    }

    //private inner class SaveProductoCUVToPreviewObserver() : BaseObserver<BasicDto<Boolean>?>() {
    private inner class SaveProductoCUVToPreviewObserver : BaseObserver<BasicDto<Collection<MensajeProl?>?>?>() {

        override fun onNext(t: BasicDto<Collection<MensajeProl?>?>?) {
            searchProductView?.hideLoading()

            t?.let {
                when {
                    t.code == SearchCUVCode.OK -> {

                        if (t.data != null) {
                            t.data?.let { it1 ->
                                searchProductView?.onMensajeProl(it1)
                            }
                        }

                        when {
                            t.message!!.toUpperCase() != GlobalConstant.OK -> {
                                searchProductView?.showToolTipError(t.message)
                            }
                            else -> {
                                searchProductView?.onProductAdded(it.message)
                            }
                        }
                    }
                    else -> {
                        when {
                            t.code == SearchCUVCode.ERROR_CANTIDAD_EXCEDIDA -> {
                                searchProductView?.errorAddingProduct(t.message!!)
                            }
                            t.code == SearchCUVCode.ERROR_PRODUCTO_DUOPERFECTO_LIMITE -> {
                                searchProductView?.showToolTipError(t.message)
                            }
                            t.code == SearchCUVCode.ERROR_PRODUCTO_CANTIDAD_LIMITE -> {
                                searchProductView?.showErrorExcedido(t.message!!)
                            }
                            else -> searchProductView?.showCUVWithError(t.message!!)
                        }
                    }
                }
                searchProductView?.hideLoading()
            }
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)

            if (searchProductView == null) return

            searchProductView?.hideLoading()
            if (exception is VersionException) {
                searchProductView?.onVersionError(exception.isRequiredUpdate, exception.url)
            } else {
                searchProductView?.onError(ErrorFactory.create(exception))
            }
        }
    }

    fun getAnalytics(tipoPersonalizacion: String?, onAnalyticsLoaded: (descriptionPalanca: String) -> Unit) {

        GlobalScope.launch {

            try {
                var descriptionPalanca = origenMarcacionUseCase.getValorPalanca(getOfferTypeForAnalytics(tipoPersonalizacion))

                if (descriptionPalanca.isNullOrEmpty()) descriptionPalanca = GlobalConstant.NOT_AVAILABLE

                GlobalScope.launch(Dispatchers.Main) { onAnalyticsLoaded.invoke(descriptionPalanca) }

            } catch (e: Exception){

                Log.w(ANALYTICS_TAG, e.message)

            }

        }

    }

    fun getAnalyticsCarousel(list: List<ProductCUV?>, onAnalyticsLoaded: (listResult: List<ProductCUV?>) -> Unit) {

        GlobalScope.launch {

            try {
                list.toMutableList().forEachIndexed { index, productCUV ->
                    val descriptionPalanca = origenMarcacionUseCase.getValorPalanca(getOfferTypeForAnalytics(productCUV?.tipoPersonalizacion))

                     list[index]?.tipoPersonalizacion = if (descriptionPalanca == biz.belcorp.library.util.StringUtil.Empty) GlobalConstant.NOT_AVAILABLE else descriptionPalanca
                }
                GlobalScope.launch(Dispatchers.Main) { onAnalyticsLoaded.invoke(list) }

            } catch (e: Exception){

                Log.w(ANALYTICS_TAG, e.message)

            }

        }

    }

    companion object {

        const val ANALYTICS_TAG = "Belcorp-Analytics"

    }

}

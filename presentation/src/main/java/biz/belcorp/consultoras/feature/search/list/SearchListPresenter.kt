package biz.belcorp.consultoras.feature.search.list

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.Log
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.consultoras.domain.interactor.*
import biz.belcorp.consultoras.domain.util.OfferTypes.getOfferTypeForAnalytics
import biz.belcorp.consultoras.util.GlobalConstant.CODE_OK
import biz.belcorp.consultoras.util.StringUtil
import biz.belcorp.consultoras.util.analytics.Search
import biz.belcorp.consultoras.util.anotation.SearchCUVCode
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.sello.Sello
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@PerActivity
class SearchListPresenter @Inject
internal constructor(private val userUseCase: UserUseCase,
                     private val productUseCase: ProductUseCase,
                     private val orderUseCase: OrderUseCase,
                     private val offerUseCase: OfferUseCase,
                     private val loginModelDataMapper: LoginModelDataMapper,
                     private val origenMarcacionUseCase: OrigenMarcacionUseCase,
                     private val festivalUseCase: FestivalUseCase)
    : Presenter<SearchListView>, SafeLet {

    private var view: SearchListView? = null
    private var wishLoadPromotion = true
    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun attachView(view: SearchListView) {
        this.view = view
    }

    override fun destroy() {
        this.userUseCase.dispose()
        this.productUseCase.dispose()
        this.orderUseCase.dispose()
        this.origenMarcacionUseCase.dispose()
        this.view = null
    }

    // Functions
    fun getUser() {
        userUseCase[GetUser()]
    }

    fun getPromotionGroupList(): Boolean? {
        return orderUseCase.getPromotionGroupList()
    }

    fun trackBackPressed() {
        userUseCase[UserBackPressedObserver()]
    }

    fun searchText(searchRequest: SearchRequest) {
        view?.showLoadingSearch()
        productUseCase.search(searchRequest, SearchObserver())
    }

    fun trackAddItem(item: ProductCUV, searchByCuv: Boolean, user: User, palanca: String, textSearched: String) {
        Search.trackAddProduct(loginModelDataMapper.transform(user), item, searchByCuv, palanca, textSearched)
    }

    fun trackPrintProduct(item: ProductCUV, user: User, position: Int, location: String, section: String) {
        GlobalScope.launch {
            val lista = origenMarcacionUseCase.getValorLista(location, section)
            var palancaDescripcion = origenMarcacionUseCase.getValorPalanca(getOfferTypeForAnalytics(item.tipoPersonalizacion))
            Search.printProduct(item, loginModelDataMapper.transform(user), position, lista, palancaDescripcion)
        }
    }

    fun trackPrintClickProduct(item: ProductCUV, user: User, position: Int, location: String, section: String) {
        GlobalScope.launch {
            val lista = origenMarcacionUseCase.getValorLista(location, section)
            var palancaDescripcion = origenMarcacionUseCase.getValorPalanca(getOfferTypeForAnalytics(item.tipoPersonalizacion))
            Search.printClickProduct(item, loginModelDataMapper.transform(user), position, lista, palancaDescripcion)
        }
    }

    fun trackPrintAddProduct(item: ProductCUV, user: User, quantity: Int, location: String, section: String) {
        GlobalScope.launch {
            val lista = origenMarcacionUseCase.getValorLista(location, section)
            var palancaDescripcion = origenMarcacionUseCase.getValorPalanca(getOfferTypeForAnalytics(item.tipoPersonalizacion))
            Search.printAddProduct(item, loginModelDataMapper.transform(user), quantity, lista, palancaDescripcion)
        }
    }

    fun trackPrintEventSearch(category: String, action: String, label: String, screen: String, event: String) {
        GlobalScope.launch {
            Search.printEventSearch(category, action, label, screen, event)
        }
    }

    /**
     * Agrega una oferta recibiendo un objeto ProductoCUV como entrada
     */
    fun agregar(item: ProductCUV, identifier: String) {
        view?.showLoading()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = orderUseCase.insertarPedido(item, identifier)
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        if (it.code == CODE_OK || it.code == SearchCUVCode.ERROR_PRODUCTO_FESTIVAL_ALCANZADO) {
                            userUseCase.updateScheduler2()

                            val type = if (it.code == CODE_OK) SearchListFragment.TYPE_FESTIVAL_ADDED else SearchListFragment.TYPE_FESTIVAL_REACHED
                            view?.onProductAdded(item, orderUseCase.getImageDialogEnabled(), it.message, type)

                        } else {
                            view?.onAddProductError(it.message, null)
                        }
                        view?.hideLoading()
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.hideLoading()
                    if (e is VersionException)
                        view?.onVersionError(e.isRequiredUpdate, e.url)
                    else
                        e.message?.let {
                            BelcorpLogger.d(it)
                            view?.onAddProductError(it, e)
                        }
                }
                // Funcion en caso se de un error
            }
        }
    }

    fun getConfigSellos(context: Context){
        val listSello = arrayListOf<Sello?>()
        val selloFest = Sello(context)
        val selloLan = Sello(context)
        val selloPromo = Sello(context)
        GlobalScope.launch (Dispatchers.IO) {
            selloFest.setType(Sello.FESTIVAL)
            selloFest.setText(context.getString(R.string.tag_festival))
            context.resources.getDimension(R.dimen.fest_text)
            selloFest.setStartColor(ContextCompat.getColor(context, R.color.gana_mas_festival_start))
            selloFest.setEndColor(ContextCompat.getColor(context, R.color.gana_mas_festival_end))
            selloFest.setOrientation(3)
            selloFest.setTextSize(context.resources.getDimension(R.dimen.fest_text))

            val selloConfigFest = festivalUseCase.getSelloConfiguracion()
            selloConfigFest?.let { config ->
                config.selloTexto?.let { if(it.isNotEmpty()) selloFest.setText(it) }
                config.selloColorTexto?.let {
                    if(StringUtil.isHexColor(it))
                        selloFest.setTextColor(Color.parseColor(it)) }
                safeLet(config.selloColorInicio, config.selloColorFin, config.selloColorDireccion){colorInicio, colorFin, orientacion ->
                    selloFest.setBackgroundSello(colorInicio, colorFin, orientacion)
                }

            }

            listSello.add(selloFest)
            selloLan.setType(Sello.LAN)
            selloLan.setText(context.getString(R.string.title_sello_new))
            selloLan.setTextSize(context.resources.getDimension(R.dimen.lan_text))
            selloLan.setStartColor(ContextCompat.getColor(context, R.color.tag_new_start))
            selloLan.setEndColor(ContextCompat.getColor(context, R.color.tag_new_end))
            selloLan.setOrientation(3)
            listSello.add(selloLan)

            selloPromo.setType(Sello.PROMOTION)
            selloPromo.setText(context.getString(R.string.title_sello_promo))
            selloPromo.setTextSize(context.resources.getDimension(R.dimen.promotion_text))
            selloPromo.setStartColor(ContextCompat.getColor(context, R.color.tag_promo_start))
            selloPromo.setEndColor(ContextCompat.getColor(context, R.color.tag_promo_end))
            selloPromo.setOrientation(3)
            listSello.add(selloPromo)

            GlobalScope.launch (Dispatchers.Main) {
                view?.setConfigSellos(listSello)
            }
        }

    }

    fun getRecentOffers(context: Context){
        GlobalScope.launch (Dispatchers.IO) {
            val session = SessionManager.getInstance(context)
            val countMax = session.getCountMaxRecentSearch()

            val result = offerUseCase.getRecentOffers(countMax)

            GlobalScope.launch (Dispatchers.Main) {
               view?.loadRecentOffers(result)
            }
        }
    }

    fun deleteByCuv(cuv: String){
        GlobalScope.launch (Dispatchers.IO) {
            val result = offerUseCase.deleteByCuv(cuv)
        }
    }

    fun deleteAllRecentOffers(){
        GlobalScope.launch (Dispatchers.IO) {
            val result = offerUseCase.deleteAllRecentOffers()
        }

    }

    // Observers
    private inner class UserBackPressedObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            view?.trackBackPressed(loginModelDataMapper.transform(t))
        }
    }

    private inner class GetUser : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            view?.setUser(t)
        }
    }

    private inner class SearchObserver : DisposableObserver<BasicDto<SearchResponse?>>() {

        override fun onNext(t: BasicDto<SearchResponse?>) {
            t.let {
                if (it.code == CODE_OK) {

                    view?.onSearchResult(t.data)
                    t.data?.promocion?.detalle?.let { detalle ->
                        if(detalle.isEmpty()) this@SearchListPresenter.loadPromotions(t.data?.productos)
                    }

                } else {
                    view?.onSearchError(t.message, null)
                }
            }
            view?.hideLoadingSearch()
        }

        override fun onError(exception: Throwable) {
            view?.onSearchError(null, exception)
            view?.hideLoadingSearch()
        }

        override fun onComplete() {
            // EMPTY
        }
    }

    fun trackEvent(screenName: String,
                   category: String,
                   eventAction: String,
                   eventLabel: String,
                   eventName: String) {
        userUseCase[EventPropertyObserver(screenName, category, eventAction, eventLabel, eventName)]
    }

    fun getCategoriasInSearch() {

        view?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = offerUseCase.categoriasInSearch()
                result?.let { list ->
                    GlobalScope.launch(Dispatchers.Main) {
                        if (list.isNotEmpty())
                            view?.setCategoriesInSearch(list)
                        view?.hideLoading()
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.hideLoading()
                    e.message?.let { BelcorpLogger.d(it) }
                }
            }

        }
    }

    private inner class EventPropertyObserver(
        var screenHome: String,
        var category: String,
        var eventAction: String,
        var eventLabel: String,
        var eventName: String) : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            t.let {
                Tracker.trackEvent(screenHome,
                    category,
                    eventAction,
                    eventLabel,
                    eventName,
                    it)
            }
        }
    }

    private fun loadPromotions(detalle: Collection<ProductCUV?>?) {
        GlobalScope.launch(Dispatchers.IO) {
            try {

                delay(300)

                val cuvs = getCuvs(detalle)
                val list = ArrayList<OfferPromotionDual>()
                if (cuvs.isNotEmpty()) {

                    val result = orderUseCase.getConditions(cuvs)

                    result?.forEach {
                        list.add(OfferPromotionDual().apply {
                            cuvCondition = it?.cuvCondition
                            cuvPromotion = it?.cuvPromotion
                            descriptionCondition = it?.descriptionCondition
                            descriptionPromotion = it?.descriptionPromotion
                            imageURLCondition = it?.imageURLCondition
                            imageURLPromotion = it?.imageURLPromotion
                            priceCondition = it?.priceCondition
                            pricePromotion = it?.pricePromotion
                            typeCondition = it?.typeCondition
                            typePromotion = it?.typePromotion
                        })
                    }
                }

                GlobalScope.launch(Dispatchers.Main) {
                    view?.loadPromotions(list)
                }

            } catch (e: Exception) {
                //this@SearchListPresenter.wishLoadPromotion = true
            }
        }
    }

    private fun getCuvs(detalle: Collection<ProductCUV?>?): String {
        var lstCuvs = ArrayList<String>()

        detalle?.filter { it -> it?.cuv?.length ?: 0 > 0 }?.forEach {
            lstCuvs.add(it!!.cuv!!)
        }

        return lstCuvs.joinToString("|")
    }
}

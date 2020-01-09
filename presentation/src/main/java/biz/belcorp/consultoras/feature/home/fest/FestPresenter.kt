package biz.belcorp.consultoras.feature.home.fest

import android.util.Log
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.FestivalNoOffersException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.consultoras.domain.interactor.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.util.Belcorp
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.AddedAlertType
import biz.belcorp.consultoras.util.anotation.SearchOriginType
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.StringUtil
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.design.filter.model.CategoryFilterModel
import biz.belcorp.mobile.components.design.filter.model.FilterModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@PerActivity
class FestPresenter @Inject
constructor(private val origenPedidoUseCase: OrigenPedidoUseCase,
            private val orderUseCase: OrderUseCase,
            private val userUseCase: UserUseCase,
            private val offerUseCase: OfferUseCase,
            private val festivalUseCase: FestivalUseCase,
            private val menuUseCase: MenuUseCase) : Presenter<FestView>, SafeLet {

    private var view: FestView? = null
    private var user: User? = null


    private val ORDER_TYPE_ASC = "ASC"
    private val ORDER_TYPE_DESC = "DESC"
    private val ORDER_DEFECTO = "ORDEN"
    private val ORDER_GANANCIA = "GANANCIA"
    private val ORDER_PRECIO = "PRECIO"

    val LAYOUT_ORDER = 1
    val LAYOUT_FILTER = 2

    /**
     * Presenter abstract functions
     */

    override fun attachView(view: FestView) {
        this.view = view
    }

    override fun resume() { /* Not necessary */
    }

    override fun pause() { /* Not necessary */
    }

    override fun destroy() {
        this.view = null
        this.menuUseCase.dispose()
        this.offerUseCase.dispose()
        this.userUseCase.dispose()
        this.orderUseCase.dispose()
        this.festivalUseCase.dispose()
        this.origenPedidoUseCase.dispose()
    }

    /**
     * Public functions
     */

    /**
     * Obtiene la información del usuario (para obtener la configuración)
     */

    fun getUser() {
        GlobalScope.launch(Dispatchers.IO) {
            userUseCase.getUser()?.let {
                user = it
                GlobalScope.launch(Dispatchers.Main) {
                    view?.setUser(it)
                    it.campaing?.toInt()?.let { campaing ->
                        try {
                            withContext(Dispatchers.IO) { festivalUseCase.getConfiguracion(campaing) }?.let { config ->
                                view?.setConfiguracion(config)
                            }
                        } catch (e: Exception) {
                            BelcorpLogger.e(e)
                            view?.showErrorScreenMessage(FestErrorFragment.ERROR_MESSAGE_EMPTY_FEST)
                        }
                    }
                }
            }
        }
    }

    fun getFestivalProgress() {
        view?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                offerUseCase.getFestivalProgress()?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        view?.hideLoading()
                        view?.updateRewards(it)
                    }
                } ?: run {
                    view?.showErrorScreenMessage(FestErrorFragment.ERROR_MESSAGE_EMPTY_PROGRESS)
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.hideLoading()
                    when (e) {
                        is NetworkErrorException -> view?.showErrorScreenMessage(FestErrorFragment.ERROR_MESSAGE_NETWORK)
                        else -> {
                            e.message?.let {
                                BelcorpLogger.d(it)
                            }
                        }
                    }
                }
            }
        }
    }

    fun getOffersFestival(categoryCode: String, categoryName: String, order: String, orderType: String) {
        view?.showLoading()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val festivalResponse = offerUseCase.getOffersFestival(order, orderType, createSearchByProductsFest(categoryCode, categoryName))

                GlobalScope.launch(Dispatchers.Main) {
                    view?.hideLoading()
                    festivalResponse?.let {
                        view?.setOffersFestival(it)
                    } ?: run {
                        view?.showErrorScreenMessage(FestErrorFragment.ERROR_MESSAGE_EMPTY_FEST)
                    }
                }

            } catch (e: Exception) {
                BelcorpLogger.e(e)
                GlobalScope.launch(Dispatchers.Main) {
                    view?.hideLoading()
                    when (e) {
                        is FestivalNoOffersException -> view?.showErrorScreenMessage(FestErrorFragment.ERROR_MESSAGE_EMPTY_FEST)
                        is NetworkErrorException -> view?.showErrorScreenMessage(FestErrorFragment.ERROR_MESSAGE_NETWORK)
                        else -> {
                            e.message?.let {
                                BelcorpLogger.d(it)
                            }
                        }
                    }
                }
            }
        }
    }

    fun addToCart(item: ProductCUV, codigoOrigenPedidoWeb: String, counterView: Counter?, identifier: String) {
        view?.showLoading()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                item.tipoPersonalizacion = if (item.tipoPersonalizacion == OfferTypes.OPM) OfferTypes.RD else item.tipoPersonalizacion
                item.tipoPersonalizacion?.let { p ->
                    if (p == OfferTypes.CAT) {
                        item.marcaId?.let { item.origenPedidoWeb = origenPedidoUseCase.getValor(p, codigoOrigenPedidoWeb + Belcorp.getBrandOrigenById(it)).toString() }
                    } else {
                        item.origenPedidoWeb = origenPedidoUseCase.getValor(p, codigoOrigenPedidoWeb).toString()
                    }
                }

                val result = orderUseCase.insertarPedido(item, identifier)
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {

                        getFestivalProgress()

                        if (it.code == GlobalConstant.CODE_OK || it.code == GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO) {
                            val codeAlert = when (it.code) {
                                GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO -> AddedAlertType.FESTIVAL
                                else -> AddedAlertType.DEFAULT
                            }
                            counterView?.resetQuantity()
                            userUseCase.updateScheduler2()
                            view?.onProductAdded(item, it.message, codeAlert)
                        } else {
                            view?.onAddProductError(it.message, null)
                        }

                        view?.hideLoading()
                    }
                }
            } catch (e: Exception) {

                getFestivalProgress()

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
            }
        }
    }

    fun getOrders() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = offerUseCase.ordenamientos()
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        if (it.isNotEmpty()) {
                            view?.setOrders(it)
                        }
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.setFilterOrderLabel(false, LAYOUT_ORDER)
                }
            }
        }
    }

    fun getFilters() {
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val result = offerUseCase.filtros(false)
                    result?.let {
                        GlobalScope.launch(Dispatchers.Main) {
                            if (it.isNotEmpty())
                                view?.setFilters(it)
                        }
                    }
                } catch (e: Exception) {
                    GlobalScope.launch(Dispatchers.Main) {
                        view?.hideFilters()
                        e.message?.let { BelcorpLogger.d(it) }
                    }
                }
            }
        }
    }


    fun agregarSubCampania(item: ProductCUV, identifier: String) {
        view?.showLoading()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                var valorOrigen = ""
                valorOrigen = origenPedidoUseCase.getValor(OfferTypes.SR, SearchOriginType.ORIGEN_LANDING_SUBCAMPANIA).toString()
                item.apply {
                    origenPedidoWeb = valorOrigen
                }
                val result = orderUseCase.insertarPedido(item, identifier)
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        if (it.code == GlobalConstant.CODE_OK || it.code == GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO) {
                            val codeAlert = when (it.code) {
                                GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO -> AddedAlertType.FESTIVAL
                                else -> AddedAlertType.DEFAULT
                            }
                            userUseCase.updateScheduler2()
                            view?.onProductAdded(item, it.message, codeAlert)
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

    fun setOrderFestivalProduct(resultResponse: List<FestivalProduct?>?, order:String, orderType:String):List<FestivalProduct?>?{
        if(  !(orderType.equals(ORDER_TYPE_ASC) && order.equals(ORDER_DEFECTO))  ){
            when(orderType){
                ORDER_TYPE_ASC->{
                    return resultResponse?.
                        sortedWith(compareBy{
                            when(order){
                                ORDER_GANANCIA->{
                                    it?.profit
                                }
                                ORDER_PRECIO->{
                                    it?.catalogPrice
                                }
                                else->{
                                    it?.description
                                }
                            }
                        })
                }
                ORDER_TYPE_DESC->{
                    return resultResponse?.
                        sortedWith(compareByDescending{
                            when(order){
                                ORDER_GANANCIA->{
                                    it?.profit
                                }
                                ORDER_PRECIO->{
                                    it?.catalogPrice
                                }
                                else->{
                                    it?.description
                                }
                            }
                        })
                }
                else->{
                }
            }
        }
        return resultResponse
    }

    fun refreshSchedule() {
        GlobalScope.launch(Dispatchers.IO) {
            userUseCase.updateScheduler2()
            GlobalScope.launch(Dispatchers.Main) {
                view?.goToOffers()
            }
        }
    }

    fun getImageEnabled() {
        GlobalScope.launch(Dispatchers.IO) {
            orderUseCase.getImageDialogEnabled()?.let {
                view?.setImageEnabled(it)
            }
        }
    }

    private fun createSearchByProductsFest(categoryCode: String, categoryName: String): MutableList<SearchFilter> {
        return mutableListOf(SearchFilter().apply {
            nombreGrupo = CATEGORY_GROUP_NAME
            opciones = mutableListOf(SearchFilterChild().apply {
                idFiltro = categoryCode
                nombreFiltro = categoryName
            })
        })
    }

    fun getConditionsOffersByFilter(categoryCode: String, categoryName: String, order: String, orderType: String, groups: ArrayList<CategoryFilterModel>?) {
        user?.let {
            view?.showLoading()
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val filterList = createSearchByProductsFest(categoryCode, categoryName)
                    groups?.filter {
                        it.list.any {
                            it as FilterModel
                            it.checked
                        }
                    }?.forEach {
                        filterList.add(SearchFilter().apply {
                            nombreGrupo = it.key
                            opciones = (it.list as List<FilterModel>).filter { it.checked }.map {
                                SearchFilterChild().apply {
                                    idFiltro = it.key
                                    nombreFiltro = it.name
                                }
                            }
                        })
                    }

                    var products: List<FestivalProduct?> = ArrayList()
                    offerUseCase.getOffersFestival(order, orderType, filterList)?.let {
                        it.listConditions?.let { list ->
                            products = list
                        }
                    }

                    GlobalScope.launch(Dispatchers.Main) {
                        view?.hideLoading()
                        view?.setConditionsOffersByFilter(products)
                    }
                } catch (e: Exception) {
                    GlobalScope.launch(Dispatchers.Main) {
                        if (e is NetworkErrorException) view?.showErrorScreenMessage(FestErrorFragment.ERROR_MESSAGE_NETWORK)
                        else view?.showErrorScreenMessage(FestErrorFragment.ERROR_MESSAGE_FEST)
                        view?.hideLoading()
                        BelcorpLogger.d(e)
                        e.message?.let { BelcorpLogger.d(it) }
                    }
                }
            }
        }
    }

    companion object {
        const val LAYOUT_ORDER = 1
        const val LAYOUT_FILTER = 2
        const val CATEGORY_GROUP_NAME = "Categorías"
    }
}

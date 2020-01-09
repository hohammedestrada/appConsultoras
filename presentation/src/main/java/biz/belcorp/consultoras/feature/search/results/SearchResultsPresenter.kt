package biz.belcorp.consultoras.feature.search.results

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.consultoras.domain.interactor.*
import biz.belcorp.consultoras.domain.util.OfferTypes.getOfferTypeForAnalytics
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.GlobalConstant.CODE_OK
import biz.belcorp.consultoras.util.analytics.Search
import biz.belcorp.consultoras.util.anotation.FilterType
import biz.belcorp.library.log.BelcorpLogger
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@PerActivity
class SearchResultsPresenter @Inject
internal constructor(private val userUseCase: UserUseCase
                     , private val productUseCase: ProductUseCase
                     , private val orderUseCase: OrderUseCase
                     , private val loginModelDataMapper: LoginModelDataMapper,
                     private val offerUseCase: OfferUseCase,
                     private val origenMarcacionUseCase: OrigenMarcacionUseCase)
    : Presenter<SearchResultsView> {

    private var view: SearchResultsView? = null

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun attachView(view: SearchResultsView) {
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

    fun trackBackPressed() {
        userUseCase[UserBackPressedObserver()]
    }

    fun getFiltros(conHijos: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = offerUseCase.filtros(conHijos)
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

    fun searchText(searchRequest: SearchRequest) {
        productUseCase.search(searchRequest, SearchObserver())
    }

    fun getImageEnabled() {
        GlobalScope.launch {
            orderUseCase.getImageDialogEnabled()?.let {
                view?.setImageEnabled(it)
            }
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
                        if(it.code == CODE_OK || it.code == GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO){
                            userUseCase.updateScheduler2()
                            view?.onProductAdded(item, it.message, it.code)
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

    fun getOrderByParameters() {
        productUseCase.getOrderByParameters(SearchOrderByParametersObserver())
    }

    fun trackEvent(screen: String,
                   eventCat: String,
                   eventAction: String,
                   eventLabel: String,
                   eventName: String,
                   user: User) {

        Tracker.trackEvent(screen,
            eventCat,
            eventAction,
            eventLabel,
            eventName,
            loginModelDataMapper.transform(user))
    }

    fun trackEvent(screenName: String,
                   category: String,
                   eventAction: String,
                   eventLabel: String,
                   eventName: String) {
        userUseCase[EventPropertyObserver(screenName, category, eventAction, eventLabel, eventName)]
    }

    fun trackEventFilterSelected(filterAction: String,
                                 screenName: String,
                                 category: String,
                                 eventAction: String,
                                 eventLabel: String,
                                 eventName: String) {
        if (filterAction == FilterType.CATEGORY || filterAction == FilterType.BRAND ||
            filterAction == FilterType.PRICE) {
            userUseCase[EventPropertyObserver(screenName, category,
                eventAction.plus(filterAction.substring(0, filterAction.length - 1)),
                eventLabel, eventName)]
        }
    }

    fun trackEventFilterOnApply(filtersApply: List<SearchFilter?>?,
                                screenName: String,
                                category: String,
                                eventAction: String,
                                eventName: String) {
        val filterList = filtersApply?.filter {
            it?.nombreGrupo == FilterType.CATEGORY ||
                it?.nombreGrupo == FilterType.BRAND || it?.nombreGrupo == FilterType.PRICE
        }
        if (filterList != null && filterList.isNotEmpty()) {
            val sb = StringBuilder()
            val listCategory = mutableListOf<String?>()
            val listBrand = mutableListOf<String?>()
            val listPrice = mutableListOf<String?>()
            filterList.let {
                it.forEach { s ->
                    s?.opciones?.forEach { o ->
                        val filtro = o?.nombreFiltro ?: ""
                        when (s.nombreGrupo) {
                            FilterType.CATEGORY -> listCategory.add(filtro)
                            FilterType.BRAND -> listBrand.add(filtro)
                            FilterType.PRICE -> listPrice.add(filtro)
                        }
                    }
                }
            }

            listCategory.forEach { o ->
                sb.append(" $o -")
            }
            if (listCategory.isNotEmpty() && (listBrand.isNotEmpty() || listPrice.isNotEmpty())) {
                sb.append("|")
            }

            listBrand.forEach { o ->
                sb.append(" $o -")
            }
            if (listPrice.isNotEmpty() && listBrand.isNotEmpty()) {
                sb.append("|")
            }

            listPrice.forEach { o ->
                sb.append(" ${o!!.replace("-", "a")} -")
            }

            var labelStr = sb.toString().replace("-|", "|")
            labelStr = labelStr.substring(0, labelStr.length - 1).trim()

            userUseCase[EventPropertyObserver(screenName, category, eventAction, labelStr, eventName)]
        }
    }

    fun trackAddItem(item: ProductCUV, searchByCuv: Boolean, user: User, palanca: String,
                     textSearched: String) {
        Search.trackAddProduct(loginModelDataMapper.transform(user), item, searchByCuv,
            palanca, textSearched)
    }

    fun trackPrintProduct(item: ProductCUV, user: User, posicion: Int, location: String, section: String) {
        GlobalScope.launch {
            val lista = origenMarcacionUseCase.getValorLista(location, section)
            var palancaDescripcion = origenMarcacionUseCase.getValorPalanca(getOfferTypeForAnalytics(item.tipoPersonalizacion))

            Search.printProduct(item, loginModelDataMapper.transform(user), posicion, lista, palancaDescripcion)
        }
    }

    fun trackPrintClickProduct(item: ProductCUV, user: User, posicion: Int, location: String, section: String) {
        GlobalScope.launch {
            val lista = origenMarcacionUseCase.getValorLista(location, section)
            var palancaDescripcion = origenMarcacionUseCase.getValorPalanca(getOfferTypeForAnalytics(item.tipoPersonalizacion))
            Search.printProduct(item, loginModelDataMapper.transform(user), posicion, lista,palancaDescripcion)
        }
    }

    fun trackPrintAddProduct(item: ProductCUV, user: User, posicion: Int, location: String, section: String) {
        GlobalScope.launch {
            val lista = origenMarcacionUseCase.getValorLista(location, section)
            var palancaDescripcion = origenMarcacionUseCase.getValorPalanca(getOfferTypeForAnalytics(item.tipoPersonalizacion))
            Search.printAddProduct(item, loginModelDataMapper.transform(user), posicion, lista, palancaDescripcion)
        }
    }

    fun trackPrintEventSearch(category: String, action: String, label: String, screen: String, event: String) {
        GlobalScope.launch {
            Search.printEventSearch(category, action, label, screen, event)
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
                if (it.code == CODE_OK) view?.onSearchResult(t.data)
                else view?.onSearchError(t.message, null)
            }
            view?.hideLoading()
        }

        override fun onError(exception: Throwable) {
            view?.onSearchError(null, exception)
            view?.hideLoading()
        }

        override fun onComplete() {
            // EMPTY
        }
    }

    private inner class SearchOrderByParametersObserver
        : DisposableObserver<BasicDto<Collection<SearchOrderByResponse?>?>?>() {

        override fun onNext(t: BasicDto<Collection<SearchOrderByResponse?>?>) {
            t.let {
                if (it.code == CODE_OK) {
                    it.data?.let { data ->
                        view?.onOrderByParametersResult(data)
                    }
                }
            }
            view?.hideLoading()
        }

        override fun onError(exception: Throwable) {
            view?.onSearchError(null, exception)
            view?.hideLoading()
        }

        override fun onComplete() {
            // EMPTY
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

}

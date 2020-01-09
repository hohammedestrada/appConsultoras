package biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.demostrador

import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.caminobrillante.DemostradorCaminoBrillante
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.consultoras.domain.interactor.CaminoBrillanteUseCase
import biz.belcorp.consultoras.domain.interactor.OfferUseCase
import biz.belcorp.consultoras.domain.interactor.OrderUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.OrderOriginCode
import biz.belcorp.consultoras.util.anotation.SearchCUVCode
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.mobile.components.design.counter.Counter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DemostradorPresenter @Inject
internal constructor(private val userUseCase: UserUseCase,
                     private val orderUseCase: OrderUseCase,
                     private val offerUseCase: OfferUseCase,
                     private val caminobrillanteUseCase: CaminoBrillanteUseCase) : Presenter<DemostradorView> {

    private var view: DemostradorView? = null

    private lateinit var user: User

    override fun attachView(view: DemostradorView) {
        this.view = view
    }

    override fun resume() {}

    override fun pause() {}

    override fun destroy() {
        this.view?.hideLoading()
        this.view = null
        this.userUseCase.dispose()
        this.orderUseCase.dispose()
        this.offerUseCase.dispose()
        this.caminobrillanteUseCase.dispose()
    }

    fun loadFiltrosYOrdenamiento() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = caminobrillanteUseCase.getConfiguracionDemostrador()
                GlobalScope.launch(Dispatchers.Main) {
                    when {
                        result.code == SearchCUVCode.OK -> {
                            result.data?.let {
                                it.filtros?.let { groupFilterList ->
                                    when {
                                        groupFilterList.isEmpty() -> {
                                            view?.hideFilters()
                                        }
                                        else -> view?.onFiltrosLoaded(groupFilterList)
                                    }
                                } ?: view?.hideFilters()

                                it.ordenamientos?.let { listOrdenamiento ->
                                    when {
                                        listOrdenamiento.isEmpty() -> {
                                            view?.hideOrders()
                                        }
                                        else -> view?.onOrderLoaded(listOrdenamiento)
                                    }
                                } ?: view?.hideOrders()

                            }
                        }
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.hideFilters()
                    view?.hideOrders()
                    e.message?.let { BelcorpLogger.e(it) }
                }
            }
        }
    }

    fun loadDemostradores(orden: String?, filtro: String?, inicio: Int?, cantidad: Int?) {
        view?.showLoading()
        view?.setCanBack(false)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = caminobrillanteUseCase.getDemostradoresOfertas(orden, filtro, inicio, cantidad)
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        when {
                            it.code == SearchCUVCode.OK -> {
                                it.data?.let {
                                    when (it.isEmpty()) {
                                        true -> view?.showError(R.string.problemas_con_los_demostradores)
                                        false -> {
                                            view?.hideError()
                                            view?.showDemostradores(it)
                                        }
                                    }
                                } ?: run {
                                    view?.showError(R.string.problemas_con_los_demostradores)
                                }
                            }
                            else -> {
                                it.message?.let {
                                    view?.showError(it)
                                } ?: view?.showError(R.string.problemas_con_los_demostradores)
                            }
                        }
                        view?.onDemostradorLoaded()
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.hideLoading()
                    when (e) {
                        is NetworkErrorException -> view?.showError(R.string.sin_conexion_a_internet)
                        is VersionException -> view?.onVersionError(e.isRequiredUpdate, e.url)
                        else -> {
                            e.message?.let {
                                BelcorpLogger.d(it)
                                view?.showError(it)
                            }.run {
                                view?.showError(R.string.problemas_con_los_demostradores)
                            }
                        }
                    }
                }
            }

        }
    }

    fun addDemostrador(counter: Counter, item: DemostradorCaminoBrillante, quantity: Int, position: Int, identifier: String) {
        view?.showLoading()

        val productCUV = ProductCUV().apply {
            cuv = item.cuv
            description = item.descripcionCUV ?: item.descripcionCortaCUV
            descripcionMarca = item.descripcionMarca
            marcaId = item.marcaID
            precioCatalogo = item.precioCatalogo
            precioValorizado = item.precioValorizado
            fotoProducto = item.fotoProductoSmall
            origenPedidoWeb = OrderOriginCode.CAMINO_BRILLANTE_OFERTAS_OFERTAS_ESPECIALES_CARRUSEL
            tipoEstrategiaId = "" + item.tipoEstrategiaID
            codigoEstrategia = item.codigoEstrategia?.toInt()
            estrategiaId = item.estrategiaID
            cantidad = quantity
            this.identifier = identifier
        }

        Tracker.CaminoBrillante.onAddProduct(productCUV, quantity, GlobalConstant.LIST_DEMOSTRADORES, user)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = orderUseCase.insertarPedido(productCUV, identifier)
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        view?.hideLoading()
                        when {
                            it.code == SearchCUVCode.OK -> {
                                /*
                                it.message?.let {
                                    when {
                                        it.toUpperCase() != GlobalConstant.OK -> {
                                            view?.showMessage(it)
                                            userUseCase.updateScheduler2()
                                        }
                                        else -> {
                                            view?.onDemostradorAdded(items, counter, position, quantity)
                                        }
                                    }
                                }
                                    ?: view?.showMessage(R.string.error_agregar_demostrador)
                                */
                                view?.onDemostradorAdded(item, counter, position, quantity)
                            }
                            else -> {
                                it.message?.let {
                                    view?.showMessage(it)
                                } ?: view?.showMessage(R.string.error_agregar_demostrador)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.hideLoading()
                    when (e) {
                        is NetworkErrorException -> view?.showMessage(R.string.sin_conexion_a_internet)
                        is VersionException -> view?.onVersionError(e.isRequiredUpdate, e.url)
                        else -> {
                            e.message?.let {
                                BelcorpLogger.d(it)
                                view?.showMessage(it)
                            }
                                ?: view?.showMessage(R.string.error_agregar_demostrador)
                        }
                    }
                }
            }
        }
    }

    fun getUser() {
        GlobalScope.launch {
            userUseCase.getUser()?.let {
                user = it
                GlobalScope.launch(Dispatchers.Main) {
                    view?.setUser(it)
                }
            }
        }
    }

}

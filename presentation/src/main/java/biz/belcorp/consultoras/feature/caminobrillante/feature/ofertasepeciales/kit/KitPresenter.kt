package biz.belcorp.consultoras.feature.caminobrillante.feature.ofertasepeciales.kit

import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.caminobrillante.KitCaminoBrillante
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.consultoras.domain.interactor.CaminoBrillanteUseCase
import biz.belcorp.consultoras.domain.interactor.OrderUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.OrderOriginCode
import biz.belcorp.consultoras.util.anotation.SearchCUVCode
import biz.belcorp.library.log.BelcorpLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class KitPresenter @Inject
internal constructor(private val userUseCase: UserUseCase,
                     private val orderUseCase: OrderUseCase,
                     private val caminobrillanteUseCase: CaminoBrillanteUseCase) : Presenter<KitView> {

    private var view: KitView? = null

    private lateinit var user: User

    override fun attachView(view: KitView) {
        this.view = view
    }

    override fun resume() {}

    override fun pause() {}

    override fun destroy() {
        view?.hideLoading()
        view = null
        userUseCase.dispose()
        orderUseCase.dispose()
        caminobrillanteUseCase.dispose()
    }

    fun loadKits() {
        view?.setCanBack(false)
        view?.showLoading()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = caminobrillanteUseCase.getKitsOfertas()
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        when {
                            it.code == SearchCUVCode.OK -> {
                                it.data?.let {
                                    when (it.isEmpty()) {
                                        true -> view?.showError(view?.context()!!.getString(R.string.problemas_con_los_kits))
                                        false -> {
                                            view?.hideError()
                                            view?.showKits(it)
                                        }
                                    }
                                } ?: run {
                                    view?.showError(view?.context()!!.getString(R.string.problemas_con_los_kits))
                                }
                            }
                            else -> {
                                view?.showError(it.message
                                    ?: view?.context()!!.getString(R.string.problemas_con_los_kits))
                            }
                        }
                        view?.onKitLoaded()
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
                            } ?: run {
                                view?.showMessage(R.string.error_agregar_kit)
                            }
                        }
                    }
                }
            }
        }
    }

    fun addKit(item: KitCaminoBrillante, quantity: Int, identifier: String) {
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
            tipoEstrategiaId = item.tipoEstrategiaID
            codigoEstrategia = item.codigoEstrategia?.toInt()
            estrategiaId = item.estrategiaId
            cantidad = quantity
            this.identifier = identifier
        }

        Tracker.CaminoBrillante.onAddProduct(productCUV, quantity, GlobalConstant.LIST_KITS ,user)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = orderUseCase.insertarPedido(productCUV, identifier)
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        view?.hideLoading()
                        when {
                            it.code == SearchCUVCode.OK -> {
                                view?.onKitAdded(item, quantity)
                                view?.hideError()
                                userUseCase.updateScheduler2()
                                loadKits()
                            }
                            else -> {
                                it.message?.let { view?.showMessage(it) }
                                    ?: view?.showMessage(R.string.error_agregar_kit)
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
                            }.run {
                                view?.showMessage(R.string.error_agregar_kit)
                            }
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

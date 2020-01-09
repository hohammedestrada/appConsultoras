package biz.belcorp.consultoras.feature.ofertafinal

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.common.model.orders.OfertaFinalModelDataMapper
import biz.belcorp.consultoras.common.model.orders.OrderModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.PremioFinalAgrega
import biz.belcorp.consultoras.domain.entity.Oferta
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.interactor.*
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.AddedAlertType
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.mobile.components.design.counter.Counter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@PerActivity
class OfertaFinalPresenter @Inject
constructor(
    private val userUseCase: UserUseCase,
    private val orderUseCase: OrderUseCase,
    private val offerUseCase: OfferUseCase,
    private val origenPedidoUseCase: OrigenPedidoUseCase,
    private val accountUseCase: AccountUseCase,
    private val loginModelDataMapper: LoginModelDataMapper,
    private val ofertaFinalModelDataMapper: OfertaFinalModelDataMapper,
    private val orderModelDataMapper: OrderModelDataMapper,
    private val productUseCase: ProductUseCase,
    private val sessionUseCase: SessionUseCase,
    private val origenMarcacionUseCase: OrigenMarcacionUseCase,
    private val premioUseCase: PremioUseCase) : Presenter<OfertaFinalView>, SafeLet {

    private var view: OfertaFinalView? = null
    private var user: User? = null


    override fun attachView(view: OfertaFinalView) {
        this.view = view
    }

    override fun destroy() {
        this.view = null
        this.userUseCase.dispose()
        this.orderUseCase.dispose()
        this.origenMarcacionUseCase.dispose()
    }

    override fun pause() {
        /* Not necessary */
    }

    override fun resume() {
        /* Not necessary */
    }

    fun getMontoMeta(){
        view?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            user?.let { user ->
                user.campaing?.let { campaign ->
                    val result = premioUseCase.getMontoMeta(campaign.toInt())

                    GlobalScope.launch(Dispatchers.Main) {
                        result?.let {
                            view?.hideLoading()
                            view?.setupProgess(result)
                        }
                    }
                }
            }
        }
    }


    fun getListaPremios(){

        view?.showLoading()

        GlobalScope.launch(Dispatchers.IO) {
            user?.let { user ->
                user.campaing?.let { campaign ->
                    val result = premioUseCase.getOfertasFinales(campaign.toInt())

                    GlobalScope.launch(Dispatchers.Main) {
                        result?.let {
                            view?.hideLoading()
                            view?.setPremios(it)
                        }
                    }

                }
            }
        }
    }


    fun addPremio(premioFinalAgrega: PremioFinalAgrega){
        view?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            user?.let { user ->
                user.campaing?.let { campaign ->
                    val result = premioUseCase.getMontoMeta(campaign.toInt())
                    GlobalScope.launch(Dispatchers.Main) {
                        view?.hideLoading()
                        result?.let {
                            premioFinalAgrega.montoPedidoFinal = result.montoPedido
                            addPremioFinal(premioFinalAgrega)
                        }
                    }
                }
            }
        }
    }

    fun addPremioFinal(premioFinalAgrega: PremioFinalAgrega){
        view?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = premioUseCase.addPremio(premioFinalAgrega)
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        view?.hideLoading()
                        getListaPremios()
                    }
                }
            }catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.hideLoading()
//                    when (e) {
//                        is NetworkErrorException -> view?.showErrorScreenMessage()
//                        else -> {
//                            e.message?.let {
//                                BelcorpLogger.d(it)
//                                view?.showErrorScreenMessage()
//                            }
//                        }
//                    }
                }
            }

        }

    }

    fun getUser() {
        view?.showLoading()
        GlobalScope.launch {
            val result = userUseCase.getUser()
            var flagBuscador = false
            result?.let {
                user = it
                flagBuscador = it.isMostrarBuscador ?: false
                getMontoMeta()
            }

            GlobalScope.launch(Dispatchers.Main) {
                view?.hideLoading()
                if (flagBuscador) view?.showSearchItem()
                view?.setUser(user)

            }
        }
    }

    fun getImageEnabled() {
        GlobalScope.launch(Dispatchers.IO) {
            orderUseCase.getImageDialogEnabled().let {
                view?.setImageEnabled(it)
            }
        }
    }

    fun getOfertasRecomendadas() {
        view?.showLoading()
        GlobalScope.launch {
            user?.let { user ->
                user.campaing?.let { campaign ->
                    val result = offerUseCase.getOfertasRecomendadas(campaign)
                    GlobalScope.launch(Dispatchers.Main) {
                        result?.let {
                            view?.hideLoading()
                            view?.setOffersRecomended(it.filterNotNull())
                        }
                    }
                }
            }
        }
    }

    /**
     * Agrega una oferta recibiendo un objeto Oferta como entrada
     */
    fun agregar(oferta: Oferta, quantity: Int, counterView: Counter, identifier: String, palanca: String, codigo: String) {
        view?.showLoading()

        GlobalScope.launch (Dispatchers.IO){
            val opw = origenPedidoUseCase.getValor(palanca, codigo)
            val productCUV = getAddRequest(oferta, opw.toString(), quantity, identifier)
            productCUV?.let { producto ->
                    try {
                        val result =  orderUseCase.insertarPedido(producto, identifier)
                        result?.let {
                            GlobalScope.launch(Dispatchers.Main) {
                                if(it.code == GlobalConstant.CODE_OK || it.code == GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO) {
                                    val codeAlert = when(it.code) {
                                        GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO -> AddedAlertType.FESTIVAL
                                        else -> AddedAlertType.DEFAULT
                                    }
                                    userUseCase.updateScheduler2()
                                    view?.onOfferAdded(quantity, producto, it.message, codeAlert)
                                    counterView.resetQuantity()
                                } else {
                                    view?.hideLoading()
                                    view?.onOfferNotAdded(it.message)
                                }
                            }
                        }
                    }catch (e: Exception){
                        GlobalScope.launch(Dispatchers.Main) {
                            when (e) {
                                is NetworkErrorException -> view?.showErrorScreenMessage(OfertaFinalErrorFragment.ERROR_MESSAGE_NETWORK)
                                else -> {
                                    view?.hideLoading()
                                    e.message?.let {
                                        BelcorpLogger.d(it)
                                        view?.onOfferNotAdded(it)
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun getAddRequest(oferta: Any, origenPedidoWebCarrusel: String?, quantity: Int, id: String): ProductCUV? {
        if (oferta is Oferta)
            return ProductCUV().apply {
                cuv = oferta.cuv
                description = oferta.nombreOferta
                descripcionMarca = oferta.nombreMarca
                marcaId = oferta.marcaID
                precioCatalogo = oferta.precioCatalogo
                precioValorizado = oferta.precioValorizado
                fotoProducto = oferta.imagenURL
                origenPedidoWeb = origenPedidoWebCarrusel
                tipoEstrategiaId = oferta.tipoEstrategiaID
                codigoEstrategia = oferta.codigoEstrategia?.toInt()
                estrategiaId = oferta.estrategiaID
                indicadorMontoMinimo = oferta.indicadorMontoMinimo
                limiteVenta = oferta.limiteVenta
                cantidad = quantity
                identifier = id
                isSugerido = false
                clienteId = 0
                tipoPersonalizacion = oferta.tipoOferta
            }
        else if (oferta is ProductCUV)
            return ProductCUV().apply {
                cuv = oferta.cuv
                description = oferta.description
                descripcionMarca = oferta.descripcionMarca
                marcaId = oferta.marcaId
                precioCatalogo = oferta.precioCatalogo
                precioValorizado = oferta.precioValorizado
                fotoProducto = oferta.fotoProductoSmall
                tipoEstrategiaId = oferta.tipoEstrategiaId
                codigoEstrategia = oferta.codigoEstrategia
                estrategiaId = oferta.estrategiaId
                indicadorMontoMinimo = oferta.indicadorMontoMinimo
                limiteVenta = oferta.limiteVenta
                cantidad = quantity
                identifier = id
                isSugerido = false
                clienteId = 0
                tipoPersonalizacion = oferta.tipoPersonalizacion
            }
        return null
    }

}

package biz.belcorp.consultoras.feature.home.ganamas.armatupack

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.ArmaTuPackNoExisteProductosException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.interactor.MenuUseCase
import biz.belcorp.consultoras.domain.interactor.OfferUseCase
import biz.belcorp.consultoras.domain.interactor.OrderUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.RevistaDigitalType
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.mobile.components.design.tone.model.CategoryToneModel
import biz.belcorp.mobile.components.design.tone.model.ToneModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@PerActivity
class ArmaTuPackPresenter @Inject
constructor(private val orderUseCase: OrderUseCase,
            private val userUseCase: UserUseCase,
            private val offerUseCase: OfferUseCase,
            private val menuUseCase: MenuUseCase) : Presenter<ArmaTuPackView>, SafeLet {

    private var view: ArmaTuPackView? = null
    private var user: User? = null

    /**
     * Presenter abstract functions
     */

    override fun attachView(view: ArmaTuPackView) { this.view = view }

    override fun resume() { /* Not necessary */ }

    override fun pause() { /* Not necessary */ }

    override fun destroy() {

        this.view = null
        this.menuUseCase.dispose()
        this.offerUseCase.dispose()
        this.userUseCase.dispose()
        this.orderUseCase.dispose()

    }

    /**
     * Public functions
     */

    /**
     * Obtiene la información del usuario (para obtener la configuración)
     */

    fun getUser(){
        GlobalScope.launch {

            userUseCase.getUser()?.let {

                user = it
                GlobalScope.launch(Dispatchers.Main) { view?.setUser(it) }

            }

        }

    }

    fun getImageEnabled(){
        GlobalScope.launch{
            orderUseCase.getImageDialogEnabled()?.let{
                view?.setImageEnabled(it)
            }
        }
    }

    /**
     *
     */
    fun refreshSchedule() {
        GlobalScope.launch {
            userUseCase.updateScheduler2()
            GlobalScope.launch(Dispatchers.Main) {
                view?.goToOffers()
            }
        }
    }

    /**
     * Obtiene la configuración de palancas (para obtener el origen pedido web)
     */

    fun getConfiguracion() {

        user?.let {

            view?.showLoading()

            GlobalScope.launch(Dispatchers.IO) {

                val time = measureTimeMillis {

                    try {
                        it.campaing?.let { campaing ->

                            var result: List<ConfiguracionPorPalanca?>? = null
                            val time = measureTimeMillis {
                                result = offerUseCase.configuracion(it.diaFacturacion, campaing.toInt(),
                                    it.consecutivoNueva, it.codigoPrograma, it.regionCode,
                                    it.zoneCode, it.zoneID?.toInt(), it.countryMoneySymbol, it.isRDEsSuscrita, it.isRDEsActiva)
                            }

                            BelcorpLogger.i("Time x servicio transcurrido $time")

                            result?.let {

                                var flagATP: Boolean? = null

                                it.forEach { item ->

                                    if(item?.tipoOferta== OfferTypes.ATP) {

                                        val orderResponse = orderUseCase.getOrders()
                                        flagATP = orderResponse?.isTieneArmaTuPack

                                    }

                                }

                                GlobalScope.launch(Dispatchers.Main) { view?.setConfig(it, flagATP) }

                            }

                        }

                    } catch (e: Exception) {

                        GlobalScope.launch(Dispatchers.Main) {

                            when (e) {
                                // Añadir un mensaje cuando viene por esta excepcion
                                is NetworkErrorException -> view?.showErrorScreenMessage(ArmaTuPackErrorFragment.ERROR_MESSAGE_NETWORK)
                                is IOException -> view?.showErrorScreenMessage(ArmaTuPackErrorFragment.ERROR_MESSAGE_NETWORK)
                                else -> view?.showErrorScreenMessage(ArmaTuPackErrorFragment.ERROR_MESSAGE_NETWORK)
                            }

                            view?.hideLoading()

                            e.message?.let { BelcorpLogger.d(it) }

                        }

                    }
                }

                BelcorpLogger.i("Time x config transcurrido $time")
            }
        }
    }

    /**
     * Obtiene los productos por grupo de Arma Tu Pack, parecido a la Ficha
     */
    fun getProductPack(typeLever: String) {

        view?.showLoading()

        GlobalScope.launch(Dispatchers.IO) {

            try {

                if (user?.revistaDigitalSuscripcion != RevistaDigitalType.CON_RD_SUSCRITA_ACTIVA) {
                    throw ArmaTuPackNoExisteProductosException()
                }

                val result: Oferta? = offerUseCase.getOfertaAtp()
                result?.tipoOferta = typeLever
                result?.let {

                    val groupProduct = transformToCategoryToneList(it.componentes)
                    val groupHistory = generateHistoryAtp(it.componentes, it.opcionesAgregadas)

                    GlobalScope.launch(Dispatchers.Main) {

                        view?.hideLoading()
                        view?.showArmaTuPack(it, groupProduct, groupHistory)

                    }

                }

            } catch (e: Exception) {

                BelcorpLogger.d(e)

                GlobalScope.launch(Dispatchers.Main) {

                    when (e) {
                        is NetworkErrorException -> view?.showErrorScreenMessage(ArmaTuPackErrorFragment.ERROR_MESSAGE_ARMA_TU_PACK)
                        is ArmaTuPackNoExisteProductosException -> view?.showErrorScreenMessage(ArmaTuPackErrorFragment.ERROR_MESSAGE_EMPTY_ARMA_TU_PACK)
                        else -> view?.showErrorScreenMessage(ArmaTuPackErrorFragment.ERROR_MESSAGE_ARMA_TU_PACK)
                    }

                    view?.hideLoading()

                }

            }

        }

    }

    /**
     * Agrega una oferta desde Arma Tu Pack
     */
    fun agregar(oferta: Oferta, productSelected: List<ToneModel>, quantity: Int, identifier: String, origenPedidoWebAtp: String, isUpdate: Boolean, typeLever: String) {

        view?.showLoading()

        GlobalScope.launch(Dispatchers.IO) {

            try {
                val productCUV = ProductCUV().apply {

                    cuv = oferta.cuv
                    description = oferta.nombreOferta
                    descripcionMarca = oferta.nombreMarca
                    marcaId = oferta.marcaID
                    precioCatalogo = oferta.precioCatalogo
                    precioValorizado = oferta.precioValorizado
                    fotoProducto = oferta.imagenURL
                    origenPedidoWeb = origenPedidoWebAtp
                    tipoEstrategiaId = oferta.tipoEstrategiaID
                    codigoEstrategia = oferta.codigoEstrategia?.toInt()
                    estrategiaId = oferta.estrategiaID
                    indicadorMontoMinimo = oferta.indicadorMontoMinimo
                    limiteVenta = oferta.limiteVenta
                    cantidad = quantity
                    listaOpciones = ProductCUVOpcion.transformList(transformProductAtpSelected(oferta, productSelected.toList()))
                    this.identifier = identifier
                }

                val result =  orderUseCase.insertarPedido(productCUV, identifier)
                result?.let {

                    if (it.code == GlobalConstant.CODE_OK) {

                        userUseCase.updateScheduler2()
                        val ofertaResult: Oferta? = offerUseCase.getOfertaAtp()
                        ofertaResult?.tipoOferta = typeLever
                        ofertaResult?.let {

                            val groupProduct = transformToCategoryToneList(it.componentes)
                            val groupHistory = generateHistoryAtp(it.componentes, it.opcionesAgregadas)

                            GlobalScope.launch(Dispatchers.Main) {

                                view?.showArmaTuPack(it, groupProduct, groupHistory)
                                view?.hideLoading()

                                 
                                if (isUpdate) view?.onOfferUpdated(productCUV, it.message)
                                else view?.onOfferAdded(quantity, productCUV, it.message)
                            }
                        }

                    } else {

                        GlobalScope.launch(Dispatchers.Main) {
                            view?.hideLoading()
                            view?.onOfferNotAdded(it.message)
                        }

                    }
                }

            } catch (e: Exception) {

                BelcorpLogger.d(e)

                GlobalScope.launch(Dispatchers.Main) {

                    view?.hideLoading()

                    when(e) {

                        is NetworkErrorException -> view?.showErrorScreenMessage(ArmaTuPackErrorFragment.ERROR_MESSAGE_ARMA_TU_PACK)
                        is ArmaTuPackNoExisteProductosException -> view?.showErrorScreenMessage(ArmaTuPackErrorFragment.ERROR_MESSAGE_EMPTY_ARMA_TU_PACK)
                        else -> {
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

    /**
     * Helper functions
     */

    private fun transformToCategoryToneList(list: List<Componente?>?) : ArrayList<CategoryToneModel> {

        return arrayListOf<CategoryToneModel>().apply {

            list?.forEach {

                it?.let { it1 ->

                    safeLet(it1.grupo, it1.opciones, it1.factorCuadre) { _, _, _ -> transformToCategoryTone(it1)?.let { it2 -> add(it2) } }

                }

            }

        }

    }

    private fun transformToCategoryTone(item: Componente) : CategoryToneModel? {

        return CategoryToneModel(
            key = item.grupo.toString(),
            name = if (item.factorCuadre!! > 1) item.descripcionPlural ?: "" else item.descripcionSingular ?: "",
            list = transformToToneList(item.opciones, item.grupo.toString()),
            listSelected = arrayListOf(),
            factorQuadrant = item.factorCuadre!!
        )

    }

    private fun transformToToneList(list: List<Opciones?>?, group: String): ArrayList<ToneModel> {

        return arrayListOf<ToneModel>().apply {

            list?.forEach {

                it?.let { it1 ->

                    safeLet(it1.cuv, it1.nombreOpcion) { _, _ -> transformToTone(it1, group)?.let { it2 -> add(it2) } }

                }

            }

        }

    }

    private fun transformToTone(item: Opciones, group: String): ToneModel? {

        return ToneModel(
            key = item.cuv!!,
            name = item.nombreOpcion!!,
            urlImage = item.imagenURL,
            quantity = item.cantidad,
            keyGroup = group
        )

    }

    private fun generateHistoryAtp(componente: List<Componente?>?, list: List<OpcionesAgregadas?>?) : ArrayList<ToneModel> {

        return arrayListOf<ToneModel>().apply {

            list?.forEach { opt ->

                opt?.cantidad?.let { cantidad ->

                    val opcion: Opciones? = componente?.firstOrNull { it?.grupo.toString() == opt.grupo }?.opciones?.firstOrNull { it?.cuv == opt.cuv }

                    for (i in 1..cantidad) {

                        safeLet(opcion, opt.grupo) { o, g -> transformToTone(o, g)?.let { it2 -> add(it2) } }

                    }

                }

            }

        }

    }

    /**
     * Tranforma los componentes de acuerdo a los productos seleccionados en Arma tu Pack
     */
    private fun transformProductAtpSelected(oferta: Oferta, productSelected: List<ToneModel>) : List<Componente?> {

        return arrayListOf<Componente?>().apply {

            val groupProduct = productSelected.groupBy { it.keyGroup }

            for ((keyGroup, product) in groupProduct) {

                val componente : Componente? = oferta.componentes?.firstOrNull { it?.grupo.toString() == keyGroup }
                val tono = product.groupingBy { it.key }.eachCount()
                val opciones = arrayListOf<Opciones>().apply {

                    for ((key, quantity) in tono) {

                        componente?.opciones?.firstOrNull { it?.cuv == key }?.apply {
                            this.cantidad = quantity
                        }?.let { add(it) }

                    }

                }

                if (opciones.isNotEmpty()) {

                    componente?.opciones = opciones
                    add(componente)

                }

            }

        }

    }

}

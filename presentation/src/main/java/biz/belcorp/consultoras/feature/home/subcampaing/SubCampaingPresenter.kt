package biz.belcorp.consultoras.feature.home.subcampaing

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.GanaMasNoConfigException
import biz.belcorp.consultoras.domain.exception.GanaMasNoOffersByCategoriesException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.consultoras.domain.interactor.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.feature.home.fest.FestErrorFragment
import biz.belcorp.consultoras.feature.home.ganamas.fragments.GanaMasErrorFragment
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.RevistaDigitalType
import biz.belcorp.consultoras.util.anotation.SearchOriginType
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.StringUtil
import kotlinx.coroutines.*
import java.io.IOException
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@PerActivity
class SubCampaingPresenter @Inject
constructor(private val origenPedidoUseCase: OrigenPedidoUseCase,
            private val orderUseCase: OrderUseCase,
            private val userUseCase: UserUseCase,
            private val offerUseCase: OfferUseCase,
            private val festivalUseCase: FestivalUseCase,
            private val menuUseCase: MenuUseCase) : Presenter<SubCampaingView>, SafeLet {

    private var view: SubCampaingView? = null
    private var user: User? = null

    private val ORDERTYPE_ASC = "ASC"
    private val ORDERTYPE_DESC= "DESC"
    private val ORDER_DEFECTO = "ORDEN"
    private val ORDER_GANANCIA = "GANANCIA"
    private val ORDER_PRECIO = "PRECIO"

    val LAYOUT_ORDER = 1
    val LAYOUT_FILTER = 2

    /**
     * Presenter abstract functions
     */

    override fun attachView(view: SubCampaingView) {
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
     * Obtiene la información del usuario (para obtener la configuración)
     */

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

    fun refreshSchedule() {
        GlobalScope.launch(Dispatchers.IO) {
            userUseCase.updateScheduler2()
            GlobalScope.launch(Dispatchers.Main) {
                view?.goToOffers()
            }
        }
    }

    fun getOrders() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = offerUseCase.ordenamientos()
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        //ganaMasView?.hideLoading()
                        if (it.isNotEmpty()){
                            view?.setOrdenamientos(it)
                        }
                    }
                }
            } catch (e: Exception){
                GlobalScope.launch(Dispatchers.Main) {
                    view?.setFilterOrderLabel(false,LAYOUT_ORDER)
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
                valorOrigen = origenPedidoUseCase.getValor(OfferTypes.SR, SearchOriginType.ORIGEN_LANDING_SUBCAMPANIA ).toString()
                item.apply {
                    origenPedidoWeb = valorOrigen
                }
                val result =  orderUseCase.insertarPedido(item, identifier)
                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        if(it.code == GlobalConstant.CODE_OK){
                            userUseCase.updateScheduler2()
                            view?.onProductAdded(item, it.message)
                        } else {
                            view?.onAddProductError(it.message, null)
                        }
                        view?.hideLoading()
                    }
                }
            }catch (e: Exception){
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


    fun getConfiguracion(internetConexion: Boolean){
        user?.let {
            view?.showLoading()
            GlobalScope.launch(Dispatchers.IO) {
                val time = measureTimeMillis {
                    try {
                        it.campaing?.let { campaing ->
                            var result: List<ConfiguracionPorPalanca?>? = null

                            val time = measureTimeMillis {

                                result = offerUseCase.configuracion(it.diaFacturacion, campaing.toInt(), it.consecutivoNueva, it.codigoPrograma, it.regionCode, it.zoneCode, it.zoneID?.toInt(), it.countryMoneySymbol, it.isRDEsSuscrita, it.isRDEsActiva)
                            }

                            BelcorpLogger.i("Time x servicio transcurrido $time")

                            result?.let {

                                var flagATP: Boolean? = false
                                if(internetConexion == true){
                                    it.forEach { item -> if(item?.tipoOferta== OfferTypes.ATP) {
                                        val orderResponse = orderUseCase.getOrders()
                                        flagATP = orderResponse?.isTieneArmaTuPack
                                    }
                                    }
                                }
                                GlobalScope.launch(Dispatchers.Main) {
                                    view?.setConfig(it, flagATP)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        GlobalScope.launch(Dispatchers.Main) {
                            when (e) {
                                // Añadir un mensaje cuando viene por esta excepcion
                                is GanaMasNoConfigException -> view?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_EMPTY_CONTAINER)
                                is NetworkErrorException -> view?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                                is IOException -> view?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                                else -> view?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
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


    fun getOffersByLever(config: ConfiguracionPorPalanca, typeLever: String) {
        view?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {

            try {
                val request = OfertaRequest().apply {
                    campaniaId = user?.campaing
                    zonaId = user?.zoneID?.toInt()
                    codigoZona = user?.zoneCode
                    codigoRegion = user?.regionCode
                    esSuscrita = user?.revistaDigitalSuscripcion == RevistaDigitalType.CON_RD_SUSCRITA_ACTIVA
                    esActiva = user?.isRDEsActiva
                    tieneMG = user?.isTieneMG
                    diaInicio = user?.diaFacturacion
                    fechaInicioFacturacion = user?.billingStartDate
                    flagComponentes = true
                    flagSubCampania = false
                    flagIndividual = true
                }


                val result=offerUseCase.ofertasXPalanca(config, request)


                result?.let {
                    GlobalScope.launch(Dispatchers.Main) {
                        result.filterNotNull().forEach { offer -> offer.tipoOferta = config.tipoOferta }
                        view?.hideLoading()
                        view?.onOffersByLeverResponse(it, typeLever)
                    }
                }
            } catch (e: Exception){
                GlobalScope.launch(Dispatchers.Main){
                    when(e){
                        // Añadir un mensaje cuando viene por esta excepcion
                        is GanaMasNoOffersByCategoriesException -> view?.showErrorScreenMessage(SubCampaingErrorFragment.ERROR_MESSAGE_EMPTY_SUBCAMPAIGN)
                        is NetworkErrorException -> view?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                        else -> view?.showErrorScreenMessage(GanaMasErrorFragment.ERROR_MESSAGE_NETWORK)
                    }
                    view?.hideLoading()
                    e.message?.let { BelcorpLogger.d(it) }
                }
            }
        }
    }


    fun showErroFragment(){
        view?.showErrorScreenMessage(FestErrorFragment.ERROR_MESSAGE_EMPTY_FEST_SUBCAMPAING)
    }

    fun setOrderOffer(resultResponse: List<Oferta?>?, order:String, orderType:String):List<Oferta?>?{
        //var result:List<Oferta?>? =resultResponse
        if(  !(orderType.equals(ORDERTYPE_ASC) && order.equals(ORDER_DEFECTO))  ){
            when(orderType){
                ORDERTYPE_ASC->{
                    return resultResponse?.
                        sortedWith(compareBy{
                            when(order){
                                ORDER_GANANCIA->{
                                    it?.ganancia
                                }
                                ORDER_PRECIO->{
                                    it?.precioCatalogo
                                }
                                else->{
                                    it?.nombreOferta
                                }
                            }
                        })
                }
                ORDERTYPE_DESC->{
                    return resultResponse?.
                        sortedWith(compareByDescending{
                            when(order){
                                ORDER_GANANCIA->{
                                    it?.ganancia
                                }
                                ORDER_PRECIO->{
                                    it?.precioCatalogo
                                }
                                else->{
                                    it?.nombreOferta
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

    fun getTitleSubcampaign() {

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = offerUseCase.configuracion()
                val configSubcampaign = result.firstOrNull {config -> config?.tipoOferta == OfferTypes.SR }?.subCampaniaConfiguracion
                var title = StringUtil.Empty
                configSubcampaign?.let { config ->
                    config.bannerTextoTitulo?.let { configTitle ->
                        if(configTitle.isNotEmpty())
                            title = configTitle
                    }
                }

                GlobalScope.launch (Dispatchers.Main) {
                    view?.setTitleSubcampaign(title)
                }

            } catch (e: Exception) {}

        }

    }

}

package biz.belcorp.consultoras.feature.caminobrillante.feature.home

import android.text.Spanned
import android.view.View
import android.widget.CheckBox
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.caminobrillante.*
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.consultoras.domain.interactor.*
import biz.belcorp.consultoras.feature.caminobrillante.CaminoBrillanteActivity
import biz.belcorp.consultoras.feature.caminobrillante.feature.home.viewmodels.BarraMontoAcumuladoViewModel
import biz.belcorp.consultoras.util.anotation.*
import biz.belcorp.consultoras.util.toHtml
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.StringUtil
import biz.belcorp.mobile.components.charts.wizard.Step
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.offers.Multi
import biz.belcorp.mobile.components.offers.model.OfferModel
import kotlinx.coroutines.*
import java.text.DecimalFormat
import javax.inject.Inject

@PerActivity
class CaminoBrillantePresenter @Inject
internal constructor(private val caminobrillanteUseCase: CaminoBrillanteUseCase,
                     private val userUseCase: UserUseCase,
                     private val accountUseCase: AccountUseCase,
                     private val orderUseCase: OrderUseCase,
                     private val menuUseCase: MenuUseCase) : Presenter<CaminoBrillanteView>, SafeLet {

    private var view: CaminoBrillanteView? = null

    //constants
    val FLAG_ACTIVO = "1"
    var FLAG_CERO = 0
    val FLAG_OLD_VALUE_TITLE = "{0}"
    val FLAG_OLD_VALUE_MESSAGE = "{1}"

    //vars
    private var nivelActualSeleccionado: String = ""

    //configs
    private var user: User? = null
    private var decimalFormat = DecimalFormat()
    private var decimalFormatWithoutDecimal = DecimalFormat()
    private var moneySymbol: String = ""
    private var nombreNivelGranBrillante: String = ""
    private var puntajeGranBrillante: Int = 0

    //entitys
    private var resumenConsultora: NivelConsultoraCaminoBrillante? = null
    private var niveles: List<NivelCaminoBrillante>? = null
    private var ofertas: List<Carousel.OfertaCarousel>? = null
    private var historicoConsultora: List<NivelConsultoraCaminoBrillante>? = null
    private var nivelSiguiente: NivelCaminoBrillante? = null
    private var nivelActual: NivelCaminoBrillante? = null
    private var pedidos: List<LogroCaminoBrillante.Indicador.Medalla>? = null

    //flags
    private var isAvaibleBarraMontoAcumulado = false
    private var isAvaibleKitsYDemostradores = false
    private var isAvaibleGanancias = false
    private var isAvaibleEnterateMas = false
    private var cantidadDelay: Long = 500

    private var isCargoCarruselCorrectamente = true
    private var isBeneficiosExpanded = false
    private var isTieneOfertas = false
    private var isTieneGanancias = false

    //views
    private var wizardStepAnterior: Step? = null

    //Animations
    private var onBoardingAnim = false
    private var onGananciaAnin = false
    private var onCambioNivelAnim = false
    private var onMontoIncentivo: Double? = null

    fun init() {
        GlobalScope.launch(Dispatchers.IO) {
            GlobalScope.launch(Dispatchers.Main) {
                view?.showLoading()
                view?.setCanBack(false)
            }

            user = userUseCase.getUser()
            user?.let {

                decimalFormat = CountryUtil.getDecimalFormatByISO(it.countryISO, true)
                decimalFormatWithoutDecimal = CountryUtil.getDecimalFormatByISO(it.countryISO, false)
                moneySymbol = it.countryMoneySymbol ?: ""

                val configuracion = accountUseCase.getConfig(UserConfigAccountCode.CAMINO_BRILLANTE)

                isAvaibleBarraMontoAcumulado = configuracion.filter { c -> c.code == CaminoBrillanteType.ENABLED_BARRA_MONTO }.map { c -> FLAG_ACTIVO == c.value1 ?: "" }.firstOrNull()
                    ?: false
                isAvaibleKitsYDemostradores = configuracion.filter { c -> c.code == CaminoBrillanteType.ENABLED_CARRUSEL }.map { c -> FLAG_ACTIVO == c.value1 ?: "" }.firstOrNull()
                    ?: false
                isAvaibleGanancias = configuracion.filter { c -> c.code == CaminoBrillanteType.ENABLED_GANANCIA }.map { c -> FLAG_ACTIVO == c.value1 ?: "" }.firstOrNull()
                    ?: false
                isAvaibleEnterateMas = configuracion.filter { c -> c.code == CaminoBrillanteType.ENABLED_ENTERATE_MAS }.map { c -> FLAG_ACTIVO == c.value1 ?: "" }.firstOrNull()
                    ?: false
                /****************************************************/

                onBoardingAnim = configuracion.filter { c -> c.code == CaminoBrillanteType.FLAG_ONBOARDING }.map { c -> FLAG_ACTIVO == c.value1 ?: false }.firstOrNull()
                    ?: false

                onGananciaAnin = configuracion.filter { c -> c.code == CaminoBrillanteType.FLAG_DEDO_GANANCIA }.map { c -> FLAG_ACTIVO == c.value1 ?: false }.firstOrNull()
                    ?: false

                onCambioNivelAnim = configuracion.filter { c -> c.code == CaminoBrillanteType.FLAG_CAMBIO_NIVEL_ANIM }.map { c -> FLAG_ACTIVO == c.value1 ?: false }.firstOrNull()
                    ?: false

                onMontoIncentivo = configuracion.firstOrNull { c -> c.code == CaminoBrillanteType.FLAG_CB_MONTO_INCENTIVO }?.value1?.toDoubleOrNull()

                /****************************************************/

                puntajeGranBrillante = configuracion.firstOrNull { c -> c.code == CaminoBrillanteType.APP_GRAN_BRILALNTE }?.value1?.toIntOrNull()
                    ?: 0

                nombreNivelGranBrillante = configuracion.firstOrNull { c -> c.code == CaminoBrillanteType.APP_GRAN_BRILALNTE }?.value2
                    ?: ""

                niveles = caminobrillanteUseCase.getNivelesCaminoBrillante()

                resumenConsultora = caminobrillanteUseCase.getResumenConsultora()

                if (niveles == null || niveles!!.isEmpty() || resumenConsultora == null) {
                    view?.onErrorLoadCaminoBrillante()
                } else {

                    if (onBoardingAnim) {
                        view?.goToOnBoarding()

                        delay(cantidadDelay)
                    }

                    async {
                        loadBarraNivelesYBeneficios()
                        loadBarraMontoAcumulado()
                        loadCarrusel()
                        loadLogros()
                        loadGanancias()
                    }.await()

                    if (!onBoardingAnim && onCambioNivelAnim) {
                        showAnimationLevel()
                    } else {
                        withContext(Dispatchers.Main) {
                            view?.showCaminoBrillante()
                            view?.hideLoading()
                            view?.setCanBack(true)
                        }
                    }
                }
            } ?: GlobalScope.launch(Dispatchers.Main) { view?.onErrorLoadCaminoBrillante() }
        }
    }

    fun loadBarraNivelesYBeneficios() {
        safeLet(niveles, resumenConsultora) { niveles, resumenConsultora ->
            nivelActualSeleccionado = resumenConsultora.nivel ?: ""

            GlobalScope.launch(Dispatchers.Main) {
                view?.onLoadBarraNiveles(niveles, resumenConsultora)
                view?.showBarraNiveles()
            }
            loadBeneficios(resumenConsultora.nivel ?: "0", false)
        }
    }

    fun loadBeneficios(codigoNivel: String?, showAll: Boolean) {
        val nivel = niveles?.filter { it.codigoNivel == codigoNivel }?.first()

        GlobalScope.launch(Dispatchers.Main) {
            view?.onLoadBeneficios(nivel, showAll)
            view?.showBeneficios()
        }
    }

    suspend fun loadBarraMontoAcumulado() {
        if (isAvaibleBarraMontoAcumulado) {
            nivelActual = caminobrillanteUseCase.getNivelActualCaminoBrillante()
            nivelSiguiente = caminobrillanteUseCase.getNivelSiguienteCaminoBrillante()
            pedidos = caminobrillanteUseCase.getPedidosPeriodoActual()

            nivelActual?.let { nivelActual ->
                val barraMontoViewModel = BarraMontoAcumuladoViewModel()

                var showMoney = true
                var showAcumulaste = false

                var nombreSiguienteNivel = nivelSiguiente?.descripcionNivel ?: ""
                val nombreNivelActual = nivelActual.descripcionNivel ?: ""

                var montoMinimoNivelSiguiente = if (ConsultoraNivelCode.NIVEL_5 != nivelActual.codigoNivel) nivelSiguiente?.montoMinimo
                    ?: 0.0 else ((nivelActual.montoMinimo ?: 0.0) * 1.10)

                val montoFaltante = if (ConsultoraNivelCode.NIVEL_5 != nivelActual.codigoNivel) nivelSiguiente?.montoFaltante?.toDouble()
                    ?: 0.0 else nivelActual.montoFaltante?.toDouble() ?: 0.0

                var montoMinimoNivelActual = nivelActual.montoMinimo ?: 0.0

                var montoAcumulado = if (ConsultoraNivelCode.NIVEL_5 != nivelActual.codigoNivel) montoMinimoNivelSiguiente.minus(montoFaltante) else nivelActual.montoMinimo?.minus(montoFaltante)
                    ?: 0.0

                barraMontoViewModel.textAccumulatedAmount = formatWithMoneySymbol(montoAcumulado)

                when (nivelActual.codigoNivel) {
                    ConsultoraNivelCode.NIVEL_1 -> {
                        barraMontoViewModel.isVisibleText1 = false
                        barraMontoViewModel.visibilityIcon1 = View.GONE
                        barraMontoViewModel.isVisibleIndicador = false
                    }
                    ConsultoraNivelCode.NIVEL_5 -> {
                        if (montoAcumulado >= montoMinimoNivelActual) {
                            barraMontoViewModel.isVisibleIndicador = false
                            barraMontoViewModel.isVisibleAccumulatedAmount = true
                            barraMontoViewModel.visibilityIcon1 = View.GONE
                            barraMontoViewModel.visibilityIcon2 = View.GONE
                            barraMontoViewModel.isVisibleText2 = true
                            barraMontoViewModel.isVisibleText1 = false
                            barraMontoViewModel.text2Alignment = View.TEXT_ALIGNMENT_CENTER

                            montoAcumulado = nivelSiguiente?.puntajeAcumulado?.toDouble() ?: 0.0
                            montoMinimoNivelSiguiente = nivelSiguiente?.puntaje?.toDouble() ?: 0.0

                            barraMontoViewModel.textAccumulatedAmount = "${formatPts(montoAcumulado.toInt())} ${view?.getTextByIdRes(R.string.pts)}"

                            showMoney = false
                            showAcumulaste = true
                        } else {
                            barraMontoViewModel.isVisibleAmountNextLevel = false
                            barraMontoViewModel.visibilityIcon2 = View.GONE
                            barraMontoViewModel.isVisibleText2 = false
                        }
                    }
                    ConsultoraNivelCode.NIVEL_6 -> {
                        barraMontoViewModel.isVisibleIndicador = true
                        barraMontoViewModel.isVisibleAccumulatedAmount = true
                        barraMontoViewModel.isVisibleAmountNextLevel = false
                        barraMontoViewModel.visibilityIcon1 = View.VISIBLE
                        barraMontoViewModel.visibilityIcon2 = View.INVISIBLE

                        nombreSiguienteNivel = nombreNivelGranBrillante

                        montoAcumulado = nivelActual.puntajeAcumulado?.toDouble() ?: 0.0

                        barraMontoViewModel.textAccumulatedAmount = "${formatPts(montoAcumulado.toInt())} ${view?.getTextByIdRes(R.string.pts)}"
                        barraMontoViewModel.text1 = "${view?.getTextByIdRes(R.string.mantienes_tu_nivel)} $nombreNivelActual ${view?.getTextByIdRes(R.string.con)} ${formatPts(nivelActual.puntaje
                            ?: 0)} ${view?.getTextByIdRes(R.string.pts)}"

                        montoMinimoNivelActual = nivelActual.puntaje?.toDouble() ?: 0.0
                        montoMinimoNivelSiguiente = puntajeGranBrillante.toDouble()

                        showMoney = false
                        showAcumulaste = true
                    }
                }

                val montoAcumuladoEnPorcentaje = (montoAcumulado / montoMinimoNivelSiguiente) * 100
                val montoMinimoEnPorcentaje = (montoMinimoNivelActual / montoMinimoNivelSiguiente) * 100

                barraMontoViewModel.textNameNextLevel = nombreSiguienteNivel
                barraMontoViewModel.textAmountNextLevel = if (showMoney) {
                    formatWithMoneySymbol(montoMinimoNivelSiguiente)
                } else {
                    if (ConsultoraNivelCode.NIVEL_6 == nivelActual.codigoNivel) {
                        "${formatPts(puntajeGranBrillante)} ${view?.getTextByIdRes(R.string.pts)}"
                    } else {
                        "${formatPts(nivelSiguiente?.puntaje
                            ?: 0)} ${view?.getTextByIdRes(R.string.pts)}"
                    }
                }

                barraMontoViewModel.montoAcumuladoPorcentaje = montoAcumuladoEnPorcentaje
                barraMontoViewModel.montoMinimoPorcentaje = montoMinimoEnPorcentaje
                barraMontoViewModel.idResColorIndicador = getColorNivelById(nivelActual.codigoNivel
                    ?: "0")

                if (showAcumulaste) {
                    barraMontoViewModel.text2 = nivelActual.mensaje ?: ""
                } else {
                    barraMontoViewModel.text1 = "${view?.getTextByIdRes(R.string.mantienes_tu_nivel)} $nombreNivelActual ${view?.getTextByIdRes(R.string.con)} ${formatWithMoneySymbol(nivelActual.montoMinimo
                        ?: 0.0)}"
                    barraMontoViewModel.text2 = "${view?.getTextByIdRes(R.string.te_falta)} ${formatWithMoneySymbol(nivelSiguiente?.montoFaltante?.toDouble()
                        ?: 0.toDouble())} ${view?.getTextByIdRes(R.string.para_cambiar_a)} <b>${view?.getTextByIdRes(R.string.nivel)} $nombreSiguienteNivel<b>"
                }

                val sumaPedidosPasados = pedidos?.filter { it.isEstado }?.size ?: 0
                val totalPedidos = pedidos?.size ?: 0

                val campaniActualCompleta = user?.campaing //Ejm: 201915
                var campaniaActual = ""
                if (campaniActualCompleta?.length == 6) {
                    campaniaActual = "C${campaniActualCompleta.substring(4)}" // Ejm: C15
                }
                val indexCampaniaActual = pedidos?.indexOfFirst { it.valor == campaniaActual } ?: 0

                val campaniasRestantes = totalPedidos - indexCampaniaActual

                barraMontoViewModel.text3 = "${view?.getTextByIdRes(R.string.mensaje_pedidos_campanias_landing, sumaPedidosPasados.toString(), totalPedidos.toString(), campaniasRestantes.toString())}"

                barraMontoViewModel.text1SizeDp = view?.getDimensionByIdRes(R.dimen.textsize_barra_monto)
                    ?: 0f
                barraMontoViewModel.text2SizeDp = view?.getDimensionByIdRes(R.dimen.textsize_barra_monto)
                    ?: 0f

                GlobalScope.launch(Dispatchers.Main) {
                    view?.onLoadBarraMontoAcumulado(barraMontoViewModel)
                    view?.showBarraMonto(isAvaibleBarraMontoAcumulado)
                }
            }
        } else {
            GlobalScope.launch(Dispatchers.Main) {
                view?.hideBarraMonto()
            }
        }
    }

    suspend fun loadCarrusel() {
        isTieneOfertas = niveles?.filter { it.codigoNivel == resumenConsultora?.nivel }?.firstOrNull()?.isTieneOfertasEspeciales
            ?: false

        if (isTieneOfertas) {
            if (isAvaibleKitsYDemostradores) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.showOffers(isTieneOfertas, isAvaibleKitsYDemostradores, true, isCargoCarruselCorrectamente)
                }
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        caminobrillanteUseCase.getOfertasCarousel()?.let {
                            GlobalScope.launch(Dispatchers.Main) {
                                when {
                                    it.code == SearchCUVCode.OK -> {
                                        it.data?.items?.let { items ->
                                            ofertas = items
                                            if (items.isNotEmpty()) {
                                                GlobalScope.launch(Dispatchers.Main) {
                                                    view?.onLoadOfertasCarousel(it.data?.verMas
                                                        ?: false, ArrayList(mapearOfertasCarousel(items)))
                                                }
                                            } else {
                                                view?.hideOffers()
                                            }
                                        } ?: run {
                                            view?.hideOffers()
                                        }
                                    }
                                    else -> {
                                        isCargoCarruselCorrectamente = false
                                        view?.hideOffers()
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        GlobalScope.launch(Dispatchers.Main) {
                            isCargoCarruselCorrectamente = false
                            view?.hideOffers()
                        }
                    }
                }
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.showOffers(isTieneOfertas, isAvaibleKitsYDemostradores, false, false)
                }
            }
        }
    }

    fun reloadCarrusel() {
        isCargoCarruselCorrectamente = true
        runBlocking {
            loadCarrusel()
        }
    }

    fun loadLogros() {
        GlobalScope.launch(Dispatchers.Main) {
            view?.showLogros()
        }
    }

    fun onClickItemCarousel(keyItem: String, marcaID: Int, marca: String, pos: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            view?.onCarouselItemClick(keyItem, marcaID, marca, OrderOriginCode.CAMINO_BRILLANTE_HOME_OFERTAS_ESPECIALES_FICHA)
        }
    }

    fun addOffer(keyItem: String, quantity: Int, pos: Int, counter: Counter, multi: Multi?, identifier: String) {
        GlobalScope.launch(Dispatchers.Main) { view?.showLoading() }

        val oferta = ofertas?.get(pos)

        oferta?.let { item ->

            val productCUV = ProductCUV().apply {
                cuv = keyItem
                description = item.descripcionCortaCUV ?: item.descripcionCUV
                descripcionMarca = item.descripcionMarca
                marcaId = item.marcaID
                precioCatalogo = item.precioCatalogo
                precioValorizado = item.precioValorizado
                fotoProducto = item.fotoProductoSmall
                origenPedidoWeb = OrderOriginCode.CAMINO_BRILLANTE_HOME_OFERTAS_ESPECIALES_CARRUSEL
                tipoEstrategiaId = if (item.tipoEstrategiaID == null) null else item.tipoEstrategiaID.toString()
                codigoEstrategia = item.codigoEstrategia?.toIntOrNull() ?: 0
                estrategiaId = item.estrategiaID ?: 0
                cantidad = quantity
                this.identifier = identifier
            }

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val result = orderUseCase.insertarPedido(productCUV, identifier)
                    result?.let {
                        GlobalScope.launch(Dispatchers.Main) {
                            when {
                                it.code == SearchCUVCode.OK -> {
                                    onOfferAdded(item, counter, quantity, multi)
                                    userUseCase.updateScheduler2()
                                }
                                else -> {
                                    if (item.tipoOferta == CaminoBrillanteActivity.TIPO_OFERTA_KIT) {
                                        multi?.setEnabledMulti(false)
                                        multi?.setImageTag(getTagNivel(item.codigoNivel
                                            ?: "", false))
                                    }
                                    it.message?.let { view?.showError(it) }
                                        ?: view?.showError(R.string.error_agregar_kit)
                                }
                            }
                            view?.hideLoading()
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
                                    view?.showError(it)
                                }
                            }
                        }
                        view?.hideLoading()
                    }
                }
            }
        }
    }

    suspend fun loadGanancias() {
        if (isAvaibleGanancias) {

            val configuracion = accountUseCase.getConfig(UserConfigAccountCode.CAMINO_BRILLANTE)

            historicoConsultora = caminobrillanteUseCase.getNivelesHistoricoCaminoBrillante()

            isTieneGanancias = historicoConsultora?.any { it.montoPedido?.toFloat() ?: 0f != 0f }
                ?: false

            onGananciaAnin = configuracion.filter { c -> c.code == CaminoBrillanteType.FLAG_DEDO_GANANCIA }.map { c -> FLAG_ACTIVO == c.value1 ?: false }.firstOrNull()
                ?: false

            if (isTieneGanancias) {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.onLoadGanancias(historicoConsultora!!)
                    view?.showGanancias(isAvaibleGanancias, onGananciaAnin)

                }
            }
        } else {
            GlobalScope.launch(Dispatchers.Main) {
                view?.hideGanancias()
            }
        }
    }

    fun loadEnterateMas() {
        val nivel = niveles?.filter { it.codigoNivel == nivelActualSeleccionado }?.first()

        nivel?.let {
            when (it.enterateMas) {
                EnterateMasType.MIACADEMIA -> view?.goToAcademia(it.enterateMasParam)
                EnterateMasType.ISSUU -> {
                    val url = "${BeneficioType.URL_ISSUU}${it.enterateMasParam}${"?mode=embed"}"
                    view?.goToIssuu(url)
                }
                else -> {
                }
            }
        }
    }

    fun onSelectedNivel(wizardStep: Step?, nivelSeleccionado: NivelCaminoBrillante) {
        val nivel = niveles?.filter { it.codigoNivel == resumenConsultora?.nivel }?.firstOrNull()
        nivel?.let { nivelActual ->
            //Sí el nivel seleccionado no es el mismo al seleccionado actualmente
            if (nivelSeleccionado.codigoNivel != nivelActualSeleccionado) {
                //Sí el nivel seleccionado es mayor al nivel actual de la consultora
                if (nivelSeleccionado.codigoNivel?.toIntOrNull() ?: 0 >= nivelActual.codigoNivel?.toIntOrNull() ?: 0) {

                    updateImageNivel(nivelSeleccionado, wizardStep)
                    nivelActualSeleccionado = nivelSeleccionado.codigoNivel ?: ""

                    val showAllBenefits: Boolean
                    val isNivelActualConsultora: Boolean

                    //Si el nivel seleccionado es el nivel actual de la consultora
                    if (nivelSeleccionado.codigoNivel == nivelActual.codigoNivel) {
                        view?.showBarraMonto(isAvaibleBarraMontoAcumulado)
                        view?.showLogros()

                        view?.showOffers(isTieneOfertas, isAvaibleKitsYDemostradores, false, isCargoCarruselCorrectamente)
                        view?.showGananciasAfter(isTieneGanancias)
                        view?.hideNivelesSuperiores()

                        showAllBenefits = false
                        isNivelActualConsultora = true

                        isBeneficiosExpanded = false
                    } else {
                        val text1: String
                        var text2: String? = null

                        //Si el nivel seleccionado es el siguiente al actual
                        if (((nivelSeleccionado.codigoNivel?.toIntOrNull()
                                ?: 0).minus(1)) == nivelActual.codigoNivel?.toIntOrNull() ?: 0) {
                            //Si el nivel seleccionado NO es brillante
                            if (nivelSeleccionado.codigoNivel != ConsultoraNivelCode.NIVEL_6) {
                                text1 = "${view?.getTextByIdRes(R.string.nivel)} ${nivelSeleccionado.descripcionNivel} ${formatWithMoneySymbol(nivelSeleccionado.montoMinimo
                                    ?: 0.0)}"
                                text2 = "${view?.getTextByIdRes(R.string.te_falta)} ${formatWithMoneySymbol(nivelSeleccionado.montoFaltante?.toDouble()
                                    ?: 0.0)} ${view?.getTextByIdRes(R.string.para_cambiar_de_nivel_a)} ${nivelSeleccionado.descripcionNivel}"
                            } else {
                                text1 = "${view?.getTextByIdRes(R.string.nivel)} ${nivelSeleccionado.descripcionNivel} ${formatPts(nivelSeleccionado.puntaje
                                    ?: 0)} ${view?.getTextByIdRes(R.string.pts)}"
                                text2 = "${view?.getTextByIdRes(R.string.acumulaste)} ${formatPts(nivelSeleccionado.puntajeAcumulado
                                    ?: 0)} ${view?.getTextByIdRes(R.string.pts)}"
                            }
                        } else {
                            //Si el nivel seleccionado NO es brillante
                            if (nivelSeleccionado.codigoNivel != ConsultoraNivelCode.NIVEL_6) {
                                text1 = "${view?.getTextByIdRes(R.string.nivel)} ${nivelSeleccionado.descripcionNivel} ${formatWithMoneySymbol(nivelSeleccionado.montoMinimo
                                    ?: 0.0)}"
                            } else {
                                text1 = "${view?.getTextByIdRes(R.string.nivel)} ${nivelSeleccionado.descripcionNivel} ${formatPts(nivelSeleccionado.puntaje
                                    ?: 0)} ${view?.getTextByIdRes(R.string.pts)}"
                                text2 = "${view?.getTextByIdRes(R.string.acumulaste)} ${formatPts(nivelSeleccionado.puntajeAcumulado
                                    ?: 0)} ${view?.getTextByIdRes(R.string.pts)}"
                            }
                        }

                        view?.showNivelesSuperiores(isAvaibleEnterateMas, getColorFondoNivelById(nivelSeleccionado.codigoNivel
                            ?: ""), text1, text2)
                        view?.hideGanancias()
                        view?.hideLogros()
                        view?.hideOffers()
                        view?.hideBarraMonto()

                        showAllBenefits = true
                        isNivelActualConsultora = false
                        isBeneficiosExpanded = true
                    }

                    view?.onSelectNivel(isNivelActualConsultora)
                    loadBeneficios(nivelSeleccionado.codigoNivel ?: "", showAllBenefits)
                }
            }
        }
    }

    private fun updateImageNivel(nuevoNivel: NivelCaminoBrillante?, wizardStepNuevoNivel: Step?) {
        view?.updateImageStep(nivelActualSeleccionado == resumenConsultora?.nivel, getIconNivelById(nivelActualSeleccionado, false), wizardStepAnterior, getIconNivelById(nuevoNivel?.codigoNivel
            ?: "", true), wizardStepNuevoNivel)
        wizardStepAnterior = wizardStepNuevoNivel
    }

    fun onBindMulti(multiItem: Multi, position: Int) {
        multiItem.setPaddingContainerLabelButton(null, null, null, 3F)

        val oferta = ofertas?.get(position)

        when (oferta?.tipoOferta) {
            CaminoBrillanteActivity.TIPO_OFERTA_DEMOSTRADOR -> GlobalScope.launch(Dispatchers.Main) { view?.onBindMultiDemostrador(oferta, multiItem) }
            CaminoBrillanteActivity.TIPO_OFERTA_KIT -> GlobalScope.launch(Dispatchers.Main) {
                view?.onBindMultiKit(oferta, multiItem, oferta.codigoNivel ?: "")
            }
        }
    }

    private fun onOfferAdded(oferta: Carousel.OfertaCarousel?, counter: Counter, quantity: Int, multi: Multi?) {
        when (oferta?.tipoOferta) {
            CaminoBrillanteActivity.TIPO_OFERTA_DEMOSTRADOR -> {
                val message = if (quantity > 1) R.string.demostradores_agregado_con_exito else R.string.demostrador_agregado_con_exito
                GlobalScope.launch(Dispatchers.Main) {
                    view?.onDemostradorAdded(counter)
                    view?.showBottomDialog(message, oferta.getFotoProducto(), R.color.product_added_success)
                }
            }
            CaminoBrillanteActivity.TIPO_OFERTA_KIT -> {
                GlobalScope.launch(Dispatchers.Main) {
                    view?.onKitAdded(multi, oferta.codigoNivel ?: "")
                    view?.showBottomDialog(R.string.kit_agregado_con_exito, oferta.getFotoProducto(), R.color.product_added_success)
                }
            }
        }
        view?.updateOffersCount(quantity)
    }

    fun formatWithMoneySymbol(precio: Double): String {
        return "$moneySymbol ${format(precio)}"
    }

    fun format(precio: Double): String {
        return decimalFormat.format(precio.toBigDecimal())
    }

    fun formatPts(pts: Int): String {
        return decimalFormatWithoutDecimal.format(pts)
    }

    private fun mapearOfertasCarousel(list: List<Carousel.OfertaCarousel>?): List<OfferModel> {
        return mutableListOf<OfferModel>().apply {
            list?.forEach {
                add(OfferModel(
                    it.CUV ?: "",
                    it.descripcionMarca ?: "",
                    it.descripcionCortaCUV ?: it.descripcionCUV ?: "",
                    formatWithMoneySymbol(
                        if (it.tipoOferta == CaminoBrillanteActivity.TIPO_OFERTA_DEMOSTRADOR) {
                            it.precioValorizado ?: 0.0
                        } else {
                            it.precioCatalogo ?: 0.0
                        }
                    ),
                    formatWithMoneySymbol(
                        if (it.tipoOferta == CaminoBrillanteActivity.TIPO_OFERTA_DEMOSTRADOR) {
                            it.precioCatalogo ?: 0.0
                        } else {
                            it.ganancia ?: 0.0
                        }
                    ),
                    it.fotoProductoSmall ?: it.fotoProductoMedium ?: "",
                    "",
                    false
                ))
            }
        }.toList()
    }

    fun getIconNivelById(idNivel: String, enable: Boolean): Int =
        when (idNivel) {
            ConsultoraNivelCode.NIVEL_1 -> R.drawable.ic_nivel_consultora
            ConsultoraNivelCode.NIVEL_2 -> if (enable) R.drawable.ic_nivel_coral else R.drawable.ic_coral_color_deshabilitado
            ConsultoraNivelCode.NIVEL_3 -> if (enable) R.drawable.ic_nivel_ambar else R.drawable.ic_ambar_color_deshabilitado
            ConsultoraNivelCode.NIVEL_4 -> if (enable) R.drawable.ic_nivel_perla else R.drawable.ic_perla_color_deshabilitado
            ConsultoraNivelCode.NIVEL_5 -> if (enable) R.drawable.ic_nivel_topacio else R.drawable.ic_topacio_color_deshabilitado
            ConsultoraNivelCode.NIVEL_6 -> if (enable) R.drawable.ic_nivel_brillante else R.drawable.ic_brillante_color_deshabilitado
            else -> R.drawable.ic_nivel_consultora
        }

    fun getColorNivelById(idNivel: String): Int =
        when (idNivel) {
            ConsultoraNivelCode.NIVEL_2 -> R.color.coral
            ConsultoraNivelCode.NIVEL_3 -> R.color.ambar
            ConsultoraNivelCode.NIVEL_4 -> R.color.perla
            ConsultoraNivelCode.NIVEL_5 -> R.color.topacio
            ConsultoraNivelCode.NIVEL_6 -> R.color.brillante
            else -> R.color.level_coral
        }

    fun getColorFondoNivelById(idNivel: String): Int =
        when (idNivel) {
            ConsultoraNivelCode.NIVEL_2 -> R.color.level_coral
            ConsultoraNivelCode.NIVEL_3 -> R.color.level_ambar
            ConsultoraNivelCode.NIVEL_4 -> R.color.level_perla
            ConsultoraNivelCode.NIVEL_5 -> R.color.level_topacio
            ConsultoraNivelCode.NIVEL_6 -> R.color.level_brillante
            else -> R.color.level_coral
        }

    fun getTagNivel(codigoNivel: String?, isHabilitado: Boolean): Int =
        when (codigoNivel) {
            ConsultoraNivelCode.NIVEL_2 -> if (isHabilitado) R.drawable.tag_coral_enabled else R.drawable.tag_coral_disabled
            ConsultoraNivelCode.NIVEL_3 -> if (isHabilitado) R.drawable.tag_ambar_enabled else R.drawable.tag_ambar_disabled
            ConsultoraNivelCode.NIVEL_4 -> if (isHabilitado) R.drawable.tag_perla_enabled else R.drawable.tag_perla_disabled
            ConsultoraNivelCode.NIVEL_5 -> if (isHabilitado) R.drawable.tag_topacio_enabled else R.drawable.tag_topacio_disabled
            else -> if (isHabilitado) R.drawable.tag_brillante_enabled else R.drawable.tag_brillante_disabled
        }

    fun autoSelectNivelActual() {
        val nivel = niveles?.filter { it.codigoNivel == resumenConsultora?.nivel }?.firstOrNull()
        nivel?.let {
            onSelectedNivel(null, it)
        }
    }

    override fun attachView(view: CaminoBrillanteView) {
        this.view = view
    }

    override fun resume() {}

    override fun pause() {}

    override fun destroy() {
        userUseCase.dispose()
        caminobrillanteUseCase.dispose()
        view = null
    }

    fun onClickBtnSeeLessMore() {
        if (isBeneficiosExpanded) {
            isBeneficiosExpanded = false
            GlobalScope.launch(Dispatchers.Main) {
                view?.collapseBeneficios()
            }
        } else {
            isBeneficiosExpanded = true
            GlobalScope.launch(Dispatchers.Main) {
                view?.expandBeneficios()
            }
        }
    }

    fun getMenuActive2(code1: String, code2: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                menuUseCase.getActive2(code1, code2)?.let { view?.onGetMenu(it) }
            } catch (e: Exception) {
                e.message?.let { BelcorpLogger.d(it) }
            }
        }
    }

    fun saveConfirmacion(chbxConfirmacion: CheckBox) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val isChecked = chbxConfirmacion.isChecked

                caminobrillanteUseCase.updateAnimFlag(
                    AnimRequest(CaminoBrillanteType.FLAG_ONBOARDING, "1", if (isChecked) "0" else "1")
                ).let {
                    if (isChecked) {
                        accountUseCase.saveConfig(UserConfigAccountCode.CAMINO_BRILLANTE, CaminoBrillanteType.FLAG_ONBOARDING, "0", if (it.data == true) "1" else "0")
                    }
                }
            } catch (e: Exception) {
                e.message?.let { BelcorpLogger.d(it) }
            }
        }
    }

    fun saveFinger() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                caminobrillanteUseCase.updateAnimFlag(
                    AnimRequest(CaminoBrillanteType.FLAG_DEDO_GANANCIA, "1", "0")
                ).let {
                    if (it.data == true) {
                        accountUseCase.saveConfig(UserConfigAccountCode.CAMINO_BRILLANTE, CaminoBrillanteType.FLAG_DEDO_GANANCIA, "0", if (it.data == true) "1" else "0")
                    }
                }
            } catch (e: Exception) {
                e.message?.let { BelcorpLogger.d(it) }
            }
        }
    }

    fun onConfirmDialog() {
        GlobalScope.launch {
            try {
                if (onCambioNivelAnim) {
                    showAnimationLevel()
                }
            } catch (e: Exception) {
                e.message?.let { BelcorpLogger.d(it) }
            }
        }

    }

    private suspend fun showAnimationLevel() {
        withContext(Dispatchers.IO) {
            val indCambioNivel = resumenConsultora?.indCambioNivel?.toIntOrNull()

            val idNivelCampanaAnterior = caminobrillanteUseCase.getNivelCamapanaAnterior()

            val idNivelAlQueCambia = resumenConsultora?.indCambioNivel?.toIntOrNull()?.let { idNivelCampanaAnterior?.plus(it) }

            val nivelAlQueCambia = niveles?.firstOrNull { it.codigoNivel?.toIntOrNull() == idNivelAlQueCambia }

            val showConfeti = indCambioNivel ?: -1 >= 0

            safeLet(indCambioNivel, nivelAlQueCambia) { indCambioNivel, nivelAlQueCambia ->
                CoroutineScope(Dispatchers.Main).launch {
                    val mensaje = resumenConsultora?.mensajeCambioNivel ?: StringUtil.Empty
                    val arrayMensaje = mensaje.split("|")

                    val titulo = if (arrayMensaje.isNotEmpty()) arrayMensaje[0] else StringUtil.Empty
                    val descripcion = if (arrayMensaje.size > 1) arrayMensaje[1] else StringUtil.Empty

                    val drawable: Int
                    val title: Spanned
                    val message: Spanned

                    when {
                        //Mantuvo su nivel
                        indCambioNivel == FLAG_CERO -> {
                            drawable = R.drawable.ic_confeti
                            title = titulo.replace(FLAG_OLD_VALUE_TITLE, user?.primerNombre ?: StringUtil.Empty).toHtml()
                            message = descripcion.replace(FLAG_OLD_VALUE_MESSAGE, nivelAlQueCambia.descripcionNivel ?: StringUtil.Empty).toHtml()
                        }
                        //Subio de nivel
                        indCambioNivel > FLAG_CERO -> {
                            drawable = getIconNivelById(nivelAlQueCambia.codigoNivel ?: StringUtil.Empty, true)
                            title = titulo.replace(FLAG_OLD_VALUE_TITLE, user?.primerNombre ?: StringUtil.Empty).toHtml()
                            message = descripcion.replace(FLAG_OLD_VALUE_MESSAGE, nivelAlQueCambia.descripcionNivel ?: StringUtil.Empty).toHtml()
                        }
                        //Bajo de nivel
                        else -> {
                            drawable = R.drawable.ic_pulgar_abajo
                            title = titulo.toHtml()
                            message = descripcion.toHtml()
                        }
                    }

                    view?.showAnimationChangeLevel(showConfeti, drawable, title, message)

                    delay(500)

                    view?.showCaminoBrillante()
                    view?.hideLoading()
                    view?.setCanBack(true)
                }
                CoroutineScope(Dispatchers.IO).launch {
                    caminobrillanteUseCase.updateAnimFlag(AnimRequest(CaminoBrillanteType.FLAG_CAMBIO_NIVEL_ANIM, "1", "0")).let {
                        if (it.data == true) {
                            accountUseCase.saveConfig(UserConfigAccountCode.CAMINO_BRILLANTE, CaminoBrillanteType.FLAG_CAMBIO_NIVEL_ANIM, "0", "1")
                        }
                    }
                }
            } ?: run {
                withContext(Dispatchers.Main) {
                    view?.showCaminoBrillante()
                    view?.hideLoading()
                    view?.setCanBack(true)
                }
            }
        }
    }

}

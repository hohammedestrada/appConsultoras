package biz.belcorp.consultoras.feature.caminobrillante.feature.home

import android.text.Spanned
import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.caminobrillante.Carousel
import biz.belcorp.consultoras.domain.entity.caminobrillante.NivelCaminoBrillante
import biz.belcorp.consultoras.domain.entity.caminobrillante.NivelConsultoraCaminoBrillante
import biz.belcorp.consultoras.feature.caminobrillante.feature.home.viewmodels.BarraMontoAcumuladoViewModel
import biz.belcorp.mobile.components.charts.wizard.Step
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.offers.Multi
import biz.belcorp.mobile.components.offers.model.OfferModel

interface CaminoBrillanteView : View, LoadingView {

    fun onLoadBarraNiveles(niveles: List<NivelCaminoBrillante>, resumenConsultora: NivelConsultoraCaminoBrillante)
    fun onLoadBarraMontoAcumulado(bma: BarraMontoAcumuladoViewModel)
    fun onLoadBeneficios(nivel: NivelCaminoBrillante?, showAll: Boolean)
    fun onLoadOfertasCarousel(verMas: Boolean, offers: ArrayList<OfferModel>)
    fun onLoadGanancias(historicoConsultora: List<NivelConsultoraCaminoBrillante>)

    fun onSelectNivel(isNivelActualConsultora: Boolean)

    fun goToIssuu(url: String)
    fun goToAcademia(enterateMasParam: String?)

    fun showCaminoBrillante()
    fun hideCaminoBrillante()
    fun showBarraNiveles()
    fun hideBarraNiveles()
    fun showBarraMonto(isAvaibleBarraMonto: Boolean)
    fun hideBarraMonto()
    fun showNivelesSuperiores(isAvaibleEnterateMas: Boolean, resIdColor: Int, text1: String, text2: String?)
    fun hideNivelesSuperiores()
    fun showBeneficios()
    fun hideBeneficios()


    fun showOffers(isTieneOfertas: Boolean, isAvaibleKitsYDemostradores: Boolean, showLoading: Boolean, isCargoAnteriormente: Boolean)
    fun hideOffers()
    fun showLogros()
    fun hideLogros()

    fun showGanancias(isAvaibleGanancias: Boolean, onGananciaAnin: Boolean)
    fun showGananciasAfter(isAvaibleGanancias: Boolean)

    fun hideGanancias()

    fun onErrorLoadCaminoBrillante()

    fun onBindMultiDemostrador(oferta: Carousel.OfertaCarousel, multi: Multi)
    fun onBindMultiKit(oferta: Carousel.OfertaCarousel, multi: Multi, codigoNivel: String)

    fun onDemostradorAdded(counter: Counter)
    fun onKitAdded(multi: Multi?, codigoNivel: String)

    fun updateOffersCount(count: Int)

    fun showMessage(resIdMessage: Int)
    fun showMessage(message: String)
    fun showError(resIdMessage: Int)
    fun showError(message: String)
    fun showBottomDialog(messageRes: Int, imageLink: String?, messageColorRes: Int)

    fun showAnimationChangeLevel(showConfeti: Boolean, icono: Int, title: Spanned, mensaje: Spanned)

    fun getTextByIdRes(id: Int, vararg params: String): String
    fun updateImageStep(isNivelAnteriorActual: Boolean, resIdAnterior: Int, wizardStepAnterior: Step?, resIdNuevo: Int, wizardStepNuevo: Step?)
    fun onCarouselItemClick(keyItem: String, marcaID: Int, marca: String, origenPedidoWeb: String)
    fun collapseBeneficios()
    fun expandBeneficios()

    fun onGetMenu(menu: Menu)
    fun getDimensionByIdRes(id: Int): Float
    fun goToOnBoarding()
    fun showConfirmDialog()
    fun hideFinder()
    fun setCanBack(canBack: Boolean)

}

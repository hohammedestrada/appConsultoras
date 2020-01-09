package biz.belcorp.consultoras.feature.ofertafinal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.domain.PremioFinalAgrega
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.ofertafinal.FichaOfertaFinalActivity
import biz.belcorp.consultoras.feature.home.ganamas.adapters.OffersGridItemListener
import biz.belcorp.consultoras.feature.ofertafinal.adapter.PremioFinalAdapter
import biz.belcorp.consultoras.feature.ofertafinal.di.OfertaFinalComponent
import biz.belcorp.consultoras.feature.search.results.SearchResultsGridAdapter
import biz.belcorp.consultoras.util.ImageUtils
import biz.belcorp.consultoras.util.anotation.AddedAlertType
import biz.belcorp.consultoras.util.anotation.OffersOriginType
import biz.belcorp.consultoras.util.anotation.PremioEstadoType
import biz.belcorp.consultoras.util.anotation.SearchOriginType.ORIGEN_LANDING_OFERTA_FINAL
import biz.belcorp.consultoras.util.decorations.GridCenterSpacingDecoration
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.library.util.StringUtil
import biz.belcorp.mobile.components.core.helpers.ImagesHelper
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import biz.belcorp.mobile.components.offers.Offer
import biz.belcorp.mobile.components.offers.PremioSimple
import biz.belcorp.mobile.components.offers.model.OfferModel
import kotlinx.android.synthetic.main.fragment_oferta_final_landing.*
import java.text.DecimalFormat
import javax.inject.Inject

class OfertaFinalLandingFragment : BaseFragment(), OfertaFinalView, OffersGridItemListener, SafeLet {

    @Inject
    lateinit var presenter: OfertaFinalPresenter

    private var listener: Listener? = null
    private var ofertas = mutableListOf<Oferta>()
    private lateinit var gridAdapter: SearchResultsGridAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var imageHelper: ImagesHelper

    private lateinit var premioFinalAdapter: PremioFinalAdapter

    private var user: User? = null
    private var decimalFormat = DecimalFormat()
    private var moneySymbol: String = StringUtil.Empty
    private var imageDialog: Boolean = false

    private var listaPremios: List<PremioFinal?>?=null
    private var valMontoInicial:Double=0.0
    private var valMontoFinal:Double=0.0

    var premioMeta:PremioFinalMeta?=null

    var premioEstado = PremioEstadoType.BLOCKED

    var flagAlcanzoMeta = false

    // Overrides Fragment
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            this.listener = context
        }
    }

    override fun onInjectView(): Boolean {
        getComponent(OfertaFinalComponent::class.java).inject(this)
        return true
    }

    override fun context(): Context {
        return context?.let { it } ?: throw NullPointerException("No hay contexto")
    }

    // Overrides Fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_oferta_final_landing, container, false)
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        init()
    }


    private fun init() {
        presenter.getUser()
        presenter.getImageEnabled()
        activity?.let { imageHelper = ImagesHelper(it) }
        setupCarouselPremios()
        setupGridOfertas()
    }


    override fun setPremios(premios: List<PremioFinal?>?) {

        rvw_premios.visibility=View.GONE
        premio_simple.visibility=View.GONE

        if(premios?.size == 1){
            if(flagAlcanzoMeta){
                if(getPremioEstado(premios) == PremioEstadoType.ALLOWED){
                    premios?.get(0)?.let { presenter.addPremio(setPremioAgrega(it))}
                }
            }
        }

        premios?.let{listapremios->

            premioEstado = getPremioEstado(listapremios)
            listener?.updatePremioEstado(premioEstado)

            listaPremios= listapremios
            if(listapremios.count()>1){

                tvw_premios_title.setText(STR_PREMIOS)
                rvw_premios.visibility=View.VISIBLE
                premioFinalAdapter.updateData(listapremios)

            }else{
                listapremios.first()?.let {premio->

                    tvw_premios_title.setText(STR_PREMIO)

                    premio_simple.updateIconImage(R.drawable.ic_candado_of)
                    premio_simple.showIconImage(!premio.habilitado!!)
                    premio_simple.setValues(premio.nombre!!, premio.imagen!!)
                    premio_simple.visibility=View.VISIBLE
                    premio_simple.premioListener= object: PremioSimple.OnPremioListener {
                        override fun onClick(view: View) {


                            val extras = Bundle()

                            extras.putString(FichaOfertaFinalActivity.EXTRA_PREMIO_IMG, premio.imagen!!)
                            extras.putString(FichaOfertaFinalActivity.EXTRA_PREMIO_NOMBRE, premio.nombre!!)
                            extras.putString(FichaOfertaFinalActivity.EXTRA_PREMIO_DETALLE, premio.descripcion!!)
                            listener?.goToFichaPremio(extras)

                        }
                    }
                }
            }
        }
    }

    private fun getPremioEstado(lista: List<PremioFinal?>?): Int {
        premioEstado = PremioEstadoType.BLOCKED
        lista?.forEach {
            if(it?.habilitado == true)
                premioEstado = PremioEstadoType.ALLOWED
        }

        lista?.forEach {
            if(it?.seleccionado == true)
                premioEstado = PremioEstadoType.CHOOSED
        }
        return premioEstado
    }


    fun showDialogSelectPremio(premio:PremioFinalAgrega){

        context?.let {
            BottomDialog.Builder(it)
                .setTitle(R.string.oferta_final_selecciona_premio_dialog_title)
                .setContent(getString(R.string.oferta_final_selecciona_premio_dialog_message))
                .setIcon(R.drawable.ic_mano_error)
                .setPositiveText(R.string.button_aceptar)
                .setPositiveBackgroundColor(R.color.magenta)
                .onPositive(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        presenter.addPremio(premio)

                    }
                })
                .setNegativeText(R.string.button_cancelar)
                .setNegativeTextColor(R.color.black)
                .setNegativeBorderColor(R.color.black)
                .setNegativeBackgroundColor(R.color.white)
                .onNegative(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()

                    }
                })
                .show()
        }

    }

    override fun onNotAdded(message: String?) {
        message?.let {
            context?.let { context ->
                BottomDialog.Builder(context)
                    .setIcon(R.drawable.ic_mano_error)
                    .setContent(it)
                    .setNeutralText(getString(R.string.msj_entendido))
                    .onNeutral(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog) {
                            dialog.dismiss()
                        }
                    })
                    .setNeutralBackgroundColor(R.color.magenta)
                    .show()
            }
        }
    }


    fun setPremioAgrega(premio:PremioFinal):PremioFinalAgrega{

        val pr = PremioFinalAgrega()
        premioMeta?.let {

            pr.tipoRango=it.tipoRango
            pr.montoPedido=valMontoInicial
            pr.montoPedidoFinal=0.0
            pr.gapMinimo=it.gapMinimo
            pr.gapMaximo=it.gapMaximo
            pr.gapAgregar=it.gapAgregar
            pr.montoMeta=it.montoMeta

            pr.cuv=premio.cuv
            pr.campaniaId=user?.campaing?.toInt()
            pr.upSellingDetalleId=premio.upSellingDetalleId
        }
        return pr
    }



    override fun setupProgess(premioFinalMeta: PremioFinalMeta) {

        premioFinalMeta?.let { premio ->

            val valMax = premio.montoMeta ?: 0.0

            llt_progress.visibility = View.VISIBLE

            if (premio.tipoRango != null){
                updateProgess(valMax, premio.montoPedido)
            } else {
                updateProgess(1.0, 1.0)
            }

            premioMeta=premioFinalMeta
            presenter.getListaPremios()
        }

    }

    private fun updateProgess(maximo: Double?, montoPedido: Double?) {

        val diferencia = maximo?.minus(montoPedido ?: 0.0)

        diferencia?.let { df ->

            if (df <= 0.0) {
                flagAlcanzoMeta = true
                progress.setTextTitulo = resources.getString(R.string.oferta_final_alcanzaste_premio)
            } else {
                flagAlcanzoMeta = false
                progress.setTextTitulo = "Te falta " + formatWithMoneySymbol(diferencia.toString()) + " para llevarte el premio"
            }

            progress.setProgressRound = true
            progress.valorMaximo = maximo?.toInt()
            progress.progress = montoPedido?.toInt() ?: 0
        }
    }


    private fun setupCarouselPremios(){

        premioFinalAdapter = PremioFinalAdapter( object : PremioFinalAdapter.OnItemClickListener{
            override fun onClick(premio: PremioFinal, v: View) {

                val extras = Bundle()

                extras.putString(FichaOfertaFinalActivity.EXTRA_PREMIO_IMG, premio.imagen!!)
                extras.putString(FichaOfertaFinalActivity.EXTRA_PREMIO_NOMBRE, premio.nombre!!)
                extras.putString(FichaOfertaFinalActivity.EXTRA_PREMIO_DETALLE, premio.descripcion!!)
                listener?.goToFichaPremio(extras)

            }
            override fun onClickAdd(premio: PremioFinal) {
//                listaPremios?.let {lista->
//                    lista.filter{ it1-> it1?.seleccionado==true}?.firstOrNull()?.let {
//                        showDialogSelectPremio(setPremioAgrega(premio))
//                    }?:run {
//                        presenter.addPremio(setPremioAgrega(premio))
//                    }
//                }
                presenter.addPremio(setPremioAgrega(premio))
            }
        })

        rvw_premios.setHasFixedSize(true)
        rvw_premios.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvw_premios.adapter = premioFinalAdapter
    }

    private fun setupGridOfertas() {
        ofertas = arrayListOf()
        gridLayoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        rvw_offers.layoutManager = gridLayoutManager
        rvw_offers.setHasFixedSize(true)
        val spacingGrid = resources.getDimensionPixelSize(R.dimen.offer_grid_recomended_spacing)
        rvw_offers.addItemDecoration(GridCenterSpacingDecoration(2, spacingGrid, 0))

        val hideViewABTest = context()?.let { SessionManager.getInstance(it) }.getHideViewsGridGanaMas()
            ?: false
        gridAdapter = SearchResultsGridAdapter(hideViewABTest)
        gridAdapter.listenerOffer = this
        gridAdapter.itemPlaceholder = resources.getDrawable(R.drawable.ic_container_placeholder)
        rvw_offers.adapter = gridAdapter
    }


    override fun setOffersRecomended(offers: List<Oferta>) {
        this.ofertas.clear()
        this.ofertas.addAll(offers)
        if (offers.isEmpty())
            showOfertasRecomendadas(false)
        else {
            showOfertasRecomendadas(true)
            rvw_offers.visibility = View.VISIBLE
            gridAdapter.updateData(transformListOferta(offers))
            gridLayoutManager.scrollToPositionWithOffset(0, 0)
        }
    }

    override fun setUser(user: User?) {
        user?.let { user ->
            this.user = user
            this.decimalFormat = CountryUtil.getDecimalFormatByISO(user.countryISO, true)
            this.moneySymbol = user.countryMoneySymbol?.let { it } ?: ""
            listener?.setScreenTitle("$TITLE${user.campaing?.substring(4)}")
            presenter.getOfertasRecomendadas()
        }
    }

    override fun onPrintAddProduct(item: OfferModel, quantity: Int) { /* Empty*/ }

    override fun onPrintClickProduct(item: OfferModel, position: Int) { /* Empty*/ }

    override fun onPrintProduct(item: OfferModel, position: Int) { /* Empty*/ }

    override fun pressedItem(keyItem: String, marcaID: Int, marca: String, position: Int) {
        showItemSelected(keyItem, marcaID, marca, position)
    }

    private fun showItemSelected(keyItem: String, marcaID: Int, marca: String, position: Int) {

        activity?.let { mContext ->
            if (!NetworkUtil.isThereInternetConnection(context())) {
                showNetworkError()
            } else {
                val oferta = ofertas.first { it.cuv == keyItem }

                val extras = Bundle()
                extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, oferta.cuv)
                extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, oferta.tipoOferta)
                marcaID.let { extras.putInt(BaseFichaActivity.EXTRA_MARCA_ID, marcaID) }
                extras.putString(BaseFichaActivity.EXTRA_MARCA_NAME, marca)
                extras.putInt(BaseFichaActivity.EXTRA_ACCESS_FROM, BaseFichaActivity.ACCESS_FROM_LANDING_OFERTA_FINAL)
                extras.putString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM, ORIGEN_LANDING_OFERTA_FINAL)
                extras.putString(BaseFichaActivity.EXTRA_TYPE_PERSONALIZATION, oferta.tipoPersonalizacion)


                listener?.goToFicha(extras)
            }
        }

    }


    override fun pressedItemButtonAdd(keyItem: String, quantity: Int, counterView: Counter) {
        if (quantity == 0) {
            context?.let {
                BottomDialog.Builder(it)
                    .setTitle(R.string.error_title)
                    .setContent(getString(R.string.error_zero_quantity))
                    .setNeutralText(getString(R.string.msj_entendido))
                    .onNeutral(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog) {
                            dialog.dismiss()
                        }
                    })
                    .setNeutralBackgroundColor(R.color.magenta)
                    .show()
            }
        } else {
            if (NetworkUtil.isThereInternetConnection(context())) {
                val oferta = ofertas.first { it.cuv == keyItem }
                presenter.agregar(oferta, quantity, counterView, DeviceUtil.getId(activity), TYPE, OffersOriginType.ORIGEN_LANDING )
            } else {
                showNetworkError()
            }
        }
    }

    override fun pressedItemButtonSelection(keyItem: String, marcaID: Int, marca: String, position: Int) {
        showItemSelected(keyItem, marcaID, marca, position)

    }

    override fun showOfertasRecomendadas(flag: Boolean) {
        if (flag) {
            vw_divider.visibility = View.VISIBLE
            tvw_ofertas_title.visibility = View.VISIBLE
        } else {
            vw_divider.visibility = View.GONE
            tvw_ofertas_title.visibility = View.GONE
        }
    }

    override fun showLoading() {
        listener?.showLoading()
    }

    override fun hideLoading() {
        listener?.hideLoading()
    }

    override fun onOfferAdded(quantity: Int, productCUV: ProductCUV, message: String?, code: Int) {
        updateCart(quantity)
        listener?.updateAdded()

        if (code == AddedAlertType.DEFAULT) {
            val messageDialog = message ?: getString(R.string.msj_offer_added_default)
            val image = ImageUtils.verifiedImageUrl(productCUV)

            presenter.getMontoMeta()

            context?.let {
                val colorText = ContextCompat.getColor(it, R.color.lograste_puntaje)
                val url = if (imageDialog) image else null
                showBottomDialog(it, messageDialog, url, colorText)
            }
        }

        if (code == AddedAlertType.FESTIVAL) {
            context?.let {
                showBottomDialogAction(it, message) {
                    goToFest()
                }
            }
        }
    }

    fun updateProgress() {
        presenter.getMontoMeta()
    }

    override fun onOfferNotAdded(message: String?) {
        message?.let {
            context?.let { context ->
                BottomDialog.Builder(context)
                    .setIcon(R.drawable.ic_mano_error)
                    .setContent(it)
                    .setNeutralText(getString(R.string.msj_entendido))
                    .onNeutral(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog) {
                            dialog.dismiss()
                        }
                    })
                    .setNeutralBackgroundColor(R.color.magenta)
                    .show()
            }
        }

    }

    override fun showSearchItem() {
        listener?.showSearchItem()
    }

    override fun showErrorScreenMessage(type: Int) {
        listener?.showErrorScreen(type)

    }

    override fun setImageEnabled(imageDialog: Boolean) {
        this.imageDialog = imageDialog

    }

    private fun transformListOferta(offers: List<Oferta>): List<OfferModel> {
        val list = arrayListOf<OfferModel>()

        for (offer in offers) {

            var showAmount = true
            if (offer?.tipoOferta == OfferTypes.HV) {
                showAmount = false
            }

            safeLet(offer?.cuv, offer?.tipoOferta, offer?.nombreMarca
                ?: "", offer?.nombreOferta, offer?.precioValorizado,
                offer?.precioCatalogo, offer?.imagenURL, offer?.configuracionOferta?.imgFondoApp
                ?: "", offer?.configuracionOferta?.colorTextoApp ?: "", offer?.marcaID
                ?: 0) { cuv, tipoOferta, nombreMarca, nombreOferta, precioValorizado, precioCatalogo, imagenURL,
                        imagenFondo, colorTexto, marcaID ->

                list.add(Offer.transform(cuv, nombreMarca, nombreOferta,
                    formatWithMoneySymbol(precioCatalogo.toString()),
                    formatWithMoneySymbol(precioValorizado.toString()),
                    imagenURL, tipoOferta, offer?.flagEligeOpcion ?: false, marcaID,
                    imageHelper.getResolutionURL(imagenFondo), colorTexto, showAmount, null, null, offer?.agotado, flagFestival = offer?.flagFestival))
            }
        }

        return list
    }


    private fun formatWithMoneySymbol(precio: String): String {
        return "$moneySymbol ${decimalFormat.format(precio.toBigDecimal())}"
    }

    private fun updateCart(quantity: Int) {
        activity?.let {
            val ordersCount = SessionManager.getInstance(it).getOrdersCount() ?: 0
            SessionManager.getInstance(it).saveOffersCount(ordersCount + quantity)

            val countIntent = Intent(BaseFichaActivity.BROADCAST_COUNT_ACTION)
            it.sendBroadcast(countIntent)
        }
    }


    private fun goToFest() {
        activity?.let { mContext ->
            if (!NetworkUtil.isThereInternetConnection(context())) {
                showNetworkError()
            } else {
                listener?.goToFest(Bundle())
            }
        }
    }

    companion object {
        val TAG: String = OfertaFinalLandingFragment::class.java.simpleName
        const val TITLE = "PREMIO C"
        const val TYPE = "OFR"

        const val STR_PREMIO = "Premio"
        const val STR_PREMIOS = "Premios"
    }


    interface Listener {
        fun setScreenTitle(title: String)
        fun showLoading()
        fun hideLoading()
        fun showErrorScreen(type: Int)
        fun goToFicha(extras: Bundle)
        fun goToFichaPremio(extras: Bundle)
        fun goToFest(extras: Bundle)
        fun showSearchItem()
        fun updateAdded()
        fun updatePremioEstado(premioEstado: Int)
    }

}

package biz.belcorp.consultoras.feature.home.addorders

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.content.res.AppCompatResources
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.component.CustomTypefaceSpan
import biz.belcorp.consultoras.common.component.GiftBarView
import biz.belcorp.consultoras.common.dialog.*
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.brand.BrandConfigModel
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.error.BooleanDtoModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.model.mensajeprol.MensajeProlModel
import biz.belcorp.consultoras.common.model.orders.*
import biz.belcorp.consultoras.common.model.product.ProductItem
import biz.belcorp.consultoras.common.model.promotion.PromotionDetailModel
import biz.belcorp.consultoras.common.model.promotion.PromotionOfferModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.domain.util.OfferTypes.getOfferTypeForAnalytics
import biz.belcorp.consultoras.feature.client.di.ClientComponent
import biz.belcorp.consultoras.feature.embedded.offers.OffersActivity
import biz.belcorp.consultoras.feature.embedded.pedidospendientes.PedidosPendientesActivity
import biz.belcorp.consultoras.feature.embedded.perfectduo.PerfectDuoActivity
import biz.belcorp.consultoras.feature.embedded.product.ProductWebActivity
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity.Companion.BROADCAST_COUNT_ACTION
import biz.belcorp.consultoras.feature.ficha.pasepedido.FichaPedidoActivity
import biz.belcorp.consultoras.feature.ficha.premio.FichaPremioActivity
import biz.belcorp.consultoras.feature.ficha.producto.FichaProductoActivity
import biz.belcorp.consultoras.feature.ficha.util.PromotionModelMapper
import biz.belcorp.consultoras.feature.finaloffer.FinalOfferActivity
import biz.belcorp.consultoras.feature.home.HomeActivity
import biz.belcorp.consultoras.feature.home.HomeActivity.Companion.KIT_NUEVA_BR_KEY
import biz.belcorp.consultoras.feature.home.HomeActivity.Companion.KIT_NUEVA_CUV
import biz.belcorp.consultoras.feature.home.addorders.DelOrdersFragment.DeleteOrderFragmentListener
import biz.belcorp.consultoras.feature.home.addorders.choosegift.GiftActivity
import biz.belcorp.consultoras.feature.home.addorders.choosegift.GiftPresenter
import biz.belcorp.consultoras.feature.home.addorders.choosegift.GiftView
import biz.belcorp.consultoras.feature.home.addorders.pedidosPendientes.PedidosPendientesDissmisableDialog
import biz.belcorp.consultoras.feature.home.addorders.pedidosPendientes.PedidosPendientesNoDissmisableDialog
import biz.belcorp.consultoras.feature.home.addorders.updatemail.ConfirmUpdateEmailDialog
import biz.belcorp.consultoras.feature.home.fest.FestActivity
import biz.belcorp.consultoras.feature.home.ganamas.armatupack.ArmaTuPackActivity
import biz.belcorp.consultoras.feature.home.ganamas.armatupack.ArmaTuPackFragment
import biz.belcorp.consultoras.feature.ofertafinal.OfertaFinalActivity
import biz.belcorp.consultoras.feature.ofertafinal.OfertaFinalLandingFragment
import biz.belcorp.consultoras.feature.orderdetail.OrderDetailActivity
import biz.belcorp.consultoras.feature.orders.OrdersList
import biz.belcorp.consultoras.feature.orders.OrdersListAdapter
import biz.belcorp.consultoras.feature.product.ProductActivity
import biz.belcorp.consultoras.feature.search.list.SearchListActivity
import biz.belcorp.consultoras.feature.search.single.SearchProductActivity
import biz.belcorp.consultoras.util.*
import biz.belcorp.consultoras.util.anotation.*
import biz.belcorp.consultoras.util.broadcast.FestBroadcast
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.library.util.KeyboardUtil
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.library.util.StringUtil.Empty
import biz.belcorp.mobile.components.core.helpers.ImagesHelper
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import biz.belcorp.mobile.components.dialogs.custom.CustomDialog
import biz.belcorp.mobile.components.offers.Multi
import biz.belcorp.mobile.components.offers.Offer
import biz.belcorp.mobile.components.offers.OrderTotal
import biz.belcorp.mobile.components.offers.PremioSimple
import biz.belcorp.mobile.components.offers.carousel.Carousel
import biz.belcorp.mobile.components.offers.model.GananciaItemModel
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.model.OrderTotalModel
import biz.belcorp.mobile.components.offers.promotion.Promotion
import biz.belcorp.mobile.components.offers.sello.Sello
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_add_orders.*
import kotlinx.android.synthetic.main.fragment_total.*
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import biz.belcorp.consultoras.util.analytics.AddOrder as AnalyticsAddOrder

class AddOrdersFragment :
    BaseFragment(),
    AddOrdersView,
    GiftView,
    OrdersList.OrderListListener,
    TotalFragment.Listener,
    GiftBarView.onClickGift,
    SafeLet,
    EnterDniDialog.OnClickEnterDniDialog,
    OrderTotal.OnOrderTotalListener,
    AgregarProductoReservaDialog.OnTombolaDialogListener{

    @Inject
    lateinit var presenter: AddOrdersPresenter

    @Inject
    lateinit var giftPresenter: GiftPresenter
    var isShowedDuo = false
    private var listener: Listener? = null
    private var listenergift: GiftBarView.onClickGift? = null
    private var clienteModel: ClienteModel? = null
    private var clientID: Int? = null
    private var clientLocalID: Int? = null
    private var montoMinimoPedido: Double = 0.toDouble()
    private var montoMaximoPedido: Double = 0.toDouble()
    private var revistaDigitalSuscripcion: Int = 0
    private var consultantName: String = ""
    private var decimalFormat: DecimalFormat = DecimalFormat()
    private var order: OrderModel? = null
    private var clientsDetalle: List<ClienteModel?>? = ArrayList()
    private var offersDataList: List<EstrategiaCarrusel?>? = ArrayList()
    private var clienteShow = false
    private var montoRegalo: Double = 0.0
    private var progreso: Double = 0.0
    private var moneySymbol: String = ""
    private var user: User? = null
    var loginModel: LoginModel? = null
    private val totalFragment = TotalFragment.newInstance()
    private val deleteFragment = DelOrdersFragment.newInstance()
    private var selectedCuv: String? = null
    private var selectetCuvQt: Int? = null
    private var addedItem: ProductItem? = null
    private var selectBrandId = 0
    private var deletedItem: ProductItem? = null
    private var orderListItemForEdit: ProductItem? = null
    private var pedidoConfigModel: PedidoConfigModel? = null
    private var configFest: FestivalConfiguracion? = null
    private var listTagItemOrder: MutableMap<Int, String?> = mutableMapOf()

    private var lastEventTime = Date()
    private var pedidosPorAprobar: Int = 0
    var changes = false
    var isVoiceSearch = false
    var tieneRegalo: Boolean = false
    private var lastProgreso: Double? = null

    var lastPorcen: Double? = null
    var animacionRegalo = false
    var imageDialog: Boolean = false
    var reservedDialog: ReservedDialog? = null

    private var enterDniDialog: EnterDniDialog? = null
    private var additionalOrderlistener: AdditionalOrderListener? = null

    private var flagTieneDuo = false
    private var tieneIncenticoAnim = false

    private var lastTab = 0
    private var lastSelectClientUnits = 0

    private var flagOfertaFinal = false
    private var flagEstadoPremio = 0
    private var hasConsultoraOfertaFinal = false

    private lateinit var imageHelper: ImagesHelper


    /****************************** 480*/

    private var montoIncentivo: Double = 00.0
    /******************************   */

    var precioRegalo: Boolean? = false

    private var nameListCarouselTracker: String? = null

    private var ofertas = mutableListOf<Oferta>()

    /** */

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(ClientComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        giftPresenter.attachView(this)
        init()

        presenter.data()
        presenter.clearMessagesPopUp()
        presenter.getOfferTitle(MenuCodeTop.OFFERS)
        presenter.getOfertaFinal()

    }

    /** */

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        }
        if (context is GiftBarView.onClickGift) listenergift = context
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        UXCam.tagScreenName(UXCamUtils.AddOrdersFragmentName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_orders, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.initTrack(SCREEN_TRACK)
        if (tieneRegalo && seekbarPuntosBar2 != null && animacionRegalo) {
            seekbarPuntosBar2.showRippleAnimation(animacionRegalo)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }


    //PRIVATE
    private fun init() {
        orderTotalComponent.orderTotalListener = this

        seekbarPuntosBar2.listenergift = this
        seekbarPuntosBar.isEnabled = false

        if (Belcorp.checkChristmasExperience()) run {
            frame_christmas.visibility = View.VISIBLE
        }

        presenter.getImageEnabled()

        activity?.let { imageHelper = ImagesHelper(it) }

        context?.let {
            flagOfertaFinal = SessionManager.getInstance(it).getFeatureFlagOfertaFinal() ?: false
        }


        val tLight = Typeface.createFromAsset(context?.assets, GlobalConstant.LATO_LIGHT_SOURCE)
        val tfBold = Typeface.createFromAsset(context?.assets, GlobalConstant.LATO_BOLD_SOURCE)
        val deleteSpannable = Spannable.Factory.getInstance()
            .newSpannable(context?.getString(R.string.add_order_button_eliminar_one))
        deleteSpannable.setSpan(CustomTypefaceSpan(tLight), 0, deleteSpannable.length, 0)

        val clicSpannable = Spannable.Factory.getInstance().newSpannable(context?.getString(R.string.add_order_button_eliminar_second))
        clicSpannable.setSpan(CustomTypefaceSpan(tfBold), 0, clicSpannable.length, 0)
        clicSpannable.setSpan(UnderlineSpan(), 0, clicSpannable.length, 0)

        val spannableString = SpannableStringBuilder()
        spannableString.append(deleteSpannable)
        spannableString.append(" ")
        spannableString.append(clicSpannable)

        tvwEliminar.text = spannableString

        lltOrder.setOnTouchListener { _, _ ->
            if (isVisible)
                hideKeyboard()
            false
        }

        llt_product_filter.setOnClickListener {
            if (tieneRegalo && seekbarPuntosBar2 != null) {
                seekbarPuntosBar2.showRippleAnimation(false)
            }
            openSearchActivity(false)
        }

        ordersList.orderListListener = this
        ordersGroup.orderListListener = this

        tvwClientFilter.setOnClickListener {
            listener?.onFilter()
            presenter.initTrack(EVENT_TRACK_COMBO_CLIENTE)
        }

        lltListarPorCliente.setOnClickListener {
            if (!clienteShow) {
                clienteShow = true
                updateTab()
            }
        }

        lltListarPorProducto.setOnClickListener {
            if (clienteShow) {
                clienteShow = false
                updateTab()
            }
        }

        presenter.getNameListAnalytics(OffersOriginTypeLocation.ORIGEN_PEDIDO, OffersOriginTypeSection.ORIGEN_OPT)

        tvwEliminar.setOnClickListener {
            if (order != null && order?.productosDetalle != null && !order?.productosDetalle!!.isEmpty()) {
                val msg = MessageAnimDialog()
                val onAction = { msg.dismiss() }
                msg
                    .setStringTitle(R.string.add_order_question_all_delete)
                    .setStringMessage(R.string.add_order_desea_continuar)
                    .setStringAceptar(R.string.button_eliminar)
                    .showIcon(true)
                    .setAnimated(false)
                    .showRipple(false)
                    .setIcon(ContextCompat.getDrawable(activity!!, R.drawable.img_bolsa_asombro), 0)
                    .setListener(object : MessageAnimDialog.MessageDialogListener {
                        override fun cancelar() {
                            // EMPTY
                        }

                        override fun aceptar() {
                            onAction()
                            presenter.deleteAllProduct(order, context)
                        }
                    })
                    .enableExtraButton1("NO, MODIFICAR PEDIDO") { _ -> onAction() }
                    .show((context as AppCompatActivity).supportFragmentManager, "modalDelete")
            }
        }

        lltVerMas.setOnClickListener {
            verMas()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    TAB_OFERTAS -> {
                        checkSuscrita()
                        tvwEliminar.visibility = View.GONE
                        lltListContent.visibility = View.GONE
                        tvwDisclaimer.visibility = View.GONE
                        lltTotal.visibility = View.GONE
                        tvwVerMas.visibility = View.VISIBLE

                        ofertas?.let {
                            if (it.isEmpty()) {
                                tvwOfertasEmpty.visibility = View.VISIBLE
                            } else {
                                tvwOfertasEmpty.visibility = View.GONE
                                carouselOffers.visibility = View.VISIBLE
                            }
                        }

                        if(ofertas.isNotEmpty() || offersDataList?.isNotEmpty() == true){
                            tvwOfertasEmpty.visibility = View.GONE
                            carouselOffers.visibility = View.VISIBLE
                        } else{
                            tvwOfertasEmpty.visibility = View.VISIBLE
                        }

                        lltVerMas.visibility = View.VISIBLE
                        if (TAB_OFERTAS != lastTab) presenter.initTrack(EVENT_TRACK_CLICK_PESTANA_PEDIDO_GANAMAS)

                        lastTab = TAB_OFERTAS
                    }
                    TAB_PEDIDO -> {
                        checkSuscrita()
                        lltClub.setBackgroundColor(Color.WHITE)
                        lltListContent.visibility = View.VISIBLE
                        lltTotal.visibility = View.VISIBLE
                        tvwOfertasEmpty.visibility = View.GONE
                        tvwVerMas.visibility = View.GONE
                        carouselOffers.visibility = View.GONE

                        if (order != null && order?.productosDetalle != null && !order?.productosDetalle!!.isEmpty()) {

                            if (order!!.precioPorNivel) tvwDisclaimer.visibility = View.VISIBLE
                            else tvwDisclaimer.visibility = View.GONE
                        } else {
                            tvwEliminar.visibility = View.GONE
                            tvwDisclaimer.visibility = View.GONE
                        }
                        lltVerMas.visibility = View.GONE

                        //EAAR
                        if (order?.cantidadProductos != SessionManager.getInstance(this@AddOrdersFragment.context!!).getOrdersCount()) {
                            presenter.data()//update pedido
                        }

                        if (TAB_PEDIDO != lastTab) presenter.initTrack(EVENT_TRACK_CLICK_PESTANA_PEDIDO_GANAMAS)

                        lastTab = TAB_PEDIDO
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // EMPTY
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // EMPTY
            }
        })

        val tab = tabLayout.getTabAt(TAB_PEDIDO)
        tab?.select()
        loadingView?.showLoading()
        arguments?.getBoolean(GlobalConstant.FROM_MODIFICAR, false)?.let {
            if (it) {
                presenter.initTrack(EVENT_TRACK_MODIFICAR_PEDIDO)
            }
        }
        arguments?.getBoolean(GlobalConstant.FROM_CLIENT_CARD, false)?.let {
            if (it) {
                rltCliente.visibility = View.GONE
                tvwClientFilterDisabled.text = arguments?.getString(GlobalConstant.CLIENT_NAME)
                tvwClientFilterDisabled.isEnabled = false
                rltClienteDisabled.visibility = View.VISIBLE
            }
        }

        arguments?.getParcelable<BrandConfigModel>(AddOrdersActivity.BRANDING_CONFIG)?.let {
            it.imageOrderUrl?.let { image ->
                Glide.with(activity).asDrawable().load(image).apply(RequestOptions.noTransformation().priority(Priority.HIGH))
                    .listener(BrandHeaderRequestListener()).submit()
            }

            it.colorOrderBar?.let { color ->
                var bgDrawable: LayerDrawable = seekbarPuntosBar2.getProgressBar().progressDrawable as LayerDrawable
                bgDrawable.getDrawable(1).setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN)

                seekbarPuntosBar2.getProgressBar().progressDrawable = bgDrawable

            }

            it.colorTextBar?.let { color ->
                seekbarPuntosBar2.updateTextColor(color)
            }
        }

        lnlVoice.setOnClickListener {
            Tracker.trackEvent(GlobalConstant.SCREEN_ORDERS_GPR,
                GlobalConstant.EVENT_CATEGORY_PEDIDO, GlobalConstant.EVENT_ACTION_BUTTON,
                GlobalConstant.EVENT_LABEL_PEDIDO_VOZ,
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, user)

            if (tieneRegalo && seekbarPuntosBar2 != null) {
                seekbarPuntosBar2.showRippleAnimation(false)
            }
            openSearchActivity(true)
        }

        btnPedidosPorAprobar.setOnClickListener {
            goPedidosPorAprobar()
        }

        tvwPendingOrders.setOnClickListener {
            goPedidosPorAprobar()
        }

        btnAccesoPremioLanding?.premioListener = object : PremioSimple.OnPremioListener {
            override fun onClick(view: View) {
                listener?.navigateToOfertaFinal()
            }

        }

        promotionMapper.df = decimalFormat
        promotionMapper.symbol = moneySymbol

    }

    //MVP
    override fun didPressedButton() {
        onClickTotalFragment()
    }

    override fun reserveOrder(){
        onClickTotalFragment()
    }

    override fun mostrarExperiencia(mostrarExperiencia: Boolean, estadoPremio: Int) {
        this.hasConsultoraOfertaFinal = mostrarExperiencia
        updatePremioLandingAccess(estadoPremio)
        if (flagOfertaFinal  && mostrarExperiencia) {
            showPremioLandingAccess(estadoPremio)
        }
    }

    override fun showLoadingDialog() {
        reservedDialog?.let {
            it.showLoading()
        } ?: run {
            loadingView?.showLoading()
        }
    }

    override fun hideLoadingDialog() {
        reservedDialog?.let {
            it.hideLoading()
        } ?: run {
            loadingView?.hideLoading()
        }
    }

    inner class BrandHeaderRequestListener : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            ivwOrderTop.visibility = View.GONE
            seekbarPuntosBar2.setDefaultTextColor()
            return false

        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            ivwOrderTop.setImageDrawable(resource)
            ivwOrderTop.visibility = View.VISIBLE
            return false

        }

    }

    private fun showEnterDniDialog() {
        context?.let {
            enterDniDialog = enterDniDialog ?: EnterDniDialog(it, this)
            enterDniDialog?.show()
        }
    }

    private fun initEnterDialog() {

        receiveOrderSwitch.setOnTouchListener { _, _ -> !NetworkUtil.isThereInternetConnection(context) }
        receiveOrderSwitch.setOnCheckedChangeListener { _, p1 ->
            if (p1) {
                showEnterDniDialog()
                enterDniDialog?.resetDialog(true)
            } else {
                showLoading()
                presenter.updateDni(p1)
            }
        }
        changePersonButton.setOnClickListener {
            showEnterDniDialog()
            enterDniDialog?.isUpdate = true
            enterDniDialog?.resetDialog(!receiveOrderSwitch.isChecked)
            enterDniDialog?.setName(nameReceiverText.text.toString())
            enterDniDialog?.setDni(dniReceiverText.text.toString())
        }
    }

    private fun initMultiOrderSwitch() {
        multiOrderSwitch.setOnTouchListener { _, _ -> !NetworkUtil.isThereInternetConnection(context) }
        multiOrderSwitch.setOnCheckedChangeListener { _, p1 ->
            presenter.updateStateMultiOrder(p1)
        }
    }

    private fun verMas() {
        presenter.initTrack(EVENT_TRACK_CLICK_OFERTAS)
        presenter.showOffers()
    }

    private fun transformOffersRecomended(offers: List<Oferta>): ArrayList<OfferModel> {
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

    // Por Borrar
    private fun transformOffersList(type: String, offers: List<EstrategiaCarrusel>): ArrayList<OfferModel> {
        val list = arrayListOf<OfferModel>()

        for (offer in offers) {

            var showAmount = true
            if (offer.tipoPersonalizacion == OfferTypes.HV) {
                showAmount = false
            }

            if (!offer.codigoEstrategia.isNullOrEmpty() && offer.codigoEstrategia == GlobalConstant.DUO) {
                list.add(Offer.transform(
                    id = "",
                    brand = "",
                    productName = "",
                    personalAmount = "",
                    clientAmount = "",
                    imageURL = "",
                    leverName = OfferTypes.DP,
                    isSelectionType = false,
                    marcaID = 0,
                    imageBgURL = offer.fotoProductoSmall ?: "",
                    textColor = "",
                    showClientAmount = false))
            } else {
                safeLet(offer.cuv, offer.descripcionMarca
                    ?: "", offer.descripcionCUV, offer.precioValorizado,
                    offer.precioFinal, offer.fotoProductoSmall, "", "", offer.codigoEstrategia
                    ?: "", offer.marcaID
                    ?: 0) { cuv2, nombreMarca, nombreOferta, precioValorizado, precioCatalogo,
                            imagenURL, imagenFondo, colorTexto, codigoEstrategia, marcaID ->

                    val leverType: String = if (user?.isRDEsSuscrita == true) type else codigoEstrategia

                    list.add(Offer.transform(cuv2, nombreMarca, nombreOferta,
                        formatWithMoneySymbol(precioCatalogo.toString()),
                        formatWithMoneySymbol(precioValorizado.toString()),
                        imagenURL, leverType, checkSelectionType(codigoEstrategia), marcaID,
                        "", colorTexto, showAmount, null, null, !offer.tieneStock, flagFestival = offer.flagFestival))
                }
            }
        }

        return list
    }

    private fun checkSelectionType(codigoEstrategia: String): Boolean {
        return if (codigoEstrategia.isNotEmpty()) {
            val codigo = codigoEstrategia.toInt()
            codigo == COD_EST_COMPUESTA_VARIABLE
        } else
            false
    }

    private fun agregarPedidoDesdeCarrusel(quantity: Int, keyItem: String, counterView: Counter, typeCarousel: String) {

        if (NetworkUtil.isThereInternetConnection(context())) {
            when (typeCarousel){
                OfferTypes.ALG -> {
                    ofertas?.firstOrNull { it?.cuv == keyItem }?.let {
                        presenter.agregar(it, quantity, counterView, DeviceUtil.getId(context), OfertaFinalLandingFragment.TYPE, OffersOriginType.ORIGEN_LANDING)
                    }
                }
                OfferTypes.OPT -> {
                    offersDataList?.firstOrNull { it?.cuv == keyItem }?.let {
                        presenter.agregar(it, quantity, counterView, DeviceUtil.getId(context), it.origenPedidoWeb?.toString()?: "")
                    }
                }
            }
        } else {
            showNetworkError()
        }

    }

    override fun onProductAdded(quantity: Int, productCUV: ProductCUV, message: String?, codeAlert: Int) {
        if (codeAlert == AddedAlertType.DEFAULT) {
            val messageDialog = message ?: getString(R.string.msj_offer_added)
            val image = ImageUtils.verifiedImageUrl(productCUV)
            context?.let {
                val url = if (imageDialog) image else null  // Verifica que el flag este activo para mostrar la imagen
                val colorText = ContextCompat.getColor(it, R.color.leaf_green)
                showBottomDialog(it, messageDialog, url, colorText)
            }
        }

        if (codeAlert == AddedAlertType.FESTIVAL) {
            context?.let {
                showBottomDialogAction(it, message) {
                    val extras = Bundle()
                    goToFest()
                }
            }
        }

        activity?.let { FestBroadcast.sendAddUpdateToCart(it) }
        listener?.updateOffersCount(quantity)

        val sectionDesc = getOfferTypeForAnalytics(productCUV.tipoPersonalizacion)

        val onAnalyticsLoaded: (descriptionPalanca: String) -> Unit = { descriptionPalanca ->
            AnalyticsAddOrder.addToCartOffer(user, productCUV, nameListCarouselTracker, descriptionPalanca)
        }

        presenter.getAnalytics(sectionDesc, onAnalyticsLoaded)

        // Se agrego este metodo del response AGREGAR del antiguo carousel
        presenter.getOrder(CallOrderType.FROM_ADD_OFFER)
    }

    override fun setImageEnabled(imageDialog: Boolean) {
        this.imageDialog = imageDialog
    }

    override fun onProductNotAdded(message: String?) {
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

    override fun onProductFestNotEliminated(message: String?) {
        message?.let {
            context?.let { context ->
                BottomDialog.Builder(context)
                    .setIcon(R.drawable.ic_mano_error)
                    .setTitle(getString(R.string.fest_confirt_message_p1))
                    .setTitleBold()
                    .setContent(it)
                    .setNegativeText(R.string.fest_confirt_calcel)
                    .setPositiveText(R.string.fest_confirt_ok)
                    .setNegativeTextColor(R.color.black)
                    .setNegativeBorderColor(R.color.black)
                    .setNegativeBackgroundColor(R.color.white)
                    .onNegative(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog) {
                            dialog.dismiss()
                        }
                    })
                    .onPositive(object : BottomDialog.ButtonCallback {
                        override fun onClick(dialog: BottomDialog, chbxConfirmacion: CheckBox) {

                            deletedItem?.let {
                                it.reemplazarFestival = true
                                presenter.deleteItem(order, it)
                            }

                        }
                    })
                    .setPositiveBackgroundColor(R.color.magenta)
                    .show()

            }
        }
    }

    override fun showError(canBack: Boolean) {
        if (!NetworkUtil.isThereInternetConnection(context)) {
            showNetworkError()
            return
        }
    }

    override fun showError(stringId: Int) {
        showDialogError(getString(stringId))
    }

    private fun showDialogError(message: String) {
        context?.let {
            BottomDialog.Builder(it)
                .setIcon(R.drawable.ic_mano_error)
                .setContent(message)
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

    private fun formatWithMoneySymbol(precio: String): String {
        return "$moneySymbol ${decimalFormat.format(precio.toBigDecimal())}"
    }

    override fun scrolldown(size: Int) {
        nsvOrders.smoothScrollTo(nsvOrders.scrollX, nsvOrders.scrollY + size)
    }

    override fun onscrolldownDelete(size: Int, position: Int) {
        if (position < 3) {
            nsvOrders.scrollTo(nsvOrders.scrollX, lltListContent.y.toInt() + 200 + (size * position))
        } else {
            nsvOrders.scrollTo(nsvOrders.scrollX, lltListContent.y.toInt() + (size * position))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            BaseFichaActivity.ACCESS_FROM_PEDIDO_GANASMAS_ADDED_FICHA -> {
                if (BaseFichaActivity.PRODUCT_ADDED) {
                    presenter.data()
                    BaseFichaActivity.PRODUCT_ADDED = false
                }
            }

            SearchProductActivity.SEARCH_RESULT -> {
                val bundle = data?.getBundleExtra(SearchProductActivity.EXTRA_BUNDLE)
                if (bundle != null) {

                    val messageProl = bundle.getParcelableArrayList<MensajeProlModel>(SearchProductActivity.EXTRA_MENSAJE_PROL)
                    val message = bundle.getString(SearchProductActivity.MESSAGE_ADDING)

                    if (messageProl != null) {
                        MensajeProlUtil.showMensajeProl(this@AddOrdersFragment.activity, messageProl)
                    } else if (message != null) {
                        if (message.contains(SEPARATOR)) {
                            activity?.let {
                                if ((message.contains(GlobalConstant.DUOPERFECTO_CODE_AGREGASTE) || message.contains(GlobalConstant.DUOPERFECTO_CODE_COMPLETASTE))) {
                                    save(bundle, resultCode)
                                    showDuoToolTip(data)
                                } else {
                                    showDuoToolTip(data)
                                }
                            }
                        } else {
                            showDuoToolTip(data)
                        }
                    } else {
                        save(bundle, resultCode)
                        if (resultCode == Activity.RESULT_OK) {
                            val fromOnProductAdded = data?.extras?.getBoolean(SearchProductActivity.FROM_ONPRODUCT_ADDED)
                                ?: false
                            var message = getString(R.string.msj_offer_added)
                            val typeAlert = data.extras?.getInt(SearchProductActivity.EXTRA_TYPE_ALERT)

                            if (fromOnProductAdded) {
                                message = data.extras?.getString(SearchProductActivity.EXTRA_MESSAGE_ADDING)
                                    ?: getString(R.string.msj_offer_added_default)
                            }

                            if (typeAlert == AddedAlertType.DEFAULT) {
                                context?.let { ctx ->
                                    val colorText = ContextCompat.getColor(ctx, R.color.leaf_green)
                                    showBottomDialog(ctx, message, null, colorText)
                                }
                            }

                            if (typeAlert == AddedAlertType.FESTIVAL) {
                                context?.let { ctx ->
                                    showBottomDialogAction(ctx, message) {
                                        goToFest()
                                    }
                                }
                            }

                        } else if (resultCode == Activity.RESULT_CANCELED) {
                            if (BaseFichaActivity.PRODUCT_ADDED) {
                                presenter.getOrder(CallOrderType.FROM_ADD_OFFER)
                                BaseFichaActivity.PRODUCT_ADDED = false
                            }
                        }
                    }
                }

            }

            BaseFichaActivity.RESULT -> {

                if (resultCode == Activity.RESULT_OK) {

                    changes = true
                    presenter.getOrder(CallOrderType.FROM_UPDATE_ITEM)

                    val bundle = data?.getBundleExtra(BaseFichaActivity.EXTRA_BUNDLE)
                    if (bundle != null) {
                        val mensajes = bundle.getParcelableArrayList<MensajeProlModel>(BaseFichaActivity.EXTRA_MENSAJE_PROL)

                        if (mensajes != null) {
                            MensajeProlUtil.showMensajeProl(this@AddOrdersFragment.activity, mensajes)

                        }
                    }

                    val message = data?.extras?.getString(BaseFichaActivity.EXTRA_MESSAGE_ADDING)
                        ?: getString(R.string.msj_offer_added_default)
                    val codeAlert = data?.extras?.getString(BaseFichaActivity.EXTRA_CODE_ALERT)

                    when (codeAlert) {
                        GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO -> {
                            context?.let { itContext ->
                                showBottomDialogAction(itContext, message) {
                                    goToFest()
                                }
                            }
                        }
                        GlobalConstant.CODE_OK -> {
                            context?.let {
                                val colorText = ContextCompat.getColor(it, R.color.lograste_puntaje)
                                showBottomDialog(it, message, null, colorText)
                            }
                        }
                    }
                }
            }

            ProductActivity.PRODUCT_RESULT -> {

                if (resultCode == Activity.RESULT_OK) {

                    val message = data?.extras?.getString(ProductActivity.EXTRA_MESSAGE_ADDING)
                        ?: getString(R.string.msj_offer_added_default)
                    context?.let {
                        val colorText = ContextCompat.getColor(it, R.color.lograste_puntaje)
                        showBottomDialog(it, message, null, colorText)
                    }

                    changes = true
                    presenter.getOrder(CallOrderType.FROM_UPDATE_ITEM)

                    val bundle = data?.getBundleExtra(ProductActivity.EXTRA_BUNDLE)
                    if (bundle != null) {
                        val mensajes = bundle.getParcelableArrayList<MensajeProlModel>(ProductActivity.EXTRA_MENSAJE_PROL)

                        if (mensajes != null) {
                            MensajeProlUtil.showMensajeProl(this@AddOrdersFragment.activity, mensajes)
                        }
                    }
                }
            }

            OffersActivity.RESULT,
            SearchListActivity.RESULT,
            FestActivity.RESULT,
            ProductWebActivity.RESULT -> {
                if (resultCode == Activity.RESULT_OK) {
                    changes = true
                    presenter.getOrder(CallOrderType.FROM_ADD_OFFER)
                }
            }

            PedidosPendientesActivity.RESULT -> {
                if (resultCode == Activity.RESULT_OK) {
                    changes = true
                    presenter.updateConfigPedidosPendientes()
                    presenter.getOrder(CallOrderType.FROM_ADD_OFFER)
                }
            }

            GiftActivity.GIFT_RESULT -> {
                if (resultCode == Activity.RESULT_OK) {
                    changes = true
                    presenter.getOrder(CallOrderType.FROM_ADD_ITEM)
                }
            }

            ArmaTuPackFragment.ATP_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    changes = true
                    val message = getString(R.string.msj_offer_added_default)
                    context?.let {
                        val colorText = ContextCompat.getColor(it, R.color.lograste_puntaje)
                        showBottomDialog(it, message, null, colorText)
                    }
                    presenter.getOrder(CallOrderType.FROM_UPDATE_ITEM)
                }
            }

            FichaPremioActivity.REQUEST_CODE_FICHA_PREMIO -> {
                if (resultCode == Activity.RESULT_OK) {
                    presenter.getOrder(0)
                }
            }

            OfertaFinalActivity.REQUEST_CODE_LANDING_OFERTAFINAL -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        val flagAddedLandingOF = it.getBooleanExtra(OfertaFinalActivity.EXTRA_FLAG_ADDED_LANDING, false)
                        val estadoPremio = it.getIntExtra(OfertaFinalActivity.EXTRA_PREMIO_ESTADO, 0)
                        if (flagAddedLandingOF)
                            presenter.getOrder(0)
                        presenter.saveEstadoPremio(estadoPremio)
                        updatePremioLandingAccess(estadoPremio)
                    }
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun save(bundle: Bundle, resultCode: Int) {
        clienteModel = bundle.getParcelable(SearchProductActivity.EXTRA_CLIENTEMODEL)
        if (clienteModel != null) {
            tvwClientFilter.text = clienteModel?.nombres +
                if (clienteModel?.apellidos != null) " " +
                    clienteModel?.apellidos else ""
        }
        if (resultCode == Activity.RESULT_OK) {
            changes = true
            selectedCuv = bundle.getString(SearchProductActivity.EXTRA_CUV)
            selectetCuvQt = bundle.getInt(SearchProductActivity.EXTRA_CUV_QT)
            selectBrandId = bundle.getInt(SearchProductActivity.EXTRA_CUV_BRAND_ID)
            isVoiceSearch = bundle.getBoolean(SearchProductActivity.EXTRA_VOICE)
            presenter.getOrder(CallOrderType.FROM_ADD_ITEM)
            presenter.initTrack(EVENT_TRACK_BUSCAR_PRODUCTO)
        }
    }

    private fun showDuoToolTip(data: Intent?) {
        activity?.let {
            val tool = ProductsToolTipMessage.newInstance()
            data?.let { data ->
                data?.getBundleExtra(SearchProductActivity.EXTRA_BUNDLE).getString(SearchProductActivity.MESSAGE_ADDING)?.let { message ->

                    val bundle = Bundle()
                    if (message.contains(SEPARATOR)) {
                        bundle.putInt("type", presenter.getTooltipDuoType(message.substring(0, message.indexOf(SEPARATOR))))
                        bundle.putString(GlobalConstant.TOOL_TIPS_MESSAGE,
                            message.substring(message.indexOf(SEPARATOR) + 1))
                    } else {
                        bundle.putInt("type", 4)
                        bundle.putString(GlobalConstant.TOOL_TIPS_MESSAGE, message)
                    }

                    tool.arguments = bundle

                    tool.show(it.supportFragmentManager, "duo_tooltip")
                }
            }


        }
    }

    private fun checkSuscrita() {
        when (revistaDigitalSuscripcion) {
            MagazineSuscriptionType.WITH_GND_SA,
            MagazineSuscriptionType.WITH_GND_SNA -> {
                context?.let {
                    tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(it, R.color.golden_gana))
                    ivwOffer.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_ganamas))
                    viewSeparator.setBackgroundColor(ContextCompat.getColor(it, R.color.golden_gana))
                    tvwOfertas.setTextColor(ContextCompat.getColor(it, R.color.golden_gana))
                    setupImageTint()
                }
            }
            else -> {
                ivwOffer.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.tab_offer))
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(activity!!.applicationContext, R.color.primary))

            }
        }
        lltClub.setBackgroundColor(Color.WHITE)
    }

    private fun setupImageTint() {
        context?.let {
            val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(it, R.color.golden_gana))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivwOffer.backgroundTintList = colorStateList
            } else {
                ViewCompat.setBackgroundTintList(ivwOffer, colorStateList)
            }
        }
    }

    private fun hideKeyboard() {
        activity?.run {
            var view = this.currentFocus
            if (view == null) view = View(activity)
            KeyboardUtil.dismissKeyboard(this, view)
        }

    }

    private fun onClickClient() {
        ordersList.visibility = View.GONE
        ordersGroup.visibility = View.VISIBLE
    }

    private fun List<ClienteModel?>.setClientQuantity() {
        var count = 0
        for (i in this) {
            if (i?.clienteID != 0) count++
        }
        tvwListarPorClienteCounter.text = resources.getString(R.string.add_order_counter, count)
        tvwListarPorCliente.text = resources.getQuantityString(R.plurals.add_order_client, count, count)
    }

    private fun onClickProduct() {
        ordersList.visibility = View.VISIBLE
        ordersGroup.visibility = View.GONE
    }

    private fun openSearchActivity(voice: Boolean) {

        val intent = Intent(context, SearchProductActivity::class.java)
        val args = Bundle()

        val fromClientCard = arguments?.getBoolean(GlobalConstant.FROM_CLIENT_CARD)
        fromClientCard?.let { _ ->
            args.putBoolean(GlobalConstant.FROM_CLIENT_CARD, fromClientCard)
        }

        if (clienteModel == null) {
            clienteModel = arguments?.getParcelable(SearchProductActivity.EXTRA_CLIENTEMODEL)
        }
        clienteModel?.let { _ ->
            args.putParcelable(SearchProductActivity.EXTRA_CLIENTEMODEL, clienteModel as Parcelable)
        }
        order?.let {
            args.putParcelable(SearchProductActivity.EXTRA_ORDER_MODEL, order as Parcelable)
        }
        args.putString(SearchProductActivity.EXTRA_MONEYSYMBOL, moneySymbol)
        args.putBoolean(SearchProductActivity.EXTRA_VOICE, voice)

        if (user?.countryISO != null) {
            args.putString(SearchProductActivity.EXTRA_COUNTRYISO, user?.countryISO)
        }

        activity?.tvw_toolbar_title?.text.toString().let { title ->
            args.putString(SearchProductActivity.EXTRA_TITLE, title)
        }
        intent.putExtras(args)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!,
            edtProductFilter as View, "profile")
        startActivityForResult(intent, SearchProductActivity.SEARCH_RESULT, options.toBundle())

    }

    override fun onClickTotalFragment() {


        order?.let { ord ->

            if (ord.pedidoValidado) { // El pedido esta reservado se puede modificar

                presenter.deshacerReserva()

            } else { // El pedido esta en modificacacion se puede reservar

                ord.productosDetalle?.let {
                    if (it.isNotEmpty()) {
                        if (user?.bloqueoPendiente ?: false) {
                            presenter.getPedidosPendientes(order, flagOfertaFinal)
                        } else {
                            presenter.reservar(order, flagOfertaFinal)
                        }
                    } else {
                        Toast.makeText(activity, activity?.getString(R.string.add_order_empty), Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    Toast.makeText(activity, activity?.getString(R.string.add_order_empty), Toast.LENGTH_SHORT).show()
                }

            }
        } ?: run {
            Toast.makeText(activity, activity?.getString(R.string.add_order_empty), Toast.LENGTH_SHORT).show()
        }
    }

    fun showOfertaFinalPopUp(configuracionPremio: ConfiguracionPremio?) {
        CustomDialog.Builder(requireContext())
            .setHeaderVisible(true)
            .setLinkVisible(true)
            .setHeaderTextColor(getString(R.string.oferta_final_popup_header_color))
            .setHeaderBold(true)
            .setTitle(configuracionPremio?.textMetaPrincipal ?: Empty)
            .setContent(configuracionPremio?.textInferior ?: Empty)
            .setTitleTextColor(getString(R.string.oferta_final_popup_title_color))
            .setTitleBold(true)
            .setHeader(getString(R.string.oferta_final_reserved))
            .setButtonText(getString(R.string.oferta_final_continue_shop))
            .setLink(configuracionPremio?.textTerminosCondiciones ?: Empty)
            .onLink(object : CustomDialog.LinkCallback {
                override fun onClick() {
                    configuracionPremio?.textTerminosCondiciones?.let {
                        try {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                            context?.startActivity(browserIntent)
                        } catch (ex: ActivityNotFoundException) {
                            Toast.makeText(context?.applicationContext, R.string.legal_activity_not_found, Toast.LENGTH_SHORT).show()
                            BelcorpLogger.w("showOfertaFinalPopUp", ex)
                        }
                    }
                }

            })
            .onButton(object : CustomDialog.ButtonCallback {
                override fun onClick(dialog: CustomDialog) {
                    context?.let {
                        dialog.dismiss()
                        listener?.navigateToOfertaFinal()
                    }
                }
            })
            .onClose(object: CustomDialog.CloseCallback{
                override fun onClick(dialog: CustomDialog) {
                    dialog.dismiss()
                    callTrackReservar()
                }

            })

            .build().show()

    }

    fun trackEvent(eventCat: String, eventAction: String,
                   eventLabel: String, eventName: String) {
        Tracker.trackEvent(GlobalConstant.SCREEN_ORDERS_GPR, eventCat, eventAction,
            eventLabel, eventName, user)
    }

    /** */

    fun callTrackReservar() {
        presenter.initTrack(EVENT_TRACK_CLICK_RESERVAR)
    }

    override fun onUndoReserve() {
        presenter.getOrder(CallOrderType.FROM_UNDO_RESERVE)
    }

    override fun setMontoIncentivo(monto: Double) {
        montoIncentivo = monto
    }

    override fun initTrack(model: LoginModel, tipoTrack: Int) {
        loginModel = model

        when (tipoTrack) {
            SCREEN_TRACK -> {
                Tracker.trackScreen(GlobalConstant.SCREEN_ORDERS_GPR, model)
            }
            EVENT_TRACK_CLICK_OFERTAS -> {
                AnalyticsAddOrder.buttonViewMore()
            }
            EVENT_TRACK_CLICK_RESERVAR -> {
                AnalyticsAddOrder.buttonSaveReserve(GlobalConstant.EVENT_LABEL_RESERVAR_PEDIDO)
                if (user?.isUltimoDiaFacturacion == true && order?.pedidoValidado == true) {
                    val intent = Intent(activity, OrderDetailActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    presenter.getOrder(CallOrderType.FROM_RESERVE)
                }
            }
            EVENT_TRACK_CLICK_PESTANA_CLIENTE_PRODUCTO -> {
                AnalyticsAddOrder.buttonTabsClientsUnits(if (clienteShow) GlobalConstant.EVENT_LABEL_TAB_CLIENTES else GlobalConstant.EVENT_LABEL_TAB_UNIDADES)
            }
            EVENT_TRACK_COMBO_CLIENTE -> {
            }
            EVENT_TRACK_BUSCAR_PRODUCTO -> {
                Tracker.Pedido.trackBotonBuscarProducto(model, selectedCuv)
            }
            EVENT_TRACK_VISUALIZAR_PRODUCTO -> {
                AnalyticsAddOrder.buttonSelectProduct(orderListItemForEdit?.descripcionProd)
            }
            EVENT_TRACK_MODIFICAR_PEDIDO -> {
                Tracker.Pedido.trackBotonModificarPedido(model, orderListItemForEdit?.id.toString())
            }
            EVENT_TRACK_AGREGAR_PRODUCTO -> {
                addedItem?.let {
                    Tracker.Pedido.trackAgregarProducto(model, addedItem, selectBrandId, isVoiceSearch)
                }
            }
            EVENT_TRACK_ELIMINAR_PRODUCTO -> {
                deletedItem?.let {

                    val onAnalyticsLoaded: (descriptionPalanca: String) -> Unit = { descriptionPalanca ->
                        AnalyticsAddOrder.removeToCart(it, null, descriptionPalanca)
                    }

                    presenter.getAnalytics(it.tipoOferta, onAnalyticsLoaded)
                }
            }
            EVENT_TRACK_CLICK_PESTANA_PEDIDO_GANAMAS -> {
                var nameLabel: String? = null
                when (tabLayout.selectedTabPosition) {
                    TAB_PEDIDO -> nameLabel = GlobalConstant.EVENT_LABEL_TAB_PEDIDO
                    TAB_OFERTAS -> nameLabel = GlobalConstant.EVENT_LABEL_TAB_GANAMAS
                }
                nameLabel?.let {
                    AnalyticsAddOrder.buttonTabsOrderGanamas(it)
                }
            }
        }

    }


    /** */

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }


    var lastProgressValue: Double? = null

    fun renderBar(config: PedidoConfigModel?, order: OrderModel?) {
        var isFirst = lastProgressValue == null
        lastProgressValue = progreso
        val lastProgress = progreso
        var tieneIncentico = montoIncentivo != null && !isFirst

        config?.let {
            val cfg = it
            order?.let {
                val ord = it
                var montoMinimo = montoMinimoPedido
                var montoMaximo = montoMaximoPedido
                montoRegalo = kotlin.math.max(order.tippingPoint?.toDouble()
                    ?: montoMinimo, montoMinimo)
                var tieneEscala = cfg.escalaDescuento?.filterNotNull()?.size?.let { it > 0 }
                    ?: false
                var importeTotal = ord.importeTotal?.toDouble() ?: 0.0
                var montoEscala = ord.montoEscala?.toDouble() ?: 0.0
                tieneRegalo = ord.muestraRegalo

                animacionRegalo = (order.productosDetalle?.filterNotNull()?.filter {
                    it.isEsPremioElectivo ?: false
                }?.size!! == 0)

                var max = kotlin.math.max(montoMinimo, montoRegalo)

                progreso = if (importeTotal < montoMinimo) importeTotal else Math.max(montoEscala, montoMinimo)
                lastProgreso = progreso

                var mensaje: String? = null
                var mEscala: EscalaDescuentoModel? = null
                var mNextEscala: EscalaDescuentoModel? = null
                var mLastEscala: EscalaDescuentoModel? = null
                var mPreLastEscalaHasta = 0.0
                var curPorcen: Double

                val montoIncentivo = montoIncentivo

                if (Belcorp.checkMaxAmmount(montoMaximoPedido) && tieneEscala) {
                    var lastHasta = montoMinimo
                    var idxNextEscala = 0
                    var idx = 0
                    cfg.escalaDescuento?.filterNotNull()?.forEach {
                        var desde = it.montoDesde?.let { it.toDouble() } ?: 0.0
                        desde = if (desde > 0.0) desde else lastHasta
                        val hasta = it.montoHasta?.let { it.toDouble() } ?: 0.0
                        if (progreso in desde..hasta) {
                            if (!Belcorp.checkMaxAmmount(hasta)) {
                                max = kotlin.math.max(max, hasta)
                            } else if (!Belcorp.checkMaxAmmount(lastHasta)) {
                                max = kotlin.math.max(max, lastHasta)
                            }
                            mEscala = it
                            idxNextEscala = idx + 1
                            mPreLastEscalaHasta = lastHasta
                        }
                        if (idx == 1) {
                            tieneIncentico = (montoIncentivo > montoMinimo && montoIncentivo <= hasta && progreso <= hasta) && tieneIncentico
                        }
                        lastHasta = hasta
                        idx++
                    }
                    idxNextEscala = kotlin.math.min(idxNextEscala, cfg.escalaDescuento?.filterNotNull()?.size.let { it?.minus(1) }
                        ?: 0)
                    mNextEscala = cfg.escalaDescuento?.filterNotNull()!![idxNextEscala]
                    mLastEscala = cfg.escalaDescuento?.filterNotNull()!![Math.max(idxNextEscala - 1, idxNextEscala)]
                    /* Mensajes */
                    mensaje = when {
                        (tieneRegalo && progreso == 0.0) -> {
                            if (precioRegalo == true) {
                                GlobalConstant.EMPIEZA_AGREGAR_PRODUCTO_NO_REGALO
                            } else {
                                GlobalConstant.EMPIEZA_AGREGAR_PRODUCTOS
                            }
                        }
                        (tieneRegalo && progreso < montoRegalo) -> {
                            if (precioRegalo == true) {
                                String.format(getString(R.string.add_order_para_alcanzar_producto), moneySymbol, decimalFormat.format(montoRegalo - progreso))
                            } else {
                                String.format(getString(R.string.add_order_para_alcanzar_regalo), moneySymbol, decimalFormat.format(montoRegalo - progreso))
                            }
                        }
                        (progreso < montoMinimo) -> {
                            String.format(getString(R.string.add_order_para_alcanzar_min), moneySymbol, decimalFormat.format(montoMinimoPedido - progreso))
                        }
                        (progreso == montoMinimo) -> getString(R.string.add_order_alcanzaste_min)
                        ((mEscala != null) && !Belcorp.checkMaxAmmount(mEscala!!.montoHasta?.let { it.toDouble() }
                            ?: 0.0) && progreso < (mEscala!!.montoHasta?.let { it.toDouble() }
                            ?: 0.0)) -> {
                            String.format(getString(
                                when (tieneRegalo) {
                                    true -> R.string.add_order_alcanzaste_regalo_tefalta
                                    else -> R.string.add_order_tefalta
                                }),
                                moneySymbol,
                                decimalFormat.format((mEscala!!.montoHasta?.let { it.toDouble() }
                                    ?: 0.0) - progreso),
                                "${cfg.escalaDescuento?.filterNotNull()!![idxNextEscala].porDescuento}% DSCTO!")
                        }
                        (mEscala != null) ->
                            String.format(getString(
                                when (tieneRegalo) {
                                    true -> R.string.add_order_alcanzaste_regalo_maximo
                                    else -> R.string.add_order_alcanzaste
                                }), "${mEscala!!.porDescuento}%!")
                        else -> ""
                    }
                    /* Animacion */
                    if (mEscala != null) {
                        curPorcen = mEscala?.porDescuento!!.toDouble()
                        val montoIncentivoEvaluacion = if (montoEscala >= montoMinimo) montoEscala else importeTotal

                        if (montoIncentivoEvaluacion >= this.montoIncentivo && tieneIncentico && (lastProgress < this.montoIncentivo) && !tieneRegalo) {
                            FullScreenDialog.Builder(context!!)
                                .withTitle("Llegaste al ", 16F)
                                .withMessage(curPorcen.toInt().toString() + "%" + " " + "Dscto." + " " + " y concursas por el incentivo. ¡Felicidades!", 36F)
                                .withIcon(R.drawable.ic_anima_por)
                                .withScreenDismiss(true)
                                .withAnimation(resources,
                                    FullScreenDialog.SIMPLE_ANIMATION,
                                    ContextCompat.getColor(context!!, R.color.dorado),
                                    ContextCompat.getColor(context!!, R.color.primary))
                                .show()
                            tieneIncentico = false
                        } else if (lastPorcen != null && lastPorcen != curPorcen && curPorcen!!.toInt() > lastPorcen!!.toInt() && (!tieneIncentico || (tieneIncentico && montoEscala >= montoIncentivo))) {
                            FullScreenDialog.Builder(context!!)
                                .withTitle((resources.getString(R.string.add_order_dialog_title)).toString(), 16F)
                                .withMessage(curPorcen.toInt().toString() + "%" + " " + "Dscto.", 36F)
                                .withIcon(R.drawable.ic_anima_por)
                                .withScreenDismiss(true)
                                .withAnimation(resources,
                                    FullScreenDialog.SIMPLE_ANIMATION,
                                    ContextCompat.getColor(context!!, R.color.dorado),
                                    ContextCompat.getColor(context!!, R.color.primary))
                                .show()
                        }

                        lastPorcen = curPorcen

                    } else {
                        lastPorcen = null
                        lastProgreso = null
                    }
                } else if (!Belcorp.checkMaxAmmount(montoMaximoPedido)) {
                    progreso = importeTotal
                    max = if (progreso <= max) max else montoMaximo
                    /* Mensajes */
                    mensaje = when {
                        (tieneRegalo && progreso == 0.0) -> {
                            if (precioRegalo == true) {
                                GlobalConstant.EMPIEZA_AGREGAR_PRODUCTO_NO_REGALO
                            } else {
                                GlobalConstant.EMPIEZA_AGREGAR_PRODUCTOS
                            }
                        }
                        (tieneRegalo && progreso < montoRegalo) -> {
                            if (precioRegalo == true) {
                                String.format(getString(R.string.add_order_para_alcanzar_producto), moneySymbol, decimalFormat.format(montoRegalo - progreso))
                            } else {
                                String.format(getString(R.string.add_order_para_alcanzar_regalo), moneySymbol, decimalFormat.format(montoRegalo - progreso))
                            }
                        }
                        (progreso < montoMinimo) -> {
                            String.format(getString(R.string.add_order_para_alcanzar_min), moneySymbol, decimalFormat.format(montoMinimoPedido - progreso))
                        }
                        (progreso == montoRegalo) -> getString(if (precioRegalo == true) R.string.add_order_alcanzaste_producto else R.string.add_order_alcanzaste_regalo)
                        (progreso == montoMinimo) -> getString(R.string.add_order_alcanzaste_min)
                        (progreso < montoMaximo) -> {
                            String.format(getString(
                                when (tieneRegalo) {
                                    true -> if (precioRegalo == true) R.string.add_order_regalo_solo_puedes_agregar_sin else R.string.add_order_regalo_solo_puedes_agregar
                                    else -> R.string.add_order_solo_puedes_agregar
                                }
                            ), moneySymbol, decimalFormat.format(montoMaximoPedido - progreso))
                        }
                        else -> getString(
                            when (tieneRegalo) {
                                true -> if (precioRegalo == true) R.string.add_order_alcanzaste_max else R.string.add_order_regalo_credit_limit
                                else -> R.string.add_order_alcanzaste_max
                            }
                        )
                    }

                    tieneIncentico = montoMinimo < this.montoIncentivo && this.montoIncentivo >= montoRegalo && this.montoIncentivo < max && lastProgress < this.montoIncentivo && importeTotal >= this.montoIncentivo && tieneIncentico

                }

                //Regalo
                val seekBar = seekbarPuntosBar2.getProgressBar()
                if (progreso <= montoRegalo) max = Math.min(max, montoRegalo)
                seekBar.max = max.toInt()
                seekBar.progress = progreso.toInt()
                seekbarPuntosBar2.hideGift(tieneRegalo)
                if (tieneRegalo) {
                    seekbarPuntosBar2.setGiftPosition(montoRegalo, animacionRegalo)
                    if (progreso > montoRegalo) {
                        if (Belcorp.checkMaxAmmount(montoMaximoPedido) && tieneEscala) {
                            /*Escala de Descuento*/
                            val mTag = "${mNextEscala!!.porDescuento}% DSCTO\n "
                            seekbarPuntosBar2.updateTag(mTag, max.toFloat())
                            mLastEscala?.apply {
                                var desde = this.montoDesde?.let { it.toDouble() } ?: 0.0
                                desde = if (desde > 0.0) desde else mPreLastEscalaHasta
                                val hasta = this.montoHasta?.let { it.toDouble() } ?: 0.0
                                if (montoRegalo in desde..hasta) {
                                    val mTagMin = "M. Mínimo\n ${moneySymbol} ${decimalFormat.format(montoMinimo)}"
                                    seekbarPuntosBar2.addTag(mTagMin, montoMinimo.toFloat())
                                }
                            }
                        } else if (!Belcorp.checkMaxAmmount(montoMaximoPedido)) {
                            /* Limite de Credito */
                            val mTagMin = "M. Mínimo\n $moneySymbol ${decimalFormat.format(montoMinimo)}"
                            seekbarPuntosBar2.updateTag(mTagMin, montoMinimo.toFloat())
                            val mTagMax = "M. Máximo\n $moneySymbol ${decimalFormat.format(max)}"
                            seekbarPuntosBar2.addTag(mTagMax, max.toFloat())
                        }
                    } else {
                        val mTag = "M. Mínimo\n $moneySymbol ${decimalFormat.format(montoMinimo)}"
                        seekbarPuntosBar2.updateTag(mTag, montoMinimo.toFloat())
                    }
                }
                seekbarPuntosBar2.setMessage(mensaje)
            }


            if (!presenter.hasGiftProlError) {
                gestionarAnimacionRegalo(progreso)
            } else {
                //todo
            }
            if (tieneRegalo) {
                presenter.getIsShowedToolTipGift()
            }

            if (tieneIncentico && !Belcorp.checkMaxAmmount(montoMaximoPedido)) {
                if (!tieneRegalo) showAnimacionIncentivo()
                else tieneIncenticoAnim = tieneIncentico
            }

            ordersList.setMoney(moneySymbol, montoRegalo)

        }
    }

    private fun showAnimacionIncentivo() {
        tieneIncenticoAnim = false
        FullScreenDialog.Builder(context!!)
            .withTitle("", 16F)
            .withMessage("Felicidades concursas por el incentivo", 36F)
            .withIcon(R.drawable.ic_anima_por)
            .withScreenDismiss(true)
            .withAnimation(resources,
                FullScreenDialog.SIMPLE_ANIMATION,
                ContextCompat.getColor(context!!, R.color.dorado),
                ContextCompat.getColor(context!!, R.color.primary))
            .show()
    }


    private fun tieneRegaloEnCola(productosDetalle: List<ProductItem?>?): Boolean {
        productosDetalle?.forEach {
            if (it?.isEsPremioElectivo == true)
                return true
        }
        return false
    }


    private fun mostrarGiftAnimacion(tieneRegalo: Boolean) {
        val textoBoton = if (tieneRegalo) GlobalConstant.TEXTO_BOTON_ANIMACION_GIFT_ESCOGIDO else GlobalConstant.TEXTO_BOTON_ANIMACION_GIFT_NO_ESCOGIDO
        FullScreenDialog.Builder(context!!)
            .withTitle((resources.getString(
                if (precioRegalo == true) R.string.felicidades_producto else R.string.felicidades_regalo
            )).toString(), 20F)
            .withMessage((resources.getString(
                if (precioRegalo == true) R.string.escogeremos_por_ti_kit else R.string.escogeremos_por_ti
            )).toString(), 20F)
            .withIcon(R.drawable.ic_regalo)
            .withScreenDismiss(true)
            .withAnimation(resources,
                FullScreenDialog.SIMPLE_ANIMATION,
                ContextCompat.getColor(context!!, R.color.dorado),
                ContextCompat.getColor(context!!, R.color.primary))
            .withButtonMessage(textoBoton)
            .withButtonStyleDefault(false)
            .withVanish(false)
            .withButtonClose(true)
            .withScreenDismiss(false)
            .setOnItemClick(object : FullScreenDialog.FullScreenDialogListener {
                override fun onDismiss() {
                    // EMPTY
                }

                override fun onClickAction(dialog: FullScreenDialog) {
                    // EMPTY
                }

                override fun onClickAceptar(dialog: FullScreenDialog) {
                    dialog.dismiss()
                    openGiftChooseActivity(tieneRegalo)
                }
            })
            .show()
    }

    private fun gestionarAnimacionRegalo(progreso: Double) {

        if (order != null && order!!.muestraRegalo) {
            if (progreso >= montoRegalo) {
                presenter.getIsShowedAnimationGift()
                presenter.setToolTipDeletedGift(false)

            } else {
                presenter.setStatusIsShowingGiftAnimation(false)
                giftPresenter.controlAnimation = 0
            }
        }
    }

    override fun getIsAnimatedShowed(isShowed: Boolean) {
        val tieneRegalo = tieneRegaloEnCola(order!!.productosDetalle)
        if (!isShowed) {
            if (giftPresenter.controlAnimation < 1) {
                giftPresenter.controlAnimation++
                presenter.setToolTipDeletedGift(false)
                presenter.setStatusIsShowingGiftAnimation(true)
                mostrarGiftAnimacion(tieneRegalo)
                if (!tieneRegalo)
                    giftPresenter.autoSaveGift(DeviceUtil.getId(activity))
            } else if (tieneIncenticoAnim) {
                showAnimacionIncentivo()
            }
        } else if (tieneIncenticoAnim) {
            showAnimacionIncentivo()
        }
    }

    override fun getIsShowedToolTip(isShowed: Boolean) {

        if (order != null) {
            if (!isShowed) {
                if (tieneRegaloEnCola(order!!.productosDetalle) && progreso < montoRegalo) {
                    activity?.let {
                        var tool = ProductsToolTipMessage.newInstance()
                        val bundle = Bundle()
                        bundle.putInt("type", 1)
                        var message: String = if (precioRegalo == true) {
                            GlobalConstant.ELIMINAR_PRODUCTO_GIFT_PRO
                        } else {
                            GlobalConstant.ELIMINAR_PRODUCTO_GIFT
                        }
                        bundle.putString(GlobalConstant.TOOL_TIPS_MESSAGE, message
                            .plus(moneySymbol).plus(" ")
                            .plus(montoRegalo.toString()).plus(" ")
                            .plus(GlobalConstant.PARA_OBTENERLO))
                        tool.arguments = bundle
                        tool.show(it.supportFragmentManager, "duo_tooltip")

                        presenter.setToolTipDeletedGift(true)
                        presenter.setStatusIsShowingGiftAnimation(false)
                    }
                }
            }
        }
    }

    override fun onAutoSavedGift() {
        //actualizo la lista de ordenes con el regalo autoelegido
        changes = true
        presenter.getOrder(CallOrderType.FROM_ADD_ITEM)
        presenter.setToolTipDeletedGift(false)

    }

    @SuppressLint("SetTextI18n")
    override fun showConfig(pedidoConfig: PedidoConfigModel?) {
        this.pedidoConfigModel = pedidoConfig
        renderBar(this.pedidoConfigModel, this.order)
    }

    fun isValidEvent(): Boolean {
        val eventTime = Date()
        if ((eventTime.time - lastEventTime.time) < 1000) return false
        lastEventTime = eventTime
        return true
    }

    /**
     *  OrderListListener
     */
    override fun onUpdateItem(item: ProductItem) {
        if (!isValidEvent()) return

        when {
            item.isEsPremioElectivo!! -> {
                val intent = Intent(activity, GiftActivity::class.java)
                intent.putExtra(GlobalConstant.EXTRA_BOOLEANO_GIFT, true)
                if (progreso < montoRegalo)
                    intent.putExtra(GlobalConstant.EXTRA_MONTO, moneySymbol + " " + decimalFormat.format(montoRegalo))
                else
                    intent.putExtra(GlobalConstant.EXTRA_MONTO, "")

                startActivityForResult(intent, GiftActivity.GIFT_RESULT)
            }
            item.isArmaTuPack ?: false -> {
                val atpIntent = Intent(activity, ArmaTuPackActivity::class.java)
                atpIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                atpIntent.putExtra(ArmaTuPackActivity.ATP_WITH_RESULT_EXTRA, true)
                startActivityForResult(atpIntent, ArmaTuPackFragment.ATP_REQUEST_CODE)
            }
            item.flagFestival == FestType.FEST_PREMIO -> {
                val intent = FichaPremioActivity.getCallingIntent(requireContext())
                intent.putExtra(BaseFichaActivity.EXTRA_PRODUCTO, item)
                intent.putExtra(BaseFichaActivity.EXTRA_ACCESS_FROM, BaseFichaActivity.ACCESS_FROM_PASE_PEDIDO)
                intent.putExtra(BaseFichaActivity.EXTRA_FICHA_OFFER_TYPE, CarouselType.DEFAULT)
                intent.putExtra(BaseFichaActivity.EXTRA_KEY_ITEM, item.cuv)
                intent.putExtra(BaseFichaActivity.EXTRA_TYPE_OFFER, GlobalConstant.TYPE_OFFER_FESTIVAL)
                intent.putExtra(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM, OffersOriginType.ORIGEN_PEDIDO)
                intent.putExtra(BaseFichaActivity.EXTRA_TYPE_FICHA, FichaType.PRODUCT_SIMPLE)
                startActivityForResult(intent, FichaPremioActivity.REQUEST_CODE_FICHA_PREMIO)
            }
            !item.tipoOferta.isNullOrEmpty() -> { // Nueva ficha resumida
                this.orderListItemForEdit = item
                val intent = FichaPedidoActivity.getCallingIntent(requireContext())
                intent.putExtra(BaseFichaActivity.EXTRA_PRODUCTO, item)
                intent.putExtra(BaseFichaActivity.EXTRA_PRODUCTO_ORDER, order)
                intent.putExtra(BaseFichaActivity.EXTRA_ACCESS_FROM, BaseFichaActivity.ACCESS_FROM_PASE_PEDIDO)
                intent.putExtra(BaseFichaActivity.EXTRA_FICHA_OFFER_TYPE, CarouselType.DEFAULT)
                intent.putExtra(BaseFichaActivity.EXTRA_KEY_ITEM, item.cuv)
                intent.putExtra(BaseFichaActivity.EXTRA_TYPE_OFFER, item.tipoOferta)
                intent.putExtra(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM, OffersOriginType.ORIGEN_PEDIDO)
                intent.putExtra(BaseFichaActivity.EXTRA_TYPE_FICHA, FichaType.PRODUCT_SIMPLE)
                intent.putExtra(BaseFichaActivity.EXTRA_PROMOTION, item.isPromocion)

                startActivityForResult(intent, BaseFichaActivity.RESULT)

                presenter.initTrack(EVENT_TRACK_VISUALIZAR_PRODUCTO)
            }
            (item.isKitCaminoBrillante != true && item.isEsKitNueva != true) -> { // Antigua ficha resumida
                this.orderListItemForEdit = item
                val intent = Intent(context, ProductActivity::class.java)
                intent.putExtra(ProductActivity.EXTRA_ID_PEDIDO, order?.pedidoID)
                intent.putExtra(ProductActivity.EXTRA_PRODUCTO, item)
                intent.putExtra(ProductActivity.EXTRA_MONEY_SYMBOL, moneySymbol)
                user?.countryISO?.let { intent.putExtra(ProductActivity.EXTRA_COUNTRY_ISO, user?.countryISO) }

                startActivityForResult(intent, ProductActivity.PRODUCT_RESULT)

                presenter.initTrack(EVENT_TRACK_VISUALIZAR_PRODUCTO)
            }

        }

    }

    /**
     *  OrderListListener
     */
    override fun onDeleteItem(item: ProductItem) {
        if (!isValidEvent()) return

        activity?.let {

            if (item.isEsDuoPerfecto!!) {

                var deleteDuo = ProductsToolTipMessage.newInstance()
                val bundle = Bundle()
                bundle.putInt("type", 3)
                deleteDuo.arguments = bundle
                deleteDuo.show(it.supportFragmentManager, "duo_delete")
                deleteDuo.listener = object : ProductsToolTipMessage.DeletePerfectDuo {
                    override fun onClickDeletePerfect() {
                        deletedItem = item
                        presenter.deleteItem(order, item)
                    }

                }

            } else {
                if (!item.isEsPremioElectivo!! || (item.isEsPremioElectivo!! && item.observacionPROLList?.size ?: 0 > 0)) {
                    deleteFragment.setOrderInfo(item.descripcionCortaProd!!)
                    deleteFragment.listener = object : DeleteOrderFragmentListener {
                        override fun onClickDeleteOrder() {
                            presenter.hasGiftProlError = !item.observacionPROL.isNullOrEmpty()
                            deletedItem = item

                            if (item.cuv == KIT_NUEVA_CUV)
                                IS_FROM_KIT_NUEVA = true

                            presenter.deleteItem(order, item)

                            if (item.isArmaTuPack == true) {
                                val deleteATPIntent = Intent(ArmaTuPackActivity.BROADCAST_ATP_ACTION)
                                deleteATPIntent.putExtra(ArmaTuPackActivity.BROADCAST_STATE_ATP_EXTRAS, ArmaTuPackStateType.DELETE)
                                it.sendBroadcast(deleteATPIntent)
                            }

                            FestBroadcast.sendDeleteToCart(it)
                        }
                    }
                    deleteFragment.show(it.supportFragmentManager, "delete_fragment")
                }

            }
        }
    }

    override fun setData(user: User) {
        this.user = user
        this.decimalFormat = CountryUtil.getDecimalFormatByISO(user.countryISO!!, true)
        this.revistaDigitalSuscripcion = user.revistaDigitalSuscripcion!!
        this.moneySymbol = user.countryMoneySymbol!!
        this.consultantName = user.consultantName!!
        this.montoMinimoPedido = user.montoMinimoPedido!!
        this.montoMaximoPedido = user.montoMaximoPedido!!

        listener?.setCampaign(user.campaing!!)

        checkSuscrita()

        changeButtonStatus(false, user.isDayProl)

        context?.let {
            totalFragment.setButtonText(user.isDayProl, it)
        }

        ordersList.setDecimalFormat(decimalFormat, moneySymbol, "", OrdersListAdapter.TYPE_PRODUCT)
        ordersGroup.setDecimalFormat(decimalFormat, moneySymbol, consultantName)

        presenter.getKits(user)
        if (user.isMostrarBuscador == true) {
            (activity as AddOrdersActivity).showSearchOption()
        }

    }

    @SuppressLint("SetTextI18n")
    override fun setPedidosPendientes(data: ArrayList<UserConfigData?>?) {
        data?.forEach {
            if (it?.code.equals(DigitalCatalogConfigCode.ORDERS)) {
                btnPedidosPorAprobar.text = "Pedidos por aprobar (${it?.value1?.toInt()})"
                pedidosPorAprobar = it?.value1?.toInt() ?: 0
                if (pedidosPorAprobar > 0) btnPedidosPorAprobar.visibility = View.VISIBLE
                else btnPedidosPorAprobar.visibility = View.GONE
            }
        } ?: kotlin.run {
            btnPedidosPorAprobar.visibility = View.GONE
        }
    }

    override fun onError(errorModel: ErrorModel) {
        processGeneralError(errorModel)
    }

    override fun onOrderError(errorModel: BooleanDtoModel) {
        showFullScreenError("ERROR", errorModel.message)
    }

    override fun trackBackPressed(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_ORDERS_GPR, model)
    }

    override fun onOrderReserved(data: ReserveResponseModel?, message: String, flagOfertaFinal: Boolean, configuracionPremio: ConfiguracionPremio?) {
        hideLoading()

        if (this.flagOfertaFinal && configuracionPremio?.showMessage ?: false && !this.hasConsultoraOfertaFinal) {
            showOfertaFinalPopUp(configuracionPremio)
            showPremioLandingAccess(0)
            data?.montoTotal?.let { presenter.saveOfertaFinal(it.toDouble()) }
        } else {
            order?.isDiaProl?.let {
                var email = ""
                user?.let { it1 ->
                    if (!it1.correoPendiente.isNullOrEmpty()) {
                        email = it1.correoPendiente ?: ""
                    } else if (!it1.email.isNullOrEmpty()) {
                        email = it1.email ?: ""
                    }
                }

                if (data?.reserva == true) listener?.onOrderReserved(!it, presenter.updateMailOF, email)
                else {
                    val type = if (it) getString(R.string.add_order_reserved) else getString(R.string.add_order_saved)

                    AnalyticsAddOrder.buttonSaveReserve(GlobalConstant.EVENT_LABEL_GUARDAR_PEDIDO)

                    if (presenter.updateMailOF) {
                        showReservedFullScreen(type, message)
                    } else {
                        showFullScreenError(type, message, R.drawable.ic_hands)
                    }
                }
            }
        }

    }

    private fun showPremioLandingAccess(estadoPremio : Int) {
        btnAccesoPremioLanding?.visibility = View.VISIBLE
        updatePremioLandingAccess(estadoPremio)

    }


    private fun updatePremioLandingAccess(state: Int) {
        when (state) {
            PremioEstadoType.BLOCKED -> blockPremio()
            PremioEstadoType.ALLOWED -> allowedPremio()
            PremioEstadoType.CHOOSED -> choosedPremio()
        }
    }

    fun blockPremio() {
        btnAccesoPremioLanding?.apply {
            this.setTitle(getString(R.string.final_offer_landing_blocked))
            this.updateIconImage(R.drawable.ic_candado_of)
            this.updatePicImage(Empty)
            this.showIconImage(true)
        }
    }

    fun allowedPremio() {
        btnAccesoPremioLanding?.apply {
            this.setTitle(getString(R.string.final_offer_landing_allowed))
            this.updatePicImage(Empty)
            this.showIconImage(false)
        }
    }

    fun choosedPremio() {
        btnAccesoPremioLanding?.apply {
            this.setTitle(getString(R.string.final_offer_landing_chosed))
            this.updatePicImage(Empty)
            this.showIconImage(false)
        }
    }


    protected fun showReservedFullScreen(title: String, message: String) {
        if (isVisible) {
            context?.let {

                ReservedDialog.Builder(it).withStatusTitle(title).withMessage(message).withEmail(user?.email
                    ?: user?.correoPendiente).setOnAccept(object : ReservedDialog.ReservedDialogListener {

                    override fun updateEmail(dialog: ReservedDialog, email: String) {
                        reservedDialog = dialog
                        presenter.updateConsultEmail(email, true)
                    }

                    override fun closeDialog(dialog: ReservedDialog) {
                        dialog.dismiss()
                    }

                }).show()


            }
        }
    }

    override fun onOrderError(message: String, orderReserved: OrderModel?) {

        var tipo: String?

        order?.isDiaProl?.let {

            tipo = if (it) "Reservar" else "Guardar"
            showFullScreenError("NO SE PUDO " + tipo?.toUpperCase() + " PEDIDO", message)

        }

        orderReserved?.let { t ->
            order?.let {
                it.productosDetalle = t.productosDetalle
                it.clientesDetalle = t.clientesDetalle
            }

            this.clientsDetalle = t.clientesDetalle
        }

        order?.let {
            ordersList.setData(order?.productosDetalle)
            ordersGroup.setData(order, clientsDetalle)
        }

        updateTab()
    }

    fun trackBackPressed() {
        presenter.trackBackPressed()
    }

    override fun onFormattedOrderReceived(order: OrderModel?,
                                          clientModelList: List<ClienteModel?>?,
                                          callFrom: Int) {
        this.order = order
        this.clientsDetalle = clientModelList
        ordersList.setPrecioRegalo(this.order?.precioRegalo)
        this.precioRegalo = this.order?.precioRegalo

        selectedCuv?.let { sele ->
            addedItem = order?.productosDetalle?.first {
                it?.cuv == sele
            }?.copy()

            addedItem?.cantidad = selectetCuvQt

            addedItem?.let {
                presenter.initTrack(EVENT_TRACK_AGREGAR_PRODUCTO)
            }
            selectedCuv = null
            selectetCuvQt = 0
        }

        this.order?.let {

            changeButtonStatus(order?.pedidoValidado ?: false, order?.isDiaProl ?: false)

            if (user?.isUltimoDiaFacturacion == true && order?.pedidoValidado == true) {
                activity?.finish()
                val intent = Intent(activity, OrderDetailActivity::class.java)
                startActivity(intent)
            } else {

                onUpdateTotal(callFrom)
            }
        } ?: run {
            onUpdateTotal(callFrom)
        }

    }


    private fun changeButtonStatus(validado: Boolean, diaProl: Boolean) {
        // status = true (validado)  | status = false (no validado)

        var btnText: String

        if (!diaProl) {
            user?.let { listener?.setCampaign(it.campaing ?: "") }
            btnText = resources.getString(R.string.add_order_button_guardar)
            context?.let { context -> totalFragment.setButtonText(diaProl, context) }
        } else if (validado) {
            listener?.setActivityTitle(resources.getString(R.string.add_order_reservado_title))
            btnText = resources.getString(R.string.add_order_button_modificar)
            context?.let { context -> totalFragment.setButtonText(null, context) }
        } else {
            user?.let { listener?.setCampaign(it.campaing ?: "") }
            btnText = resources.getString(R.string.add_order_button_reservar)
            context?.let { context -> totalFragment.setButtonText(diaProl, context) }
        }

        orderTotalComponent.setTextButton(btnText)
    }

    override fun onBackOrder(item: ProductItem) {
        presenter.backOrder(item.cuv, order?.pedidoID, item.id, DeviceUtil.getId(context))
    }

    private fun onUpdateTotal(callFrom: Int) {

        presenter.config(callFrom)
        val orderTotalModel = OrderTotalModel()
        var itemGanancia: GananciaItemModel
        val listGanancia = ArrayList<GananciaItemModel>()

        if (order != null) {
            order?.let {
                changeButtonStatus(it.pedidoValidado, it.isDiaProl ?: false)
            }

            orderTotalModel.isPagoContado = order?.isPagoContado
            orderTotalModel.total = String.format(getString(R.string.add_order_params), moneySymbol, decimalFormat.format(order?.importeTotalDescuento))
            orderTotalModel.totalSinDesc = String.format(getString(R.string.add_order_params), moneySymbol, decimalFormat.format(order?.importeTotal))
            orderTotalModel.descNivel = String.format(getString(R.string.add_order_params), moneySymbol, decimalFormat.format(order?.descuentoProl))
            orderTotalModel.ganancia = String.format(getString(R.string.add_order_params), moneySymbol, decimalFormat.format(order?.gananciaEstimada))
            orderTotalModel.misDescuentos = String.format(getString(R.string.add_order_params), moneySymbol, decimalFormat.format(order?.montoDescuentoSIC
                ?: BigDecimal.ZERO))
            orderTotalModel.gastosTransporte = String.format(getString(R.string.add_order_params), moneySymbol, decimalFormat.format(order?.montoFleteSIC
                ?: BigDecimal.ZERO))
            orderTotalModel.deudaAnterior = String.format(getString(R.string.add_order_params), moneySymbol, decimalFormat.format(order?.montoDeudaAnteriorSIC
                ?: BigDecimal.ZERO))
            orderTotalModel.pagoContado = String.format(getString(R.string.add_order_params), moneySymbol, decimalFormat.format(order?.montoPagoContadoSIC
                ?: BigDecimal.ZERO))

            order?.gananciaDetalle?.let {
                it.forEach { it2 ->
                    itemGanancia = GananciaItemModel(it2?.descripcion, String.format(getString(R.string.add_order_params), moneySymbol, decimalFormat.format(it2?.montoGanancia)))
                    listGanancia.add(itemGanancia)
                }
                orderTotalModel.listGanancia = listGanancia
            }

            orderTotalComponent.setModel(orderTotalModel)

            totalFragment.setQuantities(orderTotalModel.total.toString(), orderTotalModel.descNivel.toString(), orderTotalModel.totalSinDesc.toString(), orderTotalModel.ganancia.toString())

            if (order?.productosDetalle == null || order?.productosDetalle!!.isEmpty()) {

                tvwListarPorProductoCounter.text = resources.getString(R.string.add_order_counter, 0)
                tvwListarPorProducto.text = resources.getQuantityString(R.plurals.add_order_producto, 0, 0)

                tvwListarPorClienteCounter.text = resources.getString(R.string.add_order_counter, 0)
                tvwListarPorCliente.text = resources.getQuantityString(R.plurals.add_order_client, 0, 0)

                tvwNoProducts.visibility = View.GONE
                tvwPendingOrders.visibility = View.GONE
                lltProgress.visibility = View.VISIBLE

                productsNotFounds(order?.muestraRegalo ?: false)
            } else {
                tvwListarPorProductoCounter.text = resources.getString(R.string.add_order_counter, order?.cantidadProductos)
                tvwListarPorProducto.text = resources.getQuantityString(R.plurals.add_order_producto, order?.cantidadProductos!!, order?.cantidadProductos)

                if (callFrom != CallOrderType.FROM_ADD_OFFER) {
                    val tab = tabLayout.getTabAt(TAB_PEDIDO)
                    tab?.select()
                }

                tvwNoProducts.visibility = View.GONE
                tvwPendingOrders.visibility = View.GONE
                lltProgress.visibility = View.VISIBLE

                if (order!!.precioPorNivel) tvwDisclaimer.visibility = View.VISIBLE
                else tvwDisclaimer.visibility = View.GONE

                order?.clientesDetalle?.setClientQuantity()
            }

            updateCartCount()

            ordersList.setData(order?.productosDetalle)
            ordersGroup.isReservation = false
            ordersGroup.setData(order, clientsDetalle)
        } else {

            changeButtonStatus(false, user?.isDayProl ?: false)

            productsNotFounds(false)
            tvwListarPorCliente.text = resources.getQuantityString(R.plurals.add_order_client, 0, 0)
            tvwListarPorClienteCounter.text = resources.getString(R.string.add_order_counter, 0)
            tvwListarPorProducto.text = resources.getQuantityString(R.plurals.add_order_producto, 0, 0)
            tvwListarPorProductoCounter.text = resources.getString(R.string.add_order_counter, 0)

            orderTotalModel.total = String.format(getString(R.string.add_order_params), moneySymbol, decimalFormat.format(0))
            orderTotalModel.totalSinDesc = String.format(getString(R.string.add_order_params), moneySymbol, decimalFormat.format(0))
            orderTotalModel.descNivel = String.format(getString(R.string.add_order_params), moneySymbol, decimalFormat.format(0))
            orderTotalModel.ganancia = String.format(getString(R.string.add_order_params), moneySymbol, decimalFormat.format(0))

            order?.gananciaDetalle?.let {
                it.forEach { it2 ->
                    itemGanancia = GananciaItemModel(it2?.descripcion, String.format(getString(R.string.add_order_params), moneySymbol, decimalFormat.format(it2?.montoGanancia)))
                    listGanancia.add(itemGanancia)
                }
                orderTotalModel.listGanancia = listGanancia
            }

            orderTotalComponent.setModel(orderTotalModel)

            totalFragment.setQuantities(orderTotalModel.total.toString(), orderTotalModel.descNivel.toString(), orderTotalModel.totalSinDesc.toString(), orderTotalModel.ganancia.toString())

        }

        totalFragment.listener = this

        updateTab()
    }

    private fun updateCartCount() {
        activity?.let {
            val sm = SessionManager.getInstance(it)
            order?.cantidadProductos?.let { it2 ->
                sm.saveOffersCount(it2)

                // Send broadcast to home activity
                val countIntent = Intent(BROADCAST_COUNT_ACTION)
                if (IS_FROM_KIT_NUEVA != null) {

                    KIT_NUEVA_CUV = null
                    countIntent.putExtra(KIT_NUEVA_BR_KEY, true)

                }
                it.sendBroadcast(countIntent)
                IS_FROM_KIT_NUEVA = null
            }
        }
    }

    private fun productsNotFounds(tieneRegalo: Boolean) {
        if (!tieneRegalo) {
            tvwNoProducts.visibility = View.VISIBLE
            lltProgress.visibility = View.GONE
        } else {
            tvwNoProducts.visibility = View.GONE
            lltProgress.visibility = View.VISIBLE
        }
        tvwEliminar.visibility = View.GONE
        tvwDisclaimer.visibility = View.GONE
    }

    private fun updateTab() {
        val tfLight = Typeface.createFromAsset(activity?.assets, GlobalConstant.LATO_LIGHT_SOURCE)
        val tfBold = Typeface.createFromAsset(activity?.assets, GlobalConstant.LATO_BOLD_SOURCE)

        var withTracker = true

        if (clienteShow) {

            val spannableCliente = Spannable.Factory.getInstance().newSpannable(tvwListarPorCliente.text.toString())
            spannableCliente.setSpan(CustomTypefaceSpan(tfBold), 0, tvwListarPorCliente.text.toString().length, 0)
            tvwListarPorCliente.text = spannableCliente
            tvwListarPorCliente.setTextColor(Color.BLACK)
            tvwListarPorClienteCounter.setTextColor(Color.BLACK)

            val spannableProducto = Spannable.Factory.getInstance().newSpannable(tvwListarPorProducto.text.toString())
            spannableProducto.setSpan(CustomTypefaceSpan(tfLight), 0, tvwListarPorProducto.text.toString().length, 0)
            spannableProducto.setSpan(UnderlineSpan(), 0, spannableProducto.length, 0)
            tvwListarPorProducto.text = spannableProducto
            tvwListarPorProducto.setTextColor(Color.GRAY)
            tvwListarPorProductoCounter.setTextColor(Color.GRAY)

            if (lastSelectClientUnits == TAB_CLIENTES) withTracker = false

            lastSelectClientUnits = TAB_CLIENTES

            onClickClient()

        } else {

            val spannableCliente = Spannable.Factory.getInstance().newSpannable(tvwListarPorCliente.text.toString())
            spannableCliente.setSpan(CustomTypefaceSpan(tfLight), 0, tvwListarPorCliente.text.toString().length, 0)
            spannableCliente.setSpan(UnderlineSpan(), 0, spannableCliente.length, 0)
            tvwListarPorCliente.text = spannableCliente
            tvwListarPorCliente.setTextColor(Color.GRAY)
            tvwListarPorClienteCounter.setTextColor(Color.GRAY)


            val spannableProducto = Spannable.Factory.getInstance().newSpannable(tvwListarPorProducto.text.toString())
            spannableProducto.setSpan(CustomTypefaceSpan(tfBold), 0, tvwListarPorProducto.text.toString().length, 0)
            tvwListarPorProducto.text = spannableProducto
            tvwListarPorProducto.setTextColor(Color.BLACK)
            tvwListarPorProductoCounter.setTextColor(Color.BLACK)

            if (lastSelectClientUnits == TAB_UNIDADES) withTracker = false

            lastSelectClientUnits = TAB_UNIDADES

            onClickProduct()
        }
        if (withTracker) presenter.initTrack(EVENT_TRACK_CLICK_PESTANA_CLIENTE_PRODUCTO)
    }

    @SuppressLint("SetTextI18n")
    fun onComplete(clienteModel: ClienteModel) {
        this.clienteModel = clienteModel
        clientID = clienteModel.clienteID
        clientLocalID = clienteModel.id
        tvwClientFilter.text = clienteModel.nombres + if (clienteModel.apellidos != null) " " + clienteModel.apellidos else ""
        AnalyticsAddOrder.buttonSelectClient()
    }

    override fun onOffersReceived(offers: List<EstrategiaCarrusel>) {
        this.offersDataList = offers
        flagTieneDuo = buscarTieneDuoEnOfertas()
        if (user?.bloqueoPendiente ?: false) {
            presenter.getPedidosPendientesStart()
        } else {
            if (flagTieneDuo && !isShowedDuo) {
                presenter.searchPopUpDuoPerfecto()
            }

        }

        if (pedidosPorAprobar == 0) {
            if (order != null) {
                if (order?.productosDetalle == null || order?.productosDetalle!!.isEmpty()) {
                    val tab = tabLayout.getTabAt(TAB_OFERTAS)
                    tab?.select()
                }
            } else {
                val tab = tabLayout.getTabAt(TAB_OFERTAS)
                tab?.select()
            }
        }

        val sello = Sello(requireContext())
        sello.setText(getString(R.string.tag_festival))
        sello.setTextSize(requireContext().resources.getDimension(R.dimen.fest_text))
        sello.setType(Sello.FESTIVAL)
        sello.setStartColor(ContextCompat.getColor(requireContext(), R.color.gana_mas_festival_start))
        sello.setEndColor(ContextCompat.getColor(requireContext(), R.color.gana_mas_festival_end))
        sello.setOrientation(3)
        sello.setWidthLayout(requireContext().resources.getDimension(R.dimen.fest_widht).toInt())
        sello.setHeightLayout(requireContext().resources.getDimension(R.dimen.fest_height).toInt())

        configFest?.Sello?.let { config ->
            config.selloTexto.let { if (!it.isNullOrBlank()) sello.setText(it) }
            config.selloColorTexto?.let {
                if (StringUtil.isHexColor(it)) {
                    sello.setTextColor(Color.parseColor(it))
                }
            }

            safeLet(config.selloColorInicio, config.selloColorFin, config.selloColorDireccion) { startColor, endColor, orientation ->
                sello.setBackgroundSello(startColor, endColor, orientation)
            }
        }

        val stampList = mutableListOf<Sello>()
        stampList.add(sello)
        carouselOffers.setStamp(stampList)

        carouselOffers.carouselListener = object : Carousel.CarouselListener {

            override fun bindMulti(multiItem: Multi, position: Int) {
                // not implement
            }

            override fun pressedBanner(pos: Int) {
                // not implement
            }

            override fun pressedItem(keyItem: String, marcaID: Int, marca: String, pos: Int) {
                offersDataList?.let { list ->
                       list[pos]?.let { item ->
                           val sectionDesc = getOfferTypeForAnalytics(item.tipoPersonalizacion)

                           val onAnalyticsLoaded: (descriptionPalanca: String) -> Unit = { descriptionPalanca ->
                               AnalyticsAddOrder.clickOffer(user?.countryISO, item, nameListCarouselTracker, descriptionPalanca)
                           }

                           presenter.getAnalytics(sectionDesc, onAnalyticsLoaded)

                           goToFicha(item)
                       }
                   }
            }

            override fun pressedItemButtonAdd(keyItem: String, quantity: Int, counterView: Counter, pos: Int, multi: Multi) {
                agregarPedidoDesdeCarrusel(quantity, keyItem, counterView, OfferTypes.OPT)
            }

            override fun pressedItemButtonSelection(keyItem: String, marcaID: Int, marca: String, pos: Int) {
                offersDataList?.let { list ->
                      list[pos]?.let { item ->
                          goToFicha(item)
                      }
                  }
            }

            override fun pressedItemButtonShowOffer(keyItem: String, marcaID: Int, marca: String, pos: Int) {
                // not implement
            }

            override fun pressedButtonMore(pos: Int) {
                verMas()
            }

            override fun pressedButtonDuo(pos: Int) {
                goToBannerDuo()
            }

            override fun viewBanner(pos: Int) {
                // not implement
            }

            override fun impressionItems(list: ArrayList<OfferModel>) {
                val listResult = offersDataList?.filter { item -> list.any { it.key == item?.cuv } }

                val onAnalyticsLoaded: (list: List<EstrategiaCarrusel?>) -> Unit = { listEstrategia ->
                    AnalyticsAddOrder.impressionDigitalOffers(user?.countryISO, listEstrategia.filterNotNull(), nameListCarouselTracker)
                }

                presenter.getAnalyticsCarouselOPM(listResult, onAnalyticsLoaded)
            }

        }

        offersDataList?.filterNotNull()?.let {
            carouselOffers.updateImagePlaceHolder(resources.getDrawable(R.drawable.ic_container_placeholder))
            carouselOffers.carouselItems = it.size
            if (user?.isRDEsSuscrita == true) {
                carouselOffers.showMoreCard(true)
            }
            carouselOffers.updateList(transformOffersList(OfferTypes.OPT, it))
        }

    }

    override fun setOffersRecomended(offers: List<Oferta>) {
        this.ofertas.clear()
        this.ofertas.addAll(offers)

        if (pedidosPorAprobar == 0) {
            if (order != null) {
                if (order?.productosDetalle == null || order?.productosDetalle!!.isEmpty()) {
                    val tab = tabLayout.getTabAt(TAB_OFERTAS)
                    tab?.select()
                }
            } else {
                val tab = tabLayout.getTabAt(TAB_OFERTAS)
                tab?.select()
            }
        }

        val sello = Sello(requireContext())
        sello.setText(getString(R.string.tag_festival))
        sello.setTextSize(requireContext().resources.getDimension(R.dimen.fest_text))
        sello.setType(Sello.FESTIVAL)
        sello.setStartColor(ContextCompat.getColor(requireContext(), R.color.gana_mas_festival_start))
        sello.setEndColor(ContextCompat.getColor(requireContext(), R.color.gana_mas_festival_end))
        sello.setOrientation(3)
        sello.setWidthLayout(requireContext().resources.getDimension(R.dimen.fest_widht).toInt())
        sello.setHeightLayout(requireContext().resources.getDimension(R.dimen.fest_height).toInt())

        configFest?.Sello?.let { config ->
            config.selloTexto.let { if (!it.isNullOrBlank()) sello.setText(it) }
            config.selloColorTexto?.let {
                if (StringUtil.isHexColor(it)) {
                    sello.setTextColor(Color.parseColor(it))
                }
            }

            safeLet(config.selloColorInicio, config.selloColorFin, config.selloColorDireccion) { startColor, endColor, orientation ->
                sello.setBackgroundSello(startColor, endColor, orientation)
            }
        }

        val stampList = mutableListOf<Sello>()
        stampList.add(sello)
        carouselOffers.setStamp(stampList)

        carouselOffers.carouselListener = object : Carousel.CarouselListener {

            override fun bindMulti(multiItem: Multi, position: Int) {
                // not implement
            }

            override fun pressedBanner(pos: Int) {
                // not implement
            }

            override fun pressedItem(keyItem: String, marcaID: Int, marca: String, pos: Int) {
                ofertas?.let { list ->
                    list[pos]?.let { item ->
                        goToFicha(item)
                    }
                }
            }

            override fun pressedItemButtonAdd(keyItem: String, quantity: Int, counterView: Counter, pos: Int, multi: Multi) {
                agregarPedidoDesdeCarrusel(quantity, keyItem, counterView, OfferTypes.ALG)
            }

            override fun pressedItemButtonSelection(keyItem: String, marcaID: Int, marca: String, pos: Int) {
                ofertas?.let { list ->
                    list[pos]?.let { item ->
                          goToFicha(item)
                    }
                }
            }

            override fun pressedItemButtonShowOffer(keyItem: String, marcaID: Int, marca: String, pos: Int) {
                // not implement
            }

            override fun pressedButtonMore(pos: Int) {
                verMas()
            }

            override fun pressedButtonDuo(pos: Int) {
                goToBannerDuo()
            }

            override fun viewBanner(pos: Int) {
                // not implement
            }

            override fun impressionItems(list: ArrayList<OfferModel>) {

            }

        }

        ofertas?.let {
            carouselOffers.updateImagePlaceHolder(resources.getDrawable(R.drawable.ic_container_placeholder))
            carouselOffers.carouselItems = it.size
            if (user?.isRDEsSuscrita == true) {
                carouselOffers.showMoreCard(true)
            }
            carouselOffers.updateList(transformOffersRecomended(it))
        }
    }

    private fun goToFicha(item: Oferta) {
        activity?.let { mContext ->
            if (!NetworkUtil.isThereInternetConnection(context())) {
                showNetworkError()
            } else {
                val extras = Bundle()
                extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, item.cuv)
                extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, item.tipoOferta)
                item.marcaID?.let { extras.putInt(BaseFichaActivity.EXTRA_MARCA_ID, it) }
                extras.putString(BaseFichaActivity.EXTRA_MARCA_NAME, item.nombreMarca)
                extras.putInt(BaseFichaActivity.EXTRA_ACCESS_FROM, BaseFichaActivity.ACCESS_FROM_LANDING_OFERTA_FINAL)
                val intentToLaunch = FichaProductoActivity.getCallingIntent(mContext)
                intentToLaunch.putExtras(extras)
                mContext.startActivityForResult(intentToLaunch, BaseFichaActivity.ACCESS_FROM_PEDIDO_GANASMAS_ADDED_FICHA)
            }
        }
    }

     private fun goToFicha(item: EstrategiaCarrusel) {
         activity?.let { mContext ->
             if (!NetworkUtil.isThereInternetConnection(context())) {
                 showNetworkError()
             } else {
                 val extras = Bundle()
                 extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, item.cuv)
                 extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, item.tipoPersonalizacion)
                 item.marcaID?.let { extras.putInt(BaseFichaActivity.EXTRA_MARCA_ID, it) }
                 extras.putString(BaseFichaActivity.EXTRA_MARCA_NAME, item.descripcionMarca)
                 extras.putInt(BaseFichaActivity.EXTRA_ACCESS_FROM, BaseFichaActivity.ACCESS_FROM_PEDIDO_GANASMAS)
                 extras.putString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB, item.origenPedidoWebFicha.toString())
                 val intentToLaunch = FichaProductoActivity.getCallingIntent(mContext)
                 intentToLaunch.putExtras(extras)
                 mContext.startActivityForResult(intentToLaunch, BaseFichaActivity.ACCESS_FROM_PEDIDO_GANASMAS_ADDED_FICHA)
             }
         }
     }

    private fun goToBannerDuo() {
        Tracker.trackEvent(
            GlobalConstant.SCREEN_NAME_INGRESAR_PEDIDO,
            GlobalConstant.EVENT_CATEGORY_PEDIDO,
            GlobalConstant.EVENT_ACTION_BANNER_DUO_PERFECTO,
            getString(R.string.add_order_banner_duo_perfecto),
            GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
            loginModel
        )
        val intent = Intent(activity, PerfectDuoActivity::class.java)
        intent.putExtra(PerfectDuoActivity.RESULT_REQUEST_KEY, "1")
        startActivityForResult(intent, PerfectDuoActivity.RESULT_REQUEST_CODE)
    }

    private fun goToFest() {
        activity?.let { mContext ->
            if (!NetworkUtil.isThereInternetConnection(context())) {
                showNetworkError()
            } else {
                val extras = Bundle()
                val intentToLaunch = FestActivity.getCallingIntent(mContext)
                intentToLaunch.putExtras(extras)
                startActivityForResult(intentToLaunch, FestActivity.RESULT)
            }
        }
    }

    override fun setOfferTitle(title: String) {

        val metrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(metrics)

        if (metrics.density <= 1.5) {
            if (title.contains(" ")) {
                if (title.length > 13) tvwOfertas.text = title.replace(" ", "\n")
                    .toUpperCase()
                else tvwOfertas.text = title
            } else {
                tvwOfertas.text = title
            }
        } else {
            tvwOfertas.text = title
        }

    }

    override fun setFinalOfferList(data: OfertaFinalResponseModel?, reserve: ReserveResponseModel?) {

        val intent = Intent(activity, FinalOfferActivity::class.java)

        val bundle = Bundle()
        bundle.putParcelable(FinalOfferActivity.EXTRA_ORDER_RESERVE_RESULT, reserve)
        bundle.putParcelable(FinalOfferActivity.EXTRA_FINAL_OFFER_HEADER, data?.ofertaFinalHeader)
        bundle.putParcelableArrayList(FinalOfferActivity.EXTRA_FINAL_OFFER_LIST, data?.productosOfertaFinal
            as java.util.ArrayList<out Parcelable>)
        bundle.putString(FinalOfferActivity.EXTRA_ORDER_IMPORT_TOTAL, order?.importeTotal.toString())
        bundle.putDouble(FinalOfferActivity.EXTRA_MONTO_REGALO, montoRegalo)
        bundle.putBoolean(FinalOfferActivity.EXTRA_REGALO_ELEGIDO, !animacionRegalo)

        order?.muestraRegalo?.let {
            bundle.putBoolean(FinalOfferActivity.EXTRA_REGALO_TIENEREGALO, it)
        }
        intent.putExtras(bundle)
        startActivity(intent)
        activity?.finish()
    }

    override fun openActivityGift() {
        Tracker.EligeTuRegalo.clickGift()
        openGiftChooseActivity(tieneRegaloEnCola(order!!.productosDetalle))
    }

    fun openGiftChooseActivity(flagMensjeBotones: Boolean = true) {
        val intent = Intent(activity, GiftActivity::class.java)
        intent.putExtra(GlobalConstant.EXTRA_BOOLEANO_GIFT, flagMensjeBotones)
        if (progreso < montoRegalo)
            intent.putExtra(GlobalConstant.EXTRA_MONTO, moneySymbol + " " + decimalFormat.format(montoRegalo))
        else
            intent.putExtra(GlobalConstant.EXTRA_MONTO, "")

        startActivityForResult(intent, GiftActivity.GIFT_RESULT)
    }

    override fun showOffers(nativo: Boolean) {
        if (nativo) {
            listener?.goToOffers()
        } else {
            val intent = Intent(context, OffersActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(OffersActivity.OPTION, PageUrlType.OFFERS)
            startActivityForResult(intent, OffersActivity.RESULT)
        }
    }

    override fun showDetailOptionReceiver(dni: String, nameReceiver: String) {
        val isActive = dni.isNotEmpty() && nameReceiver.isNotEmpty()
        receiveOrderSwitch.isChecked = isActive
        nameReceiverText.text = nameReceiver
        dniReceiverText.text = dni
        receiveDetailLinear.visibility = if (isActive) View.VISIBLE else View.GONE
        initEnterDialog()
    }

    override fun showOptionReceiver() {
        receiveLinear.visibility = View.VISIBLE
    }

    override fun onCloseEnterDniDialog() {
        receiveOrderSwitch.isChecked = false
        nameReceiverText.text = ""
        dniReceiverText.text = ""
    }

    override fun onAcceptDni(dni: String, nameCollect: String) {
        presenter.updateDni(true, dni, nameCollect)
    }

    override fun onFinishUpdateDni(dni: String, nameCollect: String) {
        nameReceiverText.text = nameCollect
        dniReceiverText.text = dni
        receiveDetailLinear.visibility = View.VISIBLE
    }

    override fun updateDniSuccessful() {
        enterDniDialog?.showUpdateDniSuccessful()
    }

    override fun disabledReceiverOption() {
        receiveDetailLinear.visibility = View.GONE
        nameReceiverText.text = ""
        dniReceiverText.text = ""
        enterDniDialog?.isUpdate = false
    }

    override fun onMensajeProl(mensajes: Collection<MensajeProl?>?) {
        (activity as AddOrdersActivity).mensajeProl(mensajes)
    }

    override fun onMensajeProl(image:String, message: String, productCUV: ProductCUV) {
        MensajeProlUtil.showAgregarProductoProl(activity as Context,image,message,productCUV,this)
    }


    override fun agregarProducto(productCUV: ProductCUV) {

        presenter.insertarUnicoProducto(productCUV)

    }

    override fun onCerrarDialog() {
        presenter.genericOrderResult()
    }

    override fun onError(message: String) {
        context?.let { ToastUtil.show(it, message, Toast.LENGTH_SHORT) }

    }

    override fun onEmailUpdated(newEmail: String?) {
        showEmailConfirmation(newEmail)

    }

    fun emailUpdate(newEmail: String?, additionallistener: AdditionalOrderListener? = null) {

        newEmail?.let {
            this.additionalOrderlistener = additionallistener
            presenter.updateConsultEmail(it, true)
        }

    }

    fun showEmailConfirmation(newEmail: String?) {
        context?.let { it ->
            newEmail?.let { it2 ->
                reservedDialog?.hideLoading()
                reservedDialog?.dismiss()
                ConfirmUpdateEmailDialog.Builder(it).withEmail(it2).setOnDismissListener(object : ConfirmUpdateEmailDialog.ConfirmUpdateEmailDialogListener {
                    override fun close(dialog: ConfirmUpdateEmailDialog) {
                        dialog.dismiss()
                        additionalOrderlistener?.onClose()
                    }

                    override fun resendEmail(email: String) {
                        showLoadingDialog()
                        presenter.updateConsultEmail(email, false)
                    }
                }).show()
            }
        }
    }

    override fun showPedidosPendientesBloqueante(pedidos: Int?) {
        if (pedidos ?: 0 > 0) showPendingOrdersButton(pedidos)

        activity?.let { acti ->
            PedidosPendientesNoDissmisableDialog(acti, pedidos, object : PedidosPendientesNoDissmisableDialog.UpdateMailListener {
                override fun goToPendingOrders() {
                    goPedidosPorAprobar()
                    acti.finish()
                }

                override fun onIgnorarPedidos() {
                    presenter.reservar(order, flagOfertaFinal)
                }

                override fun onCloseDialog() {

                }
            }).show()
        }
    }

    override fun showPedidosPendientesNoBloqueante(pedidos: Int?) {
        if (pedidos ?: 0 > 0) showPendingOrdersButton(pedidos)

        activity?.let { act ->
            PedidosPendientesDissmisableDialog(act, pedidos, true, object : PedidosPendientesDissmisableDialog.PedidosPendientesListener {
                override fun goToPendingOrders() {
                    goPedidosPorAprobar()
                }

                override fun onCancelDialog() {
                    presenter.reservar(order, flagOfertaFinal)
                }

                override fun onCloseDialog() {
                    // not implement
                }
            }).show()
        }

    }

    override fun showPedidosPendientesOnStart(pedidos: Int?) {
        if (flagTieneDuo && !isShowedDuo) {

            if (totalDuoAgregadoListaPedido() < 2)
                presenter.searchPopUpDuoPerfecto()
            else {
                mostrarPedidosPendientesOnStart(pedidos)
            }

        } else {
            mostrarPedidosPendientesOnStart(pedidos)
        }

    }

    private fun mostrarPedidosPendientesOnStart(pedidos: Int?) {
        if ((arguments?.containsKey(HomeActivity.PEDIDOS_PENDIENTES_TAG) as Boolean)) {
            if (arguments?.getBoolean(HomeActivity.PEDIDOS_PENDIENTES_TAG) as Boolean) {
                arguments?.putBoolean(HomeActivity.PEDIDOS_PENDIENTES_TAG, false)
                activity?.let { it ->
                    PedidosPendientesDissmisableDialog(it, pedidos, false, object : PedidosPendientesDissmisableDialog.PedidosPendientesListener {
                        override fun goToPendingOrders() {
                            goPedidosPorAprobar()
                        }

                        override fun onCancelDialog() {
                            // not implement
                        }

                        override fun onCloseDialog() {
                            // not implement
                        }
                    }).show()
                }
            }
        }
    }

    override fun showButtonPendingOrder(pedidos: Int?) {
        showPendingOrdersButton(pedidos)
    }

    override fun hideButtonPendingOrder() {
        btnPedidosPorAprobar.visibility = View.GONE

        if (flagTieneDuo && !isShowedDuo) {
            presenter.searchPopUpDuoPerfecto()
        }
    }

    override fun showAlertPendingOrders(pedidos: Int?) {
        if (order?.productosDetalle == null || order?.productosDetalle!!.isEmpty()) {
            if (pedidos ?: 0 > 0) {
                tvwNoProducts.visibility = View.GONE
                lltProgress.visibility = View.GONE
                tvwPendingOrders.visibility = View.VISIBLE
            }
        }
    }

    override fun setNameListAnalytics(nameList: String?) {
        nameListCarouselTracker = nameList
    }

    override fun setConfigFest(configFest: FestivalConfiguracion?) {
        this.configFest = configFest
        this.listTagItemOrder[OrderItemTag.TAG_FEST] = configFest?.Titulo
        ordersList.setListItemTag(listTagItemOrder)
    }

    override fun showOptionMultiOrderDisabled() {
        multiOrderContentLinear.visibility = View.VISIBLE
        multiOrderTooltipView.visibility = View.GONE
        triangleTooltipMultiOrderView.visibility = View.GONE
        backgroundMultiOrderBlockView.visibility = View.VISIBLE
    }

    override fun showOptionMultiOrderEnabled(isOn: Boolean) {
        multiOrderSwitch.isChecked = isOn
        initOptionMultiOrder()
        multiOrderContentLinear.visibility = View.VISIBLE
        backgroundMultiOrderBlockView.visibility = View.GONE
        initMultiOrderSwitch()
    }

    override fun showTooltipOn() {
        multiOrderTooltipView.visibility = View.VISIBLE
        triangleTooltipMultiOrderView.visibility = View.VISIBLE
        tooltipMultiOrderImage.setImageResource(R.drawable.mt_check)
        triangleTooltipMultiOrderView.background = AppCompatResources.getDrawable(requireContext(), R.drawable.triangle_multi_order_green)
        multiOrderTooltipView.setBackgroundResource(R.drawable.bg_green_order_tooltip_check)
        tooltipMultiOrderText.text = getString(R.string.multi_message_info_on)
    }

    override fun showTooltipOff() {
        multiOrderTooltipView.visibility = View.VISIBLE
        triangleTooltipMultiOrderView.visibility = View.VISIBLE
        tooltipMultiOrderImage.setImageResource(R.drawable.mt_info)
        triangleTooltipMultiOrderView.background = AppCompatResources.getDrawable(requireContext(), R.drawable.triangle_multi_order_red)
        multiOrderTooltipView.setBackgroundResource(R.drawable.bg_red_order_tooltip_check)
        tooltipMultiOrderText.text = getString(R.string.multi_message_info_off)
    }

    private fun initOptionMultiOrder() {
        tooltipMultiOrderCloseButton.setOnClickListener {
            multiOrderTooltipView.visibility = View.GONE
            triangleTooltipMultiOrderView.visibility = View.GONE
        }
        multiOrderSwitch.setOnTouchListener { _, _ -> !NetworkUtil.isThereInternetConnection(context) }
        multiOrderSwitch.setOnCheckedChangeListener { _, p1 ->
            if (p1) {
                showTooltipOn()
            } else {
                showTooltipOff()
            }
            presenter.updateStateMultiOrder(p1)
        }
    }

    private fun showPendingOrdersButton(pedidos: Int?) {
        btnPedidosPorAprobar.text = "Pedidos por aprobar ($pedidos)"
        btnPedidosPorAprobar.visibility = View.VISIBLE
    }

    fun goPedidosPorAprobar() {
        val intent = Intent(activity, PedidosPendientesActivity::class.java)
        startActivityForResult(intent, PedidosPendientesActivity.RESULT)
    }

    private fun buscarTieneDuoEnOfertas(): Boolean {

        offersDataList?.filterNotNull()?.also { offers ->
            offers.find { it.codigoEstrategia == GlobalConstant.DUO }
                .run {
                    this?.also {
                        return true
                    }
                }
        } ?: kotlin.run { return false }
        return false
    }

    private fun totalDuoAgregadoListaPedido(): Int = this.order?.productosDetalle?.count { it?.isEsDuoPerfecto != null && it.isEsDuoPerfecto == true }
        ?: kotlin.run { 0 }

    override fun showPopUpDuoRecordatorio() {
        context?.also { contexto ->

            this.order?.productosDetalle?.also { list ->
                list.filterNotNull().also { listafiltrada ->

                    listafiltrada.filter {
                        it.isEsDuoPerfecto != null && it.isEsDuoPerfecto == true
                    }.run {

                        if (this.size in 0..1) {
                            DialogRecordatorioDuoPerfecto(contexto, this.size,
                                object : DialogRecordatorioDuoPerfecto.RecordatorioDuoListener {
                                    override fun onGoToProducts() {
                                        goToBannerDuo()
                                    }

                                }).show()
                            isShowedDuo = true
                        }
                    }
                }
            }
        }
    }

    private var promotionMapper = PromotionModelMapper()
    private var componentePromocion: Promotion? = null

    override fun loadPromotion(promotion: PromotionOfferModel?) {

        promotionMapper.df = decimalFormat
        promotionMapper.symbol = moneySymbol

        promotion?.let { promo ->

            val promotionOffer = promotionMapper.transformPromotion(promo.producto)
            val conditionsOffer = promotionMapper.transformListPromotion(promo.listaApoyo)

            val placeholderImage = resources.getDrawable(R.drawable.ic_container_placeholder)

            promotionOffer?.let {
                componentePromocion = Promotion.Builder(requireContext())
                    .setTitleCondition(resources.getString(R.string.promotion_title_conditions))
                    .setTitlePromotion(resources.getString(R.string.promotion_title_promotion))
                    .setPlaceholderCarrusel(placeholderImage)
                    .setImageDefault(placeholderImage)
                    .setImage(it.imageURL)
                    .activeButtonAdd(true)
                    .setPromotionOffer(it)
                    .setConditionOfferList(conditionsOffer)
                    .setPromotionConditionListener(object : Promotion.PromotionConditionListener {
                        override fun didPressedItem(item: OfferModel, pos: Int) {

                        }

                        override fun didPressedItemButtonAdd(typeLever: String, keyItem: String, quantity: Int, counterView: Counter) {
                            promo.listaApoyo?.firstOrNull { cond -> cond?.cuv == keyItem }?.let { pd ->

                                pd.flagPromocion = false
                                agregarDesdePromociones(pd, keyItem, quantity, counterView, true)
                            }
                        }

                        override fun didPressedItemButtonSelection(item: OfferModel, pos: Int) {

                        }

                        override fun didPressedItemButtonShowOffer(item: OfferModel, pos: Int) {

                        }

                        override fun impressionItems(typeLever: String, list: ArrayList<OfferModel>) {

                        }
                    })
                    .setPromotionOfferListener(object : Promotion.PromotionOfferListener {
                        override fun didPressedItemButtonAdd(typeLever: String, keyItem: String, quantity: Int, counterView: Counter) {
                            promo.producto?.let { pd ->

                                pd.flagPromocion = true
                                agregarDesdePromociones(pd, keyItem, quantity, counterView, true)
                            }
                        }
                    })
                    .show()
            }

        }

    }

    override fun loadPromotion(cuv: String?) {
        presenter.loadPromotion(cuv)
    }

    private fun agregarDesdePromociones(promociondetail: PromotionDetailModel, keyItem: String, quantity: Int, counterView: Counter, isPromotionDialog: Boolean) {

        val offerPromotion = PromotionDetailModel.transforToOfferEC(promociondetail)

        val palanca = offerPromotion?.tipoPersonalizacion ?: ""

        var codigoOrigen = if (!(offerPromotion?.flagPromocion ?: false))
            OffersOriginType.ORIGEN_CONDICION_PROMOCION_FICHA_CARRUSEL
        else
            OffersOriginType.ORIGEN_CONTENEDOR_PROMOCION_FICHA

        if (palanca == OfferTypes.CAT && offerPromotion?.marcaID != null) {
            codigoOrigen += Belcorp.getBrandOrigenById(offerPromotion?.marcaID!!)
        }

        offerPromotion?.let {
            presenter.agregar(it, quantity, counterView, DeviceUtil.getId(context), codigoOrigen, true)
        }

    }

    /** Listener */

    interface Listener {
        fun onFilter()
        fun onOrderReserved(saved: Boolean, updateEmail: Boolean, pendingEmail: String?)
        fun setCampaign(campaign: String)
        fun setActivityTitle(title: String)
        fun goToOffers()
        fun updateOffersCount(count: Int)
        fun navigateToOfertaFinal()
    }


    interface AdditionalOrderListener {
        fun onClose()
    }

    /** Companion */

    companion object {

        fun newInstance(): AddOrdersFragment {
            return AddOrdersFragment()
        }

        const val SCREEN_TRACK = 1
        const val EVENT_TRACK_CLICK_OFERTAS = 2
        const val EVENT_TRACK_CLICK_RESERVAR = 3
        const val EVENT_TRACK_CLICK_PESTANA_CLIENTE_PRODUCTO = 4
        const val EVENT_TRACK_COMBO_CLIENTE = 5
        const val EVENT_TRACK_BUSCAR_PRODUCTO = 6
        const val EVENT_TRACK_VISUALIZAR_PRODUCTO = 7
        const val EVENT_TRACK_MODIFICAR_PEDIDO = 8
        const val EVENT_TRACK_AGREGAR_PRODUCTO = 9
        const val EVENT_TRACK_ELIMINAR_PRODUCTO = 10
        const val EVENT_TRACK_CLICK_PESTANA_PEDIDO_GANAMAS = 11

        const val TAB_PEDIDO = 0
        const val TAB_OFERTAS = 1

        const val TAB_UNIDADES = 0
        const val TAB_CLIENTES = 1

        const val COD_EST_COMPUESTA_VARIABLE = 2003

        const val CLIENT_DEFAULT_NAME = "PARA MÍ"
        const val SEPARATOR = '|'

        var IS_FROM_KIT_NUEVA: Boolean? = null

    }

}

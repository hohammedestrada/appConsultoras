package biz.belcorp.consultoras.feature.finaloffer

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.dialog.FullScreenDialog
import biz.belcorp.consultoras.common.dialog.ReservedDialog
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.model.orders.*
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.feature.finaloffer.di.FinalOfferComponent
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity
import biz.belcorp.consultoras.feature.home.addorders.updatemail.ConfirmUpdateEmailDialog
import biz.belcorp.consultoras.feature.orderdetail.OrderDetailActivity
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.analytics.AddOrder as AnalyticsAddOrder
import biz.belcorp.consultoras.util.anotation.MetaType
import biz.belcorp.consultoras.util.anotation.OffersOriginTypeLocation
import biz.belcorp.consultoras.util.anotation.OffersOriginTypeSection
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.CountryUtil
import biz.belcorp.library.util.DeviceUtil
import kotlinx.android.synthetic.main.fragment_final_offer.*
import java.math.BigDecimal
import java.text.DecimalFormat
import javax.inject.Inject

class FinalOfferFragment :
    BaseFragment(),
    FinalOfferView,
    FinalOfferList.Listener {

    @Inject
    lateinit var presenter: FinalOfferPresenter

    private var listener: Listener? = null

    private var user: User? = null
    private var moneySymbol: String = ""
    private var decimalFormat: DecimalFormat = DecimalFormat()
    private var montoMinimoPedido: Double = 0.toDouble()
    private var montoMaximoPedido: Double = 0.toDouble()
    private var importTotal: BigDecimal = BigDecimal(0.0)
    private var ganancia: BigDecimal = BigDecimal(0.0)

    private var order: OrderModel? = null
    private var finalOfferHeader: OfertaFinalHeaderModel? = null
    private var reserveResponse: ReserveResponseModel? = null
    private var list: List<OfertaFinalModel>? = ArrayList()

    private var reservedDialog : ReservedDialog? = null

    private lateinit var identifier: String

    private lateinit var tfRegular: Typeface
    private lateinit var tfBold: Typeface

    private var nameListTracker: String? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is Listener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_final_offer, container, false)
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(FinalOfferComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        presenter.data()
        init()
    }

    override fun onResume() {
        super.onResume()
        presenter.initScreenTrack()
    }

    override fun context(): Context {
        return activity as Context
    }


    /** FinalOfferView  */

    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_ORDERS_FINAL_OFFER, model)
    }

    override fun onError(errorModel: ErrorModel) {
        processGeneralError(errorModel)
    }

    override fun onError(message: String) {
        showFullScreenError("ERROR", message)
    }

    override fun setData(user: User) {
        this.user = user
        this.decimalFormat = CountryUtil.getDecimalFormatByISO(user.countryISO!!, true)
        this.moneySymbol = user.countryMoneySymbol!!
        this.montoMinimoPedido = user.montoMinimoPedido!!
        this.montoMaximoPedido = user.montoMaximoPedido!!
        listener?.setCampaign(user.campaing!!)

        finalOfferList.setDataUser(user)
        finalOfferList.setDecimalFormat(decimalFormat, moneySymbol)
        setHeaderText()
    }

    override fun updateOrder(t: OrderModel?) {
        this.order = t

        importTotal = order?.importeTotal?.let { it } ?: BigDecimal("0.0")
        ganancia = order?.gananciaEstimada.let { it } ?: BigDecimal("0.0")
        arguments?.let {
            if (it.getBoolean(FinalOfferActivity.EXTRA_REGALO_TIENEREGALO)) {
                if (importTotal >= it.getDouble(FinalOfferActivity.EXTRA_MONTO_REGALO).toBigDecimal()) {
                    if (it.getBoolean(FinalOfferActivity.EXTRA_REGALO_ELEGIDO)) {
                        updateHeaderText()
                    } else {
                        presenter.autoSaveGift(DeviceUtil.getId(activity))
                    }
                    showGiftRain()
                }
            }
        }
        updateHeaderText()
    }

    override fun onReserveError(message: String, data: ReservaResponse?) {

        var type: String?

        order?.isDiaProl?.let {

            type = if (it) "RESERVAR" else "GUARDAR"
            val title = "NO SE PUDO " + type?.toUpperCase() + " PEDIDO"

            if (isVisible) {
                try {
                    FullScreenDialog.Builder(context!!)
                        .withTitle(title)
                        .withMessage(message)
                        .withIcon(-1)
                        .withButtonMessage("Regresar al Pedido")
                        .setOnItemClick(object : FullScreenDialog.FullScreenDialogListener {
                            override fun onDismiss() {
                                // EMPTY
                            }

                            override fun onClickAction(dialog: FullScreenDialog) {
                                // EMPTY
                            }

                            override fun onClickAceptar(dialog: FullScreenDialog) {
                                dialog.dismiss()
                                goToOrder()
                            }
                        })
                        .show()
                } catch (e: Exception) {
                    BelcorpLogger.w("showFullScreenError", e)
                }

            }

        }
    }

    override fun onReserveSuccess(data: ReservaResponse?) {
        data?.reserva?.let {
            if (it) {
                user?.let { u ->
                    Tracker.trackEvent(
                        GlobalConstant.SCREEN_NAME_INGRESAR_PEDIDO,
                        GlobalConstant.EVENT_CATEGORY_PEDIDO,
                        GlobalConstant.EVENT_ACTION_RESERVA_EXITOSA_O_F,
                        GlobalConstant.EVENT_LABEL_NOT_AVAILABLE,
                        GlobalConstant.EVENT_NAME_RESERVATION_COMPLETE, u)
                }
                goToOrderReserved()

            } else {
                showOrderSaved()
            }
        }

    }


    override fun onFinalOfferAddResponse(item: OfertaFinalModel, position: Int) {
        finalOfferList?.onProductAdded(item, position)
    }

    override fun onAutoSavedGift() {
        updateHeaderText()
    }

    /** FinalOfferList.ListListener  */
//Aqui se agrega la orden
    override fun addFinalOffer(item: OfertaFinalModel, position: Int) {
        presenter.addFinalOffer(item, position, identifier)
        AnalyticsAddOrder.addToCartFinalOffer(user?.countryISO, item, nameListTracker)
    }

    /** Functions */

    private fun init() {

        identifier = DeviceUtil.getId(context)
        tfRegular = Typeface.createFromAsset(context?.assets, GlobalConstant.LATO_REGULAR_SOURCE)
        tfBold = Typeface.createFromAsset(context?.assets, GlobalConstant.LATO_BOLD_SOURCE)

        importTotal = BigDecimal(arguments?.getString(FinalOfferActivity.EXTRA_ORDER_IMPORT_TOTAL)?.let { it } ?: "0.0")
        reserveResponse = arguments?.getParcelable(FinalOfferActivity.EXTRA_ORDER_RESERVE_RESULT)
        finalOfferHeader = arguments?.getParcelable(FinalOfferActivity.EXTRA_FINAL_OFFER_HEADER)
        list = arguments?.getParcelableArrayList(FinalOfferActivity.EXTRA_FINAL_OFFER_LIST)

        btnAccept.setOnClickListener {
            order?.let { ord ->
                presenter.reserve(ord)
            } ?: kotlin.run {
                reserveResponse?.reserva?.let { b ->
                    if (b) goToOrderReserved()
                    else showOrderSaved()
                }
            }
        }

        finalOfferList.listener = this

        presenter.initializeListOfferFinal(OffersOriginTypeLocation.ORIGEN_PEDIDO, OffersOriginTypeSection.ORIGEN_OFERTA_FINAL)
    }

    private fun goToOrderReserved() {

        val facturacion =  user?.isUltimoDiaFacturacion ?: false

        if(facturacion){
            activity?.finish()
            val intent = Intent(activity, OrderDetailActivity::class.java)
            startActivity(intent)
        }else{
            goToOrder()
        }

    }

    private fun goToOrder() {
        activity?.finish()
        val intent = Intent(activity, AddOrdersActivity::class.java)
        startActivity(intent)
    }

    private fun setHeaderText() {
        val titleSpannable = SpannableStringBuilder()
        val subTitleSpannable = SpannableStringBuilder()

        val subtitle1 = getString(R.string.final_offer_subtitle_1)
        val title2 = getString(R.string.final_offer_title_2)
        val subtitle2 = getString(R.string.final_offer_subtitle_2_3)

        finalOfferHeader?.tipoMeta?.let { t ->
            when (t) {
                MetaType.MM -> {

                    val text1 = "Aún te faltan $moneySymbol"
                    titleSpannable.append(text1)

                    val faltante = this.decimalFormat.format(finalOfferHeader?.montoMeta)
                    titleSpannable.append(faltante.toString())

                    val text2 = " para llegar al monto mínimo"
                    titleSpannable.append(text2)

                    subTitleSpannable.append(subtitle1)

                }
                MetaType.GM -> {
                    titleSpannable.append(title2)
                    subTitleSpannable.append(subtitle2)
                }
                else -> {
                    titleSpannable.append(title2)

                    val text1 = "Agrega $moneySymbol"
                    subTitleSpannable.append(text1)

                    val faltante = this.decimalFormat.format(finalOfferHeader?.montoMeta)
                    subTitleSpannable.append(faltante.toString())

                    val text2 = " y obtén \n"
                    subTitleSpannable.append(text2)

                    val descuento = "${finalOfferHeader?.porcentajeMeta?.toInt()}% dscto."
                    val textDscto = SpannableString(descuento)
                    textDscto.setSpan(ForegroundColorSpan(ContextCompat
                        .getColor(context(), R.color.primary)), 0, descuento.length, 0)
                    subTitleSpannable.append(textDscto)

                    val text3 = " en tu compra"
                    subTitleSpannable.append(text3)
                }
            }
        } ?: kotlin.run {
            titleSpannable.append(title2)
            subTitleSpannable.append(subtitle2)
        }

        if (!finalOfferHeader?.mensajeTipingPoint.isNullOrEmpty()) {
            finalOfferHeader?.let {
                val subtitulo = createSubTiTle(it, moneySymbol)
                tvwTitle.text = titleSpannable
                tvwSubTitle.text = subtitulo
            }
        } else {
            setTexts(titleSpannable, subTitleSpannable)
        }
    }

    private fun updateHeaderText() {

        val titleSpannable = SpannableStringBuilder()
        val subTitleSpannable = SpannableStringBuilder()

        val title1 = getString(R.string.final_offer_title_2)
        val subtitle1 = getString(R.string.final_offer_subtitle_2_3)
        val title2 = getString(R.string.final_offer_title_3)
        val subtitle2 = getString(R.string.final_offer_subtitle_3)
        val title3 = getString(R.string.final_offer_title_4)

        val totalString: String = this.decimalFormat.format(importTotal)
        val gananciaString: String = this.decimalFormat.format(ganancia)

        finalOfferHeader?.tipoMeta?.let { t ->
            when (t) {
                MetaType.MM -> {
                    titleSpannable.append(title2)
                    subTitleSpannable.append(String.format(subtitle2, moneySymbol, gananciaString,
                        moneySymbol, totalString))
                }
                MetaType.GM -> {
                    titleSpannable.append(title1)
                    subTitleSpannable.append(subtitle1)
                }
                else -> {
                    val desc = "${finalOfferHeader?.porcentajeMeta?.toInt()} %"
                    titleSpannable.append(String.format(title3, desc))
                    subTitleSpannable.append(String.format(subtitle2, moneySymbol, gananciaString,
                        moneySymbol, totalString))
                }
            }
        } ?: run {
            titleSpannable.append(title1)
            subTitleSpannable.append(subtitle1)
        }


        finalOfferHeader?.let {
            if (!it.mensajeTipingPoint.isNullOrEmpty()) {
                val subtitulo = createSubTiTle(it, moneySymbol)
                arguments?.let { it1 ->
                    if (importTotal >= it1.getDouble(FinalOfferActivity.EXTRA_MONTO_REGALO).toBigDecimal()) {
                        if (it1.getBoolean(FinalOfferActivity.EXTRA_REGALO_ELEGIDO)) {
                            tvwSubTitle.text = (resources.getString(R.string.felicidades_regalo)).toString()
                        } else
                            tvwSubTitle.text = subtitulo
                    } else {
                        tvwSubTitle.text = subtitulo
                    }
                    tvwTitle.text = titleSpannable
                }

            } else {
                setTexts(titleSpannable, subTitleSpannable)
            }
        }
    }

    private fun setTexts(title: Spannable, subtitle: Spannable) {
        tvwTitle.text = title
        tvwSubTitle.text = subtitle
    }

    private fun setFinalOfferList() {
        list?.let {
            if (it.isEmpty()) {
                goToOrderReserved()
            } else {
                finalOfferList.setNameListTracker(nameListTracker)
                finalOfferList.setData(it)
            }
        } ?: run {
            goToOrderReserved()
        }
    }

    private fun showOrderSaved() {

        val title = "¡GUARDASTE TU \n" + "PEDIDO CON ÉXITO!"

        if (isVisible) {
            try {
                context?.let{

                    if(presenter.updateMailOF) {

                        ReservedDialog.Builder(it).withStatusTitle(title).setOnAccept(object : ReservedDialog.ReservedDialogListener {

                            override fun closeDialog(dialog: ReservedDialog) {
                                dialog.dismiss()
                                goToOrder()
                            }

                            override fun updateEmail(dialog: ReservedDialog, email: String) {
                                reservedDialog = dialog
                                presenter.updateConsultEmail(email)
                            }

                        }).show()

                    }else {
                        FullScreenDialog.Builder(it)
                            .withTitle(title)
                            .withMessage("")
                            .withIcon(R.drawable.ic_hands)
                            .withButtonMessage(resources.getString(R.string.button_aceptar))
                            .setOnItemClick(object : FullScreenDialog.FullScreenDialogListener {
                                override fun onDismiss() {
                                    //EMPTY
                                }

                                override fun onClickAction(dialog: FullScreenDialog) {
                                    // EMPTY
                                }

                                override fun onClickAceptar(dialog: FullScreenDialog) {
                                    dialog.dismiss()
                                    goToOrder()
                                }
                            })
                            .show()
                    }

                }

            } catch (e: Exception) {
                BelcorpLogger.w("showFullScreenError", e)
            }

        }
    }


    private fun createSubTiTle(ofertaFinal: OfertaFinalHeaderModel, moneda: String): Spanned? {
        var monto = decimalFormat.format(ofertaFinal.faltanteTipingPoint)

        var text = ofertaFinal.mensajeTipingPoint?.replace(GlobalConstant.MARCADOR_MONTO, "<b>$moneda $monto</b>")?.replace(GlobalConstant.GANAR_UN + GlobalConstant.MARCADOR_PRODUCTO,
            "<b>" + GlobalConstant.GANAR_UN + " " + ofertaFinal.descripcionRegalo + "</b>")

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text)
        }
    }


    fun showGiftRain() {
        FullScreenDialog.Builder(context!!)
            .withTitle((resources.getString(R.string.felicidades_regalo)).toString(), 20F)
            .withMessage((resources.getString(R.string.escogeremos_por_ti)).toString(), 20F)
            .withIcon(R.drawable.ic_regalo)
            .withScreenDismiss(true)
            .withAnimation(resources,
                FullScreenDialog.SIMPLE_ANIMATION,
                ContextCompat.getColor(context!!, R.color.dorado),
                ContextCompat.getColor(context!!, R.color.primary))
            .show()
        presenter.setStatusIsShowingGiftAnimation(true)
    }

    override fun onEmailUpdated(newEmail: String?) {
        context?.let{ it ->
            newEmail?.let {it2 ->
                reservedDialog?.hideLoading()
                reservedDialog?.dismiss()
                ConfirmUpdateEmailDialog.Builder(it).
                    withEmail(it2).
                    setOnDismissListener(object: ConfirmUpdateEmailDialog.ConfirmUpdateEmailDialogListener{
                        override fun close(dialog: ConfirmUpdateEmailDialog) {
                            dialog.dismiss()
                        }

                        override fun resendEmail(email: String){
                            presenter.updateConsultEmail(email)
                        }
                    }).show()
            }
        }
    }

    override fun showLoadingDialog() {
        reservedDialog?.showLoading()
    }

    override fun hideLoadingDialog() {
        reservedDialog?.hideLoading()
    }

    override fun initializeListOfferFinal(nameList: String?) {
        nameListTracker = nameList
        setFinalOfferList()
    }

    /** Listeners */

    interface Listener {
        fun setCampaign(campaign: String)
    }

}

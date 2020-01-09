package biz.belcorp.consultoras.feature.home.ganamas.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.domain.entity.ConfiguracionPorPalanca
import biz.belcorp.consultoras.domain.entity.Oferta
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.feature.home.BaseHomeFragment
import biz.belcorp.consultoras.feature.home.HomeActivity
import biz.belcorp.consultoras.feature.home.di.HomeComponent
import biz.belcorp.consultoras.util.StorieUtils
import biz.belcorp.consultoras.util.TimeUtil
import biz.belcorp.consultoras.util.anotation.OffersOriginType
import biz.belcorp.consultoras.util.anotation.RedirectionStories
import biz.belcorp.library.util.NetworkUtil
import biz.belcorp.mobile.components.core.custom.CustomVerticalNestedScrollView
import biz.belcorp.mobile.components.core.helpers.ImagesHelper
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import biz.belcorp.mobile.components.offers.Multi
import biz.belcorp.mobile.components.offers.Offer
import biz.belcorp.mobile.components.offers.model.OfferModel
import biz.belcorp.mobile.components.offers.sello.Sello
import kotlinx.android.synthetic.main.fragment_ganamas_offers.*

class GanaMasOffersFragment : BaseHomeFragment(), Offer.OfferListener, SafeLet {

    override fun bindMulti(multiItem: Multi, position: Int) {
    }

    private var configuracion: ArrayList<ConfiguracionPorPalanca>? = null
    private var stampList: MutableList<Sello> = mutableListOf()
    private lateinit var imageHelper: ImagesHelper
    var listener: Listener? = null

    private var offers = mutableMapOf<String, Oferta>()

    private var setOffersCount = 0

    override fun onInjectView(): Boolean {
        getComponent(HomeComponent::class.java).inject(this)
        return true
    }

    // Overrides Fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ganamas_offers, container, false)
    }

    // Override BaseFragment
    override fun context(): Context {
        return context?.let { it } ?: throw NullPointerException("No hay contexto")
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        init()
    }

    // Overrides Fragment
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            this.listener = context
        }
    }

    fun init() {
        activity?.let {
            imageHelper = ImagesHelper(it)
        }
    }

    override fun onPause() {
        super.onPause()
        if (listener?.isOffersvisible() == true) {
            stopTimer()
        }
    }

    fun stopTimer() {
        val oddView = getLeverView(OfferTypes.ODD)
        oddView?.let {
            oddView.stopTimers()
        }
    }

    override fun onResume() {
        super.onResume()
        if (listener?.isOffersvisible() == true) {
            startTimer()
        }
    }

    fun startTimer() {
        val oddView = getLeverView(OfferTypes.ODD)
        oddView?.let {
            // Modificar esto para que utilice el time correcto (getdevicetime es solo para pruebas)
            val time = TimeUtil.setServerTime(BuildConfig.TIME_SERVER)
            oddView.updateTimers(time)
        }
    }

    fun setOffers(type: String, leverOferts: ArrayList<OfferModel>, showSeeMore: Boolean = false) {
        val leverView = getLeverView(type)

        if (leverView != null) {

            val lever = leverView.tag as ConfiguracionPorPalanca
            var time: Long? = null

            when (lever.tipoOferta) {
                OfferTypes.ODD -> {

                    // Modificar esto para que utilice el time correcto (getdevicetime es solo para pruebas)
                    time = TimeUtil.setServerTime(BuildConfig.TIME_SERVER)

                    if (leverOferts.size == 1) {
                        leverView.updateSimpleOfert()

                        if (!lever.colorFondo.isNullOrEmpty())
                            leverView.updateSimpleBg(Color.parseColor(lever.colorFondo))

                        if (!lever.colorTexto.isNullOrEmpty())
                            leverView.updateSimpleTextColor(Color.parseColor(lever.colorTexto))

                    }
                }
                OfferTypes.LAN -> {
                    leverView.showSeeMore(showSeeMore)
                    leverView.showMoreCard(showSeeMore)
                }
            }

            leverView.setProducts(leverOferts, time, stampList)

            setOffersCount++
        }

        if (checkForOffersLeft()) {

            doScroll()

            nsv_content.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    nsv_content.startScrollerTask()
                }
                false
            }

            nsv_content.setOnScrollStoppedListener(object : CustomVerticalNestedScrollView.OnScrollStoppedListener {
                override fun onScrollStopped(position: Int) {
                    configuracion?.let {
                        val screenRect = Rect()

                        nsv_content?.let {
                            it.getGlobalVisibleRect(screenRect)

                            for (i in 0 until lnlOffers.childCount) {
                                val offer = lnlOffers.getChildAt(i) as Offer
                                val element = offer.getVisibleElement()
                                if (elementIsVisible(element, screenRect))
                                    offer.trackVisibleElement()
                            }
                        }
                    }
                }
            })

            nsv_content?.let { scrollView ->
                scrollView.post {
                    scrollView.startScrollerTask()
                }
            }

            activity?.let {
                (it as HomeActivity).unlockBackButtons()
            }
        }
    }

    fun doScroll(){
        arguments?.let {argumentos->
            val palanca = StorieUtils.getRedirectionGanaMas(argumentos).let {donde->
                donde.substring(RedirectionStories.GANA_MAS.length+1,donde.length)
            }
            val index = getLeverIndex(palanca)?:0
            index.let {
                val child = lnlOffers.getChildAt(it)
                val y = child.y.toInt()
                nsv_content.smoothScrollTo(0, y)
            }
        }


    }

    private fun elementIsVisible(view: View, screenRect: Rect) : Boolean {
        val elementRect = Rect()
        view.getGlobalVisibleRect(elementRect)
        return ((elementRect.top > screenRect.top) && (elementRect.bottom < screenRect.bottom))
    }

    private fun checkForOffersLeft() : Boolean {
        configuracion?.let {
            return setOffersCount == it.size
        }
        return false
    }

    fun setOffersByCuv(offers: List<Oferta?>) {
        offers.forEach {
            it?.cuv?.let { cuv -> this.offers.put(cuv, it) }
        }
    }

    fun removeOffer(type: String) {
        val leverIndex = getLeverIndex(type)
        if (leverIndex != null) {
            lnlOffers.removeViewAt(leverIndex)
            configuracion?.removeAt(leverIndex)
        }
        lnlOffers.invalidate()
    }

    fun updateKitNueva(value: Boolean) {

        val view = getLeverView(OfferTypes.HV)

        view?.updateKitNuevaFlag(value)

    }

    fun updateOfferView(type: String, offer: ConfiguracionPorPalanca?, hasATP: Boolean) {
        val offerView = getLeverView(type)
        val textBtn: String = if(hasATP) {
            offer?.textoModificar ?: getString(R.string.ganamas_btn_text_default_update_pack_atp)
        } else {
            offer?.textoInicio ?: getString(R.string.ganamas_btn_text_default_add_pack_atp)
        }

        offerView?.updateTextButtonPack(textBtn)
    }

    private fun getLeverIndex(type: String): Int? {
        for (i in 0 until lnlOffers.childCount) {
            val leverView = lnlOffers.getChildAt(i)!! as Offer
            val lever = leverView.tag as ConfiguracionPorPalanca
            if (lever.tipoOferta.equals(type)) {
                return i
            }
        }
        return null
    }

    private fun getLeverView(type: String): Offer? {
        for (i in 0 until lnlOffers.childCount) {
            val leverView = lnlOffers.getChildAt(i)!! as Offer
            val lever = leverView.tag as ConfiguracionPorPalanca
            if (lever.tipoOferta.equals(type)) {
                return leverView
            }
        }
        return null
    }

    fun setConfig(offerList: List<ConfiguracionPorPalanca>, flagATP: Boolean?) {
        lnlOffers.removeAllViews()
        configuracion = arrayListOf()
        configuracion?.addAll(offerList)
        for (offer in offerList) {
            context?.let {
                val offerView = Offer(it)


                val hide: Boolean = arguments?.getBoolean("flag_hide_views_for_testing")?:false


                offerView.setHideViewsForTesting(hide)
                offerView.updateTitle(offer.titulo ?: "-")
                offerView.updateCarouselItems(offer.cantidadMostrarCarrusel)
                offerView.updateItemPlaceHolder(resources.getDrawable(R.drawable.ic_container_placeholder))

                if (!offer.bannerOferta.isNullOrEmpty()) {
                    when (offer.tipoOferta) {
                        OfferTypes.PN, OfferTypes.DP -> {
                            offerView.updatePackOffer()
                            offerView.updateTextButtonPack(getString(R.string.ganamas_btn_text_pack_duo))
                            offer.bannerOferta?.let {
                                offerView.updateBannerPack(ColorDrawable(resources.getColor(R.color.gray_3)), imageHelper.getResolutionURL(it))
                            }
                            setOffersCount++
                        }
                        OfferTypes.ATP -> {
                            offerView.updatePackOffer()

                            flagATP?.let { atp ->
                                val textBtn: String = if(atp) {
                                    offer.textoModificar ?: getString(R.string.ganamas_btn_text_default_update_pack_atp)
                                } else {
                                    offer.textoInicio ?: getString(R.string.ganamas_btn_text_default_add_pack_atp)
                                }

                                offerView.updateTextButtonPack(textBtn)
                            }

                            offerView.updateTextPack(offer.titulo, offer.subTitulo)
                            if (!offer.colorTexto.isNullOrEmpty())
                                offerView.updateTextColorPack(Color.parseColor(offer.colorTexto), Color.parseColor(offer.colorTexto))

                            val colorDrawablePack = if (!offer.colorFondo.isNullOrEmpty()) {
                                ColorDrawable(Color.parseColor(offer.colorFondo))
                            } else {
                                ColorDrawable(resources.getColor(R.color.golden_gana))
                            }

                            if (!offer.bannerOferta.isNullOrEmpty()) {
                                offerView.updateBannerPack(colorDrawablePack, imageHelper.getResolutionURL(offer.bannerOferta!!))
                            } else {
                                offerView.updateBannerBackgroundColor(colorDrawablePack)
                            }
                            setOffersCount++
                        }
                        else -> {
                            offer.bannerOferta?.let {
                                offerView.updateBanner(ColorDrawable(resources.getColor(R.color.gray_3)), imageHelper.getResolutionURL(it))
                            }
                        }
                    }
                }

                offerView.leverType = offer.tipoOferta!!
                if (offerView.leverType == OfferTypes.LAN)
                    offerView.useAlternativeLayout()
                offerView.leverListener = this
                offerView.tag = offer

                lnlOffers.addView(offerView)
            }
        }
        startLeversLoading()
    }

    fun setStampList(stamps: MutableList<Sello>) {
        stampList = stamps
    }

    private fun startLeversLoading() {
        for (i in 0..(lnlOffers.childCount - 1)) {
            val leverView = lnlOffers.getChildAt(i)!! as Offer
            leverView.startLoading()
        }
    }

    // START LISTENER LEVER
    override fun didPressedAppBarOption(typeLever: String) {
        openLanding(typeLever, false)
    }

    override fun didPressedBanner(typeLever: String, pos: Int) {
        listener?.clicBanner(typeLever, pos)
        openLanding(typeLever, true)
    }

    override fun didPressedButtonMore(typeLever: String, pos: Int) {
        listener?.clicBanner(typeLever, pos)
        openLanding(typeLever, true)
    }

    override fun didPressedButtonDuo(typeLever: String, pos: Int) {
    }

    fun openLanding(typeLever: String, fromBanner: Boolean){
        listener?.openLandingVerMas(typeLever, fromBanner)
    }

    override fun didPressedItem(typeLever: String, keyItem: String, marcaID: Int, marca: String,
                                pos: Int) {
        trackClicItem(typeLever, keyItem, pos)
        showItemSelected(typeLever, keyItem, marcaID, marca)
    }


    override fun didPressedItemButtonAdd(typeLever: String, keyItem: String, quantity: Int, counterView: Counter, pos: Int, multi: Multi?) {
        if(quantity == 0){
            context?.let {
                BottomDialog.Builder(it)
                    .setContent(getString(R.string.error_zero_quantity))
                    .setNeutralText(getString(R.string.msj_entendido))
                    .onNeutral(object: BottomDialog.ButtonCallback{
                        override fun onClick(dialog: BottomDialog) {
                            dialog.dismiss()
                        }
                    })
                    .setNeutralBackgroundColor(R.color.magenta)
                    .show()

            }
        } else {
            if(NetworkUtil.isThereInternetConnection(context())) {
                configuracion?.forEach { config ->
                    if(config.tipoOferta == typeLever){
                        offers[keyItem]?.let {

                            listener?.addFromHome(it, quantity, counterView, OffersOriginType.ORIGEN_CONTENEDOR)

                            //listener?.addFromHome(it, quantity, counterView, config.origenPedidoWebCarrusel)
                            //presenter.agregar(it, quantity, counterView, DeviceUtil.getId(activity), config.origenPedidoWebCarrusel)
                        }
                    }
                }
            } else {
                showNetworkError()
            }
        }
    }

    override fun viewBanner(typeLever: String, pos: Int) {
        listener?.viewBanner(typeLever, pos)
    }

    override fun impressionItems(typeLever: String, list: ArrayList<OfferModel>) {
        val visibleList = arrayListOf<Oferta>()
        list.forEach { le ->
            val item = offers[le.key]
            item?.let {
                item.index = le.index
                visibleList.add(item)
            }
        }
        listener?.impressionItems(typeLever, visibleList)
    }

    override fun finishedTimer(typeLever: String) {
        listener?.restartNow()
    }

    override fun didPressedItemButtonSelection(typeLever: String, keyItem: String, marcaID: Int,
                                               marca: String, pos: Int) {
        trackClicItem(typeLever, keyItem, pos)
        showItemSelected(typeLever, keyItem, marcaID, marca)
    }

    override fun didPressedItemButtonShowOffer(typeLever: String, keyItem: String, marcaID: Int,
                                               marca: String, pos: Int) {
        trackClicItem(typeLever, keyItem, pos)
        showItemSelected(typeLever, keyItem, marcaID, marca)
    }

    override fun didPressedPackBanner(typeLever: String, isUpdate: Boolean) {
        listener?.clicBanner(typeLever, 1)
        goToLeverPack(typeLever)
    }

    override fun didPressedPackButton(typeLever: String, isUpdate: Boolean) {
        goToLeverPack(typeLever)
    }

    private fun goToLeverPack(typeLever: String) {
        listener?.goToLeverPack(typeLever)
    }

    private fun showItemSelected(typeLever: String, keyItem: String, marcaID: Int, marca: String){
        val session = SessionManager.getInstance(context())
        if (!NetworkUtil.isThereInternetConnection(context()) && session.getApiCacheEnabled() == false) {
            showNetworkError()
        } else {
            val extras = Bundle()
            extras.putString(BaseFichaActivity.EXTRA_KEY_ITEM, keyItem)
            extras.putString(BaseFichaActivity.EXTRA_TYPE_OFFER, typeLever)
            extras.putInt(BaseFichaActivity.EXTRA_MARCA_ID, marcaID)
            extras.putString(BaseFichaActivity.EXTRA_MARCA_NAME, marca)
            extras.putString(BaseFichaActivity.EXTRA_ORIGEN_PEDIDO_WEB_FROM, OffersOriginType.ORIGEN_CONTENEDOR)
            extras.putInt(BaseFichaActivity.EXTRA_ACCESS_FROM, BaseFichaActivity.ACCESS_FROM_GANAMAS)
            listener?.goToFicha(extras)
        }
    }

    fun resetScroll(){
        nsv_content?.smoothScrollBy(0, 0) // To stop scrolling
        nsv_content?.post { nsv_content?.scrollBy(0, -lnlOffers.height) }
        for (i in 0..(lnlOffers.childCount - 1)) {
            val leverView = lnlOffers.getChildAt(i)!! as Offer
            leverView.resetScroll()
        }
    }

    fun trackClicItem(typeLever: String, keyItem: String, pos: Int) {
        val item = offers[keyItem]
        item?.let {
            listener?.clicItem(typeLever, it, pos)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(flagHideViewsForTesting: Boolean) = GanaMasOffersFragment().apply {
            arguments = Bundle().apply {
                putBoolean("flag_hide_views_for_testing", flagHideViewsForTesting)
            }
        }
    }

    /** Listeners */

    interface Listener {
        fun restartNow()
        fun addFromHome(oferta: Oferta, quantity: Int, counterView: Counter, originType: String)
        fun openLandingVerMas(typeLever: String, fromBanner: Boolean)
        fun goToFicha(extras: Bundle)
        fun isOffersvisible(): Boolean
        fun goToLeverPack(typeLever: String)
        fun viewBanner(typeLever: String, pos: Int)
        fun clicBanner(typeLever: String, pos: Int)
        fun impressionItems(typeLever: String, list: ArrayList<Oferta>)
        fun clicItem(typeLever: String, item: Oferta, pos: Int)
    }

}

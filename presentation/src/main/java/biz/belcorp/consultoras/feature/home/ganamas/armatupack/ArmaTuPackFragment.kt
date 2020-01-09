package biz.belcorp.consultoras.feature.home.ganamas.armatupack

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.search.OrigenModel
import biz.belcorp.consultoras.domain.entity.ConfiguracionPorPalanca
import biz.belcorp.consultoras.domain.entity.Oferta
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.feature.home.BaseHomeFragment
import biz.belcorp.consultoras.feature.home.ganamas.GanaMasFragment
import biz.belcorp.consultoras.feature.home.ganamas.armatupack.ArmaTuPackActivity.Companion.ATP_TYPE_KEY
import biz.belcorp.consultoras.feature.home.ganamas.armatupack.ArmaTuPackActivity.Companion.ATP_WITH_RESULT_EXTRA
import biz.belcorp.consultoras.feature.home.ganamas.armatupack.di.ArmaTuPackComponent
import biz.belcorp.consultoras.util.ImageUtils
import biz.belcorp.consultoras.util.anotation.ArmaTuPackStateType
import biz.belcorp.consultoras.util.anotation.OffersOriginType
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.library.util.StringUtil
import biz.belcorp.mobile.components.core.helpers.ImagesHelper
import biz.belcorp.mobile.components.core.helpers.StylesHelper
import biz.belcorp.mobile.components.design.button.Button
import biz.belcorp.mobile.components.design.tone.group.CategoryTagList
import biz.belcorp.mobile.components.design.tone.model.CategoryToneModel
import biz.belcorp.mobile.components.design.tone.model.ToneModel
import biz.belcorp.mobile.components.design.tone.pic.PickedTone
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.fragment_ganamas_arma_pack.*
import javax.inject.Inject

class ArmaTuPackFragment : BaseHomeFragment(), CategoryTagList.Listener, ArmaTuPackView {

    @Inject
    lateinit var presenter: ArmaTuPackPresenter

    lateinit var typeLever: String

    private var configuracionListOriginal = mutableListOf<ConfiguracionPorPalanca>()
    private var configuracionAtp: ConfiguracionPorPalanca? = null
    private var origenesBuscador: List<OrigenModel>? = null

    private lateinit var imageHelper: ImagesHelper
    private lateinit var stylesHelper: StylesHelper

    var listener: Listener? = null
    var totalQuadrant: Int = 0
    var savePack: Boolean = false
    var ofertaAtp: Oferta? = null
    var isUpdateAtp: Boolean = false
    var imageDialog: Boolean = false
    private var withRedirectBack: Boolean = false

    /**
     * Lifecycle override functions
     */

    override fun onAttach(context: Context?) {

        super.onAttach(context)
        if (context is Listener) this.listener = context

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_ganamas_arma_pack, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //UXCam.tagScreenName("ArmaTuPackFragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    /**
     * Custom override functions
     */

    override fun context(): Context = context?.let { it }
        ?: throw NullPointerException("No hay contexto")

    override fun onInjectView(): Boolean {

        getComponent(ArmaTuPackComponent::class.java).inject(this)
        return true

    }

    override fun onViewInjected(savedInstanceState: Bundle?) {

        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        init()

    }

    override fun onGroupProductUpdated(products: ArrayList<ToneModel>) {

        isUpdateAtp = true
        setupTextButton()

        pickedProduct.isScrollEnd = false
        updatePickedProduct(products)

    }

    override fun onGroupProductCompleted(completed: Boolean) {

        this.savePack = completed

        if (completed) {

            btnComplete.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            btnComplete.addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.magenta))

        } else {

            btnComplete.textColor = ContextCompat.getColor(requireContext(), R.color.gray_4)
            btnComplete.addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_3))

        }

    }

    override fun onGroupProductSelected(products: ArrayList<ToneModel>) {

        pickedProduct.isScrollEnd = true
        updatePickedProduct(products)

    }

    override fun onGroupProductRemove(products: ArrayList<ToneModel>, position: Int) {

        pickedProduct.isScrollEnd = (position - 1) in (products.size - 2)..(products.size - 1)
        updatePickedProduct(products)

    }

    // ArmaTuPackView

    override fun showLoading() {
        listener?.showLoading()
    }

    override fun hideLoading() {
        listener?.hideLoading()
    }

    override fun trackBackPressed() {
        // TODO analytics
    }

    override fun initScreenTrack() {
        // TODO analytics
    }

    override fun setUser(user: User?) {

        user?.let {
            presenter.getConfiguracion()
        }

    }

    override fun setConfig(config: List<ConfiguracionPorPalanca?>, flagATP: Boolean?) {

        configuracionListOriginal.addAll(config.filterNotNull())
        configuracionAtp = configuracionListOriginal.firstOrNull { it.tipoOferta == typeLever }
        val title = configuracionAtp?.titulo ?: ""

        listener?.setScreenTitle(title)

        presenter.getProductPack(typeLever)

    }

    override fun setImageEnabled(imageDialog: Boolean) {
        this.imageDialog = imageDialog
    }

    override fun onOfferAdded(quantity: Int, productCUV: ProductCUV, message: String?) {
        //override fun onOfferAdded(quantity: Int, productCUV: ProductCUV) {
        activity?.let {
            val saveATPIntent = Intent(ArmaTuPackActivity.BROADCAST_ATP_ACTION)
            saveATPIntent.putExtra(ArmaTuPackActivity.BROADCAST_STATE_ATP_EXTRAS, ArmaTuPackStateType.INSERT_UPDATE)
            it.sendBroadcast(saveATPIntent)
        }
        listener?.updateOffersCount(quantity)
        setResultToGanaMas(quantity, productCUV, false, message)
    }

    override fun onOfferUpdated(productCUV: ProductCUV, message: String?) {
        setResultToGanaMas(0, productCUV, true, message)
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

    override fun showErrorScreenMessage(type: Int) {
        listener?.showErrorScreen(type)
    }

    override fun goToOffers() {
        listener?.goToOffers()
    }

    override fun showArmaTuPack(oferta: Oferta, groupProduct: ArrayList<CategoryToneModel>, productHistory: ArrayList<ToneModel>) {

        btnComplete.visibility = View.VISIBLE
        setupBanner(groupProduct)
        setGroupProduct(oferta, groupProduct, productHistory)

    }

    /**
     * Fragment functions
     */

    private fun init() {

        arguments?.let {
            typeLever = it.getString(ATP_TYPE_KEY)?:StringUtil.Empty
            withRedirectBack = it.getBoolean(ATP_WITH_RESULT_EXTRA, false)
        }

        imageHelper = ImagesHelper(context())
        stylesHelper = StylesHelper(context())

        pickedProduct.setPlusVisible(true)
        pickedProduct.setNumberVisible(true)
        pickedProduct.setItemTextVisible(true)
        pickedProduct.setupItemCenter()

        btnComplete.textColor = ContextCompat.getColor(requireContext(), R.color.gray_4)
        btnComplete.addBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_3))
        setupTextButton()

        fltContainerBanner.visibility = View.GONE
        btnComplete.visibility = View.GONE

        tvwTitleBanner.typeface = getFont(requireContext(), stylesHelper.fontLatoBold)
        tvwSubTitleBanner.typeface = getFont(requireContext(), stylesHelper.fontLatoRegular)

        setupListener()

        presenter.getUser()
        presenter.getImageEnabled()

    }

    private fun setupListener() {

        pickedProduct.pickedTonesListener = object : PickedTone.PickedTonesListener {

            override fun onChange() {}

            override fun onComplete() {}

            override fun onDelete(position: Int) {
                deleteToneSelected(position)
            }

        }

        categoryTagList.categoryTagListener = this

        btnComplete.buttonClickListener = object : Button.OnClickListener {

            override fun onClick(view: View) {

                if (savePack) {

                    ofertaAtp?.let {

                        val origenPedidoWeb = getOrigenPedidoWeb(it.tipoOferta, OffersOriginType.ORIGEN_LANDING_FICHA, null)
                        presenter.agregar(it, pickedProduct.listPickedTones, 1,
                            DeviceUtil.getId(activity), origenPedidoWeb, isUpdateAtp, typeLever)
                    }

                } else categoryTagList.validateList()

            }

        }

    }

    private fun getFont(context: Context,fontId: Int): Typeface? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            resources.getFont(fontId)
        } else {
            ResourcesCompat.getFont(context, fontId)
        }
    }

    private fun setupTextButton() {
        if (isUpdateAtp) {
            btnComplete.setText(resources.getString(R.string.arma_tu_pack_btn_text_update))
        } else {
            btnComplete.setText(resources.getString(R.string.arma_tu_pack_btn_text_add))
        }
    }

    private fun deleteToneSelected(position: Int) {

        if (position in 0..(totalQuadrant - 1)) categoryTagList.setPositionRemoveProduct(position)

    }

    private fun updatePickedProduct(listProductSelected: ArrayList<ToneModel>) {

        pickedProduct.setPickedTones(listProductSelected)

    }

    private fun setResultToGanaMas(quantity: Int, productCUV: ProductCUV, isUpdate: Boolean, message: String?) {
        if (withRedirectBack) {
            activity?.run {
                val resultIntent = Intent()
                resultIntent.putExtra(GanaMasFragment.ATP_QUANTITY_KEY, quantity)
                resultIntent.putExtra(GanaMasFragment.ATP_PRODUCT_KEY, productCUV)
                resultIntent.putExtra(GanaMasFragment.ATP_IS_UPDATE_KEY, isUpdate)
                resultIntent.putExtra(GanaMasFragment.ATP_MESSAGE_ADDED, message)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        } else {
            val message =  if (isUpdate) {
                getString(R.string.msj_offer_updated)
            } else {
                getString(R.string.msj_offer_added)
            }

            val image = ImageUtils.verifiedImageUrl(productCUV)
            context?.let {
                val url = if (imageDialog) image else null  // Verifica que el flag este activo para mostrar la imagen
                val colorText = ContextCompat.getColor(it, R.color.leaf_green)
                showBottomDialog(it, message, null, colorText)
            }
        }
    }

    private fun getOrigenPedidoWeb(palancaType: String?, originType: String?, materialGanancia: Boolean?): String {

        var palanca = palancaType
        if (materialGanancia != null) {
            if (palancaType == OfferTypes.OPM) {
                palanca = when (materialGanancia) {
                    false -> {
                        OfferTypes.RD
                    }
                    true -> {
                        OfferTypes.MG
                    }
                }
            }
        }
        val opw = configuracionListOriginal.firstOrNull { it.tipoOferta == palanca }?.listaOrigenes?.filterNotNull()?.firstOrNull { it.codigo == originType }?.valor
            ?: "0"
        return opw
    }

    private fun setupBanner(products: ArrayList<CategoryToneModel>) {

        val colorDrawablePack = if (!configuracionAtp?.colorFondo.isNullOrEmpty()) {
            ColorDrawable(Color.parseColor(configuracionAtp?.colorFondo))
        } else {
            ColorDrawable(ContextCompat.getColor(context(), R.color.golden_gana))
        }

        if (!configuracionAtp?.bannerOferta.isNullOrEmpty()) {

            val urlImage = configuracionAtp?.bannerOferta?.let { imageHelper.getResolutionURL(it) }

            val requestOptions = RequestOptions()
                .placeholder(colorDrawablePack)
                .error(colorDrawablePack)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

            Glide.with(ivwBanner)
                .load(urlImage)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivwBanner)

        } else {
            ivwBanner.setImageDrawable(colorDrawablePack)
        }

        val colorTextPack = if (!configuracionAtp?.colorTexto.isNullOrEmpty()) {
            Color.parseColor(configuracionAtp?.colorTexto)
        } else {
            ContextCompat.getColor(context(), R.color.white)
        }

        tvwTitleBanner.setTextColor(colorTextPack)
        tvwSubTitleBanner.setTextColor(colorTextPack)

        val titleBanner = if (configuracionAtp?.subTitulo?.isEmpty() == true) {
            getString(R.string.arma_tu_pack_banner_subtitle_default)
        } else configuracionAtp?.subTitulo

        var subTitleBanner = ""
        products.forEachIndexed { index, element ->
            subTitleBanner += "${element.factorQuadrant} ${element.name}"
            if ((products.size - 1) != index) subTitleBanner += " + "
        }

        fltContainerBanner.visibility = View.VISIBLE
        tvwTitleBanner.text = titleBanner
        tvwSubTitleBanner.text = subTitleBanner

    }

    /**
     * Public functions
     */

    fun setGroupProduct(oferta: Oferta, groupProduct: ArrayList<CategoryToneModel>, productHistory: ArrayList<ToneModel>) {

        isUpdateAtp = false
        setupTextButton()

        listener?.resetAtpRemoved()
        ofertaAtp = oferta
        val sizePicked = groupProduct.sumBy { it.factorQuadrant }
        totalQuadrant = sizePicked
        pickedProduct.setTotalSize(sizePicked)
        pickedProduct.resetPosition()

        categoryTagList.setList(groupProduct, productHistory)

    }

    fun refreshTodo() {
        init()
    }

    fun refreshAtp() {
        presenter.getProductPack(typeLever)
    }

    fun refreshSchedule() {
        presenter.refreshSchedule()
    }

    /**
     * Listener
     */

    interface Listener {

        fun setScreenTitle(title: String)
        fun showLoading()
        fun hideLoading()
        fun showErrorScreen(type: Int)
        fun updateOffersCount(count: Int)
        fun resetAtpRemoved()
        fun goToOffers()
    }

    /**
     * Static constants/functions
     */

    companion object {

        val TAG: String = ArmaTuPackFragment::class.java.simpleName
        const val ATP_REQUEST_CODE = 191
        const val ATP_QUANTITY_KEY = "ATP_QUANTITY_KEY"
        const val ATP_IS_UPDATE_KEY = "ATP_IS_UPDATE_KEY"
        const val ATP_PRODUCT_KEY = "ATP_PRODUCT_KEY"

        fun newInstance(): ArmaTuPackFragment = ArmaTuPackFragment()

    }
}

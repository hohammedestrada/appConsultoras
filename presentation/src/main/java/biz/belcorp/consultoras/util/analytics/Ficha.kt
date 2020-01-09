package biz.belcorp.consultoras.util.analytics

import android.os.Bundle
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.ficha.FichaSectionEnriquecidaModel
import biz.belcorp.consultoras.common.model.product.ProductCUVModel
import biz.belcorp.consultoras.common.model.promotion.PromotionDetailModel
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.util.Belcorp
import biz.belcorp.consultoras.util.Constant
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.StringUtil.unAccent
import biz.belcorp.consultoras.util.anotation.AnalyticsCategoryType
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.util.CurrencyUtil
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*


object Ficha: SafeLet {

    /** events name's */
    private const val OUT_EVENT = "out_detail"
    private const val SHARE_EVENT = "share_detail"
    private const val SELECT_EVENT = "select_option"
    private const val CHANGE_EVENT = "change_option"
    private const val CLICK_BUTTON = "click_button"
    private const val CHOOSE_EVENT = "choose_option"
    private const val POPUP_APPLY_EVENT = "popup_apply_selection"
    private const val VIRTUAL_EVENT = "virtualEvent"

    /** category description */
    private val CATEGORY_DESC_TEST = "${Constant.BRAND_FOCUS_NAME} | Detalle de producto"
    private val CATEGORY_RESUMIDA_DESC = "${Constant.BRAND_FOCUS_NAME} | Ficha Resumida"
    private val CATEGORY_DESC = "Contenedor - Ficha de Producto"

    /** actions description */
    private const val OUT_DETAIL_DESC = "Salir del detalle del producto"
    private const val SHARE = "Compartir"
    private const val SELECT_OPTION_DESC = "Aplicar Tono Paleta"
    private const val CHANGE_OPTION_DESC = "Cambiar Opcion"
    private const val CLICK_BOTON = "Click Botón"
    private const val CHANGE_CLIENT = "Cambiar Cliente"
    private const val MODIFY = "Modificar producto"
    private const val CHOOSE = "Elegir Opcion"

    private const val VIEW_POPUP_DESC = "Panel Tono - Cambiar opción"
    private const val POPUP_SELECT_OPTION_DESC = "Aplicar Tono Paleta"
    private const val POPUP_APPLY_DESC = "Aplicar Tono Panel"

    /** label description **/
    private const val CANTIDAD = "Cantidad"
    private const val CLIENTE = "Cliente"
    private const val TONO = "Tono"
    private const val FICHA = "Ficha"

    fun view(item: Oferta, user: User?, analytics: Analytics ) {

        user?.let {

            val bundles = ArrayList<Bundle>()
            val product = Bundle()
            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.nombreOferta)
            product.putDouble(FirebaseAnalytics.Param.PRICE,  item.precioCatalogo ?: 0.0)
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, unAccent(item.nombreMarca?:""))
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(it.countryISO ?: ""))
            product.putString(Ofertas.DIMENSION16, analytics.dimension16)
            product.putString(Ofertas.DIMENSION17, FICHA)
            product.putString(Ofertas.DIMENSION18, analytics.dimension18)

            bundles.add(product)
            val ecommerceBundle = Bundle()
            ecommerceBundle.putParcelableArrayList(GlobalConstant.ITEMS, bundles)

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.VIEW_ITEM, ecommerceBundle)

        }

    }

    fun viewItemsCarousel(user: User, item: Oferta, analytics: Analytics) {

        val bundles = ArrayList<Bundle>()

        val product = Bundle()
        product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
        product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.nombreOferta)
        product.putDouble(FirebaseAnalytics.Param.PRICE, item.precioCatalogo ?: 0.0)
        product.putString(FirebaseAnalytics.Param.ITEM_BRAND, unAccent(item.nombreMarca?:""))
        product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(user.countryISO ?: ""))
        product.putLong(FirebaseAnalytics.Param.INDEX, item.index.toLong()+1)
        product.putString(Ofertas.DIMENSION16, analytics.dimension16)
        product.putString(Ofertas.DIMENSION17, analytics.dimension17)
        product.putString(Ofertas.DIMENSION18, analytics.dimension18)

        bundles.add(product)

        val ecommerceBundle = Bundle()
        ecommerceBundle.putParcelableArrayList (GlobalConstant.ITEMS, bundles)
        ecommerceBundle.putString(FirebaseAnalytics.Param.ITEM_LIST, analytics.list)
        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, ecommerceBundle)

    }

    fun clicItemCarousel(user: User, item: Oferta, pos: Int, analytics: Analytics) {

        val bundles = ArrayList<Bundle>()

        val product = Bundle()
        product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
        product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.nombreOferta)
        product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.ITEM_BRAND, unAccent(item.nombreMarca?:""))
        product.putDouble(FirebaseAnalytics.Param.PRICE, item.precioCatalogo ?: 0.00)
        product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil
            .getCurrencyByISO(user.countryISO ?: ""))
        product.putLong(FirebaseAnalytics.Param.INDEX, (pos + 1).toLong())    // Se maneja posicion de 1 a N
        product.putString(Ofertas.DIMENSION16, analytics.dimension16)
        product.putString(Ofertas.DIMENSION17, analytics.dimension17)
        product.putString(Ofertas.DIMENSION18, analytics.dimension18)

        bundles.add(product)
        val ecommerce = Bundle()
        ecommerce.putParcelableArrayList(GlobalConstant.ITEMS, bundles)
        ecommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, analytics.list)

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.SELECT_CONTENT, ecommerce)
    }

    fun addItemCarousel(user: User?, item: ProductCUV, analytics: Analytics){

        user?.let {

            val bundles = ArrayList<Bundle>()

            val  product = Bundle()
            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.description)
            product.putDouble(FirebaseAnalytics.Param.PRICE, item.precioCatalogo ?: 0.00)
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, unAccent(Belcorp.getBrand(item.descripcionMarca, item.marcaId)))
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(it.countryISO ?: ""))
            product.putLong(FirebaseAnalytics.Param.QUANTITY, item.cantidad?.toLong() ?: 0L)
            product.putString(Ofertas.DIMENSION16, analytics.dimension16)
            product.putString(Ofertas.DIMENSION17, analytics.dimension17)
            product.putString(Ofertas.DIMENSION18, analytics.dimension18)

            bundles.add(product)
            val ecommerceBundle = Bundle()
            ecommerceBundle.putParcelableArrayList ("items", bundles)
            ecommerceBundle.putString(FirebaseAnalytics.Param.ITEM_LIST, analytics.list)
            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.ADD_TO_CART, ecommerceBundle)

        }

    }

    fun share() {
        val event = Bundle()
        event.putString(GlobalConstant.EVENT_VAR_CATEGORY, CATEGORY_DESC)
        event.putString(GlobalConstant.EVENT_VAR_ACTION, SHARE)
        event.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE)
        event.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_GANA_MAS_DETAIL)
        event.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(SHARE_EVENT, event)
    }

    fun choose(item: Componente) {

        val params = Bundle()

        params.putString(GlobalConstant.EVENT_VAR_CATEGORY, CATEGORY_DESC)
        params.putString(GlobalConstant.EVENT_VAR_ACTION, CHOOSE)
        params.putString(GlobalConstant.EVENT_VAR_LABEL, item.nombreComercial)
        params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_GANA_MAS_DETAIL)
        params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(CHOOSE_EVENT, params)
    }

    fun applyOption(accessFrom: Int, item: Componente) {

        val params = Bundle()

        params.putString(GlobalConstant.EVENT_VAR_CATEGORY, CATEGORY_DESC)
        params.putString(GlobalConstant.EVENT_VAR_ACTION, POPUP_APPLY_DESC)
        params.putString(GlobalConstant.EVENT_VAR_LABEL, item.nombreComercial)
        params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_GANA_MAS_DETAIL)
        params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(POPUP_APPLY_EVENT, params)
    }

    fun select(accessFrom: Int, item: Componente, option: Opciones) {

        val params = Bundle()

        params.putString(GlobalConstant.EVENT_VAR_CATEGORY, CATEGORY_DESC)
        params.putString(GlobalConstant.EVENT_VAR_ACTION, SELECT_OPTION_DESC)
        params.putString(GlobalConstant.EVENT_VAR_LABEL, "${item.nombreComercial} - ${option.nombreOpcion}")
        params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_GANA_MAS_DETAIL)
        params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(SELECT_EVENT, params)
    }

    fun change(accessFrom: Int, item: Componente) {

        val params = Bundle()

        params.putString(GlobalConstant.EVENT_VAR_CATEGORY, CATEGORY_DESC)
        params.putString(GlobalConstant.EVENT_VAR_ACTION, CHANGE_OPTION_DESC)
        params.putString(GlobalConstant.EVENT_VAR_LABEL, item.nombreComercial)
        params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_GANA_MAS_DETAIL)
        params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(CHANGE_EVENT, params)
    }

    fun viewDetailfromGanaMas(item: Componente) {
        val detail = Bundle()
        detail.putString(GlobalConstant.EVENT_VAR_CATEGORY, CATEGORY_DESC)
        detail.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_VER_DETALLE)
        detail.putString(GlobalConstant.EVENT_VAR_LABEL, "not set")
        detail.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_GANA_MAS_DETAIL)
        detail.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(VIRTUAL_EVENT, detail)
    }

    fun clickTab(ficha: FichaSectionEnriquecidaModel, item: Componente){
        val params = Bundle()
        params.putString(GlobalConstant.EVENT_VAR_CATEGORY, CATEGORY_DESC)
        params.putString(GlobalConstant.EVENT_VAR_ACTION, "Ver-${ficha.title}")
        params.putString(GlobalConstant.EVENT_VAR_LABEL, item.nombreComercial)
        params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_GANA_MAS_DETAIL)
        params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(VIRTUAL_EVENT, params)
    }

    fun clickTabFromProductDetail(ficha: FichaSectionEnriquecidaModel, item: Componente){
        val params = Bundle()
        params.putString(GlobalConstant.EVENT_VAR_CATEGORY, CATEGORY_DESC)
        params.putString(GlobalConstant.EVENT_VAR_ACTION, "Ver-${ficha.title}")
        params.putString(GlobalConstant.EVENT_VAR_LABEL, item.nombreComercial)
        params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_INFORMATION_PRODUCT)
        params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(VIRTUAL_EVENT, params)
    }

    fun clickVideo(item: Componente){
        val params = Bundle()
        params.putString(GlobalConstant.EVENT_VAR_CATEGORY, CATEGORY_DESC)
        params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_INIT_VIDEO)
        params.putString(GlobalConstant.EVENT_VAR_LABEL, item.nombreComercial)
        params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_GANA_MAS_DETAIL)
        params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(VIRTUAL_EVENT, params)
    }

    fun clickVideoFromProductDetail(item: Componente){
        val params = Bundle()
        params.putString(GlobalConstant.EVENT_VAR_CATEGORY, CATEGORY_DESC)
        params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_INIT_VIDEO)
        params.putString(GlobalConstant.EVENT_VAR_LABEL, item.nombreComercial)
        params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_INFORMATION_PRODUCT)
        params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(VIRTUAL_EVENT, params)
    }

    fun changeClient(){
        val event = Bundle()
        event.putString(GlobalConstant.EVENT_VAR_CATEGORY, CATEGORY_RESUMIDA_DESC) // TODO
        event.putString(GlobalConstant.EVENT_VAR_ACTION, CLICK_BOTON)
        event.putString(GlobalConstant.EVENT_VAR_LABEL, CHANGE_CLIENT)
        event.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_FICHA_RESUMIDA)
        event.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(CLICK_BUTTON, event)
    }

    fun modify(flagCantidad: Boolean, flagClient: Boolean, flagTono: Boolean){
        val event = Bundle()
        event.putString(GlobalConstant.EVENT_VAR_CATEGORY, AnalyticsCategoryType.PEDIDO)
        event.putString(GlobalConstant.EVENT_VAR_ACTION, MODIFY)

        var label = ""

        if(flagCantidad)
            label+=CANTIDAD

        if(flagClient)
            label += if(label.isNotEmpty())
                " | $CLIENTE"
            else
                CLIENTE

        if(flagTono)
            label += if(label.isNotEmpty())
                " | $TONO"
            else
                TONO


        event.putString(GlobalConstant.EVENT_VAR_LABEL, label)
        event.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_INGRESAR_PEDIDO)
        event.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_MODIFICAR_FICHA_RESUMIDA, event)
    }

    fun clickBannerPromotion(item: PromotionDetailModel) {
        val params = Bundle()
        params.putString(GlobalConstant.EVENT_VAR_CATEGORY, CATEGORY_DESC)
        params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_CLICK_PROMOTION)
        params.putString(GlobalConstant.EVENT_VAR_LABEL, item.descripcionCortada)
        params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_INFORMATION_PRODUCT)
        params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(VIRTUAL_EVENT, params)
    }

    fun addProductPromotion(user: User?, item: ProductCUV) {
        user?.let {
            val product = Bundle()
            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.description)
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, item.descripcionCategoria)
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.VARIANT_ADD_TO_CART)
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, Belcorp.getBrand(item.descripcionMarca, item.marcaId))
            product.putDouble(FirebaseAnalytics.Param.PRICE, item.precioCatalogo ?: 0.00)
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(user.countryISO ?: ""))
            product.putLong(FirebaseAnalytics.Param.QUANTITY, item.cantidad?.toLong() ?: 0L)


            val ecommerce = Bundle()
            ecommerce.putBundle(GlobalConstant.ITEMS, product)

            ecommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, GlobalConstant.FICHA_PROMOCION)

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.ADD_TO_CART, ecommerce)
        }
    }

}


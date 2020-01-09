package biz.belcorp.consultoras.util.analytics

import android.os.Bundle
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.common.model.orders.OfertaFinalModel
import biz.belcorp.consultoras.common.model.product.ProductItem
import biz.belcorp.consultoras.domain.entity.Analytics
import biz.belcorp.consultoras.domain.entity.EstrategiaCarrusel
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.util.Belcorp
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.analytics.Ofertas.DIMENSION16
import biz.belcorp.consultoras.util.analytics.Ofertas.DIMENSION17
import biz.belcorp.consultoras.util.analytics.Ofertas.DIMENSION18
import biz.belcorp.consultoras.util.anotation.AnalyticsCategoryType
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.CurrencyUtil
import biz.belcorp.library.util.StringUtil
import biz.belcorp.mobile.components.offers.model.OfferModel
import com.google.firebase.analytics.FirebaseAnalytics
import biz.belcorp.consultoras.util.StringUtil as StringUtilApp

object AddOrder {

    fun addToCartOffer(user: User?, item: ProductCUV, nameList: String?, descriptionPalanca: String){
        val product = Bundle()
        product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
        product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.description)
        product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.ITEM_BRAND, StringUtilApp.unAccent(Belcorp.getBrand(item.descripcionMarca, item.marcaId)))
        product.putString(FirebaseAnalytics.Param.PRICE, item.precioCatalogo.toString())
        product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(user?.countryISO ?: StringUtil.Empty))
        product.putLong(FirebaseAnalytics.Param.QUANTITY, item.cantidad?.toLong() ?: 0L)
        product.putString(DIMENSION16, descriptionPalanca)
        product.putString(DIMENSION17, null)
        product.putString(DIMENSION18, GlobalConstant.NOT_AVAILABLE)

        val eCommerce = Bundle()

        eCommerce.putBundle(GlobalConstant.ITEMS, product)

        eCommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, nameList)

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.ADD_TO_CART, eCommerce)
    }

    fun impressionOffers(user: User?, items: List<ProductCUV>, nameList: String?){
        val bundles = ArrayList<Bundle>()

        items?.forEach{ item ->
            val product = Bundle()
            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.description)
            product.putString(FirebaseAnalytics.Param.PRICE, item.precioCatalogo.toString())
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, StringUtilApp.unAccent(item.descripcionMarca ?: StringUtil.Empty))
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(user?.countryISO ?: StringUtil.Empty))
            product.putLong(FirebaseAnalytics.Param.INDEX, (item.index + 1).toLong())
            product.putString(DIMENSION16, item.tipoPersonalizacion ?: GlobalConstant.NOT_AVAILABLE)
            product.putString(DIMENSION17, null)
            product.putString(DIMENSION18, GlobalConstant.NOT_AVAILABLE)

            bundles.add(product)
        }
        val eCommerce = Bundle()

        eCommerce.putParcelableArrayList(GlobalConstant.ITEMS, bundles)

        eCommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, nameList)

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, eCommerce)
    }

    fun impressionFinalOffers(countryISO: String?, items: List<OfertaFinalModel>, nameList: String?) {

        val bundles = ArrayList<Bundle>()

        items?.forEach{ item ->
            val product = Bundle()
            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.nombreComercial)
            product.putString(FirebaseAnalytics.Param.PRICE, item.precioCatalogo.toString())
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, StringUtilApp.unAccent(item.nombreMarca ?: StringUtil.Empty))
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(countryISO ?: StringUtil.Empty))
            product.putLong(FirebaseAnalytics.Param.INDEX, (item.index + 1).toLong())
            product.putString(DIMENSION16,  GlobalConstant.NOT_AVAILABLE)
            product.putString(DIMENSION17, null)
            product.putString(DIMENSION18, GlobalConstant.NOT_AVAILABLE)

            bundles.add(product)
        }

        val eCommerce = Bundle()

        eCommerce.putParcelableArrayList(GlobalConstant.ITEMS, bundles)

        eCommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, nameList)

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, eCommerce)
    }

    fun impressionDigitalOffers(countryISO: String?, items: List<EstrategiaCarrusel>, nameList: String?){
        val bundles = ArrayList<Bundle>()

        items?.forEach{ item ->
            val product = Bundle()
            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.descripcionCUV)
            product.putString(FirebaseAnalytics.Param.PRICE, item.precioFinal.toString())
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, StringUtilApp.unAccent(item.descripcionMarca ?: StringUtil.Empty))
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(countryISO ?: StringUtil.Empty))
            product.putLong(FirebaseAnalytics.Param.INDEX, (item.index + 1).toLong())
            product.putString(DIMENSION16, item.nombrePersonalizacion ?: GlobalConstant.NOT_AVAILABLE)
            product.putString(DIMENSION17, null)
            product.putString(DIMENSION18, GlobalConstant.NOT_AVAILABLE)

            bundles.add(product)
        }
        val eCommerce = Bundle()

        eCommerce.putParcelableArrayList(GlobalConstant.ITEMS, bundles)

        eCommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, nameList)

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, eCommerce)
    }

    fun clickOffer(countryISO: String?, item: EstrategiaCarrusel, nameList: String?, descriptionPalanca: String){

        val product = Bundle()
        product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
        product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.descripcionCUV)
        product.putString(FirebaseAnalytics.Param.PRICE, item.precioFinal.toString())
        product.putString(FirebaseAnalytics.Param.ITEM_BRAND, StringUtilApp.unAccent(item.descripcionMarca ?: StringUtil.Empty))
        product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(countryISO ?: StringUtil.Empty))
        product.putLong(FirebaseAnalytics.Param.INDEX, (item.index + 1).toLong())
        product.putString(DIMENSION16, descriptionPalanca)
        product.putString(DIMENSION17, null)
        product.putString(DIMENSION18, GlobalConstant.NOT_AVAILABLE)

        val eCommerce = Bundle()

        eCommerce.putBundle(GlobalConstant.ITEMS, product)

        eCommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, nameList)

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.SELECT_CONTENT, eCommerce)
    }

    fun clickOffer(countryISO: String?, item: OfferModel, nameList: String?, descriptionPalanca: String) {

        val product = Bundle()
        product.putString(FirebaseAnalytics.Param.ITEM_ID, item.key)
        product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.productName)
        product.putString(FirebaseAnalytics.Param.PRICE, item.personalAmount)
        product.putString(FirebaseAnalytics.Param.ITEM_BRAND, StringUtilApp.unAccent(item.brand))
        product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(countryISO ?: StringUtil.Empty))
        product.putLong(FirebaseAnalytics.Param.INDEX, (item.index + 1).toLong())
        product.putString(DIMENSION16, descriptionPalanca)
        product.putString(DIMENSION17, null)
        product.putString(DIMENSION18, GlobalConstant.NOT_AVAILABLE)

        val eCommerce = Bundle()

        eCommerce.putBundle(GlobalConstant.ITEMS, product)

        eCommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, nameList)

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.SELECT_CONTENT, eCommerce)
    }

    fun addToCartFinalOffer(countryISO: String?, item: OfertaFinalModel, nameList: String?) {

        val product = Bundle()
        product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
        product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.nombreComercial)
        product.putString(FirebaseAnalytics.Param.PRICE, item.precioCatalogo.toString())
        product.putString(FirebaseAnalytics.Param.ITEM_BRAND, StringUtilApp.unAccent(item.nombreMarca ?: StringUtil.Empty))
        product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(countryISO ?: StringUtil.Empty))
        product.putLong(FirebaseAnalytics.Param.QUANTITY, (item.cantidad ?: 0).toLong())
        product.putString(DIMENSION16, GlobalConstant.NOT_AVAILABLE)
        product.putString(DIMENSION17, null)
        product.putString(DIMENSION18, GlobalConstant.NOT_AVAILABLE)

        val eCommerce = Bundle()

        eCommerce.putBundle(GlobalConstant.ITEMS, product)

        eCommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, nameList)

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.ADD_TO_CART, eCommerce)
    }

    fun removeToCart(item: ProductItem, nameList: String?, descriptionPalanca: String) {

        val product = Bundle()
        product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
        product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.descripcionProd)
        product.putString(FirebaseAnalytics.Param.PRICE, (item.precioUnidad ?: 0).toString())
        product.putString(FirebaseAnalytics.Param.ITEM_BRAND, GlobalConstant.NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
        product.putLong(FirebaseAnalytics.Param.QUANTITY, (item.cantidad ?: 0).toLong())
        product.putString(DIMENSION16,  descriptionPalanca)
        product.putString(DIMENSION17, null)
        product.putString(DIMENSION18, GlobalConstant.NOT_AVAILABLE)

        val eCommerce = Bundle()

        eCommerce.putBundle(GlobalConstant.ITEMS, product)

        nameList?.let {
            eCommerce.putString(FirebaseAnalytics.Param.ITEM_LIST, nameList)
        }

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART, eCommerce)
    }

    fun buttonViewMore(){
        val product = Bundle()
        product.putString(GlobalConstant.EVENT_VAR_CATEGORY, AnalyticsCategoryType.PEDIDO)
        product.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_VER_MAS)
        product.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_OFERTAS_PARA_TI)
        product.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_INGRESAR_PEDIDO)
        product.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_VER_MAS, product)
    }

    fun buttonSaveReserve(label: String){
        val product = Bundle()
        product.putString(GlobalConstant.EVENT_VAR_CATEGORY, AnalyticsCategoryType.PEDIDO)
        product.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_CLIC_BOTON)
        product.putString(GlobalConstant.EVENT_VAR_LABEL, label)
        product.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_INGRESAR_PEDIDO)
        product.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_GUARDAR_PEDIDO, product)
    }

    fun buttonTabsOrderGanamas(labelEvent: String){
        val product = Bundle()
        product.putString(GlobalConstant.EVENT_VAR_CATEGORY, AnalyticsCategoryType.PEDIDO)
        product.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_CLIC_TAB)
        product.putString(GlobalConstant.EVENT_VAR_LABEL, labelEvent)
        product.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_INGRESAR_PEDIDO)
        product.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_TAB_PEDIDO_GANAMAS, product)
    }

    fun buttonTabsClientsUnits(labelEvent: String){
        val product = Bundle()
        product.putString(GlobalConstant.EVENT_VAR_CATEGORY, AnalyticsCategoryType.PEDIDO)
        product.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_CLIC_TAB)
        product.putString(GlobalConstant.EVENT_VAR_LABEL, labelEvent)
        product.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_INGRESAR_PEDIDO)
        product.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_TAB_CLIENTES_UNIDADES, product)
    }

    fun buttonSelectClient(){
        val product = Bundle()
        product.putString(GlobalConstant.EVENT_VAR_CATEGORY, AnalyticsCategoryType.PEDIDO)
        product.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_SELECCIONAR_CLIENTE)
        product.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.NOT_AVAILABLE)
        product.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_INGRESAR_PEDIDO)
        product.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_SELECCIONA_CLIENTE, product)
    }

    fun buttonSearch(label: String){
        val product = Bundle()
        product.putString(GlobalConstant.EVENT_VAR_CATEGORY, AnalyticsCategoryType.PEDIDO)
        product.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_CLIC_BOTON)
        product.putString(GlobalConstant.EVENT_VAR_LABEL, label)
        product.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_INGRESAR_PEDIDO)
        product.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_INGRESAR_BUSQUEDA, product)
    }

    fun buttonSelectProduct(nameProduct: String?){
        val product = Bundle()
        product.putString(GlobalConstant.EVENT_VAR_CATEGORY, AnalyticsCategoryType.PEDIDO)
        product.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_VISUALIZAR_PRODUCTO)
        product.putString(GlobalConstant.EVENT_VAR_LABEL, nameProduct)
        product.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_INGRESAR_PEDIDO)
        product.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_SELECCIONAR_PRODUCTO_LISTA, product)
    }
}

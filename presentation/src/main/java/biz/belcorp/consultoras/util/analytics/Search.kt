package biz.belcorp.consultoras.util.analytics

import android.os.Bundle
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.util.AnalyticsUtil
import biz.belcorp.consultoras.util.Belcorp
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.StringUtil
import biz.belcorp.consultoras.util.analytics.Ofertas.DIMENSION16
import biz.belcorp.consultoras.util.analytics.Ofertas.DIMENSION17
import biz.belcorp.consultoras.util.analytics.Ofertas.DIMENSION18
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.util.CurrencyUtil
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*

object Search : SafeLet {

    private const val PALANCA_BUSCADOR = "Buscador - "
    private const val PALANCA_BUSCADOR_POR_CUV = " por CUV"
    private const val GUION_CON_ESPACIOS = " - "
    const val NOT_AVAILABLE = "(not available)"
    private const val NULL = "null"
    const val MIDDLE_DASH = "-"

    /**
    Estas variables deben cambiarse para que sean usadas desde el archivo json del analytics
     **/

    const val EVENT_SEARCH_SELECT = "seleccionar_buscador"
    const val EVENT_SEARCH_NOT_FOUND = "busqueda_sin_resultados"
    const val EVENT_SEARCH_RESULTS = "busqueda_con_resultados"
    const val EVENT_SEARCH_RESULTS_MORE = "ver_mas_resultados"
    const val EVENT_SEARCH_VOICE_RESULT = "voz_con_resultados"
    const val EVENT_SEARCH_VOICE_NOT_FOUND_RESULT = "voz_sin_resultados"
    const val EVENT_SEARCH_RESULT_ADD_FILTER = "agregar_filtros"
    const val EVENT_SEARCH_RESULT_CLEAR_FILTER = "limpiar_filtros"
    const val EVENT_SEARCH_RESULT_APPLY_FILTER = "aplicar_filtros"
    const val EVENT_SEARCH_RESULT_ORDER_BY= "ordenar_productos"

    const val ACTION_SEARCH_SELECT = "Seleccionar"
    const val ACTION_SEARCH_NOT_FOUND = "Busqueda sin Resultados"
    const val ACTION_SEARCH_RESULTS = "Busqueda Satisfactoria"
    const val ACTION_SEARCH_RESULTS_MORE = "Ver mas Resultados"
    const val ACTION_SEARCH_VOICE = "Busqueda por Voz Satisfactoria"
    const val ACTION_SEARCH_VOICE_NOT_FOUND = "Busqueda por Voz sin Resultado"
    const val ACTION_ADD_FILTER_BY = "Agregar Filtro de "
    const val ACTION_CLEAR_FILTER = "Limpiar Filtros"
    const val ACTION_APPLY_FILTER = "Aplicar Filtros"
    const val ACTION_ORDER_BY = "Ordenar por"




    fun printEventSearch(category: String, action: String, label: String, screen: String, event: String) {

        val bundle = Bundle()

        bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, category)
        bundle.putString(GlobalConstant.EVENT_VAR_ACTION, action)
        bundle.putString(GlobalConstant.EVENT_VAR_LABEL, label)
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, screen)
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(event, bundle)

    }

    fun printAddProduct(item: ProductCUV, model: LoginModel, cantidad: Int, lista: String?, palancaDescripcion : String) {
        val bundles = ArrayList<Bundle>()
        val product = Bundle()

        product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
        product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.description)
        product.putString(FirebaseAnalytics.Param.PRICE, item.precioCatalogo.toString())
        product.putString(FirebaseAnalytics.Param.ITEM_BRAND, StringUtil.unAccent(Belcorp.getBrand(item.descripcionMarca, item.marcaId)))
        product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, item.descripcionCategoria
            ?: NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(model.countryISO))
        product.putLong(FirebaseAnalytics.Param.QUANTITY, cantidad.toLong())

        product.putString(DIMENSION16, palancaDescripcion)
        product.putString(DIMENSION17, NULL)
        product.putString(DIMENSION18, NOT_AVAILABLE)

        bundles.add(product)
        val ecommerceBundle = Bundle()
        ecommerceBundle.putParcelableArrayList(GlobalConstant.ITEMS, bundles)
        ecommerceBundle.putString(FirebaseAnalytics.Param.ITEM_LIST, lista)

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.ADD_TO_CART, ecommerceBundle)
    }


    fun printClickProduct(item: ProductCUV, model: LoginModel, posicion: Int, lista: String?, palancaDescripcion : String) {
        val bundles = ArrayList<Bundle>()
        val product = Bundle()

        product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
        product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.description)
        product.putString(FirebaseAnalytics.Param.PRICE, item.precioCatalogo.toString())
        product.putString(FirebaseAnalytics.Param.ITEM_BRAND, StringUtil.unAccent(Belcorp.getBrand(item.descripcionMarca, item.marcaId)))
        product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, item.descripcionCategoria
            ?: NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(model.countryISO))
        product.putLong(FirebaseAnalytics.Param.INDEX, (posicion + 1).toLong())

        product.putString(DIMENSION16, palancaDescripcion)
        product.putString(DIMENSION17, NULL)
        product.putString(DIMENSION18, NOT_AVAILABLE)

        bundles.add(product)
        val ecommerceBundle = Bundle()
        ecommerceBundle.putParcelableArrayList(GlobalConstant.ITEMS, bundles)
        ecommerceBundle.putString(FirebaseAnalytics.Param.ITEM_LIST, lista)

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.SELECT_CONTENT, ecommerceBundle)
    }

    fun printProduct(item: ProductCUV, model: LoginModel, posicion: Int, lista: String?, palancaDescripcion : String) {
        val bundles = ArrayList<Bundle>()
        val product = Bundle()

        product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
        product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.description)
        product.putString(FirebaseAnalytics.Param.PRICE, item.precioCatalogo.toString())
        product.putString(FirebaseAnalytics.Param.ITEM_BRAND, StringUtil.unAccent(Belcorp.getBrand(item.descripcionMarca, item.marcaId)))
        product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, item.descripcionCategoria
            ?: NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, NOT_AVAILABLE)
        product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(model.countryISO))
        product.putLong(FirebaseAnalytics.Param.INDEX, (posicion + 1).toLong())


        product.putString(DIMENSION16, palancaDescripcion)
        product.putString(DIMENSION17, NULL)
        product.putString(DIMENSION18, NOT_AVAILABLE)

        bundles.add(product)
        val ecommerceBundle = Bundle()
        ecommerceBundle.putParcelableArrayList(GlobalConstant.ITEMS, bundles)
        ecommerceBundle.putString(FirebaseAnalytics.Param.ITEM_LIST, lista)

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, ecommerceBundle)

    }


    fun trackAddProduct(model: LoginModel, item: ProductCUV, searchByCuv: Boolean,
                        palanca: String, textSearched: String) {

        val product = Bundle()

        product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
        product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.description)
        product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, item.descripcionCategoria)
        product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, textSearched)
        product.putString(FirebaseAnalytics.Param.ITEM_BRAND, StringUtil.unAccent(Belcorp.getBrand(item.descripcionMarca, item.marcaId)))
        product.putDouble(FirebaseAnalytics.Param.PRICE, item.precioCatalogo ?: 0.0)
        product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(model.countryISO))
        product.putLong(FirebaseAnalytics.Param.QUANTITY, item.cantidad!!.toLong())

        if (searchByCuv) {
            product.putString(GlobalConstant.PALANCA, PALANCA_BUSCADOR + item.tipoPersonalizacion + GUION_CON_ESPACIOS + palanca + PALANCA_BUSCADOR_POR_CUV)
        } else {
            product.putString(GlobalConstant.PALANCA, PALANCA_BUSCADOR + item.tipoPersonalizacion + GUION_CON_ESPACIOS + palanca)
        }

        val properties = AnalyticsUtil.getUserProperties(model)
        val eCommerce = Bundle()
        eCommerce.putBundle(GlobalConstant.ITEMS, product)

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.ADD_TO_CART, eCommerce, properties)
    }

}

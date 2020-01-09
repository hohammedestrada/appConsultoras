package biz.belcorp.consultoras.util.analytics

import android.os.Bundle
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.domain.entity.Analytics
import biz.belcorp.consultoras.domain.entity.Oferta
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.util.OfferTypes
import biz.belcorp.consultoras.feature.home.marquee.MarqueeItem
import biz.belcorp.consultoras.util.AnalyticsUtil
import biz.belcorp.consultoras.util.Belcorp
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.StringUtil.unAccent
import biz.belcorp.consultoras.util.anotation.OffersOriginTypeLocation
import biz.belcorp.library.analytics.BelcorpAnalytics
import biz.belcorp.library.util.CurrencyUtil
import biz.belcorp.library.util.StringUtil
import biz.belcorp.mobile.components.design.sections.model.SectionModel
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*


object Ofertas {

    const val DIMENSION16 = "dimension16"
    const val DIMENSION17 = "dimension17"
    const val DIMENSION18 = "dimension18"
    const val CONTENEDOR = "Contenedor - "
    const val LANDING = "Landing "

    fun addOfferFromHome(user: User?, analytics: Analytics, cantidad: Int, item: ProductCUV) {

        user?.let {

            val itemList: String
            val bundles = ArrayList<Bundle>()
            val product = Bundle()

            if (item.tipoPersonalizacion == OfferTypes.RD || item.tipoPersonalizacion == OfferTypes.OPT) {

                itemList = CONTENEDOR + OfferTypes.OPT
                item.tipoPersonalizacion = OfferTypes.OPT

            } else
                itemList = CONTENEDOR + item.tipoPersonalizacion

            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, unAccent(item.description.toString()))
            product.putDouble(FirebaseAnalytics.Param.PRICE,  item.precioCatalogo ?: 0.0)
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, unAccent(item.descripcionMarca.toString()))
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(user.countryISO ?: StringUtil.Empty))
            product.putLong(FirebaseAnalytics.Param.QUANTITY, cantidad.toLong())
            product.putString(DIMENSION16, analytics.dimension16)
            product.putString(DIMENSION17, analytics.dimension17)
            product.putString(DIMENSION18, analytics.dimension18)

            bundles.add(product)
            val ecommerceBundle = Bundle()

            ecommerceBundle.putParcelableArrayList(GlobalConstant.ITEMS, bundles)
            ecommerceBundle.putString(FirebaseAnalytics.Param.ITEM_LIST, itemList)

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.ADD_TO_CART, ecommerceBundle)

        }

    }

    fun addOfferForLanding(user: User?, item: ProductCUV, analytics: Analytics, category: String?) {

        user?.let {

            val  product = Bundle()

            product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.description)
            product.putDouble(FirebaseAnalytics.Param.PRICE, item.precioCatalogo ?: 0.0)
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, unAccent(Belcorp.getBrand(item.descripcionMarca, item.marcaId)))
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category ?: GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(it.countryISO ?: ""))
            product.putLong(FirebaseAnalytics.Param.QUANTITY, (item.cantidad ?: 0).toLong())
            product.putString(DIMENSION16, analytics.dimension16)
            product.putString(DIMENSION17, analytics.dimension17)
            product.putString(DIMENSION18, analytics.dimension18)

            val properties = AnalyticsUtil.getUserProperties(it)
            val ecommerceBundle = Bundle()

            ecommerceBundle.putBundle(GlobalConstant.ITEMS, product)
            ecommerceBundle.putString(FirebaseAnalytics.Param.ITEM_LIST, analytics.list)

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.ADD_TO_CART, ecommerceBundle, properties)
        }
    }

    fun impressionBanner(campaing: String?, name: String, pos: Int, analytics : Analytics) {

        val promotions = ArrayList<Bundle>()
        val promotion = Bundle()

        promotion.putString(FirebaseAnalytics.Param.ITEM_ID, unAccent(campaing.toString()) + "_" + unAccent(name))
        promotion.putString(FirebaseAnalytics.Param.ITEM_NAME, unAccent(analytics.dimension16.toString()))
        promotion.putString(FirebaseAnalytics.Param.CREATIVE_NAME, unAccent(analytics.dimension18.toString()))
        promotion.putString(FirebaseAnalytics.Param.CREATIVE_SLOT, pos.toString())
        promotions.add(promotion)

        val eCommerce = Bundle()

        eCommerce.putParcelableArrayList(GlobalConstant.EVENT_LIST_NAME_PROMOTIONS, promotions)

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.VIEW_ITEM, eCommerce)

    }

    fun clickBanner(campaing: String?, name: String, pos: Int, analytics : Analytics) {

        val promotions = ArrayList<Bundle>()
        val promotion = Bundle()

        promotion.putString(FirebaseAnalytics.Param.ITEM_ID, unAccent(campaing) + "_" + unAccent(name))
        promotion.putString(FirebaseAnalytics.Param.ITEM_NAME, unAccent(analytics.dimension16))
        promotion.putString(FirebaseAnalytics.Param.CREATIVE_NAME, unAccent(LANDING + name))
        promotion.putString(FirebaseAnalytics.Param.CREATIVE_SLOT, pos.toString())
        promotions.add(promotion)

        val eCommerce = Bundle()

        eCommerce.putParcelableArrayList(GlobalConstant.EVENT_LIST_NAME_PROMOTIONS, promotions)

        BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.SELECT_CONTENT, eCommerce)

    }

    fun clickVerMas(analytics: Analytics) {

        val event = Bundle()

        event.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.CATEGORY_VER_MAS)
        event.putString(GlobalConstant.EVENT_VAR_ACTION, unAccent(GlobalConstant.ACTION_VER_MAS))
        event.putString(GlobalConstant.EVENT_VAR_LABEL, unAccent(analytics.dimension16))
        event.putString(GlobalConstant.EVENT_VAR_SCREEN, unAccent(GlobalConstant.SCREEN_GANA_MAS))

        event.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_NAME_VIRTUAL_EVENT, event)

    }

    fun impressionItems(user: User?, items: List<Oferta>, analytics: Analytics) {

        user?.let {

            val bundles = ArrayList<Bundle>()

            for (item in items) {
                val product = Bundle()

                product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
                product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.nombreOferta)
                product.putDouble(FirebaseAnalytics.Param.PRICE, item.precioCatalogo ?: 0.00)
                product.putString(FirebaseAnalytics.Param.ITEM_BRAND, unAccent(item.nombreMarca))
                product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
                product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
                product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(user.countryISO ?: ""))
                product.putLong(FirebaseAnalytics.Param.INDEX, (item.index + 1).toLong()) // Se maneja posicion de 1 a N
                product.putString(DIMENSION16, analytics.dimension16)
                product.putString(DIMENSION17, analytics.dimension17)
                product.putString(DIMENSION18, analytics.dimension18)
                bundles.add(product)

            }

            val ecommerceBundle = Bundle()
            ecommerceBundle.putParcelableArrayList(GlobalConstant.ITEMS, bundles)
            ecommerceBundle.putString(FirebaseAnalytics.Param.ITEM_LIST, analytics.list)

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, ecommerceBundle)

        }
    }

    fun clickItem(user: User?, item: Any, pos: Int?, analytics: Analytics) {

        user?.let {

            val bundles = ArrayList<Bundle>()
            val product = Bundle()

            val cuv = if (item is Oferta) item.cuv else (item as ProductCUV).cuv
            val nombreOferta = if (item is Oferta) item.nombreOferta else (item as ProductCUV).description
            val nombreMarca = if (item is Oferta) item.nombreMarca else (item as ProductCUV).descripcionMarca
            val precioCatalogo = if (item is Oferta) item.precioCatalogo else (item as ProductCUV).precioCatalogo

            product.putString(FirebaseAnalytics.Param.ITEM_ID, cuv)
            product.putString(FirebaseAnalytics.Param.ITEM_NAME, unAccent(nombreOferta.toString()))
            product.putDouble(FirebaseAnalytics.Param.PRICE, precioCatalogo ?: 0.00)
            product.putString(FirebaseAnalytics.Param.ITEM_BRAND, unAccent(nombreMarca.toString()))
            product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
            product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(user.countryISO ?: ""))
            product.putLong(FirebaseAnalytics.Param.INDEX, pos!!.toLong()) // Se maneja posicion de 1 a N
            product.putString(DIMENSION16, analytics.dimension16 )
            product.putString(DIMENSION17, analytics.dimension17)
            product.putString(DIMENSION18, analytics.dimension18)

            bundles.add(product)

            val ecommerceBundle = Bundle()
            val properties = AnalyticsUtil.getUserProperties(user)

            ecommerceBundle.putParcelableArrayList(GlobalConstant.ITEMS, bundles)
            ecommerceBundle.putString(FirebaseAnalytics.Param.ITEM_LIST, analytics.list)

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.SELECT_CONTENT, ecommerceBundle, properties)
        }
    }

    fun categories(user : User?,categories: Map<Int, SectionModel>, impression: Boolean) {

        val promotions = ArrayList<Bundle>()
        user?.let {

            for (key in categories.keys) {
                val section = categories[key]
                val promotion = Bundle()

                val promotionItem = MarqueeItem()

                if (section != null) {
                    promotionItem.id = user.campaing + "_" + section.name
                    promotionItem.name = section.name
                }


                section?.let {
                    promotion.putString(FirebaseAnalytics.Param.ITEM_ID, promotionItem.id)
                    promotion.putString(FirebaseAnalytics.Param.ITEM_NAME, promotionItem.name)
                    promotion.putString(FirebaseAnalytics.Param.CREATIVE_NAME, GlobalConstant.EVENT_PREFIX_LANDING + promotionItem.name)
                    promotion.putString(FirebaseAnalytics.Param.CREATIVE_SLOT,  (key + 1).toString()) // Se maneja posicion de 1 a N
                    promotions.add(promotion)
                }
            }

        }

        val eCommerce = Bundle()

        eCommerce.putParcelableArrayList(GlobalConstant.EVENT_LIST_NAME_PROMOTIONS, promotions)

        if (impression) {
            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.VIEW_ITEM, eCommerce)
        } else {
            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.SELECT_CONTENT, eCommerce)
        }

    }

    fun impressionItemsCategories(user: User?, list: ArrayList<ProductCUV>, analytics: Analytics, categoryName: String) {

        user?.let {

            val bundles = ArrayList<Bundle>()

            for (item in list) {
                val product = Bundle()

                product.putString(FirebaseAnalytics.Param.ITEM_ID, item.cuv)
                product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.description)
                product.putDouble(FirebaseAnalytics.Param.PRICE, item.precioCatalogo ?: 0.00)
                product.putString(FirebaseAnalytics.Param.ITEM_BRAND, unAccent(item.descripcionMarca))
                product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, categoryName)
                product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, GlobalConstant.NOT_AVAILABLE)
                product.putString(FirebaseAnalytics.Param.CURRENCY, CurrencyUtil.getCurrencyByISO(user.countryISO ?: ""))
                product.putLong(FirebaseAnalytics.Param.INDEX, (item.index + 1).toLong()) // Se maneja posicion de 1 a N
                product.putString(DIMENSION16, analytics.dimension16)
                product.putString(DIMENSION17, analytics.dimension17)
                product.putString(DIMENSION18, analytics.dimension18)
                bundles.add(product)

            }

            val ecommerceBundle = Bundle()
            ecommerceBundle.putParcelableArrayList(GlobalConstant.ITEMS, bundles)
            ecommerceBundle.putString(FirebaseAnalytics.Param.ITEM_LIST, analytics.list)

            BelcorpAnalytics.trackEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, ecommerceBundle)

        }

    }

    fun applySort(user: User?, section: String?, detalle: String?, analytics: Analytics) {

        user?.let {

            val params = Bundle()

            OffersOriginTypeLocation.ORIGEN_LANDING

            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, analytics.list)
            params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.ACTION_SORT)
            params.putString(GlobalConstant.EVENT_VAR_LABEL, detalle)
            params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.BRAND_FOCUS_NAME + " | " + section)
            params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)
            val properties = AnalyticsUtil.getUserProperties(user)

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_SORT, params, properties)
        }
    }

    fun applyFilter(user: User?, section: String?, detalle: String?, analytics: Analytics) {

        user?.let {

            val params = Bundle()

            OffersOriginTypeLocation.ORIGEN_LANDING

            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, analytics.list)
            params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.ACTION_FILTER)
            params.putString(GlobalConstant.EVENT_VAR_LABEL, detalle)
            params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.BRAND_FOCUS_NAME + " | " + section)
            params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)
            val properties = AnalyticsUtil.getUserProperties(user)

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_FILTER_APPLY, params, properties)
        }
    }

    fun clickFilterButton(user: User?, section: String?, botonName: String?, analytics: Analytics) {

        user?.let {
            val params = Bundle()

            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, analytics.list)
            params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.ACTION_FILTER_CLEAN)
            params.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.NOT_AVAILABLE)
            params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.BRAND_FOCUS_NAME + " | " + section)
            params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

            val properties = AnalyticsUtil.getUserProperties(user)

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_FILTER_CLEAN, params, properties)
        }
    }

    fun clickFilterItem(filterGroup: String?, user: User?, filter: String?, analytics: Analytics, section: String?) {

        user?.let {

            val params = Bundle()

            OffersOriginTypeLocation.ORIGEN_LANDING

            params.putString(GlobalConstant.EVENT_VAR_CATEGORY, analytics.list)
            params.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.ACTION_FILTER_BY + " $filterGroup")
            params.putString(GlobalConstant.EVENT_VAR_LABEL, filter)
            params.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.BRAND_FOCUS_NAME + " | " + section)
            params.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)
            val properties = AnalyticsUtil.getUserProperties(user)

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_FILTER_ADD, params, properties)
        }
    }

    fun removeFilterItem(user: User?, section: String?, filter: String?) {

        user?.let {

            val event = Bundle()

            event.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_GANA_MAS + " | " + section)
            event.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.ACTION_REMOVE_FILTER)
            event.putString(GlobalConstant.EVENT_VAR_LABEL, filter)
            event.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_GANA_MAS + " | " + section)
            event.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS)

            val properties = AnalyticsUtil.getUserProperties(user)

            BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_FILTER_AND_SORT, event, properties)

        }

    }

}

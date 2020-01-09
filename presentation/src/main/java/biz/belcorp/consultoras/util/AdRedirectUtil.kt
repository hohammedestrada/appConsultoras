package biz.belcorp.consultoras.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import biz.belcorp.consultoras.feature.announcement.AnnouncementActivity
import biz.belcorp.consultoras.feature.catalog.CatalogContainerActivity
import biz.belcorp.consultoras.feature.embedded.academia.AcademiaActivity
import biz.belcorp.consultoras.feature.embedded.changes.ChangesActivity
import biz.belcorp.consultoras.feature.embedded.closeout.CloseoutActivity
import biz.belcorp.consultoras.feature.embedded.offers.OffersActivity
import biz.belcorp.consultoras.feature.embedded.ordersfic.OrdersFicActivity
import biz.belcorp.consultoras.feature.home.HomeActivity
import biz.belcorp.consultoras.feature.home.accountstate.AccountStateActivity
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity
import biz.belcorp.consultoras.feature.home.myorders.MyOrdersActivity
import biz.belcorp.consultoras.feature.home.profile.MyProfileActivity
import biz.belcorp.consultoras.feature.stockouts.StockoutsActivity
import biz.belcorp.consultoras.util.anotation.AdRedirectCode
import biz.belcorp.consultoras.util.anotation.PageUrlType
import biz.belcorp.consultoras.util.anotation.RedirectionStories

class AdRedirectUtil {

    companion object {

        fun open(context: Context, adRedirectCode: String, bundle: Bundle? = null) {
            when (adRedirectCode) {
                AdRedirectCode.ORDER -> redirectToOrder(context)
                AdRedirectCode.OFFERS -> redirectToOffer(context)

                AdRedirectCode.OFFERS_FOR_YOU -> redirectToOfferForYou(context)

                AdRedirectCode.OFFERS_ONLY_TODAY -> redirectToOnlyToday(context)

                AdRedirectCode.OFFERS_SALES_TOOLS -> redirectToSalesTools(context)

                AdRedirectCode.OFFERS_INFORMATION -> redirectToOffersInformation(context)

                AdRedirectCode.CATALOG -> redirectToCatalog(context)

                AdRedirectCode.ACCOUNT -> redirectToAccount(context)

                AdRedirectCode.ANNOUNCEMENT -> redirectToAnnouncement(context)

                AdRedirectCode.MY_PROFILE -> redirectToMyProfile(context)

                AdRedirectCode.STOCKOUTS -> redirectToStockouts(context)

                AdRedirectCode.CHANGE -> redirectToChange(context)

                AdRedirectCode.ORDERS_FIC -> redirectToOrdersFic(context)

                AdRedirectCode.MY_ORDER -> redirectToMyOrder(context)

                AdRedirectCode.CLOSE_OUT -> redirectToCloseOut(context)

                AdRedirectCode.CAMINO_BRILLANTE_LANDING -> redirectToCaminoBrillanteLanding(context)

                AdRedirectCode.CAMINO_BRILLANTE_OFERTAS_ESPECIALES -> redirectToCaminoBrillanteOfertasEspeciales(context)

                AdRedirectCode.CAMINO_BRILLANTE_MEDALLAS -> redirectToCaminoBrillanteMedallas(context)

                AdRedirectCode.DREAM_METER_LANDING -> redirectToDreamMeterLanding(context)

                AdRedirectCode.BONIFICACION,
                AdRedirectCode.CLIENTES,
                AdRedirectCode.PASE_PEDIDO,
                AdRedirectCode.MI_ACADEMIA,
                AdRedirectCode.TVO,
                AdRedirectCode.ACTUALIZACION_DATO,
                AdRedirectCode.GANA_MAS,
                AdRedirectCode.GANA_MAS_ODD,
                AdRedirectCode.GANA_MAS_SR,
                AdRedirectCode.GANA_MAS_MG,
                AdRedirectCode.GANA_MAS_OPT,
                AdRedirectCode.GANA_MAS_RD,
                AdRedirectCode.GANA_MAS_HV,
                AdRedirectCode.GANA_MAS_DP,
                AdRedirectCode.GANA_MAS_PN,
                AdRedirectCode.GANA_MAS_ATP,
                AdRedirectCode.GANA_MAS_OPM,
                AdRedirectCode.CHAT
                -> redirectFromStories(context, adRedirectCode)


                else -> return


            }
        }


        fun redirectToOrder(context: Context) {
            val intent = Intent(context, AddOrdersActivity::class.java)
            startActivity(context, intent, null)

        }

        fun redirectToOffer(context: Context) {
            val intent = Intent(context, OffersActivity::class.java)
            startActivity(context, intent, null)

        }

        fun redirectToOfferForYou(context: Context) {
            var intent = Intent(context, OffersActivity::class.java)
            intent.putExtra(OffersActivity.OPTION, PageUrlType.OFERTAS_PARA_TI)
            startActivity(context, intent, null)

        }

        fun redirectToOnlyToday(context: Context) {
            var intent = Intent(context, OffersActivity::class.java)
            intent.putExtra(OffersActivity.OPTION, PageUrlType.SOLO_HOY)
            startActivity(context, intent, null)

        }

        fun redirectToSalesTools(context: Context) {
            var intent = Intent(context, OffersActivity::class.java)
            intent.putExtra(OffersActivity.OPTION, PageUrlType.HERRAMIENTAS_DE_VENTA)
            startActivity(context, intent, null)

        }

        fun redirectToOffersInformation(context: Context) {
            var intent = Intent(context, OffersActivity::class.java)
            intent.putExtra(OffersActivity.OPTION, PageUrlType.REVISTA_DIGITAL_INFO)
            startActivity(context, intent, null)
        }

        fun redirectToPerfectDuo(context: Context) {
            var intent = Intent(context, OffersActivity::class.java)
            intent.putExtra(OffersActivity.OPTION, PageUrlType.PERFECT_DUO)
            startActivity(context, intent, null)

        }

        fun redirectToCatalog(context: Context) {
            val intent = Intent(context, CatalogContainerActivity::class.java)
            startActivity(context, intent, null)

        }

        fun redirectToAccount(context: Context) {
            val intent = Intent(context, AccountStateActivity::class.java)
            startActivity(context, intent, null)

        }

        fun redirectToAnnouncement(context: Context) {
            val intent = Intent(context, AnnouncementActivity::class.java)
            startActivity(context, intent, null)

        }

        fun redirectToMyProfile(context: Context) {
            val intent = Intent(context, MyProfileActivity::class.java)
            startActivity(context, intent, null)

        }

        fun redirectToStockouts(context: Context) {
            val intent = Intent(context, StockoutsActivity::class.java)
            startActivity(context, intent, null)

        }

        fun redirectToChange(context: Context) {
            val intent = Intent(context, ChangesActivity::class.java)
            startActivity(context, intent, null)

        }

        fun redirectToOrdersFic(context: Context) {
            val intent = Intent(context, OrdersFicActivity::class.java)
            startActivity(context, intent, null)

        }

        fun redirectToMyOrder(context: Context) {
            val intent = Intent(context, MyOrdersActivity::class.java)
            startActivity(context, intent, null)

        }

        fun redirectToCloseOut(context: Context) {
            val intent = Intent(context, CloseoutActivity::class.java)
            startActivity(context, intent, null)

        }

        fun redirectToCaminoBrillanteLanding(context: Context) {
            val intent = Intent(context, CloseoutActivity::class.java)
            startActivity(context, intent, null)
        }

        fun redirectToCaminoBrillanteOfertasEspeciales(context: Context) {
            val intent = Intent(context, CloseoutActivity::class.java)
            intent.putExtra(OffersActivity.OPTION, PageUrlType.OFERTAS_ESPECIALES)
            startActivity(context, intent, null)
        }

        fun redirectToCaminoBrillanteMedallas(context: Context) {
            val intent = Intent(context, CloseoutActivity::class.java)
            intent.putExtra(OffersActivity.OPTION, PageUrlType.MEDALLAS)
            startActivity(context, intent, null)
        }

        fun redirectToDreamMeterLanding(context: Context){
            val intent = Intent(context, CloseoutActivity::class.java)
            startActivity(context, intent, null)
        }

        fun redirectFromStories(context: Context, donde: String) {
            //primero voy al home para abrir la pantalla de bonificacion
            val intent  = Intent(context, HomeActivity::class.java)
            when (donde) {
                AdRedirectCode.BONIFICACION -> {
                    intent.putExtra(AdRedirectCode.BONIFICACION, RedirectionStories.BONIFICACION)
                }
                AdRedirectCode.CLIENTES -> {
                    intent.putExtra(AdRedirectCode.CLIENTES, RedirectionStories.CLIENTES)
                }
                AdRedirectCode.PASE_PEDIDO -> {
                    intent.putExtra(AdRedirectCode.PASE_PEDIDO, RedirectionStories.PASE_PEDIDO)
                }
                AdRedirectCode.MI_ACADEMIA -> {
                    intent.putExtra(AdRedirectCode.MI_ACADEMIA, RedirectionStories.MI_ACADEMIA)
                }
                AdRedirectCode.TVO -> {
                    intent.putExtra(AdRedirectCode.TVO, RedirectionStories.TVO)
                }
                AdRedirectCode.ACTUALIZACION_DATO -> {
                    intent.putExtra(AdRedirectCode.ACTUALIZACION_DATO, RedirectionStories.ACTUALIZACION_DATOS)
                }
                AdRedirectCode.GANA_MAS -> {
                    intent.putExtra(AdRedirectCode.GANA_MAS, RedirectionStories.GANA_MAS)
                }

                AdRedirectCode.GANA_MAS_ODD -> {
                    intent.putExtra(AdRedirectCode.GANA_MAS_ODD, RedirectionStories.GANA_MAS_ODD)
                }
                AdRedirectCode.GANA_MAS_SR -> {
                    intent.putExtra(AdRedirectCode.GANA_MAS_SR, RedirectionStories.GANA_MAS_SR)
                }
                AdRedirectCode.GANA_MAS_MG -> {
                    intent.putExtra(AdRedirectCode.GANA_MAS_MG, RedirectionStories.GANA_MAS_MG)
                }
                AdRedirectCode.GANA_MAS_OPT -> {
                    intent.putExtra(AdRedirectCode.GANA_MAS_OPT, RedirectionStories.GANA_MAS_OPT)
                }
                AdRedirectCode.GANA_MAS_RD -> {
                    intent.putExtra(AdRedirectCode.GANA_MAS_RD, RedirectionStories.GANA_MAS_RD)
                }
                AdRedirectCode.GANA_MAS_HV -> {
                    intent.putExtra(AdRedirectCode.GANA_MAS_HV, RedirectionStories.GANA_MAS_HV)
                }
                AdRedirectCode.GANA_MAS_DP -> {
                    intent.putExtra(AdRedirectCode.GANA_MAS_DP, RedirectionStories.GANA_MAS_DP)
                }
                AdRedirectCode.GANA_MAS_PN -> {
                    intent.putExtra(AdRedirectCode.GANA_MAS_PN, RedirectionStories.GANA_MAS_PN)
                }
                AdRedirectCode.GANA_MAS_ATP -> {
                    intent.putExtra(AdRedirectCode.GANA_MAS_ATP, RedirectionStories.GANA_MAS_ATP)
                }
                AdRedirectCode.GANA_MAS_LAN -> {
                    intent.putExtra(AdRedirectCode.GANA_MAS_LAN, RedirectionStories.GANA_MAS_LAN)
                }
                AdRedirectCode.GANA_MAS_OPM -> {
                    intent.putExtra(AdRedirectCode.GANA_MAS_OPM, RedirectionStories.GANA_MAS_OPM)
                }
                AdRedirectCode.CHAT -> {
                    intent.putExtra(AdRedirectCode.CHAT, RedirectionStories.CHAT)
                }
                else -> {
                    intent.putExtra(AdRedirectCode.HOME, RedirectionStories.HOME)
                }
            }
            startActivity(context, intent, null)


        }

    }


}

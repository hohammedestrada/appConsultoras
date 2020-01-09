package biz.belcorp.consultoras.data.util

import biz.belcorp.consultoras.data.BuildConfig
import biz.belcorp.library.net.header.Content

/**
 *
 */
class Constant private constructor() {

    init {
        throw UnsupportedOperationException()
    }
    companion object {

        /** Const WS Headers */

        const val TRANSFORM = Content.TRANSFORM_LABEL + ":" + Content.TRANSFORM_VALUE_SERVICE
        const val APP_VERSION = Content.APP_VERSION + ":" + BuildConfig.VERSION_NAME
        const val APP_SO = Content.APP_SO + ":" + Content.APP_SO_VALUE


        /** Const General  */

        const val NOT_IMPLEMENTED = "No se va a implementar"
        const val SECRET = "kjsfg!)=)4diof25sfdg302dfg57438)!#$#70dfgf234asdnan"
        const val ERROR_TRANSFORM = "error transformando la data"
        const val FORMATO_DEFAULT_STORIE = "IMG"
        const val ACTIVAR_CATALOGO_DIGITAL = "CATALOGO-DIGITAL"
        const val ACTIVAR_ADC = "ADC"
        const val ACTIVAR_BANNER = "ACTIVA_BANNER"
        const val BLOQUEO_PENDIENTE = "BLOQUEO_PENDIENTE"
        const val ACTIVAR_ACTUALIZACION_DATOS = "ACTUALIZACION_DATOS"
        const val CHECK_ACTIVAR_WHATSAPP = "PUBLI_WHATSAPP"
        const val CATALOGO_DIGITAL_SUB_INDEX = 0
        const val PEDIDOS_PENDIENTES_SUB_INDEX = 1
        const val ACTUALIZAR_CELULAR_SUB_INDEX = 0
        /** Const WS Params */

        const val CODIGO_MEDIO_PAGO_INTERNET = "PBI"
        const val TRACKING_TOP = 3
        const val MENU_VERSION = 12
        const val ORDER_TOP = 6
        const val CODIGO_ORIGEN_PEDIDO_REGALO = 4021801
        const val ESTADO__NO_VISTO = 0

        /** Const Camino brillante **/
        const val VERS_LOGROS_CAMINO_BRILLANTE = 2

        /** Oferta Final **/
        const val CODE_FEATURE_FLAG_OFERTA_FINAL = "OFR"

    }
}

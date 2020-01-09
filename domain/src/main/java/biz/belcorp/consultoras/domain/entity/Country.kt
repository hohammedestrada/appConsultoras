package biz.belcorp.consultoras.domain.entity

/**
 * Entidad de dominio Country
 * que recibe o envia los datos a la capa de datos o a la capa de presentacion
 *
 * @version 1.0
 * @since 2017-04-14
 */

class Country {

    var id: Int? = null
    var name: String? = null
    var iso: String? = null
    var urlImage: String? = null
    var focusBrand: Int? = 0
    var textHelpUser: String? = null
    var textHelpPassword: String? = null
    var urlJoinBelcorp: String? = null
    var configForgotPassword: Int = 0   // 1 = valida Email , 2 = valida documento
    private var showDecimals: Boolean? = false
    var urlTerminos: String? = null
    var urlPrivacidad: String? = null
    var receiverFeedBack: String? = null

    var maxNoteAmount = 10
    var maxMovementAmount: Int = 0
    var historicMovementMonth: Int = 0
    var capturaDatosConsultora: Boolean? = null

    var telefono1: String? = null
    var telefono2: String? = null
    var urlContratoActualizacionDatos: String? = null
    var urlContratoVinculacion: String? = null

    fun isShowDecimals(): Boolean? {
        return showDecimals
    }

    fun setShowDecimals(showDecimals: Boolean?) {
        this.showDecimals = showDecimals
    }

    fun getShowDecimals(): Boolean? {
        return showDecimals
    }
}// EMPTY

package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

import java.io.Serializable

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

/**
 * Entidad Pais
 * que crea una tabla en la base de datos o
 * recibe un JSON por parte de un servicio
 *
 * @version 1.0
 * @since 2017-04-14
 */

@Table(database = ConsultorasDatabase::class, name = "Country")
class CountryEntity : BaseModel(), Serializable {

    @Column(name = "Id")
    @PrimaryKey
    @SerializedName("PaisID")
    var id: Int? = null

    @Column(name = "Name")
    @SerializedName("Nombre")
    var name: String? = null

    @Column(name = "UrlImage")
    @SerializedName("UrlImage")
    var urlImage: String? = null

    @Column(name = "ISO")
    @SerializedName("PaisISO")
    var iso: String? = null

    @Column(name = "FocusBrand")
    @SerializedName("MarcaEnfoque")
    var focusBrand: Int? = null

    @SerializedName("TextoAyudaUsuario")
    @Column(name = "TextHelpUser")
    var textHelpUser: String? = null

    @SerializedName("TextoAyudaClave")
    @Column(name = "TextHelpKey")
    var textHelpPassword: String? = null

    @SerializedName("UrlUneteABelcorp")
    @Column(name = "UrlJoinBelcorp")
    var urlJoinBelcorp: String? = null

    @SerializedName("ConfigOlvidarClave")
    @Column(name = "ConfigForgotPassword")
    var configForgotPassword: Int = 0   // 1 = valida Email , 2 = valida documento

    @SerializedName("UrlTerminos")
    @Column(name = "UrlTerminos")
    var urlTerminos: String? = null

    @SerializedName("UrlPrivacidad")
    @Column(name = "UrlPrivacidad")
    var urlPrivacidad: String? = null

    @SerializedName("DestinatariosFeedback")
    @Column(name = "DestinatariosFeedback")
    var receiverFeedBack: String? = null

    @SerializedName("NotaCantidadMaxima")
    @Column(name = "NotaCantidadMaxima")
    var maxNoteAmount: Int = 0

    @SerializedName("MovimientoCantidadMaxima")
    @Column(name = "MovimientoCantidadMaxima")
    var maxMovementAmount: Int = 0

    @SerializedName("MovimientoHistoricoMes")
    @Column(name = "MovimientoHistoricoMes")
    var historicMovementMonth: Int = 0

    @SerializedName("MostrarDecimales")
    @Column(name = "ShowDecimals")
    private var showDecimals: Boolean? = null

    @SerializedName("CapturaDatosConsultora")
    @Column(name = "CaptureData")
    var isCaptureData: Boolean? = null

    @SerializedName("Telefono1")
    @Column(name = "Telefono1")
    var telefono1: String? = null

    @SerializedName("Telefono2")
    @Column(name = "Telefono2")
    var telefono2: String? = null

    @SerializedName("UrlContratoActualizacionDatos")
    @Column(name = "UrlContratoActualizacionDatos")
    var urlContratoActualizacionDatos: String? = null

    @SerializedName("UrlContratoVinculacion")
    @Column(name = "UrlContratoVinculacion")
    var urlContratoVinculacion: String? = null

    fun isShowDecimals(): Boolean? {
        return showDecimals
    }

    fun setShowDecimals(showDecimals: Boolean?) {
        this.showDecimals = showDecimals
    }
}

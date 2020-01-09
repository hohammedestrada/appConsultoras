package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import java.io.Serializable

@Table(database = ConsultorasDatabase::class, name = "Visa")
class VisaEntity : Serializable {

    @PrimaryKey(autoincrement = false)
    @Column(name = "ID_UNICO")
    @SerializedName("ID_UNICO")
    var iDUnico: String? = null

    @Column(name = "PAN")
    @SerializedName("PAN")
    var pan: String? = null

    @Column(name = "CARDTOKENUUID")
    @SerializedName("CARDTOKENUUID")
    var cardtokenuuid: String? = null

    @Column(name = "IMP_AUTORIZADO")
    @SerializedName("IMP_AUTORIZADO")
    var imPAutorizado: String? = null

    @Column(name = "CSICODIGOPROGRAMA")
    @SerializedName("CSICODIGOPROGRAMA")
    var csicodigoprograma: String? = null

    @Column(name = "DECISIONCS")
    @SerializedName("DECISIONCS")
    var decisioncs: String? = null

    @Column(name = "RES_CVV2")
    @SerializedName("RES_CVV2")
    var resCv2: String? = null

    @Column(name = "CSIPORCENTAJEDESCUENTO")
    @SerializedName("CSIPORCENTAJEDESCUENTO")
    var csiporcentajedescuento: String? = null

    @Column(name = "NROCUOTA")
    @SerializedName("NROCUOTA")
    var nrocuota: String? = null

    @Column(name = "ECI")
    @SerializedName("ECI")
    var eci: String? = null

    @Column(name = "RESPUESTA")
    @SerializedName("RESPUESTA")
    var respuesta: String? = null

    @Column(name = "DSC_ECI")
    @SerializedName("DSC_ECI")
    var dscEci: String? = null

    @Column(name = "DSC_COD_ACCION")
    @SerializedName("DSC_COD_ACCION")
    var dscCOdAccion: String? = null

    @Column(name = "COD_AUTORIZA")
    @SerializedName("COD_AUTORIZA")
    var codAutoriza: String? = null

    @Column(name = "REVIEWTRANSACTION")
    @SerializedName("REVIEWTRANSACTION")
    var reviewtransaction: String? = null

    @Column(name = "CODTIENDA")
    @SerializedName("CODTIENDA")
    var codtienda: String? = null

    @Column(name = "NUMORDEN")
    @SerializedName("NUMORDEN")
    var numorden: String? = null

    @Column(name = "CODACCION")
    @SerializedName("CODACCION")
    var codaccion: String? = null

    @Column(name = "USERTOKENUUID")
    @SerializedName("USERTOKENUUID")
    var usertokenuuid: String? = null

    @Column(name = "FECHAYHORA_TX")
    @SerializedName("FECHAYHORA_TX")
    var fechayhoraTx: String? = null

    @Column(name = "IMPCUOTAAPROX")
    @SerializedName("IMPCUOTAAPROX")
    var impcuotaaprox: String? = null

    @Column(name = "CSIIMPORTECOMERCIO")
    @SerializedName("CSIIMPORTECOMERCIO")
    var csiimportecomercio: String? = null

    @Column(name = "CSIMENSAJE")
    @SerializedName("CSIMENSAJE")
    var csimensaje: String? = null

    @Column(name = "NOM_EMISOR")
    @SerializedName("NOM_EMISOR")
    var nomEmisor: String? = null

    @Column(name = "ORI_TARJETA")
    @SerializedName("ORI_TARJETA")
    var oriTarjeta: String? = null

    @Column(name = "CSITIPOCOBRO")
    @SerializedName("CSITIPOCOBRO")
    var csitipocobro: String? = null

    @Column(name = "NUMREFERENCIA")
    @SerializedName("NUMREFERENCIA")
    var numreferencia: String? = null

    @Column(name = "ETICKET")
    @SerializedName("ETICKET")
    var eticket: String? = null

    @Column(name = "Sincro", defaultValue = "0")
    @SerializedName("Sincro")
    var sincro: Int? = null

    var error: Throwable? = null
}

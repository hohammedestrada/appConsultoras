package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.*
import java.io.Serializable
@Table(database = ConsultorasDatabase::class, name = "VisaLog")
class VisaLogPaymentEntity: Serializable {

    @Column(name = "ID")
    @PrimaryKey(autoincrement = true)
    @SerializedName("ID")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column(name ="MontoGastosAdministrativos")
    @SerializedName("MontoGastosAdministrativos")
    var montoGastosAdministrativos: Double? = null

    @Column(name ="EMail")
    @SerializedName("EMail")
    var email: String? = null

    @Column(name ="PaymentStatus")
    @SerializedName("PaymentStatus")
    var paymentStatus: String? = null

    @Column(name ="MontoPago")
    @SerializedName("MontoPago")
    var montoPago: Double? = null

    @Column(name ="FechaLimPago")
    @SerializedName("FechaLimPago")
    var fechaLimPago: String? = null

    @Column(name ="TransactionId")
    @SerializedName("TransactionId")
    var transactionId: String? = null

    @SerializedName("Data")
    @ForeignKey(stubbedRelationship = true)
    var visa: VisaEntity? = null

    @Column(name ="CampaniaID")
    @SerializedName("CampaniaID")
    var campaniaID: String? = null

    @Column(name ="DocumentoIdentidad")
    @SerializedName("DocumentoIdentidad")
    var documentoIdentidad: String? = null

    @Column(name ="CodigoUsuario")
    @SerializedName("CodigoUsuario")
    var codigoUsuario: String? = null

    @Column(name ="Simbolo")
    @SerializedName("Simbolo")
    var simbolo: String? = null

    @Column(name ="PrimerNombre")
    @SerializedName("PrimerNombre")
    var primerNombre: String? = null

    @Column(name ="MontoDeudaConGastos")
    @SerializedName("MontoDeudaConGastos")
    var montoDeudaConGastos: Double? = null

    @Column(name ="TransactionDateTime")
    @SerializedName("TransactionDateTime")
    var transactionDateTime: Long? = null

    @Column(name ="MerchantId")
    @SerializedName("MerchantId")
    var merchantId: String? = null

    @Column(name ="Nombre")
    @SerializedName("Nombre")
    var nombre: String? = null

    @Column(name ="PaymentDescription")
    @SerializedName("PaymentDescription")
    var paymentDescription: String? = null

    @Column(name ="UserTokenId")
    @SerializedName("UserTokenId")
    var userTokenId: String? = null

    @Column(name ="AliasNameTarjeta")
    @SerializedName("AliasNameTarjeta")
    var aliasNameTarjeta: String? = null

    @Column(name ="PrimerApellido")
    @SerializedName("PrimerApellido")
    var primerApellido: String? = null

    @Column(name ="ExternalTransactionId")
    @SerializedName("ExternalTransactionId")
    var externalTransactionId: String? = null

    @Column(name ="TransactionUUID")
    @SerializedName("TransactionUUID")
    var transactionUUID: String? = null

    @Column(name ="Sincro", defaultValue = "0")
    @SerializedName("Sincro")
    var sincro: Int? =null

    var error: Throwable? = null

}

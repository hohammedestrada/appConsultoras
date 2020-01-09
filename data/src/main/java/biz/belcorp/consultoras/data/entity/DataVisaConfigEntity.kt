package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class DataVisaConfigEntity {
    @SerializedName("AccessKeyId")
    var accessKeyId: String? = null
    @SerializedName("SecretAccessKey")
    var secretAccessKey: String? = null
    @SerializedName("EndPointURL")
    var endPointUrl: String? = null
    @SerializedName("SessionToken")
    var sessionToken: String? = null
    @SerializedName("MerchantId")
    var merchantId: String? = null
    @SerializedName("NextCounterURL")
    var nextCounterUrl: String? = null
    @SerializedName("Recurrence")
    var recurrence: String? = null
    @SerializedName("RecurrenceType")
    var recurrenceType: String? = null
    @SerializedName("RecurrenceFrequency")
    var recurrenceFrecuency: String? = null
    @SerializedName("RecurrenceAmount")
    var recurrenceAmount: String? = null
    @SerializedName("TokenTarjetaGuardada")
    var tokenTarjetaGuardada: String? = null
}

package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

class IncentivesRequestEntity {

    var countryISO: String? = null

    @SerializedName("CodigoCampania")
    var campaingCode: String? = null
    var consultantCode: String? = null
    var regionCode: String? = null
    var zoneCode: String? = null
    var tipoConcurso: String? = null
}

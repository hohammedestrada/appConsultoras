package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.structure.BaseModel

import java.math.BigDecimal


class DeudorRequestEntity : BaseModel() {

    @SerializedName("ClienteID")
    var clienteID: Int? = null

    @SerializedName("TotalDeuda")
    var totalDeuda: BigDecimal? = null

    @SerializedName("Recordatorio")
    var recordatorio: RecordatorioEntity? = null

    var error: Throwable? = null
}

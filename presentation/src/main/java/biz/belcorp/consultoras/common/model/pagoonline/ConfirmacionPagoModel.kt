package biz.belcorp.consultoras.common.model.pagoonline

import android.os.Parcel
import android.os.Parcelable
import biz.belcorp.library.util.StringUtil
import java.io.Serializable

class ConfirmacionPagoModel(): Parcelable  {

    var montoBruto: Double? = null
    var labelGasto: String? = null
    var porcentaje: String? = null
    var url: String?= null
    var porcentajeBruto: Double? = null
    var totalPagar: String? = null
    var simboloMoneda: String = StringUtil.Empty
    var tipoPago: String = StringUtil.Empty

    constructor(parcel: Parcel) : this() {
        montoBruto = parcel.readValue(Double::class.java.classLoader) as? Double
        labelGasto = parcel.readString()
        porcentaje = parcel.readString()
        url = parcel.readString()
        porcentajeBruto = parcel.readValue(Double::class.java.classLoader) as? Double
        totalPagar = parcel.readString()
        simboloMoneda = parcel.readString()?:StringUtil.Empty
        tipoPago = parcel.readString()?:StringUtil.Empty
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(montoBruto)
        parcel.writeString(labelGasto)
        parcel.writeString(porcentaje)
        parcel.writeString(url)
        parcel.writeValue(porcentajeBruto)
        parcel.writeString(totalPagar)
        parcel.writeString(simboloMoneda)
        parcel.writeString(tipoPago)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ConfirmacionPagoModel> {
        override fun createFromParcel(parcel: Parcel): ConfirmacionPagoModel {
            return ConfirmacionPagoModel(parcel)
        }

        override fun newArray(size: Int): Array<ConfirmacionPagoModel?> {
            return arrayOfNulls(size)
        }
    }


}

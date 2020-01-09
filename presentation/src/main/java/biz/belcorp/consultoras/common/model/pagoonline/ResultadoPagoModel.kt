package biz.belcorp.consultoras.common.model.pagoonline

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.*

class ResultadoPagoModel(): Parcelable {

    var nombre: String? = ""
    var numeroOperacion: String? = ""
    var numeroTarjeta: String? = ""
    var fechaPago: String? = ""
    var monto: String? = ""
    var cargo: String? = ""
    var totalPagado: String? = ""
    var saldoPendiente: String? = ""
    var fechaVencimiento: String? = ""
    //var fechaVencimiento: Date = null
    var gastosAdmin: String? = ""
    var simboloMoneda: String? = ""


    constructor(parcel: Parcel) : this() {
        nombre = parcel.readString()
        numeroOperacion = parcel.readString()
        numeroTarjeta = parcel.readString()
        fechaPago = parcel.readString()
        monto = parcel.readString()
        cargo = parcel.readString()
        totalPagado = parcel.readString()
        saldoPendiente = parcel.readString()
        fechaVencimiento = parcel.readString()
        //fechaVencimiento = parcel.readLong()
        gastosAdmin = parcel.readString()
        simboloMoneda = parcel.readString()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object CREATOR : Parcelable.Creator<ResultadoPagoModel> {
        override fun createFromParcel(parcel: Parcel): ResultadoPagoModel {
            return ResultadoPagoModel(parcel)
        }

        override fun newArray(size: Int): Array<ResultadoPagoModel?> {
            return arrayOfNulls(size)
        }
    }


    class ResultadoPagoRechazadoModel() {
        var operacion: String? = ""
        var fecha: String? = ""
        var mensaje: String? = ""
    }

}

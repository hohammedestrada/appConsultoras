package biz.belcorp.consultoras.common.model.pagoonline

import android.os.Parcel
import android.os.Parcelable

class PagoOnlineConfigModel(): Parcelable  {

    var estadoCuenta: EstadoCuenta? = null

    var listaTipoPago: List<TipoPago>? = null

    var listaMedioPago: List<MedioPago>? = null

    var listaMetodoPago: List<MetodoPago>? = null

    var listaBanco: List<Banco>? = null

    constructor(parcel: Parcel) : this() {
        estadoCuenta = parcel.readParcelable(EstadoCuenta::class.java.classLoader)
        listaTipoPago = parcel.createTypedArrayList(TipoPago)
        listaMedioPago = parcel.createTypedArrayList(MedioPago)
        listaMetodoPago = parcel.createTypedArrayList(MetodoPago)
        listaBanco = parcel.createTypedArrayList(Banco)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(estadoCuenta, flags)
        parcel.writeTypedList(listaTipoPago)
        parcel.writeTypedList(listaMedioPago)
        parcel.writeTypedList(listaMetodoPago)
        parcel.writeTypedList(listaBanco)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PagoOnlineConfigModel> {
        override fun createFromParcel(parcel: Parcel): PagoOnlineConfigModel {
            return PagoOnlineConfigModel(parcel)
        }

        override fun newArray(size: Int): Array<PagoOnlineConfigModel?> {
            return arrayOfNulls(size)
        }
    }



    class EstadoCuenta() : Parcelable {

        var deuda: Double? = null

        var deudaFormateada : String? = null

        constructor(parcel: Parcel) : this() {
            deuda = parcel.readValue(Double::class.java.classLoader) as? Double
            deudaFormateada = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(deuda)
            parcel.writeString(deudaFormateada)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<EstadoCuenta> {
            override fun createFromParcel(parcel: Parcel): EstadoCuenta {
                return EstadoCuenta(parcel)
            }

            override fun newArray(size: Int): Array<EstadoCuenta?> {
                return arrayOfNulls(size)
            }
        }

    }

    class TipoPago() : Parcelable {

        var tipoId: Int? = null

        var descripcion: String? = null

        var codigo: String? = null

        var estado: Boolean? = null

        var previousMount: Double = 0.0

        constructor(parcel: Parcel) : this() {
            tipoId = parcel.readValue(Int::class.java.classLoader) as? Int
            descripcion = parcel.readString()
            codigo = parcel.readString()
            estado = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(tipoId)
            parcel.writeString(descripcion)
            parcel.writeString(codigo)
            parcel.writeValue(estado)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<TipoPago> {
            override fun createFromParcel(parcel: Parcel): TipoPago {
                return TipoPago(parcel)
            }

            override fun newArray(size: Int): Array<TipoPago?> {
                return arrayOfNulls(size)
            }
        }
    }

    class Banco() : Parcelable{

        var id: Int? = null

        var banco: String? = null

        var urlWeb: String? = null

        var urlIcono: String? = null

        var packageApp: String? = null

        var estado: Boolean? = null

        constructor(parcel: Parcel) : this() {
            id = parcel.readValue(Int::class.java.classLoader) as? Int
            banco = parcel.readString()
            urlWeb = parcel.readString()
            urlIcono = parcel.readString()
            packageApp = parcel.readString()
            estado = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(id)
            parcel.writeString(banco)
            parcel.writeString(urlWeb)
            parcel.writeString(urlIcono)
            parcel.writeString(packageApp)
            parcel.writeValue(estado)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Banco> {
            override fun createFromParcel(parcel: Parcel): Banco {
                return Banco(parcel)
            }

            override fun newArray(size: Int): Array<Banco?> {
                return arrayOfNulls(size)
            }
        }
    }

    class MedioPago() : Parcelable {

        var medioId: Int? = null

        var descripcion: String? = null

        var codigo: String? = null

        var rutaIcono: String? = null

        var orden: Int? = null

        var estado: Boolean? = null

        constructor(parcel: Parcel) : this() {
            medioId = parcel.readValue(Int::class.java.classLoader) as? Int
            descripcion = parcel.readString()
            codigo = parcel.readString()
            rutaIcono = parcel.readString()
            orden = parcel.readValue(Int::class.java.classLoader) as? Int
            estado = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(medioId)
            parcel.writeString(descripcion)
            parcel.writeString(codigo)
            parcel.writeString(rutaIcono)
            parcel.writeValue(orden)
            parcel.writeValue(estado)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<MedioPago> {
            override fun createFromParcel(parcel: Parcel): MedioPago {
                return MedioPago(parcel)
            }

            override fun newArray(size: Int): Array<MedioPago?> {
                return arrayOfNulls(size)
            }
        }
    }

    class MetodoPago() : Parcelable{

        var medioPagoDetalleId: Int? = null

        var medioPagoId: Int? = null

        var descripcion: String? = null

        var orden: Int? = null

        var tipoVisualizacion: String? = null

        var termCondicion: String? = null

        var tipoPasarela: String? = null

        var regExpreTarjeta: String? = null

        var tipoTarjeta: String? = null

        var porcentajeGastosAdministrativos: Double = 0.0

        var pagoEnLineaGastosLabel: String? = null

        var montoMinimoPago: Double? = null

        var estado: Boolean? = null

        constructor(parcel: Parcel) : this() {
            medioPagoDetalleId = parcel.readValue(Int::class.java.classLoader) as? Int
            medioPagoId = parcel.readValue(Int::class.java.classLoader) as? Int
            descripcion = parcel.readString()
            orden = parcel.readValue(Int::class.java.classLoader) as? Int
            tipoVisualizacion = parcel.readString()
            termCondicion = parcel.readString()
            tipoPasarela = parcel.readString()
            regExpreTarjeta = parcel.readString()
            tipoTarjeta = parcel.readString()
            porcentajeGastosAdministrativos = parcel.readDouble()
            pagoEnLineaGastosLabel = parcel.readString()
            montoMinimoPago = parcel.readValue(Double::class.java.classLoader) as? Double
            estado = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(medioPagoDetalleId)
            parcel.writeValue(medioPagoId)
            parcel.writeString(descripcion)
            parcel.writeValue(orden)
            parcel.writeString(tipoVisualizacion)
            parcel.writeString(termCondicion)
            parcel.writeString(tipoPasarela)
            parcel.writeString(regExpreTarjeta)
            parcel.writeString(tipoTarjeta)
            parcel.writeDouble(porcentajeGastosAdministrativos)
            parcel.writeString(pagoEnLineaGastosLabel)
            parcel.writeValue(montoMinimoPago)
            parcel.writeValue(estado)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<MetodoPago> {
            override fun createFromParcel(parcel: Parcel): MetodoPago {
                return MetodoPago(parcel)
            }

            override fun newArray(size: Int): Array<MetodoPago?> {
                return arrayOfNulls(size)
            }
        }
    }


}

package biz.belcorp.consultoras.common.model.stories

import android.os.Parcel
import android.os.Parcelable
import biz.belcorp.library.util.StringUtil


class StorieModel() : Parcelable {

    var codigoResumen: String = StringUtil.Empty

    var urlMiniatura: String? = null

    var totalContenido: Int = 0

    var contenidoVisto: Int = 0

    var contenidoDetalle: List<ContenidoDetalleModel>? = null

    constructor(parcel: Parcel) : this() {
        codigoResumen = parcel.readString()?: StringUtil.Empty
        urlMiniatura = parcel.readString()
        totalContenido = parcel.readInt()
        contenidoVisto = parcel.readInt()
        contenidoDetalle = parcel.createTypedArrayList(ContenidoDetalleModel)
    }


    class ContenidoDetalleModel() : Parcelable {

        var grupo: String? = null

        var typeContenido: String = StringUtil.Empty

        var idContenido: Int = 0

        var codigoDetalleResumen: String? = null

        var urlDetalleResumen: String? = null

        var accion: String? = null

        var ordenDetalleResumen: Int = 0

        var visto: Boolean = false

        var iterado = false

        var descargado = false

        constructor(parcel: Parcel) : this() {
            grupo = parcel.readString()
            typeContenido = parcel.readString()?:StringUtil.Empty
            idContenido = parcel.readInt()
            codigoDetalleResumen = parcel.readString()
            urlDetalleResumen = parcel.readString()
            accion = parcel.readString()
            ordenDetalleResumen = parcel.readInt()
            visto = parcel.readByte() != 0.toByte()
            iterado = parcel.readByte() != 0.toByte()
            descargado = parcel.readByte() != 0.toByte()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(grupo)
            parcel.writeString(typeContenido)
            parcel.writeInt(idContenido)
            parcel.writeString(codigoDetalleResumen)
            parcel.writeString(urlDetalleResumen)
            parcel.writeString(accion)
            parcel.writeInt(ordenDetalleResumen)
            parcel.writeByte(if (visto) 1 else 0)
            parcel.writeByte(if (iterado) 1 else 0)
            parcel.writeByte(if (descargado) 1 else 0)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ContenidoDetalleModel> {
            override fun createFromParcel(parcel: Parcel): ContenidoDetalleModel {
                return ContenidoDetalleModel(parcel)
            }

            override fun newArray(size: Int): Array<ContenidoDetalleModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(codigoResumen)
        parcel.writeString(urlMiniatura)
        parcel.writeInt(totalContenido)
        parcel.writeInt(contenidoVisto)
        parcel.writeTypedList(contenidoDetalle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StorieModel> {
        override fun createFromParcel(parcel: Parcel): StorieModel {
            return StorieModel(parcel)
        }

        override fun newArray(size: Int): Array<StorieModel?> {
            return arrayOfNulls(size)
        }
    }


}

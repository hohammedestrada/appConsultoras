package biz.belcorp.consultoras.common.model.banner

import android.os.Parcel
import android.os.Parcelable
import biz.belcorp.library.util.StringUtil

class BannerModel {

    var totalContenido: Int = 0

    var codigoResumen: String = ""

    var contenidoDetalle: List<ContenidoDetalleModel?> = listOf(null)

    class ContenidoDetalleModel() : Parcelable {

        var grupo: String? = null

        var typeContenido: String = StringUtil.Empty

        var idContenido: String = StringUtil.Empty

        var codigoDetalleResumen: String? = null

        var urlDetalleResumen: String? = null

        var accion: String? = null

        var ordenDetalleResumen: Int = 0

        var descripcion: String? = null

        constructor(parcel: Parcel) : this() {
            grupo = parcel.readString()
            typeContenido = parcel.readString()?: StringUtil.Empty
            idContenido = parcel.readString()?:StringUtil.Empty
            codigoDetalleResumen = parcel.readString()
            urlDetalleResumen = parcel.readString()
            accion = parcel.readString()
            ordenDetalleResumen = parcel.readInt()
            descripcion = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(grupo)
            parcel.writeString(typeContenido)
            parcel.writeString(idContenido)
            parcel.writeString(codigoDetalleResumen)
            parcel.writeString(urlDetalleResumen)
            parcel.writeString(accion)
            parcel.writeInt(ordenDetalleResumen)
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
}

package biz.belcorp.consultoras.data.entity.gallery

import com.google.gson.annotations.SerializedName

class ListadoImagenEntity{

    @SerializedName("Titulo")
    var Titulo : String = ""

    @SerializedName("NombreArchivo")
    var NombreArchivo : String = ""

    @SerializedName("UrlImagenThumb")
    var UrlImagenThumb : String = ""

    @SerializedName("UrlImagenVisualiza")
    var UrlImagenVisualiza : String = ""

    @SerializedName("UrlImagenDescarga")
    var UrlImagenDescarga : String = ""

    @SerializedName("Categoria")
    var Categoria : String = ""

    @SerializedName("Orden")
    var Orden : Int = -1

    @SerializedName("Seccion")
    var Seccion : String = ""
}

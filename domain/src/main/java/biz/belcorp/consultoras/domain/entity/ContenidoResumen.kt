package biz.belcorp.consultoras.domain.entity

class ContenidoResumen {


    var codigoResumen: String = ""

    var urlMiniatura: String? = null

    var totalContenido: Int = 0

    var contenidoVisto: Int = 0

    var contenidoDetalle: List<ContenidoDetalle>? = null


    class ContenidoDetalle {

        var grupo: String? = null

        var typeContenido: String = ""

        var idContenido: String = ""

        var codigoDetalleResumen: String? = null

        var urlDetalleResumen: String? = null

        var accion: String? = null

        var ordenDetalleResumen: Int = 0

        var visto: Boolean = false

        var descripcion: String? = null
    }
}

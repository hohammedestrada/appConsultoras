package biz.belcorp.consultoras.domain.entity.caminobrillante

import java.io.Serializable

class LogroCaminoBrillante : Serializable {

    var id: String? = null
    var titulo: String? = null
    var descripcion: String? = null
    var indicadores: List<Indicador>? = null

    class Indicador : Serializable {

        var id: Long? = null
        var codigo: String? = null
        var idLogro: String? = null
        var orden: Int? = null
        var titulo: String? = null
        var descripcion: String? = null
        var medallas: List<Medalla>? = null

        class Medalla : Serializable {

            var id: Long? = null
            var orden: Int? = null
            var tipo: String? = null
            var titulo: String? = null
            var subtitulo: String? = null
            var valor: String? = null
            var isDestacado: Boolean = false
            var isEstado: Boolean = false
            var modalTitulo: String? = null
            var modalDescripcion: String? = null

        }

    }

}



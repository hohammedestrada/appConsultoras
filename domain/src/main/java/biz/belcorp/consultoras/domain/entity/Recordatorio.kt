package biz.belcorp.consultoras.domain.entity

/**
 * Entidad de dominio Recordatorio
 * que recibe o envia los datos a la capa de datos o a la capa de presentacion
 *
 * @version 1.0
 * @since 2017-04-14
 */

class Recordatorio {

    var id: Int? = null
    var recordatorioID: Int? = null
    var clienteID: Int? = null
    var clienteLocalID: Int? = null
    var fecha: String? = null
    var descripcion: String? = null
    var sincronizado: Int? = null
    var estado: Int? = null
}

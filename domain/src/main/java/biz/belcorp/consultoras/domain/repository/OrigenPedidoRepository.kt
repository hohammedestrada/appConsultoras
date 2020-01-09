package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.*
import io.reactivex.Observable

/**
 *
 * tomando en cuenta la entidad que devuelve la respuesta del REST o el SQL
 *
 * @version 1.0
 * @since 2017-05-15
 */
interface OrigenPedidoRepository {

    /**
     * Metodo que obtiene la data
     *
     * @return Objeto que se ejecutara en un hilo diferente al principal
     */
    suspend fun get(tipo:String, codigo:String): OrigenPedidoWeb?
    suspend fun getValor(tipo:String, codigo:String): Int?

}

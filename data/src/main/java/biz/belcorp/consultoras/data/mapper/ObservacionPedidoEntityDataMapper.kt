package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.ObservacionPedidoEntity
import biz.belcorp.consultoras.domain.entity.ObservacionPedido
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clase encarga de realizar el mapeo de la entidad Config(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-20
 */
@Singleton
class ObservacionPedidoEntityDataMapper @Inject
internal constructor()  {
    /**
     * Transforma la entidad a una entidad del dominio.
     * C
     *
     * @param input Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(input: ObservacionPedidoEntity?): ObservacionPedido? {
        return input?.let {
            ObservacionPedido().apply {
                cuv = it.cuv
                descripcion = it.descripcion
                caso = it.caso
                cuvObs = it.cuvObs
                conjuntoID = it.conjuntoID
                pedidoDetalleID = it.pedidoDetalleID
            }
        }
    }

    /**
     * Transforma la entidad a una entidad del dominio.
     * C
     *
     * @param input Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    fun transform(input: ObservacionPedido?): ObservacionPedidoEntity? {
        return input?.let {
            ObservacionPedidoEntity().apply {
                cuv = it.cuv
                descripcion = it.descripcion
                caso = it.caso
                cuvObs = it.cuvObs
                conjuntoID = it.conjuntoID
                pedidoDetalleID = it.pedidoDetalleID
            }
        }
    }

    /**
     * Transforma una lista de entidades a una lista de entidades de dominio
     *
     * @param list Lista de entidades
     * @return Lista de entidades del dominio
     */
    fun transform(list: Collection<ObservacionPedidoEntity?>?): List<ObservacionPedido?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<ObservacionPedido>()
        }
    }


    /**
     * Transforma una lista de entidades a una lista de entidades de dominio
     *
     * @param list Lista de entidades
     * @return Lista de entidades del dominio
     */
    fun transform(list: Collection<ObservacionPedido?>?): Collection<ObservacionPedidoEntity?>? {
        return list?.let {
            it
                .map { it1 -> transform(it1) }
                .filter { it1 -> null != it1 }
        } ?: run {
            emptyList<ObservacionPedidoEntity>()
        }
    }

}

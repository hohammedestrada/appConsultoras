package biz.belcorp.consultoras.data.repository.datasource.anotacion

import biz.belcorp.consultoras.data.entity.AnotacionEntity
import biz.belcorp.consultoras.data.net.service.IAnotacionService
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable

/**
 * Clase de Client encargada de obtener los datos desde un servicio
 *
 * @version 1.0
 * @since 2017-04-14
 */

class AnotacionCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
(private val service: IAnotacionService) : AnotacionDataStore {

    override fun get(id: Int?): Observable<AnotacionEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun listByClientLocalID(clientLocalID: Int?): Observable<List<AnotacionEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun save(anotacionEntity: AnotacionEntity?): Observable<AnotacionEntity?> {
        anotacionEntity?.let {
            return service.save(anotacionEntity.clienteID, anotacionEntity)
        } ?: return Observable.error(NullPointerException(javaClass.canonicalName))
    }

    override fun update(anotacionEntity: AnotacionEntity?): Observable<Boolean?> {
        anotacionEntity?.let {
            return service.update(anotacionEntity.clienteID, anotacionEntity.anotacionID, anotacionEntity)
        } ?: return Observable.error(NullPointerException(javaClass.canonicalName))

    }

    override fun delete(anotacionEntity: AnotacionEntity?): Observable<Boolean?> {
        anotacionEntity?.let {
            return service.delete(anotacionEntity.clienteID, anotacionEntity.anotacionID)
        } ?: return Observable.error(NullPointerException(javaClass.canonicalName))
    }

    override fun deleteByClientLocalID(clientLocalID: Int?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun countByClient(maxCount: Int?, clientLocalId: Int?): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }
}

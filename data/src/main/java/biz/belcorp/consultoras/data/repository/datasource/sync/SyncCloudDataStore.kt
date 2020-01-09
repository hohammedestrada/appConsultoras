package biz.belcorp.consultoras.data.repository.datasource.sync


import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.net.service.ISyncDataService
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable

/**
 * Clase de Sync encargada de obtener los datos desde un servicio
 *
 * @version 1.0
 * @since 2017-04-14
 */
class SyncCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
(private val service: ISyncDataService) : SyncDataStore {
    override fun getPaymentLocal(): Observable<VisaLogPaymentEntity> {
        return throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }


    override val clientsToSync: Observable<List<ClienteEntity?>?>
        get() = throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)


    override val anotationsToSync: Observable<List<AnotacionEntity?>?>
        get() = throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)

    override val notificationsHybrisToSync: Observable<List<HybrisDataEntity?>?>
        get() = throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)

    override fun syncClients(clients: List<ClienteEntity?>?): Observable<Collection<ClienteEntity?>?> {
        return service.syncClients(clients)
    }

    @Suppress("UNREACHABLE_CODE")
    override fun saveSyncClients(clients: List<ClienteEntity?>?): Observable<Boolean?> {
        return throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    /**
     * Servicio de tipo POST que sube todas las notas.
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun syncAnotations(anotations: List<AnotacionEntity?>?)
        : Observable<List<AnotacionEntity?>?> {
        return service.syncAnotations(anotations)
    }

    /**
     * Servicio de tipo POST que sube los Pagos En Linea no reportados a Somos Belcorp.
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun syncPaymentLocalToService(visaLogPaymentEntity: VisaLogPaymentEntity)
        : Observable<ServiceDto<ResultadoPagoEnLineaEntity>> {
        return service.registerPayment(visaLogPaymentEntity)
    }

    @Suppress("UNREACHABLE_CODE")
    override fun saveSyncAnotations(notes: List<AnotacionEntity?>?): Observable<Boolean?> {
        return throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    @Suppress("UNREACHABLE_CODE")
    override fun updateHybrisData(list: List<HybrisDataEntity?>?): Observable<Boolean?> {
        return throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

}

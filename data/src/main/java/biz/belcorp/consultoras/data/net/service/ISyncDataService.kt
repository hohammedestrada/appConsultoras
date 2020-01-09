package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.AnotacionEntity
import biz.belcorp.consultoras.data.entity.ClienteEntity
import biz.belcorp.consultoras.data.entity.ResultadoPagoEnLineaEntity
import biz.belcorp.consultoras.data.entity.VisaLogPaymentEntity
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Interface que implementa los metodos del servicio de sincronizacion
 * basandose en REST API
 *
 * @version 1.0
 * @since 2017-05-03
 */
interface ISyncDataService {

    /**
     * Servicio de tipo POST que sube clientes
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/Cliente/Sincronizar")
    fun syncClients(@Body clients: List<ClienteEntity?>?): Observable<Collection<ClienteEntity?>?>

    /**
     * Servicio de tipo POST que sube las anotaciones
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/Cliente/Notas")
    fun syncAnotations(@Body anotations: List<AnotacionEntity?>?): Observable<List<AnotacionEntity?>?>


    /**
     * Servicio de tipo POST que sube los Pagos en Linea realizados que no pudieron sincronizarse
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/PagoEnLinea/Visa")
    fun registerPayment(@Body entity: VisaLogPaymentEntity?): Observable<ServiceDto<ResultadoPagoEnLineaEntity>>

}

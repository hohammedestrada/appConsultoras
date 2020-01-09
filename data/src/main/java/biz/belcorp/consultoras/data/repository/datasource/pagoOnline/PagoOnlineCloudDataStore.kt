package biz.belcorp.consultoras.data.repository.datasource.pagoOnline

import biz.belcorp.consultoras.data.entity.DataPagoConfigEntity
import biz.belcorp.consultoras.data.entity.DataVisaConfigEntity
import biz.belcorp.consultoras.data.entity.ResultadoPagoEnLineaEntity
import biz.belcorp.consultoras.data.entity.VisaLogPaymentEntity
import biz.belcorp.consultoras.data.net.service.IPagoOnline
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable

internal class PagoOnlineCloudDataStore
(private val service: IPagoOnline) : PagoOnlineDataStore {

    override fun savePayment(visaLogPaymentEntity: VisaLogPaymentEntity, sincronizado: Int): Observable<ServiceDto<ResultadoPagoEnLineaEntity>> {
        return service.registerPayment(visaLogPaymentEntity)
    }

    override fun getInitialConfig(fechaVencimientoPago:String?, indicadorConsultoraDigital:Int?): Observable<ServiceDto<DataPagoConfigEntity>> {
        return service.getInitialConfiguration(fechaVencimientoPago, indicadorConsultoraDigital)
    }

    override fun getVisaInicialConfig(): Observable<ServiceDto<DataVisaConfigEntity>> {
        return service.getVisaConfiguration()
    }

    override fun getVisaNextCounter(authorization: String): Observable<String> {
        return service.getVisaNextCounter(authorization)
    }

}

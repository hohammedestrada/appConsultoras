package biz.belcorp.consultoras.data.repository.datasource.pagoOnline

import biz.belcorp.consultoras.data.entity.DataPagoConfigEntity
import biz.belcorp.consultoras.data.entity.DataVisaConfigEntity
import biz.belcorp.consultoras.data.entity.ResultadoPagoEnLineaEntity
import biz.belcorp.consultoras.data.entity.VisaLogPaymentEntity
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable

interface PagoOnlineDataStore {
    fun getInitialConfig(fechaVencimientoPago:String?, indicadorConsultoraDigital:Int?): Observable<ServiceDto<DataPagoConfigEntity>> { throw UnsupportedOperationException("No se va a implementar") } //TODO MCAPP-14
    fun getVisaInicialConfig(): Observable<ServiceDto<DataVisaConfigEntity>> { throw UnsupportedOperationException("No se va a implementar") } //TODO MCAPP-14
    fun getVisaNextCounter(authorization:String):Observable<String> { throw UnsupportedOperationException("No se va a implementar") } //TODO MCAPP-14
    fun savePayment(visaLogPaymentEntity: VisaLogPaymentEntity, sincronizado: Int): Observable<ServiceDto<ResultadoPagoEnLineaEntity>> { throw UnsupportedOperationException("No se va a implementar") } //TODO MCAPP-14
    fun saveLocalPayment(visaLogPaymentEntity: VisaLogPaymentEntity, sincronizado: Int): Observable<Boolean> { throw UnsupportedOperationException("No se va a implementar") } //TODO MCAPP-14
    fun deleteLocal(idVisa: String, transitionId: String, codigoUsuario: String){ }
}

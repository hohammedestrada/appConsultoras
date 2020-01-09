package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.*
import io.reactivex.Observable

interface PagoOnlineRepository {
    fun getInitialConfiguration(fechaVencimientoPago:String?, indicadorConsultoraDigital:Int?): Observable<BasicDto<PagoOnlineConfig>>
    fun getInitialConfigurationVisa(): Observable<BasicDto<VisaConfig>>
    fun savePayment(visaSave: VisaPayment, sincronizado: Int):Observable<BasicDto<ResultadoPagoEnLinea>>
    fun getVisaNextCounter(nextCounterURL:String, authorization:String): Observable<String>
}

package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import biz.belcorp.consultoras.data.entity.DataPagoConfigEntity
import biz.belcorp.consultoras.data.entity.DataVisaConfigEntity
import biz.belcorp.consultoras.data.entity.ResultadoPagoEnLineaEntity
import biz.belcorp.consultoras.data.entity.VisaLogPaymentEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IPagoOnline
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PagoOnlineService(context: Context, accessToken: AccessToken?, appName: String?, appCountry: String?,
                        private var nextCounterURL: String? = null)
    : BaseService(context), IPagoOnline {

    private val service: IPagoOnline = RestApi.create(IPagoOnline::class.java, accessToken, appName, appCountry)

    override fun getInitialConfiguration(fechaVencimientoPago:String?, indicadorConsultoraDigital:Int?): Observable<ServiceDto<DataPagoConfigEntity>> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.getInitialConfiguration(fechaVencimientoPago, indicadorConsultoraDigital)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        },
                            { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }
            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    override fun getVisaConfiguration(): Observable<ServiceDto<DataVisaConfigEntity>> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.getVisaConfiguration()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            it?.let { it1 -> emitter.onNext(it1) }
                                ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                        }, { error -> emitter.onError(getError(error)) },
                            { emitter.onComplete() })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }
            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    override fun getVisaNextCounter(authorization: String): Observable<String> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                context?.let {
                    val serviceVisa: IPagoOnline = RestApi.create(IPagoOnline::class.java, nextCounterURL)
                    try {
                        serviceVisa.getVisaNextCounter(authorization)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                it?.let { it1 -> emitter.onNext(it1) }
                                    ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                            }, { error -> emitter.onError(getError(error)) },
                                { emitter.onComplete() })
                    } catch (e: Exception) {
                        e.printStackTrace()
                        emitter.onError(ServiceException(e.cause))
                    }
                }

            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }

    override fun registerPayment(visaLog: VisaLogPaymentEntity?): Observable<ServiceDto<ResultadoPagoEnLineaEntity>> {
        return Observable.create { emitter ->
            if (isThereInternetConnection) {
                try {
                    service.registerPayment(visaLog)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                it?.let { it1 -> emitter.onNext(it1) }
                                    ?: emitter.onError(NullPointerException(javaClass.canonicalName))
                            },
                            { error ->
                                emitter.onError(getError(error))
                            })
                } catch (e: Exception) {
                    emitter.onError(ServiceException(e.cause))
                }
            } else {
                emitter.onError(NetworkErrorException())
            }
        }
    }
}

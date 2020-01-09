package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.entity.ResultadoPagoEnLineaEntity
import biz.belcorp.consultoras.data.mapper.PagoConfigEntityDataMapper
import biz.belcorp.consultoras.data.mapper.VisaConfigEntityDataMapper
import biz.belcorp.consultoras.data.repository.datasource.pagoOnline.PagoOnlineDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.user.UserDataStoreFactory
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.repository.PagoOnlineRepository
import biz.belcorp.library.net.dto.ServiceDto
import javax.inject.Inject
import javax.inject.Singleton
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

@Singleton
class PagoOnlineDataRepository @Inject internal constructor(
    private val pagoOnlineDataStoreFactory: PagoOnlineDataStoreFactory,
    private val pagoConfigEntityDataMapper: PagoConfigEntityDataMapper,
    private val visaConfigEntityDataMapper: VisaConfigEntityDataMapper,
    private val userDataStoreFactory: UserDataStoreFactory
) : PagoOnlineRepository {


    override fun getInitialConfiguration(fechaVencimientoPago:String?, indicadorConsultoraDigital:Int?): Observable<BasicDto<PagoOnlineConfig>> {
        val pagoDataStore = pagoOnlineDataStoreFactory.createCloudDataStore()
        return pagoDataStore.getInitialConfig(fechaVencimientoPago, indicadorConsultoraDigital).map {
            pagoConfigEntityDataMapper.transform(it)
        }
    }

    override fun getInitialConfigurationVisa(): Observable<BasicDto<VisaConfig>> {
        val pagoDataStore = pagoOnlineDataStoreFactory.createCloudDataStore()
        return pagoDataStore.getVisaInicialConfig().map {
            visaConfigEntityDataMapper.transformVisaConfig(it)
        }
    }

    override fun getVisaNextCounter(nextCounterURL: String, authorization: String): Observable<String> {
        val pagoDataStore = pagoOnlineDataStoreFactory.createCloudDataStore(nextCounterURL)
        return pagoDataStore.getVisaNextCounter(authorization).map {
            it
        }
    }

    override fun savePayment(visaSave: VisaPayment, sincronizado: Int): Observable<BasicDto<ResultadoPagoEnLinea>> {
        val pagoDataStore = pagoOnlineDataStoreFactory.createCloudDataStore()
        val localDataStore = pagoOnlineDataStoreFactory.createDb()
        val userData = userDataStoreFactory.createDB()
        val observableStore = pagoDataStore.savePayment(visaConfigEntityDataMapper.transform(visaSave)!!, sincronizado)
            .onErrorReturn { throwable ->
                ServiceDto<ResultadoPagoEnLineaEntity>().apply {
                    ResultadoPagoEnLineaEntity().error = throwable
                }
            }
        val observableLocal = localDataStore.saveLocalPayment(visaConfigEntityDataMapper.transform(visaSave)!!, sincronizado)
            .onErrorReturn {
                false
            }
        return Observable.zip(
            observableLocal
            , observableStore
            , BiFunction<Boolean, ServiceDto<ResultadoPagoEnLineaEntity>, ServiceDto<ResultadoPagoEnLineaEntity>> { t1, t2 ->
            if (t2.data.error == null) {
                if (t1) {
                    if (t2.data.saldoPendiente != null)
                        userData.updateMount(t2.data.saldoPendiente!!)
                    localDataStore.deleteLocal(visaSave.visa!!.iDUnico!!, visaSave.transactionId!!, visaSave.codigoUsuario!!)
                }
            }
            return@BiFunction t2
        }
        ).map {
            visaConfigEntityDataMapper.transformSaldo(it)
        }
    }

}

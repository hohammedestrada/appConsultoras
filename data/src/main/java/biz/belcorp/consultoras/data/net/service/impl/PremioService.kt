package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import biz.belcorp.consultoras.data.entity.OrderEntity
import biz.belcorp.consultoras.data.entity.PremioFinalAgregaEntity
import biz.belcorp.consultoras.data.entity.PremioFinalEntity
import biz.belcorp.consultoras.data.entity.PremioFinalMetaEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IOfferService
import biz.belcorp.consultoras.data.net.service.IPremioService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Deferred
import retrofit2.http.Body

class PremioService(context: Context, accessToken: AccessToken?, appName: String?,
                    appCountry: String?): BaseService(context), IPremioService {

    private val service: IPremioService = RestApi.createWithCache(context,
        IPremioService::class.java, accessToken, appName, appCountry)

    private val serviceNoCache: IPremioService = RestApi.create(IPremioService::class.java, accessToken, appName, appCountry)

    override fun getOfertasFinales(codigoCampania: Int?): Deferred<List<PremioFinalEntity?>?> {
        if (!isThereInternetConnection){
            throw NetworkErrorException()
        }else{
            return serviceNoCache.getOfertasFinales(codigoCampania)
        }

    }

    override fun addPremio(@Body body: PremioFinalAgregaEntity?): Deferred<ServiceDto<Any>?> {

        if (isThereInternetConnection) {
            return service.addPremio(body)
        }else{
            throw NetworkErrorException()
        }

    }

    override fun getMontoMeta(codigoCampania: Int?): Deferred<PremioFinalMetaEntity?> {
        if (!isThereInternetConnection){
            throw NetworkErrorException()
        }else{
            return serviceNoCache.getMontoMeta(codigoCampania)
        }
    }


}

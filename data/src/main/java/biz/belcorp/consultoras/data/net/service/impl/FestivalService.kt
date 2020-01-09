package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import biz.belcorp.consultoras.data.entity.FestivalConfiguracionEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IFestivalService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.AccessToken
import kotlinx.coroutines.Deferred


class FestivalService(context: Context, accessToken: AccessToken?, appName: String?,
                      appCountry: String?) : BaseService(context), IFestivalService {

    private val service: IFestivalService = RestApi.createWithCache(context,
        IFestivalService::class.java, accessToken, appName, appCountry)

    override fun getConfiguracion(grantType: Int?): Deferred<FestivalConfiguracionEntity?> {

        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return service.getConfiguracion(grantType)
    }

}




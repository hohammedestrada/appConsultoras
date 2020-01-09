package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import biz.belcorp.consultoras.data.net.RestApi.create
import biz.belcorp.consultoras.data.net.dto.dreamMeter.request.DreamMeterRequest
import biz.belcorp.consultoras.data.net.dto.dreamMeter.response.DreamMeterResponse
import biz.belcorp.consultoras.data.net.service.IDreamMeterService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.net.exception.TokenInvalidException
import kotlinx.coroutines.Deferred

class DreamMeterService
/**
 * Constructor
 *
 * @param context Contexto que llamo al Servicio
 * @param accessToken Token de la session
 * @param appName Nombre del app
 * @param appCountry Pais de conexion
 */
(context: Context, url: String?, accessToken: AccessToken?, oAccessToken: String?, appName: String?, appCountry: String?)
    : BaseService(context), IDreamMeterService {

    private val service: IDreamMeterService = create(IDreamMeterService::class.java, url, accessToken, oAccessToken, appName, appCountry)

    override fun getDreamMeter(countryISO: String?, campaignId: Int?, consultantId: String?): Deferred<ServiceDto<DreamMeterResponse>> {
        if (isThereInternetConnection) {
            if (isTokenNotNull) {
                return service.getDreamMeter(countryISO, campaignId, consultantId)
            } else {
                throw TokenInvalidException()
            }
        } else {
            throw NetworkErrorException()
        }
    }

    override fun saveDreamMeter(dreamMeterRequest: DreamMeterRequest): Deferred<ServiceDto<Boolean>> {
        if (isThereInternetConnection) {
            if (isTokenNotNull) {
                return service.saveDreamMeter(dreamMeterRequest)
            } else {
                throw TokenInvalidException()
            }
        } else {
            throw NetworkErrorException()
        }
    }

    override fun updateDreamMeter(dreamMeterRequest: DreamMeterRequest): Deferred<ServiceDto<Boolean>> {
        if (isThereInternetConnection) {
            if (isTokenNotNull) {
                return service.updateDreamMeter(dreamMeterRequest)
            } else {
                throw TokenInvalidException()
            }
        } else {
            throw NetworkErrorException()
        }
    }

    override fun updateStatus(dreamMeterRequest: DreamMeterRequest): Deferred<ServiceDto<Boolean>> {
        if (isThereInternetConnection) {
            if (isTokenNotNull) {
                return service.updateStatus(dreamMeterRequest)
            } else {
                throw TokenInvalidException()
            }
        } else {
            throw NetworkErrorException()
        }
    }

}

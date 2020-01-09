package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import biz.belcorp.consultoras.data.entity.gallery.GalleryResponseEntity
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IGaleryService
import biz.belcorp.library.net.AccessToken
import kotlinx.coroutines.Deferred

class GaleryService(context: Context, accessToken: AccessToken?, appName: String?, appCountry: String?)
    : BaseService(context), IGaleryService {

    private val service: IGaleryService = RestApi.create(IGaleryService::class.java, accessToken,
        appName, appCountry)

    override fun getGalery(campaign : String?): Deferred<GalleryResponseEntity?> {
        return service.getGalery(campaign)
    }
}

package biz.belcorp.consultoras.data.repository.datasource.downloadDableContent

import biz.belcorp.consultoras.data.entity.gallery.GalleryResponseEntity
import biz.belcorp.consultoras.data.net.service.IGaleryService
import kotlinx.coroutines.Deferred

class GaleryCloudDataStore
internal constructor(private val service: IGaleryService) : GaleryDataStore {

    override suspend fun getGalery(campaign : String?): Deferred<GalleryResponseEntity?> {
        return service.getGalery(campaign)
    }
}

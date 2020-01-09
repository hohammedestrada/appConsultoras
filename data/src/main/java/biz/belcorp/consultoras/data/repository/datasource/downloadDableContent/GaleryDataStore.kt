package biz.belcorp.consultoras.data.repository.datasource.downloadDableContent

import biz.belcorp.consultoras.data.entity.gallery.GalleryResponseEntity
import kotlinx.coroutines.Deferred

interface GaleryDataStore {

    suspend fun getGalery(campaign : String?): Deferred<GalleryResponseEntity?>

}

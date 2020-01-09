package biz.belcorp.consultoras.data.repository.datasource.downloadDableContent

import android.content.Context
import biz.belcorp.consultoras.data.entity.gallery.GalleryResponseEntity
import biz.belcorp.consultoras.data.util.Constant
import kotlinx.coroutines.Deferred

class GaleryDBDataStore(val context: Context) : GaleryDataStore {
    override suspend fun getGalery(campaign : String?): Deferred<GalleryResponseEntity?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }
}

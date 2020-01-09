package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.mapper.GaleryDataMapper
import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.repository.datasource.downloadDableContent.GaleryDataStoreFactory
import biz.belcorp.consultoras.domain.entity.gallery.GalleryResponse
import biz.belcorp.consultoras.domain.repository.GaleryRepository

@Singleton
class GaleryDataRepository
@Inject internal constructor(private val galeryDataStoreFactory : GaleryDataStoreFactory,
                             private val galeryDataMapper:GaleryDataMapper) : GaleryRepository {

    override suspend fun getGalery(campaign : String?): GalleryResponse? {
        val galleryDataStore = galeryDataStoreFactory.createCloudDataStore()

        val result = galleryDataStore.getGalery(campaign).await()
        return galeryDataMapper.transform(result)
    }
}

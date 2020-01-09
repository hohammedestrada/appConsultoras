package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.gallery.GalleryResponse

interface GaleryRepository {

    /**
     * Metodo que obtiene la data
     *
     * @return Objeto que se ejecutara en un hilo diferente al principal
     */
    suspend fun getGalery(campaign : String?): GalleryResponse?

}

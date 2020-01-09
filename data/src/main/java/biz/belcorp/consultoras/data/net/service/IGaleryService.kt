package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.gallery.GalleryResponseEntity
import biz.belcorp.consultoras.data.util.Constant
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface IGaleryService {

    /**
     * Servicio de tipo GET que lista las imagenes de la seccion de descargables
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.3/Consultora/Contenido/Galeria")
    fun getGalery(@Query("campania") campaign : String?): Deferred<GalleryResponseEntity?>

}

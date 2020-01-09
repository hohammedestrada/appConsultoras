package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.CatalogoEntity
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 *
 */
interface ICatalogService {

    /**
     * Servicio de tipo GET que obtiene una lista de catalogos y revistas
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/MisCatalogosRevistas/GetByCampanias")
    operator fun get(@Query("campaniaActual") currentCampaign: String?,
                     @Query("codigoZona") zoneCode: String?,
                     @Query("topAnterior") topLast: Int?,
                     @Query("topSiguiente") topNext: Int?,
                     @Query("nroCampanias") maximumCampaign: Int?,
                     @Query("mostrarCampaniaActual") showCurrent: Boolean?,
                     @Query("esBrillante") isBrillante: Boolean?)
        : Observable<List<CatalogoEntity?>?>


    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/MisCatalogosRevistas/GetByCampanias")
    fun getCoroutine(@Query("campaniaActual") currentCampaign: String?,
                     @Query("codigoZona") zoneCode: String?,
                     @Query("topAnterior") topLast: Int?,
                     @Query("topSiguiente") topNext: Int?,
                     @Query("nroCampanias") maximumCampaign: Int?,
                     @Query("mostrarCampaniaActual") showCurrent: Boolean?,
                     @Query("esBrillante") isBrillante: Boolean?): Deferred<List<CatalogoEntity?>?>

    /**
     * Servicio de tipo GET que obtiene la url de descarga en pdf de un catalogo
     *
     * @return String con la url de descarga del pdf del catalogo
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.0/MisCatalogosRevistas/GetUrlDescarga")
    fun getUrlDescarga(@Query("inModel.descripcion") descripcion: String?) : Deferred<String?>

    /**
     * Servicio de tipo GET que obtiene la url de descarga en pdf de un catalogo
     *
     * @return String con la url de descarga del pdf del catalogo
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.0/MisCatalogosRevistas/GetUrlDescarga")
    fun getObservableUrlDescarga(@Query("inModel.descripcion") descripcion: String?) : Observable<String?>
}

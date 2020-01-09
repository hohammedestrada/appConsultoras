package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.OfertaEntity
import biz.belcorp.consultoras.data.entity.caminobrillante.*
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface ICaminoBrillante {

    /**
     * Servicio de tipo GET que retorna los kits de ofertas especiales de camino brillante
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Pedido/CaminoBrillante/Kits")
    fun getKitsOfertas(
        @Query("inModel.CampaniaID") campaniaId: Int,
        @Query("inModel.NivelID") nivelId: Int
    ): Deferred<ServiceDto<List<KitCaminoBrillanteEntity>>?>


    /**
     * Servicio de tipo GET que retorna los demostradores de ofertas especiales de camino brillante
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Pedido/CaminoBrillante/Demostradores")
    fun getDemostradoresOfertas(
        @Query("inModel.CampaniaID") campaniaId: Int,
        @Query("inModel.NivelID") nivelId: Int,
        @Query("inModel.Orden") orden: String?,
        @Query("inModel.Filtro") filtro: String?,
        @Query("inModel.Inicio") inicio: Int?,
        @Query("inModel.Cantidad") cantidad: Int?
        ): Deferred<ServiceDto<List<DemostradorCaminoBrillanteEntity>>?>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.0/CaminoBrillante/Configuracion")
    fun getConfiguracionDemostrador(): Deferred<ServiceDto<ConfiguracionDemostradorEntity>>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.0/CaminoBrillante/Carrusel")
    fun getOfertasCarousel(
        @Query("inModel.CampaniaID") campaniaId: Int,
        @Query("inModel.NivelID") nivelId: Int
    ): Deferred<ServiceDto<CarouselEntity>>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.0/CaminoBrillante/FichaProducto")
    fun getFichaProducto(
        @Query("inModel.tipo") tipo: String = "0",
        @Query("inModel.CampaniaID") campaniaId: Int,
        @Query("inModel.nivelCaminoBrillanteId") nivelId: Int,
        @Query("inModel.cUV") cuv: String
    ): Deferred<ServiceDto<OfertaEntity?>>

    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.0/CaminoBrillante/Anim")
    fun updateFlagAnim(
        @Body request: AnimRequestUpdate
    ): Deferred<ServiceDto<Boolean>>

}

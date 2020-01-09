package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface IOfferService {


    /**
     * Servicio de tipo GET que obtiene la lista de orden
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Producto/Buscador/Ordenamiento")
    fun listaOrden(): Deferred<List<OrdenamientoEntity?>?>


    /**
     * Servicio de tipo GET que verifica la existencia de ofertas
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Ofertas/GanaMas/Configuracion")
    fun configuracion(@Query("listaOferta") listaOferta: String?,
                      @Query("campaniaID") campaniaID: Int?,
                      @Query("consecutivoNueva") consecutivoNueva: Int?,
                      @Query("codigoPrograma") codigoPrograma: String?,
                      @Query("codigoRegion") codigoRegion: String?,
                      @Query("codigoZona") codigoZona: String?,
                      @Query("zonaId") zonaId: Int?,
                      @Query("simbolo") simbolo: String?)
        : Deferred<GanaMasConfiguracionEntity?>

    /**
     * Servicio de tipo GET que lista las ofertas disponibles para la consultora
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Ofertas/GanaMas/OfertasDisponibles")
    fun ofertasDisponibles(@Query("campaniaID") campaniaID: Int?,
                           @Query("diaInicio") diaInicio: Int?,
                           @Query("esSuscrita") esSuscrita: Boolean?,
                           @Query("esActiva") esActiva: Boolean?)
        : Deferred<List<String?>?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Producto/Buscador/Categoria")
    fun categorias(): Deferred<List<CategoriaEntity?>?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Producto/Buscador/Categoria")
    fun categoriasInSearch(@Query("inModel.campaniaID") campaniaId: String?,
                           @Query("inModel.codigoConsultora") codigoConsultora: String?,
                           @Query("inModel.codigoZona") codigoZona: String?,
                           @Query("inModel.perzonalizaciones") perzonalizaciones: String?,
                           @Query("inModel.lider") lider: Int?,
                           @Query("inModel.rDEsSuscrita") rdesSuscrita: Boolean?,
                           @Query("inModel.rDEsActiva") rdesActiva: Boolean?,
                           @Query("inModel.rDActivoMdo") rdActivoModo: Boolean?,
                           @Query("inModel.rDTieneRDC") rdTieneRdc: Boolean?,
                           @Query("inModel.rDTieneRDI") rdTieneRdi: Boolean?,
                           @Query("inModel.rDTieneRDCR") rdTieneRdcr: Boolean?,
                           @Query("inModel.diaFacturacion") diaFacturacion: Int?): Deferred<List<CategoriaEntity?>?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Producto/Buscador/Filtro")
    fun filtros(@Query("conHijos") conHijos: Boolean?): Deferred<List<GroupFilterEntity?>?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "/api/v1.2/Producto/Buscador")
    fun ofertasXCategoria(@Body searchRequest: SearchRequestEntity): Deferred<SearchResponseEntity?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "/api/v1.1/Ofertas/GanaMas/Upselling")
    fun ofertasUpselling(@Body upSellingRequest: UpSellingRequestEntity): Deferred<List<OfertaEntity?>?>

    /**
     * Servicio de tipo GET que obtiene la informacion de lista de producto
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Ofertas/GanaMas/CrossSelling")
    fun ofertasCrosselling(
        @Query("inModel.tipo") tipo: String?,
        @Query("inModel.campaniaId") campaniaId: Int?,
        @Query("inModel.cUV") cuv: String?,
        @Query("inModel.fechaInicioFacturacion") fechaInicioFacturacion: String?,
        @Query("inModel.sugeridos") sugeridos: Boolean?)
        : Deferred<List<OfertaEntity?>?>

    /**
     * Servicio de tipo GET que obtiene la lista de ofertas
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Ofertas/GanaMas")
    fun ofertas(@Query("inModel.tipo") tipo: String?,
                @Query("inModel.campaniaID") campaniaId: String?,
                @Query("inModel.zonaId") zonaId: Int?,
                @Query("inModel.codigoZona") codigoZona: String?,
                @Query("inModel.codigoRegion") codigoRegion: String?,
                @Query("inModel.esSuscrita") esSuscrita: Boolean?,
                @Query("inModel.esActiva") esActiva: Boolean?,
                @Query("inModel.tieneMG") tieneMG: Boolean?,
                @Query("inModel.diaInicio") diaInicio: Int?,
                @Query("inModel.fechaInicioFacturacion") fechaInicioFacturacion: String?,
                @Query("inModel.codigoPrograma") codigoPrograma: String?,
                @Query("inModel.consecutivoNueva") consecutivoNueva: Int?,
                @Query("inModel.montoMaximoPedido") montoMaximoPedido: Double?,
                @Query("inModel.consultoraNueva") consultoraNueva: Int?,
                @Query("inModel.nroCampanias") nroCampanias: Int?,
                @Query("inModel.nombreConsultora") nombreConsultora: String?,
                @Query("inModel.codigoSeccion") codigoSeccion: String?,
                @Query("inModel.esUltimoDiaFacturacion") esUltimoDiaFacturacion: Boolean?,
                @Query("inModel.pagoContado") pagoContado: Boolean?,
                @Query("inModel.fechaFinFacturacion") fechaFinFacturacion: String?,
                /*INI ABT GAB-11*/
                @Query("inModel.variantea") variantea: Boolean?,
                @Query("inModel.varianteb") varianteb: Boolean?,
                @Query("inModel.variantec") variantec: Boolean?
                /*END ABT GAB-11*/
                )
        : Deferred<List<OfertaEntity?>?>

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Ofertas/GanaMas")
    fun ofertasNoCache(@Query("inModel.tipo") tipo: String?,
                       @Query("inModel.campaniaID") campaniaId: String?,
                       @Query("inModel.zonaId") zonaId: Int?,
                       @Query("inModel.codigoZona") codigoZona: String?,
                       @Query("inModel.codigoRegion") codigoRegion: String?,
                       @Query("inModel.esSuscrita") esSuscrita: Boolean?,
                       @Query("inModel.esActiva") esActiva: Boolean?,
                       @Query("inModel.tieneMG") tieneMG: Boolean?,
                       @Query("inModel.diaInicio") diaInicio: Int?,
                       @Query("inModel.fechaInicioFacturacion") fechaInicioFacturacion: String?,
                       @Query("inModel.codigoPrograma") codigoPrograma: String?,
                       @Query("inModel.consecutivoNueva") consecutivoNueva: Int?,
                       @Query("inModel.montoMaximoPedido") montoMaximoPedido: Double?,
                       @Query("inModel.consultoraNueva") consultoraNueva: Int?,
                       @Query("inModel.nroCampanias") nroCampanias: Int?,
                       @Query("inModel.nombreConsultora") nombreConsultora: String?,
                       @Query("inModel.codigoSeccion") codigoSeccion: String?,
                       @Query("inModel.esUltimoDiaFacturacion") esUltimoDiaFacturacion: Boolean?,
                       @Query("inModel.pagoContado") pagoContado: Boolean?,
                       @Query("inModel.fechaFinFacturacion") fechaFinFacturacion: String?,

        /*INI ABT GAB-11*/
                       @Query("inModel.variantea") variantea: Boolean?,
                       @Query("inModel.varianteb") varianteb: Boolean?,
                       @Query("inModel.variantec") variantec: Boolean?
        /*END ABT GAB-11*/
                       )
        : Deferred<List<OfertaEntity?>?>

    /**
     * Servicio de tipo GET que obtiene las ofertas de Arma tu Pack
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Ofertas/GanaMas/ArmaTuPack")
    fun getOfertaAtp(@Query(value = "inModel.campaniaID") campaniaId: Int?,
                     @Query(value = "inModel.codigoPrograma") codigoPrograma: String?,
                     @Query(value = "inModel.consecutivoNueva") consecutivoNueva: Int?,
                     @Query(value = "inModel.montoMaximoPedido") montoMaximoPedido: Double?,
                     @Query(value = "inModel.consultoraNueva") consultoraNueva: Int?,
                     @Query(value = "inModel.nroCampanias") nroCampanias: Int?,
                     @Query(value = "inModel.codigoSeccion") codigoSeccion: String?,
                     @Query(value = "inModel.codigoRegion") codigoRegion: String?,
                     @Query(value = "inModel.codigoZona") codigoZona: String?,
                     @Query(value = "inModel.zonaID") zonaId: Int?): Deferred<OfertaEntity?>

    /**
     * Servicio de tipo GET que obtiene la informacion completa del producto
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Ofertas/GanaMas/FichaProducto")
    fun getFicha(@Query("inModel.campaniaId") campaniaId: Int?,
                 @Query("inModel.tipo") tipo: String?,
                 @Query("inModel.cUV") cuv: String?,
                 @Query("inModel.simbolo") simbolo: String?,
                 @Query("inModel.fechaInicioFacturacion") fechaInicioFacturacion: String?,
                 @Query("inModel.codigoPrograma") codigoPrograma: String?,
                 @Query("inModel.consecutivoNueva") consecutivoNueva: Int?,
                 @Query("inModel.montoMaximoPedido") montoMaximoPedido: Double?,
                 @Query("inModel.consultoraNueva") consultoraNueva: Int?,
                 @Query("inModel.nroCampanias") nroCampanias: Int?,
                 @Query("inModel.nombreConsultora") nombreConsultora: String?,
                 @Query("inModel.codigoRegion") codigoRegion: String?,
                 @Query("inModel.codigoZona") codigoZona: String?,
                 @Query("inModel.codigoSeccion") codigoSeccion: String?,
                 @Query("inModel.esUltimoDiaFacturacion") esUltimoDiaFacturacion: Boolean?,
                 @Query("inModel.pagoContado") pagoContado: Boolean?,
                 @Query("inModel.fechaFinFacturacion") fechaFinFacturacion: String?): Deferred<FichaProductoEntity?>

    /**
     * Servicio de tipo GET que obtiene la url del producto para compartir
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Ofertas/GanaMas/FichaProducto/Compartir")
    fun getFichaURL(@Query("campaniaID") campaniaID: Int?,
                    @Query("CUV") cuv: String?,
                    @Query("TipoPersonalizacion") tipoPersonalizacion: String?,
                    @Query("ImagenUrl") imagenUrl: String?,
                    @Query("MarcaId") marcaId: Int?,
                    @Query("NombreMarca") nombreMarca: String?,
                    @Query("NombreOferta") nombreOferta: String?): Deferred<ShareOfertaEntity?>

    /**
     * Servicio de tipo GET que obtiene los premios de festivales y sus condiciones
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "api/v1.0/Festival")
    fun getOffersFestival(@Body festivalRequestEntity: FestivalRequestEntity): Deferred<ServiceDto<FestivalResponseEntity?>>


    /**
     * Servicio de tipo GET que obtiene los premios de festivales y sus condiciones
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.0/Festival/Progreso")
    fun getFestivalProgress(@Query("CampaniaId") campaignId: Int?,
                            @Query("NombreConsultora") nombreConsultora: String?,
                            @Query("CodigoPrograma") codigoPrograma: String?,
                            @Query("ConsecutivoNueva") consecutivoNueva: Int?,
                            @Query("CuvFiltro") cuvFiltro: String?): Deferred<List<FestivalProgressResponseEntity?>?>

    /**
     * Servicio de tipo GET que obtiene los premios de festivales y sus condiciones
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Ofertas/OfertaFinal/Recomendacion")
    fun getOfertasRecomendadas(@Query("codigoCampania") codigoCampania: Int?): Deferred<List<OfertaEntity?>?>


    @Headers( Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.2/Ofertas/OfertaFinal/Configuracion")
    fun getOfertaFinalConfiguracion(@Query("codigoCampania") campaignId: Int?,
                                    @Query("simbolo") simboloMoneda: String?): Deferred<ServiceDto<ConfiguracionPremioEntity?>>

    /**
     * Servicio de tipo GET que obtiene la promocion con sus condiciones
     *
     * Tipos:
     * 1 - Promocion con Condiciones
     * 2 - Condicion con Promociones
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    @Headers(Constant.APP_VERSION, Constant.APP_SO)
    @GET(value = "api/v1.1/Producto/Promociones")
    fun getOfferPromotion(@Query("campaniaID") campaniaID: Int?,
                          @Query("CUV") cuv: String?,
                          @Query("tipo") tipo: Int?) : Deferred<ServiceDto<PromotionResponseEntity?>>

}

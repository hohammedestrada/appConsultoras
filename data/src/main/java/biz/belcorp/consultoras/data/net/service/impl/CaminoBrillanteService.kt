package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import biz.belcorp.consultoras.data.entity.OfertaEntity
import biz.belcorp.consultoras.data.entity.caminobrillante.*
import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.ICaminoBrillante
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.dto.ServiceDto
import kotlinx.coroutines.Deferred

class CaminoBrillanteService
/**
 * Constructor
 *
 * @param context Contexto que llamo al Servicio
 * @param accessToken Token de la session
 * @param appName Nombre del app
 * @param appCountry Pais de conexion
 */
(context: Context, accessToken: AccessToken?, appName: String?, appCountry: String?)
    : BaseService(context), ICaminoBrillante {

    private val service: ICaminoBrillante = RestApi.create(ICaminoBrillante::class.java,
        accessToken, appName, appCountry)


    /**
     * Metodo que retorna kits de ofertas espaciales de camino brillante
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getKitsOfertas(campaniaId: Int, nivelId: Int): Deferred<ServiceDto<List<KitCaminoBrillanteEntity>>?> {
        if (isThereInternetConnection) {
            return service.getKitsOfertas(campaniaId, nivelId)
        } else {
            throw NetworkErrorException()
        }
    }

    override fun getDemostradoresOfertas(campaniaId: Int, nivelId: Int, orden: String?, filtro: String?, inicio: Int?, cantidad: Int?): Deferred<ServiceDto<List<DemostradorCaminoBrillanteEntity>>?> {
        if (isThereInternetConnection) {
            return service.getDemostradoresOfertas(campaniaId, nivelId, orden, filtro, inicio, cantidad)
        } else {
            throw NetworkErrorException()
        }
    }

    override fun getConfiguracionDemostrador(): Deferred<ServiceDto<ConfiguracionDemostradorEntity>> {
        if (isThereInternetConnection) {
            return service.getConfiguracionDemostrador()
        } else {
            throw NetworkErrorException()
        }
    }

    override fun getOfertasCarousel(campaniaId: Int, nivelId: Int): Deferred<ServiceDto<CarouselEntity>> {
        if (isThereInternetConnection) {
            return service.getOfertasCarousel(campaniaId, nivelId)
        } else {
            throw NetworkErrorException()
        }
    }

    override fun getFichaProducto(tipo: String, campaniaId: Int, nivelId: Int, cuv: String): Deferred<ServiceDto<OfertaEntity?>> {
        if (isThereInternetConnection) {
            return service.getFichaProducto(tipo, campaniaId, nivelId, cuv)
        } else {
            throw NetworkErrorException()
        }
    }

    override fun updateFlagAnim(request: AnimRequestUpdate): Deferred<ServiceDto<Boolean>> {
        if (isThereInternetConnection) {
            return service.updateFlagAnim(request)
        } else {
            throw NetworkErrorException()
        }
    }

}

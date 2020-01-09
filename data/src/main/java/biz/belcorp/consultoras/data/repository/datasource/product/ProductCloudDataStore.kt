package biz.belcorp.consultoras.data.repository.datasource.product

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.net.service.IProductService
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import io.reactivex.Observable

/**
 * Clase de Product encargada de obtener los datos desde un servicio
 *
 * @version 1.0
 * @since 2017-11-28
 */

class ProductCloudDataStore
/**
 * Constructor que recibe el servicio ha ser ejecutado
 *
 * @param service Clase encargado de ejecutar un metodo del servicio
 */
internal constructor(private val service: IProductService) : ProductDataStore {


    override fun getStockouts(data: ProductSearchRequestEntity?): Observable<List<ProductEntity?>?> {
        data?.let {
            return service.getStockouts(data.campaingId, data.zoneId, data.cuv,
                data.description)
        } ?: return Observable.error(NullPointerException(javaClass.canonicalName))
    }

    override fun saveStockouts(data: List<ProductEntity?>?): Observable<List<ProductEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getPersonalization(campaingId: Int?): Observable<String?> {
        return service.getPersonalization(campaingId)
    }

    override fun search(data: SearchRequestEntity?): Observable<ServiceDto<SearchResponseEntity?>?> {
        data?.let {
            return service.search(it)
        } ?: return Observable.error(NullPointerException(javaClass.canonicalName))
    }

    override fun getOrderByParameters(): Observable<ServiceDto<List<SearchOrderByResponseEntity?>?>?> {
        return service.getOrderByParameters()
    }

    override fun getlistGift(campaniaID: Int?, nroCampanias: Int?, codigoPrograma: String?, consecutivoNueva: Int?): Observable<List<EstrategiaCarruselEntity?>?> {
        return service.listaRegalos(campaniaID, nroCampanias, codigoPrograma, consecutivoNueva)
    }


    override fun autoGuardarRegalo(request:GiftSaveRequestEntity): Observable<Boolean?> {
        return service.autoSaveGift(request)
    }

}

package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.entity.GiftSaveRequestEntity
import biz.belcorp.consultoras.data.mapper.*
import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.repository.datasource.product.ProductDataStoreFactory
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.repository.ProductRepository
import io.reactivex.Observable

/**
 * Clase de Product encargada de obtener la data desde una fuente de datos (interna o externa)
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-11-28
 */

@Singleton
class ProductDataRepository
/**
 * Constructor que se le injecta las dependencias que necesita
 *
 * @param productDataStoreFactory Clase encargada de obtener los datos
 * @param productEntityDataMapper Clase encargade de realizar el parseo de datos
 */
@Inject
internal constructor(private val productDataStoreFactory: ProductDataStoreFactory,
                     private val productEntityDataMapper: ProductEntityDataMapper,
                     private val productSearchRequestEntityMapper: ProductSearchRequestEntityMapper,
                     private val searchRequestEntityMapper: SearchRequesEntityDataMapper,
                     private val orderEntityDataMapper:OrderEntityDataMapper,
                     private val basicDtoDataMapper: BasicDtoDataMapper)
    : ProductRepository {


    override fun getStockouts(request: ProductSearchRequest?): Observable<Collection<Product?>?> {
        val wsRepository = productDataStoreFactory.createCloudDataStore()
        val localRepository = productDataStoreFactory.createDB()
        return wsRepository.getStockouts(productSearchRequestEntityMapper.transform(request)!!)
                .flatMap { localRepository.saveStockouts(it) }.map {
                productEntityDataMapper.transform(it) }
    }

    override fun getStockoutsLocal(request: ProductSearchRequest?): Observable<Collection<Product?>?> {
        val localRepository = productDataStoreFactory.createDB()
        return localRepository.getStockouts(productSearchRequestEntityMapper.transform(request)!!)
                .map { productEntityDataMapper.transform(it) }
    }

    override fun getPersonalization(campaingId: Int): Observable<String?> {
        val wsRepository = productDataStoreFactory.createCloudDataStore()
        return wsRepository.getPersonalization(campaingId)
    }

    override fun search(request: SearchRequest?): Observable<BasicDto<SearchResponse?>> {
        val wsRepository = productDataStoreFactory.createCloudDataStore()
        return wsRepository.search(searchRequestEntityMapper.transform(request))
            .map { searchRequestEntityMapper.transformSearchResult(it) }
    }

    override fun getOrderByParameters(): Observable<BasicDto<Collection<SearchOrderByResponse?>?>?> {
        val wsRepository = productDataStoreFactory.createCloudDataStore()
        return wsRepository.getOrderByParameters()
            .map { searchRequestEntityMapper.transformOrderByResult(it) }
    }

    override fun getListGift(campaniaID: Int?, nroCampanias: Int?, codigoPrograma: String?, consecutivoNueva: Int?): Observable<Collection<EstrategiaCarrusel?>?> {
        val wsRepository = productDataStoreFactory.createCloudDataStore()

        return wsRepository.getlistGift(campaniaID, nroCampanias, codigoPrograma, consecutivoNueva)
            .map {
                orderEntityDataMapper.transform(it)
            }
    }


    override fun autoGuardoDelRegalo(request: GiftAutoSaverRequest): Observable<Boolean?> {
        val wsRepository = productDataStoreFactory.createCloudDataStore()
        return  wsRepository.autoGuardarRegalo(GiftSaveRequestEntity().apply {
            campaniaID = request.campaniaID
            nroCampanias = request.nroCampanias
            codigoPrograma = request.codigoPrograma
            consecutivoNueva = request.consecutivoNueva
            identifier = request.identifier
        })
    }


}

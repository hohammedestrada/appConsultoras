package biz.belcorp.consultoras.data.repository

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.mapper.CatalogoEntityDataMapper
import biz.belcorp.consultoras.data.mapper.ParamsEntityDataMapper
import biz.belcorp.consultoras.data.mapper.UserCatalogoRequestDataMapper
import biz.belcorp.consultoras.data.repository.datasource.catalog.CatalogDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.params.ParamsDataStoreFactory
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.CatalogoWrapper
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.repository.CatalogRepository
import io.reactivex.Observable

/**
 *
 */
@Singleton
class CatalogDataRepository @Inject
internal constructor(private val userCatalogoRequestDataMapper: UserCatalogoRequestDataMapper,
                     private val catalogoEntityDataMapper: CatalogoEntityDataMapper,
                     private val catalogDataStoreFactory: CatalogDataStoreFactory,
                     private val paramsDataStoreFactory: ParamsDataStoreFactory,
                     private val paramsEntityDataMapper: ParamsEntityDataMapper) : CatalogRepository {

    override fun get(user: User?, maximumCampaign: Int?): Observable<List<CatalogoWrapper?>?> {

        val localParam = paramsDataStoreFactory.create()
        val wsStore = catalogDataStoreFactory.createCloud()
        val localStore = catalogDataStoreFactory.createDB()

        val catalogRequest = userCatalogoRequestDataMapper.transform(user, maximumCampaign)
        val last = paramsEntityDataMapper.transform(localParam.get())

        if (last?.getHash() == catalogRequest?.getHash() && localStore.hasData()) {
            return getLocal(user, maximumCampaign)
        }

        localParam.save(paramsEntityDataMapper.transform(catalogRequest)).subscribe()
        return wsStore[catalogRequest].flatMap { localStore.save(it) }
            .map { catalogoEntityDataMapper.transformCollection(it) }

    }

    override suspend fun getCorroutine(user: User?, maximumCampaign: Int?,mostrarSiguieteAnterior: Boolean): List<CatalogoWrapper?>? {
        val localParam = paramsDataStoreFactory.create()
        val wsStore = catalogDataStoreFactory.createCloud()
        val localStore = catalogDataStoreFactory.createDB()

        val catalogRequest = userCatalogoRequestDataMapper.transform(user, maximumCampaign,mostrarSiguieteAnterior)
        val last = paramsEntityDataMapper.transform(localParam.get())

        if(mostrarSiguieteAnterior){
            if (last?.getHash() == catalogRequest?.getHash() && localStore.hasData()) {
                return getLocalCorroutine(user, maximumCampaign)
            }

            localParam.saveCoroutine(paramsEntityDataMapper.transform(catalogRequest))
            val listaCatalogos = wsStore.getWithCoroutine(catalogRequest)

            return catalogoEntityDataMapper.transformCollection(localStore.saveCoroutine(listaCatalogos))
        }
        else{
            val lista = wsStore.getWithCoroutine(catalogRequest)
            var retorno  = catalogoEntityDataMapper.transformCollection(lista)
            return retorno
        }


    }

    override suspend fun getLocalCorroutine(user: User?, maximumCampaign: Int?): List<CatalogoWrapper?>? {
        val dataStore = catalogDataStoreFactory.createDB()
        val catalogos = dataStore.getWithCoroutine(userCatalogoRequestDataMapper.transform(user, maximumCampaign))
        return catalogoEntityDataMapper.transformCollection(catalogos)
    }


    override fun getLocal(user: User?, maximumCampaign: Int?): Observable<List<CatalogoWrapper?>?> {
        val dataStore = catalogDataStoreFactory.createDB()
        return dataStore[userCatalogoRequestDataMapper.transform(user, maximumCampaign)]
            .map { catalogoEntityDataMapper.transformCollection(it) }
    }

    override suspend fun getUrlDescarga(descripcion: String?): String? {
        val dataStore = catalogDataStoreFactory.createDB()

        return dataStore.getUrlDescarga(descripcion).await()
    }

    override fun getObservableUrlDescarga(descripcion: String?): Observable<String?> {
        return catalogDataStoreFactory.createCloud().getObservableUrlDescarga(descripcion)
    }
}

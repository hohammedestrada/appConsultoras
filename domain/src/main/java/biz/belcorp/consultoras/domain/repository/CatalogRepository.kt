package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.CatalogoWrapper
import biz.belcorp.consultoras.domain.entity.User
import io.reactivex.Observable

/**
 *
 */
interface CatalogRepository {

    operator fun get(user: User?, maximumCampaign: Int?): Observable<List<CatalogoWrapper?>?>

    fun getLocal(user: User?, maximumCampaign: Int?): Observable<List<CatalogoWrapper?>?>

    suspend fun getLocalCorroutine(user: User?, maximumCampaign: Int?): List<CatalogoWrapper?>?

    suspend fun getUrlDescarga(descripcion: String?): String?

    suspend fun getCorroutine(user: User?, maximumCampaign: Int?,mostrarSiguieteAnterior: Boolean): List<CatalogoWrapper?>?

    fun getObservableUrlDescarga(descripcion: String?): Observable<String?>
}

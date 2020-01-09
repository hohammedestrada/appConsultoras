package biz.belcorp.consultoras.data.repository.datasource.storie

import biz.belcorp.consultoras.data.entity.ContenidoUpdateRequest
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

interface StorieDataSource {

    fun updateStateContenido(request: ContenidoUpdateRequest): Observable<String?>
    fun updateStateContenidoCorroutine(request: ContenidoUpdateRequest):Deferred<String?>
}

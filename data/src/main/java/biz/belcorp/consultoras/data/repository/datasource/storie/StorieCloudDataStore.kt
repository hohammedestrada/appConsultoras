package biz.belcorp.consultoras.data.repository.datasource.storie

import biz.belcorp.consultoras.data.entity.ContenidoUpdateRequest
import biz.belcorp.consultoras.data.net.service.IStorie
import io.reactivex.Observable
import kotlinx.coroutines.Deferred

class StorieCloudDataStore internal constructor(private val service: IStorie) : StorieDataSource {


    override fun updateStateContenido(request: ContenidoUpdateRequest): Observable<String?> {
        request.let {
            return service.actualizarEstadoContenido(it)
        }

    }

    override fun updateStateContenidoCorroutine(request: ContenidoUpdateRequest): Deferred<String?> {
       return service.actualizarEstadoContenidoCorroutine(request)
    }


}

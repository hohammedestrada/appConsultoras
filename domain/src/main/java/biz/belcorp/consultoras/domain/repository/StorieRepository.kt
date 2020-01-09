package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.*
import io.reactivex.Observable

interface StorieRepository {
    fun updateEstadoContenido(contenidoRequest: ContenidoRequest): Observable<String?>
    suspend fun updateEstadoContenidoCoroutine(contenidoRequest: ContenidoRequest): String?
}

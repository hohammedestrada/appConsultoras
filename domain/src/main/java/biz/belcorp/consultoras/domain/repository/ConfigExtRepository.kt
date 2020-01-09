package biz.belcorp.consultoras.domain.repository


import kotlinx.coroutines.Deferred


interface ConfigExtRepository {

    suspend fun getWithCoroutines(token: String): Deferred<Boolean?>

}

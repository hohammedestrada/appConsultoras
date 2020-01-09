package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.domain.repository.AuthRepository
import biz.belcorp.library.net.exception.TokenInvalidException
import kotlinx.coroutines.Deferred
import retrofit2.HttpException

open class BaseRepository(
    private val authRepository: AuthRepository
) {

    suspend fun <RESPONSE> safeApiCall(recreateDataStore: () -> Unit, call: suspend () -> Deferred<RESPONSE>): RESPONSE {
        return try {
            invokeCall(recreateDataStore, call, false)
        } catch (e: TokenInvalidException) {
            invokeCall(recreateDataStore, call, true)
        } catch (e: HttpException) {
            when (e.code()) {
                403 -> {
                    invokeCall(recreateDataStore, call, true)
                }
                else -> throw e
            }
        }
    }


    private suspend fun <RESPONSE> invokeCall(recreateDataStore: () -> Unit, call: suspend () -> Deferred<RESPONSE>, invokeToken: Boolean): RESPONSE {
        if (invokeToken) {
            authRepository.generateOAccessToken().await()
            recreateDataStore.invoke()
        }
        return call.invoke().await()
    }

}

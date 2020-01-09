package biz.belcorp.consultoras.data.net


import android.content.Context
import biz.belcorp.consultoras.data.BuildConfig
import com.google.gson.JsonElement

import java.net.UnknownHostException

import javax.net.ssl.SSLHandshakeException

import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.net.AccessToken
import biz.belcorp.library.net.BelcorpApi
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.net.exception.NetworkConnectionException
import biz.belcorp.library.net.exception.ServiceException
import io.reactivex.exceptions.UndeliverableException

/**
 * Clase encargada de realizar las llamadas a un servicio
 * aquí se configura las propiedades de la petición request
 * los interceptores y los convertidores que procesaran la respuesta
 * una vez el servicio haya devuelto un mensaje al cliente
 *
 * @version 1.0
 * @since 2017-04-14
 */
object RestApiAuth {

    private val TAG = "RestApiAuth"

    private val CONNECT_TIMEOUT: Long = 30
    private val READ_TIMEOUT: Long = 30
    private val WRITE_TIMEOUT: Long = 30

    private val belcorpApi = BelcorpApi.Builder()
        .baseUrl(BuildConfig.BASE_ANALYTICS)
        .connectTimeout(CONNECT_TIMEOUT)
        .readTimeout(READ_TIMEOUT)
        .writeTimeout(WRITE_TIMEOUT)
        .build()

    /**
     * Metodo que se encarga de crear un servicio con sus respectivos metodos
     *
     * @param clazz Interface que implementa el servicio con sus metodos
     * @param <S>   Generico que toma el tipo de la clase de servicio que se le ha pasado
     * @return object Objeto que contiene los metodos del servicio
    </S> */
    fun <S> create(clazz: Class<S>): S {
        return belcorpApi.create(clazz)
    }

    /**
     * Metodo que se encarga de crear un servicio con sus respectivos metodos
     *
     * @param clazz Interface que implementa el servicio con sus metodos
     * @param url   URL principal donde se encuentra el servicio
     * @param <S>   Generico que toma el tipo de la clase de servicio que se le ha pasado
     * @return object Objeto que contiene los metodos del servicio
    </S> */
    fun <S> create(clazz: Class<S>, url: String?): S {
        return belcorpApi.create(clazz, url)
    }

    /**
     * Metodo que se encarga de crear un servicio con sus respectivos metodos
     *
     * @param clazz       Interface que implementa el servicio con sus metodos
     * @param accessToken Token principal donde se encuentra el servicio
     * @param <S>         Generico que toma el tipo de la clase de servicio que se le ha pasado
     * @return object Objeto que contiene los metodos del servicio
    </S> */
    fun <S> create(clazz: Class<S>, accessToken: AccessToken?): S {
        return belcorpApi.create(clazz, accessToken)
    }

    /**
     * Metodo que se encarga de crear un servicio con sus respectivos metodos
     *
     * @param clazz       Interface que implementa el servicio con sus metodos
     * @param accessToken Token principal donde se encuentra el servicio
     * @param <S>         Generico que toma el tipo de la clase de servicio que se le ha pasado
     * @return object Objeto que contiene los metodos del servicio
    </S> */
    fun <S> create(clazz: Class<S>, accessToken: AccessToken?, appName: String?, appCountry: String?): S {
        return belcorpApi.create(clazz, accessToken, appName, appCountry)
    }

    /**
     * Metodo que se encarga de crear un servicio con sus respectivos metodos
     *
     * @param clazz       Interface que implementa el servicio con sus metodos
     * @param url         URL principal donde se encuentra el servicio
     * @param accessToken Token principal donde se encuentra el servicio
     * @param <S>         Generico que toma el tipo de la clase de servicio que se le ha pasado
     * @return object Objeto que contiene los metodos del servicio
    </S> */
    fun <S> create(clazz: Class<S>, url: String?, accessToken: AccessToken?): S {
        return belcorpApi.create(clazz, url, accessToken, null, null, null)
    }

    /**
     * Metodo que se encarga de crear un servicio con sus respectivos metodos
     *
     * @param clazz       Interface que implementa el servicio con sus metodos
     * @param url         URL principal donde se encuentra el servicio
     * @param accessToken Token principal donde se encuentra el servicio
     * @param appName     Nombre del appName para version.
     * @param <S>         Generico que toma el tipo de la clase de servicio que se le ha pasado
     * @return object Objeto que contiene los metodos del servicio
    </S> */
    fun <S> create(clazz: Class<S>, url: String?, accessToken: AccessToken?, appName: String?, appCountry: String?): S {
        return belcorpApi.create(clazz, url, accessToken, null, appName, appCountry)
    }

    /**
     * Metodo que se encarga de crear un servicio con sus respectivos metodos con cache
     *
     * @param clazz       Interface que implementa el servicio con sus metodos
     * @param accessToken Token principal donde se encuentra el servicio
     * @param <S>         Generico que toma el tipo de la clase de servicio que se le ha pasado
     * @return object Objeto que contiene los metodos del servicio
    </S> */
    fun <S> createWithCache(context: Context, clazz: Class<S>, accessToken: AccessToken?, appName: String?, appCountry: String?): S {
        val sessionManager = SessionManager.getInstance(context)
        val apiCacheEnabled = sessionManager.getApiCacheEnabled()
        val apiCacheOfflineTime = sessionManager.getApiCacheOfflineTime()
        val apiCacheOnlineTime = sessionManager.getApiCacheOnlineTime()

        return belcorpApi.create(context, clazz, accessToken, appName, appCountry, apiCacheEnabled, apiCacheOnlineTime, apiCacheOfflineTime)
    }

    fun parseError(error: Throwable): ServiceException {
        return try {
            belcorpApi.parseError(error)
        } catch (ex: UndeliverableException) {
            NetworkConnectionException()
        } catch (ex: UnknownHostException) {
            NetworkConnectionException()
        } catch (ex: SSLHandshakeException) {
            NetworkConnectionException()
        } catch (ex: Exception) {
            BelcorpLogger.w(TAG, ex)
            ServiceException(ex)
        }

    }

    fun readError(params: JsonElement): ServiceDto<*> {
        return belcorpApi.readError(params)
    }

    fun <T> readError(error: JsonElement, clazz: Class<T>): T {
        return belcorpApi.readError(error, clazz)
    }

    fun clearCache(context: Context) {
        return belcorpApi.clearCache(context)
    }

}

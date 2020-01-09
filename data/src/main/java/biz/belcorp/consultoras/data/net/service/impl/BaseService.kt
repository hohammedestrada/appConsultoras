package biz.belcorp.consultoras.data.net.service.impl

import android.content.Context
import biz.belcorp.consultoras.data.BuildConfig
import biz.belcorp.consultoras.data.manager.ISessionManager
import biz.belcorp.consultoras.data.manager.SessionManager

import biz.belcorp.consultoras.data.net.RestApi
import biz.belcorp.consultoras.data.net.service.IAuthService
import biz.belcorp.consultoras.domain.exception.ExpiredSessionException
import biz.belcorp.consultoras.domain.exception.ExpiredTokensException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.library.net.exception.ExpiredTokenException
import biz.belcorp.library.net.exception.NetworkConnectionException
import biz.belcorp.library.net.exception.PreviousVersionException
import biz.belcorp.library.net.exception.UnauthorizedException
import biz.belcorp.library.util.NetworkUtil

/**
 * Clase base de un servicio
 * que contiene los metodos comunes que involucra cualquier servicio
 *
 * @version 1.0
 * @since 2017-04-14
 */
abstract class BaseService
/**
 * Constructor
 *
 * @param context Contexto que llamo al servicio
 */
protected constructor(val context: Context?) {

    /**
     * Metodo que valida si tiene conexion a internet
     *
     * @return boolean Valor que determina la conectividad del dispositivo
     */
    protected val isThereInternetConnection: Boolean
        get() = NetworkUtil.isThereInternetConnection(context)

    init {
        if (null == context) {
            throw IllegalArgumentException("El constructor no puede recibir parametros nulos!!!")
        }
    }

    protected val isTokenNotNull: Boolean
        get() {
            context?.let {
                return SessionManager.getInstance(it).oAccessToken != null
            } ?: run {
                return false
            }
        }

    protected fun getError(error: Throwable): Throwable {
        val throwable = RestApi.parseError(error)
        return when (throwable) {
            is NetworkConnectionException -> NetworkErrorException()
            is UnauthorizedException -> ExpiredSessionException()
            is PreviousVersionException -> VersionException(throwable.isRequiredUpdate, throwable.url)
            is ExpiredTokenException -> ExpiredTokensException()
            else -> throwable
        }
    }

    fun clearCache() {
        context?.let { RestApi.clearCache(it) }
    }

}

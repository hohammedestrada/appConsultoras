package biz.belcorp.consultoras.exception

import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.data.exception.SessionException
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import biz.belcorp.consultoras.util.anotation.ErrorCode
import biz.belcorp.library.net.exception.*
import biz.belcorp.library.sql.exception.SqlException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

object ErrorFactory {

    fun create(exception: Throwable): ErrorModel {
        return when (exception) {
            is NetworkConnectionException -> ErrorModel(ErrorCode.NETWORK, "No hay conexión a internet.", exception.response)
            is NetworkErrorException -> ErrorModel(ErrorCode.NETWORK, "No hay conexión a internet.", null)
            is ServiceException -> if (exception.cause is SocketTimeoutException)
                ErrorModel(ErrorCode.GATEWAY_TIMEOUT, "Se supero el tiempo limite de consulta", null)
            else
                ErrorModel(ErrorCode.SERVICE, exception.message, exception.response)
            is SqlException -> ErrorModel(ErrorCode.SQL, "Ocurrió un error al ejecutar la operación.", null)
            is BadRequestException -> ErrorModel(ErrorCode.BAD_REQUEST, "Ocurrió un error al ejecutar la operación.", exception.response)
            is UnauthorizedException -> ErrorModel(ErrorCode.UNAUTHORIZED, "Su sesión ha expirado.", exception.response)
            is ForbiddenException -> ErrorModel(ErrorCode.FORBIDDEN, "No tiene permisos necesarios.", exception.response)
            is NotFoundException -> ErrorModel(ErrorCode.NOT_FOUND, "No se ha encontrado el servicio.", exception.response)
            is InternalServerErrorException -> ErrorModel(ErrorCode.INTERNAL_SERVER, "Ocurrió un error al ejecutar la operación.", exception.response)
            is NotImplementedException -> ErrorModel(ErrorCode.NOT_IMPLEMENTED, "Ocurrió un error al ejecutar la operación.", exception.response)
            is BadGatewayException -> ErrorModel(ErrorCode.BAD_GATEWAY, "Ocurrió un error al ejecutar la operación.", exception.response)
            is ServiceUnavaiableException -> ErrorModel(ErrorCode.SERVICE_UNAVAIABLE, "Los servicios no estan disponibles.", exception.response)
            is GatewayTimeoutException -> ErrorModel(ErrorCode.GATEWAY_TIMEOUT, "Se supero el tiempo limite de consulta", exception.response)
            is SessionException -> ErrorModel(ErrorCode.SESSION, "Su sesión ha espirado", null)
            is HttpException -> ErrorModel(ErrorCode.HTTP, getMessage(exception.code()), null)
            is SSLException -> ErrorModel(ErrorCode.HTTP, "Ocurrió un error al ejecutar la operación.", null)
            is UnknownHostException -> ErrorModel(ErrorCode.HTTP, "No hay conexión a internet.", null)
            is ConnectException -> ErrorModel(ErrorCode.DEFAULT, "Ocurrió un error al ejecutar la operación.", null)
            is DebtNotPaidException -> ErrorModel(ErrorCode.DEFAULT, "Deuda no pagada.", null)
            else -> ErrorModel(ErrorCode.DEFAULT, exception.message, null)
        }
    }

    private fun getMessage(code: Int): String {
        return when (code) {
            400 -> "Ocurrió un error al ejecutar la operación."
            401 -> "Su sesión ha expirado."
            403 -> "No tiene permisos necesarios."
            404 -> "No se ha encontrado el servicio."
            500 -> "Ocurrió un error al ejecutar la operación."
            501 -> "Ocurrió un error al ejecutar la operación."
            502 -> "Ocurrió un error al ejecutar la operación."
            503 -> "Los servicios no estan disponibles."
            504 -> "Se supero el tiempo limite de consulta"
            else -> "Ocurrió un error al ejecutar la operación."
        }
    }
}

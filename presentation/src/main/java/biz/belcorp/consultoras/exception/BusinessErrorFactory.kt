package biz.belcorp.consultoras.exception

import biz.belcorp.consultoras.common.model.error.BusinessErrorModel
import biz.belcorp.consultoras.domain.exception.ActualizaMisDatosCorreoYaExisteException
import biz.belcorp.consultoras.domain.exception.AutenticacionInvalidaException
import biz.belcorp.consultoras.domain.exception.AutenticacionUsuarioCorreoExisteException
import biz.belcorp.consultoras.domain.exception.AutenticacionUsuarioNoAuthorizadoException
import biz.belcorp.consultoras.domain.exception.AutenticacionUsuarioNoExisteException
import biz.belcorp.consultoras.domain.exception.AutenticacionUsuarioNoPasaPedidoException
import biz.belcorp.consultoras.domain.exception.ClienteNoEliminadoException
import biz.belcorp.consultoras.domain.exception.GenericException
import biz.belcorp.consultoras.domain.exception.InternoException
import biz.belcorp.consultoras.domain.exception.OfertaInputInvalidoException
import biz.belcorp.consultoras.domain.exception.PedidoLockGprException
import biz.belcorp.consultoras.domain.exception.PedidoLockHorarioRestringidoException
import biz.belcorp.consultoras.domain.exception.PedidoLockReservadoException
import biz.belcorp.consultoras.domain.exception.PedidoStockEstrategiaException
import biz.belcorp.consultoras.domain.exception.ProductoNoEncontradoException
import biz.belcorp.consultoras.domain.exception.ProductoStockInvalidoException
import biz.belcorp.consultoras.domain.exception.RecuperaContraseniaCedulaNoExisteException
import biz.belcorp.consultoras.domain.exception.RecuperaContraseniaCorreoNoIdentificadoException
import biz.belcorp.consultoras.domain.exception.RecuperaContraseniaCorreoNoRegistradoException
import biz.belcorp.consultoras.domain.exception.SincronizacionClienteNoActualizadoException
import biz.belcorp.consultoras.domain.exception.SincronizacionClienteNoRegistradoException
import biz.belcorp.consultoras.domain.exception.SincronizacionContactoPrincipalNoExisteException
import biz.belcorp.consultoras.domain.exception.ValidacionException
import biz.belcorp.consultoras.util.anotation.BusinessErrorCode

object BusinessErrorFactory {

    fun create(exception: Throwable): BusinessErrorModel {
        return when (exception) {
            is InternoException -> BusinessErrorModel(BusinessErrorCode.INTERNO, exception.response)
            is PedidoStockEstrategiaException -> BusinessErrorModel(BusinessErrorCode.PEDIDO_STOCK_ESTRATEGIA, exception.response)
            is PedidoLockGprException -> BusinessErrorModel(BusinessErrorCode.PEDIDO_LOCK_GPR, exception.response)
            is PedidoLockReservadoException -> BusinessErrorModel(BusinessErrorCode.PEDIDO_LOCK_RESERVADO, exception.response)
            is PedidoLockHorarioRestringidoException -> BusinessErrorModel(BusinessErrorCode.PEDIDO_LOCK_HORARIO_RESTRINGIDO, exception.response)
            is ProductoNoEncontradoException -> BusinessErrorModel(BusinessErrorCode.PRODUCTO_NO_ENCONTRADO, exception.response)
            is ProductoStockInvalidoException -> BusinessErrorModel(BusinessErrorCode.PRODUCTO_STOCK_INVALIDO, exception.response)
            is GenericException -> BusinessErrorModel(BusinessErrorCode.GENERIC, exception.response)
            is ValidacionException -> BusinessErrorModel(BusinessErrorCode.VALIDACION, exception.response)
            is AutenticacionInvalidaException -> BusinessErrorModel(BusinessErrorCode.AUTENTICACION_INVALIDA, exception.response)
            is AutenticacionUsuarioNoExisteException -> BusinessErrorModel(BusinessErrorCode.AUTENTICACION_USUARIO_NOEXISTE, exception.response)
            is AutenticacionUsuarioNoAuthorizadoException -> BusinessErrorModel(BusinessErrorCode.AUTENTICACION_USUARIO_NOAUTORIZADO, exception.response)
            is AutenticacionUsuarioNoPasaPedidoException -> BusinessErrorModel(BusinessErrorCode.AUTENTICACION_USUARIO_NOPASAPEDIDO, exception.response)
            is AutenticacionUsuarioCorreoExisteException -> BusinessErrorModel(BusinessErrorCode.AUTENTICACION_USUARIO_CORREO_EXISTE, exception.response)
            is OfertaInputInvalidoException -> BusinessErrorModel(BusinessErrorCode.OFERTA_INPUT_INVALIDO, exception.response)
            is RecuperaContraseniaCedulaNoExisteException -> BusinessErrorModel(BusinessErrorCode.RECUPERA_CONTRASENIA_CEDULANOEXISTE, exception.response)
            is RecuperaContraseniaCorreoNoRegistradoException -> BusinessErrorModel(BusinessErrorCode.RECUPERA_CONTRASENIA_CORREONOREGISTRADO, exception.response)
            is RecuperaContraseniaCorreoNoIdentificadoException -> BusinessErrorModel(BusinessErrorCode.RECUPERA_CONTRASENIA_CORREONOIDENTIFICADO, exception.response)
            is ActualizaMisDatosCorreoYaExisteException -> BusinessErrorModel(BusinessErrorCode.ACTUALIZA_MIS_DATOS_CORREOYAEXISTE, exception.response)
            is SincronizacionContactoPrincipalNoExisteException -> BusinessErrorModel(BusinessErrorCode.SINCRONIZACION_CONTACTOPRINCIPALNOEXISTE, exception.response)
            is SincronizacionClienteNoRegistradoException -> BusinessErrorModel(BusinessErrorCode.SINCRONIZACION_CLIENTENOREGISTRADO, exception.response)
            is SincronizacionClienteNoActualizadoException -> BusinessErrorModel(BusinessErrorCode.SINCRONIZACION_CLIENTENOACTUALIZADO, exception.response)
            is ClienteNoEliminadoException -> BusinessErrorModel(BusinessErrorCode.SINCRONIZACION_CLIENTENOELIMINADO, exception.message)
            else -> BusinessErrorModel(BusinessErrorCode.DEFAULT, exception.message)
        }
    }
}// EMPTY

package biz.belcorp.consultoras.data.exception

import biz.belcorp.consultoras.domain.exception.ErrorBundle

/**
 * Excepcion capturada cuando falla un repositorio
 *
 * @version 1.0
 * @since 2017-04-14
 */
class RepositoryErrorBundle internal constructor(private val exception: Exception?) : ErrorBundle {

    override val errorMessage: String
        get() {
            var message = ""
            if (this.exception != null) {
                message = this.exception.message.toString()
            }
            return message
        }

    override fun getException(): Exception? {
        return exception
    }
}

package biz.belcorp.consultoras.domain.exception

/**
 * Interface que implementa el error de un repositorio
 *
 * @version 1.0
 * @since 2017-04-14
 */
interface ErrorBundle {
    val errorMessage: String
    fun getException() : Exception?
}

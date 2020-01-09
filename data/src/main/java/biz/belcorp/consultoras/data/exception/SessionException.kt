package biz.belcorp.consultoras.data.exception

/**
 * Created by Usuario on 30/05/2017.
 */

class SessionException : Exception {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)

}

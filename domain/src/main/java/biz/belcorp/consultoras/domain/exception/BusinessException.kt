package biz.belcorp.consultoras.domain.exception

open class BusinessException : Exception {

    var response: Any? = null

    constructor() : super() {}

    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}

    constructor(cause: Throwable) : super(cause) {}

    companion object {

        private val TAG = "BusinessException"
    }
}

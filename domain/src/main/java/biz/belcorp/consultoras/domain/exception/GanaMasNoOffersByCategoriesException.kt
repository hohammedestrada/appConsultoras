package biz.belcorp.consultoras.domain.exception

class GanaMasNoOffersByCategoriesException : BusinessException {

    constructor() : super() {}

    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}

    constructor(cause: Throwable) : super(cause) {}

    companion object {

        private val TAG = "GanaMasNoOffersByCategoriesException"
    }

}

package biz.belcorp.consultoras.domain.entity

class RenewBody {
    var image: String? = ""
    var imagelogo: String? = ""
    var message: String? = ""
    var isShow: Boolean = false

    constructor(image: String?,imagelogo: String?, message: String?, isShow: Boolean) {
        this.image = image
        this.imagelogo = imagelogo
        this.message = message
        this.isShow = isShow
    }
}

package biz.belcorp.consultoras.domain.entity

class IntrigueBody {
    var image : String? = ""
    var isShow: Boolean = false

    constructor(image : String?, isShow: Boolean){
        this.image = image
        this.isShow = isShow
    }
}

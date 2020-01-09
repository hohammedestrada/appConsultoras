package biz.belcorp.consultoras.domain.entity

class Session {

    var countrySIM: String? = null

    var authType: Int? = 0
    var username: String? = null
    var password: String? = null
    var email: String? = null
    var country: String? = null
    var tokenType: String? = null
    var accessToken: String? = null
    var refreshToken: String? = null
    var expiresIn: Long? = 0
    var issued: String? = null
    var expires: String? = null
    var isTutorial: Boolean? = false

    var isLogged: Boolean? = false

    var started: Long? = 0
    var updated: Long? = 0
    var isAceptaTerminosCondiciones: Boolean? = false

    var ordersCount: Int? = 0
}

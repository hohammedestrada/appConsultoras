package biz.belcorp.consultoras.domain.entity

class AccessToken {

    var tokenType: String? = null
    var accessToken: String? = null
    var refreshToken: String? = null

    constructor(tokenType: String?, accessToken: String?) {
        this.tokenType = tokenType
        this.accessToken = accessToken
    }

    constructor(tokenType: String?, accessToken: String?, refreshToken: String?) {
        this.tokenType = tokenType
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    override fun toString(): String {
        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}'
    }

}

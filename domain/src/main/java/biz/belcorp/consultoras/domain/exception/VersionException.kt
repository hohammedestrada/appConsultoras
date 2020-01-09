package biz.belcorp.consultoras.domain.exception

class VersionException(val isRequiredUpdate: Boolean, val url: String) : Exception()

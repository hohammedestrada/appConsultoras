package biz.belcorp.consultoras.domain.exception

class NetworkErrorException : Exception(){
    override val message: String?
        get() = "No hay conexión a Internet."
}

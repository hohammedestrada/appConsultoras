package biz.belcorp.consultoras.domain.entity

class ConfiguracionPremio {

    var showMessage : Boolean = false

    var textMetaPrincipal: String? = null

    var textInferior: String? = null

    var textTerminosCondiciones: String? = null

    override fun toString(): String {
        return "ConfiguracionPremio(showMessage=$showMessage, textMetaPrincipal=$textMetaPrincipal, textInferior=$textInferior, textTerminosCondiciones=$textTerminosCondiciones)"
    }


}

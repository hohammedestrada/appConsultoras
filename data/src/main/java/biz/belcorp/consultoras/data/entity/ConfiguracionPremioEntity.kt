package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.ConfiguracionPremio
import com.google.gson.annotations.SerializedName

class ConfiguracionPremioEntity {

    @SerializedName("MostrarMensaje")
    var showMessage : Boolean = false

    @SerializedName("TextoMetaPrincipal")
    var textMetaPrincipal: String? = null

    @SerializedName("TextoInferior")
    var textInferior: String? = null

    @SerializedName("UrlTerminosCondiciones")
    var textTerminosCondiciones: String? = null


    companion object {

        fun transform(input: ConfiguracionPremioEntity?): ConfiguracionPremio?{
            return input?.let{
                ConfiguracionPremio().apply {
                    this.showMessage = input.showMessage
                    this.textMetaPrincipal = input.textMetaPrincipal
                    this.textInferior = input.textInferior
                    this.textTerminosCondiciones = input.textTerminosCondiciones
                }
            }
        }
    }
}

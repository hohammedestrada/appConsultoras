package biz.belcorp.consultoras.data.entity.caminobrillante

import com.google.gson.annotations.SerializedName

class CaminoBrillanteEntity {

    @SerializedName("NivelConsultora")
    var nivelesConsultoraCaminoBrillanteEntity: List<NivelConsultoraCaminoBrillanteEntity>? = null

    @SerializedName("Niveles")
    var nivelesCaminoBrillante: List<NivelCaminoBrillanteEntity>? = null

    @SerializedName("ResumenLogros")
    var resumenLogroCaminoBrillanteEntity: LogroCaminoBrillanteEntity? = null

    @SerializedName("Logros")
    var logrosCaminoBrillanteEntity: List<LogroCaminoBrillanteEntity>? = null

}

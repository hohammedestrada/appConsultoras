package biz.belcorp.consultoras.common.model.ficha

data class FichaSectionEnriquecidaModel(
    val title: String,
    val type: Int,
    val hashAPI: String?,
    val data: ArrayList<Comparable<*>>?
)

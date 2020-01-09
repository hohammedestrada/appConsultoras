package biz.belcorp.consultoras.data.repository.datasource.origenmarcacion




interface OrigenMarcacionDataStore {

    fun getValor(tipo: String, codigo: String): String?

    fun getValorPalanca(codigo: String): String?

    fun getValorSubseccion(codigo: String): String?

}

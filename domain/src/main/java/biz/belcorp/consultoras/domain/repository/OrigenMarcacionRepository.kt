package biz.belcorp.consultoras.domain.repository




interface OrigenMarcacionRepository {

    /**
     * Metodo que obtiene la data
     *
     * @return Objeto que se ejecutara en un hilo diferente al principal
     */

    suspend fun getValor(tipo:String, codigo:String): String?

    suspend fun getValorPalanca(codigo:String): String?

    suspend fun getValorSubseccion(codigo:String): String?


}

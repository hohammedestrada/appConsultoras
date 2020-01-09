package biz.belcorp.consultoras.domain.repository

import io.reactivex.Observable

interface ProfitRepository {

    /**
     * Metodo que obtiene los profits
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getProfits(data: String?): Observable<String?>

}

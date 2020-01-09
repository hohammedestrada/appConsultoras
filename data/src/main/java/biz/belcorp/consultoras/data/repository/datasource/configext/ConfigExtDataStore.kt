package biz.belcorp.consultoras.data.repository.datasource.configext


import biz.belcorp.consultoras.data.entity.ConfigExtResponseEntity
import kotlinx.coroutines.Deferred


interface ConfigExtDataStore {

    /**
     * Metodo que obtiene los datos de Configuracion
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    fun getWithCoroutines(token: String): Deferred<ConfigExtResponseEntity?>

    fun saveWithCoroutines(entity: ConfigExtResponseEntity?): Boolean


}

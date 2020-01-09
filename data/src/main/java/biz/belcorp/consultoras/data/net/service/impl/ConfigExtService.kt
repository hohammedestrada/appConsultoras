package biz.belcorp.consultoras.data.net.service.impl


import android.content.Context
import biz.belcorp.consultoras.data.entity.ConfigExtResponseEntity
import biz.belcorp.consultoras.data.net.RestApiExt
import biz.belcorp.consultoras.data.net.service.IConfigExtService
import biz.belcorp.consultoras.domain.exception.NetworkErrorException
import kotlinx.coroutines.Deferred

/**
 * Servicio de Configuracion inicial
 * que implementa las transacciones basicas
 *
 * @version 1.0
 * @since 2017-04-14
 */

class ConfigExtService
/**
 * Constructor
 *
 * @param context Contexto que llamo al Servicio
 */
(context: Context) : BaseService(context), IConfigExtService {

    private val service: IConfigExtService = RestApiExt.create(IConfigExtService::class.java)

    /**
     * Metodo que obtiene los datos de configuracion
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */

    override fun get(token: String?): Deferred<ConfigExtResponseEntity?> {

        if (!isThereInternetConnection)
            throw NetworkErrorException()
        else
            return service.get(token)

    }

}



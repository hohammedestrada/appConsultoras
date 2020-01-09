package biz.belcorp.consultoras.data.repository.datasource.product

import android.content.Context
import biz.belcorp.consultoras.data.entity.*

import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.Delete

import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.library.net.dto.ServiceDto
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import io.reactivex.Observable


/**
 * Clase de Producto encargada de obtener la data desde el SQLite
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-11-28
 */
class ProductDBDataStore
/**
 * Constructor
 *
 * @param context contexto que llamo al SQLite
 */
internal constructor(val context: Context) : ProductDataStore {

    override fun getStockouts(data: ProductSearchRequestEntity?): Observable<List<ProductEntity?>?> {
        return Observable.create { emitter ->
            try {
                val list = (select from  ProductEntity::class).list
                emitter.onNext(list)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun saveStockouts(data: List<ProductEntity?>?): Observable<List<ProductEntity?>?> {
        return Observable.create { emitter ->
            try {
                data?.let {
                    Delete.tables(ProductEntity::class.java)
                    FlowManager.getModelAdapter(ProductEntity::class.java).saveAll(data)
                    emitter.onNext(data)
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun getPersonalization(campaingId: Int?): Observable<String?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun search(data: SearchRequestEntity?): Observable<ServiceDto<SearchResponseEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun getOrderByParameters(): Observable<ServiceDto<List<SearchOrderByResponseEntity?>?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }

    override fun autoGuardarRegalo(requestEntity: GiftSaveRequestEntity): Observable<Boolean?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }


    override fun getlistGift(campaniaID: Int?, nroCampanias: Int?, codigoPrograma: String?, consecutivoNueva: Int?): Observable<List<EstrategiaCarruselEntity?>?> {
        throw UnsupportedOperationException(Constant.NOT_IMPLEMENTED)
    }
}

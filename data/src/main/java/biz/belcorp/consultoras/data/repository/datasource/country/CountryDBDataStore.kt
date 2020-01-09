package biz.belcorp.consultoras.data.repository.datasource.country

import android.content.Context
import android.util.Log

import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.sql.language.Select

import biz.belcorp.consultoras.data.entity.CountryEntity
import biz.belcorp.consultoras.data.entity.CountryEntity_Table
import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.kotlinextensions.*
import io.reactivex.Observable

/**
 * Clase de Pais encargada de obtener la data desde el SQLite
 * dicha clase se instancaria una sola vez y se mantendra mientras la aplicacion este viva
 *
 * @version 1.0
 * @since 2017-04-14
 */
class CountryDBDataStore
/**
 * Constructor
 *
 * @param context contexto que llamo al SQLite
 */
(private val context: Context) : CountryDataStore {

    /**
     * Metodo que obtiene el listado de paises desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(): Observable<List<CountryEntity?>?> {

        return Observable.create { emitter ->
            try {
                val countries = Select()
                        .from(CountryEntity::class.java)
                        .queryList()

                emitter.onNext(countries)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    /**
     * Metodo que obtiene el listado de paises por marca desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getByBrand(brand: Int?): Observable<List<CountryEntity?>?> {
        return Observable.create { emitter ->
            try {
                brand?.let {
                    val countries = (select
                        from CountryEntity::class
                        where (CountryEntity_Table.FocusBrand eq brand)
                        ).list
                    emitter.onNext(countries)
                } ?: emitter.onError(NullPointerException("id is null"))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }

    /**
     * Metodo que busca un pais por id desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun find(id: Int?): Observable<CountryEntity?> {
        return Observable.create { emitter ->
            try {
                id?.let {
                    val country = (select
                        from CountryEntity::class
                        where (CountryEntity_Table.Id eq id)
                        ).result
                    country?.let {
                        it1 -> emitter.onNext(it1)
                    } ?: emitter.onError(NullPointerException("country no encontrado"))

                } ?: emitter.onError(NullPointerException("id is null"))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }


    /**
     * Metodo que busca un pais por iso desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun find(iso: String?): Observable<CountryEntity?> {
        return Observable.create { emitter ->
            try {
                iso?.let {
                    val country = (select
                        from CountryEntity::class
                        where (CountryEntity_Table.ISO eq iso)
                        ).result
                    country?.let {
                        it1 -> emitter.onNext(it1)
                    } ?: emitter.onError(NullPointerException("country no encontrado"))

                } ?: emitter.onError(NullPointerException("iso is null"))

            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }

    /**
     * Metodo que guarda un pais desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun save(entity: CountryEntity?): Observable<Boolean?> {

        return Observable.create { emitter ->
            try {
                entity?.let {
                    val result = entity.save()
                    Log.d("DBData", "result " + java.lang.Boolean.toString(result))
                    emitter.onNext(result)
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                Log.e("DBData", "result " + ex.message)
                emitter.onError(SqlException(ex.cause))

            }
            emitter.onComplete()
        }
    }

    /**
     * Metodo que elimina un pais desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun delete(id: Int?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                val result = SQLite.delete(CountryEntity::class.java)
                        .where(CountryEntity_Table.Id.eq(id))
                        .executeUpdateDelete() > 0
                emitter.onNext(result)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }
            emitter.onComplete()
        }
    }
}

package biz.belcorp.consultoras.data.repository.datasource.config


import android.content.Context
import android.util.Log
import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.domain.entity.OrigenPedidoWeb
import biz.belcorp.consultoras.domain.entity.listaOrigenPedidoWeb

import com.raizlabs.android.dbflow.config.FlowManager

import biz.belcorp.library.sql.exception.SqlException
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.sql.language.Delete
import com.raizlabs.android.dbflow.sql.language.Select
import io.reactivex.Observable

import kotlin.collections.ArrayList

class ConfigDBDataStore
/**
 * Constructor
 *
 * @param context contexto que llamo al SQLite
 */
(private val context: Context) : ConfigDataStore {

    override fun get(): Observable<ConfigResponseEntity?> {
        return Observable.create<ConfigResponseEntity> { emitter ->
            try {
                val countryEntities = Select().from(CountryEntity::class.java)
                    .queryList()

                val origenPedidoWebEntity = Select().from(OrigenPedidoWebLocalEntity::class.java)
                    .queryList()

               val origenMarcacionWebEntity = Select().from(OrigenMarcacionWebLocalEntity::class.java)
               .queryList()
                                                                                                                                            if (!countryEntities.isEmpty()) {
                    val entity = ConfigResponseEntity()
                    entity.countries = countryEntities
                    entity.apps = ArrayList<AppEntity>()
                    entity.origenPedidoWeb = this.transformar(origenPedidoWebEntity) //ArrayList<OrigenPedidoWebEntity>()
                    //entity.origenMarcacionWeb = this.transformarMarcacion(origenMarcacionWebEntity)
                    emitter.onNext(entity)
                    emitter.onComplete()
                } else
                    emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (e: Exception) {
                emitter.onError(SqlException(e.cause))
            }
        }
    }

    fun transformar(lista: List<OrigenPedidoWebLocalEntity>): List<OrigenPedidoWebEntity> {

        var lstOrigenPedidoWeb = ArrayList<OrigenPedidoWebEntity>()

        lista.groupBy { t -> t.tipoOferta }.toList().forEach {

            var contenidoGrupo = ArrayList<listaOrigenPedidoWebEntity>().apply {
                lista.filter { t -> t.tipoOferta == it.first }.forEach { it2 ->
                    it2.run {
                        var item = listaOrigenPedidoWebEntity()
                        item.codigo = it2.codigo
                        item.valor = it2.valor
                        return@run item
                    }.let { it3 -> add(it3) }
                }
            }

            lstOrigenPedidoWeb.add(OrigenPedidoWebEntity().apply {
                tipoOferta=it.first
                listaOrigenPedidoWeb = contenidoGrupo
            })
        }

        return lstOrigenPedidoWeb
    }




    fun getItemOrigenPedidoWebEntity(tipoOferta: String?, codigo: String?, valor: Int?): OrigenPedidoWebLocalEntity {
        var res = OrigenPedidoWebLocalEntity()
        res.tipoOferta = tipoOferta
        res.codigo = codigo
        res.valor = valor
        return res
    }


    override fun save(entity: ConfigResponseEntity?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {

                //Ini: almacenar los dados anidados(array de array) en un objeto de tipo OrigenPedidoWebLocalEntity
                var origenPedidoWebLocalEntity = ArrayList<OrigenPedidoWebLocalEntity>().apply {
                    entity?.origenPedidoWeb.apply {
                        this?.forEach { it1 ->
                            it1?.listaOrigenPedidoWeb?.forEach { it2 ->
                                it2.run { return@run getItemOrigenPedidoWebEntity(it1?.tipoOferta, it2?.codigo, it2?.valor) }.let { it3 -> add(it3) }
                            }
                        }
                    }
                }



                entity?.let {

                    entity.origenPedidoWeb?.let {
                        Delete.tables(OrigenPedidoWebLocalEntity::class.java)
                        FlowManager.getModelAdapter(OrigenPedidoWebLocalEntity::class.java).saveAll(origenPedidoWebLocalEntity)

                    } ?: emitter.onError(NullPointerException("origenPedidoWeb is null"))



                    entity.countries?.let { it1 ->
                        Delete.tables(CountryEntity::class.java)
                        FlowManager.getModelAdapter(CountryEntity::class.java).saveAll(it1)

                        emitter.onNext(true)

                    } ?: emitter.onError(NullPointerException("countries is null"))


                    emitter.onComplete()
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex))
            }
        }
    }

    override fun save(entity: ConfigEntity?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                entity?.let {
                    Delete.tables(CountryEntity::class.java)
                    emitter.onNext(entity.save())
                    emitter.onComplete()
                } ?: NullPointerException(javaClass.canonicalName)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex))
            }
        }
    }
}

package biz.belcorp.consultoras.data.repository.datasource.configext


import android.content.Context
import biz.belcorp.consultoras.data.entity.ConfigExtResponseEntity
import biz.belcorp.consultoras.data.entity.OrigenMarcacionWebLocalEntity
import biz.belcorp.consultoras.data.entity.PalancaEntity
import biz.belcorp.consultoras.data.entity.SubseccionEntity
import biz.belcorp.consultoras.data.util.Constant.Companion.NOT_IMPLEMENTED
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.Delete
import kotlinx.coroutines.Deferred

class ConfigExtDBDataStore
/**
 * Constructor
 *
 * @param context contexto que llamo al SQLite
 */
(private val context: Context) : ConfigExtDataStore {

    fun getItemOrigenMarcacionWebEntity( codigo: String?, valor: String?): OrigenMarcacionWebLocalEntity {
        var res = OrigenMarcacionWebLocalEntity()
        res.codigo = codigo
        res.valor = valor
        return res
    }

    fun getItemPalancaWebEntity( codigo: String?, valor: String?): PalancaEntity {
        var res = PalancaEntity()
        res.codigo = codigo
        res.valor = valor
        return res
    }


    fun getItemSubseccionWebEntity( codigo: String?, valor: String?): SubseccionEntity {
        var res = SubseccionEntity()
        res.codigo = codigo
        res.valor = valor
        return res
    }

    override fun getWithCoroutines(token: String): Deferred<ConfigExtResponseEntity?> {

        throw UnsupportedOperationException(NOT_IMPLEMENTED)

    }

    override fun saveWithCoroutines(entity: ConfigExtResponseEntity?): Boolean {

        try {

            val origenMarcacionWebLocalEntity = ArrayList<OrigenMarcacionWebLocalEntity>().apply {

                entity?.listaUbicacion?.forEach {ubicacion ->

                    entity.listaSeccion?.forEach {seccion->

                        entity.listaListas?.forEach { lista ->

                            val codigoTabla= ubicacion?.codigo+seccion?.codigo
                            val descripcionTabla= ubicacion?.descripcion+" - "+seccion?.descripcion
                            if(codigoTabla.equals(lista)){
                                lista.run {
                                    return@run getItemOrigenMarcacionWebEntity( codigoTabla, descripcionTabla)
                                }.let {
                                    it3 -> add(it3)
                                }
                            }
                        }
                    }
                }

            }

            val palancaWebLocalEntity = ArrayList<PalancaEntity>().apply {
                entity?.listaPalancas?.forEach { palanca ->
                    palanca.run {
                        return@run getItemPalancaWebEntity( palanca?.codigo, palanca?.descripcion)
                    }.let {
                        it3 -> add(it3)
                    }
                }
            }

            val subseccionWebLocalEntity = ArrayList<SubseccionEntity>().apply {
                entity?.listaSubseccion?.forEach { subseccion ->

                    subseccion.run {
                        return@run getItemSubseccionWebEntity( subseccion?.codigo, subseccion?.descripcion)
                    }.let {
                        it3 -> add(it3)
                    }
                }
            }

            entity?.let {

                entity.listaSubseccion?.let {
                    Delete.tables(SubseccionEntity::class.java)
                    FlowManager.getModelAdapter(SubseccionEntity::class.java).saveAll(subseccionWebLocalEntity)
                }?: run { throw NullPointerException("listasubsecciones is null") }

                entity.listaPalancas?.let {
                    Delete.tables(PalancaEntity::class.java)
                    FlowManager.getModelAdapter(PalancaEntity::class.java).saveAll(palancaWebLocalEntity)
                }?: run { throw NullPointerException("listapalancas is null") }

                entity.listaListas?.let {
                    Delete.tables(OrigenMarcacionWebLocalEntity::class.java)
                    FlowManager.getModelAdapter(OrigenMarcacionWebLocalEntity::class.java).saveAll(origenMarcacionWebLocalEntity)
                }?: run { throw NullPointerException("listas is null") }

                return true
            } ?: return false

        } catch (ex: Exception) {
            return false
        }

    }

}

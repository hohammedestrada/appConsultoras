package biz.belcorp.consultoras.data.repository.datasource.menu

import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.Delete
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.sql.language.Select

import biz.belcorp.consultoras.data.entity.MenuEntity
import biz.belcorp.consultoras.data.entity.MenuEntity_Table
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
internal class MenuDBDataStore : MenuDataStore {

    /**
     * Metodo que obtiene el listado de menues desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(paisISO: String?, campaign: String?, revistaDigital: Int?): Observable<List<MenuEntity?>?> {

        return Observable.create { emitter ->
            try {
                val menuEntities = (select
                        from MenuEntity::class
                        where (MenuEntity_Table.CodigoMenuPadre eq "")
                    ).list

                emitter.onNext(menuEntities)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    /**
     * Metodo que obtiene el listado de menues desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun get(pos: Int?): Observable<List<MenuEntity?>?> {

        return Observable.create { emitter ->
            try {
                pos?.let {
                    val menuEntities = (select
                        from MenuEntity::class
                        where (MenuEntity_Table.CodigoMenuPadre eq "")
                        and (MenuEntity_Table.Posicion eq pos)
                        and (MenuEntity_Table.Visible eq true)
                        orderBy MenuEntity_Table.Orden.asc()
                        ).list
                    emitter.onNext(menuEntities)
                } ?: emitter.onError(NullPointerException("position is null"))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    /**
     * Metodo que busca un menu por codigo desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun getActive(code1: String?, code2: String?): Observable<MenuEntity?> {
        return Observable.create { emitter ->
            try {
                code1?.let {
                    val menuEntity1 = (select
                        from MenuEntity::class
                        where (MenuEntity_Table.Codigo eq code1)
                        and (MenuEntity_Table.Visible.eq(true))
                        ).result

                    menuEntity1?.let { it1 ->
                        emitter.onNext(it1)
                    } ?: kotlin.run {
                        code2?.let { _ ->
                            val menuEntity2 = (select
                                from MenuEntity::class
                                where (MenuEntity_Table.Codigo eq code2)
                                and (MenuEntity_Table.Visible.eq(true))
                                ).result

                            menuEntity2?.let {it1 ->
                                emitter.onNext(it1)
                            } ?: emitter.onError(NullPointerException("menu no encontrado"))
                        } ?: emitter.onError(NullPointerException("codigo is null"))
                    }
                } ?: emitter.onError(NullPointerException("codigo is null"))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }


    override fun getActive2(code1: String?, code2: String?): MenuEntity? {

        code1?.let { _ ->
            val menuEntity1 = (select from MenuEntity::class
            where (MenuEntity_Table.Codigo eq code1)
            and (MenuEntity_Table.Visible.eq(true))
            ).result

            menuEntity1?.let { it1 -> return it1 }

        }

       code2?.let { _ ->
           val menuEntity2 = (select from MenuEntity::class
               where (MenuEntity_Table.Codigo eq code2)
               and (MenuEntity_Table.Visible.eq(true))
               ).result

           menuEntity2?.let { it1 -> return it1 }
       }

        throw NullPointerException("codigo is null")
    }

    /**
     * Metodo que busca un menu por id desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun find(id: Int?): Observable<MenuEntity?> {
        return Observable.create { emitter ->
            try {
                id?.let {
                    val menuEntity = (select
                        from MenuEntity::class
                        where (MenuEntity_Table.MenuAppId eq id)
                        ).result

                    menuEntity?.let { it1 ->
                        emitter.onNext(it1)
                    } ?: emitter.onError(NullPointerException("menu no encontrado"))
                } ?: emitter.onError(NullPointerException("id is null"))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }


    /**
     * Metodo que busca un menu por codigo desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun find(codigo: String?): Observable<MenuEntity?> {
        return Observable.create { emitter ->
            try {
                codigo?.let {
                    val menuEntity = (select
                        from MenuEntity::class
                        where (MenuEntity_Table.Codigo eq codigo)
                        ).result

                    menuEntity?.let { it1 ->
                        emitter.onNext(it1)
                    } ?: emitter.onError(NullPointerException("menu no encontrado"))
                } ?: emitter.onError(NullPointerException("codigo is null"))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    /**
     * Metodo que guarda un menu desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun save(entity: MenuEntity?): Observable<Boolean?> {

        return Observable.create { emitter ->
            try {
                entity?.let {
                    val result = entity.save()
                    emitter.onNext(result)
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    /**
     * Metodo que elimina un menu desde el SQLite
     *
     * @return observable Objeto que se ejecutara en un hilo diferente al principal
     */
    override fun delete(id: Int?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                val result = SQLite.delete(MenuEntity::class.java)
                        .where(MenuEntity_Table.MenuAppId.eq(id))
                        .executeUpdateDelete() > 0
                emitter.onNext(result)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun saveList(menuList: List<MenuEntity?>?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                menuList?.let {
                    Delete().from(MenuEntity::class.java).execute()

                    for (entity in menuList) {
                        if (entity?.subMenus != null) {
                            FlowManager.getModelAdapter(MenuEntity::class.java).saveAll(entity.subMenus!!)
                            for (subEntity in entity.subMenus!!) {
                                if (subEntity?.subMenus != null) {
                                    FlowManager.getModelAdapter(MenuEntity::class.java).saveAll(subEntity.subMenus!!)
                                }
                            }
                        }
                    }

                    FlowManager.getModelAdapter(MenuEntity::class.java)
                        .saveAll(menuList)
                    emitter.onNext(true)
                } ?: emitter.onError(NullPointerException(javaClass.canonicalName))
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

    override fun updateVisibleAndOrder(menuList: List<MenuEntity?>?): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                menuList?.forEach { entity ->
                    entity?.let {
                        val stored = Select().from(MenuEntity::class.java)
                            .where(MenuEntity_Table.Codigo.eq(entity.codigo))
                            .querySingle()
                        if (stored != null) {
                            SQLite.update(MenuEntity::class.java)
                                .set(MenuEntity_Table.Orden.eq(entity.orden), MenuEntity_Table.Visible.eq(entity.isVisible))
                                .where(MenuEntity_Table.Codigo.eq(entity.codigo))
                                .execute()
                        }
                    }
                }
                emitter.onNext(true)
            } catch (ex: Exception) {
                emitter.onError(SqlException(ex.cause))
            }

            emitter.onComplete()
        }
    }

}

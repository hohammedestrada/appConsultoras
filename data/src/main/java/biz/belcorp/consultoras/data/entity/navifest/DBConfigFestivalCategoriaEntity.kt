package biz.belcorp.consultoras.data.entity.navifest

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Table(database = ConsultorasDatabase::class, name = "DBConfigFestivalCategoriaEntity")
class DBConfigFestivalCategoriaEntity {

    @PrimaryKey(autoincrement = true)
    @Column
    var uid: Long = 0L

    @Column(name = "IdFestivalCategoria")
    var idFestivalCategoria: Int? = null

    @Column(name = "IdFestival")
    var idFestival: Int? = null

    @Column(name = "CodigoCategoria")
    var codigoCategoria: String? = null

    @Column(name = "Activo")
    var isActivo: Boolean? = null
}

package biz.belcorp.consultoras.data.entity.navifest

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Table(database = ConsultorasDatabase::class, name = "DBConfiguracionFestivalEntity")
class DBConfiguracionFestivalEntity {

    @PrimaryKey(autoincrement = true)
    @Column
    var uid: Long = 0L

    @Column(name = "IdFestival")
    var idFestival: Int? = null

    @Column(name = "CampaniaId")
    var campaniaId: Int? = null

    @Column(name = "PremioGratis")
    var isPremioGratis: Boolean? = null

    @Column(name = "Tipo")
    var tipo: Int? = null

    @Column(name = "Activo")
    var isActivo: Boolean? = false

    @Column(name = "Titulo")
    var titulo: String? = null

    @Column(name = "DescripcionCorta")
    var descripcionCorta: String? = null

    @Column(name = "DescripcionLarga")
    var descripcionLarga: String? = null

}

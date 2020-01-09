package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.structure.BaseModel

import java.io.Serializable

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "MenuEntity")
class MenuEntity : BaseModel(), Serializable {

    @Column(name = "MenuAppId")
    @PrimaryKey
    @SerializedName("MenuAppId")
    var menuAppId: Int? = null

    @Column(name = "Codigo")
    @SerializedName("Codigo")
    var codigo: String? = null

    @Column(name = "Descripcion")
    @SerializedName("Descripcion")
    var descripcion: String? = null

    @Column(name = "Orden")
    @SerializedName("Orden")
    var orden: Int? = null

    @Column(name = "CodigoMenuPadre")
    @SerializedName("CodigoMenuPadre")
    var codigoMenuPadre: String? = null

    @Column(name = "Posicion")
    @SerializedName("Posicion")
    var posicion: Int? = null

    @Column(name = "Visible")
    @SerializedName("Visible")
    var isVisible: Boolean? = null

    @Column(name = "FlagMenuNuevo")
    @SerializedName("FlagMenuNuevo")
    var FlagMenuNuevo: Int? = 0

    @SerializedName("SubMenus")
    var subMenus: List<MenuEntity?>? = null

    val subMenusDB: List<MenuEntity?>?
        @OneToMany(methods = [(OneToMany.Method.ALL)], variableName = "subMenus", isVariablePrivate = true)
        get() {
            if (subMenus == null) {
                subMenus = SQLite.select()
                    .from(MenuEntity::class.java)
                    .where(MenuEntity_Table.CodigoMenuPadre.eq(codigo))
                    .and(MenuEntity_Table.Visible.eq(true))
                    .orderBy(MenuEntity_Table.Orden, true)
                    .queryList()
            }
            return subMenus
        }

    override fun toString(): String {
        return "MenuEntity{" +
            "menuAppId=" + menuAppId +
            ", codigo='" + codigo + '\''.toString() +
            ", descripcion='" + descripcion + '\''.toString() +
            ", orden=" + orden +
            ", codigoMenuPadre='" + codigoMenuPadre + '\''.toString() +
            ", posicion=" + posicion +
            ", subMenus=" + subMenus +
            '}'.toString()
    }
}

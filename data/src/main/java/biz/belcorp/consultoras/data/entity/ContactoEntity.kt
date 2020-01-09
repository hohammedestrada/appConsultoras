package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

import java.io.Serializable

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "Contacto")
class ContactoEntity : BaseModel(), Serializable {


    @Column(name = "ID")
    @PrimaryKey(autoincrement = true)
    @SerializedName("ID")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column(name = "ContactoClienteID")
    @SerializedName("ContactoClienteID")
    var contactoClienteID: Int? = null

    @Column(name = "ClienteID")
    @SerializedName("ClienteID")
    var clienteID: Int? = null

    @Column(name = "TipoContactoID")
    @SerializedName("TipoContactoID")
    var tipoContactoID: Int? = null

    @Column(name = "Valor")
    @SerializedName("Valor")
    var valor: String? = null

    @Column(name = "ClienteLocalID")
    @SerializedName("ClienteLocalID")
    @Expose(serialize = false, deserialize = false)
    var clienteLocalID: Int? = null

    @Column(name = "Estado")
    @SerializedName("Estado")
    var estado: Int? = null
}

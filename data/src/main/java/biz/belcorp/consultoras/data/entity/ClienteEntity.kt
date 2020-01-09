package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.OneToMany
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.structure.BaseModel

import java.io.Serializable
import java.math.BigDecimal

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = ClienteEntity.NAME)
class ClienteEntity : BaseModel(), Serializable {

    companion object {
        const val ENDPOINT = "Client"
        const val NAME = "Cliente"
    }

    @Column(name = "ID")
    @PrimaryKey(autoincrement = true)
    @SerializedName("ID")
    @Expose(serialize = false, deserialize = false)
    var id: Int? = null

    @Column(name = "ClienteID")
    @SerializedName("ClienteID")
    var clienteID: Int? = null

    @Column(name = "Apellidos")
    @SerializedName("Apellidos")
    var apellidos: String? = null

    @Column(name = "Nombres")
    @SerializedName("Nombres")
    var nombres: String? = null

    @Column(name = "Alias")
    @SerializedName("Alias")
    var alias: String? = null

    @Column(name = "Foto")
    @SerializedName("Foto")
    var foto: String? = null

    @Column(name = "FechaNacimiento")
    @SerializedName("FechaNacimiento")
    var fechaNacimiento: String? = null

    @Column(name = "Sexo")
    @SerializedName("Sexo")
    var sexo: String? = null

    @Column(name = "Documento")
    @SerializedName("Documento")
    var documento: String? = null

    @Column(name = "Origen")
    @SerializedName("Origen")
    var origen: String? = null

    @Column(name = "Favorito")
    @SerializedName("Favorito")
    var favorito: Int? = null

    @Column(name = "Estado")
    @SerializedName("Estado")
    var estado: Int? = null

    @Column(name = "TipoRegistro")
    @SerializedName("TipoRegistro")
    var tipoRegistro: Int? = null

    @Column(name = "TotalDeuda")
    @SerializedName(value = "TotalDeuda", alternate = ["Saldo"])
    @Expose
    var totalDeuda: BigDecimal? = null

    @Column(name = "Sincronizado")
    @SerializedName("Sincronizado")
    @Expose(serialize = false, deserialize = false)
    var sincronizado: Int? = null

    @Column(name = "TipoContactoFavorito")
    @SerializedName("TipoContactoFavorito")
    var tipoContactoFavorito: Int? = null

    @Column(name = "CantidadProductos")
    @SerializedName("CantidadProductos")
    var cantidadProductos: Int? = null

    @Column(name = "CodigoRespuesta")
    @SerializedName("CodigoRespuesta")
    var codigoRespuesta: String? = null

    @Column(name = "MensajeRespuesta")
    @SerializedName("MensajeRespuesta")
    var mensajeRespuesta: String? = null

    @Column(name = "CantidadPedido")
    @SerializedName("CantidadPedido")
    var cantidadPedido: Int? = null

    @Column(name = "MontoPedido")
    @SerializedName(value = "MontoPedido")
    var montoPedido: BigDecimal? = null

    var error: Throwable? = null

    @SerializedName("Contactos")
    @Expose
    var contactoEntities: List<ContactoEntity?>? = null
    @SerializedName("Notas")
    @Expose
    var anotacionEntities: List<AnotacionEntity?>? = null
    @SerializedName("Recordatorios")
    @Expose
    var recordatorioEntities: List<RecordatorioEntity?>? = null
    @SerializedName("Movimientos")
    @Expose
    var movimientoEntities: List<ClientMovementEntity?>? = null

    val anotacionesDB: List<AnotacionEntity>
        @OneToMany(methods = [(OneToMany.Method.ALL)], variableName = "anotacionEntities", isVariablePrivate = true)
        get() {
            if (anotacionEntities == null) {
                anotacionEntities = SQLite.select()
                        .from(AnotacionEntity::class.java)
                        .where(AnotacionEntity_Table.ClienteLocalID.eq(id))
                        .and(AnotacionEntity_Table.Estado.notEq(-1))
                        .orderBy(AnotacionEntity_Table.ID, false)
                        .queryList()
            }
            return anotacionEntities as List<AnotacionEntity>
        }

    val movimientoEntitiesDB: List<ClientMovementEntity>
        @OneToMany(methods = [(OneToMany.Method.ALL)], variableName = "movimientoEntities", isVariablePrivate = true)
        get() {
            if (movimientoEntities == null || movimientoEntities!!.isEmpty()) {
                movimientoEntities = SQLite.select()
                        .from(ClientMovementEntity::class.java)
                        .where(ClientMovementEntity_Table.ClienteLocalID.eq(id))
                        .and(ContactoEntity_Table.Estado.notEq(-1))
                        .queryList()
            }
            return movimientoEntities as List<ClientMovementEntity>
        }

    @OneToMany(methods = [(OneToMany.Method.ALL)], variableName = "contactoEntities", isVariablePrivate = true)
    fun contactoEntities(): List<ContactoEntity> {
        if (contactoEntities == null || contactoEntities!!.isEmpty()) {
            contactoEntities = SQLite.select()
                    .from(ContactoEntity::class.java)
                    .where(ContactoEntity_Table.ClienteLocalID.eq(id))
                    .and(ContactoEntity_Table.Estado.eq(1))
                    .queryList()
        }
        return contactoEntities as List<ContactoEntity>
    }

    @OneToMany(methods = [(OneToMany.Method.ALL)], variableName = "recordatorioEntities", isVariablePrivate = true)
    fun recordatorioEntities(): List<RecordatorioEntity> {
        if (recordatorioEntities == null) {
            recordatorioEntities = SQLite.select()
                    .from(RecordatorioEntity::class.java)
                    .where(RecordatorioEntity_Table.ClienteLocalID.eq(id))
                    .and(ContactoEntity_Table.Estado.notEq(-1))
                    .queryList()
        }
        return recordatorioEntities as List<RecordatorioEntity>
    }


}

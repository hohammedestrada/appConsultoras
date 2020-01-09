package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "Device")
class DeviceEntity {

    @Column(name = "ID")
    @PrimaryKey(autoincrement = true)
    @SerializedName("Id")
    var id: Int? = null

    @Column(name = "AppID")
    @SerializedName("AppID")
    var appID: Int? = null

    @Column(name = "Pais")
    @SerializedName("Pais")
    var pais: String? = null

    @Column(name = "RolID")
    @SerializedName("RolID")
    var rolId: String? = null

    @Column(name = "Usuario")
    @SerializedName("Usuario")
    var usuario: String? = null

    @Column(name = "UUID")
    @SerializedName("UUID")
    var uuid: String? = null

    @Column(name = "IMEI")
    @SerializedName("IMEI")
    var imei: String? = null

    @Column(name = "SO")
    @SerializedName("SO")
    var so: String? = null

    @Column(name = "Modelo")
    @SerializedName("Modelo")
    var modelo: String? = null

    @Column(name = "Version")
    @SerializedName("Version")
    var version: String? = null

    @Column(name = "TokenFCM")
    @SerializedName("TokenFCM")
    var tokenFCM: String? = null

    @Column(name = "TopicFCM")
    @SerializedName("TopicFCM")
    var topicFCM: String? = null
}

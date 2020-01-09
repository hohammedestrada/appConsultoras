package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

import java.io.Serializable

import biz.belcorp.consultoras.data.db.ConsultorasDatabase

@Table(database = ConsultorasDatabase::class, name = "FacebookProfile")
class FacebookProfileEntity : BaseModel(), Serializable {

    @Column(name = "Id")
    @PrimaryKey
    @SerializedName("id")
    var id: String? = null

    @Column(name = "Name")
    @SerializedName("name")
    var name: String? = null

    @Column(name = "Email")
    @SerializedName("email")
    var email: String? = null

    @Column(name = "Image")
    @SerializedName("image")
    var image: String? = null

    @Column(name = "FirstName")
    @SerializedName("firstName")
    var firstName: String? = null

    @Column(name = "LastName")
    @SerializedName("lastName")
    var lastName: String? = null

    @Column(name = "LinkProfile")
    @SerializedName("linkProfile")
    var linkProfile: String? = null

    @Column(name = "Birthday")
    @SerializedName("birthday")
    var birthday: String? = null

    @Column(name = "Gender")
    @SerializedName("gender")
    var gender: String? = null

    @Column(name = "Location")
    @SerializedName("location")
    var location: String? = null
}

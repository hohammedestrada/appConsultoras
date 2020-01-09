package biz.belcorp.consultoras.data.entity.navifest

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Table(database = ConsultorasDatabase::class, name = "DBConfigBannerSelloEntity")
class DBConfigBannerSelloEntity {

    @PrimaryKey(autoincrement = true)
    @Column
    var uid: Long = 0L

    @Column(name = "BannerImgDesktop")
    var bannerImgDesktop: String? = null

    @Column(name = "BannerImgMobile")
    var bannerImgMobile: String? = null

    @Column(name = "BannerImgProducto")
    var bannerImgProducto: String? = null

    @Column(name = "BannerFondoColorInicio")
    var bannerFondoColorInicio: String? = null

    @Column(name = "BannerFondoColorFin")
    var bannerFondoColorFin: String? = null

    @Column(name = "BannerFondoColorDireccion")
    var bannerFondoColorDireccion: Int? = null

    @Column(name = "BannerColorTexto")
    var bannerColorTexto: String? = null

    @Column(name = "BannerDescripcion")
    var bannerDescripcion: String? = null

    @Column(name = "SelloColorInicio")
    var selloColorInicio: String? = null

    @Column(name = "SelloColorFin")
    var selloColorFin: String? = null

    @Column(name = "SelloColorDireccion")
    var selloColorDireccion: Int? = null

    @Column(name = "SelloTexto")
    var selloTexto: String? = null

    @Column(name = "SelloColorTexto")
    var selloColorTexto: String? = null
}

package biz.belcorp.consultoras.data.manager

import android.content.Context
import biz.belcorp.consultoras.data.entity.BackupEntity
import biz.belcorp.library.security.AesEncryption
import biz.belcorp.library.security.BelcorpEncryption
import biz.belcorp.library.util.PreferenceUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupManager @Inject
internal constructor(context: Context) : IBackupManager {

    private val pref: PreferenceUtil = PreferenceUtil.Builder(context).build()
    private val encryption: BelcorpEncryption

    private var backup: BackupEntity? = null

    init {
        this.encryption = AesEncryption.newInstance()
    }

    override fun saveBackup(entity: BackupEntity): Boolean {
        getPref()

        if (null != entity.cliente) backup!!.cliente = entity.cliente
        if (null != entity.anotacion) backup!!.anotacion = entity.anotacion
        if (null != entity.contacto) backup!!.contacto = entity.contacto
        if (null != entity.recordatorio) backup!!.recordatorio = entity.recordatorio
        if (null != entity.campania) backup!!.campania = entity.campania
        if (null != entity.concurso) backup!!.concurso = entity.concurso
        if (null != entity.movimiento) backup!!.movimiento = entity.movimiento
        if (null != entity.producto) backup!!.producto = entity.producto
        if (null != entity.config) backup!!.config = entity.config
        if (null != entity.country) backup!!.country = entity.country
        if (null != entity.facebook) backup!!.facebook = entity.facebook
        if (null != entity.menu) backup!!.menu = entity.menu
        if (null != entity.premio) backup!!.premio = entity.premio
        if (null != entity.user) backup!!.user = entity.user
        if (null != entity.detalleUser) backup!!.detalleUser = entity.detalleUser
        if (null != entity.origenPedidoWebLocal) backup!!.origenPedidoWebLocal = entity.origenPedidoWebLocal
        if (null != entity.origenMarcacionWebLocal) backup!!.origenMarcacionWebLocal = entity.origenMarcacionWebLocal
        if (null != entity.palancaWebLocal) backup!!.palancaWebLocal = entity.palancaWebLocal
        if (null != entity.subseccionWebLocal) backup!!.subseccionWebLocal = entity.subseccionWebLocal
        if (null != entity.bannerSello) backup!!.bannerSello = entity.bannerSello
        if (null != entity.festivalCategoria) backup!!.festivalCategoria = entity.festivalCategoria
        if (null != entity.configFestival) backup!!.configFestival = entity.configFestival
        if (null != entity.searchRecentOffer) backup!!.searchRecentOffer = entity.searchRecentOffer
        if (null != entity.ofertaFinalEstado) backup!!.ofertaFinalEstado = entity.ofertaFinalEstado
        return pref.save<BackupEntity>(KEY_NAME, backup)
    }

    override fun getBackup(): BackupEntity {
        return pref.get(KEY_NAME, BackupEntity::class.java)
    }

    private fun getPref() {
        backup = pref.get(KEY_NAME, BackupEntity::class.java)
    }

    companion object {
        private const val KEY_NAME = "BACKUP"
    }
}

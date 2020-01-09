package biz.belcorp.consultoras.data.db

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.entity.caminobrillante.LogroCaminoBrillanteEntity
import biz.belcorp.consultoras.data.entity.caminobrillante.NivelCaminoBrillanteEntity
import biz.belcorp.consultoras.data.entity.caminobrillante.NivelConsultoraCaminoBrillanteEntity
import com.raizlabs.android.dbflow.annotation.Database
import com.raizlabs.android.dbflow.annotation.Migration
import com.raizlabs.android.dbflow.sql.SQLiteType
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration


/**
 * Base de datos
 *
 * @version 11.0
 * @since 2017-04-14
 * @update 2018-11-08
 */
@Database(name = ConsultorasDatabase.NAME, version = ConsultorasDatabase.VERSION)
class ConsultorasDatabase {

    companion object {
        const val NAME = "ConsultorasDatabase"
        const val VERSION = 22
    }

    /** Migrations */

    @Migration(version = 8, database = ConsultorasDatabase::class)
    class Migration1 : AlterTableMigration<CountryEntity>(CountryEntity::class.java) {

        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "CaptureData")
        }
    }

    @Migration(version = 8, database = ConsultorasDatabase::class)
    class Migration2 : AlterTableMigration<UserEntity>(UserEntity::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT, "horaFinPortal")
        }
    }

    @Migration(version = 9, database = ConsultorasDatabase::class)
    class Migration3 : AlterTableMigration<CountryEntity>(CountryEntity::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT, "Telefono1")
            addColumn(SQLiteType.TEXT, "Telefono2")
            addColumn(SQLiteType.TEXT, "UrlContratoActualizacionDatos")
            addColumn(SQLiteType.TEXT, "UrlContratoVinculacion")
        }
    }

    @Migration(version = 9, database = ConsultorasDatabase::class)
    class Migration4 : AlterTableMigration<UserEntity>(UserEntity::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "IndicadorContratoCredito")
            addColumn(SQLiteType.INTEGER, "CambioCorreoPendiente")
            addColumn(SQLiteType.TEXT, "CorreoPendiente")
            addColumn(SQLiteType.TEXT, "PrimerNombre")
            addColumn(SQLiteType.INTEGER, "PuedeActualizar")
            addColumn(SQLiteType.INTEGER, "PuedeActualizarEmail")
            addColumn(SQLiteType.INTEGER, "PuedeActualizarCelular")
        }
    }

    @Migration(version = 10, database = ConsultorasDatabase::class)
    class Migration5 : AlterTableMigration<UserEntity>(UserEntity::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "MostrarBuscador")
            addColumn(SQLiteType.INTEGER, "CaracteresBuscador")
            addColumn(SQLiteType.INTEGER, "CaracteresBuscadorMostrar")
            addColumn(SQLiteType.INTEGER, "TotalResultadosBuscador")
            addColumn(SQLiteType.INTEGER, "Lider")
            addColumn(SQLiteType.INTEGER, "RDEsActiva")
            addColumn(SQLiteType.INTEGER, "RDEsSuscrita")
            addColumn(SQLiteType.INTEGER, "RDActivoMdo")
            addColumn(SQLiteType.INTEGER, "RDTieneRDC")
            addColumn(SQLiteType.INTEGER, "RDTieneRDI")
            addColumn(SQLiteType.INTEGER, "RDTieneRDCR")
            addColumn(SQLiteType.INTEGER, "DiaFacturacion")
        }
    }

    @Migration(version = 11, database = ConsultorasDatabase::class)
    class Migration6 : AlterTableMigration<UserEntity>(UserEntity::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "IndicadorConsultoraDummy")
            addColumn(SQLiteType.TEXT, "PersonalizacionesDummy")
            addColumn(SQLiteType.INTEGER, "MostrarBotonVerTodosBuscador")
            addColumn(SQLiteType.INTEGER, "AplicarLogicaCantidadBotonVerTodosBuscador")
            addColumn(SQLiteType.INTEGER, "MostrarOpcionesOrdenamientoBuscador")
            addColumn(SQLiteType.INTEGER, "TieneMG")
        }
    }

    @Migration(version = 12, database = ConsultorasDatabase::class)
    class Migration7 : AlterTableMigration<UserEntity>(UserEntity::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "MostrarFiltrosBuscador")
            addColumn(SQLiteType.INTEGER, "TienePagoEnLinea")
            addColumn(SQLiteType.INTEGER, "TieneChatBot")
        }
    }

    @Migration(version = 13, database = ConsultorasDatabase::class)
    class Migration8 : AlterTableMigration<UserEntity>(UserEntity::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT, "TipoIngreso")
            addColumn(SQLiteType.TEXT, "SegmentoDatami")
        }
    }

    @Migration(version = 13, database = ConsultorasDatabase::class)
    class Migration9 : AlterTableMigration<OrderListItemEntity>(OrderListItemEntity::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "FlagNueva")
            addColumn(SQLiteType.INTEGER, "EnRangoProgNuevas")
            addColumn(SQLiteType.INTEGER, "EsDuoPerfecto")
        }
    }

    @Migration(version = 14, database = ConsultorasDatabase::class)
    class Migration10: AlterTableMigration<OrderListItemEntity>(OrderListItemEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER,"EsPremioElectivo")
            addColumn(SQLiteType.INTEGER, "FlagMenuNuevo")
        }
    }

    @Migration(version = 15, database = ConsultorasDatabase::class)
    class Migration11: AlterTableMigration<UserEntity>(UserEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER,"IndicadorConsultoraDigital")
            addColumn(SQLiteType.INTEGER,"GanaMasNativo")
        }
    }

    @Migration(version = 16, database = ConsultorasDatabase::class)
    class Migration12 : AlterTableMigration<NivelCaminoBrillanteEntity>(NivelCaminoBrillanteEntity::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "EnterateMas")
            addColumn(SQLiteType.TEXT, "EnterateMasParam")
            addColumn(SQLiteType.INTEGER, "Puntaje")
            addColumn(SQLiteType.INTEGER, "PuntajeAcumulado")
        }
    }

    @Migration(version = 16, database = ConsultorasDatabase::class)
    class Migration13 : AlterTableMigration<NivelConsultoraCaminoBrillanteEntity>(NivelConsultoraCaminoBrillanteEntity::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "FlagSeleccionMisGanancias")
        }
    }

    @Migration(version = 16, database = ConsultorasDatabase::class)
    class Migration14: AlterTableMigration<UserEntity>(UserEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT,"PrimerApellido")
            addColumn(SQLiteType.TEXT,"FechaNacimiento")
            addColumn(SQLiteType.INTEGER,"BloqueoPendiente")
            addColumn(SQLiteType.INTEGER,"ActualizacionDatos")
            addColumn(SQLiteType.INTEGER,"NotificacionesWhatsapp")
            addColumn(SQLiteType.INTEGER,"ShowCheckWhatsapp")
            addColumn(SQLiteType.INTEGER,"EsUltimoDiaFacturacion")
        }
    }

    @Migration(version = 17, database = ConsultorasDatabase::class)
    class Migration15: AlterTableMigration<UserEntity>(UserEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER,"PagoContado")
        }
    }

    @Migration(version = 17, database = ConsultorasDatabase::class)
    class Migration16: AlterTableMigration<ConcursoEntity>(ConcursoEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER,"NivelSiguiente")
        }
    }

    @Migration(version = 18, database = ConsultorasDatabase::class)
    class Migration17: AlterTableMigration<UserEntity>(UserEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER,"CambioCelularPendiente")
            addColumn(SQLiteType.TEXT, "CelularPendiente")

        }
    }

    @Migration(version = 18, database = ConsultorasDatabase::class)
    class Migration18: AlterTableMigration<MyOrderEntity>(MyOrderEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER,"EstadoEncuesta")
        }
    }

    @Migration(version = 18, database = ConsultorasDatabase::class)
    class Migration19: AlterTableMigration<LogroCaminoBrillanteEntity.IndicadorEntity.MedallaEntity>(LogroCaminoBrillanteEntity.IndicadorEntity.MedallaEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER,"isDestacado")
        }
    }

    @Migration(version = 18, database = ConsultorasDatabase::class)
    class Migration20: AlterTableMigration<NivelCaminoBrillanteEntity>(NivelCaminoBrillanteEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT,"Mensaje")
        }
    }

    @Migration(version = 19, database = ConsultorasDatabase::class)
    class Migration21: AlterTableMigration<UserEntity>(UserEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER,"esBrillante")
            addColumn(SQLiteType.INTEGER,"MontoMaximoDesviacion")
        }
    }

    @Migration(version = 19, database = ConsultorasDatabase::class)
    class Migration22: AlterTableMigration<ParamsEntity>(ParamsEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER,"esBrillante")
        }
    }

    @Migration(version = 19, database = ConsultorasDatabase::class)
    class Migration23: AlterTableMigration<CatalogoEntity>(CatalogoEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER,"UrlDescargaEstado")
        }
    }

    @Migration(version = 19, database = ConsultorasDatabase::class)
    class Migration24: AlterTableMigration<OrderListItemEntity>(OrderListItemEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER,"EliminableSE")
        }
    }

    @Migration(version = 20, database = ConsultorasDatabase::class)
    class Migration25: AlterTableMigration<UserEntity>(UserEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER,"flagMultipedido")
            addColumn(SQLiteType.TEXT,"lineaConsultora")
        }
    }

    @Migration(version = 21, database = ConsultorasDatabase::class)
    class Migration26: AlterTableMigration<SearchRecentOfferEntity>(SearchRecentOfferEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT,"codigoConsultora")
            addColumn(SQLiteType.TEXT,"countryIso")
        }
    }

    @Migration(version = 21, database = ConsultorasDatabase::class)
    class Migration27: AlterTableMigration<NivelConsultoraCaminoBrillanteEntity>(NivelConsultoraCaminoBrillanteEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT, "IndCambioNivel")
            addColumn(SQLiteType.TEXT, "MensajeCambioNivel")
        }
    }

    @Migration(version = 21, database = ConsultorasDatabase::class)
    class Migration28: AlterTableMigration<UserEntity>(UserEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT, "DescripcionNivelLider")
            addColumn(SQLiteType.TEXT, "Periodo")
            addColumn(SQLiteType.TEXT, "SemanaPeriodo")
        }
    }

    @Migration(version = 22, database = ConsultorasDatabase::class)
    class Migration29 : AlterTableMigration<OrderListItemEntity>(OrderListItemEntity::class.java) {
        override fun onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "EsPromocion")
        }
    }

    @Migration(version = 22, database = ConsultorasDatabase::class)
    class Migration30: AlterTableMigration<UserEntity>(UserEntity::class.java){
        override fun onPreMigrate() {
            addColumn(SQLiteType.TEXT,"SiguienteCampania")
        }
    }

}

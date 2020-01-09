package biz.belcorp.consultoras.data.repository.datasource.backup

import android.content.Context

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.sql.language.SQLite

import java.util.ArrayList

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.entity.navifest.DBConfigBannerSelloEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfigFestivalCategoriaEntity
import biz.belcorp.consultoras.data.entity.navifest.DBConfiguracionFestivalEntity
import biz.belcorp.consultoras.data.exception.SessionException
import biz.belcorp.consultoras.data.manager.IBackupManager
import io.reactivex.Observable

class BackupLocalDataStore(private val backupManager: IBackupManager, internal var context: Context)
    : BackupDataStore {

    override fun backup(): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                FlowManager.getDatabase(ConsultorasDatabase::class.java).beginTransactionAsync { databaseWrapper ->

                    databaseWrapper.beginTransaction()

                    val listaConfig = SQLite.select(ConfigEntity_Table.ID,
                        ConfigEntity_Table.ConnectivityType, ConfigEntity_Table.Notification,
                        ConfigEntity_Table.Sonido).from(ConfigEntity::class.java).queryList()

                    val listaCountry = SQLite.select(CountryEntity_Table.Id,
                        CountryEntity_Table.Name, CountryEntity_Table.UrlImage,
                        CountryEntity_Table.ISO, CountryEntity_Table.FocusBrand,
                        CountryEntity_Table.TextHelpUser, CountryEntity_Table.TextHelpKey,
                        CountryEntity_Table.UrlJoinBelcorp, CountryEntity_Table.ConfigForgotPassword,
                        CountryEntity_Table.ShowDecimals, CountryEntity_Table.UrlTerminos,
                        CountryEntity_Table.UrlPrivacidad, CountryEntity_Table.DestinatariosFeedback,
                        CountryEntity_Table.NotaCantidadMaxima, CountryEntity_Table.MovimientoCantidadMaxima,
                        CountryEntity_Table.MovimientoHistoricoMes, CountryEntity_Table.CaptureData,
                        CountryEntity_Table.Telefono1, CountryEntity_Table.Telefono2,
                        CountryEntity_Table.UrlContratoActualizacionDatos,
                        CountryEntity_Table.UrlContratoVinculacion
                    ).from(CountryEntity::class.java).queryList()

                    val listaUser = SQLite.select(UserEntity_Table.ConsultantId,
                        UserEntity_Table.CountryId, UserEntity_Table.CountryISO,
                        UserEntity_Table.CountryMoneySymbol, UserEntity_Table.ConsultantCode,
                        UserEntity_Table.ConsultantAssociateId, UserEntity_Table.UserCode,
                        UserEntity_Table.UserTest, UserEntity_Table.UserType,
                        UserEntity_Table.ConsultantName, UserEntity_Table.Alias,
                        UserEntity_Table.Email, UserEntity_Table.Phone, UserEntity_Table.OtherPhone,
                        UserEntity_Table.Mobile, UserEntity_Table.PhotoProfile, UserEntity_Table.Campaing,
                        UserEntity_Table.NumberOfCampaings, UserEntity_Table.RegionID,
                        UserEntity_Table.RegionCode, UserEntity_Table.ZoneID, UserEntity_Table.ZoneCode,
                        UserEntity_Table.ExpirationDate, UserEntity_Table.DayProl,
                        UserEntity_Table.ConsultantAcceptDA, UserEntity_Table.UrlBelcorpChat,
                        UserEntity_Table.BillingStartDate, UserEntity_Table.EndTime,
                        UserEntity_Table.TimeZone, UserEntity_Table.ClosingDays,
                        UserEntity_Table.HasDayOffer, UserEntity_Table.HasDayOffer,
                        UserEntity_Table.ShowRoom, UserEntity_Table.ShowRoom,
                        UserEntity_Table.AceptaTerminosCondiciones, UserEntity_Table.AceptaPoliticaPrivacidad,
                        UserEntity_Table.DestinatariosFeedback, UserEntity_Table.GPRMostrarBannerRechazo,
                        UserEntity_Table.GPRBannerTitulo, UserEntity_Table.GPRBannerMensaje,
                        UserEntity_Table.GPRBannerUrl, UserEntity_Table.GPRTextovinculo,
                        UserEntity_Table.Birthday, UserEntity_Table.Anniversary,
                        UserEntity_Table.PasoSextoPedido, UserEntity_Table.RevistaDigitalSuscripcion,
                        UserEntity_Table.UrlBannerGanaMas, UserEntity_Table.CuponEstado,
                        UserEntity_Table.CuponPctDescuento, UserEntity_Table.CuponMontoMaxDscto,
                        UserEntity_Table.TieneGND, UserEntity_Table.CuponTipoCondicion,
                        UserEntity_Table.horaFinPortal, UserEntity_Table.IndicadorContratoCredito,
                        UserEntity_Table.CambioCorreoPendiente, UserEntity_Table.CorreoPendiente,
                        UserEntity_Table.PrimerNombre, UserEntity_Table.PuedeActualizar,
                        UserEntity_Table.PuedeActualizarEmail, UserEntity_Table.PuedeActualizarCelular,
                        UserEntity_Table.MostrarBuscador, UserEntity_Table.CaracteresBuscador,
                        UserEntity_Table.CaracteresBuscadorMostrar, UserEntity_Table.TotalResultadosBuscador,
                        UserEntity_Table.Lider, UserEntity_Table.RDEsActiva, UserEntity_Table.RDEsSuscrita,
                        UserEntity_Table.RDActivoMdo, UserEntity_Table.RDTieneRDC,
                        UserEntity_Table.RDTieneRDI, UserEntity_Table.RDTieneRDCR,
                        UserEntity_Table.DiaFacturacion, UserEntity_Table.IndicadorConsultoraDummy,
                        UserEntity_Table.PersonalizacionesDummy
                    )
                        .from(UserEntity::class.java).queryList()

                    val listaDetalleUser = SQLite.select(UserDetailEntity_Table.detailType,
                        UserDetailEntity_Table.amount, UserDetailEntity_Table.value,
                        UserDetailEntity_Table.state, UserDetailEntity_Table.name,
                        UserDetailEntity_Table.detailDescription).from(UserDetailEntity::class.java).queryList()

                    val listaFacebook = SQLite.select(FacebookProfileEntity_Table.Id,
                        FacebookProfileEntity_Table.Name, FacebookProfileEntity_Table.Email,
                        FacebookProfileEntity_Table.Image, FacebookProfileEntity_Table.FirstName,
                        FacebookProfileEntity_Table.LastName, FacebookProfileEntity_Table.LinkProfile,
                        FacebookProfileEntity_Table.Birthday, FacebookProfileEntity_Table.Gender,
                        FacebookProfileEntity_Table.Location).from(FacebookProfileEntity::class.java).queryList()

                    val listaOrigenPedidoWeb = SQLite.select(OrigenPedidoWebLocalEntity_Table.uid,
                        OrigenPedidoWebLocalEntity_Table.tipoOferta, OrigenPedidoWebLocalEntity_Table.codigo,
                        OrigenPedidoWebLocalEntity_Table.valor).from(OrigenPedidoWebLocalEntity::class.java).queryList()

                    val listaMarcacionWeb = SQLite.select(OrigenMarcacionWebLocalEntity_Table.uid,
                        OrigenMarcacionWebLocalEntity_Table.codigo, OrigenMarcacionWebLocalEntity_Table.valor)
                        .from(OrigenMarcacionWebLocalEntity::class.java).queryList()

                    val listaPalanca = SQLite.select(PalancaEntity_Table.uid,
                        PalancaEntity_Table.codigo, PalancaEntity_Table.valor)
                        .from(PalancaEntity::class.java).queryList()

                    val listaSubseccion = SQLite.select(SubseccionEntity_Table.uid,
                        SubseccionEntity_Table.codigo, SubseccionEntity_Table.valor)
                        .from(SubseccionEntity::class.java).queryList()

                    val listSearchRecentOffer = SQLite.select(SearchRecentOfferEntity_Table.uid,
                        SearchRecentOfferEntity_Table.cuv, SearchRecentOfferEntity_Table.nombreOferta,
                        SearchRecentOfferEntity_Table.precioCatalogo, SearchRecentOfferEntity_Table.precioValorizado,
                        SearchRecentOfferEntity_Table.imagenURL, SearchRecentOfferEntity_Table.tipoOferta,
                        SearchRecentOfferEntity_Table.flagFestival, SearchRecentOfferEntity_Table.flagPromocion,
                        SearchRecentOfferEntity_Table.agotado, SearchRecentOfferEntity_Table.flagCatalogo)
                        .from(SearchRecentOfferEntity::class.java).queryList()


                    val listaCliente = ArrayList<ClienteEntity>()
                    val listaAnotacion = ArrayList<AnotacionEntity>()
                    val listaContacto = ArrayList<ContactoEntity>()
                    val listaRecordatorio = ArrayList<RecordatorioEntity>()
                    val listaConcurso = ArrayList<ConcursoEntity>()
                    val listaMovimiento = ArrayList<ClientMovementEntity>()
                    val listaProducto = ArrayList<ProductResponseEntity>()
                    val listaPremio = ArrayList<PremioEntity>()
                    val listaMenu = ArrayList<MenuEntity>()
                    val listBannerSello = ArrayList<DBConfigBannerSelloEntity>()
                    val listFestivalCategory = ArrayList<DBConfigFestivalCategoriaEntity>()
                    val listConfigFestival = ArrayList<DBConfiguracionFestivalEntity>()
                    val listOfertaFinalEstado = ArrayList<OfertaFinalEstadoEntity>()


                    /*

                    val listaMenu = SQLite.select(MenuEntity_Table.MenuAppId, MenuEntity_Table.Codigo, MenuEntity_Table.Descripcion, MenuEntity_Table.Orden, MenuEntity_Table.CodigoMenuPadre, MenuEntity_Table.Posicion).from(MenuEntity::class.java).queryList()

                    */

                    val gson = Gson()

                    val backupEntity = BackupEntity()
                    backupEntity.config = gson.toJson(listaConfig)
                    backupEntity.country = gson.toJson(listaCountry)
                    backupEntity.user = gson.toJson(listaUser)
                    backupEntity.detalleUser = gson.toJson(listaDetalleUser)
                    backupEntity.facebook = gson.toJson(listaFacebook)
                    backupEntity.cliente = gson.toJson(listaCliente)

                    backupEntity.menu = gson.toJson(listaMenu)
                    backupEntity.anotacion = gson.toJson(listaAnotacion)
                    backupEntity.contacto = gson.toJson(listaContacto)
                    backupEntity.recordatorio = gson.toJson(listaRecordatorio)
                    backupEntity.concurso = gson.toJson(listaConcurso)
                    backupEntity.movimiento = gson.toJson(listaMovimiento)
                    backupEntity.producto = gson.toJson(listaProducto)
                    backupEntity.premio = gson.toJson(listaPremio)

                    backupEntity.origenPedidoWebLocal = gson.toJson(listaOrigenPedidoWeb)
                    backupEntity.origenMarcacionWebLocal = gson.toJson(listaMarcacionWeb)
                    backupEntity.palancaWebLocal = gson.toJson(listaPalanca)
                    backupEntity.subseccionWebLocal = gson.toJson(listaSubseccion)
                    backupEntity.bannerSello = gson.toJson(listBannerSello)
                    backupEntity.festivalCategoria = gson.toJson(listFestivalCategory)
                    backupEntity.configFestival = gson.toJson(listConfigFestival)
                    backupEntity.searchRecentOffer = gson.toJson(listSearchRecentOffer)
                    backupEntity.ofertaFinalEstado = gson.toJson(listOfertaFinalEstado)



                    backupManager.saveBackup(backupEntity)

                    databaseWrapper.setTransactionSuccessful()
                    databaseWrapper.endTransaction()

                }.error { _, _ ->
                    emitter.onNext(true)
                    emitter.onComplete()
                }.success {
                    emitter.onNext(true)
                    emitter.onComplete()
                }.build().execute()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun reset(): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                FlowManager.getDatabase(ConsultorasDatabase::class.java).beginTransactionAsync { databaseWrapper ->

                    databaseWrapper.beginTransaction()

                    databaseWrapper.execSQL("DROP TABLE IF EXISTS Contacto")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS Anotacion")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS ProductMovement")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS ClientMovement")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS Recordatorio")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS Cliente")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS Premio")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS Concurso")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS Config")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS Country")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS FacebookProfile")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS MenuEntity")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS UserDetail")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS User")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS Catalogo")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS OrigenPedidoWebLocal")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS OrigenMarcacionWebLocal")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS PalancaWebLocal")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS SubseccionWebLocal")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS DBConfigBannerSelloEntity")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS DBConfigFestivalCategoriaEntity")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS DBConfiguracionFestivalEntity")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS SearchRecentOffer")
                    databaseWrapper.execSQL("DROP TABLE IF EXISTS OfertaFinalEstado")

                    databaseWrapper.setTransactionSuccessful()
                    databaseWrapper.endTransaction()

                }.error { _, error -> emitter.onError(SessionException(error)) }.success {
                    FlowManager.getDatabase(ConsultorasDatabase.NAME).reset(context)
                    emitter.onNext(true)
                    emitter.onComplete()
                }.build().execute()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }

    override fun restore(): Observable<Boolean?> {
        return Observable.create { emitter ->
            try {
                FlowManager.getDatabase(ConsultorasDatabase::class.java).beginTransactionAsync { databaseWrapper ->

                    databaseWrapper.beginTransaction()

                    val gson = Gson()

                    val backupEntity = backupManager.getBackup()

                    //Cliente

                    val listaCliente = gson.fromJson<List<ClienteEntity>>(backupEntity.cliente, object : TypeToken<List<ClienteEntity>>() {

                    }.type)
                    if (null != listaCliente && !listaCliente.isEmpty())
                        FlowManager.getModelAdapter(ClienteEntity::class.java).saveAll(listaCliente)

                    //Anotacion

                    val listaAnotacion = gson.fromJson<List<AnotacionEntity>>(backupEntity.anotacion, object : TypeToken<List<AnotacionEntity>>() {

                    }.type)

                    if (null != listaAnotacion && !listaAnotacion.isEmpty())
                        FlowManager.getModelAdapter(AnotacionEntity::class.java).saveAll(listaAnotacion)

                    //Contacto

                    val listaContacto = gson.fromJson<List<ContactoEntity>>(backupEntity.contacto, object : TypeToken<List<ContactoEntity>>() {

                    }.type)

                    if (null != listaContacto && !listaContacto.isEmpty())
                        FlowManager.getModelAdapter(ContactoEntity::class.java).saveAll(listaContacto)

                    //Recordatorio

                    val listaRecordatorio = gson.fromJson<List<RecordatorioEntity>>(backupEntity.recordatorio, object : TypeToken<List<RecordatorioEntity>>() {

                    }.type)

                    if (null != listaRecordatorio && !listaRecordatorio.isEmpty())
                        FlowManager.getModelAdapter(RecordatorioEntity::class.java).saveAll(listaRecordatorio)

                    //Concurso

                    val listaConcurso = gson.fromJson<List<ConcursoEntity>>(backupEntity.concurso, object : TypeToken<List<ConcursoEntity>>() {

                    }.type)

                    if (null != listaConcurso && !listaConcurso.isEmpty())
                        FlowManager.getModelAdapter(ConcursoEntity::class.java).saveAll(listaConcurso)

                    //Movimientos

                    val listaMovimiento = gson.fromJson<List<ClientMovementEntity>>(backupEntity.movimiento, object : TypeToken<List<ClientMovementEntity>>() {

                    }.type)

                    if (null != listaMovimiento && !listaMovimiento.isEmpty())
                        FlowManager.getModelAdapter(ClientMovementEntity::class.java).saveAll(listaMovimiento)

                    //Productos

                    val listaProducto = gson.fromJson<List<ProductResponseEntity>>(backupEntity.producto, object : TypeToken<List<ProductResponseEntity>>() {

                    }.type)
                    if (null != listaProducto && !listaProducto.isEmpty())
                        FlowManager.getModelAdapter(ProductResponseEntity::class.java).saveAll(listaProducto)

                    //Config

                    val listaConfig = gson.fromJson<List<ConfigEntity>>(backupEntity.config, object : TypeToken<List<ConfigEntity>>() {

                    }.type)

                    if (null != listaProducto && !listaProducto.isEmpty())
                        FlowManager.getModelAdapter(ConfigEntity::class.java).saveAll(listaConfig)

                    //Country

                    val listaCountry = gson.fromJson<List<CountryEntity>>(backupEntity.country, object : TypeToken<List<CountryEntity>>() {

                    }.type)

                    if (null != listaCountry && !listaCountry.isEmpty())
                        FlowManager.getModelAdapter(CountryEntity::class.java).saveAll(listaCountry)

                    //Facebook

                    val listaFacebook = gson.fromJson<List<FacebookProfileEntity>>(backupEntity.facebook, object : TypeToken<List<FacebookProfileEntity>>() {

                    }.type)

                    if (null != listaFacebook && !listaFacebook.isEmpty())
                        FlowManager.getModelAdapter(FacebookProfileEntity::class.java).saveAll(listaFacebook)

                    //Menú

                    val listaMenu = gson.fromJson<List<MenuEntity>>(backupEntity.menu, object : TypeToken<List<MenuEntity>>() {

                    }.type)

                    if (null != listaMenu && !listaMenu.isEmpty())
                        FlowManager.getModelAdapter(MenuEntity::class.java).saveAll(listaMenu)

                    //Premio

                    val listaPremio = gson.fromJson<List<PremioEntity>>(backupEntity.premio, object : TypeToken<List<PremioEntity>>() {

                    }.type)

                    if (null != listaPremio && !listaPremio.isEmpty())
                        FlowManager.getModelAdapter(PremioEntity::class.java).saveAll(listaPremio)

                    //User

                    val listaUser = gson.fromJson<List<UserEntity>>(backupEntity.user, object : TypeToken<List<UserEntity>>() {

                    }.type)
                    if (null != listaUser && !listaUser.isEmpty())
                        FlowManager.getModelAdapter(UserEntity::class.java).saveAll(listaUser)

                    //Detalle User

                    val listaDetalleUser = gson.fromJson<List<UserDetailEntity>>(backupEntity.detalleUser, object : TypeToken<List<UserDetailEntity>>() {

                    }.type)

                    if (null != listaDetalleUser && !listaDetalleUser.isEmpty())
                        FlowManager.getModelAdapter(UserDetailEntity::class.java).saveAll(listaDetalleUser)

                    // OrigenPedidoWeb
                    val listaOrigenPedidoWeb = gson.fromJson<List<OrigenPedidoWebLocalEntity>>(backupEntity.origenPedidoWebLocal, object : TypeToken<List<OrigenPedidoWebLocalEntity>>() {

                    }.type)
                    if (null != listaOrigenPedidoWeb && !listaOrigenPedidoWeb.isEmpty())
                        FlowManager.getModelAdapter(OrigenPedidoWebLocalEntity::class.java).saveAll(listaOrigenPedidoWeb)

                    // OrigenMarcacionWeb
                    val listaOrigenMarcaciones = gson.fromJson<List<OrigenMarcacionWebLocalEntity>>(backupEntity.origenMarcacionWebLocal, object : TypeToken<List<OrigenMarcacionWebLocalEntity>>() {

                    }.type)
                    if (null != listaOrigenMarcaciones && !listaOrigenMarcaciones.isEmpty())
                        FlowManager.getModelAdapter(OrigenMarcacionWebLocalEntity::class.java).saveAll(listaOrigenMarcaciones)

                    // Palanca
                    val listaPalanca = gson.fromJson<List<PalancaEntity>>(backupEntity.palancaWebLocal, object : TypeToken<List<PalancaEntity>>() {

                    }.type)
                    if (null != listaPalanca && !listaPalanca.isEmpty())
                        FlowManager.getModelAdapter(PalancaEntity::class.java).saveAll(listaPalanca)

                    // Subseccion
                    val listaSubseccion = gson.fromJson<List<SubseccionEntity>>(backupEntity.subseccionWebLocal, object : TypeToken<List<SubseccionEntity>>() {

                    }.type)
                    if (null != listaSubseccion && !listaSubseccion.isEmpty())
                        FlowManager.getModelAdapter(SubseccionEntity::class.java).saveAll(listaSubseccion)

                    // BannerSello
                    val listaBannerSello = gson.fromJson<List<DBConfigBannerSelloEntity>>(backupEntity.bannerSello, object : TypeToken<List<DBConfigBannerSelloEntity>>() {

                    }.type)
                    if (null != listaBannerSello && !listaBannerSello.isEmpty())
                        FlowManager.getModelAdapter(DBConfigBannerSelloEntity::class.java).saveAll(listaBannerSello)

                    // FestivalCategoria
                    val listFestivalCategoria = gson.fromJson<List<DBConfigFestivalCategoriaEntity>>(backupEntity.festivalCategoria, object : TypeToken<List<DBConfigFestivalCategoriaEntity>>() {

                    }.type)
                    if (null != listFestivalCategoria && !listFestivalCategoria.isEmpty())
                        FlowManager.getModelAdapter(DBConfigFestivalCategoriaEntity::class.java).saveAll(listFestivalCategoria)

                    // ConfigFestival
                    val listConfigFestival = gson.fromJson<List<DBConfiguracionFestivalEntity>>(backupEntity.configFestival, object : TypeToken<List<DBConfiguracionFestivalEntity>>() {

                    }.type)
                    if (null != listConfigFestival && !listConfigFestival.isEmpty())
                        FlowManager.getModelAdapter(DBConfiguracionFestivalEntity::class.java).saveAll(listConfigFestival)

                    // Ofertas Recientes
                    val listSearchRecentOffer = gson.fromJson<List<SearchRecentOfferEntity>>(backupEntity.searchRecentOffer, object : TypeToken<List<SearchRecentOfferEntity>>() {

                    }.type)
                    if (null != listSearchRecentOffer && !listSearchRecentOffer.isEmpty())
                        FlowManager.getModelAdapter(SearchRecentOfferEntity::class.java).saveAll(listSearchRecentOffer)

                    //OfertaFinal
                    // Ofertas Recientes
                    val listOfertaFinalEstado = gson.fromJson<List<OfertaFinalEstadoEntity>>(backupEntity.ofertaFinalEstado, object : TypeToken<List<OfertaFinalEstadoEntity>>() {

                    }.type)
                    if (null != listOfertaFinalEstado && !listOfertaFinalEstado.isEmpty())
                        FlowManager.getModelAdapter(OfertaFinalEstadoEntity::class.java).saveAll(listOfertaFinalEstado)


                    databaseWrapper.setTransactionSuccessful()
                    databaseWrapper.endTransaction()

                }.error { _, error -> emitter.onError(SessionException(error)) }.success {
                    emitter.onNext(true)
                    emitter.onComplete()
                }.build().execute()
            } catch (ex: Exception) {
                emitter.onError(SessionException(ex))
            }
        }
    }
}

package biz.belcorp.consultoras.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import biz.belcorp.consultoras.ConsultorasApp;
import biz.belcorp.consultoras.UIThread;
import biz.belcorp.consultoras.data.executor.JobExecutor;
import biz.belcorp.consultoras.data.manager.BackupManager;
import biz.belcorp.consultoras.data.manager.IBackupManager;
import biz.belcorp.consultoras.data.manager.ISessionManager;
import biz.belcorp.consultoras.data.manager.SessionManager;
import biz.belcorp.consultoras.data.repository.AccountDataRepository;
import biz.belcorp.consultoras.data.repository.AccountStateDataRepository;
import biz.belcorp.consultoras.data.repository.ApiDataRepository;
import biz.belcorp.consultoras.data.repository.AuthDataRepository;
import biz.belcorp.consultoras.data.repository.BackupDataRepository;
import biz.belcorp.consultoras.data.repository.CaminoBrillanteDataRepository;
import biz.belcorp.consultoras.data.repository.CatalogDataRepository;
import biz.belcorp.consultoras.data.repository.ClienteDataRepository;
import biz.belcorp.consultoras.data.repository.ConfigDataRepository;
import biz.belcorp.consultoras.data.repository.ConfigExtDataRepository;
import biz.belcorp.consultoras.data.repository.CountryDataRepository;
import biz.belcorp.consultoras.data.repository.DebtDataRepository;
import biz.belcorp.consultoras.data.repository.DreamMeterDataRepository;
import biz.belcorp.consultoras.data.repository.FacebookDataRepository;
import biz.belcorp.consultoras.data.repository.GaleryDataRepository;
import biz.belcorp.consultoras.data.repository.FestivaDataRepository;
import biz.belcorp.consultoras.data.repository.IncentivosDataRepository;
import biz.belcorp.consultoras.data.repository.MenuDataRepository;
import biz.belcorp.consultoras.data.repository.NoteDataRepository;
import biz.belcorp.consultoras.data.repository.NotificacionDataRepository;
import biz.belcorp.consultoras.data.repository.OfferDataRepository;
import biz.belcorp.consultoras.data.repository.OrderDataRepository;
import biz.belcorp.consultoras.data.repository.OrigenMarcacionDataRepository;
import biz.belcorp.consultoras.data.repository.OrigenPedidoDataRepository;
import biz.belcorp.consultoras.data.repository.PagoOnlineDataRepository;
import biz.belcorp.consultoras.data.repository.PremioDataRepository;
import biz.belcorp.consultoras.data.repository.ProductDataRepository;
import biz.belcorp.consultoras.data.repository.SessionDataRepository;
import biz.belcorp.consultoras.data.repository.StorieDataRepository;
import biz.belcorp.consultoras.data.repository.SurveyDataRepository;
import biz.belcorp.consultoras.data.repository.SyncDataRepository;
import biz.belcorp.consultoras.data.repository.TrackingDataRepository;
import biz.belcorp.consultoras.data.repository.UserDataRepository;
import biz.belcorp.consultoras.domain.executor.PostExecutionThread;
import biz.belcorp.consultoras.domain.executor.ThreadExecutor;
import biz.belcorp.consultoras.domain.repository.AccountRepository;
import biz.belcorp.consultoras.domain.repository.AccountStateRepository;
import biz.belcorp.consultoras.domain.repository.ApiRepository;
import biz.belcorp.consultoras.domain.repository.AuthRepository;
import biz.belcorp.consultoras.domain.repository.BackupRepository;
import biz.belcorp.consultoras.domain.repository.CaminoBrillanteRepository;
import biz.belcorp.consultoras.domain.repository.CatalogRepository;
import biz.belcorp.consultoras.domain.repository.ClienteRepository;
import biz.belcorp.consultoras.domain.repository.ConfigExtRepository;
import biz.belcorp.consultoras.domain.repository.ConfigRepository;
import biz.belcorp.consultoras.domain.repository.CountryRepository;
import biz.belcorp.consultoras.domain.repository.DebtRepository;
import biz.belcorp.consultoras.domain.repository.DreamMeterRepository;
import biz.belcorp.consultoras.domain.repository.FacebookRepository;
import biz.belcorp.consultoras.domain.repository.OrigenMarcacionRepository;
import biz.belcorp.consultoras.domain.repository.GaleryRepository;
import biz.belcorp.consultoras.domain.repository.FestivalRepository;
import biz.belcorp.consultoras.domain.repository.OrigenPedidoRepository;
import biz.belcorp.consultoras.domain.repository.IncentivosRepository;
import biz.belcorp.consultoras.domain.repository.MenuRepository;
import biz.belcorp.consultoras.domain.repository.NoteRepository;
import biz.belcorp.consultoras.domain.repository.NotificacionRepository;
import biz.belcorp.consultoras.domain.repository.OfferRepository;
import biz.belcorp.consultoras.domain.repository.OrderRepository;
import biz.belcorp.consultoras.domain.repository.PagoOnlineRepository;
import biz.belcorp.consultoras.domain.repository.PremioRepository;
import biz.belcorp.consultoras.domain.repository.ProductRepository;
import biz.belcorp.consultoras.domain.repository.SessionRepository;
import biz.belcorp.consultoras.domain.repository.StorieRepository;
import biz.belcorp.consultoras.domain.repository.SurveyRepository;
import biz.belcorp.consultoras.domain.repository.SyncRepository;
import biz.belcorp.consultoras.domain.repository.TrackingRepository;
import biz.belcorp.consultoras.domain.repository.UserRepository;
import dagger.Module;
import dagger.Provides;

/**
 * Módulo Dagger que proporciona objetos que vivirán durante el ciclo de vida de la aplicación.
 */
@Module
public class AppModule {

    private static final String PREF_CONSULTORAS = "PREF_CONSULTORAS";
    private final ConsultorasApp app;

    /**********************************************************/

    public AppModule(ConsultorasApp app) {
        this.app = app;
    }

    /**********************************************************/

    @Provides
    @Singleton
    Context provideContext() {
        return this.app;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_CONSULTORAS, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    ISessionManager provideSessionManager(SessionManager sessionManager) {
        return sessionManager;
    }

    @Provides
    @Singleton
    IBackupManager backupManager(BackupManager backupManager) {
        return backupManager;
    }

    /**********************************************************/

    @Provides
    @Singleton
    SessionRepository provideSessionRepository(SessionDataRepository sessionDataRepository) {
        return sessionDataRepository;
    }

    @Provides
    @Singleton
    AuthRepository provideAuthRepository(AuthDataRepository authDataRepository) {
        return authDataRepository;
    }

    @Provides
    @Singleton
    AccountRepository provideAccountRepository(AccountDataRepository accountDataRepository) {
        return accountDataRepository;
    }

    @Provides
    @Singleton
    FacebookRepository provideFacebookRepository(FacebookDataRepository facebookDataRepository) {
        return facebookDataRepository;
    }

    @Provides
    @Singleton
    ApiRepository provideApiRepository(ApiDataRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository(UserDataRepository userDataRepository) {
        return userDataRepository;
    }

    @Provides
    @Singleton
    CountryRepository provideCountryRepository(CountryDataRepository countryDataRepository) {
        return countryDataRepository;
    }

    @Provides
    @Singleton
    MenuRepository provideMenuRepository(MenuDataRepository menuDataRepository) {
        return menuDataRepository;
    }

    @Provides
    @Singleton
    ConfigRepository provideConfigRepository(ConfigDataRepository configDataRepository) {
        return configDataRepository;
    }

    @Provides
    @Singleton
    ConfigExtRepository provideConfigExtRepository(ConfigExtDataRepository configExtDataRepository) {
        return configExtDataRepository;
    }

    @Provides
    @Singleton
    ClienteRepository provideClientRepository(ClienteDataRepository clienteDataRepository) {
        return clienteDataRepository;
    }

    @Provides
    @Singleton
    IncentivosRepository provideIncentivesRepository(IncentivosDataRepository incentivesDataRepository) {
        return incentivesDataRepository;
    }

    @Provides
    @Singleton
    DebtRepository provideDebtRepository(DebtDataRepository debtRepository) {
        return debtRepository;
    }

    @Provides
    @Singleton
    BackupRepository backupRepository(BackupDataRepository backupRepository) {
        return backupRepository;
    }

    @Provides
    @Singleton
    SyncRepository syncRepository(SyncDataRepository syncRepository) {
        return syncRepository;
    }

    @Provides
    @Singleton
    NoteRepository noteRepository(NoteDataRepository noteRepository) {
        return noteRepository;
    }

    @Provides
    @Singleton
    OrderRepository orderRepository(OrderDataRepository orderRepository) {
        return orderRepository;
    }

    @Provides
    @Singleton
    CatalogRepository catalogRepository(CatalogDataRepository catalogRepository) {
        return catalogRepository;
    }

    @Provides
    @Singleton
    AccountStateRepository accountStateRepository(AccountStateDataRepository accountStateRepository) {
        return accountStateRepository;
    }

    @Provides
    @Singleton
    TrackingRepository trackingRepository(TrackingDataRepository trackingRepository) {
        return trackingRepository;
    }

    @Provides
    @Singleton
    ProductRepository productRepository(ProductDataRepository productDataRepository) {
        return productDataRepository;
    }


    @Provides
    @Singleton
    PagoOnlineRepository pagoOnlineRepository(PagoOnlineDataRepository pagoOnlineDataRepository) {
        return pagoOnlineDataRepository;
    }

    @Provides
    @Singleton
    NotificacionRepository notificacionRepository(NotificacionDataRepository notificacionDataRepository) {
        return notificacionDataRepository;
    }

    @Provides
    @Singleton
    CaminoBrillanteRepository caminobrillanteRepository(CaminoBrillanteDataRepository caminobrillanteDataRepository) {
        return caminobrillanteDataRepository;
    }


    @Provides
    @Singleton
    OfferRepository offerRepository(OfferDataRepository offerDataRepository) {
        return offerDataRepository;
    }

    @Provides
    @Singleton
    OrigenPedidoRepository origenPedidoRepository(OrigenPedidoDataRepository origenPedidoDataRepository) {
        return origenPedidoDataRepository;
    }


    @Provides
    @Singleton
    OrigenMarcacionRepository origenMarcacionRepository(OrigenMarcacionDataRepository origenMarcacionDataRepository) {
        return origenMarcacionDataRepository;
    }


    @Provides
    @Singleton
    StorieRepository storieRepository(StorieDataRepository storieRepository){
        return  storieRepository;
    }

    @Provides
    @Singleton
    SurveyRepository surveyRepository(SurveyDataRepository surveyRepository){
        return surveyRepository;
    }

    @Provides
    @Singleton
    DreamMeterRepository dreamMeterRepository(DreamMeterDataRepository dreamMeterDataRepository) {
        return dreamMeterDataRepository;
    }


    @Provides
    @Singleton
    GaleryRepository galeryRepository(GaleryDataRepository galeryRepository){
        return galeryRepository;
    }

    @Provides
    @Singleton
    FestivalRepository festivalRepository(FestivaDataRepository festivalRepository){
        return festivalRepository;
    }


    @Provides
    @Singleton
    PremioRepository premiolRepository(PremioDataRepository premioRepository){
        return premioRepository;
    }

}

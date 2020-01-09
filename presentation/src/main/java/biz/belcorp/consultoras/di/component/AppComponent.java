package biz.belcorp.consultoras.di.component;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import biz.belcorp.consultoras.base.BaseActivity;
import biz.belcorp.consultoras.base.BaseService;
import biz.belcorp.consultoras.di.module.AppModule;
import biz.belcorp.consultoras.domain.entity.Premio;
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
import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(BaseActivity activity);

    void inject(BaseService service);

    /**********************************************************/

    Context context();

    SharedPreferences sharedPreferences();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    /**********************************************************/

    SessionRepository sessionRepository();

    AuthRepository authRepository();

    AccountRepository accountRepository();

    FacebookRepository facebookRepository();

    ApiRepository apiRepository();

    UserRepository userRepository();

    CountryRepository countryRepository();

    MenuRepository menuRepository();

    ConfigRepository configRepository();

    ConfigExtRepository configExtRepository();

    ClienteRepository clientRepository();

    IncentivosRepository incentivesRepository();

    DebtRepository provideDebtRepository();

    BackupRepository backupRepository();

    SyncRepository syncRepository();

    NoteRepository noteRepository();

    OrderRepository orderRepository();

    CatalogRepository catalogRepository();

    AccountStateRepository accountStateRepository();

    TrackingRepository trackingRepository();

    ProductRepository productRepository();

    PagoOnlineRepository pagoOnlineRepository();

    NotificacionRepository notificacionRepository();

    OfferRepository offerRepository();
    
    CaminoBrillanteRepository caminobrillanteRepository();

    OrigenPedidoRepository origenPedidoRepository();

    OrigenMarcacionRepository origenMarcacionRepository();

    StorieRepository storieRepository();

    SurveyRepository surveyRepository();

    DreamMeterRepository dreamMeterRepository();

    GaleryRepository galeryRepository();

    FestivalRepository festivalRepository();

    PremioRepository premioRepository();
}

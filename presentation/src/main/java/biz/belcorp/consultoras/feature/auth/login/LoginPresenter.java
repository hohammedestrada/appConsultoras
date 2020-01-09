package biz.belcorp.consultoras.feature.auth.login;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.CredentialsModel;
import biz.belcorp.consultoras.common.model.auth.CredentialsModelDataMapper;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.country.CountryModelDataMapper;
import biz.belcorp.consultoras.common.model.facebook.FacebookProfileModel;
import biz.belcorp.consultoras.common.model.facebook.FacebookProfileModelDataMapper;
import biz.belcorp.consultoras.common.model.kinesis.RemoteConfigDataMapper;
import biz.belcorp.consultoras.common.model.session.SessionModel;
import biz.belcorp.consultoras.common.model.session.SessionModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Country;
import biz.belcorp.consultoras.domain.entity.Login;
import biz.belcorp.consultoras.domain.entity.Verificacion;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.AuthUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.CountryUseCase;
import biz.belcorp.consultoras.domain.interactor.SessionUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.exception.ErrorFactory;
import biz.belcorp.consultoras.util.anotation.AuthType;

@PerActivity
public class LoginPresenter implements Presenter<LoginView> {

    private LoginView loginView;

    private final SessionUseCase sessionUseCase;
    private final CountryUseCase countryUseCase;
    private final AuthUseCase authUseCase;
    private final UserUseCase userUseCase;
    private AccountUseCase accountUseCase;
    private final SessionModelDataMapper sessionModelDataMapper;
    private final CountryModelDataMapper countryModelDataMapper;
    private final CredentialsModelDataMapper credentialsModelDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;
    private final FacebookProfileModelDataMapper facebookProfileModelDataMapper;
    private final RemoteConfigDataMapper remoteConfigDataMapper;

    private CredentialsModel credentialsModel;
    private FacebookProfileModel facebookProfileModel;
    private LoginModel loginModel;

    @Inject
    LoginPresenter(SessionUseCase sessionUseCase,
                   CountryUseCase countryUseCase,
                   AuthUseCase authUseCase,
                   UserUseCase userUseCase,
                   AccountUseCase accountUseCase,
                   SessionModelDataMapper sessionModelDataMapper,
                   CountryModelDataMapper countryModelDataMapper,
                   CredentialsModelDataMapper credentialsModelDataMapper,
                   LoginModelDataMapper loginModelDataMapper,
                   FacebookProfileModelDataMapper facebookProfileModelDataMapper,
                   RemoteConfigDataMapper remoteConfigDataMapper) {
        this.sessionUseCase = sessionUseCase;
        this.countryUseCase = countryUseCase;
        this.authUseCase = authUseCase;
        this.userUseCase = userUseCase;
        this.accountUseCase = accountUseCase;
        this.sessionModelDataMapper = sessionModelDataMapper;
        this.countryModelDataMapper = countryModelDataMapper;
        this.credentialsModelDataMapper = credentialsModelDataMapper;
        this.loginModelDataMapper = loginModelDataMapper;
        this.facebookProfileModelDataMapper = facebookProfileModelDataMapper;
        this.remoteConfigDataMapper = remoteConfigDataMapper;
    }

    @Override
    public void attachView(@NonNull LoginView view) {
        loginView = view;
    }

    @Override
    public void resume() {
        // EMPTY
    }

    @Override
    public void pause() {
        // EMPTY
    }

    @Override
    public void destroy() {
        this.sessionUseCase.dispose();
        this.countryUseCase.dispose();
        this.authUseCase.dispose();
        this.userUseCase.dispose();
        this.loginView = null;
    }

    /**********************************************************/

    public void data() {
        this.countryUseCase.find(new FindCountryByISO());
    }

    public void loginWithFacebook(CredentialsModel credentials, FacebookProfileModel facebookProfile) {
        this.facebookProfileModel = facebookProfile;
        this.credentialsModel = credentials;

        this.loginView.showLoading();

        authUseCase.loginOnline(credentialsModelDataMapper.transform(credentials), new LoginRequest());
    }

    public void getUsabilityConfig(Login login){
        userUseCase.getUsabilityConfig(new LoginPresenter.GetUsabilityConfigObserver(login));
    }

    /**********************************************************/

    private final class FindCountryByISO extends BaseObserver<Country> {
        @Override
        public void onNext(Country country) {
            if (loginView == null) return;
            loginView.renderData(countryModelDataMapper.transform(country));
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (loginView == null) return;
            loginView.renderData(null);
        }
    }

    private final class LoginRequest extends BaseObserver<Login> {

        @Override
        public void onNext(Login login) {
            loginModel = loginModelDataMapper.transform(login);
            userUseCase.getAndSaveInfo(credentialsModel.getPais(), login.getCampaing(),
                login.getRevistaDigitalSuscripcion(), new GetAndSaveMenu(login));
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (loginView == null) return;
            loginView.hideLoading();
            loginView.failed(ErrorFactory.INSTANCE.create(exception));
        }
    }

    private final class GetAndSaveMenu extends BaseObserver<Boolean> {

        private Login login;

        GetAndSaveMenu(Login login) {
            this.login = login;
        }

        @Override
        public void onNext(Boolean result) {
            SessionModel sessionModel = new SessionModel();

            sessionModel.setAuthType(AuthType.FACEBOOK);
            sessionModel.setUsername(credentialsModel.getUsername());
            sessionModel.setPassword(credentialsModel.getPassword());
            sessionModel.setCountry(loginModel.getCountryISO());
            sessionModel.setTokenType(loginModel.getTokenType());
            sessionModel.setAccessToken(loginModel.getAccessToken());
            sessionModel.setRefreshToken(loginModel.getRefreshToken());
            sessionModel.setExpiresIn(loginModel.getExpiresIn());
            sessionModel.setIssued(loginModel.getIssued());
            sessionModel.setExpires(loginModel.getExpires());
            sessionModel.setAceptaTerminosCondiciones(loginModel.isAceptaTerminosCondiciones());
            sessionModel.setTutorial(true);

            authUseCase.save(
                login,
                sessionModelDataMapper.transform(sessionModel),
                facebookProfileModelDataMapper.transform(facebookProfileModel),
                new SaveAuthenticated(login)
            );
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (loginView == null) return;
            loginView.hideLoading();
            if (exception instanceof VersionException){
                VersionException vE = (VersionException) exception;
                loginView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            }
            else {
                loginView.failed(ErrorFactory.INSTANCE.create(exception));
            }
        }
    }

    private final class SaveAuthenticated extends BaseObserver<Boolean> {
        private Login login;

        SaveAuthenticated(Login login) {
            this.login = login;
        }

        @Override
        public void onNext(Boolean result) {
            if (loginView == null) return;
            accountUseCase.verificacion(new VerificacionObserver(login));
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (loginView == null) return;
            loginView.hideLoading();
            loginView.failed(ErrorFactory.INSTANCE.create(exception));
        }
    }

    private final class VerificacionObserver extends BaseObserver<Verificacion>{

        private Login login;

        VerificacionObserver(Login login) {
            this.login = login;
        }

        @Override
        public void onNext(Verificacion verificacion) {
            if (loginView == null) return;
            loginView.success(login, loginModel, verificacion);
        }

    }

    private final class GetUsabilityConfigObserver extends BaseObserver<String> {

        private Login login;

        GetUsabilityConfigObserver(Login login) {
            this.login = login;
        }

        @Override
        public void onNext(String config) {
            loginView.setLogAccess(remoteConfigDataMapper.transform(config), login);
        }
    }



}

package biz.belcorp.consultoras.feature.auth.login.facebook;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.CredentialsModel;
import biz.belcorp.consultoras.common.model.auth.CredentialsModelDataMapper;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.country.CountryModel;
import biz.belcorp.consultoras.common.model.country.CountryModelDataMapper;
import biz.belcorp.consultoras.common.model.facebook.FacebookProfileModel;
import biz.belcorp.consultoras.common.model.facebook.FacebookProfileModelDataMapper;
import biz.belcorp.consultoras.common.model.kinesis.RemoteConfigDataMapper;
import biz.belcorp.consultoras.common.model.session.SessionModel;
import biz.belcorp.consultoras.common.model.session.SessionModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Country;
import biz.belcorp.consultoras.domain.entity.FacebookProfile;
import biz.belcorp.consultoras.domain.entity.Login;
import biz.belcorp.consultoras.domain.entity.Verificacion;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.AuthUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.CountryUseCase;
import biz.belcorp.consultoras.domain.interactor.FacebookUseCase;
import biz.belcorp.consultoras.domain.interactor.SessionUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.exception.ErrorFactory;
import biz.belcorp.consultoras.util.anotation.AuthType;

@PerActivity
public class LoginFacebookPresenter implements Presenter<LoginFacebookView> {

    private LoginFacebookView loginFacebookView;

    private final SessionUseCase sessionUseCase;
    private final FacebookUseCase facebookUseCase;
    private final CountryUseCase countryUseCase;
    private final AuthUseCase authUseCase;
    private final UserUseCase userUseCase;
    private final AccountUseCase accountUseCase;
    private final SessionModelDataMapper sessionModelDataMapper;
    private final CountryModelDataMapper countryModelDataMapper;
    private final CredentialsModelDataMapper credentialsModelDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;
    private final FacebookProfileModelDataMapper facebookProfileModelDataMapper;
    private final RemoteConfigDataMapper remoteConfigDataMapper;

    private CountryModel countryModel;
    private CredentialsModel credentialsModel;
    private FacebookProfileModel facebookProfileModel;
    private LoginModel loginModel;

    @Inject
    LoginFacebookPresenter(SessionUseCase sessionUseCase,
                           FacebookUseCase facebookUseCase,
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
        this.facebookUseCase = facebookUseCase;
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
    public void attachView(@NonNull LoginFacebookView view) {
        loginFacebookView = view;
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
        this.facebookUseCase.dispose();
        this.countryUseCase.dispose();
        this.authUseCase.dispose();
        this.userUseCase.dispose();
        this.loginFacebookView = null;
    }

    /**********************************************************/

    void data() {
        this.countryUseCase.find(new FindCountryByISO());
    }

    void loginWithFacebook(CredentialsModel credentials, FacebookProfileModel facebookProfile) {
        this.facebookProfileModel = facebookProfile;
        this.credentialsModel = credentials;

        this.loginFacebookView.showLoading();

        authUseCase.loginOnline(credentialsModelDataMapper.transform(credentials), new LoginRequest());
    }

    void getUsabilityConfig(Login login){
        userUseCase.getUsabilityConfig(new LoginFacebookPresenter.GetUsabilityConfigObserver(login));
    }

    /**********************************************************/

    private final class FindCountryByISO extends BaseObserver<Country> {
        @Override
        public void onNext(Country country) {
            countryModel = countryModelDataMapper.transform(country);
            facebookUseCase.get(new GetFacebook());
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (loginFacebookView == null) return;
            loginFacebookView.failed(ErrorFactory.INSTANCE.create(new Exception()));
        }
    }

    private final class GetFacebook extends BaseObserver<FacebookProfile> {

        @Override
        public void onNext(FacebookProfile facebookProfile) {
            if (loginFacebookView == null) return;
            loginFacebookView.renderData(countryModel,
                facebookProfileModelDataMapper.transform(facebookProfile));
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (loginFacebookView == null) return;
            loginFacebookView.failed(ErrorFactory.INSTANCE.create(new Exception()));
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
            if (loginFacebookView == null) return;
            loginFacebookView.hideLoading();
            loginFacebookView.failed(ErrorFactory.INSTANCE.create(exception));
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
            if (loginFacebookView == null) return;
            loginFacebookView.hideLoading();
            if (exception instanceof VersionException){
                VersionException vE = (VersionException) exception;
                loginFacebookView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            }
            else {
                loginFacebookView.failed(ErrorFactory.INSTANCE.create(exception));
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
            accountUseCase.verificacion(new VerificacionObserver(login));
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (loginFacebookView == null) return;
            loginFacebookView.hideLoading();
            loginFacebookView.failed(ErrorFactory.INSTANCE.create(exception));
        }
    }

    private final class VerificacionObserver extends BaseObserver<Verificacion>{

        private Login login;

        VerificacionObserver(Login login) {
            this.login = login;
        }

        @Override
        public void onNext(Verificacion verificacion) {
            if (loginFacebookView == null) return;
            loginFacebookView.hideLoading();
            loginFacebookView.success(login, loginModel, verificacion);
        }

    }

    private final class GetUsabilityConfigObserver extends BaseObserver<String> {

        private Login login;

        GetUsabilityConfigObserver(Login login) {
            this.login = login;
        }

        @Override
        public void onNext(String config) {
            loginFacebookView.setLogAccess(remoteConfigDataMapper.transform(config), login);
        }
    }
}

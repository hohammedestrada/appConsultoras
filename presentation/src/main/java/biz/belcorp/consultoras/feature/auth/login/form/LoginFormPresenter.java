package biz.belcorp.consultoras.feature.auth.login.form;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.CredentialsModel;
import biz.belcorp.consultoras.common.model.auth.CredentialsModelDataMapper;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.country.CountryModelDataMapper;
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
import biz.belcorp.consultoras.util.Constant;
import biz.belcorp.consultoras.util.anotation.CardType;
import biz.belcorp.library.net.exception.NetworkConnectionException;

@PerActivity
public class LoginFormPresenter implements Presenter<LoginFormView> {

    private LoginFormView loginFormView;

    private final SessionUseCase sessionUseCase;
    private final CountryUseCase countryUseCase;
    private final AuthUseCase authUseCase;
    private final UserUseCase userUseCase;
    private AccountUseCase accountUseCase;
    private final SessionModelDataMapper sessionModelDataMapper;
    private final CountryModelDataMapper countryModelDataMapper;
    private final CredentialsModelDataMapper credentialsModelDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;
    private final RemoteConfigDataMapper remoteConfigDataMapper;

    private CredentialsModel credentialsModel;
    private LoginModel loginModel;

    @Inject
    LoginFormPresenter(SessionUseCase sessionUseCase,
                       CountryUseCase countryUseCase,
                       AuthUseCase authUseCase,
                       UserUseCase userUseCase,
                       AccountUseCase accountUseCase,
                       SessionModelDataMapper sessionModelDataMapper,
                       CountryModelDataMapper countryModelDataMapper,
                       CredentialsModelDataMapper credentialsModelDataMapper,
                       LoginModelDataMapper loginModelDataMapper,
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
        this.remoteConfigDataMapper = remoteConfigDataMapper;
    }

    @Override
    public void attachView(@NonNull LoginFormView view) {
        loginFormView = view;
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
        this.credentialsModel = null;
        this.loginModel = null;
        this.sessionUseCase.dispose();
        this.countryUseCase.dispose();
        this.authUseCase.dispose();
        this.userUseCase.dispose();
        this.accountUseCase.dispose();
        this.loginFormView = null;
    }

    /**********************************************************/

    public void data() {
        this.sessionUseCase.getCountrySIM(new GetCountrySIM());
    }

    void loginWithForm(CredentialsModel credentials, boolean isOnline) {
        this.credentialsModel = credentials;

        loginFormView.showLoading();

        if (isOnline)
            authUseCase.loginOnline(credentialsModelDataMapper.transform(credentials),
                new LoginOnline());
        else
            authUseCase.loginOffline(credentialsModelDataMapper.transform(credentials),
                new LoginOffline());
    }

    public void verificacion(LoginModel model) {
        accountUseCase.verificacion(new VerificacionObserver(loginModel));
    }

    public void getUsabilityConfig(Login login){
        userUseCase.getUsabilityConfig(new LoginFormPresenter.GetUsabilityConfigObserver(login));
    }

    /**********************************************************/

    private final class GetCountrySIM extends BaseObserver<String> {
        @Override
        public void onNext(String iso) {
            countryUseCase.getByBrand(Constant.BRAND_FOCUS, new GetCountry(iso));
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (loginFormView == null) return;
            loginFormView.hideLoading();
            loginFormView.failedOnline(ErrorFactory.INSTANCE.create(exception));
        }
    }

    private final class GetCountry extends BaseObserver<List<Country>> {

        private final String iso;

        GetCountry(String iso) {
            this.iso = iso;
        }

        @Override
        public void onNext(List<Country> countries) {
            if (loginFormView == null) return;
            loginFormView.renderData(iso, countryModelDataMapper.transformToDataModel(countries));
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (loginFormView == null) return;
            loginFormView.hideLoading();
            loginFormView.failedOnline(ErrorFactory.INSTANCE.create(exception));
        }
    }

    private final class LoginOnline extends BaseObserver<Login> {

        @Override
        public void onNext(Login login) {
            loginModel = loginModelDataMapper.transform(login);
            userUseCase.getAndSaveInfo(credentialsModel.getPais(), login.getCampaing(),
                login.getRevistaDigitalSuscripcion(), new GetAndSaveMenu(login));
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (loginFormView == null) return;
            if (exception instanceof NetworkConnectionException) {
                authUseCase.loginOffline(credentialsModelDataMapper.transform(credentialsModel), new LoginOffline());
            } else {
                loginFormView.hideLoading();
                loginFormView.failedOnline(ErrorFactory.INSTANCE.create(exception));
            }
        }
    }

    private final class LoginOffline extends BaseObserver<Boolean> {
        @Override
        public void onNext(Boolean result) {
            if (result) {
                sessionUseCase.saveAuthenticated(true, new SaveAuthenticatedOffline());
            } else {
                if (loginFormView == null) return;
                loginFormView.hideLoading();
                loginFormView.failedOffline();
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (loginFormView == null) return;
            loginFormView.hideLoading();
            loginFormView.failedOffline();
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

            sessionModel.setAuthType(AuthType.FORM);
            sessionModel.setUsername(loginModel.getUserCode());
            sessionModel.setPassword(credentialsModel.getPassword());
            sessionModel.setEmail(loginModel.getEmail());
            sessionModel.setCountry(loginModel.getCountryISO());
            sessionModel.setTokenType(loginModel.getTokenType());
            sessionModel.setAccessToken(loginModel.getAccessToken());
            sessionModel.setRefreshToken(loginModel.getRefreshToken());
            sessionModel.setExpiresIn(loginModel.getExpiresIn());
            sessionModel.setIssued(loginModel.getIssued());
            sessionModel.setExpires(loginModel.getExpires());
            sessionModel.setTutorial(true);
            if(login.getDetail().get(CardType.ORDERS).getCount() != null){
                sessionModel.setOrdersCount(login.getDetail().get(CardType.ORDERS).getCount());
            }
            authUseCase.save(login, sessionModelDataMapper.transform(sessionModel),
                new SaveAuthenticatedOnline(login));
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (loginFormView == null) return;
            loginFormView.hideLoading();

            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                loginFormView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                loginFormView.failedOnline(ErrorFactory.INSTANCE.create(exception));
            }
        }
    }

    private final class SaveAuthenticatedOnline extends BaseObserver<Boolean> {

        private Login login;

        SaveAuthenticatedOnline(Login login) {
            this.login = login;
        }

        @Override
        public void onNext(Boolean result) {
            if (loginFormView == null) return;
            loginFormView.successOnline(login, loginModel);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (loginFormView == null) return;
            loginFormView.hideLoading();
            loginFormView.failedOnline(ErrorFactory.INSTANCE.create(exception));
        }
    }

    private final class SaveAuthenticatedOffline extends BaseObserver<Boolean> {
        @Override
        public void onNext(Boolean result) {
            if (loginFormView == null) return;
            loginFormView.hideLoading();
            if (result)
                loginFormView.successOffline();
            else
                loginFormView.failedOffline();
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (loginFormView == null) return;
            loginFormView.hideLoading();
            loginFormView.failedOffline();
        }
    }

    private final class VerificacionObserver extends BaseObserver<Verificacion>{

        private LoginModel loginModel;

        VerificacionObserver(LoginModel loginModel) {
            this.loginModel = loginModel;
        }

        @Override
        public void onNext(Verificacion verificacion) {
            loginFormView.onVerificacionResponse(verificacion, loginModel);
        }

        @Override
        public void onError(@NotNull Throwable exception) {
            super.onError(exception);
            loginFormView.hideLoading();
        }
    }

    private final class GetUsabilityConfigObserver extends BaseObserver<String> {

        private Login login;

        GetUsabilityConfigObserver(Login login) {
            this.login = login;
        }

        @Override
        public void onNext(String config) {
            loginFormView.setLogAccess(remoteConfigDataMapper.transform(config), login);
        }
    }




}

package biz.belcorp.consultoras.feature.auth.registration;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.AssociateModel;
import biz.belcorp.consultoras.common.model.auth.AssociateModelDataMapper;
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
import biz.belcorp.consultoras.util.Constant;

@PerActivity
public class RegistrationPresenter implements Presenter<RegistrationView> {

    private RegistrationView registrationView;

    private final SessionUseCase sessionUseCase;
    private final CountryUseCase countryUseCase;
    private final AuthUseCase authUseCase;
    private final UserUseCase userUseCase;
    private final AccountUseCase accountUseCase;
    private final SessionModelDataMapper sessionModelDataMapper;
    private final CountryModelDataMapper countryModelDataMapper;
    private final CredentialsModelDataMapper credentialsModelDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;
    private final AssociateModelDataMapper associateModelDataMapper;
    private final FacebookProfileModelDataMapper facebookProfileModelDataMapper;
    private final RemoteConfigDataMapper remoteConfigDataMapper;

    private CredentialsModel credentialsModel;
    private FacebookProfileModel facebookProfileModel;
    private LoginModel loginModel;

    @Inject
    RegistrationPresenter(SessionUseCase sessionUseCase,
                          CountryUseCase countryUseCase,
                          AuthUseCase authUseCase,
                          UserUseCase userUseCase,
                          AccountUseCase accountUseCase,
                          SessionModelDataMapper sessionModelDataMapper,
                          CountryModelDataMapper countryModelDataMapper,
                          CredentialsModelDataMapper credentialsModelDataMapper,
                          LoginModelDataMapper loginModelDataMapper,
                          AssociateModelDataMapper associateModelDataMapper,
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
        this.associateModelDataMapper = associateModelDataMapper;
        this.facebookProfileModelDataMapper = facebookProfileModelDataMapper;
        this.remoteConfigDataMapper = remoteConfigDataMapper;
    }

    @Override
    public void attachView(@NonNull RegistrationView view) {
        registrationView = view;
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
        this.accountUseCase.dispose();
        registrationView = null;
    }

    /**********************************************************/

    public void data() {
        this.sessionUseCase.getCountrySIM(new GetCountrySIM());
    }

    void associate(AssociateModel associate, CredentialsModel credentials, FacebookProfileModel facebookProfile) {
        registrationView.showLoading();
        credentialsModel = credentials;
        facebookProfileModel = facebookProfile;
        this.accountUseCase.associate(associateModelDataMapper.transform(associate), new AssociateRequest());
    }

    void getUsabilityConfig(Login login){
        userUseCase.getUsabilityConfig(new RegistrationPresenter.GetUsabilityConfigObserver(login));
    }

    /**********************************************************/

    private final class GetCountrySIM extends BaseObserver<String> {
        @Override
        public void onNext(String iso) {
            countryUseCase.getByBrand(Constant.BRAND_FOCUS, new GetCountry(iso));
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (registrationView == null) return;
            registrationView.hideLoading();
            registrationView.failed(ErrorFactory.INSTANCE.create(exception));
        }
    }

    private final class GetCountry extends BaseObserver<List<Country>> {

        private final String iso;

        GetCountry(String iso) {
            this.iso = iso;
        }

        @Override
        public void onNext(List<Country> countries) {
            if (registrationView == null) return;
            registrationView.renderData(iso, countryModelDataMapper.transformToDataModel(countries));
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (registrationView == null) return;
            registrationView.hideLoading();
            registrationView.failed(ErrorFactory.INSTANCE.create(exception));
        }
    }

    private final class AssociateRequest extends BaseObserver<String> {
        @Override
        public void onNext(String s) {
            authUseCase.loginOnline(credentialsModelDataMapper.transform(credentialsModel),
                new LoginRequest());
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (registrationView == null) return;
            registrationView.hideLoading();
            if (exception instanceof VersionException){
                VersionException vE = (VersionException) exception;
                registrationView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            }
            else {
                registrationView.failed(ErrorFactory.INSTANCE.create(exception));
            }

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
            if (registrationView == null) return;
            registrationView.hideLoading();
            if (exception instanceof VersionException){
                VersionException vE = (VersionException) exception;
                registrationView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            }
            else {
                registrationView.failed(ErrorFactory.INSTANCE.create(exception));
            }
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

            authUseCase.save(login,
                sessionModelDataMapper.transform(sessionModel),
                facebookProfileModelDataMapper.transform(facebookProfileModel),
                new SaveAuthenticated(login)
            );
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (registrationView == null) return;
            registrationView.hideLoading();
            if (exception instanceof VersionException){
                VersionException vE = (VersionException) exception;
                registrationView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            }
            else {
                registrationView.failed(ErrorFactory.INSTANCE.create(exception));
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
            if (registrationView == null) return;
            registrationView.hideLoading();
            registrationView.failed(ErrorFactory.INSTANCE.create(exception));
        }
    }

    private final class VerificacionObserver extends BaseObserver<Verificacion>{

        private Login login;

        VerificacionObserver(Login login) {
            this.login = login;
        }

        @Override
        public void onNext(Verificacion verificacion) {
            if (registrationView == null) return;
            registrationView.hideLoading();
            registrationView.success(login, loginModel, verificacion);
        }

    }

    private final class GetUsabilityConfigObserver extends BaseObserver<String> {

        private Login login;

        GetUsabilityConfigObserver(Login login) {
            this.login = login;
        }

        @Override
        public void onNext(String config) {
            registrationView.setLogAccess(remoteConfigDataMapper.transform(config), login);
        }
    }

}

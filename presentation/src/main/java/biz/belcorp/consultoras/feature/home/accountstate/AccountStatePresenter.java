package biz.belcorp.consultoras.feature.home.accountstate;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.accountState.AccountStateModel;
import biz.belcorp.consultoras.common.model.accountState.AccountStateModelDataMapper;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.AccountState;
import biz.belcorp.consultoras.domain.entity.Country;
import biz.belcorp.consultoras.domain.entity.Login;
import biz.belcorp.consultoras.domain.entity.LoginDetail;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.entity.UserConfigData;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.AccountStateUseCase;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.CountryUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.anotation.PagoEnLineaConfigCode;
import biz.belcorp.consultoras.util.anotation.UserConfigAccountCode;

@PerActivity
public class AccountStatePresenter implements Presenter<AccountStateView> {

    private AccountStateView accountStateView;
    private final AccountUseCase accountUseCase;
    private final UserUseCase userUseCase;
    private final CountryUseCase countryUseCase;
    private final AccountStateUseCase accountStatusUseCase;
    private final LoginModelDataMapper loginModelDataMapper;
    private final AccountStateModelDataMapper accountStateModelDataMapper;

    @Inject
    AccountStatePresenter(AccountUseCase accountUseCase, UserUseCase userUseCase,
                          CountryUseCase countryUseCase, AccountStateUseCase accountStatusUseCase,
                          LoginModelDataMapper loginModelDataMapper,
                          AccountStateModelDataMapper accountStateModelDataMapper) {
        this.accountUseCase = accountUseCase;
        this.userUseCase = userUseCase;
        this.countryUseCase = countryUseCase;
        this.accountStatusUseCase = accountStatusUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
        this.accountStateModelDataMapper = accountStateModelDataMapper;
    }

    @Override
    public void attachView(@NonNull AccountStateView view) {
        accountStateView = view;
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
        this.accountUseCase.dispose();
        this.userUseCase.dispose();
        this.countryUseCase.dispose();
        this.accountStatusUseCase.dispose();
        this.accountStateView = null;
    }

    /**********************************************************/

    public void data(boolean reload) {
        accountStateView.showLoading();
        if(reload){
            this.accountUseCase.refreshData(new BaseObserver<Login>(){
                @Override
                public void onNext(Login login) {
                    userUseCase.get(new GetUser());
                }
            });
        }else{
            this.userUseCase.get(new GetUser());
        }
    }

    public void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    public void goToPaidOnline() {
        this.userUseCase.get( new GetUserTrack());
    }

    public  void gotToMethodPay(){
        accountUseCase.getConfig(UserConfigAccountCode.PAYONLINE, new BaseObserver<Collection<UserConfigData>>(){
            @Override
            public void onNext(Collection<UserConfigData> userConfigData) {
                if(userConfigData!= null){
                    for (UserConfigData item :userConfigData) {
                        if(PagoEnLineaConfigCode.FLUJO.compareToIgnoreCase(item.getCode()) == 0 ){
                            accountStateView.gotToMethodPay(item);
                        }
                    }
                }
            }
        });
    }

    /**********************************************************/


    private final class GetUserTrack extends BaseObserver<User>{
        @Override
        public void onNext(User user) {
            if(user!=null)
                accountStateView.sendToPay(user);
        }
    }


    private final class GetUser extends BaseObserver<User> {

        @Override
        public void onNext(User user) {

            if (null == user) return;

            Login login = new Login();
            login.setAlias(user.getAlias());
            login.setConsultantCode(user.getConsultantCode());
            login.setCampaing(user.getCampaing());
            login.setPhotoProfile(user.getPhotoProfile());
            login.setClosingDays(user.getClosingDays());
            login.setEndTime(user.getEndTime());
            login.setBillingStartDate(user.getBillingStartDate());
            login.setDetail(user.getDetail());
            login.setCountryMoneySymbol(user.getCountryMoneySymbol());
            login.setCountryISO(user.getCountryISO());
            login.setCountryId(user.getCountryId());
            login.setRegionCode(user.getRegionCode());
            login.setRegionID(user.getRegionID());
            login.setZoneCode(user.getZoneCode());
            login.setZoneID(user.getZoneID());
            login.setAceptaTerminosCondiciones(user.isAceptaTerminosCondiciones());
            login.setBirthday(user.isBirthday());
            login.setAnniversary(user.isAnniversary());
            login.setConsultantName(user.getConsultantName());
            login.setCountryISO(user.getCountryISO());
            login.setDetail(user.getDetail());
            login.setPagoEnLinea(user.isPagoEnLinea());
            login.setCelularPendiente(user.getCelularPendiente());//REVISAR SI ES NECESARIO
            login.setCambioCelularPendiente(user.isCambioCelularPendiente());//REVISAR SI ES NECESARIO
            accountStateView.setData(login.getCountryISO(), login.getCountryMoneySymbol(), login.getAlias());

            if (login.getDetail() != null && login.getDetail().size() > 4) {
                LoginDetail estadoCuentaDetail = login.getDetail().get(5);
                accountStateView.showResumen(estadoCuentaDetail, login.isPagoEnLinea());
            }

            countryUseCase.find(login.getCountryISO(), new GetCountryUser());
            accountStatusUseCase.get(new GetAccountStatusMovement());
        }

        @Override
        public void onError(Throwable exception) {
            if (accountStateView == null) return;

            accountStateView.hideLoading();
            accountStateView.onError(exception);

        }
    }

    private final class GetCountryUser extends BaseObserver<Country> {

        @Override
        public void onNext(Country country) {
            if (null != country) {
                accountStateView.setShowDecimals(country.isShowDecimals() ? 1 : 0);
            }
        }

        @Override
        public void onError(Throwable exception) {
            if (accountStateView == null) return;
            accountStateView.hideLoading();
            accountStateView.onError(exception);
        }
    }

    private final class GetAccountStatusMovement extends BaseObserver<Collection<AccountState>> {

        @Override
        public void onNext(Collection<AccountState> accountStatusMovement) {
            if (accountStateView != null) {
                List<AccountStateModel> list = new ArrayList<>(accountStateModelDataMapper.transform(accountStatusMovement));
                accountStateView.showMovements(list);
                accountStateView.hideLoading();
            }
        }

        @Override
        public void onError(Throwable exception) {
            if (accountStateView == null) return;

            accountStateView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                accountStateView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                accountStateView.onError(exception);
            }
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            accountStateView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            accountStateView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }

}

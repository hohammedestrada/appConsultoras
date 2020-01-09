package biz.belcorp.consultoras.feature.payment;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.user.UserModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.ClientMovement;
import biz.belcorp.consultoras.domain.entity.Country;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.CountryUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.anotation.UserType;
import biz.belcorp.library.log.BelcorpLogger;

@PerActivity
public class PaymentPresenter implements Presenter<PaymentView> {

    private PaymentView paymentView;

    private final UserUseCase userUseCase;
    private final UserModelDataMapper userModelDataMapper;
    private final CountryUseCase countryUseCase;
    private final ClienteUseCase clientUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    public PaymentPresenter(UserUseCase userUseCase, UserModelDataMapper userModelDataMapper
        , CountryUseCase countryUseCase, ClienteUseCase clientUseCase, LoginModelDataMapper loginModelDataMapper) {

        this.userUseCase = userUseCase;
        this.userModelDataMapper = userModelDataMapper;
        this.countryUseCase = countryUseCase;
        this.clientUseCase = clientUseCase;

        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull PaymentView view) {
        paymentView = view;
    }

    @Override
    public void resume() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void pause() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destroy() {
        this.userUseCase.dispose();
        this.clientUseCase.dispose();
        this.countryUseCase.dispose();
        this.paymentView = null;
    }

    /**********************************************************/

    public void load() {
        this.userUseCase.get(new GetUser());
    }

    void addPayment(ClientMovement movement, LoginModel loginModel) {
        paymentView.showLoading();

        if (loginModel.getUserType() == UserType.CONSULTORA) {
            this.clientUseCase.saveMovementByClient(new AddPayment(), movement);
        } else {
            this.clientUseCase.saveTransactionOfflinePostulant(new AddPayment(), movement);
        }
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void initScreenTrackRegistrarPagoExitoso(ClientMovement result) {
        userUseCase.get(new UserPropertyRegistrarPagoExitosoObserver(result));
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    /**********************************************************/

    private final class GetUser extends BaseObserver<User> {
        @Override
        public void onNext(User user) {

            if (null != user) {
                String iso = user.getCountryISO();
                countryUseCase.find(iso, new GetCountryUser(user));
            }
        }
    }

    private final class GetCountryUser extends BaseObserver<Country> {

        private User user;

        private GetCountryUser(User user) {
            this.user = user;
        }

        @Override
        public void onNext(Country country) {

            if (null != country && null != paymentView) {
                user.setCountryShowDecimal(country.isShowDecimals() ? 1 : 0);
                paymentView.setData(userModelDataMapper.transform(user));
            }
        }
    }

    private final class AddPayment extends BaseObserver<ClientMovement> {

        @Override
        public void onNext(ClientMovement result) {
            if (null != paymentView) {
                initScreenTrackRegistrarPagoExitoso(result);
            }

        }

        @Override
        public void onError(Throwable exception) {
            if (null != paymentView) {
                paymentView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    paymentView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    super.onError(exception);
                }
            }
        }

        @Override
        public void onComplete() {
            super.onComplete();
            if (null != paymentView) paymentView.hideLoading();
        }
    }

    private final class UpdateObserver extends BaseObserver<Boolean> {
        @Override
        public void onNext(Boolean b) {
            super.onNext(b);
            BelcorpLogger.d("PaymentPresenter", "TarjetaPago registrado: " + b);
        }
    }

    private final class UserPropertyRegistrarPagoExitosoObserver extends BaseObserver<User> {

        private ClientMovement result;

        public UserPropertyRegistrarPagoExitosoObserver(ClientMovement result) {

            this.result = result;
        }

        @Override
        public void onNext(User user) {
            paymentView.initScreenTrackRegistrarPagoExitoso(loginModelDataMapper.transform(user));
            if (null != result) {

                paymentView.showResult(true);
                userUseCase.updateScheduler(new UpdateObserver());
            } else
                paymentView.showResult(false);
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            paymentView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            paymentView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }
}

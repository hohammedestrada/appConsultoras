package biz.belcorp.consultoras.feature.history.debt;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ClientMovementModel;
import biz.belcorp.consultoras.common.model.client.ClientMovementModelDataMapper;
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

/**
 *
 */
@PerActivity
public class DebtPresenter implements Presenter<DebtView> {

    private final ClienteUseCase clienteUseCase;
    private final UserUseCase userUseCase;
    private final CountryUseCase countryUseCase;
    private final ClientMovementModelDataMapper clientMovementModelDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;
    private final UserModelDataMapper userModelDataMapper;

    private DebtView debtView;

    @Inject
    DebtPresenter(ClienteUseCase clienteUseCase, UserUseCase userUseCase, CountryUseCase countryUseCase,
                  ClientMovementModelDataMapper clientMovementModelDataMapper,
                  LoginModelDataMapper loginModelDataMapper,
                  UserModelDataMapper userModelDataMapper) {
        this.clienteUseCase = clienteUseCase;
        this.userUseCase = userUseCase;
        this.countryUseCase = countryUseCase;
        this.clientMovementModelDataMapper = clientMovementModelDataMapper;
        this.loginModelDataMapper = loginModelDataMapper;
        this.userModelDataMapper = userModelDataMapper;
    }

    /**********************************************************/

    @Override
    public void attachView(@NonNull DebtView view) {
        this.debtView = view;
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
        debtView = null;
    }

    /**********************************************************/

    void getMovement(int movementLocalID) {
        debtView.showLoading();
        userUseCase.get(new UserObserver(movementLocalID));
    }

    void updateNote(ClientMovementModel clientMovementModel, LoginModel loginModel) {
        debtView.showLoading();

        if (loginModel.getUserType() == UserType.CONSULTORA) {
            clienteUseCase.updateMovementNote(clientMovementModelDataMapper.transform(clientMovementModel), new MovementUpdateObserver(clientMovementModel));
        } else {
            clienteUseCase.updateTransactionOfflinePostulant(clientMovementModelDataMapper.transform(clientMovementModel), new MovementUpdateObserver(clientMovementModel));
        }
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void initScreenTrackActualizarDeuda(ClientMovementModel clientMovementModel) {
        userUseCase.get(new UserPropertyActualizarDeudaObserver());
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }


    /**********************************************************/

    private class MovementObserver extends BaseObserver<ClientMovement> {
        @Override
        public void onNext(ClientMovement clientMovement) {
            debtView.showMovement(clientMovementModelDataMapper.transform(clientMovement));
            debtView.hideLoading();
        }

        @Override
        public void onError(Throwable exception) {
            BelcorpLogger.w("MovementObserver", exception);
            debtView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                debtView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                debtView.onError(exception);
            }
        }
    }

    private class MovementUpdateObserver extends BaseObserver<ClientMovement> {

        private ClientMovementModel clientMovementModel;

        MovementUpdateObserver(ClientMovementModel clientMovementModel) {
            this.clientMovementModel = clientMovementModel;
        }

        @Override
        public void onComplete() {
            if (debtView == null) return;
            debtView.onEditNote(clientMovementModel);
            userUseCase.updateScheduler(new UpdateObserver());
            debtView.hideLoading();
        }

        @Override
        public void onError(Throwable exception) {
            BelcorpLogger.w("MovementUpdateObserver", exception);
            if (debtView == null) return;
            debtView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                debtView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                debtView.onError(exception);
            }
        }
    }

    private final class UserObserver extends BaseObserver<User> {

        private int movementID;

        UserObserver(int movementID) {
            this.movementID = movementID;
        }

        @Override
        public void onNext(User user) {
            super.onNext(user);

            if (null != user) {
                String iso = user.getCountryISO();
                countryUseCase.find(iso, new GetCountryUser(user, movementID));
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            super.onError(exception);

            if (null != debtView) {
                debtView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    debtView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    debtView.onError(exception);
                }
            }
        }
    }

    private final class GetCountryUser extends BaseObserver<Country> {

        private User user;
        private int movementID;

        private GetCountryUser(User user, int movementID) {
            this.user = user;
            this.movementID = movementID;
        }

        @Override
        public void onNext(Country country) {
            if (null != debtView) {
                user.setCountryShowDecimal(country.isShowDecimals() ? 1 : 0);
                debtView.setData(userModelDataMapper.transform(user));
                clienteUseCase.getMovementOffline(new MovementObserver(), movementID);
            }
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            if (debtView != null)
                debtView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyActualizarDeudaObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            if (debtView == null) return;
            debtView.initScreenTrackActualizarDeuda(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (debtView != null)
                debtView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }

    private final class UpdateObserver extends BaseObserver<Boolean> { }
}

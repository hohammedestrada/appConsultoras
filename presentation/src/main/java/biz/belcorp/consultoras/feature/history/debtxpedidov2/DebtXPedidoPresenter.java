package biz.belcorp.consultoras.feature.history.debtxpedidov2;


import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ClientMovementModel;
import biz.belcorp.consultoras.common.model.client.ClientMovementModelDataMapper;
import biz.belcorp.consultoras.common.model.user.UserModelDataMapper;
import biz.belcorp.consultoras.common.tracking.Tracker;
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
public class DebtXPedidoPresenter implements Presenter<DebtXPedidoView> {

    private final ClienteUseCase clienteUseCase;
    private final UserUseCase userUseCase;
    private final CountryUseCase countryUseCase;
    private final ClientMovementModelDataMapper clientMovementModelDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;
    private final UserModelDataMapper userModelDataMapper;

    private DebtXPedidoView debtXPedidoView;

    @Inject
    DebtXPedidoPresenter(ClienteUseCase clienteUseCase, UserUseCase userUseCase, CountryUseCase countryUseCase,
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
    public void attachView(@NonNull DebtXPedidoView view) {
        this.debtXPedidoView = view;
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
        debtXPedidoView = null;
    }

    /**********************************************************/

    void getMovement(int movementLocalID) {
        debtXPedidoView.showLoading();
        userUseCase.get(new UserObserver(movementLocalID));
    }

    void updateProductos(ClientMovementModel clientMovementModel, LoginModel loginModel) {
        debtXPedidoView.showLoading();
//        clienteUseCase.updateProductos(
//        clienteUseCase.updateMovementNote(
//            clientMovementModelDataMapper.transform(clientMovementModel)
//            , new UpdateProductosObserver());

        if (loginModel.getUserType() == UserType.CONSULTORA) {
            clienteUseCase.updateMovementNote(clientMovementModelDataMapper.transform(clientMovementModel), new MovementUpdateObserver(clientMovementModel));
        } else {
            clienteUseCase.updateTransactionOfflinePostulant(clientMovementModelDataMapper.transform(clientMovementModel), new MovementUpdateObserver(clientMovementModel));
        }
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    public void trackEvent(String screenName,
                           String eventCat,
                           String eventAction,
                           String labelName,
                           String eventName) {
        userUseCase.get(new EventPropertyObserver(screenName, eventCat, eventAction, labelName, eventName));
    }

    /**********************************************************/

    private class MovementObserver extends BaseObserver<ClientMovement> {
        @Override
        public void onNext(ClientMovement clientMovement) {
            if (null == debtXPedidoView) return;
            debtXPedidoView.hideLoading();
            if (null != clientMovement)
                debtXPedidoView.showMovement(clientMovementModelDataMapper.transform(clientMovement));
        }

        @Override
        public void onError(Throwable exception) {
            BelcorpLogger.w("MovementObserver.onError", exception);
            if (null == debtXPedidoView) return;
            debtXPedidoView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                debtXPedidoView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                debtXPedidoView.onError(exception);
            }
        }
    }

    private final class UserObserver extends BaseObserver<User> {

        private int movementID;

        public UserObserver(int movementID) {
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
        public void onError(Throwable exception) {
            super.onError(exception);
            if (null == debtXPedidoView) return;
            debtXPedidoView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                debtXPedidoView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                debtXPedidoView.onError(exception);
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
            if (null == debtXPedidoView) return;
            user.setCountryShowDecimal(country.isShowDecimals() ? 1 : 0);
            debtXPedidoView.setData(userModelDataMapper.transform(user));
            clienteUseCase.getMovementOffline(new MovementObserver(), movementID);
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            if (null == debtXPedidoView) return;
            debtXPedidoView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == debtXPedidoView) return;
            debtXPedidoView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }

    private final class UpdateProductosObserver extends BaseObserver<Boolean> {

        @Override
        public void onNext(Boolean guardado) {
            debtXPedidoView.onProductosUpdated();
            debtXPedidoView.hideLoading();
        }

        @Override
        public void onError(Throwable exception) {
            debtXPedidoView.onError(exception);
        }
    }

    private class MovementUpdateObserver extends BaseObserver<ClientMovement> {

        private ClientMovementModel clientMovementModel;

        MovementUpdateObserver(ClientMovementModel clientMovementModel) {
            this.clientMovementModel = clientMovementModel;
        }

        @Override
        public void onNext(ClientMovement clientMovement) {
            if (null == debtXPedidoView) return;
            debtXPedidoView.hideLoading();
            userUseCase.updateScheduler(new UpdateObserver());
            if (null != clientMovement) debtXPedidoView.onEditNote(clientMovementModelDataMapper.transform(clientMovement));
        }

        @Override
        public void onError(Throwable exception) {
            BelcorpLogger.w("MovementUpdateObserver.onError", exception);
            if (debtXPedidoView == null) return;
            debtXPedidoView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                debtXPedidoView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                debtXPedidoView.onError(exception);
            }
        }
    }

    private final class EventPropertyObserver extends BaseObserver<User> {

        final String screenHome;
        final String eventCat;
        final String eventAction;
        final String eventLabel;
        final String eventName;

        private EventPropertyObserver(String screenHome,
                                      String eventCat,
                                      String eventAction,
                                      String eventLabel,
                                      String eventName) {
            this.screenHome = screenHome;
            this.eventCat = eventCat;
            this.eventAction = eventAction;
            this.eventLabel = eventLabel;
            this.eventName = eventName;
        }

        @Override
        public void onNext(User user) {
            Tracker.trackEvent(screenHome,
                eventCat,
                eventAction,
                eventLabel,
                eventName,
                loginModelDataMapper.transform(user));
        }
    }

    private final class UpdateObserver extends BaseObserver<Boolean> { }

}

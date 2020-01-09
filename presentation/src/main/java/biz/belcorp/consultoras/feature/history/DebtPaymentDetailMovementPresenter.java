package biz.belcorp.consultoras.feature.history;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ClientMovementModel;
import biz.belcorp.consultoras.common.model.client.ClientMovementModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.ClientMovement;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.library.log.BelcorpLogger;

/**
 *
 */
@PerActivity
public class DebtPaymentDetailMovementPresenter implements Presenter<DebtPaymentDetailMovementView> {

    private final ClienteUseCase clienteUseCase;
    private final UserUseCase userUseCase;
    private final ClientMovementModelDataMapper clientMovementModelDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;

    private DebtPaymentDetailMovementView movementView;
    private ClientMovementModel clientMovementModel;

    @Inject
    DebtPaymentDetailMovementPresenter(ClienteUseCase clienteUseCase, UserUseCase userUseCase, ClientMovementModelDataMapper clientMovementModelDataMapper, LoginModelDataMapper loginModelDataMapper) {
        this.clienteUseCase = clienteUseCase;
        this.userUseCase = userUseCase;
        this.clientMovementModelDataMapper = clientMovementModelDataMapper;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    /**********************************************************/

    @Override
    public void attachView(@NonNull DebtPaymentDetailMovementView view) {
        this.movementView = view;
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
        movementView = null;
    }

    /**********************************************************/

    void getMovement(int movementLocalID) {
        movementView.showLoading();
        userUseCase.get(new GetUser(movementLocalID));
    }

    void deleteNote(ClientMovementModel clientMovementModel) {

        if (TextUtils.isEmpty(clientMovementModel.getNote())) return;

        movementView.showLoading();
        clientMovementModel.setNote("");
        clienteUseCase.updateMovementNote(
            clientMovementModelDataMapper.transform(clientMovementModel)
            , new MovementUpdateObserver(clientMovementModel));
    }

    public void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    /**********************************************************/

    private class MovementObserver extends BaseObserver<ClientMovement> {
        @Override
        public void onNext(ClientMovement clientMovement) {
            movementView.showMovement(clientMovementModelDataMapper.transform(clientMovement));
            movementView.hideLoading();
        }

        @Override
        public void onError(Throwable exception) {
            BelcorpLogger.w("onError", exception);
            movementView.hideLoading();
            if (exception instanceof VersionException){
                VersionException vE = (VersionException) exception;
                movementView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                movementView.onError(exception);
            }
        }
    }

    private class MovementUpdateObserver extends BaseObserver<ClientMovement> {

        private ClientMovementModel clientMovementModel;

        public MovementUpdateObserver(ClientMovementModel clientMovementModel) {
            this.clientMovementModel = clientMovementModel;
        }

        @Override
        public void onComplete() {
            if (movementView == null) return;
            movementView.onEditNote(clientMovementModel);
            movementView.hideLoading();
        }

        @Override
        public void onError(Throwable exception) {
            BelcorpLogger.w("onError", exception);
            if (movementView == null) return;
            movementView.hideLoading();
            if (exception instanceof VersionException){
                VersionException vE = (VersionException) exception;
                movementView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                movementView.onError(exception);
            }
        }
    }

    private final class GetUser extends BaseObserver<User> {

        String symbol = "S/.";
        private int movementID;

        public GetUser(int movementID) {
            this.movementID = movementID;
        }

        @Override
        public void onNext(User user) {
            if (null != user) {
                symbol = user.getCountryMoneySymbol();
                String iso = user.getCountryISO();

                if (movementView != null)
                    movementView.setData(iso, symbol);
                clienteUseCase.getMovementOffline(new MovementObserver(), movementID);
            }
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            movementView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            movementView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }
}

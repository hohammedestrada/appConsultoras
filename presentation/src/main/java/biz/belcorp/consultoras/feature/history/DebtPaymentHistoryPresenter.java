package biz.belcorp.consultoras.feature.history;

import android.support.annotation.NonNull;

import java.util.Collection;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ClientMovementModel;
import biz.belcorp.consultoras.common.model.client.ClientMovementModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.ClientMovement;
import biz.belcorp.consultoras.domain.entity.Cliente;
import biz.belcorp.consultoras.domain.entity.Country;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.CountryUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.exception.DebtNotPaidException;
import biz.belcorp.consultoras.util.anotation.StatusType;
import biz.belcorp.consultoras.util.anotation.UserType;

@PerActivity
public class DebtPaymentHistoryPresenter implements Presenter<DebtPaymentHistoryView> {

    private DebtPaymentHistoryView historyView;

    private final UserUseCase userUseCase;
    private final CountryUseCase countryUseCase;
    private final ClienteUseCase clientUseCase;
    private final ClientMovementModelDataMapper clientMovementModelDataMapper;
    private final ClienteModelDataMapper clientModelDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    DebtPaymentHistoryPresenter(UserUseCase userUseCase, CountryUseCase countryUseCase
        , ClienteUseCase clientUseCase, ClientMovementModelDataMapper clientMovementModelDataMapper, ClienteModelDataMapper clientModelDataMapper, LoginModelDataMapper loginModelDataMapper) {

        this.userUseCase = userUseCase;
        this.countryUseCase = countryUseCase;
        this.clientUseCase = clientUseCase;
        this.clientMovementModelDataMapper = clientMovementModelDataMapper;
        this.clientModelDataMapper = clientModelDataMapper;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull DebtPaymentHistoryView view) {
        historyView = view;
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
        this.historyView = null;
    }

    /**********************************************************/

    public void load(Integer clienteId, Integer clienteLocalId, boolean recordatory) {
        historyView.showLoading();
        this.userUseCase.get(new GetUser(clienteId, clienteLocalId, recordatory));
        this.clientUseCase.findClienteById(clienteLocalId, new GetClientObserver());
    }

    public void delete(ClientMovementModel clientMovementModel) {
        historyView.showLoading();
        clientMovementModel.setEstado(StatusType.DELETE);
        this.clientUseCase.deleteMovement(clientMovementModelDataMapper.transform(clientMovementModel)
            , new DeleteMovement());
    }

    void deleteOffline(ClientMovementModel clientMovementModel) {
        historyView.showLoading();
        clientMovementModel.setEstado(StatusType.DELETE);
        this.clientUseCase.deleteMovementOffline(clientMovementModelDataMapper.transform(clientMovementModel)
            , new DeleteMovement());
    }

    void eliminarRecordatory(int recordatorioId, int clienteId, int clienteLocalID) {
        historyView.showLoading();
        clientUseCase.deleteRecordatory(recordatorioId, clienteId, clienteLocalID, new EliminarRecordatory());
    }

    public void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void initScreenTrackAnadirRecordatorio() {
        userUseCase.get(new UserPropertyAnadirRecordatorioObserver());
    }

    void initScreenTrackAnadirDeuda() {
        userUseCase.get(new UserPropertyAnadirDeudaObserver());
    }

    void initScreenTrackRegistrarUnPago() {
        userUseCase.get(new UserPropertyRegistrarUnPagoObserver());
    }

    void initScreenTrackEnviarDetalleDeuda() {
        userUseCase.get(new UserPropertyEnviarDetalleDeudaObserver());
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

    private final class GetUser extends BaseObserver<User> {

        private final Integer clienteId;
        private final Integer clienteLocalId;
        private Boolean recordatorio;
        String symbol = "S/.";

        public GetUser(Integer clienteId, Integer clienteLocalId, Boolean recordatorio) {
            this.clienteId = clienteId;
            this.clienteLocalId = clienteLocalId;
            this.recordatorio = recordatorio;
        }

        @Override
        public void onNext(User user) {

            if (null == user) return;

            symbol = user.getCountryMoneySymbol();
            String iso = user.getCountryISO();
            historyView.setData(iso, symbol);
            countryUseCase.find(iso, new GetCountryUser(user, clienteId, clienteLocalId, recordatorio));
        }

        @Override
        public void onError(Throwable exception) {
            if (historyView == null) return;

            historyView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                historyView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                historyView.onError(exception);
            }
        }
    }

    private final class GetCountryUser extends BaseObserver<Country> {

        private User user;
        private final Integer clienteId;
        private final Integer clienteLocalId;
        private Boolean recordatorio;

        public GetCountryUser(User user, Integer clienteId, Integer clienteLocalId, Boolean recordatorio) {
            this.user = user;
            this.clienteId = clienteId;
            this.clienteLocalId = clienteLocalId;
            this.recordatorio = recordatorio;
        }

        @Override
        public void onNext(Country country) {
            if (null != country) {
                historyView.setShowDecimals(country.isShowDecimals() ? 1 : 0);

                if (user.getUserType() == UserType.CONSULTORA) {
                    clientUseCase.getMovementsByClient(new GetUserMovements(recordatorio), clienteId, clienteLocalId);
                } else {
                    clientUseCase.getTransactionsClientOfflinePostulant(new GetUserMovements(recordatorio), clienteLocalId);
                }
            }
        }

        @Override
        public void onError(Throwable exception) {
            if (historyView == null) return;

            historyView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                historyView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                historyView.onError(exception);
            }
        }
    }

    private final class GetUserMovements extends BaseObserver<Collection<ClientMovement>> {

        boolean recordatory;

        GetUserMovements(boolean recordatory) {
            this.recordatory = recordatory;
        }

        @Override
        public void onNext(Collection<ClientMovement> list) {

            if (historyView == null) return;
            if (null != list) {
                historyView.showHistory(clientMovementModelDataMapper.transform(list), recordatory);
                historyView.hideLoading();
            }
        }

        @Override
        public void onError(Throwable exception) {
            super.onError(exception);

            if (historyView == null) return;

            historyView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                historyView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                historyView.onError(exception);
            }
        }
    }

    private final class DeleteMovement extends BaseObserver<Boolean> {

        @Override
        public void onNext(Boolean eliminado) {

            if (eliminado) {
                if (historyView != null) {
                    historyView.hideLoading();
                    historyView.refresh();
                }
                userUseCase.updateScheduler(new UpdateObserver());
            } else {
                onError(new DebtNotPaidException());
            }
        }

        @Override
        public void onError(Throwable exception) {
            super.onError(exception);

            if (historyView == null) return;

            historyView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                historyView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                historyView.onError(exception);
            }
        }
    }

    private final class EliminarRecordatory extends BaseObserver<Boolean> {

        @Override
        public void onComplete() {
            if (null == historyView) return;
            historyView.hideLoading();
            historyView.deleteRecordatory();
        }

        @Override
        public void onError(Throwable exception) {
            if (null == historyView) return;
            historyView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                historyView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                historyView.onError(exception);
            }
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            historyView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyAnadirRecordatorioObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            historyView.initScreenTrackAnadirRecordatorio(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyAnadirDeudaObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            historyView.initScreenTrackAnadirDeuda(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyRegistrarUnPagoObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            historyView.initScreenTrackRegistrarUnPago(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyEnviarDetalleDeudaObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            historyView.initScreenTrackEnviarDetalleDeuda(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            historyView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }

    private final class GetClientObserver extends BaseObserver<Cliente> {
        @Override
        public void onNext(Cliente client) {
            if (null != historyView)
                historyView.showRecordatorio(clientModelDataMapper.transform(client));
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

    private final class UpdateObserver extends BaseObserver<Boolean> {
    }

}

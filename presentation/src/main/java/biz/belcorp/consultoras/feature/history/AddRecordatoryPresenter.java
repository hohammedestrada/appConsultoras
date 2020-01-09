package biz.belcorp.consultoras.feature.history;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.recordatorio.RecordatorioModel;
import biz.belcorp.consultoras.common.model.recordatorio.RecordatorioModelDataMapper;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Recordatorio;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.anotation.UserType;

/**
 *
 */
@PerActivity
class AddRecordatoryPresenter implements Presenter<AddRecordatoryView> {

    private AddRecordatoryView addRecordatoryView;
    private final UserUseCase userUseCase;
    private final ClienteUseCase clienteUseCase;
    private final LoginModelDataMapper loginModelDataMapper;
    private final RecordatorioModelDataMapper recordatorioModelDataMapper;

    @Inject
    AddRecordatoryPresenter(UserUseCase userUseCase, ClienteUseCase clienteUseCase, LoginModelDataMapper loginModelDataMapper, RecordatorioModelDataMapper recordatorioModelDataMapper) {
        this.userUseCase = userUseCase;
        this.clienteUseCase = clienteUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
        this.recordatorioModelDataMapper = recordatorioModelDataMapper;
    }

    /**********************************************************/

    @Override
    public void attachView(@NonNull AddRecordatoryView view) {
        addRecordatoryView = view;
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
        this.addRecordatoryView = null;
    }

    /**********************************************************/

    void trackScreen() {
        userUseCase.get(new UserPropertyObserver());
    }

    void saveRecordatory(RecordatorioModel recordatorioModel, LoginModel loginModel, int clienteId) {
        addRecordatoryView.showLoading();
        recordatorioModel.setClienteLocalID(clienteId);

        if (loginModel.getUserType() == UserType.CONSULTORA) {
            clienteUseCase.saveRecordatory(recordatorioModelDataMapper.transform(recordatorioModel), new SaveRecordatory());
        } else {
            clienteUseCase.saveRecordatoryOfflinePostulant(recordatorioModelDataMapper.transform(recordatorioModel), new SaveRecordatory());
        }
    }

    void updateRecordatory(RecordatorioModel recordatorioModel, LoginModel loginModel, int clienteId) {
        addRecordatoryView.showLoading();
        recordatorioModel.setClienteLocalID(clienteId);

        if (loginModel.getUserType() == UserType.CONSULTORA) {
            clienteUseCase.updateRecordatory(recordatorioModelDataMapper.transform(recordatorioModel), new UpdateRecordatory());
        } else {
            clienteUseCase.updateRecordatoryOfflinePostulant(recordatorioModelDataMapper.transform(recordatorioModel), new UpdateRecordatory());
        }
    }

    void eliminarRecordatory(int recordatorioId, int clienteId, int clienteLocalID) {
        addRecordatoryView.showLoading();
        clienteUseCase.deleteRecordatory(recordatorioId, clienteId, clienteLocalID, new EliminarRecordatory());
    }

    public void trackEvent(String screenName,
                           String eventCat,
                           String eventAction,
                           String labelName,
                           String eventName) {
        userUseCase.get(new EventPropertyObserver(screenName, eventCat, eventAction, labelName, eventName));
    }

    private final class SaveRecordatory extends BaseObserver<Recordatorio> {

        @Override
        public void onComplete() {
            if (null == addRecordatoryView) return;
            addRecordatoryView.saveData();
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == addRecordatoryView) return;
            addRecordatoryView.hideLoading();
            addRecordatoryView.onError(exception);
        }
    }

    private final class UpdateRecordatory extends BaseObserver<Boolean> {

        @Override
        public void onComplete() {
            if (null == addRecordatoryView) return;
            addRecordatoryView.saveData();
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == addRecordatoryView) return;
            addRecordatoryView.hideLoading();
            addRecordatoryView.onError(exception);
        }
    }

    private final class EliminarRecordatory extends BaseObserver<Boolean> {

        @Override
        public void onComplete() {
            if (null == addRecordatoryView) return;
            addRecordatoryView.deleteData();
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == addRecordatoryView) return;
            addRecordatoryView.hideLoading();
            addRecordatoryView.onError(exception);
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            addRecordatoryView.initScreenTrack(loginModelDataMapper.transform(user));
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
}

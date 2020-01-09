package biz.belcorp.consultoras.feature.client.note;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.client.AnotacionModel;
import biz.belcorp.consultoras.common.model.client.AnotacionModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Anotacion;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.GenericException;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.NoteUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.library.log.BelcorpLogger;

@PerActivity
public class NotePresenter implements Presenter<NoteView> {


    public static final String TAG = "NotePresenter";
    private static final String ERROR = "Error";
    private NoteView noteView;

    private final ClienteUseCase clienteUseCase;
    private final UserUseCase userUseCase;
    private final NoteUseCase noteUseCase;
    private final AnotacionModelDataMapper anotacionModelDataMapper;
    private LoginModelDataMapper loginModelDataMapper;

    @Inject
    NotePresenter(ClienteUseCase clienteUseCase, UserUseCase userUseCase,
                         NoteUseCase noteUseCase, AnotacionModelDataMapper anotacionModelDataMapper,
                         LoginModelDataMapper loginModelDataMapper) {
        this.clienteUseCase = clienteUseCase;
        this.userUseCase = userUseCase;
        this.noteUseCase = noteUseCase;
        this.anotacionModelDataMapper = anotacionModelDataMapper;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull NoteView view) {
        noteView = view;
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
        this.userUseCase.dispose();
        this.clienteUseCase.dispose();
        this.noteView = null;
    }

    /**********************************************************/

    void getAnotation(Integer id) {
        noteView.showLoading();
        noteUseCase.getById(id, new GetAnotationObserver());
    }

    void saveAnotacion(String countryISO, AnotacionModel anotacionModel) {
        noteUseCase.countByClientLocalID(countryISO, anotacionModel.getClienteLocalID(),
            new ValidateAnotationObserver(anotacionModel));
    }

    void updateAnotacion(AnotacionModel anotacionModel) {
        noteView.showLoading();
        clienteUseCase.updateAnotacion(anotacionModelDataMapper.transform(anotacionModel), new UpdateAnotacionObserver(anotacionModel));
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    public void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    /**********************************************************/

    private final class GetAnotationObserver extends BaseObserver<Anotacion> {

        @Override
        public void onNext(Anotacion anotacion) {
            if (null == noteView) return;
            noteView.show(anotacionModelDataMapper.transform(anotacion));
        }

        @Override
        public void onError(Throwable exception) {
            if (null == noteView) return;
            noteView.hideLoading();
            noteView.show(null);
        }

        @Override
        public void onComplete() {
            if (null == noteView) return;
            noteView.hideLoading();
        }
    }

    private final class ValidateAnotationObserver extends BaseObserver<Boolean> {
        AnotacionModel anotacionModel;

        private ValidateAnotationObserver(AnotacionModel anotacionModel) {
            this.anotacionModel = anotacionModel;
        }

        @Override
        public void onNext(Boolean result) {
            if (noteView == null) return;

            if (result) {
                noteView.showLoading();
                clienteUseCase.saveAnotacion(anotacionModelDataMapper.transform(anotacionModel), new SaveAnotacionObserver());
            } else {
                Throwable exception = new GenericException("El cliente superó el máximo de notas creadas.");
                noteView.onError(exception);
            }
        }

        @Override
        public void onError(Throwable exception) {
            if (noteView == null) return;
            noteView.onError(exception);
            BelcorpLogger.w(ERROR, exception);
        }
    }

    private final class SaveAnotacionObserver extends BaseObserver<Anotacion> {

        @Override
        public void onNext(Anotacion result) {
            if (noteView != null) {
                noteView.hideLoading();
                noteView.anotacionSaved(anotacionModelDataMapper.transform(result));
            }
        }

        @Override
        public void onError(Throwable exception) {
            BelcorpLogger.w(ERROR, exception);
            if (noteView != null) {
                noteView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    noteView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    noteView.onError(exception);
                }
            }
        }
    }

    private final class UpdateAnotacionObserver extends BaseObserver<Boolean> {

        AnotacionModel anotacionModel;

        private UpdateAnotacionObserver(AnotacionModel anotacionModel) {
            this.anotacionModel = anotacionModel;
        }

        @Override
        public void onNext(Boolean result) {
            if (noteView != null) {
                noteView.hideLoading();
                noteView.anotacionUpdated(anotacionModel);
            }
        }

        @Override
        public void onError(Throwable exception) {
            if (noteView != null) {
                noteView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    noteView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    noteView.onError(exception);
                }
            }
            BelcorpLogger.w(ERROR, exception);
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            noteView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            noteView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }
}

package biz.belcorp.consultoras.common.notification.home.postulant;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.anotation.UserType;

/**
 *
 */
class PostulantNotificationPresenter implements Presenter<PostulantNotificationView> {

    private UserUseCase userUseCase;
    private PostulantNotificationView notificationView;

    @Inject
    PostulantNotificationPresenter(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    /**********************************************************************************************/

    @Override
    public void attachView(@NonNull PostulantNotificationView view) {
        this.notificationView = view;
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

        this.notificationView = null;
    }

    /**********************************************************************************************/

    void get() {
        userUseCase.get(new UserObserver());
    }

    /**********************************************************************************************/

    private class UserObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (user.getUserType() == UserType.POSTULANTE) {
                notificationView.show();
            } else {
                notificationView.hide();
            }
        }

        @Override
        public void onError(Throwable exception) {
            notificationView.hide();
        }
    }
}

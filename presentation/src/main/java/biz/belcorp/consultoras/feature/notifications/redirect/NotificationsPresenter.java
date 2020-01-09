package biz.belcorp.consultoras.feature.notifications.redirect;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.exception.ErrorFactory;
import biz.belcorp.library.notification.TipoIngreso;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Auth;
import biz.belcorp.consultoras.domain.entity.HybrisData;
import biz.belcorp.consultoras.domain.entity.Menu;
import biz.belcorp.consultoras.domain.interactor.AuthUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.MenuUseCase;
import biz.belcorp.consultoras.domain.interactor.NotificacionUseCase;
import biz.belcorp.consultoras.domain.interactor.SessionUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.anotation.MenuCodeTop;

@PerActivity
public class NotificationsPresenter implements Presenter<NotificationsView> {

    private NotificationsView notificationsView;

    private final AuthUseCase authUseCase;
    private final UserUseCase userUseCase;
    private final MenuUseCase menuUseCase;
    private final SessionUseCase sessionUseCase;
    private final NotificacionUseCase notificacionUseCase;

    @Inject
    NotificationsPresenter(AuthUseCase authUseCase, UserUseCase userUseCase,
                           SessionUseCase sessionUseCase, MenuUseCase menuUseCase, NotificacionUseCase notificacionUseCase) {
        this.authUseCase = authUseCase;
        this.userUseCase = userUseCase;
        this.sessionUseCase = sessionUseCase;
        this.menuUseCase = menuUseCase;
        this.notificacionUseCase = notificacionUseCase;
    }

    @Override
    public void attachView(@NonNull NotificationsView view) {
        notificationsView = view;
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
        this.notificationsView = null;
    }

    /**********************************************************/

    void init(boolean resetAnim, boolean isOrder) {
        if (resetAnim)
            sessionUseCase.cleanBelcorp(new CleanBelcorpObserver(isOrder));
        else if (isOrder)
            menuUseCase.getActive(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE, new GetMenuObserver());
        else
            authUseCase.data(new DataObserver(0));
    }

    void saveHybrisData(HybrisData hybrisData) {
        this.userUseCase.saveHybrisData(hybrisData, new SaveHybrisDataObserver());
    }

    void updateNotificacion(int idNotificacion, int status) {
        notificacionUseCase.updateEstadoByNotificationId(idNotificacion, status, new GetNotificacionesObserver());
    }

    void updateSchedule(){
        authUseCase.updateScheduler(new UpdateScheduler());
    }

    /**********************************************************/

    private final class UpdateScheduler extends BaseObserver<Boolean> { }

    private final class UpdateNotificationStatus extends BaseObserver<Boolean> { }

    private final class GetNotificacionesObserver extends BaseObserver<Boolean> {
        @Override
        public void onNext(Boolean status) {
            if (status) sessionUseCase.updateNotificationStatus(false, new UpdateNotificationStatus());
        }
    }

    private final class DataObserver extends BaseObserver<Auth> {
        int option;

        DataObserver(int option){
            this.option = option;
        }

        @Override
        public void onNext(Auth auth) {
            userUseCase.updateTipoIngreso(TipoIngreso.PUSH_NOTIFICATION
                , new UpdatedTipoIngresoObserver(option, auth));
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == notificationsView) return;
            notificationsView.initScreen(null, 0);
        }
    }

    private final class UpdatedTipoIngresoObserver extends BaseObserver<Boolean> {
        int option;
        private Auth auth;

        UpdatedTipoIngresoObserver(int option, Auth auth){
            this.option = option;
            this.auth = auth;
        }

        @Override
        public void onNext(Boolean b) {
            if (null == notificationsView) return;

            if (null != auth) notificationsView.initScreen(auth, option);
            else notificationsView.initScreen(null, option);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == notificationsView) return;
            notificationsView.initScreen(null, 0);
        }
    }

    private final class SaveHybrisDataObserver extends BaseObserver<Boolean> { }

    private final class CleanBelcorpObserver extends BaseObserver<Boolean> {

        boolean isOrder;

        CleanBelcorpObserver(boolean isOrder){
            this.isOrder = isOrder;
        }

        @Override
        public void onNext(Boolean aBoolean) {
            authUseCase.data(new DataObserver(0));
        }
    }

    private final class GetMenuObserver extends BaseObserver<Menu> {

        @Override
        public void onNext(Menu menu) {
            if (menu != null) {
                if (menu.isVisible() && menu.getCodigo().equals(MenuCodeTop.ORDERS))
                    authUseCase.data(new DataObserver(1));
                else if (menu.isVisible() && menu.getCodigo().equals(MenuCodeTop.ORDERS_NATIVE))
                    authUseCase.data(new DataObserver(2));
            }
        }

        @Override
        public void onError(@NotNull Throwable exception) {
            authUseCase.data(new DataObserver(0));
        }
    }

}

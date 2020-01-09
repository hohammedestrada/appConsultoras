package biz.belcorp.consultoras.common.notification.home;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.menu.MenuModelDataMapper;
import biz.belcorp.consultoras.domain.entity.Menu;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.MenuUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;

/**
 *
 */
class OrderNotificationPresenter implements Presenter<OrderNotificationView> {

    private UserUseCase userUseCase;
    private final MenuUseCase menuUseCase;
    private final MenuModelDataMapper menuModelDataMapper;

    private OrderNotificationView notificationView;

    @Inject
    OrderNotificationPresenter(UserUseCase userUseCase, MenuUseCase menuUseCase,
                               MenuModelDataMapper menuModelDataMapper) {
        this.userUseCase = userUseCase;
        this.menuUseCase = menuUseCase;
        this.menuModelDataMapper = menuModelDataMapper;
    }

    /**********************************************************************************************/

    @Override
    public void attachView(@NonNull OrderNotificationView view) {
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

    void getMenuActive(String code1, String code2) {
        menuUseCase.getActive(code1, code2, new GetMenuObserver());
    }

    /**********************************************************************************************/

    private class UserObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == notificationView) return;
            if (user.isShowBanner()) {
                notificationView.setNotificationState(user.getBannerUrl());
                notificationView.setNotificationTitle(user.getBannerTitle());
                notificationView.setNotificationDetail(user.getBannerMessage());
                notificationView.setNotificationURL(user.getBannerVinculo());
            } else {
                notificationView.hide();
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == notificationView) return;
            notificationView.hide();
        }
    }

    private final class GetMenuObserver extends BaseObserver<Menu> {

        @Override
        public void onNext(Menu menu) {
            if (null == notificationView) return;
            notificationView.showMenuOrder(menuModelDataMapper.transform(menu));
        }
    }
}

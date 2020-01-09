package biz.belcorp.consultoras.common.notification.home;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.menu.MenuModel;

/**
 *
 */
interface OrderNotificationView extends View {

    void setNotificationState(String notificationState);

    void setNotificationDetail(String description);

    void setNotificationTitle(String title);

    void setNotificationURL(String url);

    void showMenuOrder(MenuModel menu);

    void show();

    void hide();
}

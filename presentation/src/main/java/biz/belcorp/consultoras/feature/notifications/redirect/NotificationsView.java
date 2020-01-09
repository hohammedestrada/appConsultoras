package biz.belcorp.consultoras.feature.notifications.redirect;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.domain.entity.Auth;

interface NotificationsView extends View {

    void initScreen(Auth auth, int optionOrder);

}

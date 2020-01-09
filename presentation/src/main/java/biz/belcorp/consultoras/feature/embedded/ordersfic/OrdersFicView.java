package biz.belcorp.consultoras.feature.embedded.ordersfic;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.domain.entity.Menu;

interface OrdersFicView extends View, LoadingView {

    void initScreenTrack(LoginModel model);

    void trackBack(LoginModel model);

    void showSearchOption();

    void showUrl(String url);

    void showError();

    void onGetMenu(Menu menu);
}

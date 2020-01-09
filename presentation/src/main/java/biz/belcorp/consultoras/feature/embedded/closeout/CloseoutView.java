package biz.belcorp.consultoras.feature.embedded.closeout;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.domain.entity.Menu;

interface CloseoutView extends View, LoadingView {

    void setMenuTitle(String title);

    void initScreenTrack(LoginModel transform);

    void trackBack(LoginModel transform);

    void showSearchOption();

    void showUrl(String url);

    void showError();

    void onGetMenu(Menu menu);

}

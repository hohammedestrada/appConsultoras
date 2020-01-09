package biz.belcorp.consultoras.feature.embedded.offers;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.domain.entity.Menu;

interface OffersView extends View, LoadingView {

    void setTitle(String title);

    void initScreenTrack(LoginModel model);

    void trackBack(LoginModel model);

    void showSearchOption();

    void showUrl(String url);

    void showError();

    void onGetMenu(Menu menu);

    void onGanaMasFinish();
}

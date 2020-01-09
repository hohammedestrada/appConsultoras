package biz.belcorp.consultoras.feature.client.order;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface ClientOrderView extends View, LoadingView {

    void showPostulant();

    void showUrl(String url);

    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);
}

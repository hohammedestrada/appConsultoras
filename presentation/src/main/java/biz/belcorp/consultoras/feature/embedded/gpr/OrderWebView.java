package biz.belcorp.consultoras.feature.embedded.gpr;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.view.LoadingView;

/**
 *
 */
interface OrderWebView extends View, LoadingView {

    void showPostulant();

    void showUrl(String url);

    void showError();

    void setCampaign(String campaign);

    void initScreenTrack(LoginModel model);

    void trackBack(LoginModel model);

    void showSearchOption();
}

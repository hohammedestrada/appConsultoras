package biz.belcorp.consultoras.feature.client.order.history;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.domain.entity.Menu;

interface ClientOrderHistoryView extends View, LoadingView {

    /**
     * Muestra una url.
     */
    void showURL(String url);

    void showError(Throwable throwable);

    void showError();

    void initScreenTrack(LoginModel transform);

    void trackBack(LoginModel transform);

    void onGetMenu(Menu menu);
}

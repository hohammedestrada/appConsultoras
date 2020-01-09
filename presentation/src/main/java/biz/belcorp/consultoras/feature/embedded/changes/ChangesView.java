package biz.belcorp.consultoras.feature.embedded.changes;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface ChangesView extends View, LoadingView {

    /**
     * Muestra una url.
     */
    void showUrl(String url);

    void showError();

    void initScreenTrack(LoginModel transform);

    void trackBack(LoginModel transform);
}

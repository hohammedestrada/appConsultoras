package biz.belcorp.consultoras.feature.terms;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.view.LoadingView;

/**
 * @author andres.escobar on 4/08/2017.
 */
interface TermsView extends View, LoadingView {

    void onTermsAccepted();

    void onError(Throwable exception);

    void onUrlTermsGot(String urlTerminos);

    void initScreenTrack(LoginModel transform);

    void checkSDKPermission();
}

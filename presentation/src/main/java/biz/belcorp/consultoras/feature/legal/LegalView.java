package biz.belcorp.consultoras.feature.legal;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface LegalView extends View, LoadingView {

    void initScreenTrack(LoginModel model);

    void trackBackPressed(LoginModel model);

    void setData(LoginModel model);

    void onPrivacyAccepted();

    void onError(Throwable exception);

    void onUrlLegalGot(String url);

    void stopSDK();

}

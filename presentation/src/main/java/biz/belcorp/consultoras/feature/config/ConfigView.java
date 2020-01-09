package biz.belcorp.consultoras.feature.config;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface ConfigView extends View, LoadingView {

    void saved(Boolean result);

    void initScreenTrack(LoginModel transform);
}

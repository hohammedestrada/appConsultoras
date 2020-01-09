package biz.belcorp.consultoras.feature.verifyaccount;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.view.LoadingView;

/**
 * @author andres.escobar on 4/08/2017.
 */
interface VerifyAccountView extends View, LoadingView {

    void initScreenTrack(LoginModel transform);

}

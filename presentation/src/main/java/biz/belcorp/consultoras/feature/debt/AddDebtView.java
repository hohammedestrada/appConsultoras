package biz.belcorp.consultoras.feature.debt;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.debt.CampaniaModel;
import biz.belcorp.consultoras.common.model.user.UserModel;
import biz.belcorp.consultoras.common.view.LoadingView;

/**
 *
 */
interface AddDebtView extends View, LoadingView {

    void initScreenTrack(LoginModel loginModel);

    void onUploadDebtComplete();

    void setData(UserModel model);

    void setUpCampaign(List<CampaniaModel> campaigns);

    void onError(Throwable exception);

    void trackBackPressed(LoginModel transform);

    void initScreenTrackAgregarDeuda(LoginModel loginModel);
}

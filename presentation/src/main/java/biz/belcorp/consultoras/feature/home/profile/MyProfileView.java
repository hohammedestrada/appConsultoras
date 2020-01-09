package biz.belcorp.consultoras.feature.home.profile;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.model.error.BooleanDtoModel;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.domain.entity.Country;
import biz.belcorp.consultoras.domain.entity.User;

public interface MyProfileView extends View, LoadingView {

    void showUserData(User user, int update);

    void saveUserData(Boolean status);

    void saveUpload(Boolean status);

    void saveDelete(Boolean status);

    void savePassword();

    void onError(ErrorModel errorModel);

    void onError(BooleanDtoModel errorModel);

    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);

    void saveUserCountry(Country country);

    void activeCellphone();

    void activateCheckWhatsapp( boolean mostrar);

    void goToVerifyEmail();
}

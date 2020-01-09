package biz.belcorp.consultoras.feature.home.tutorial;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;

/**
 *
 */
interface TutorialView extends View {

    void initScreenTrack(LoginModel loginModel);

    void initScreenTrackPosition(LoginModel loginModel, int position);

    void showIntrigueDialog(String image);

    void showRenewDialog(String image,String imageLogo, String message);

    void onHome();
}

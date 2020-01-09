package biz.belcorp.consultoras.feature.home.clients;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.error.BusinessErrorModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface ClientsView extends View, LoadingView {

    void onError(ErrorModel exception);

    void onClientsSaved();

    void onBusinessError(BusinessErrorModel errorModel);

    void initScreenTrack(LoginModel loginModel, int type);

    void trackEvent(String screenHome, String eventCat, String eventAction, String eventLabel, String eventName, LoginModel transform);

    void showMaterialTap();

    void showMaterialTapDeuda();

    void initScreenTrackNuevoCliente(LoginModel transform);

    void initScreenTrackAgregarDesdeContacto(LoginModel transform);

    void initScreenTrackBotonAgregarCliente(LoginModel transform);

    void initScreenTrackAgregarDeuda(LoginModel transform);
}

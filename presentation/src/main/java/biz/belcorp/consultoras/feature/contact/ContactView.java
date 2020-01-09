package biz.belcorp.consultoras.feature.contact;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface ContactView extends View, LoadingView {

    void showContacts(List<ClienteModel> contactList);

    void saved(Boolean result);

    void onError(Throwable exception);

    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);

    void initScreenTrackAgregar(LoginModel transform);
}

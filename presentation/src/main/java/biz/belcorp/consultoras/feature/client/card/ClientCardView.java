package biz.belcorp.consultoras.feature.client.card;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.AnotacionModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.menu.MenuModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface ClientCardView extends View, LoadingView {

    void saved(Boolean result);

    void showClient(ClienteModel model, String iso, String moneySymbol, int maxNoteAmount);

    void onError(Throwable exception);

    void anotacionDeleted(AnotacionModel anotacionModel);

    void initScreenTrack(LoginModel loginModel);

    void initScreenTrackGestionarDeuda(LoginModel loginModel);

    void initScreenTrackIngresarPedido(LoginModel loginModel);

    void initScreenTrackRevisarPedidos(LoginModel loginModel);

    void trackBackPressed(LoginModel transform);

    void showMaximumNoteAmount(int maxNoteAmount);

    void setMenuModel(MenuModel menu);
}

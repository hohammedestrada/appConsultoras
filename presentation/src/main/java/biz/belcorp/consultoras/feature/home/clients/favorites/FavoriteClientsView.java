package biz.belcorp.consultoras.feature.home.clients.favorites;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.error.BusinessErrorModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface FavoriteClientsView extends View, LoadingView {

    /**
     * Muestra la lista de Clientes de la consultora.
     */
    void showClients(List<ClienteModel> clientModelList);

    void onError(ErrorModel exception);

    void onErrorComunication(Integer type);

    void onClientsSaved(String guardado);

    void onDataUpdated(String mensaje);

    void onBusinessError(BusinessErrorModel errorModel);

    void initScreenTrack(LoginModel loginModel, int type);
}

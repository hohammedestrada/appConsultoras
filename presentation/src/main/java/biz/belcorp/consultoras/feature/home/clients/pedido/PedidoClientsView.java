package biz.belcorp.consultoras.feature.home.clients.pedido;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.error.BusinessErrorModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.model.user.UserModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface PedidoClientsView extends View, LoadingView {

    /**
     * Muestra la lista de Clientes de la consultora.
     */
    void showClients(List<ClienteModel> clientModelList, UserModel model);

    void onError(ErrorModel exception);

    void onBusinessError(BusinessErrorModel errorModel);
}

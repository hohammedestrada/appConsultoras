package biz.belcorp.consultoras.feature.home.cupon;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.domain.entity.Login;

interface CuponView extends View, LoadingView {
    void setData(Login login);
}

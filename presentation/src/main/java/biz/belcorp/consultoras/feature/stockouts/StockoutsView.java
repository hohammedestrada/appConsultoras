package biz.belcorp.consultoras.feature.stockouts;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.domain.entity.Menu;
import biz.belcorp.consultoras.domain.entity.Product;

interface StockoutsView extends View, LoadingView {

    void show(List<Product> list);

    void onError(ErrorModel exception);

    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);

    void showSearchOption();

    void onGetMenu(Menu menu);

}

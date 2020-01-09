package biz.belcorp.consultoras.feature.catalog;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.catalog.CatalogByCampaignModel;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.domain.entity.Menu;
import biz.belcorp.consultoras.domain.entity.User;

/**
 *
 */
interface CatalogContainerView extends View, LoadingView {

    void showCatalog(List<CatalogByCampaignModel> catalogs, String paisISO, User user);

    void showMagazine(List<CatalogByCampaignModel> magazines);

    void showError(Throwable throwable);

    void initScreenTrack(LoginModel transform, int type);

    void initializeAdapter(int countSize);

    void trackBackPressed(LoginModel transform, int type);

    void showSearchOption();

    void onGetMenu(Menu menu);

    void getPdfUrlSuccess(String url, String title);

    void getPdfUrlFail();

}

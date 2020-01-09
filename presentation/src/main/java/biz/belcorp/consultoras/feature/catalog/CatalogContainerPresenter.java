package biz.belcorp.consultoras.feature.catalog;

import android.support.annotation.NonNull;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import javax.inject.Inject;
import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.catalog.CatalogByCampaignModel;
import biz.belcorp.consultoras.common.model.catalog.CatalogModelDataMapper;
import biz.belcorp.consultoras.data.manager.SessionManager;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.CatalogoWrapper;
import biz.belcorp.consultoras.domain.entity.Menu;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.CatalogUseCase;
import biz.belcorp.consultoras.domain.interactor.MenuUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.CountryUtil;

/**
 *
 */
@PerActivity
public class CatalogContainerPresenter implements Presenter<CatalogContainerView> {

    private CatalogContainerView catalogContainerView;

    private final AccountUseCase accountUseCase;
    private final CatalogUseCase catalogUseCase;
    private final UserUseCase userUseCase;
    private final MenuUseCase menuUseCase;
    private final CatalogModelDataMapper catalogDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    CatalogContainerPresenter(AccountUseCase accountUseCase,
                              CatalogUseCase catalogUseCase, UserUseCase userUseCase,
                              MenuUseCase menuUseCase,
                              CatalogModelDataMapper catalogDataMapper,
                              LoginModelDataMapper loginModelDataMapper) {
        this.accountUseCase = accountUseCase;
        this.catalogUseCase = catalogUseCase;
        this.menuUseCase = menuUseCase;
        this.userUseCase = userUseCase;
        this.catalogDataMapper = catalogDataMapper;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull CatalogContainerView view) {
        this.catalogContainerView = view;
    }

    @Override
    public void resume() {
        // EMPTY
    }

    @Override
    public void pause() {
        // EMPTY
    }

    @Override
    public void destroy() {
        this.userUseCase.dispose();
        this.accountUseCase.dispose();
        this.menuUseCase.dispose();
        this.catalogContainerView = null;
    }

    void getMenuActive(String code1, String code2) {
        menuUseCase.getActive(code1, code2, new GetMenuObserver());
    }

    /**********************************************************/

    private final class GetMenuObserver extends BaseObserver<Menu> {

        @Override
        public void onNext(Menu menu) {
            if (null == catalogContainerView) return;
            catalogContainerView.onGetMenu(menu);
        }
    }

    void loadBooks() {
        catalogContainerView.showLoading();
        userUseCase.get(new GetUser());
    }

    void initScreenTrack(int position) {
        userUseCase.get(new UserPropertyObserver(position));
    }

    void trackBackPressed(int currentPage) {
        userUseCase.get(new BackPressedObserver(currentPage));
    }

    void downLoadCatalog(String descripcion, String title) {
        catalogUseCase.getObservableUrlDescarga(descripcion, new GetUrlDescarga(title));
    }

    /**********************************************************/

    private final class GetUser extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (user != null) {
                if (user.isMostrarBuscador()) catalogContainerView.showSearchOption();
                catalogUseCase.get(user, CountryUtil.getMaximumCampaign(user.getCountryISO()),
                    new BooksObserver(user));
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.w("onError", exception);
            catalogContainerView.hideLoading();

            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                catalogContainerView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                catalogContainerView.showError(exception);
            }
        }
    }

    private final class BooksObserver extends BaseObserver<List<CatalogoWrapper>> {

        private User user;
        private String countryISO;

        BooksObserver(User user) {
            this.user = user;
            this.countryISO = user.getCountryISO();
        }

        @Override
        public void onNext(List<CatalogoWrapper> wrapper) {

            List<CatalogByCampaignModel> catalogByCampaigns = catalogDataMapper.transformCatalog(wrapper);
            catalogContainerView.showCatalog(catalogByCampaigns, countryISO, user);
            catalogContainerView.showMagazine(catalogByCampaigns);
            catalogContainerView.initializeAdapter(catalogByCampaigns.size());
            catalogContainerView.hideLoading();
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.w("onError", exception);
            catalogContainerView.hideLoading();

            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                catalogContainerView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                catalogContainerView.showError(exception);
            }
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        private final int type;

        private UserPropertyObserver(int type) {
            this.type = type;
        }

        @Override
        public void onNext(User user) {
            catalogContainerView.initScreenTrack(loginModelDataMapper.transform(user), type);
        }
    }

    private final class BackPressedObserver extends BaseObserver<User> {

        private final int type;

        private BackPressedObserver(int type) {
            this.type = type;
        }

        @Override
        public void onNext(User user) {
            catalogContainerView.trackBackPressed(loginModelDataMapper.transform(user), type);
        }
    }

    private final class GetUrlDescarga extends BaseObserver<String> {


        private final String title;

        public GetUrlDescarga(String title){
            this.title = title;
        }

        @Override
        public void onNext(String s) {
            if (s == null) {
                catalogContainerView.getPdfUrlFail();
                return;
            }

            if (s.trim().length() == 0) {
                catalogContainerView.getPdfUrlFail();
                return;
            }

            catalogContainerView.getPdfUrlSuccess(s, title);
        }

        @Override
        public void onError(@NotNull Throwable exception) {
            super.onError(exception);
            catalogContainerView.getPdfUrlFail();
        }
    }
}

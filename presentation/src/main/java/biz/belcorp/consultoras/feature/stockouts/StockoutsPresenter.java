package biz.belcorp.consultoras.feature.stockouts;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.domain.entity.Menu;
import biz.belcorp.consultoras.domain.entity.Product;
import biz.belcorp.consultoras.domain.entity.ProductSearchRequest;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.MenuUseCase;
import biz.belcorp.consultoras.domain.interactor.ProductUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.exception.ErrorFactory;
import biz.belcorp.consultoras.feature.embedded.ordersfic.OrdersFicPresenter;

class StockoutsPresenter implements Presenter<StockoutsView> {

    private StockoutsView stockoutsView;

    private final ProductUseCase productUseCase;
    private final UserUseCase userUseCase;
    private final MenuUseCase menuUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    StockoutsPresenter(ProductUseCase productUseCase, UserUseCase userUseCase,
                       MenuUseCase menuUseCase,
                       LoginModelDataMapper loginModelDataMapper) {
        this.productUseCase = productUseCase;
        this.userUseCase = userUseCase;
        this.menuUseCase = menuUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull StockoutsView view) {
        stockoutsView = view;
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
        this.productUseCase.dispose();
        this.userUseCase.dispose();
        this.menuUseCase.dispose();
        this.stockoutsView = null;
    }

    /**********************************************************/

    public void data() {
        stockoutsView.showLoading();
        this.userUseCase.get(new GetUser());
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    void getMenuActive(String code1, String code2) {
        menuUseCase.getActive(code1, code2, new GetMenuObserver());
    }

    /**********************************************************/

    private final class GetMenuObserver extends BaseObserver<Menu> {

        @Override
        public void onNext(Menu menu) {
            if (null == stockoutsView) return;
            stockoutsView.onGetMenu(menu);
        }
    }

    private final class GetUser extends BaseObserver<User> {

        @Override
        public void onNext(User user) {

            if (null == user) return;

            if(user.isMostrarBuscador()){
                stockoutsView.showSearchOption();
            }

            ProductSearchRequest params = new ProductSearchRequest();
            params.setCampaingId(Integer.parseInt(user.getCampaing()));
            params.setZoneId(Integer.parseInt(user.getZoneID()));
            params.setCuv("");
            params.setDescription("");

            productUseCase.getStockouts(params, new GetStockouts());
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (stockoutsView == null) return;
            stockoutsView.hideLoading();
            stockoutsView.onError(ErrorFactory.INSTANCE.create(exception));

        }
    }

    private final class GetStockouts extends BaseObserver<Collection<Product>> {

        @Override
        public void onNext(Collection<Product> products) {
            if (null == stockoutsView) return;
            stockoutsView.hideLoading();
            stockoutsView.show((ArrayList<Product>) products);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == stockoutsView) return;
            stockoutsView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                stockoutsView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                stockoutsView.onError(ErrorFactory.INSTANCE.create(exception));
            }

        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            if (null == stockoutsView) return;
            stockoutsView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == stockoutsView) return;
            stockoutsView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }
}

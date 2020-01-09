package biz.belcorp.consultoras.feature.embedded.closeout;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Menu;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.MenuUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.feature.embedded.ordersfic.OrdersFicPresenter;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.security.JwtEncryption;

@PerActivity
public class CloseoutPresenter implements Presenter<CloseoutView> {

    private CloseoutView closeoutView;

    private final AccountUseCase accountUseCase;
    private final UserUseCase userUseCase;
    private final MenuUseCase menuUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    CloseoutPresenter(AccountUseCase accountUseCase,
                      UserUseCase userUseCase,
                      MenuUseCase menuUseCase,
                      LoginModelDataMapper loginModelDataMapper) {
        this.accountUseCase = accountUseCase;
        this.userUseCase = userUseCase;
        this.menuUseCase = menuUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull CloseoutView view) {
        closeoutView = view;
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
        this.accountUseCase.dispose();
        this.userUseCase.dispose();
        this.menuUseCase.dispose();
        this.closeoutView = null;
    }

    /**********************************************************/

    void getPageTitle(String code){
        menuUseCase.get(code, new GetTitleObserver());
    }

    public void load(String deviceId, String page) {
        closeoutView.showLoading();
        this.userUseCase.get(new GetUser(deviceId, page));
        this.userUseCase.updateScheduler(new UpdateObserver());
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void trackBack() {
        userUseCase.get(new UserBackPressedObserver());
    }

    void getMenuActive(String code1, String code2) {
        menuUseCase.getActive(code1, code2, new GetMenuObserver());
    }

    /**********************************************************/

    private final class GetMenuObserver extends BaseObserver<Menu> {

        @Override
        public void onNext(Menu menu) {
            if (null == closeoutView) return;
            closeoutView.onGetMenu(menu);
        }
    }

    private final class GetTitleObserver extends BaseObserver<Menu> {
        @Override
        public void onNext(Menu result) {
            if (null == closeoutView) return;
            if (null != result) closeoutView.setMenuTitle(result.getDescripcion());
        }
    }

    private final class GetUser extends BaseObserver<User> {

        String deviceId;
        String page;

        public GetUser(String deviceId, String page) {
            this.deviceId = deviceId;
            this.page = page;
        }

        @Override
        public void onNext(User user) {

            String url = "";

            if (null != user) {

                String token = "";
                String countryISO = user.getCountryISO();
                String consultantId = user.getUserCode();

                if (!"".equals(countryISO) && !"".equals(consultantId)) {

                    if (null == deviceId || deviceId.isEmpty())
                        deviceId = consultantId;

                    JSONObject jsonPayLoad = new JSONObject();

                    try {
                        jsonPayLoad.put("Pais", countryISO);
                        jsonPayLoad.put("Pagina", page);
                        jsonPayLoad.put("CodigoUsuario", consultantId);
                        jsonPayLoad.put("EsAppMobile", "True");
                        jsonPayLoad.put("ClienteId", "0");
                        jsonPayLoad.put("Identifier", deviceId);

                        String payload = jsonPayLoad.toString();
                        token = JwtEncryption.newInstance().encrypt(GlobalConstant.SECRET_PASE_PEDIDO, payload);

                    } catch (Exception e) {
                        BelcorpLogger.w("GetUser.onNext", e);
                    }

                }

                if (!"".equals(token))
                    url = BuildConfig.PASE_OFERTA + token;

                if(user.isMostrarBuscador()){
                    closeoutView.showSearchOption();
                }
            }

            closeoutView.showUrl(url);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            super.onError(exception);

            closeoutView.hideLoading();
            closeoutView.showError();
        }
    }

    private final class UpdateObserver extends BaseObserver<Boolean> {
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            closeoutView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == closeoutView) return;
            closeoutView.trackBack(loginModelDataMapper.transform(user));
        }
    }
}

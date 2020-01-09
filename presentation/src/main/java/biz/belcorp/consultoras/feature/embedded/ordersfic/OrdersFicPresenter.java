package biz.belcorp.consultoras.feature.embedded.ordersfic;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Menu;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.CaminoBrillanteUseCase;
import biz.belcorp.consultoras.domain.interactor.MenuUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.PageUrlType;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.security.JwtEncryption;

@PerActivity
public class OrdersFicPresenter implements Presenter<OrdersFicView> {

    private OrdersFicView myOrdersView;

    private final AccountUseCase accountUseCase;
    private final UserUseCase userUseCase;
    private final MenuUseCase menuUseCase;
    private final CaminoBrillanteUseCase caminoBrillanteUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    OrdersFicPresenter(AccountUseCase accountUseCase,
                              UserUseCase userUseCase,
                              MenuUseCase menuUseCase,
                              CaminoBrillanteUseCase caminoBrillanteUseCase,
                              LoginModelDataMapper loginModelDataMapper) {
        this.accountUseCase = accountUseCase;
        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
        this.menuUseCase = menuUseCase;
        this.caminoBrillanteUseCase = caminoBrillanteUseCase;
    }

    @Override
    public void attachView(@NonNull OrdersFicView view) {
        myOrdersView = view;
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
        this.caminoBrillanteUseCase.dispose();
        this.myOrdersView = null;
    }

    /**********************************************************/

    public void load(String deviceId) {
        myOrdersView.showLoading();
        this.caminoBrillanteUseCase.getNivelConsultoraAsObserver(new GetNivelConsultoraObserver(deviceId));
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void trackBack() {
        this.userUseCase.get(new UserBackPressedObserver());
    }

    void getMenuActive(String code1, String code2) {
        menuUseCase.getActive(code1, code2, new GetMenuObserver());
    }

    /**********************************************************/

    private final class GetMenuObserver extends BaseObserver<Menu> {

        @Override
        public void onNext(Menu menu) {
            if (null == myOrdersView) return;
            myOrdersView.onGetMenu(menu);
        }
    }

    private final class GetUser extends BaseObserver<User> {

        String deviceId;
        int nivelId;

        public GetUser(String deviceId, int nivelId) {
            this.deviceId = deviceId;
            this.nivelId = nivelId;
        }

        @Override
        public void onNext(User user) {

            String url = "";

            if (null != user) {

                String token = "";
                String countryISO = user.getCountryISO();
                String consultantId = user.getUserCode();

                if (!"".equals(countryISO) && !"".equals(consultantId)) {

                    JSONObject jsonPayLoad = new JSONObject();

                    try {
                        jsonPayLoad.put("Pais", countryISO);
                        jsonPayLoad.put("Pagina", PageUrlType.PEDIDOSFIC);
                        jsonPayLoad.put("CodigoUsuario", consultantId);
                        jsonPayLoad.put("EsAppMobile", "True");
                        jsonPayLoad.put("ClienteId", "0");
                        jsonPayLoad.put("Identifier", deviceId);
                        jsonPayLoad.put("NivelCaminoBrillante", nivelId);

                        String payload = jsonPayLoad.toString();
                        token = JwtEncryption.newInstance().encrypt(GlobalConstant.SECRET_PASE_PEDIDO, payload);

                    } catch (Exception e) {
                        BelcorpLogger.w("GetUser.onNext", e);
                    }

                }

                if (!"".equals(token))
                    url = GlobalConstant.URL_PASE_PEDIDO + token;

                if(user.isMostrarBuscador()){
                    myOrdersView.showSearchOption();
                }

            }

            myOrdersView.showUrl(url);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            super.onError(exception);

            myOrdersView.hideLoading();
            myOrdersView.showError();
        }
    }

    private final class UpdateObserver extends BaseObserver<Boolean> {
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            myOrdersView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == myOrdersView) return;
            myOrdersView.trackBack(loginModelDataMapper.transform(user));
        }
    }

    private final class GetNivelConsultoraObserver extends BaseObserver<Integer> {

        String deviceId;

        public GetNivelConsultoraObserver(String deviceId){
            this.deviceId = deviceId;
        }

        @Override
        public void onNext(Integer nivel) {
            userUseCase.get(new GetUser(deviceId, nivel));
            userUseCase.updateScheduler(new UpdateObserver());
        }
    }

}

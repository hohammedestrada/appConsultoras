package biz.belcorp.consultoras.feature.client.order.history;


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
public class ClientOrderHistoryPresenter implements Presenter<ClientOrderHistoryView> {

    private ClientOrderHistoryView clientOrderHistoryView;

    private final AccountUseCase accountUseCase;
    private final UserUseCase userUseCase;
    private final MenuUseCase menuUseCase;
    private final CaminoBrillanteUseCase caminoBrillanteUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    public ClientOrderHistoryPresenter(AccountUseCase accountUseCase, UserUseCase userUseCase, MenuUseCase menuUseCase, CaminoBrillanteUseCase caminoBrillanteUseCase, LoginModelDataMapper loginModelDataMapper) {
        this.accountUseCase = accountUseCase;
        this.userUseCase = userUseCase;
        this.menuUseCase = menuUseCase;
        this.caminoBrillanteUseCase = caminoBrillanteUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull ClientOrderHistoryView view) {
        clientOrderHistoryView = view;
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
        this.clientOrderHistoryView = null;
    }

    /**********************************************************/

    public void load(int clientId, int campaign, String deviceId) {
        this.clientOrderHistoryView.showLoading();
        this.caminoBrillanteUseCase.getNivelConsultoraAsObserver(new GetNivelConsultoraObserver(clientId, campaign, deviceId));
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
            if (null == clientOrderHistoryView) return;
            clientOrderHistoryView.onGetMenu(menu);
        }
    }

    private final class GetUser extends BaseObserver<User> {

        private int clientId;
        private int campaign;
        private String deviceId;
        private int nivel;

        GetUser(int clientId, int campaign, String deviceId, int nivel) {
            this.clientId = clientId;
            this.deviceId = deviceId;
            this.campaign = campaign;
            this.nivel = nivel;
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
                        jsonPayLoad.put("Pagina", PageUrlType.CLIENT_ORDER_HISTORY);
                        jsonPayLoad.put("CodigoUsuario", consultantId);
                        jsonPayLoad.put("EsAppMobile", "True");
                        jsonPayLoad.put("ClienteId", clientId);
                        jsonPayLoad.put("Campania", campaign);
                        jsonPayLoad.put("Identifier", deviceId);
                        jsonPayLoad.put("NivelCaminoBrillante", nivel);

                        String payload = jsonPayLoad.toString();
                        token = JwtEncryption.newInstance().encrypt(GlobalConstant.SECRET_PASE_PEDIDO, payload);

                    } catch (Exception e) {
                        BelcorpLogger.w("GetUser.onNext", e);
                    }

                }

                if (!"".equals(token))
                    url = GlobalConstant.URL_PASE_PEDIDO + token;

            }

            clientOrderHistoryView.showURL(url);
        }

        @Override
        public void onError(Throwable exception) {
            BelcorpLogger.w("onError", exception);

            clientOrderHistoryView.hideLoading();
            clientOrderHistoryView.showError();
        }

    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            clientOrderHistoryView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            clientOrderHistoryView.trackBack(loginModelDataMapper.transform(user));
        }
    }

    private final class GetNivelConsultoraObserver extends BaseObserver<Integer> {

        int clientId;
        int campaign;
        String deviceId;

        public GetNivelConsultoraObserver(int clientId, int campaign, String deviceId) {
            this.deviceId = deviceId;
            this.clientId = clientId;
            this.campaign = campaign;
        }

        @Override
        public void onNext(Integer nivel) {
            userUseCase.get(new GetUser(clientId, campaign, deviceId, nivel));
        }
    }


}

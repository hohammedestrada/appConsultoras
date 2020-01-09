package biz.belcorp.consultoras.feature.embedded.success;

import android.support.annotation.NonNull;
import javax.inject.Inject;
import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.security.MdfiveEncryption;

public class WalkSuccessPresenter implements Presenter<WalkSuccessWebView> {
    private WalkSuccessWebView wlkSuccessWebView;
    private final UserUseCase userUseCase;
    private final LoginModelDataMapper loginModelDataMapper;
    @Inject
     WalkSuccessPresenter(UserUseCase userUseCase,LoginModelDataMapper loginModelDataMapper) {
        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull WalkSuccessWebView view) {
        wlkSuccessWebView=view;
    }

    @Override
    public void resume() {
        //nada
    }

    @Override
    public void pause() {
        //nada
    }

    @Override
    public void destroy() {
        this.userUseCase.dispose();
        this.wlkSuccessWebView=null;
    }

    /** */

    public void loadURL(){
        wlkSuccessWebView.showLoading();
        this.userUseCase.get(new GetUser());

    }

    void trackBack() {
        userUseCase.get(new UserBackPressedObserver());
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }


    /** */

    private final class GetUser extends BaseObserver<User> {

        public GetUser() {}

        @Override
        public void onNext(User user) {

            String url = "";

            if (null != user) {
                String countryISO = user.getCountryISO();
                String consultantId = user.getUserCode();
                String userCampaing = user.getCampaing();

                try {
                   url = url.concat(GlobalConstant.URL_CAMINO).concat(countryISO).concat(GlobalConstant.SEPARATOR).concat(userCampaing);
                   url=url.concat(GlobalConstant.SEPARATOR).concat(MdfiveEncryption.newInstance().encrypt(consultantId));
                    wlkSuccessWebView.showUrl(url);
                } catch (Exception e) {
                    wlkSuccessWebView.showError();
                }
            }
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            wlkSuccessWebView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == wlkSuccessWebView) return;
            wlkSuccessWebView.trackBack(loginModelDataMapper.transform(user));
        }
    }
}

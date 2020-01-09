package biz.belcorp.consultoras.feature.home.profile;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.error.DtoModelMapper;
import biz.belcorp.consultoras.common.model.user.UserModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.BasicDto;
import biz.belcorp.consultoras.domain.entity.Country;
import biz.belcorp.consultoras.domain.entity.Login;
import biz.belcorp.consultoras.domain.entity.PasswordUpdateRequest;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.entity.UserConfigData;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.AuthUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.CountryUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.exception.ErrorFactory;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.ImageUtils;
import biz.belcorp.consultoras.util.anotation.ChangePasswordCode;
import biz.belcorp.consultoras.util.anotation.UserConfigAccountCode;
import biz.belcorp.library.log.BelcorpLogger;
import okhttp3.MultipartBody;

@PerActivity
public class MyProfilePresenter implements Presenter<MyProfileView> {

    private static final String TAG = "MyProfilePresenter";
    private MyProfileView myProfileView;
    private final AuthUseCase authUseCase;
    private final AccountUseCase accountUseCase;
    private final UserUseCase userUseCase;
    private final CountryUseCase countryUseCase;
    private final LoginModelDataMapper loginModelDataMapper;
    private final UserModelDataMapper userModelDataMapper;
    private final DtoModelMapper dtoModelMapper;

    @Inject
    MyProfilePresenter(AuthUseCase authUseCase, AccountUseCase accountUseCase,
                       UserUseCase userUseCase, CountryUseCase countryUseCase, LoginModelDataMapper loginModelDataMapper,
                       UserModelDataMapper userModelDataMapper, DtoModelMapper dtoModelMapper) {
        this.authUseCase = authUseCase;
        this.accountUseCase = accountUseCase;
        this.userUseCase = userUseCase;
        this.countryUseCase = countryUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
        this.userModelDataMapper = userModelDataMapper;
        this.dtoModelMapper = dtoModelMapper;
    }

    @Override
    public void attachView(@NonNull MyProfileView view) {
        myProfileView = view;
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
        this.authUseCase.dispose();
        this.accountUseCase.dispose();
        this.userUseCase.dispose();
        this.myProfileView = null;
    }

    /**********************************************************/

    void data() {
        data(false);
    }

    void data(boolean actualizacionDatos) {
        myProfileView.showLoading();
        userUseCase.get(new GetUser(0));
        if(actualizacionDatos){
            accountUseCase.getConfig(UserConfigAccountCode.ADC, new BaseObserver<Collection<UserConfigData>>(){
                @Override
                public void onNext(Collection<UserConfigData> userConfigData) {
                    if(userConfigData!= null){
                        for (UserConfigData item :userConfigData) {
                            if(UserConfigAccountCode.ADC_CELULAR.compareToIgnoreCase(item.getCode()) == 0 && "2".compareToIgnoreCase(item.getValue1()) == 0 ){
                                myProfileView.activeCellphone();
                            }

                        }
                    }
                }
            });
        }
    }


    void enviarCorreo(String correoNuevo ){
        myProfileView.showLoading();
        accountUseCase.enviarCorreo(correoNuevo,new BaseObserver<BasicDto<Boolean>>(){
            @Override
            public void onNext(BasicDto<Boolean> t) {
                myProfileView.hideLoading();

                if(t.getCode().equals(GlobalConstant.CODE_OK)){
                    myProfileView.goToVerifyEmail();
                }
                else{
                    if(t.getMessage()!=null){
                        BelcorpLogger.d(t.getMessage());
                    }
                }
            }

            @Override
            public void onError(@NotNull Throwable exception) {
                if (null == myProfileView) return;
                myProfileView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    myProfileView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    myProfileView.onError(ErrorFactory.INSTANCE.create(exception));
                }

            }
        });
    }

    void refreshData(int updated){
        myProfileView.showLoading();
        accountUseCase.refreshData(new RefreshData(updated));
    }

    void saveData(User user) {
        myProfileView.showLoading();
        accountUseCase.update(userModelDataMapper.transformUser(user), new SaveUser());
    }

    void uploadPhoto(String contentType, MultipartBody body) {
        myProfileView.showLoading();
        accountUseCase.uploadFile(contentType, body, new UploadFile());
    }

    void deletePhoto(User user) {
        myProfileView.showLoading();

        if (user.getPhotoName() != null && user.getPhotoName().isEmpty()) {
            accountUseCase.deletePhotoFB(userModelDataMapper.transformUser(user), new DeletePhoto());
        } else {
            accountUseCase.deletePhotoServer(userModelDataMapper.transformUser(user), new DeletePhoto());
        }
    }

    public void changePassword(PasswordUpdateRequest request) {
        myProfileView.showLoading();
        accountUseCase.updatePassword(request, new ChangePassword());
    }

    public void getCountry(String iso){ countryUseCase.find(iso, new GetCountryUser() ); }

    public void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    public void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }


    /**********************************************************/


    private final class GetCountryUser extends BaseObserver<Country> {
        @Override
        public void onNext(Country country) {
            super.onNext(country);
            myProfileView.saveUserCountry(country);
        }
    }


    private final class RefreshData extends BaseObserver<Login> {
        private int update;

        RefreshData(int update){
            this.update = update;
        }

        @Override
        public void onNext(Login login) {
            if (myProfileView == null) return;
            if (null != login) {
                userUseCase.get(new GetUser(update));
            } else {
                myProfileView.hideLoading();
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (myProfileView == null) return;
            myProfileView.hideLoading();
        }
    }

    private final class GetUser extends BaseObserver<User> {

        private int update;

        GetUser(int update){
            this.update = update;
        }

        @Override
        public void onNext(User user) {
            user.setTipoArchivo("01");

            myProfileView.activateCheckWhatsapp(user.isActivaNotificaconesWhatsapp());
            user.setPhotoName(ImageUtils.INSTANCE.getPhotoNameUrl(user.getPhotoProfile()));
            myProfileView.hideLoading();
            myProfileView.showUserData(user, update);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            myProfileView.hideLoading();
            BelcorpLogger.w(TAG, "ERROR!", exception);
        }
    }

    private final class SaveUser extends BaseObserver<Boolean> {
        @Override
        public void onComplete() {
            authUseCase.updateScheduler(new UpdateScheduler());
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == myProfileView) return;
            myProfileView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                myProfileView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                myProfileView.onError(ErrorFactory.INSTANCE.create(exception));
            }
        }
    }

    private final class DeletePhoto extends BaseObserver<Boolean> {
        @Override
        public void onComplete() {
            if (null == myProfileView) return;
            myProfileView.hideLoading();
            myProfileView.saveDelete(true);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == myProfileView) return;
            myProfileView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                myProfileView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                myProfileView.onError(ErrorFactory.INSTANCE.create(exception));
            }
        }
    }

    private final class UploadFile extends BaseObserver<Boolean> {

        @Override
        public void onComplete() {
            if (null == myProfileView) return;
            myProfileView.hideLoading();
            myProfileView.saveUpload(true);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == myProfileView) return;
            myProfileView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                myProfileView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                myProfileView.onError(ErrorFactory.INSTANCE.create(exception));
            }
        }
    }

    private final class ChangePassword extends BaseObserver<BasicDto<Boolean>> {

        @Override
        public void onNext(BasicDto<Boolean> basicDto) {
            if (myProfileView == null) return;

            myProfileView.hideLoading();
            if (null != basicDto) {
                if (basicDto.getCode()!= null && basicDto.getCode().equals(ChangePasswordCode.OK)) {
                    myProfileView.savePassword();
                } else {
                    myProfileView.onError(dtoModelMapper.transform(basicDto));
                }
            }

        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == myProfileView) return;
            myProfileView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                myProfileView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                myProfileView.onError(ErrorFactory.INSTANCE.create(exception));
            }
        }
    }

    private final class UpdateScheduler extends BaseObserver<Boolean> {

        @Override
        public void onComplete() {
            if (null == myProfileView) return;
            myProfileView.hideLoading();
            myProfileView.saveUserData(true);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == myProfileView) return;
            myProfileView.hideLoading();
            myProfileView.onError(ErrorFactory.INSTANCE.create(exception));
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == myProfileView) return;
            myProfileView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == myProfileView) return;
            myProfileView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }
}

package biz.belcorp.consultoras.feature.home.tutorial;

import android.support.annotation.NonNull;

import java.util.Collection;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.domain.entity.IntrigueBody;
import biz.belcorp.consultoras.domain.entity.Login;
import biz.belcorp.consultoras.domain.entity.RenewBody;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.entity.UserConfigData;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.SessionUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.anotation.UserConfigAccountCode;

/**
 *
 */
public class TutorialPresenter implements Presenter<TutorialView> {

    private final SessionUseCase sessionUseCase;
    private final AccountUseCase accountUseCase;
    private final UserUseCase userUseCase;
    private final LoginModelDataMapper dataMapper;

    private TutorialView view;

    @Inject
    TutorialPresenter(SessionUseCase sessionUseCase, AccountUseCase accountUseCase,
                      UserUseCase userUseCase, LoginModelDataMapper dataMapper) {
        this.sessionUseCase = sessionUseCase;
        this.accountUseCase = accountUseCase;
        this.userUseCase = userUseCase;
        this.dataMapper = dataMapper;
    }

    @Override
    public void attachView(@NonNull TutorialView view) {
        this.view = view;
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
        this.view = null;
    }

    /**********************************************************************************************/

    public void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void trackEvent(String screenName,
                    String eventCat,
                    String eventAction,
                    String labelName,
                    String eventName) {
        userUseCase.get(new EventPropertyObserver(screenName, eventCat, eventAction, labelName, eventName));
    }

    void initScreenTrackPosition(int position) {
        userUseCase.get(new UserPropertyPositionObserver(position));
    }

    void initIntrigue(){
        userUseCase.getLogin(new GetUser());
    }

    /**********************************************************************************************/

    private final class GetUser extends BaseObserver<Login> {

        @Override
        public void onNext(Login login) {
            sessionUseCase.isIntrigueStatus(new CheckIntrigueStatusObserver(login.getCampaing()));
        }
    }

    private final class CheckIntrigueStatusObserver extends BaseObserver<Boolean> {

        private String campaign;

        CheckIntrigueStatusObserver(String campaign) {
            this.campaign = campaign;
        }

        @Override
        public void onNext(Boolean aBoolean) {
            accountUseCase.getConfig(UserConfigAccountCode.INTRIGA, new IntrigueObserver(campaign, aBoolean));
        }
    }

    private final class CheckRenewStatusObserver extends BaseObserver<Boolean> {

        private String campaign;

        CheckRenewStatusObserver(String campaign) {
            this.campaign = campaign;
        }

        @Override
        public void onNext(Boolean aBoolean) {
            accountUseCase.getConfig(UserConfigAccountCode.INTRIGA, new RenewObserver(campaign, aBoolean));
        }
    }

    private final class IntrigueObserver extends BaseObserver<Collection<UserConfigData>> {

        private String campaign;
        private Boolean isIntrigueShowing;

        IntrigueObserver(String campaign, Boolean isIntrigueShowing) {
            this.campaign = campaign;
            this.isIntrigueShowing = isIntrigueShowing;
        }

        @Override
        public void onNext(Collection<UserConfigData> userConfigData) {
            if (!isIntrigueShowing){
                userUseCase.checkIntrigueStatus(userConfigData, campaign, new BaseObserver<IntrigueBody>() {
                    @Override
                    public void onNext(IntrigueBody intrigueBody) {
                        if (intrigueBody.isShow()){
                            sessionUseCase.updateIntrigueStatus(true , new BaseObserver<Boolean>(){
                                @Override
                                public void onNext(Boolean aBoolean) {
                                    view.showIntrigueDialog(intrigueBody.getImage());
                                }
                            });
                        } else {
                            renewCheck(campaign);
                        }
                    }
                });
            } else {
                renewCheck(campaign);
            }
        }
    }

    private final class RenewObserver extends BaseObserver<Collection<UserConfigData>> {

        private String campaign;
        private Boolean isRenewShowing;

        RenewObserver(String campaign, Boolean isRenewShowing) {
            this.campaign = campaign;
            this.isRenewShowing = isRenewShowing;
        }

        @Override
        public void onNext(Collection<UserConfigData> userConfigData) {
            if (!isRenewShowing){
                userUseCase.checkRenewStatus(userConfigData, campaign, new BaseObserver<RenewBody>() {
                    @Override
                    public void onNext(RenewBody renewBody) {
                        if (renewBody.isShow()){
                            sessionUseCase.updateRenewStatus(true , new BaseObserver<Boolean>(){
                                @Override
                                public void onNext(Boolean aBoolean) {
                                    view.showRenewDialog(renewBody.getImage(),renewBody.getImagelogo(), renewBody.getMessage());
                                }
                            });
                        } else {
                            view.onHome();
                        }
                    }
                });
            } else {
                view.onHome();
            }
        }
    }

    private void renewCheck(String campaign){
        sessionUseCase.isRenewStatus(new CheckRenewStatusObserver(campaign));
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            view.initScreenTrack(dataMapper.transform(user));
        }
    }

    private final class UserPropertyPositionObserver extends BaseObserver<User> {

        private int position;

        public UserPropertyPositionObserver(int position) {
            this.position = position;
        }

        @Override
        public void onNext(User user) {
            view.initScreenTrackPosition(dataMapper.transform(user), position);
        }
    }

    private final class EventPropertyObserver extends BaseObserver<User> {

        final String screenHome;
        final String eventCat;
        final String eventAction;
        final String eventLabel;
        final String eventName;

        private EventPropertyObserver(String screenHome,
                                      String eventCat,
                                      String eventAction,
                                      String eventLabel,
                                      String eventName) {
            this.screenHome = screenHome;
            this.eventCat = eventCat;
            this.eventAction = eventAction;
            this.eventLabel = eventLabel;
            this.eventName = eventName;
        }

        @Override
        public void onNext(User user) {
            Tracker.trackEvent(screenHome,
                eventCat,
                eventAction,
                eventLabel,
                eventName,
                dataMapper.transform(user));
        }
    }
}

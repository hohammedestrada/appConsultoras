package biz.belcorp.consultoras.feature.contest.news;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.contest.OrderModel;
import biz.belcorp.consultoras.common.model.contest.OrderModelDataMapper;
import biz.belcorp.consultoras.common.model.error.DtoModelMapper;
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel;
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModelDataMapper;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Concurso;
import biz.belcorp.consultoras.domain.entity.BasicDto;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.IncentivesUseCase;
import biz.belcorp.consultoras.domain.interactor.OrderUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.exception.ErrorFactory;
import biz.belcorp.consultoras.util.anotation.OrderResultCode;

@PerActivity
class NewProgramPresenter implements Presenter<NewProgramView> {

    private final UserUseCase userUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    private final IncentivesUseCase incentivesUseCase;
    private final ConcursoModelDataMapper concursoModelDataMapper;
    private final OrderUseCase orderUseCase;
    private final OrderModelDataMapper orderModelDataMapper;
    private final DtoModelMapper dtoModelMapper;

    private NewProgramView newProgramView;


    @Inject
    NewProgramPresenter(UserUseCase userUseCase,
                        LoginModelDataMapper loginModelDataMapper,
                        IncentivesUseCase incentivesUseCase,
                        ConcursoModelDataMapper concursoModelDataMapper,
                        OrderUseCase orderUseCase,
                        OrderModelDataMapper orderModelDataMapper,
                        DtoModelMapper dtoModelMapper) {
        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
        this.incentivesUseCase = incentivesUseCase;
        this.concursoModelDataMapper = concursoModelDataMapper;
        this.orderUseCase = orderUseCase;
        this.orderModelDataMapper = orderModelDataMapper;
        this.dtoModelMapper = dtoModelMapper;
    }

    @Override
    public void attachView(@NonNull NewProgramView view) {
        if (view instanceof NewProgramFragment) {
            newProgramView = view;
        }
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
        this.incentivesUseCase.dispose();
        this.newProgramView = null;
    }

    /******************************************************/

    public void get(String contestCode) {
        newProgramView.showLoading();
        userUseCase.get(new GetUser(contestCode));
    }

    public void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void addCupon(OrderModel orderModel) {
        newProgramView.showLoading();
        userUseCase.get(new UserCuponObserver(orderModel));
    }

    void removeReservation() {
        newProgramView.showLoading();
        userUseCase.get(new UserRemoveReservationObserver());

    }

    void trackEvent(String screenName,
                    String eventCat,
                    String eventAction,
                    String labelName,
                    String eventName) {
        userUseCase.get(new EventPropertyObserver(screenName, eventCat, eventAction, labelName, eventName));
    }

    /**********************************************************/

    private final class GetUser extends BaseObserver<User> {

        private String contestCode;

        GetUser(String contestCode) {
            this.contestCode = contestCode;
        }

        @Override
        public void onNext(User user) {
            incentivesUseCase.getContest(contestCode, new GetContestObserver(user));
        }
    }

    private final class GetContestObserver extends BaseObserver<Concurso> {

        private String campaingCurrent;
        private String countryMoneySymbol;
        private String countryISO;

        private GetContestObserver(User user) {
            this.campaingCurrent = user.getCampaing();
            this.countryMoneySymbol = user.getCountryMoneySymbol();
            this.countryISO = user.getCountryISO();
        }

        @Override
        public void onNext(Concurso data) {

            if (null == newProgramView) return;

            ConcursoModel model = concursoModelDataMapper.transform(data);
            newProgramView.initializeAdapter(countryISO);
            newProgramView.showContest(model, campaingCurrent, countryMoneySymbol);
            newProgramView.hideLoading();
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == newProgramView) return;
            newProgramView.initializeAdapter(countryISO);
            newProgramView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                newProgramView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                newProgramView.onError(ErrorFactory.INSTANCE.create(exception));
            }
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            newProgramView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserCuponObserver extends BaseObserver<User> {

        private final OrderModel orderModel;

        private UserCuponObserver(OrderModel orderModel) {
            this.orderModel = orderModel;
        }

        @Override
        public void onNext(User user) {

            orderModel.setCampania(Integer.parseInt(user.getCampaing()));
            orderModel.setAceptacionConsultoraDA(user.getConsultantAcceptDA());

            orderUseCase.addOrder(orderModelDataMapper.transform(orderModel), new AddOrderObserver());
        }
    }

    private final class AddOrderObserver extends BaseObserver<BasicDto<Boolean>> {

        @Override
        public void onNext(BasicDto<Boolean> result) {

            if (null == newProgramView) return;

            newProgramView.hideLoading();
            if (null != result) {

                if (result.getCode().equals(OrderResultCode.OK))
                    newProgramView.orderAdded();
                else
                    newProgramView.onOrderError(dtoModelMapper.transform(result));
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == newProgramView) return;

            newProgramView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                newProgramView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                newProgramView.onError(ErrorFactory.INSTANCE.create(exception));
            }
        }
    }


    private final class UserRemoveReservationObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {

            OrderModel model = new OrderModel();
            model.setAceptacionConsultoraDA(user.getConsultantAcceptDA());
            model.setCampania(Integer.parseInt(user.getCampaing()));

            orderUseCase.removeReservation(orderModelDataMapper.transform(model), new RemoveReservationObserver());
        }
    }

    private final class RemoveReservationObserver extends BaseObserver<BasicDto<Boolean>> {

        @Override
        public void onNext(BasicDto<Boolean> result) {

            if (null == newProgramView) return;

            newProgramView.hideLoading();
            if (null != result) {

                if (result.getCode().equals(OrderResultCode.OK))
                    newProgramView.removeReservation();
                else
                    newProgramView.onOrderError(dtoModelMapper.transform(result));
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == newProgramView) return;

            newProgramView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                newProgramView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                newProgramView.onError(ErrorFactory.INSTANCE.create(exception));
            }
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
                loginModelDataMapper.transform(user));
        }
    }
}

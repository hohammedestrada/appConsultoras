package biz.belcorp.consultoras.feature.contest.constancy;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel;
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Concurso;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.IncentivesUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.exception.ErrorFactory;

@PerActivity
class PerConstancyPresenter implements Presenter<PerConstancyView>{

    private final UserUseCase userUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    private final IncentivesUseCase incentivesUseCase;
    private final ConcursoModelDataMapper concursoModelDataMapper;

    private PerConstancyView perConstancyView;


    @Inject
    PerConstancyPresenter(UserUseCase userUseCase,
                          LoginModelDataMapper loginModelDataMapper,
                          IncentivesUseCase incentivesUseCase,
                          ConcursoModelDataMapper concursoModelDataMapper){
        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
        this.incentivesUseCase = incentivesUseCase;
        this.concursoModelDataMapper = concursoModelDataMapper;
    }

    @Override
    public void attachView(@NonNull PerConstancyView view) {
        if (view instanceof PerConstancyFragment) {
            perConstancyView = view;
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
        this.perConstancyView = null;
    }

    /******************************************************/

    public void get(String contestCode) {
        perConstancyView.showLoading();
        userUseCase.get(new GetUser(contestCode));
    }

    public void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
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

            if (null == perConstancyView) return;

            ConcursoModel model = concursoModelDataMapper.transform(data);
            perConstancyView.showContest(model, campaingCurrent, countryMoneySymbol);
            perConstancyView.initializeAdapter(countryISO);
            perConstancyView.hideLoading();
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == perConstancyView) return;
            perConstancyView.initializeAdapter(countryISO);
            perConstancyView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                perConstancyView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                perConstancyView.onError(ErrorFactory.INSTANCE.create(exception));
            }
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            perConstancyView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

}

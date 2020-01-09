package biz.belcorp.consultoras.feature.terms;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;

/**
 * @author andres.escobar on 4/08/2017.
 */
@PerActivity
public class TermsPresenter implements Presenter<TermsView> {

    private TermsView termsView;
    private AccountUseCase accountUseCase;

    private final UserUseCase userUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    TermsPresenter(AccountUseCase accountUseCase,
                   UserUseCase userUseCase,
                   LoginModelDataMapper loginModelDataMapper) {
        this.accountUseCase = accountUseCase;
        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull TermsView view) {
        termsView = view;
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
        this.termsView = null;
    }

    void acceptTerms(Boolean acceptedTerms, Boolean acceptedPrivacy) {
        termsView.showLoading();
        accountUseCase.terms(acceptedTerms, acceptedPrivacy, new TermsObserver());
    }

    void getPdfTermsUrl() {
        accountUseCase.getPdfTermsUrl(new GetPdfObserver());
    }

    void getPdfPrivacyUrl() {
        this.accountUseCase.getPdfPrivacyUrl(new GetPdfObserver());
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    private final class TermsObserver extends BaseObserver<Boolean> {

        @Override
        public void onNext(Boolean result) {
            if (termsView == null) return;
            termsView.hideLoading();
            termsView.onTermsAccepted();
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (termsView == null) return;
            termsView.hideLoading();
            termsView.onError(exception);
        }
    }

    private final class GetPdfObserver extends BaseObserver<String> {

        @Override
        public void onNext(String result) {
            termsView.onUrlTermsGot(result);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            termsView.onError(exception);
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            termsView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }
}

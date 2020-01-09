package biz.belcorp.consultoras.feature.home.tracking;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.tracking.TrackingModel;
import biz.belcorp.consultoras.common.model.tracking.TrackingModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Tracking;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.TrackingUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;

@PerActivity
public class TrackingPresenter implements Presenter<TrackingView> {

    private TrackingView trackingView;
    private final TrackingUseCase trackingUseCase;
    private final UserUseCase userUseCase;
    private final LoginModelDataMapper loginModelDataMapper;
    private final TrackingModelDataMapper trackingModelDataMapper;

    @Inject
    TrackingPresenter(TrackingUseCase trackingUseCase, UserUseCase userUseCase, LoginModelDataMapper loginModelDataMapper, TrackingModelDataMapper trackingModelDataMapper) {
        this.trackingUseCase = trackingUseCase;
        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
        this.trackingModelDataMapper = trackingModelDataMapper;
    }

    @Override
    public void attachView(@NonNull TrackingView view) {
        trackingView = view;
    }

    @Override
    public void resume() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void pause() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destroy() {
        this.trackingUseCase.dispose();
        this.userUseCase.dispose();
        this.trackingView = null;
    }

    /**********************************************************/

    public void data(int top) {
        trackingView.showLoading();
        trackingUseCase.get(top, new GetTracking());
    }

    public void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    /**********************************************************/

    private final class GetTracking extends BaseObserver<Collection<Tracking>> {

        @Override
        public void onNext(Collection<Tracking> tracking) {
            if (trackingView == null) return;

            List<TrackingModel> trackingModels = new ArrayList<>(trackingModelDataMapper.transform(tracking));
            Collections.sort(trackingModels, new Comparator<TrackingModel>() {
                @Override
                public int compare(TrackingModel o1, TrackingModel o2) {
                    return o1.getCampania().compareTo(o2.getCampania());
                }
            });

            trackingView.hideLoading();
            trackingView.showTracking(trackingModels);
        }

        @Override
        public void onError(Throwable exception) {
            if (trackingView == null) return;
            trackingView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                trackingView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                trackingView.onError(exception);
            }
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            trackingView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            trackingView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }
}

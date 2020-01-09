package biz.belcorp.consultoras.sync;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sap.cec.marketing.ymkt.mobile.configuration.Boot;
import com.sap.cec.marketing.ymkt.mobile.tracker.model.NotificationViewed;
import java.util.List;
import javax.inject.Inject;
import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.fcm.HybrisManager;
import biz.belcorp.consultoras.di.PerService;
import biz.belcorp.consultoras.domain.entity.HybrisData;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.SyncUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.anotation.UserType;
import biz.belcorp.library.log.BelcorpLogger;

@PerService
class SyncPresenter implements Presenter<SyncI> {

    private SyncI syncI;
    private final SyncUseCase syncUseCase;
    private final UserUseCase userUseCase;
    private Context context;

    public static final String ERROR = "Error";

    @Inject
    SyncPresenter(Context context, SyncUseCase syncUseCase, UserUseCase userUseCase) {
        this.context = context;
        this.syncUseCase = syncUseCase;
        this.userUseCase = userUseCase;
    }

    @Override
    public void attachView(@NonNull SyncI view) {
        this.syncI = view;
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
        // EMPTY
    }

    /******************************************************/

    void initSync() {
        syncUseCase.checkStatusSync(new CheckStatusSyncObserver());
    }

    private void updateStatusFalse() {
        Thread thread = new Thread(() -> {
            try {
                long millis = (long) (1000);
                Thread.sleep(millis);
                syncUseCase.sincroPayment();
                syncUseCase.updateStatusSync(false, new UpdateStatusSyncObserver());
            } catch (Exception e) {
                BelcorpLogger.d(e);
            }
        });

        thread.start();
    }

    /******************************************************/

    private final class UserObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            if (null != user && null != user.getUserType() &&
                user.getUserType() == UserType.CONSULTORA) {
                syncUseCase.existsClientsToSync(new ExistClientSyncObserver());
            } else {
                updateStatusFalse();
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.d(ERROR, exception);
            updateStatusFalse();
        }
    }

    private final class CheckStatusSyncObserver extends BaseObserver<Boolean> {

        @Override
        public void onNext(Boolean result) {
            if (!result)
                userUseCase.get(new UserObserver());

        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.d(ERROR, exception);
            updateStatusFalse();
        }
    }

    private final class UpdateStatusSyncObserver extends BaseObserver<Boolean> {
        @Override
        public void onNext(Boolean result) {
            BelcorpLogger.d("UpdateStatusSyncObserver - " + result + " - ", result);
        }
    }

    private final class ExistClientSyncObserver extends BaseObserver<Boolean> {

        @Override
        public void onNext(Boolean result) {
            if (result) {
                syncI.startSync();
                syncUseCase.syncClientsData(new SyncClientObserver());
            } else {
                syncUseCase.existsAnotationsToSync(new ExistOnlyAnotationsSyncObserver());
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.d(ERROR, exception);
            updateStatusFalse();
        }
    }

    private final class SyncClientObserver extends BaseObserver<Boolean> {

        @Override
        public void onNext(Boolean result) {
            syncUseCase.existsAnotationsToSync(new ExistAnotationsSyncObserver());
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.d(ERROR, exception);
            syncI.stopSync();
            updateStatusFalse();
        }
    }

    private final class ExistAnotationsSyncObserver extends BaseObserver<Boolean> {

        @Override
        public void onNext(Boolean result) {
            if (result) {
                syncUseCase.syncAnnotationsData(new SyncAnotationsObserver());
            } else {
                syncI.stopSync();
                syncUseCase.getNotificationsHybris(new SyncHybrisObserver());
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.d(ERROR, exception);
            syncI.stopSync();
            updateStatusFalse();
        }
    }

    private final class ExistOnlyAnotationsSyncObserver extends BaseObserver<Boolean> {

        @Override
        public void onNext(Boolean result) {
            if (result) {
                syncI.startSync();
                syncUseCase.syncAnnotationsData(new SyncAnotationsObserver());
            } else {
                syncUseCase.getNotificationsHybris(new SyncHybrisObserver());
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.d(ERROR, exception);
            updateStatusFalse();
        }
    }

    private final class SyncAnotationsObserver extends BaseObserver<Boolean> {

        @Override
        public void onNext(Boolean result) {
            syncI.stopSync();
            syncUseCase.getNotificationsHybris(new SyncHybrisObserver());
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            syncI.stopSync();
            updateStatusFalse();
            BelcorpLogger.d(ERROR, exception);
        }
    }

    private final class SyncHybrisObserver extends BaseObserver<List<HybrisData>> {

        private int count;

        @Override
        public void onNext(List<HybrisData> list) {

            if (null != list && !list.isEmpty()) {
                syncUseCase.updateHybrisData(list, new SyncHybrisUpdateObserver());
                try {
                    Boot.setupHybrisMarketing(HybrisManager.getInstance().getConfiguration(context));

                    for (HybrisData data : list) {
                        final String url = data.getTrackingURL();

                        Thread thread = new Thread(() -> {
                            try {
                                long millis = (long) (5000 * count);
                                Thread.sleep(millis);
                                NotificationViewed notificationViewed = new NotificationViewed(
                                    HybrisManager.getInstance().getConfiguration(context), context);
                                notificationViewed.setTrackingURL(url);
                                notificationViewed.execute();
                            } catch (Exception e) {
                                BelcorpLogger.d(e);
                            }
                        });

                        thread.start();
                        count += 1;
                    }
                } catch (Exception e) {
                    BelcorpLogger.d(e);
                }
            }
            updateStatusFalse();
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            updateStatusFalse();
            BelcorpLogger.d(ERROR, exception);
        }
    }

    private final class SyncHybrisUpdateObserver extends BaseObserver<Boolean> { }
}

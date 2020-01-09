package biz.belcorp.consultoras.feature.notifications.redirect;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sap.cec.marketing.ymkt.mobile.tracker.model.NotificationViewed;
import javax.inject.Inject;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.fcm.HybrisManager;
import biz.belcorp.consultoras.domain.entity.Auth;
import biz.belcorp.consultoras.domain.entity.HybrisData;
import biz.belcorp.consultoras.feature.notifications.di.NotificationsComponent;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.NotificationCode;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.NetworkUtil;

public class NotificationsFragment extends BaseFragment implements NotificationsView {

    @Inject
    NotificationsPresenter presenter;

    private OnNotificationListener listener;

    /**********************************************************/

    public NotificationsFragment() {
        super();
    }

    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(NotificationsComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);

        String notificationCode = "";

        //hybris
        Bundle args = getArguments();
        if (null != args) {
            notificationCode = args.getString("notification_code", "");
            String trackingURL = args.getString(GlobalConstant.TRACKING_URL);
            if (null != trackingURL && !trackingURL.isEmpty())
                if (NetworkUtil.isThereInternetConnection(getActivity())){
                    try {
                        NotificationViewed notificationViewed = new NotificationViewed(
                            HybrisManager.getInstance().getConfiguration(getContext()), getContext());
                        notificationViewed.setTrackingURL(trackingURL);
                        notificationViewed.execute(responseHandler -> {
                            if (responseHandler.getResponseStatus() != 200) {
                                saveHybrisOffline(trackingURL);
                            }
                        });
                    } catch (Exception e) {
                        BelcorpLogger.d(e);
                        saveHybrisOffline(trackingURL);
                    }
                }
                else {
                    saveHybrisOffline(trackingURL);
                }
        }

        boolean resetAnim50 = false;
        boolean isOrder = false;
        if (notificationCode.equals(NotificationCode.HOME_ANIM_50)) resetAnim50 = true;
        if (notificationCode.equals(NotificationCode.ORDER)) isOrder = true;
        if (notificationCode.equals(NotificationCode.OFFERS)) presenter.updateSchedule();

        presenter.init(resetAnim50, isOrder);
    }

    /***********************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNotificationListener) {
            this.listener = (OnNotificationListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return new View(getContext());
    }

    @Override
    public Context context() {
        FragmentActivity activity = getActivity();
        if (activity != null)
            return activity.getApplicationContext();
        else
            return null;
    }

    /***********************************************************/

    @Override
    public void initScreen(Auth auth, int optionOrder) {
        if (null != auth) {
            listener.initScreen(auth.isLogged(), optionOrder);
        } else
            listener.initScreen(false, optionOrder);
    }

    private void saveHybrisOffline(String trackingURL) {
        HybrisData data = new HybrisData();
        data.setTrackingURL(trackingURL);
        if (null != presenter) presenter.saveHybrisData(data);
    }

    /*********************************************************************/

    interface OnNotificationListener {

        void initScreen(boolean isLogged, int optionOrder);

    }

}

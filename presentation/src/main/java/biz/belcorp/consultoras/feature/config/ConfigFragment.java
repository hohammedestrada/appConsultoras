package biz.belcorp.consultoras.feature.config;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.dialog.CustomDialog;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.config.ConfigAppModel;
import biz.belcorp.consultoras.feature.config.di.ConfigComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.analytics.annotation.AnalyticEvent;
import biz.belcorp.library.analytics.annotation.AnalyticScreen;
import biz.belcorp.library.log.BelcorpLogger;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

@AnalyticScreen(name = "ConfigScreen")
public class ConfigFragment extends BaseFragment implements ConfigView {

    @Inject
    ConfigPresenter presenter;

    @BindView(R.id.tvw_data_mobile_message)
    TextView tvwDataMobileMessage;

    private CustomDialog dialogClearHistory;

    private boolean isSoundON = false;
    private boolean isDataMobileON = false;
    private boolean isNotificationON = false;


    private View.OnClickListener onDgClearHistoryYesClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dialogClearHistory.isShowing()) dialogClearHistory.dismiss();
        }
    };

    private View.OnClickListener onDgClearHistoryNotClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dialogClearHistory.isShowing()) dialogClearHistory.dismiss();
        }
    };

    /**********************************************************/

    public ConfigFragment() {
        super();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(ConfigComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);
        initControls();
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_config, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.initScreenTrack();
    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {

        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_CONFIGURATION);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    /**********************************************************/


    @Override
    public Context context() {
        FragmentActivity activity = getActivity();
        if (activity != null)
            return activity.getApplicationContext();
        else
            return null;
    }

    /**********************************************************/

    private void initControls() {

        try {
            dialogClearHistory = new CustomDialog(getContext());
            dialogClearHistory.setIconDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_clear_search));
            dialogClearHistory.setMessage(R.string.config_dg_clear_history_message);
            dialogClearHistory.setButtonAction1Text(R.string.config_dg_clear_history);
            dialogClearHistory.setButtonAction1Listener(onDgClearHistoryYesClickListener);
            dialogClearHistory.setButtonAction2Text(R.string.config_dg_cancel);
            dialogClearHistory.setButtonAction2Listener(onDgClearHistoryNotClickListener);
        } catch (IllegalStateException e) {
            BelcorpLogger.w("initControls", e);
        }
    }

    @Override
    public void saved(Boolean result) {
        // EMPTY
    }

    /**********************************************************/

    @OnCheckedChanged(R.id.swc_data_mobile)
    @AnalyticEvent(action = "OnChangeDataMobileClick", category = "Click")
    public void onChangeDataMobileON(boolean isChecked) {

        if (isDataMobileON != isChecked) {
            isDataMobileON = isChecked;

            if (isChecked) {
                tvwDataMobileMessage.setText(R.string.config_wifi_datos);
            } else {
                tvwDataMobileMessage.setText(R.string.config_wifi);
            }

            ConfigAppModel configAppModel = new ConfigAppModel();
            configAppModel.setConnectivityType(isChecked ? 1 : 0);

            presenter.save(configAppModel);

        }

    }

    @OnCheckedChanged(R.id.swc_notification)
    @AnalyticEvent(action = "OnChangeNotificationClick", category = "Click")
    public void onChangeNotificationON(boolean isChecked) {

        if (isNotificationON != isChecked) {
            isNotificationON = isChecked;

            ConfigAppModel configAppModel = new ConfigAppModel();
            configAppModel.setNotification(isChecked);

            presenter.save(configAppModel);

        }

    }

    @OnCheckedChanged(R.id.swc_sound)
    @AnalyticEvent(action = "OnChangeSoundClick", category = "Click")
    public void onChangeSoundON(boolean isChecked) {
        if (isSoundON != isChecked) {
            isSoundON = isChecked;

            ConfigAppModel configAppModel = new ConfigAppModel();
            configAppModel.setSonido(isChecked);

            presenter.save(configAppModel);
        }
    }

    @OnClick(R.id.tvw_clear_history)
    @AnalyticEvent(action = "OnClickClearHistoryClick", category = "Click")
    public void onClickClearHistory() {
        dialogClearHistory.show();
    }
}

package biz.belcorp.consultoras.feature.legal;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.feature.legal.di.LegalComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.mobile.analytics.core.Analytics;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LegalFragment extends BaseFragment implements LegalView {

    @Inject
    LegalPresenter presenter;

    @BindView(R.id.tvw_legal_subtitle)
    TextView tvwLegalSubtitle;
    @BindView(R.id.tvw_legal_description)
    TextView tvwLegalDescription;
    @BindView(R.id.tvw_legal_check)
    TextView tvwTerms;
    @BindView(R.id.tvw_privacy_check)
    TextView tvwPrivacy;
    @BindView(R.id.chk_privacy_accept)
    CheckBox chkPrivacy;
    @BindView(R.id.btn_legal_aceptar)
    Button btnLegal;

    Unbinder unbinder;

    private OnLegalListener listener;

    private static final String RETURN = "0";
    private static final String ACCEPT_PRIVACY = "1";

    @Override
    public Context context() {
        FragmentActivity activity = getActivity();
        if (activity != null)
            return activity.getApplicationContext();
        else
            return null;
    }

    /**********************************************************/

    public LegalFragment() {
        super();
    }

    public static LegalFragment newInstance() {
        return new LegalFragment();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(LegalComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);
        presenter.load();
    }

    /***********************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLegalListener) {
            this.listener = (OnLegalListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_legal, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    public String applicationLabel(Context con, ApplicationInfo info) {
        PackageManager p = con.getPackageManager();
        return p.getApplicationLabel(info).toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null) presenter.initScreenTrack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void init() {
        String title = getString(R.string.legal_subtitle) + " " + applicationLabel(getActivity(), getActivity().getApplicationInfo());
        tvwLegalSubtitle.setText(title);
        btnLegal.setTag(ACCEPT_PRIVACY);

        chkPrivacy.setOnCheckedChangeListener((compoundButton, b) -> {
            btnLegal.setText(R.string.legal_accept);
            btnLegal.setTag(ACCEPT_PRIVACY);
        });
    }

    /*********************************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_TEMS);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }


    @Override
    public void trackBackPressed(LoginModel model) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_TEMS);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(model);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);

        if (listener != null) {
            listener.onBackFromFragment();
        }
    }

    @Override
    public void setData(LoginModel model) {
        if (null != model) {
            chkPrivacy.setChecked(model.isAceptaPoliticaPrivacidad());

            if (model.isAceptaPoliticaPrivacidad()) {
                btnLegal.setText(R.string.legal_back);
                btnLegal.setTag(RETURN);
            }
        }
    }

    @Override
    public void onPrivacyAccepted() {

        if(chkPrivacy.isChecked()){
            Toast.makeText(context(), R.string.legal_privacy_check_message_ok, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context(), R.string.legal_privacy_check_message_cancel, Toast.LENGTH_SHORT).show();
        }

        if (listener != null) {
            listener.onBackFromFragment();
        }

    }

    @Override
    public void onError(Throwable throwable) {
        processError(throwable);
    }

    @Override
    public void onUrlLegalGot(String url) {
        openPdfTerms(url);
    }

    /*********************************************************************/

    @OnClick({R.id.tvw_legal_check, R.id.tvw_privacy_check, R.id.btn_legal_aceptar})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tvw_legal_check:
                presenter.getPdfTermsUrl();
                break;
            case R.id.tvw_privacy_check:
                presenter.getPdfPrivacyUrl();
                break;
            case R.id.btn_legal_aceptar:
                if (btnLegal.getTag().toString().equals(RETURN))
                    trackBackPressed();
                else
                    presenter.acceptTerms(chkPrivacy.isChecked());
                break;
            default:
                break;
        }
    }

    private void openPdfTerms(String urlTerminos) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlTerminos));
            startActivity(browserIntent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), R.string.legal_activity_not_found, Toast.LENGTH_SHORT).show();
            BelcorpLogger.w("openPdfTerms", ex);
        }
    }

    public void trackBackPressed() {
        presenter.trackBackPressed();
    }

    /*********************************************************************/

    interface OnLegalListener {

        void onBackFromFragment();

    }

    @Override
    public void stopSDK() {
        Analytics.checkOptIn(chkPrivacy.isChecked());
    }
}

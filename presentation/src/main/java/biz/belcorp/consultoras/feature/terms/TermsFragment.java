package biz.belcorp.consultoras.feature.terms;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.dialog.MessageDialog;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.feature.terms.di.TermsComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.mobile.analytics.core.Analytics;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author andres.escobar on 3/08/2017.
 */
@RuntimePermissions
public class TermsFragment extends BaseFragment implements TermsView {

    @Inject
    TermsPresenter presenter;
    @BindView(R.id.tvw_terms_subtitle)
    TextView tvwTermsSubtitle;
    @BindView(R.id.tvw_terms_description)
    TextView tvwTermsDescription;
    @BindView(R.id.tvw_terms_check)
    TextView tvwTermsCheck;
    @BindView(R.id.chk_terms_accept)
    CheckBox chkTermsAccept;
    @BindView(R.id.tvw_privacy_check)
    TextView tvwPrivacyCheck;
    @BindView(R.id.chk_privacy_accept)
    CheckBox chkPrivacyAccept;

    Unbinder unbinder;

    private Listener listener;

    @Override
    public Context context() {
        return null;
    }

    /**********************************************************/

    public TermsFragment() {
        super();
    }

    public static TermsFragment newInstance() {
        return new TermsFragment();
    }

    /**********************************************************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(TermsComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);
    }

    /**********************************************************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            this.listener = (Listener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvwTermsSubtitle.setText(tvwTermsSubtitle.getText().toString() + " " + applicationLabel(getActivity(), getActivity().getApplicationInfo()));
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


    /**********************************************************/

    @Override
    public void onTermsAccepted() {
        if (listener != null) {

            Analytics.checkOptIn(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(chkPrivacyAccept.isChecked()){
                    TermsFragmentPermissionsDispatcher.checkSDKPermissionWithPermissionCheck(this);
                }else{
                    listener.onHome();
                }
            }else{
                listener.onHome();
            }

        }
    }

    @Override
    @NeedsPermission({Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION})
    public void checkSDKPermission() {
        if (!isAdded()) return;
        listener.onHome();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TermsFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION})
    void showDeniedForSDKPermission() {
        if (!isAdded()) return;
        listener.onHome();
    }

    @Override
    public void onError(Throwable exception) {
        if (!isVisible()) return;

        showError(getString(R.string.terms_error_title), getString(R.string.terms_error_server));
    }

    @Override
    public void onUrlTermsGot(String urlTerminos) {
        openPdfTerms(urlTerminos);
    }

    @OnClick(R.id.btn_terms_aceptar)
    public void onBtnTermsAceptarClicked() {
        if (chkTermsAccept.isChecked()) {
            presenter.acceptTerms(true, chkPrivacyAccept.isChecked());
        } else {
            new MessageDialog()
                .setIcon(R.drawable.ic_alerta, 0)
                .setStringTitle(R.string.terms_error_title)
                .setStringMessage(R.string.terms_error_accept)
                .setStringAceptar(R.string.button_aceptar)
                .showCancel(false)
                .showIcon(true)
                .showClose(false)
                .setListener(aceptarListener)
                .show(getFragmentManager(), "modalError");
        }
    }

    @OnClick({R.id.tvw_terms_check, R.id.tvw_privacy_check})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tvw_terms_check:
                presenter.getPdfTermsUrl();
                break;
            case R.id.tvw_privacy_check:
                presenter.getPdfPrivacyUrl();
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
            Toast.makeText(getActivity(), R.string.terms_activity_not_found, Toast.LENGTH_SHORT).show();
            BelcorpLogger.w("openPdfTerms", ex);
        }
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

    /*********************************************************************/

    public interface Listener {

        void onHome();
        void onTerms();
    }

    /**********************************************************/

    private final MessageDialog.MessageDialogListener aceptarListener = new MessageDialog.MessageDialogListener() {
        @Override
        public void aceptar() {
            // EMPTY
        }

        @Override
        public void cancelar() {
            // EMPTY
        }
    };
}

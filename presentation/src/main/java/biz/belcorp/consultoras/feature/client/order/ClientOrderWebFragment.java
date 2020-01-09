package biz.belcorp.consultoras.feature.client.order;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.dialog.MessageDialog;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DeviceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientOrderWebFragment extends BaseFragment implements ClientOrderView {

    @Inject
    ClientOrderPresenter presenter;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.rlt_content_error)
    RelativeLayout rltContentError;

    private boolean isWebBackLocked = false;
    private boolean isFirstTime = true;
    private boolean hasError = false;

    /**********************************************************/

    public ClientOrderWebFragment() {
        super();
    }

    public static ClientOrderWebFragment newInstance() {
        return new ClientOrderWebFragment();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(ClientComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);

        if (savedInstanceState == null) {
            int clientId = getActivity().getIntent().getIntExtra(GlobalConstant.CLIENTE_ID, 0);

            String deviceId = DeviceUtil.getId(getActivity());
            presenter.load(clientId, deviceId);
        }
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_client_order_web, container, false);
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
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_CLIENT_ORDERS);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_CLIENT_ORDERS);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);
    }

    public void trackBackPressed() {
        presenter.trackBackPressed();
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

    @Override
    public void showPostulant() {
        try {
            MessageDialog messageDialog = new MessageDialog();
            messageDialog.setCancelable(false);
            messageDialog
                .setIcon(R.drawable.ic_alerta, 0)
                .setStringTitle(R.string.error_postulant_title)
                .setStringMessage(R.string.error_postulant_message)
                .setStringAceptar(R.string.button_continuar)
                .showCancel(false)
                .showIcon(true)
                .showClose(false)
                .show(getChildFragmentManager(), "modalAceptar");
        } catch (Exception e) {
            BelcorpLogger.w("showPostulant", e);
        }
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void showUrl(final String url) {

        if (!"".equals(url)) {

            BelcorpLogger.d("TAG URL ", url);

            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.setVerticalScrollBarEnabled(true);
            webView.setFocusable(true);

            if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }

            webView.setWebViewClient(new WebViewClient() {

                /**
                 * @deprecated
                 */
                @Override
                @Deprecated
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return false;
                }

                /**
                 * @deprecated
                 */
                @Override
                @Deprecated
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.w("WebActivity", "Error loading page " + description);
                    hasError = true;
                }

                @Override
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);

                    if (request.isForMainFrame()) {
                        hasError = true;
                        webView.setVisibility(View.GONE);
                        rltContentError.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    presenter.hideLoading();

                    if (isFirstTime || !url.contains("/Login")) {

                        isFirstTime = false;
                        isWebBackLocked = url.endsWith("/Mobile/Pedido/Validado");

                        if (hasError && isVisible()) {
                            hasError = false;
                            showNetworkError();
                            webView.setVisibility(View.GONE);
                            rltContentError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        showExpiredSesion();
                    }
                }
            });

            webView.loadUrl(url);
        } else {
            presenter.hideLoading();
            webView.setVisibility(View.GONE);
            rltContentError.setVisibility(View.VISIBLE);
        }
    }

    public boolean onBackWebView() {
        if (!isWebBackLocked && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else
            return false;
    }

    private void showExpiredSesion() {
        try {
            MessageDialog messageDialog = new MessageDialog();
            messageDialog.setCancelable(false);
            messageDialog
                .setIcon(R.drawable.ic_alerta, 0)
                .setStringTitle(R.string.error_expire_session_title)
                .setStringMessage(R.string.error_expire_session_message)
                .setStringAceptar(R.string.button_aceptar)
                .showCancel(false)
                .showIcon(true)
                .showClose(false)
                .setListener(retryListener).show(getChildFragmentManager(), "modalAceptar");
        } catch (Exception e) {
            BelcorpLogger.w("showExpiredSesion", e);
        }
    }

    /**********************************************************/

    private final MessageDialog.MessageDialogListener retryListener = new MessageDialog.MessageDialogListener() {
        @Override
        public void aceptar() {
            Intent resultIntent = new Intent();
            getActivity().setResult(Activity.RESULT_OK, resultIntent);
            getActivity().finish();
        }

        @Override
        public void cancelar() {
            //EMPTY
        }
    };
}

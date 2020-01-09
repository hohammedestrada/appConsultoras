package biz.belcorp.consultoras.feature.embedded.gpr;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.dialog.MessageDialog;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.feature.embedded.gpr.di.OrderWebComponent;
import biz.belcorp.consultoras.feature.search.list.SearchListActivity;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DeviceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public class OrderWebFragment extends BaseFragment implements OrderWebView {

    @Inject
    OrderWebPresenter presenter;

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.rltContentError)
    RelativeLayout rltContentError;

    Unbinder unbinder;
    Listener listener;

    private boolean isSessionExpired = false;
    private boolean isWebBackLocked = false;
    private boolean isFirstTime = true;
    private boolean hasGoneBack = false;
    private boolean hasError = false;

    private Handler refreshDataHandler;
    private Runnable refreshDataRunnable;

    private LoginModel loginModel;
    private String deviceId;

    /**********************************************************/

    public OrderWebFragment() {
        super();
    }

    public static OrderWebFragment newInstance() {
        return new OrderWebFragment();
    }


    /**********************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(OrderWebComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);
        deviceId = DeviceUtil.getId(context());
        presenter.load(deviceId);
    }

    /**********************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (refreshDataHandler == null) initHandler();

        refreshDataHandler.removeCallbacks(refreshDataRunnable);
        refreshDataHandler.postDelayed(refreshDataRunnable, 200);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != unbinder) unbinder.unbind();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != presenter) presenter.destroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SearchListActivity.RESULT && resultCode == Activity.RESULT_OK ) {
            webView.reload();
        }
    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel model) {
        loginModel = model;
        Tracker.trackScreen(GlobalConstant.SCREEN_ORDERS_GPR, model);
    }


    @Override
    public void trackBack(LoginModel model) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_ORDERS_GPR, model);

        if (listener != null) {
            listener.onBackFromFragment();
        }
    }

    public void trackBackPressed() {
        if (presenter != null)
            presenter.trackBack();
    }

    public void trackEvent(String category, String action, String label, String eventName) {
        Tracker.trackEvent(GlobalConstant.SCREEN_ORDERS_GPR,
            category, action, label, eventName, loginModel);
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
    public void showUrl(final String url) {

        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setVerticalScrollBarEnabled(true);
        webView.setFocusable(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration             
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration             
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
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
                BelcorpLogger.d("WebActivity", "Error loading page " + description);
                hasError = true;
            }

            @Override
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);

                if (request.isForMainFrame()) {
                    hasError = true;
                    if (isVisible()) {
                        webView.setVisibility(View.GONE);
                        rltContentError.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();

                if (isFirstTime || !url.contains("/Login")) {

                    isFirstTime = false;
                    isWebBackLocked = url.endsWith("/Pedido/Validado");

                    if (hasError && isVisible()) {
                        hasError = false;
                        showNetworkError();
                        webView.setVisibility(View.GONE);
                        rltContentError.setVisibility(View.VISIBLE);
                    } else if (hasGoneBack && isVisible()) {
                        hasGoneBack = false;
                        webView.reload();
                    }
                } else if (isVisible()) {
                    isSessionExpired = true;
                    showExpiredSesion();
                }
            }
        });
        webView.loadUrl(url);
    }

    @Override
    public void showLoading() {
        if (isVisible()) {
            webView.setVisibility(View.GONE);
        }
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        if (isVisible())
            webView.setVisibility(View.VISIBLE);
        super.hideLoading();
    }

    @Override
    public void showError() {
        if (isVisible()) {
            webView.setVisibility(View.GONE);
            rltContentError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setCampaign(String campaign) {
        if (listener != null) {
            listener.setCampaign(campaign);
        }
    }

    public boolean onBackWebView() {
        if (!isSessionExpired && !isWebBackLocked && webView.canGoBack()) {
            hasGoneBack = true;
            webView.goBack();
            return true;
        } else
            return false;
    }

    @Override
    public void showSearchOption() {
        ((OrderWebActivity)getActivity()).showSearchOption();
    }

    /*****************************************************************/

    private void initHandler() {
        refreshDataHandler = new Handler();
        refreshDataRunnable = () -> {
            if (presenter != null) presenter.initScreenTrack();
        };
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
            BelcorpLogger.w("showExpiredSession", e);
        }
    }

    /**********************************************************/

    private final MessageDialog.MessageDialogListener retryListener = new MessageDialog.MessageDialogListener() {
        @Override
        public void aceptar() {
            getActivity().finish();
        }

        @Override
        public void cancelar() {
            // EMPTY
        }
    };

    /**********************************************************/

    interface Listener {
        void onBackFromFragment();
        void setCampaign(String campaign);
    }
}

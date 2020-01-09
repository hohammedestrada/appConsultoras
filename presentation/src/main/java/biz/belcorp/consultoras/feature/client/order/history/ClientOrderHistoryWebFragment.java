package biz.belcorp.consultoras.feature.client.order.history;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
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
import biz.belcorp.consultoras.domain.entity.Menu;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.MenuCodeTop;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DeviceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ClientOrderHistoryWebFragment extends BaseFragment implements ClientOrderHistoryView {

    @Inject
    ClientOrderHistoryPresenter presenter;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.rlt_content_error)
    RelativeLayout rltContentError;

    private Unbinder unbinder;
    private boolean hasError = false;
    private boolean isFirstTime = true;
    private int clientId;
    private int campaign;
    Listener listener;

    /**********************************************************/

    public ClientOrderHistoryWebFragment() {
        super();
    }

    public static ClientOrderHistoryWebFragment newInstance() {
        return new ClientOrderHistoryWebFragment();
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
             clientId = getActivity().getIntent().getExtras().getInt(GlobalConstant.CLIENTE_ID, 0);
             campaign = getActivity().getIntent().getExtras().getInt(GlobalConstant.CAMPAIGN_KEY, 0);
             String deviceId = DeviceUtil.getId(getActivity());
             presenter.getMenuActive(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE);
             presenter.load(clientId, campaign, deviceId);
        }
    }

    @Override
    public void onGetMenu(Menu menu) {
        listener.onGetMenu(menu);
    }

    public void refresh(){
        showLoading();
        webView.clearView();
        webView.reload();
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_client_order_history_web, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.initScreenTrack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != unbinder) unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.destroy();
    }

    public void trackBackPressed() {
        if (presenter != null)
            presenter.trackBack();
    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_CLIENT_ORDERS_HISTORY, loginModel);
    }

    @Override
    public void trackBack(LoginModel loginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_CLIENT_ORDERS_HISTORY, loginModel);
        if (listener != null) listener.onBackFromFragment();
    }

    public void trackBack() {
        if (presenter != null) presenter.trackBack();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        }
    }

    @Override
    public void showLoading() {
        if (isVisible())
            webView.setVisibility(View.GONE);
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        if (isVisible())
            webView.setVisibility(View.VISIBLE);
        super.hideLoading();
    }

    /**********************************************************/

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void showURL(final String url) {

        if (TextUtils.isEmpty(url)) return;

        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setFocusable(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
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
                BelcorpLogger.w("WebActivity", "Error loading page " + description);
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

                hideLoading();

                if (!isVisible() || !hasError) return;

                if (isFirstTime || !url.contains("/Login")) {

                    hasError = false;
                    isFirstTime = false;

                    showNetworkError();
                    webView.setVisibility(View.GONE);
                    rltContentError.setVisibility(View.VISIBLE);
                } else {
                    showExpiredSesion();
                }
            }
        });

        webView.loadUrl(url);

    }

    @Override
    public void showError(Throwable throwable) {
        processError(throwable);
        webView.setVisibility(View.GONE);
        rltContentError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
        webView.setVisibility(View.GONE);
        rltContentError.setVisibility(View.VISIBLE);
    }

    /**********************************************************/

    public boolean onBackWebView() {
        if (webView.canGoBack()) {
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
            getActivity().finish();
        }

        @Override
        public void cancelar() {
            // EMPTY
        }
    };

    interface Listener {
        void onBackFromFragment();
        void onGetMenu(Menu menu);
    }

}

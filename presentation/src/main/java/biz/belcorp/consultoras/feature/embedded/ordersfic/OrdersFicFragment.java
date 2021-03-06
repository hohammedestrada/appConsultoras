package biz.belcorp.consultoras.feature.embedded.ordersfic;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
import biz.belcorp.consultoras.feature.embedded.ordersfic.di.OrdersFicComponent;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.MenuCodeTop;
import biz.belcorp.library.analytics.annotation.AnalyticScreen;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DeviceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@AnalyticScreen(name = "OrdersFic")
public class OrdersFicFragment extends BaseFragment implements OrdersFicView {

    @Inject
    OrdersFicPresenter presenter;

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.rlt_content_error)
    RelativeLayout rltContentError;

    Unbinder unbinder;
    Listener listener;

    private boolean isWebBackLocked = false;
    private boolean isFirstTime = true;
    private boolean hasGoneBack = false;
    private boolean hasError = false;

    private Handler refreshDataHandler;
    private Runnable refreshDataRunnable;

    private LoginModel loginModel;

    /**********************************************************/

    public OrdersFicFragment() {
        super();
    }

    public static OrdersFicFragment newInstance() {
        return new OrdersFicFragment();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() throws IllegalStateException {
        getComponent(OrdersFicComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);

        initHandler();

        if (savedInstanceState == null) {
            String deviceId = DeviceUtil.getId(getActivity());
            presenter.load(deviceId);
        }

        presenter.getMenuActive(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE);
    }

    @Override
    public void onGetMenu(Menu menu) {
        listener.onGetMenu(menu);
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orders_fic, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
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

    /**********************************************************/

    @Override
    public void showSearchOption() {
        ((OrdersFicActivity)getActivity()).showSearchOption();
    }

    @Override
    public void initScreenTrack(LoginModel model) {
        loginModel = model;
        Tracker.trackScreen(GlobalConstant.SCREEN_ORDERS_FIC, model);
    }

    @Override
    public void trackBack(LoginModel model) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_ORDERS_FIC, model);

        if (listener != null) {
            listener.onBackFromFragment();
        }
    }

    public void trackBackPressed() {
        if (presenter != null)
            presenter.trackBack();
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
    public void showUrl(final String url) {

        if (!isVisible()) return;

        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
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
                BelcorpLogger.w("WebActivity", "Error loading page " + description);
                hasError = true;
            }

            @Override
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);

                if (request.isForMainFrame()) {
                    hasError = true;

                    if (webView != null) {
                        webView.setVisibility(View.GONE);
                    }

                    if (rltContentError != null) {
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
                    isWebBackLocked = url.endsWith("/Mobile/Pedido/Validado");

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
                    if (hasError) {
                        hasError = false;
                        showNetworkError();
                        webView.setVisibility(View.GONE);
                        rltContentError.setVisibility(View.VISIBLE);
                    } else {
                        showExpiredSesion();
                    }
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

    public boolean onBackWebView() {
        if (!isWebBackLocked && webView.canGoBack()) {
            hasGoneBack = true;
            webView.goBack();
            return true;
        } else
            return false;
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
            BelcorpLogger.w("showExpiredSesion", e);
        }
    }

    public void trackEvent(String category, String action, String label, String eventName) {
        Tracker.trackEvent(GlobalConstant.SCREEN_ORDERS_FIC,
            category, action, label, eventName, loginModel);
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
        void onGetMenu(Menu menu);
    }
}

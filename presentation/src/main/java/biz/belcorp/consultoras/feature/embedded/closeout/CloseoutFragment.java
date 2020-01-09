package biz.belcorp.consultoras.feature.embedded.closeout;

import android.annotation.TargetApi;
import android.content.Context;
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
import biz.belcorp.consultoras.domain.entity.Menu;
import biz.belcorp.consultoras.feature.embedded.closeout.di.CloseoutComponent;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.MenuCode;
import biz.belcorp.consultoras.util.anotation.MenuCodeTop;
import biz.belcorp.consultoras.util.anotation.PageUrlType;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DeviceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class CloseoutFragment extends BaseFragment implements CloseoutView {

    @Inject
    CloseoutPresenter presenter;

    @BindView(R.id.wvw_closeout)
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

    private String page = PageUrlType.LIQUIDACIONWEB;

    /**********************************************************/

    public CloseoutFragment() {
        super();
    }

    public static CloseoutFragment newInstance() {
        return new CloseoutFragment();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(CloseoutComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);

        initBundle();
        initHandler();

        if (savedInstanceState == null) {
            String deviceId = DeviceUtil.getId(getActivity());
            // Obtener el titulo de la pagina
            presenter.getPageTitle(MenuCode.CLOSEOUT);
            presenter.load(deviceId, page);
            presenter.getMenuActive(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE);
        }
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_closeout, container, false);
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

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        this.loginModel = loginModel;
        Tracker.trackScreen(GlobalConstant.SCREEN_CLOSEOUT, loginModel);
    }

    /**********************************************************/
    @Override
    public void showSearchOption() {
        ((CloseoutActivity)getActivity()).showSearchOption();
    }

    @Override
    public void onGetMenu(Menu menu) {
        listener.onGetMenu(menu);
    }

    @Override
    public Context context() {
        FragmentActivity activity = getActivity();
        if (activity != null)
            return activity.getApplicationContext();
        else
            return null;
    }

    @Override
    public void setMenuTitle(String title) {
        if (null != listener) listener.setMenuTitle(title);
    }

    @Override
    public void showUrl(final String url) {

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
//                if (url.contains("/Catalogo/MiRevista")) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                } else {
//                    webView.loadUrl(url);
//                    return false;
//                }
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
    public void showError() {
        if (isVisible()) {
            webView.setVisibility(View.GONE);
            rltContentError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void trackBack(LoginModel loginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_CLOSEOUT, loginModel);
        if (listener != null) {
            listener.onBackFromFragment();
        }
    }

    public void trackBackPressed() {
        if (presenter != null)
            presenter.trackBack();
    }

    public boolean onBackWebView() {
        if (!isWebBackLocked && webView.canGoBack()) {
            hasGoneBack = true;
            webView.goBack();
            return true;
        } else
            return false;
    }

    public void trackEvent(String category, String action, String label, String eventName) {
        Tracker.trackEvent(GlobalConstant.SCREEN_CLOSEOUT,
            category, action, label, eventName, loginModel);
    }

    /*****************************************************************/

    private void initBundle() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            page = bundle.getString(CloseoutActivity.OPTION);
        }
    }

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

    /** */

    interface Listener {
        void onGetMenu(Menu menu);
        void setMenuTitle(String title);
        void onBackFromFragment();
    }
}

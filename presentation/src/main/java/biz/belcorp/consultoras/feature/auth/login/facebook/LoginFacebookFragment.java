package biz.belcorp.consultoras.feature.auth.login.facebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.FacebookException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.component.CircleImageView;
import biz.belcorp.consultoras.common.dialog.MessageDialog;
import biz.belcorp.consultoras.common.model.auth.CredentialsModel;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.country.CountryModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.model.facebook.FacebookProfileModel;
import biz.belcorp.consultoras.common.model.kinesis.KinesisModel;
import biz.belcorp.consultoras.data.manager.SessionManager;
import biz.belcorp.consultoras.domain.entity.Login;
import biz.belcorp.consultoras.domain.entity.Verificacion;
import biz.belcorp.consultoras.feature.auth.di.AuthComponent;
import biz.belcorp.consultoras.feature.auth.login.Listener;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.anotation.AuthType;
import biz.belcorp.consultoras.util.FacebookUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.KinesisManager;
import biz.belcorp.consultoras.util.anotation.CreditApplicationType;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.analytics.annotation.AnalyticEvent;
import biz.belcorp.library.analytics.annotation.AnalyticScreen;
import biz.belcorp.library.annotation.Country;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.CountryUtil;
import biz.belcorp.library.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@AnalyticScreen(name = "FacebookLoginScreen")
public class LoginFacebookFragment extends BaseFragment implements LoginFacebookView {

    @Inject
    LoginFacebookPresenter presenter;

    @BindView(R.id.tvw_title)
    TextView tvwTitle;

    @BindView(R.id.ivw_image)
    CircleImageView ivwImage;

    @BindView(R.id.tvw_btn_continue)
    TextView tvwBtnContinue;

    @BindView(R.id.tvw_continue)
    TextView tvwContinue;

    @BindView(R.id.tvw_change)
    TextView tvwChange;

    private FacebookUtil fbUtil;
    private FacebookProfileModel facebookProfile;
    private CountryModel country;

    private KinesisManager kinesisManager;
    private Listener listener;

    /**********************************************************/

    public LoginFacebookFragment() {
        super();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() throws IllegalStateException {
        getComponent(AuthComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);

        if (savedInstanceState == null) {
            init();
        }
    }

    /**********************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            this.listener = (Listener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_facebook, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fbUtil == null)
            fbUtil = new FacebookUtil(facebookListener);
        fbUtil.getCallBackManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        initTrackScreen();
    }

    /**********************************************************/

    private void init() {
        if (CountryUtil.getCode(context()).equals(Country.CR)) {
            tvwTitle.setVisibility(View.GONE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                tvwTitle.setText(Html.fromHtml(getString(R.string.login_welcome), Html.FROM_HTML_MODE_LEGACY));
            else
                tvwTitle.setText(Html.fromHtml(getString(R.string.login_welcome)));
        }

        tvwChange.setPaintFlags(tvwChange.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        fbUtil = new FacebookUtil(facebookListener);

        presenter.data();
    }

    private void initTrackScreen() {

        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_LOGIN_FB);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserNullProperties(null);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    /**********************************************************/

    private void openRegisterLink(String url) {
        if (null != listener) {
            listener.openRegisterLink(url);
        }
    }

    /**********************************************************/

    @Override
    public void renderData(CountryModel country, FacebookProfileModel facebookProfile) {
        this.country = country;
        this.facebookProfile = facebookProfile;

        tvwBtnContinue.setText(String.format(getString(R.string.login_facebook_user), facebookProfile.getFirstName()));
        tvwContinue.setText(String.format(getString(R.string.login_facebook_name), facebookProfile.getFirstName()));

        Glide.with(this).load(facebookProfile.getImage()).apply(RequestOptions.noTransformation()
            .placeholder(R.drawable.ic_contact_default)
            .error(R.drawable.ic_contact_default)
            .priority(Priority.HIGH))
            .into(ivwImage);
    }

    @Override
    public void success(Login login, LoginModel loginModel, Verificacion verificacion) {
        presenter.getUsabilityConfig(login);

        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_LOGIN_FB);
        bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_LOGIN);
        bundle.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_LOGIN_SUCCESS);
        bundle.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_LOGIN_FB);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        loginModel.setTipoIngreso(login.getTipoIngreso());
        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_LOGIN_FB_NAME, bundle, properties);

        if (null != listener) {
            if((verificacion.getOpcionVerificacionSMS() == CreditApplicationType.NOT_APPLY
                ||  verificacion.getOpcionVerificacionSMS() == CreditApplicationType.ACCEPT)
                && (verificacion.getOpcionVerificacionCorreo() == CreditApplicationType.NOT_APPLY
                ||  verificacion.getOpcionVerificacionCorreo() == CreditApplicationType.ACCEPT)
                && (verificacion.getOpcionCambioClave() == CreditApplicationType.NOT_APPLY
                ||  verificacion.getOpcionCambioClave() == CreditApplicationType.ACCEPT)
                && (loginModel.getIndicadorContratoCredito() == CreditApplicationType.NOT_APPLY
                || loginModel.getIndicadorContratoCredito() == CreditApplicationType.ACCEPT)
                && loginModel.isAceptaTerminosCondiciones()) {

                SessionManager sessionManager = SessionManager.Companion.getInstance(context());
                if (sessionManager.isTutorial(loginModel.getConsultantCode())) {
                    listener.onTutorial(loginModel.getConsultantCode(), loginModel.getCountryISO());
                } else {
                    listener.onHome();
                }
            } else {
                listener.onTerms(loginModel);
            }
        }

    }

    @Override
    public void setLogAccess(KinesisModel kinesisModel, Login login) {
        try {
            if (kinesisManager == null) {
                kinesisManager = KinesisManager.INSTANCE.create(getActivity(), GlobalConstant.SCREEN_LOG_LOGIN, kinesisModel);
            }
            kinesisManager.save(login);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void failed(ErrorModel error) {
        onChange();
    }

    @Override
    public Context context() {
        FragmentActivity activity = getActivity();
        if (activity != null)
            return activity.getApplicationContext();
        else
            return null;
    }

    /**********************************************************/

    @OnClick(R.id.btn_facebook)
    @AnalyticEvent(action = "OnFacebookClick", category = "Click")
    void onFacebook() {

        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_LOGIN_FB);
        bundle.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.SCREEN_LOGIN);
        bundle.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BUTTON);
        bundle.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LOGIN_FB);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_LOGIN_NAME, bundle);

        if (NetworkUtil.isThereInternetConnection(getContext())) {
            showLoading();

            if (fbUtil == null)
                fbUtil = new FacebookUtil(facebookListener);

            fbUtil.login(getActivity());
        } else {
            showNetworkError();
        }
    }

    @OnClick(R.id.tvw_change)
    @AnalyticEvent(action = "OnChangeClick", category = "Click")
    void onChange() {
        if (null != listener)
            listener.openLogin();
    }

    @OnClick(R.id.tvw_register)
    @AnalyticEvent(action = "onRegisterClick", category = "Click")
    void onRegister() {
        if (null == country)
            openRegisterLink("http://www.uneteabelcorp.com/");
        else
            openRegisterLink(country.getUrlJoinBelcorp());
    }

    /**********************************************************/

    final FacebookUtil.OnFacebookListener facebookListener = new FacebookUtil.OnFacebookListener() {
        @Override
        public void onLoginSuccess(FacebookProfileModel model) {
            facebookProfile = model;

            CredentialsModel credentials = new CredentialsModel();
            credentials.setUsername(model.getId());
            credentials.setPassword("");
            credentials.setPais("");
            credentials.setTipoAutenticacion(AuthType.FACEBOOK);

            presenter.loginWithFacebook(credentials, facebookProfile);
        }

        @Override
        public void onLoginFailure(FacebookException exception) {
            hideLoading();
            showErrorFacebook();
        }

        @Override
        public void onLoginCancel() {
            hideLoading();
        }

        @Override
        public void onRenderData(FacebookProfileModel model) {
            hideLoading();
            facebookProfile = model;
        }

        @Override
        public void onLoginPermissionDeclined() {
            // EMPTY
        }

        private void showErrorFacebook() {
            try {
                new MessageDialog()
                    .setIcon(R.drawable.ic_alerta, 0)
                    .setStringTitle(R.string.login_error_facebook_title)
                    .setStringMessage(R.string.login_error_facebook_message)
                    .setStringAceptar(R.string.button_aceptar)
                    .showIcon(true)
                    .showClose(false)
                    .show(getFragmentManager(), "modalError");
            } catch (IllegalStateException e) {
                BelcorpLogger.w("showErrorFacebook", e);
            }
        }
    };

}

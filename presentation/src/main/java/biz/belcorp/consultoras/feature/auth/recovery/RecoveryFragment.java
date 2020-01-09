package biz.belcorp.consultoras.feature.auth.recovery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.adapter.CountryAdapter;
import biz.belcorp.consultoras.common.dialog.ListDialog;
import biz.belcorp.consultoras.common.dialog.MessageDialog;
import biz.belcorp.consultoras.common.dialog.Tooltip;
import biz.belcorp.consultoras.common.model.country.CountryModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.data.net.RestApi;
import biz.belcorp.consultoras.feature.auth.di.AuthComponent;
import biz.belcorp.consultoras.util.anotation.ErrorCode;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.analytics.annotation.AnalyticEvent;
import biz.belcorp.library.analytics.annotation.AnalyticScreen;
import biz.belcorp.library.annotation.Country;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.net.dto.ServiceDto;
import biz.belcorp.library.util.CountryUtil;
import biz.belcorp.library.util.KeyboardUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@AnalyticScreen(name = "RecoveryScreen")
public class RecoveryFragment extends BaseFragment implements RecoveryView {

    @Inject
    RecoveryPresenter presenter;

    @BindView(R.id.img_flag)
    ImageView imgFlag;

    @BindView(R.id.tvw_country)
    TextView tvwCountry;

    @BindView(R.id.til_username)
    TextInputLayout tilUsername;

    @BindView(R.id.ivw_user)
    ImageView ivwUser;

    @BindView(R.id.ted_username)
    TextInputEditText tedUsername;

    @BindView(R.id.img_help_user)
    ImageView imgHelpUser;

    @BindView(R.id.tvw_validation_message)
    TextView tvwValidationMessage;

    @BindView(R.id.btn_recovery_asociar)
    Button btnRecoveryAssociate;

    String countrySIM;
    List<CountryModel> countries;
    CountryAdapter countryAdapter;

    String helpToolTip = "";
    private CountryModel countrySelected = null;

    /**********************************************************/

    interface RecoveryFragmentListener {
        void onLogin();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recovery, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initScreenTrack();
    }

    /**********************************************************/

    private void init() {
        presenter.data();
    }

    public void initScreenTrack() {
        Tracker.trackScreen(GlobalConstant.SCREEN_FORGOT_PSW, null);
    }

    public void trackBackPressed() {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_FORGOT_PSW, null);
    }

    /**********************************************************/

    private void setSelectedCountry(CountryModel model) {
        countrySelected = model;
        tvwCountry.setText(countrySelected.getName());
        tvwCountry.setAllCaps(true);
        imgFlag.setImageDrawable(ContextCompat.getDrawable(context(), CountryUtil.getFlag(countrySelected.getIso())));

        String messageCountry = changeCodigoConsultora(model.getIso());
        helpToolTip = String.format(getString(R.string.recovery_tooltip_ingresa), messageCountry);
        ivwUser.setImageDrawable(VectorDrawableCompat.create(context().getResources(), R.drawable.ic_user_black, null));
        tilUsername.setHint(messageCountry);

//        if (model.getConfigForgotPassword() == 2) {
//            helpToolTip = getString(R.string.recovery_tooltip_cedula);
//            ivwUser.setImageDrawable(VectorDrawableCompat.create(context().getResources(), R.drawable.ic_user_black, null));
//        } else {
//            helpToolTip = getString(R.string.recovery_tooltip_correo);
//            ivwUser.setImageDrawable(VectorDrawableCompat.create(context().getResources(), R.drawable.ic_user_black, null));
//        }
    }

    private String changeCodigoConsultora(String country) {
        switch (country) {
            case Country.BO:
                return getString(R.string.recovery_country_bo);
            case Country.CL:
                return getString(R.string.recovery_country_cl);
            case Country.CO:
                return getString(R.string.recovery_country_co);
            case Country.CR:
                return getString(R.string.recovery_country_cr);
            case Country.EC:
                return getString(R.string.recovery_country_ec);
            case Country.SV:
                return getString(R.string.recovery_country_sv);
            case Country.GT:
                return getString(R.string.recovery_country_gt);
            case Country.MX:
                return getString(R.string.recovery_country_mx);
            case Country.PA:
                return getString(R.string.recovery_country_pa);
            case Country.PE:
                return getString(R.string.recovery_country_pe);
            case Country.PR:
                return getString(R.string.recovery_country_pr);
            case Country.DO:
                return getString(R.string.recovery_country_do);
            default:
                return "";
        }
    }

    /**********************************************************/

    @Override
    public void renderData(String countrySIM, List<CountryModel> countries) {
        this.countrySIM = countrySIM;
        this.countries = countries;

        if (null != countries && !countries.isEmpty()) {
            this.countryAdapter = new CountryAdapter(context(), countries);

            for (CountryModel model : countries) {
                if (model.getIso().equals(countrySIM)) {
                    setSelectedCountry(model);
                    break;
                }
            }
        } else {
            this.countryAdapter = new CountryAdapter(context(), Collections.emptyList());
        }
    }

    @Override
    public void success(String mail) {

        String html = "Te hemos enviado las instrucciones para recuperar tu contraseña a: <br><b>" + mail + "</b>";

        try {
            new MessageDialog()
                .setIcon(R.drawable.ic_mail_alert, 1)
                .setStringTitle(R.string.recovery_success_title)
                .setMessageHtml(html)
                .setStringAceptar(R.string.button_aceptar)
                .showIcon(true)
                .showClose(false)
                .setListener(successListener)
                .show(getFragmentManager(), "modalSuccess");
        } catch (IllegalStateException e) {
            BelcorpLogger.w("success", e);
        }
    }

    @Override
    public void failed(ErrorModel error) {
        switch (error.getCode()) {
            case ErrorCode.NETWORK:
                showNetworkError();
                break;
            case ErrorCode.BAD_REQUEST:
            case ErrorCode.SERVICE:
                ServiceDto response = RestApi.INSTANCE.readError(error.getParams());
                if (null == response || StringUtil.isNullOrEmpty(response.getMessage()))
                    showErrorDefault();
                else
                    showErrorMessage(response.getMessage());
                break;
            default:
                showErrorDefault();
                break;
        }
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

    @OnClick(R.id.btn_recovery_asociar)
    @AnalyticEvent(action = "OnRecoveryClick", category = "Click")
    public void recovery() {
        String username = tedUsername.getText().toString();

        if (null == countrySelected) {
            showTooltip(tvwCountry, getString(R.string.login_validation_pais), Tooltip.GRAVITY_RIGHT, true);
            return;
        }

        if (StringUtil.isNullOrEmpty(username)) {

//            if (1 == countrySelected.getConfigForgotPassword()) {
//                message = getString(R.string.recovery_validation_email);
//            } else {
//                message = getString(R.string.recovery_validation_cedula);
//            }

            String message = String.format(getString(R.string.recovery_tooltip_ingresa), changeCodigoConsultora(countrySelected.getIso()));
            showTooltip(tedUsername, message, Tooltip.GRAVITY_RIGHT, true);
            return;
        }

//        if (1 == countrySelected.getConfigForgotPassword() && !ValidUtil.validate(ValidType.EMAIL, username)) {
//            showTooltip(tedUsername, getString(R.string.recovery_validation_email_format), Tooltip.GRAVITY_RIGHT, true);
//            return;
//        }

        KeyboardUtil.dismissKeyboard(getContext(), tedUsername);
        presenter.recovery(countrySelected.getId(), username);

    }

    @OnClick(R.id.img_help_user)
    @AnalyticEvent(action = "OnTooltipUserClick", category = "Click")
    public void tooltipUser(View view) {
        if (countrySelected != null)
            showTooltip(view, helpToolTip, Tooltip.GRAVITY_LEFT);
        else {
            String message = getString(R.string.login_validation_pais);
            showTooltip(tvwCountry, message, Tooltip.GRAVITY_RIGHT, true);
        }
    }

    @OnClick(R.id.layout_select_country)
    @AnalyticEvent(action = "OnShowCountriesClick", category = "Click")
    public void onShowCountries() {
        try {
            new ListDialog()
                .setTitle(R.string.login_select_country)
                .setCountries(countries)
                .setAdapter(countryAdapter)
                .setListViewListener(listDialogListener)
                .show(getFragmentManager(), "modalCountries");
        } catch (IllegalStateException e) {
            BelcorpLogger.w("onShowCountries", e);
        }
    }

    /**********************************************************/

    final ListDialog.ListDialogListener listDialogListener = new ListDialog.ListDialogListener() {
        @Override
        public void selectedItem(int position) {
            setSelectedCountry((CountryModel) countryAdapter.getItem(position));
        }
    };

    final MessageDialog.MessageDialogListener successListener = new MessageDialog.MessageDialogListener() {
        @Override
        public void aceptar() {
            tedUsername.setText("");
        }

        @Override
        public void cancelar() {
            // EMPTY
        }
    };

}

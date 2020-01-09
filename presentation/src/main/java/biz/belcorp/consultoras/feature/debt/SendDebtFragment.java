package biz.belcorp.consultoras.feature.debt;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.debt.DebtResumeModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.feature.debt.di.DebtComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.CommunicationUtils;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.ToastUtil;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 */
public class SendDebtFragment extends BaseFragment implements SendDebtView {

    @BindView(R.id.ivw_enviar_sms)
    protected ImageView ivwSendSMS;
    @BindView(R.id.ivw_sms_check)
    protected AppCompatImageView ivwSMSCheck;
    @BindView(R.id.ivw_enviar_msn)
    protected ImageView ivwSendEmail;
    @BindView(R.id.ivw_msn_check)
    protected AppCompatImageView ivwEmailCheck;
    @BindView(R.id.ivw_enviar_wapp)
    protected ImageView ivwSendWAPP;
    @BindView(R.id.ivw_wapp_check)
    protected AppCompatImageView ivwWAPPCheck;
    @BindView(R.id.edt_message)
    protected EditText edtMessage;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.tvw_sms)
    TextView tvwSms;

    @BindView(R.id.tvw_correo)
    TextView tvwCorreo;

    @BindView(R.id.tvw_whatsapp)
    TextView tvwWhatsapp;

    private Unbinder unbinder;

    @Inject
    protected SendDebtPresenter sendDebtPresenter;

    private int primaryColor;
    private int shareType = 0;

    /******************************************************/

    @Override
    protected boolean onInjectView() throws IllegalStateException {
        getComponent(DebtComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        sendDebtPresenter.attachView(this);

        primaryColor = ContextCompat.getColor(getActivity(), R.color.primary);

        Bundle bundle = getArguments();

        if (bundle != null) {
            int clientID = bundle.getInt(GlobalConstant.CLIENTE_ID, 0);
            BigDecimal totalDebt = new BigDecimal(bundle.getString(GlobalConstant.CLIENT_TOTAL_DEBT, "0"));

            if (clientID == 0 || totalDebt.equals(BigDecimal.ZERO)) {
                getActivity().finish();
                return;
            }

            if (sendDebtPresenter != null) {
                sendDebtPresenter.getDebtData(clientID);
                sendDebtPresenter.setTotalDebt(totalDebt);
            }
        }
    }

    /******************************************************/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_debt, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(sendDebtPresenter != null) sendDebtPresenter.initScreenTrack();
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sendDebtPresenter != null) sendDebtPresenter.destroy();
    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_DEBT_SHARE);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    /**********************************************************/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES)
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    /******************************************************/

    @Override
    @OnClick({R.id.ivw_enviar_sms, R.id.ivw_enviar_msn, R.id.ivw_enviar_wapp})
    public void onShareTypeClick(View view) {

        int viewID = view == null ? 0 : view.getId();

        switch (viewID) {
            case R.id.ivw_enviar_sms:

                if (ivwSendSMS.getTag().toString().equals("1")) {
                    if (String.valueOf(ivwSMSCheck.getTag()).equals("unchecked")) {
                        ivwSMSCheck.setTag("checked");
                        ivwEmailCheck.setTag("unchecked");
                        ivwWAPPCheck.setTag("unchecked");
                        ivwSMSCheck.setImageResource(R.drawable.ic_check_selector);
                        ivwEmailCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwWAPPCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        shareType = 1;
                    } else {
                        ivwSMSCheck.setTag("unchecked");
                        ivwEmailCheck.setTag("unchecked");
                        ivwWAPPCheck.setTag("unchecked");
                        ivwSMSCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwEmailCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwWAPPCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        shareType = 0;
                    }
                }

                break;
            case R.id.ivw_enviar_msn:

                if (ivwSendEmail.getTag().toString().equals("1")) {
                    if (String.valueOf(ivwEmailCheck.getTag()).equals("unchecked")) {
                        ivwEmailCheck.setTag("checked");
                        ivwSMSCheck.setTag("unchecked");
                        ivwWAPPCheck.setTag("unchecked");
                        ivwSMSCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwEmailCheck.setImageResource(R.drawable.ic_check_selector);
                        ivwWAPPCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        shareType = 2;
                    } else {
                        ivwSMSCheck.setTag("unchecked");
                        ivwEmailCheck.setTag("unchecked");
                        ivwWAPPCheck.setTag("unchecked");
                        ivwSMSCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwEmailCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwWAPPCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        shareType = 0;
                    }
                }

                break;
            case R.id.ivw_enviar_wapp:

                if (ivwSendWAPP.getTag().toString().equals("1")) {
                    if (String.valueOf(ivwWAPPCheck.getTag()).equals("unchecked")) {
                        ivwWAPPCheck.setTag("checked");
                        ivwSMSCheck.setTag("unchecked");
                        ivwEmailCheck.setTag("unchecked");
                        ivwSMSCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwEmailCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwWAPPCheck.setImageResource(R.drawable.ic_check_selector);
                        shareType = 3;
                    } else {
                        ivwSMSCheck.setTag("unchecked");
                        ivwEmailCheck.setTag("unchecked");
                        ivwWAPPCheck.setTag("unchecked");
                        ivwSMSCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwEmailCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwWAPPCheck.setImageResource(R.drawable.ic_check_selector_gray);
                        shareType = 0;
                    }
                }

                break;
            default:

                ivwSMSCheck.setTag("unchecked");
                ivwEmailCheck.setTag("unchecked");
                ivwWAPPCheck.setTag("unchecked");
                ivwSMSCheck.setImageResource(R.drawable.ic_check_selector_gray);
                ivwEmailCheck.setImageResource(R.drawable.ic_check_selector_gray);
                ivwWAPPCheck.setImageResource(R.drawable.ic_check_selector_gray);
                shareType = 0;
        }

        sendDebtPresenter.validateShareType(shareType);
    }

    @Override
    public void setUpShareType(boolean whatsApp, boolean sms, boolean email) {

        if (whatsApp) {
            ivwSendWAPP.setTag("1");
            tvwWhatsapp.setTag("1");
            ivwSendWAPP.setBackgroundResource(R.drawable.ic_circle_black);
            tvwWhatsapp.setTextColor(Color.BLACK);
        } else {
            ivwSendWAPP.setTag("0");
            tvwWhatsapp.setTag("0");
            ivwSendWAPP.setBackgroundResource(R.drawable.ic_circle_client_gray);
            tvwWhatsapp.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray_disabled));
            ivwWAPPCheck.setVisibility(View.GONE);
        }

        if (sms) {
            ivwSendSMS.setTag("1");
            tvwSms.setTag("1");
            ivwSendSMS.setBackgroundResource(R.drawable.ic_circle_black);
            tvwSms.setTextColor(Color.BLACK);
        } else {
            ivwSendSMS.setTag("0");
            tvwSms.setTag("0");
            ivwSendSMS.setBackgroundResource(R.drawable.ic_circle_client_gray);
            tvwSms.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray_disabled));
            ivwSMSCheck.setVisibility(View.GONE);
        }

        if (email) {
            ivwSendEmail.setTag("1");
            tvwCorreo.setTag("1");
            ivwSendEmail.setBackgroundResource(R.drawable.ic_circle_black);
            tvwCorreo.setTextColor(Color.BLACK);
        } else {
            ivwSendEmail.setTag("0");
            tvwCorreo.setTag("0");
            ivwSendEmail.setBackgroundResource(R.drawable.ic_circle_client_gray);
            tvwCorreo.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray_disabled));
            ivwEmailCheck.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tvw_add_debt)
    protected void onShareDebt() {
        sendDebtPresenter.initScreenTrackEnviarMensajeDeuda(shareType);
    }

    /******************************************************/
    private void shareToSMS(String phoneNumber) {
        CommunicationUtils.enviarSms(context(),phoneNumber,edtMessage.getText().toString().trim());
    }

    private Intent shareToMail(String mail) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.send_debt_subject));
        intent.putExtra(Intent.EXTRA_TEXT, edtMessage.getText().toString().trim());

        return intent;
    }

    private Intent shareToWAPP(String phoneNumber) {
        String toNumber = phoneNumber.replace("+", "").replace(" ", "");

        Intent intent = new Intent("android.intent.action.MAIN");
        intent.putExtra("jid", toNumber + "@s.whatsapp.net");
        intent.putExtra(Intent.EXTRA_TEXT, edtMessage.getText().toString().trim());
        intent.setAction(Intent.ACTION_SEND);
        intent.setPackage("com.whatsapp");
        intent.setType("text/plain");

        return intent;
    }

    /******************************************************/

    @Override
    public Context context() {
        FragmentActivity activity = getActivity();
        if (activity != null)
            return activity.getApplicationContext();
        else
            return null;
    }

    /******************************************************/

    @Override
    public void showMessage(DebtResumeModel debtResumeModel) {

        SpannableStringBuilder spannableString = new SpannableStringBuilder();

        String greeting = String.format(getString(R.string.send_debt_gretting), debtResumeModel.getClientName().split(Pattern.quote(" "))[0]);
        Spannable greetingSpannable = Spannable.Factory.getInstance().newSpannable(greeting);
        greetingSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, greeting.length(), 0);

        String centralMessage = getString(R.string.send_debt_central_message);

        String totalDebt = "\n\n" + String.format(getString(R.string.send_debt_total_debt), debtResumeModel.getTotalDebt());
        Spannable totalDebtSpannable = Spannable.Factory.getInstance().newSpannable(totalDebt);
        totalDebtSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, totalDebt.length(), 0);

        String detailDebt = getString(R.string.send_debt_detail_label);
        Spannable detailDebtSpannable = Spannable.Factory.getInstance().newSpannable(detailDebt);
        detailDebtSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, detailDebt.length(), 0);

        String deadLine = String.format(getString(R.string.send_debt_dead_line), debtResumeModel.getDeadLine());
        Spannable deadLineSpannable = Spannable.Factory.getInstance().newSpannable(deadLine);
        deadLineSpannable.setSpan(new ForegroundColorSpan(primaryColor), 0, 14, 0);

        String greeting2 = debtResumeModel.getConsultantName().split(Pattern.quote(" "))[0];
        Spannable greeting2Spannable = Spannable.Factory.getInstance().newSpannable(StringUtil.capitalize(greeting2.toLowerCase()));

        spannableString.append(greetingSpannable);
        spannableString.append(centralMessage);
        spannableString.append(totalDebtSpannable);
        spannableString.append("\n\n");
        spannableString.append(getString(R.string.send_payment_regards));
        spannableString.append("\n");
        spannableString.append(greeting2Spannable);

        if (!TextUtils.isEmpty(debtResumeModel.getDebtDescription())) {
            spannableString.append("\n\n");
            spannableString.append(detailDebtSpannable);
            spannableString.append(debtResumeModel.getDebtDescription());
        }

        if (!TextUtils.isEmpty(debtResumeModel.getDeadLine())) {
            spannableString.append("\n\n");
            spannableString.append(deadLineSpannable);
        }

        edtMessage.setText(spannableString);
        edtMessage.setSelection(spannableString.toString().length());
    }

    @Override
    public void shareDebt(String contactNumber) {

        Intent intent=null;

        switch (shareType) {
            case 1:
                shareToSMS(contactNumber);
                break;
            case 2:
                intent = shareToMail(contactNumber);
                break;
            case 3:
                intent = shareToWAPP(contactNumber);
                break;
            default:
                ToastUtil.INSTANCE.show(getActivity(), R.string.send_debt_share_type_error,
                    Toast.LENGTH_LONG);
                return;
        }
        if(shareType!=1){
            try {
                startActivity(intent);
                getActivity().finish();
            } catch (ActivityNotFoundException e) {
                ToastUtil.INSTANCE.show(getActivity(), R.string.debt_share_debt_error, Toast.LENGTH_LONG);
                BelcorpLogger.w("shareDebt", e);
            }
        }

    }

    @Override
    public void onError(Throwable exception) {
        processError(exception);
    }

    @Override
    public void onError(Integer type) {
        switch (shareType) {
            case 1:
            case 3:
                ToastUtil.INSTANCE.show(context(), R.string.send_debt_no_mobile, Toast.LENGTH_SHORT);
                break;
            case 2:
                ToastUtil.INSTANCE.show(context(), R.string.send_debt_no_email, Toast.LENGTH_SHORT);
                break;
            default:
                ToastUtil.INSTANCE.show(context(), R.string.send_debt_share_type_error, Toast.LENGTH_SHORT);
                break;
        }
    }

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_DEBT_SHARE);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);
        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);
    }

    @Override
    public void initScreenTrackEnviarMensajeDeuda(LoginModel loginModel) {
        Tracker.Deudas.trackEnviarMensajeDeDeuda(loginModel);
    }

    public void trackBackPressed() {
        sendDebtPresenter.trackBackPressed();
    }
}

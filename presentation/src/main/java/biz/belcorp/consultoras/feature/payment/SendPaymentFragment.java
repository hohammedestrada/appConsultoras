package biz.belcorp.consultoras.feature.payment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.regex.Pattern;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.client.ContactoModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.CommunicationUtils;
import biz.belcorp.consultoras.util.Constant;
import biz.belcorp.consultoras.util.anotation.ContactType;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.analytics.annotation.AnalyticEvent;
import biz.belcorp.library.analytics.annotation.AnalyticScreen;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.KeyboardUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@AnalyticScreen(name = "SendPaymentScreen")
public class SendPaymentFragment extends BaseFragment implements SendPaymentView {

    private static final int REQUEST_SEND_MESSAGE = 120;
    @Inject
    SendPaymentPresenter presenter;

    @BindView(R.id.ivw_enviar_sms)
    ImageView ivwSendSMS;

    @BindView(R.id.ivw_sms_check)
    AppCompatImageView ivwCheckSMS;

    @BindView(R.id.ivw_enviar_mail)
    ImageView ivwSendEmail;

    @BindView(R.id.ivw_mail_check)
    AppCompatImageView ivwCheckEmail;

    @BindView(R.id.ivw_enviar_wapp)
    ImageView ivwSendWhatsApp;

    @BindView(R.id.ivw_wapp_check)
    AppCompatImageView ivwCheckWhastApp;

    @BindView(R.id.edt_message)
    EditText edtMessage;

    @BindView(R.id.scrollView)
    ScrollView svwScroll;

    @BindView(R.id.tvw_sms)
    TextView tvwSms;

    @BindView(R.id.tvw_correo)
    TextView tvwCorreo;

    @BindView(R.id.tvw_whatsapp)
    TextView tvwWhatsapp;

    private Unbinder unbinder;

    private int clientLocalId;
    private String clientName;
    private String clientPayment;
    private String clientTotalDebt;
    private String clientNewTotalDebt;
    private String clientMobile;
    private String clientEmail;

    private int shareType = 0;

    private SendPaymentListener listener;

    /**********************************************************/

    public SendPaymentFragment() {
        super();
    }

    public static SendPaymentFragment newInstance() {
        return new SendPaymentFragment();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() throws IllegalStateException {
        getComponent(ClientComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);

        if (savedInstanceState == null) {
            presenter.load(clientLocalId);
        }
    }

    /**********************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SendPaymentListener) {
            listener = (SendPaymentListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_send_payment, container, false);
        unbinder = ButterKnife.bind(this, v);
        initBundle();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null) presenter.initScreenTrack();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {

        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_PAYMENT_SHARE);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    /**********************************************************/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES)
            svwScroll.fullScroll(ScrollView.FOCUS_DOWN);
    }

    /**********************************************************/

    private void initBundle() {

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            clientLocalId = extras.getInt(GlobalConstant.CLIENTE_LOCAL_ID, -1);
            clientName = extras.getString(GlobalConstant.CLIENT_NAME, "");
            clientPayment = extras.getString(GlobalConstant.CLIENT_PAYMENT, "");
            clientTotalDebt = extras.getString(GlobalConstant.CLIENT_TOTAL_DEBT, "");
            clientNewTotalDebt = extras.getString(GlobalConstant.CLIENT_NEW_TOTAL_DEBT, "");
        }

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
    public void showMessage(String consultantName) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();

        String[] parts = clientName.split(Pattern.quote(" "));

        String greeting = String.format(getString(R.string.send_payment_message_title), parts[0]);
        Spannable greetingSpannable = Spannable.Factory.getInstance().newSpannable(greeting);
        greetingSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, greeting.length(), 0);

        String centralMessage = String.format(getString(R.string.send_payment_message_comment), Constant.BRAND_FOCUS_NAME);

        String totalPayment = String.format(getString(R.string.send_payment_message_payment), clientPayment);
        Spannable totalDebtSpannable = Spannable.Factory.getInstance().newSpannable(totalPayment);
        totalDebtSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, totalPayment.length(), 0);

        String deudaTotalPayment = String.format(getString(R.string.send_payment_message_deuda), clientTotalDebt);
        Spannable deudaTotalSpannable = Spannable.Factory.getInstance().newSpannable(deudaTotalPayment);
        deudaTotalSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, deudaTotalPayment.length(), 0);

        String newTotalDebt = String.format(getString(R.string.send_payment_message_nueva_deuda), clientNewTotalDebt);
        Spannable newTotalDebtSpannable = Spannable.Factory.getInstance().newSpannable(newTotalDebt);
        newTotalDebtSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, newTotalDebt.length(), 0);

        spannableString.append(greetingSpannable);
        spannableString.append("\n\n");
        spannableString.append(centralMessage);
        spannableString.append("\n\n");
        spannableString.append(deudaTotalSpannable);
        spannableString.append("\n");
        spannableString.append(totalDebtSpannable);
        spannableString.append("\n--------------------------------------\n");
        spannableString.append(newTotalDebtSpannable);
        spannableString.append("\n--------------------------------------\n\n");
        spannableString.append(getString(R.string.send_payment_regards));
        spannableString.append("\n");
        spannableString.append(StringUtil.capitalize(consultantName.toLowerCase()));

        edtMessage.setText(spannableString);
        edtMessage.setSelection(spannableString.toString().length());
    }

    @Override
    public void setData(ClienteModel model) {
        Map<Integer, ContactoModel> map = model.getContactoModelMap();

        if (map.get(ContactType.MOBILE) != null) {
            ivwSendSMS.setTag("1");
            ivwSendWhatsApp.setTag("1");

            ivwSendSMS.setBackgroundResource(R.drawable.ic_circle_black);
            ivwSendWhatsApp.setBackgroundResource(R.drawable.ic_circle_black);
            tvwSms.setTextColor(Color.BLACK);
            tvwWhatsapp.setTextColor(Color.BLACK);

            clientMobile = map.get(ContactType.MOBILE).getValor();
        } else {
            ivwSendSMS.setTag("0");
            ivwSendWhatsApp.setTag("0");

            ivwSendSMS.setBackgroundResource(R.drawable.ic_circle_client_gray);
            ivwSendWhatsApp.setBackgroundResource(R.drawable.ic_circle_client_gray);
            tvwSms.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray_disabled));
            tvwWhatsapp.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray_disabled));

            ivwCheckSMS.setVisibility(View.GONE);
            ivwCheckWhastApp.setVisibility(View.GONE);
        }

        if (map.get(ContactType.EMAIL) != null) {
            ivwSendEmail.setTag("1");
            ivwSendEmail.setBackgroundResource(R.drawable.ic_circle_black);
            tvwCorreo.setTextColor(Color.BLACK);

            clientEmail = map.get(ContactType.EMAIL).getValor();
        } else {
            ivwSendEmail.setTag("0");
            ivwSendEmail.setBackgroundResource(R.drawable.ic_circle_client_gray);
            tvwCorreo.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray_disabled));

            ivwCheckEmail.setVisibility(View.GONE);
        }
    }

    @Override
    public void sharePayment(String text) {

        switch (shareType) {
            case 1:
                shareToSMS(text);
                break;
            case 2:
                shareToEmail(text);
                break;
            case 3:
                shareToWAPP(text);
                break;
            default:
                showMessageToast(R.string.send_pick_share_type_error, Toast.LENGTH_LONG);
                break;
        }
    }

    /**********************************************************/

    @OnClick({R.id.ivw_enviar_sms, R.id.ivw_enviar_mail, R.id.ivw_enviar_wapp})
    @AnalyticEvent(action = "ShareTypeClick", category = "Click")
    protected void onShareTypeClick(View view) {
        switch (view.getId()) {
            case R.id.ivw_enviar_sms:

                if (ivwSendSMS.getTag().toString().equals("1")) {
                    if (String.valueOf(ivwCheckSMS.getTag()).equals("unchecked")) {
                        ivwCheckSMS.setTag("checked");
                        ivwCheckEmail.setTag("unchecked");
                        ivwCheckWhastApp.setTag("unchecked");
                        ivwCheckSMS.setImageResource(R.drawable.ic_check_selector);
                        ivwCheckEmail.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwCheckWhastApp.setImageResource(R.drawable.ic_check_selector_gray);
                        shareType = 1;
                    } else {
                        ivwCheckSMS.setTag("unchecked");
                        ivwCheckEmail.setTag("unchecked");
                        ivwCheckWhastApp.setTag("unchecked");
                        ivwCheckSMS.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwCheckEmail.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwCheckWhastApp.setImageResource(R.drawable.ic_check_selector_gray);
                        shareType = 0;
                    }
                }

                break;
            case R.id.ivw_enviar_mail:

                if (ivwSendEmail.getTag().toString().equals("1")) {
                    if (String.valueOf(ivwCheckEmail.getTag()).equals("unchecked")) {
                        ivwCheckEmail.setTag("checked");
                        ivwCheckSMS.setTag("unchecked");
                        ivwCheckWhastApp.setTag("unchecked");
                        ivwCheckSMS.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwCheckEmail.setImageResource(R.drawable.ic_check_selector);
                        ivwCheckWhastApp.setImageResource(R.drawable.ic_check_selector_gray);
                        shareType = 2;
                    } else {
                        ivwCheckSMS.setTag("unchecked");
                        ivwCheckEmail.setTag("unchecked");
                        ivwCheckWhastApp.setTag("unchecked");
                        ivwCheckSMS.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwCheckEmail.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwCheckWhastApp.setImageResource(R.drawable.ic_check_selector_gray);
                        shareType = 0;
                    }
                }

                break;
            case R.id.ivw_enviar_wapp:

                if (ivwSendWhatsApp.getTag().toString().equals("1")) {
                    if (String.valueOf(ivwCheckWhastApp.getTag()).equals("unchecked")) {
                        ivwCheckWhastApp.setTag("checked");
                        ivwCheckSMS.setTag("unchecked");
                        ivwCheckEmail.setTag("unchecked");
                        ivwCheckSMS.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwCheckEmail.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwCheckWhastApp.setImageResource(R.drawable.ic_check_selector);
                        shareType = 3;
                    } else {
                        ivwCheckSMS.setTag("unchecked");
                        ivwCheckEmail.setTag("unchecked");
                        ivwCheckWhastApp.setTag("unchecked");
                        ivwCheckSMS.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwCheckEmail.setImageResource(R.drawable.ic_check_selector_gray);
                        ivwCheckWhastApp.setImageResource(R.drawable.ic_check_selector_gray);
                        shareType = 0;
                    }
                }

                break;
            default:

                ivwCheckSMS.setTag("unchecked");
                ivwCheckEmail.setTag("unchecked");
                ivwCheckWhastApp.setTag("unchecked");
                ivwCheckSMS.setImageResource(R.drawable.ic_check_selector_gray);
                ivwCheckEmail.setImageResource(R.drawable.ic_check_selector_gray);
                ivwCheckWhastApp.setImageResource(R.drawable.ic_check_selector_gray);
                shareType = 0;
        }
    }

    @OnClick(R.id.btn_send)
    @AnalyticEvent(action = "SendClick", category = "Click")
    public void sendProof() {
        hideKeyboard();

        String comment = edtMessage.getText().toString().trim();

        if (shareType == 0) {
            showMessageToast(R.string.send_payment_no_type_selected, Toast.LENGTH_LONG);
            return;
        }

        if (!comment.equals("")) {
            sharePayment(comment);
        } else {
            showMessageToast(R.string.send_payment_no_comment, Toast.LENGTH_LONG);
        }
        presenter.initScreenTrackEnviarMensajePago();

    }

    public void showMessageToast(int text, int time) {
        Toast.makeText(getContext(), text, time).show();
    }

    /****************************************************************/

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view == null) view = new View(getActivity());
        KeyboardUtil.dismissKeyboard(getActivity(), view);
    }

    private void shareToSMS(String text) {

        CommunicationUtils.enviarSms(context(),clientMobile,text);
    }

    private void shareToEmail(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{clientEmail});
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.send_payment_subject));
        intent.setType("text/plain");
        try {
            startActivityForResult(Intent.createChooser(intent, "Enviar email..."), REQUEST_SEND_MESSAGE);
        } catch (ActivityNotFoundException ex) {
            showMessageToast(R.string.send_payment_error, Toast.LENGTH_SHORT);
            BelcorpLogger.w("shareToEmail", ex);
        }
    }

    private void shareToWAPP(String text) {
        String toNumber = clientMobile.replace("+", "").replace(" ", "");

        Intent intent = new Intent("android.intent.action.MAIN");
        intent.putExtra("jid", toNumber + "@s.whatsapp.net");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setAction(Intent.ACTION_SEND);
        intent.setPackage("com.whatsapp");
        intent.setType("text/plain");

        try {
            startActivityForResult(intent, REQUEST_SEND_MESSAGE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), R.string.send_payment_error, Toast.LENGTH_LONG).show();
            BelcorpLogger.w("shareToWAPP", e);
        }
    }

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_PAYMENT_SHARE);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);

        if (listener != null) {
            listener.onBackFromFragment();
        }
    }

    @Override
    public void initScreenTrackEnviarMensajePago(LoginModel loginModel) {
        Tracker.Deudas.trackEnviarMensajeDePago(loginModel);
    }

    public void trackBackPressed() {
        presenter.trackBackPressed();
    }

    /************************************************************************************/

    interface SendPaymentListener {
        void onBackFromFragment();
    }

}

package biz.belcorp.consultoras.feature.payment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.component.CurrencyEditText;
import biz.belcorp.consultoras.common.component.EmptyCurrencyTextWatcher;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.user.UserModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.domain.entity.ClientMovement;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.MovementType;
import biz.belcorp.consultoras.util.anotation.StatusType;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.analytics.annotation.AnalyticEvent;
import biz.belcorp.library.analytics.annotation.AnalyticScreen;
import biz.belcorp.library.annotation.DatetimeFormat;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.CountryUtil;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.KeyboardUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@AnalyticScreen(name = "AddPaymentScreen")
public class PaymentFragment extends BaseFragment implements PaymentView {

    @Inject
    PaymentPresenter presenter;

    @BindView(R.id.tvw_title)
    TextView tvwTitle;

    @BindView(R.id.tvw_debt)
    TextView tvwDebt;

    @BindView(R.id.tvw_currency)
    TextView tvwCurrency;

    @BindView(R.id.btn_add_payment)
    Button btnAddPayment;

    @BindView(R.id.edt_amount)
    CurrencyEditText edtAmount;

    @BindView(R.id.tvw_send_constancy)
    TextView tvwSendConstancy;

    @BindView(R.id.swc_share_transaction)
    SwitchCompat swcShareTransaction;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private PaymentFragmentListener listener;

    private String clientName;
    private String campaing;
    private String moneySymbol;
    private BigDecimal totalDebt;
    private String clientPayment;
    private DecimalFormat decimalFormat;
    private DecimalFormat decimalFormatConstancia;
    private int showDecimal = 1;

    Unbinder unbinder;
    private Integer clientId;
    private Integer clienteLocalID;
    private LoginModel loginModel;

    /**********************************************************/

    interface PaymentFragmentListener {

        void setResult(boolean result);

        void showConstancy(String payment, String totalDebt, String newTotalDebt);
    }

    /**********************************************************/

    public PaymentFragment() {
        super();
    }

    public static PaymentFragment newInstance() {
        return new PaymentFragment();
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

        initBundle();
        if (savedInstanceState == null) {
            presenter.load();
        }
    }

    /**********************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PaymentFragmentListener) {
            this.listener = (PaymentFragmentListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_payment, container, false);
        unbinder = ButterKnife.bind(this, v);

        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                v.getWindowVisibleDisplayFrame(r);
                int screenHeight = v.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    scrollView.scrollTo(0, btnAddPayment.getBottom());
                }
            }
        });

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
        this.loginModel = loginModel;

        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_PAYMENT_ADD);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);
        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    /**********************************************************/

    private void initBundle() {

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            clientId = extras.getInt(GlobalConstant.CLIENTE_ID, -1);
            clienteLocalID = extras.getInt(GlobalConstant.CLIENTE_LOCAL_ID, -1);
            clientName = extras.getString(GlobalConstant.CLIENT_NAME, "");
            totalDebt = new BigDecimal(extras.getString(GlobalConstant.CLIENT_TOTAL_DEBT, "0"));
        }

    }

    private void initParams() {

        String[] parts = clientName.split(Pattern.quote(" "));
        tvwTitle.setText(String.format(getString(R.string.debt_history_title), parts[0]));

        tvwSendConstancy.setText(String.format(getString(R.string.payment_send_transaction_label), parts[0]));

        tvwCurrency.setText(moneySymbol);
        edtAmount.addTextChangedListener(new EmptyCurrencyTextWatcher(edtAmount, decimalFormat, showDecimal));

        edtAmount.setSelection(edtAmount.getText().length());
        edtAmount.setHint(decimalFormatConstancia.format(0.0));

        String debt = moneySymbol + " " + decimalFormat.format(totalDebt);
        tvwDebt.setText(debt);

        edtAmount.setSelection(edtAmount.getText().length());

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });
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
    public void onDestroyView() {
        super.onDestroyView();
        if (null != unbinder) unbinder.unbind();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.destroy();
    }

    /**********************************************************/

    @Override
    public void setData(UserModel model) {

        this.campaing = model.getCampaing();
        this.moneySymbol = model.getCountryMoneySymbol();
        this.decimalFormat = CountryUtil.getDecimalFormatByISO(model.getCountryISO(), false);
        this.decimalFormatConstancia = CountryUtil.getDecimalFormatConstanciaByISO(model.getCountryISO(), false);
        this.showDecimal = model.getCountryShowDecimal();

        initParams();
    }

    @Override
    public void showResult(Boolean result) {
        if (result && swcShareTransaction.isChecked()) {
            try {
                Number number = decimalFormatConstancia.parse(clientPayment.replace(moneySymbol, "").replace(" ", "").trim());
                BigDecimal newtotalDebt = totalDebt.subtract(BigDecimal.valueOf(number.doubleValue()));

                if (listener != null) {
                    listener.showConstancy(decimalFormatConstancia.format(number), moneySymbol + " " + decimalFormatConstancia.format(totalDebt), moneySymbol + " " + decimalFormatConstancia.format(newtotalDebt));
                }
            } catch (ParseException e) {
                BelcorpLogger.w("showResult", e);
            }
        } else {
            if (listener != null) {
                listener.setResult(result);
            }
        }
    }

    /**********************************************************/

    @OnClick(R.id.btn_add_payment)
    @AnalyticEvent(action = "AddPaymentClick", category = "Click")
    public void addPayment() {
        hideKeyboard();

        String amount = edtAmount.getText().toString().trim();

        amount = amount.replace(moneySymbol, "").replace(" ", "").trim();

        Number number = BigDecimal.ZERO;
        String date = "";

        if (showDecimal == 0) amount = amount.replace(".", "");

        try {
            number = decimalFormat.parse(amount);
            date = DateUtil.convertFechaToString(new Date(), DatetimeFormat.ISO_8601);
        } catch (ParseException e) {
            BelcorpLogger.w("addPayment", e);
        }

        if (number.doubleValue() < 0.01) {
            Toast.makeText(context(), R.string.add_payment_not_amount, Toast.LENGTH_SHORT).show();
        } else {

            clientPayment = moneySymbol + " " + amount;

            ClientMovement oClientMovement = new ClientMovement();
            oClientMovement.setMovementID(0);
            oClientMovement.setClientID(clientId);
            oClientMovement.setClienteLocalID(clienteLocalID);
            oClientMovement.setDate(date);
            oClientMovement.setDescription("PAGO");
            oClientMovement.setCampaing(campaing);
            oClientMovement.setAmount(BigDecimal.valueOf(number.doubleValue()));
            oClientMovement.setType(MovementType.ABONO);
            oClientMovement.setEstado(StatusType.CREATE);
            oClientMovement.setSaldo(BigDecimal.valueOf(0));

            presenter.addPayment(oClientMovement, loginModel);
        }
    }

    /****************************************************************/

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_PAYMENT_ADD);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);
    }

    @Override
    public void initScreenTrackRegistrarPagoExitoso(LoginModel loginModel) {
        Tracker.Deudas.trackRegistrarPagoExitoso(loginModel);
    }

    public void trackBackPressed() {
        presenter.trackBackPressed();
    }

    /****************************************************************/

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view == null) view = new View(getActivity());
        KeyboardUtil.dismissKeyboard(getActivity(), view);
    }

}

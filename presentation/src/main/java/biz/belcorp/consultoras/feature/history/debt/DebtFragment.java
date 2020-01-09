package biz.belcorp.consultoras.feature.history.debt;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.component.CurrencyEditText;
import biz.belcorp.consultoras.common.component.EmptyCurrencyTextWatcher;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClientMovementModel;
import biz.belcorp.consultoras.common.model.user.UserModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.MovementType;
import biz.belcorp.consultoras.util.anotation.StatusType;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.CountryUtil;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 */
public class DebtFragment extends BaseFragment implements DebtView {

    @Inject
    DebtPresenter debtPresenter;

    @BindView(R.id.ted_note_description)
    EditText edtNoteDescription;
    @BindView(R.id.btn_note_add)
    Button btnNoteAdd;
    @BindView(R.id.tvw_debt_amount)
    TextView tvwDebtAmount;
    @BindView(R.id.ivw_arrow_debt_amount)
    ImageView ivwArrowDebtAmount;
    @BindView(R.id.rlt_debt_amount)
    RelativeLayout rltDebtAmount;
    @BindView(R.id.tvw_debt_description)
    TextView tvwDebtDescription;
    @BindView(R.id.tvw_currency)
    TextView tvwCurrency;
    @BindView(R.id.edt_amount)
    CurrencyEditText edtAmount;
    @BindView(R.id.llt_debt_amount)
    LinearLayout lltDebtAmount;
    @BindView(R.id.cvw_debt_amount)
    CardView cvwDebtAmount;
    @BindView(R.id.tvw_debt_note)
    TextView tvwDebtNote;
    @BindView(R.id.ivw_arrow_debt_note)
    ImageView ivwArrowDebtNote;
    @BindView(R.id.rlt_debt_note)
    RelativeLayout rltDebtNote;
    @BindView(R.id.llt_debt_note)
    LinearLayout lltDebtNote;
    @BindView(R.id.cvw_debt_note)
    CardView cvwDebtNote;

    private Unbinder bind;

    private DebtListener debtListener;
    private ClientMovementModel clientMovementModel;
    private LoginModel loginModel;

    private DecimalFormat decimalFormat;
    private String moneySymbol;
    private String movementType;
    private int showDecimal = 1;
    private DecimalFormat decimalFormatConstancia;

    /***********************************************************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(ClientComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.debtPresenter.attachView(this);

        edtNoteDescription.requestFocus();

        Bundle bundle = getArguments();
        if (bundle != null) {

            int movementLocalID = bundle.getInt(GlobalConstant.TRANSACTION);
            debtPresenter.getMovement(movementLocalID);

            movementType = bundle.getString(GlobalConstant.MOVEMENT_TYPE);

            if (movementType == null) return;
            if (movementType.equals(MovementType.ABONO)) {
                btnNoteAdd.setText(R.string.debt_debt_update_label);
                tvwDebtAmount.setText(R.string.debt_payment_label);
            } else if (movementType.equals(MovementType.CARGO)) {
                btnNoteAdd.setText(R.string.debt_payment_update_label);
            }
        }
    }

    /***********************************************************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DebtListener)
            debtListener = (DebtListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_debt, container, false);
        bind = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (debtPresenter != null) debtPresenter.initScreenTrack();
    }

    @Override
    public void onDestroyView() {

        if (bind != null)
            bind.unbind();
        if (debtPresenter != null)
            debtPresenter.destroy();

        super.onDestroyView();
    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        this.loginModel = loginModel;

        Bundle bundle = new Bundle();
        String screenName;

        if (movementType.equals(MovementType.ABONO)) {
            screenName = GlobalConstant.SCREEN_PAYMENT_EDIT;
        } else {
            screenName = GlobalConstant.SCREEN_DEBT_EDIT;
        }

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, screenName);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    @Override
    public void trackBackPressed(LoginModel loginModel) {

        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_DEBT_ADD);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);
        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);
    }

    public void trackBackPressed() {
        debtPresenter.trackBackPressed();
    }

    /***********************************************************************************************/

    @Override
    public Context context() {
        return getActivity();
    }

    /***********************************************************************************************/

    @OnClick(R.id.rlt_debt_amount)
    public void showAmount() {
        if (lltDebtAmount.getVisibility() == View.VISIBLE) {
            lltDebtAmount.setVisibility(View.GONE);
            ivwArrowDebtAmount.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_down_black, null));
        } else {
            lltDebtAmount.setVisibility(View.VISIBLE);
            ivwArrowDebtAmount.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_up_black, null));
        }
    }

    @OnClick(R.id.rlt_debt_note)
    public void showNote() {
        if (lltDebtNote.getVisibility() == View.VISIBLE) {
            lltDebtNote.setVisibility(View.GONE);
            ivwArrowDebtNote.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_down_black, null));

        } else {
            lltDebtNote.setVisibility(View.VISIBLE);
            ivwArrowDebtNote.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_up_black, null));
        }
    }

    @OnClick(R.id.btn_note_add)
    public void onAddNoteClick() {

        clientMovementModel.setNote(edtNoteDescription.getText().toString());
        String amount = edtAmount.getText().toString().trim();

        amount = amount.replace(moneySymbol, "").replace(" ", "").trim();
        Number number = BigDecimal.ZERO;

        try {
            if (showDecimal == 0) amount = amount.replace(".", "");
            number = decimalFormat.parse(amount);
        } catch (ParseException e) {
            BelcorpLogger.w("onAddNoteClick", e);
        }

        if (TextUtils.isEmpty(amount) || number.doubleValue() < 0.01) {
            Toast.makeText(context(), R.string.debt_amount_error, Toast.LENGTH_SHORT).show();
            return;
        }

        clientMovementModel.setEstado(StatusType.CREATE);
        if (null != clientMovementModel.getMovementID() && clientMovementModel.getMovementID() != 0)
            clientMovementModel.setEstado(StatusType.UPDATE);

        clientMovementModel.setAmount(BigDecimal.valueOf(number.doubleValue()));
        debtPresenter.updateNote(clientMovementModel, loginModel);
    }

    /***********************************************************************************************/

    @Override
    public void setData(UserModel model) {
        decimalFormatConstancia = CountryUtil.getDecimalFormatConstanciaByISO(model.getCountryISO(), true);
        decimalFormat = CountryUtil.getDecimalFormatByISO(model.getCountryISO(), false);
        showDecimal = model.getCountryShowDecimal();
        moneySymbol = model.getCountryMoneySymbol();

        tvwCurrency.setText(moneySymbol);
        edtAmount.addTextChangedListener(new EmptyCurrencyTextWatcher(edtAmount, decimalFormat, showDecimal));
        edtAmount.setSelection(edtAmount.getText().length());
        edtAmount.setHint(decimalFormatConstancia.format(0.0));
    }

    @Override
    public void showMovement(ClientMovementModel clientMovementModel) {
        this.clientMovementModel = clientMovementModel;
        this.clientMovementModel.setSaldo(this.clientMovementModel.getSaldo() == null ? BigDecimal.valueOf(0) : this.clientMovementModel.getSaldo());

        String note = clientMovementModel.getNote();

        if (note != null) {
            edtNoteDescription.setText(note);
            edtNoteDescription.setSelection(note.length());
        } else {
            cvwDebtNote.setVisibility(View.GONE);
        }


        if (movementType.equals(MovementType.ABONO)) {
            tvwDebtAmount.setText(R.string.debt_title_payment);

            if (debtListener != null) {
                debtListener.changeToolbar(getString(R.string.debt_title_payment));
            }

            cvwDebtNote.setVisibility(View.GONE);
            BigDecimal amount = clientMovementModel.getAmount();
            String format = decimalFormatConstancia.format(amount.doubleValue() < 0 ? amount.negate() : amount);
            edtAmount.setText(format);
        } else if (movementType.equals(MovementType.CARGO)) {
            tvwDebtAmount.setText(R.string.debt_title_debt);

            if (debtListener != null) {
                debtListener.changeToolbar(getString(R.string.debt_title_debt));
            }

            String format = decimalFormatConstancia.format(clientMovementModel.getAmount());
            edtAmount.setText(format);
        }

        String date = clientMovementModel.getDate();
        try {
            String finalDate = StringUtil.capitalize(DateUtil.convertFechaToString(DateUtil.convertirISODatetoDate(date), "dd MMM"));
            tvwDebtDescription.setText(finalDate + " | " + clientMovementModel.getDescription());
        } catch (ParseException e) {
            tvwDebtDescription.setText(clientMovementModel.getDescription());
        }

        ivwArrowDebtAmount.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_up_black, null));
        ivwArrowDebtNote.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_up_black, null));
    }

    @Override
    public void onEditNote(ClientMovementModel movementModel) {
        this.clientMovementModel = movementModel;

        if (debtListener != null) {
            debtListener.onDebtUpdated(clientMovementModel);
        }

        debtPresenter.initScreenTrackActualizarDeuda(clientMovementModel);
    }

    @Override
    public void initScreenTrackActualizarDeuda(LoginModel loginModel) {
        Tracker.Deudas.trackActualizarDeuda(loginModel);
    }

    @Override
    public void onError(Throwable throwable) {
        processError(throwable);
    }

    /***********************************************************************************************/

    interface DebtListener {
        void onDebtUpdated(ClientMovementModel transactionModel);

        void changeToolbar(String desc);
    }
}

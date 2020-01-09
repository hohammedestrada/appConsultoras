package biz.belcorp.consultoras.feature.history;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.github.jinatonic.confetti.CommonConfetti;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.animation.ripple.RippleView;
import biz.belcorp.consultoras.common.dialog.MessageDialog;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClientMovementModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.recordatorio.RecordatorioModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.feature.debt.AddDebtActivity;
import biz.belcorp.consultoras.feature.debt.SendDebtActivity;
import biz.belcorp.consultoras.feature.history.debt.DebtActivity;
import biz.belcorp.consultoras.feature.history.debtxpedidov2.DebtXPedidoActivity;
import biz.belcorp.consultoras.feature.payment.PaymentActivity;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.MovementType;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.CountryUtil;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.KeyboardUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DebtPaymentHistoryFragment extends BaseFragment implements
    DebtPaymentHistoryView, DebtPaymentHistoryListAdapter.OnItemSelectedListener {

    @Inject
    DebtPaymentHistoryPresenter presenter;

    @BindView(R.id.tvw_title)
    TextView tvwTitle;

    @BindView(R.id.tvw_debt)
    TextView tvwDebt;

    @BindView(R.id.tvw_recordatory)
    TextView tvwRecordatory;

    @BindView(R.id.ivw_arrow_down)
    ImageView ivwArrowDown;

    @BindView(R.id.llt_reminder)
    LinearLayout lltReminder;

    @BindView(R.id.llt_header_content)
    LinearLayout lltHeaderContent;

    @BindView(R.id.rvw_movements)
    RecyclerView rvwMovements;

    @BindView(R.id.rlt_no_movements)
    LinearLayout rltNoMovements;

    @BindView(R.id.llt_without_debt)
    LinearLayout lltWithoutDebt;

    @BindView(R.id.llt_with_debt)
    LinearLayout lltWithDebt;

    @BindView(R.id.llt_confetti)
    FrameLayout fltConfetti;

    @BindView(R.id.ripple_view)
    RippleView rippleView;

    @BindView(R.id.btn_add_debt)
    Button btnAddDebt;
    @BindView(R.id.btn_add_payment)
    Button btnAddPayment;

    private Unbinder bind;

    DebtPaymentHistoryListAdapter adapter;
    private Integer clienteId;
    private Integer clienteLocalID;
    private String clientName;
    private String moneySymbol;

    private BigDecimal totalDebt;
    private BigDecimal firstTotalDebt;
    private DecimalFormat decimalFormat;

    public static final int REQUEST_CODE_ADD_MOVEMENT = 210;
    public static final int REQUEST_ADD_RECORDATORY = 220;
    private boolean fromAddMovement = false;

    private int recordatorioId;
    private int localRecordatoryID;
    private String fechaRecordatorio = "";

    private RecordatorioModel recordatorioModel = null;

    /**********************************************************/

    public DebtPaymentHistoryFragment() {
        super();
    }

    public static DebtPaymentHistoryFragment newInstance() {
        return new DebtPaymentHistoryFragment();
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

        initBundle();
        if (savedInstanceState == null) {
            presenter.load(clienteId, clienteLocalID, false);
        }
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debt_payment_history, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_MOVEMENT && resultCode == Activity.RESULT_OK) {
            fromAddMovement = true;
            presenter.load(clienteId, clienteLocalID, false);
        } else if (requestCode == REQUEST_ADD_RECORDATORY && resultCode == Activity.RESULT_OK) {
            recordatorioId = 0;
            localRecordatoryID = 0;
            fechaRecordatorio = "";
            presenter.load(clienteId, clienteLocalID, true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null) presenter.initScreenTrack();
    }

    @Override
    public void onDestroyView() {
        if (bind != null)
            bind.unbind();
        if (presenter != null)
            presenter.destroy();

        super.onDestroyView();
    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {

        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_DEBT_HISTORY);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    /**********************************************************/

    private void initBundle() {

        boolean isFromReminder = false;

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            clienteId = extras.getInt(GlobalConstant.CLIENTE_ID, -1);
            clienteLocalID = extras.getInt(GlobalConstant.CLIENTE_LOCAL_ID, -1);
            clientName = extras.getString(GlobalConstant.CLIENT_NAME, "");
            totalDebt = new BigDecimal(extras.getString(GlobalConstant.CLIENT_TOTAL_DEBT, "0"));
            isFromReminder = extras.getBoolean(GlobalConstant.FROM_REMINDER, false);
        }

        if (isFromReminder)
            presenter.trackEvent(
                GlobalConstant.EVENT_LABEL_NOT_AVAILABLE,
                GlobalConstant.EVENT_CAT_REMINDER,
                GlobalConstant.EVENT_ACTION_REMINDER,
                GlobalConstant.EVENT_LABEL_REMINDER,
                GlobalConstant.EVENT_NAME_REMINDER);

        rippleView.initLifeCycleCallback(this);
        rippleView.stopRipple();
    }

    private void initParams() {

        String[] parts = clientName.split(Pattern.quote(" "));
        tvwTitle.setText(String.format(getString(R.string.debt_history_title), parts[0]));

        String debt = moneySymbol + " " + decimalFormat.format(totalDebt);
        tvwDebt.setText(debt);

        initializeButtons();
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
    public void showHistory(List<ClientMovementModel> movements, boolean recordatory) {

        lltHeaderContent.setVisibility(View.VISIBLE);
        calculateDebts(movements, recordatory);
        if (movements != null && !movements.isEmpty()) {
            adapter = new DebtPaymentHistoryListAdapter(context(), movements, moneySymbol, decimalFormat, this);

            rvwMovements.setAdapter(adapter);
            rvwMovements.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rvwMovements.setHasFixedSize(true);
            rvwMovements.setNestedScrollingEnabled(false);

            rvwMovements.setOnTouchListener((v, event) -> {
                hideKeyboard();
                return false;
            });
            rvwMovements.setVisibility(View.VISIBLE);
            rltNoMovements.setVisibility(View.GONE);
        } else {
            if (!rippleView.isPlaying()) {
                lltWithDebt.setVisibility(View.VISIBLE);
                lltWithoutDebt.setVisibility(View.GONE); //Ya no aplica el diseño
            }
            rvwMovements.setVisibility(View.GONE);
            rltNoMovements.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void setData(String iso, String moneySymbol) {

        this.decimalFormat = CountryUtil.getDecimalFormatByISO(iso, true);
        this.moneySymbol = moneySymbol;

        initParams();
    }

    @Override
    public void setShowDecimals(int show) {
        // EMPTY
    }

    @Override
    public void showRecordatorio(ClienteModel clienteModel) {
        if (clienteModel.getRecordatorioModels() != null && !clienteModel.getRecordatorioModels().isEmpty() &&
            clienteModel.getRecordatorioModels().get(0) != null && clienteModel.getRecordatorioModels().get(0).getEstado() != -1) {
            recordatorioModel = new RecordatorioModel();
            recordatorioModel = clienteModel.getRecordatorioModels().get(0);

            try {
                recordatorioId = recordatorioModel.getRecordatorioID();
                localRecordatoryID = recordatorioModel.getId();
                fechaRecordatorio = recordatorioModel.getFecha();

                Calendar calendarToday = Calendar.getInstance();
                Date dateRecordatory = DateUtil.convertirISODatetoDate(recordatorioModel.getFecha());

                if (calendarToday.getTime().after(dateRecordatory)) {
                    if (totalDebt.doubleValue() > 0) {
                        tvwRecordatory.setText(String.format(getString(R.string.debt_history_vencida_recordatorio), StringUtil.capitalize(DateUtil.convertFechaToString(dateRecordatory, "dd MMM"))));

                    } else {
                        tvwRecordatory.setText(getString(R.string.debt_history_recordatorio));
                    }
                } else {
                    tvwRecordatory.setText(getString(R.string.debt_history_recordatorio_cobrar) + " " + StringUtil.capitalize(DateUtil.convertFechaToString(dateRecordatory, "dd MMM")));
                }
            } catch (ParseException e) {
                Log.e("DbtPaymntHistoryFragmnt", "ParseException", e);
            }
        } else {
            recordatorioModel = null;
            tvwRecordatory.setText(getString(R.string.debt_history_recordatorio));
        }
    }

    @Override
    public void onError(Throwable throwable) {
        processError(throwable);
    }

    @Override
    public void refresh() {
        fromAddMovement = true;
        presenter.load(clienteId, clienteLocalID, false);
    }

    @Override
    public void deleteRecordatory() {

        tvwRecordatory.setText(getString(R.string.debt_history_recordatorio));

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getActivity()));
        dispatcher.cancel(getString(R.string.reminder_key) + clienteId);
    }

    /**********************************************************/

    @OnClick(R.id.llt_reminder)
    public void addReminder() {
        String[] parts = clientName.split(Pattern.quote(" "));

        Intent intent = new Intent(context(), AddRecordatoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(GlobalConstant.CLIENTE_ID, clienteId);
        intent.putExtra(GlobalConstant.CLIENT_NAME, parts[0]);
        intent.putExtra(GlobalConstant.CLIENT_ID_RECORDATORY, recordatorioId);
        intent.putExtra(GlobalConstant.CLIENT_LOCAL_ID_RECORDATORY, localRecordatoryID);
        intent.putExtra(GlobalConstant.CLIENT_DATE_RECORDATORY, fechaRecordatorio);

        intent.putExtra(GlobalConstant.CLIENTE_LOCAL_ID, clienteLocalID);
        intent.putExtra(GlobalConstant.CLIENT_NAME_2, clientName);
        intent.putExtra(GlobalConstant.CLIENT_TOTAL_DEBT, totalDebt.toString());

        presenter.initScreenTrackAnadirRecordatorio();

        startActivityForResult(intent, REQUEST_ADD_RECORDATORY);
    }

    @OnClick(R.id.btn_add_payment)
    public void addPayment() {

        presenter.initScreenTrackRegistrarUnPago();

        String clientTotalDebt = "0";

        if (totalDebt != null) clientTotalDebt = "" + totalDebt.toString();

        Intent intent = new Intent(context(), PaymentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(GlobalConstant.CLIENTE_ID, clienteId);
        intent.putExtra(GlobalConstant.CLIENTE_LOCAL_ID, clienteLocalID);
        intent.putExtra(GlobalConstant.CLIENT_NAME, clientName);
        intent.putExtra(GlobalConstant.CLIENT_TOTAL_DEBT, clientTotalDebt);
        intent.putExtra(GlobalConstant.MONEY_SYMBOL, moneySymbol);
        startActivityForResult(intent, REQUEST_CODE_ADD_MOVEMENT);
    }

    @OnClick(R.id.rlt_send_debt)
    public void sentDebt() {

        String clientTotalDebt = "0";
        if (totalDebt != null) clientTotalDebt = totalDebt.toString();

        if (totalDebt == null || totalDebt.compareTo(BigDecimal.ZERO) <= 0) {
            Toast.makeText(getActivity(), "El cliente no tiene deuda", Toast.LENGTH_LONG).show();
            return;
        }

        presenter.initScreenTrackEnviarDetalleDeuda();

        Intent intent = new Intent(getActivity(), SendDebtActivity.class);
        intent.putExtra(GlobalConstant.CLIENTE_ID, clienteId);
        intent.putExtra(GlobalConstant.CLIENTE_LOCAL_ID, clienteLocalID);
        intent.putExtra(GlobalConstant.CLIENT_TOTAL_DEBT, clientTotalDebt);
        startActivity(intent);
    }

    @OnClick(R.id.btn_add_debt)
    public void addDebt() {

        presenter.initScreenTrackAnadirDeuda();

        Intent intent = new Intent(context(), AddDebtActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(GlobalConstant.CLIENTE_ID, clienteId);
        intent.putExtra(GlobalConstant.CLIENTE_LOCAL_ID, clienteLocalID);
        intent.putExtra(GlobalConstant.CLIENT_NAME, clientName);
        intent.putExtra(GlobalConstant.MONEY_SYMBOL, moneySymbol);
        startActivityForResult(intent, REQUEST_CODE_ADD_MOVEMENT);
    }

    /****************************************************************/

    private void calculateDebts(List<ClientMovementModel> movements, boolean recordatory) {

        BigDecimal amountTotal = BigDecimal.ZERO;

        if (null != movements && !movements.isEmpty())
            for (ClientMovementModel model : movements) {
                BigDecimal temp = model.getAmount();

                if (model.getType().equals(MovementType.ABONO) && temp.doubleValue() > 0f)
                    temp = temp.negate();

                amountTotal = amountTotal.add(temp);
            }

        String debt = moneySymbol + " " + decimalFormat.format(amountTotal);
        tvwDebt.setText(debt);
        totalDebt = amountTotal;

        if (recordatory) return;

        initializeButtons();
        String[] parts = clientName.split(Pattern.quote(" "));
        tvwTitle.setText(String.format(getString(R.string.debt_history_title), parts[0]));

        int res = amountTotal.compareTo(BigDecimal.ZERO);

        if (res <= 0 && fromAddMovement) {
            if (firstTotalDebt.doubleValue() <= 0 && totalDebt.doubleValue() <= 0) {
            } else {
                lltWithDebt.setVisibility(View.GONE);
                lltWithoutDebt.setVisibility(View.VISIBLE);
                rippleView.startRipple();

                CommonConfetti.rainingConfetti(fltConfetti, new int[]{ContextCompat.getColor(getContext(), R.color.primary)})
                    .stream(3000);

                new Handler().postDelayed(() -> {
                    if (isVisible()) {
                        rippleView.stopRipple();
                        lltWithDebt.setVisibility(View.VISIBLE);
                        lltWithoutDebt.setVisibility(View.GONE);
                        onDeleteRecordatorio();
                    }
                }, 4000);
            }
            firstTotalDebt = amountTotal;
        } else {
            lltWithDebt.setVisibility(View.VISIBLE);
            lltWithoutDebt.setVisibility(View.GONE);

            if (firstTotalDebt == null) {
                firstTotalDebt = amountTotal;
            } else {
                valueAnimation();
            }
        }

        fromAddMovement = false;
    }

    public void valueAnimation() {

        int first = firstTotalDebt.intValue();
        int last = totalDebt.intValue();

        int diff = first - last;
        final boolean animationWay = diff <= 0;
        diff = (animationWay ? diff * -1 : diff) / 100;

        if (!animationWay) ivwArrowDown.setVisibility(View.VISIBLE);

        int duration = 400 * diff;

        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivwArrowDown, "Alpha", 0f, 1f);
        objectAnimator.setDuration(duration < 1000 ? 400 : 600);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setInterpolator(new LinearInterpolator());

        duration = duration < 1000 ? 1000 : duration;
        duration = duration > 2000 ? 2000 : duration;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(first, last);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            String debt = moneySymbol + " " + decimalFormat.format(BigDecimal.valueOf(Double.parseDouble(valueAnimator1.getAnimatedValue().toString())));

            if (isVisible()) {
                tvwDebt.setText(debt);
                initializeButtons();
            } else
                valueAnimator1.cancel();
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                String debt = moneySymbol + " " + decimalFormat.format(totalDebt);

                if (isVisible()) {
                    if (!animationWay) {
                        objectAnimator.cancel();
                        objectAnimator.end();
                        ivwArrowDown.setVisibility(View.GONE);
                    }

                    tvwDebt.setText(debt);
                    initializeButtons();
                    firstTotalDebt = totalDebt;
                }
            }
        });

        valueAnimator.start();
        if (!animationWay)
            objectAnimator.start();
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view == null) view = new View(getActivity());
        KeyboardUtil.dismissKeyboard(getActivity(), view);
    }

    private void initializeButtons() {
        if (totalDebt.compareTo(BigDecimal.ZERO) <= 0) {
            btnAddDebt.setBackgroundResource(R.drawable.selector_button_primary);
            btnAddDebt.setTextColor(Color.WHITE);
            btnAddPayment.setBackgroundResource(R.drawable.selector_button_alternative);
            btnAddPayment.setTextColor(Color.BLACK);
        } else {
            btnAddDebt.setBackgroundResource(R.drawable.selector_button_alternative);
            btnAddDebt.setTextColor(Color.BLACK);
            btnAddPayment.setBackgroundResource(R.drawable.selector_button_primary);
            btnAddPayment.setTextColor(Color.WHITE);
        }
    }

    @Override
    public void onClienteModelClick(ClientMovementModel clientMovementModel) {
        edit(clientMovementModel);
    }

    @Override
    public void onClienteModelLongClick(ClientMovementModel clientMovementModel) {
        if (clientMovementModel != null) {
            String label;
            String eventName;

            if (clientMovementModel.getType().equals(MovementType.ABONO)) {
                label = GlobalConstant.EVENT_LABEL_PAYMENT_LONG_CLICK;
                eventName = GlobalConstant.EVENT_NAME_PAYMENT_LONG_CLICK;
            } else {
                label = GlobalConstant.EVENT_LABEL_DEBT_LONG_CLICK;
                eventName = GlobalConstant.EVENT_NAME_DEBT_LONG_CLICK;
            }

            presenter.trackEvent(
                GlobalConstant.SCREEN_DEBT_HISTORY,
                GlobalConstant.EVENT_CAT_DEBT,
                GlobalConstant.EVENT_ACTION_DEBT,
                label,
                eventName);

            drawTooltipOptionsDeuda(clientMovementModel, new OnPopupOptionSelectedListener() {
                @Override
                public void editar(ClientMovementModel clientemodel) {
                    edit(clientemodel);
                }

                @Override
                public void eliminar(final ClientMovementModel clienteModel) {
                    try {
                        new MessageDialog()
                            .setIcon(R.drawable.ic_alerta, 0)
                            .setStringTitle(R.string.debt_dg_delete_title)
                            .setStringMessage(R.string.debt_dg_delete_message)
                            .setStringAceptar(R.string.button_aceptar)
                            .setStringCancelar(R.string.button_cancelar)
                            .showIcon(true)
                            .showClose(true)
                            .showCancel(true)
                            .setListener(new MessageDialog.MessageDialogListener() {
                                @Override
                                public void aceptar() {
                                    String label;
                                    String eventName;

                                    if (clienteModel.getType().equals(MovementType.ABONO)) {
                                        label = GlobalConstant.EVENT_LABEL_PAYMENT_DELETE;
                                        eventName = GlobalConstant.EVENT_NAME_PAYMENT_DELETE;
                                    } else {
                                        label = GlobalConstant.EVENT_LABEL_DEBT_DELETE;
                                        eventName = GlobalConstant.EVENT_NAME_DEBT_DELETE;
                                    }

                                    presenter.trackEvent(
                                        GlobalConstant.SCREEN_DEBT_HISTORY,
                                        GlobalConstant.EVENT_CAT_DEBT,
                                        GlobalConstant.EVENT_ACTION_DEBT,
                                        label,
                                        eventName);

                                    if (clienteModel.getMovementID() != null
                                        && !clienteModel.getMovementID().equals(0)) {
                                        presenter.delete(clienteModel);
                                    } else {
                                        presenter.deleteOffline(clienteModel);
                                    }
                                }

                                @Override
                                public void cancelar() {
                                    // EMPTY
                                }
                            })
                            .show(getFragmentManager(), "modalDelete");
                    } catch (IllegalStateException e) {
                        BelcorpLogger.w("modalDelete", e);
                    }
                }
            });
        }
    }

    private void edit(ClientMovementModel clientMovementModel) {
        Intent intent;
        switch (clientMovementModel.getType()) {
            case MovementType.ABONO:
                presenter.trackEvent(
                    GlobalConstant.SCREEN_DEBT_PAGO,
                    GlobalConstant.EVENT_CATEGORY_DEUDAS,
                    GlobalConstant.EVENT_ACTION_MOVIMIENTO_CLIENTE,
                    GlobalConstant.EVENT_LABEL_PAGO,
                    GlobalConstant.EVENT_NAME_PAYMENT_EDIT);

                intent = new Intent(getContext(), DebtActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(GlobalConstant.CLIENTE_ID, clientMovementModel.getClientID());
                intent.putExtra(GlobalConstant.TRANSACTION, clientMovementModel.getId());
                intent.putExtra(GlobalConstant.CLIENT_PAYMENT, clientMovementModel.getId());
                intent.putExtra(GlobalConstant.MOVEMENT_TYPE, MovementType.ABONO);
                startActivityForResult(intent, REQUEST_CODE_ADD_MOVEMENT);
                break;
            case MovementType.CARGO:
                presenter.trackEvent(
                    GlobalConstant.SCREEN_DEBT_DEUDA_AGREGADA,
                    GlobalConstant.EVENT_CATEGORY_DEUDAS,
                    GlobalConstant.EVENT_ACTION_MOVIMIENTO_CLIENTE,
                    GlobalConstant.EVENT_LABEL_DEUDA_AGREGADA,
                    GlobalConstant.EVENT_NAME_DEBT_EDIT);

                intent = new Intent(getContext(), DebtActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(GlobalConstant.CLIENTE_ID, clientMovementModel.getClientID());
                intent.putExtra(GlobalConstant.TRANSACTION, clientMovementModel.getId());
                intent.putExtra(GlobalConstant.CLIENT_PAYMENT, clientMovementModel.getId());
                intent.putExtra(GlobalConstant.MOVEMENT_TYPE, MovementType.CARGO);
                startActivityForResult(intent, REQUEST_CODE_ADD_MOVEMENT);
                break;

            case MovementType.CARGO_BELCORP:
                presenter.trackEvent(
                    GlobalConstant.SCREEN_DEBT_PEDIDO_BELCORP,
                    GlobalConstant.EVENT_CATEGORY_DEUDAS,
                    GlobalConstant.EVENT_ACTION_MOVIMIENTO_CLIENTE,
                    GlobalConstant.EVENT_LABEL_PEDIDO_BELCORP,
                    GlobalConstant.EVENT_NAME_DEBT_EDIT);

                intent = new Intent(getContext(), DebtXPedidoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(GlobalConstant.CLIENTE_ID, clientMovementModel.getClientID());
                intent.putExtra(GlobalConstant.TRANSACTION, clientMovementModel.getId());
                intent.putExtra(GlobalConstant.CLIENT_PAYMENT, clientMovementModel.getId());
                intent.putExtra(GlobalConstant.CLIENT_NAME, clientName);
                String[] parts = clientName.split(Pattern.quote(" "));
                intent.putExtra(GlobalConstant.CLIENT_NAME_2, parts[0]);
                intent.putExtra(GlobalConstant.CLIENT_TOTAL_DEBT, totalDebt.toString());
                startActivityForResult(intent, REQUEST_CODE_ADD_MOVEMENT);
                break;
            default:
                break;
        }
    }

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_DEBT_HISTORY);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);
    }

    @Override
    public void initScreenTrackAnadirRecordatorio(LoginModel loginModel) {
        Tracker.DetalleDeuda.trackAnadirRecordatorio(loginModel);
    }

    @Override
    public void initScreenTrackAnadirDeuda(LoginModel loginModel) {
        Tracker.DetalleDeuda.trackAnadirDeuda(loginModel);
    }

    @Override
    public void initScreenTrackRegistrarUnPago(LoginModel loginModel) {
        Tracker.DetalleDeuda.trackRegistrarUnPago(loginModel);
    }

    @Override
    public void initScreenTrackEnviarDetalleDeuda(LoginModel loginModel) {
        Tracker.DetalleDeuda.trackEnviarDetalleDeuda(loginModel);
    }

    public void trackBackPressed() {
        presenter.trackBackPressed();
    }

    protected void drawTooltipOptionsDeuda(final ClientMovementModel clientMovementModel, final OnPopupOptionSelectedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.layout_debts_options);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView tvwTooltipTitle = alertDialog.findViewById(R.id.tvw_tooltip_title);
        TextView tvwTooltipEditar = alertDialog.findViewById(R.id.tvw_tooltip_editar);
        TextView tvwTooltipEliminar = alertDialog.findViewById(R.id.tvw_tooltip_eliminar);

        if (clientMovementModel.getType().equals(MovementType.CARGO_BELCORP) ||
            clientMovementModel.getType().equals(MovementType.HISTORICO)) {
            alertDialog.findViewById(R.id.vie_middle_line).setVisibility(View.GONE);
            tvwTooltipEliminar.setVisibility(View.GONE);
        }

        tvwTooltipTitle.setText(clientMovementModel.getDescription());

        tvwTooltipEditar.setOnClickListener(v -> {
            alertDialog.dismiss();
            listener.editar(clientMovementModel);
        });
        tvwTooltipEliminar.setOnClickListener(v -> {
            alertDialog.dismiss();
            listener.eliminar(clientMovementModel);
        });

    }

    protected void drawTooltipOptionsPago(final ClientMovementModel clientMovementModel, final OnPopupOptionSelectedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.layout_payments_options);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView tvwTooltipTitle = alertDialog.findViewById(R.id.tvw_tooltip_title);
        TextView tvwTooltipEliminar = alertDialog.findViewById(R.id.tvw_tooltip_eliminar);

        tvwTooltipTitle.setText(clientMovementModel.getDescription());

        tvwTooltipEliminar.setOnClickListener(v -> {
            alertDialog.dismiss();
            listener.eliminar(clientMovementModel);
        });

    }

    protected void drawTooltipOptionsDeudaPedido(final ClientMovementModel clientMovementModel, final OnPopupOptionSelectedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.layout_debts_options);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView tvwTooltipTitle = alertDialog.findViewById(R.id.tvw_tooltip_title);
        TextView tvwTooltipEditar = alertDialog.findViewById(R.id.tvw_tooltip_editar);

        tvwTooltipTitle.setText(clientMovementModel.getDescription());

        tvwTooltipEditar.setOnClickListener(v -> {
            alertDialog.dismiss();
            listener.editar(clientMovementModel);
        });
    }

    public void onDeleteRecordatorio() {
        if (null != recordatorioModel) {

            new Handler().postDelayed(() -> {
                if (isVisible()) {
                    try {
                        new MessageDialog()
                            .setIcon(R.drawable.ic_alerta, 0)
                            .setStringTitle(R.string.debt_history_debt_dg_delete_title)
                            .setStringMessage(R.string.debt_history_debt_dg_delete_message)
                            .setStringAceptar(R.string.button_aceptar)
                            .setStringCancelar(R.string.button_cancelar)
                            .showIcon(true)
                            .showClose(true)
                            .showCancel(true)
                            .setListener(deleteListener)
                            .show(getFragmentManager(), "modalDelete");
                    } catch (IllegalStateException e) {
                        BelcorpLogger.w("modalDelete", e);
                    }
                }
            }, 300);

        }
    }

    /*****************************************************/

    final MessageDialog.MessageDialogListener deleteListener = new MessageDialog.MessageDialogListener() {
        @Override
        public void aceptar() {
            presenter.eliminarRecordatory(recordatorioModel.getId(), clienteId, clienteLocalID);
        }

        @Override
        public void cancelar() {
            // EMPTY
        }
    };


    interface OnPopupOptionSelectedListener {
        void editar(ClientMovementModel clientemodel);

        void eliminar(ClientMovementModel clienteModel);
    }

}

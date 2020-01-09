package biz.belcorp.consultoras.feature.history.debtxpedidov2;


import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.dialog.DeudaDialog;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClientMovementModel;
import biz.belcorp.consultoras.common.model.product.ProductModel;
import biz.belcorp.consultoras.common.model.user.UserModel;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.util.CountryUtil;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.KeyboardUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 */
public class DebtXPedidoFragment extends BaseFragment implements DebtXPedidoView {

    @Inject
    DebtXPedidoPresenter debtXPedidoPresenter;

    @BindView(R.id.rvw_debt_productos)
    LinearLayout rvwDebtProductos;

    @BindView(R.id.rlt_main)
    LinearLayout rltMain;
    @BindView(R.id.tvw_total_antes)
    TextView tvwTotalAntes;
    @BindView(R.id.tvw_total)
    TextView tvwTotal;
    @BindView(R.id.tvw_debt_description)
    TextView tvwDebtDescription;

    private Unbinder bind;

    private ReminderListener reminderListener;

    private ClientMovementModel clientMovementModel;
    private LoginModel loginModel;

    private DecimalFormat decimalFormat;
    private String moneySymbol;
    private int showDecimal = 1;
    private boolean firsTimeChangeDebt = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ReminderListener)
            reminderListener = (ReminderListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_debt_x_pedido_v2, container, false);
        bind = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (debtXPedidoPresenter != null) debtXPedidoPresenter.initScreenTrack();
    }

    @Override
    public void onDestroyView() {

        if (bind != null)
            bind.unbind();
        if (debtXPedidoPresenter != null)
            debtXPedidoPresenter.destroy();

        super.onDestroyView();
    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        this.loginModel = loginModel;

        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_DEBT_PEDIDO_BELCORP);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);
        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_DEBT_PEDIDO_BELCORP);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);
    }

    public void trackBackPressed() {
        debtXPedidoPresenter.trackBackPressed();
    }

    /***********************************************************************************************/

    @Override
    protected boolean onInjectView() throws IllegalStateException {
        getComponent(ClientComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.debtXPedidoPresenter.attachView(this);

        Bundle bundle = getArguments();

        if (bundle != null) {
            int movementLocalID = bundle.getInt(GlobalConstant.TRANSACTION);
            debtXPedidoPresenter.getMovement(movementLocalID);
        }
    }

    @Override
    public Context context() {
        return getActivity();
    }

    /***********************************************************************************************/

    @OnClick(R.id.llt_aplicar_dcto)
    public void aplicarDcto() {

        final BigDecimal anterior = calculateMontoAnterior();

        final DeudaDialog deudaDialog = new DeudaDialog(getContext());
        deudaDialog.setButtonAction1Listener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deudaDialog.dismiss();
                String sTotal = deudaDialog.getEdtAmount().getText().toString();
                if (sTotal != null && !sTotal.isEmpty()) {
                    BigDecimal total = BigDecimal.valueOf(
                        Double.parseDouble(sTotal));

                    if (total.compareTo(BigDecimal.ZERO) > 0) {

                        if (total.compareTo(anterior) != 0) {
                            tvwTotalAntes.setVisibility(View.VISIBLE);
                            tvwTotalAntes.setPaintFlags(tvwTotalAntes.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            if (firsTimeChangeDebt) {
                                tvwTotalAntes.setText(moneySymbol + " " + decimalFormat.format(anterior));
                                firsTimeChangeDebt = false;
                            }
                        } else {
                            tvwTotalAntes.setVisibility(View.GONE);
                        }

                        clientMovementModel.setAmount(total);
                        debtXPedidoPresenter.updateProductos(clientMovementModel, loginModel);
                        debtXPedidoPresenter.trackEvent(
                            GlobalConstant.SCREEN_DEBT_PEDIDO_BELCORP,
                            GlobalConstant.EVENT_CATEGORY_DEUDAS,
                            GlobalConstant.EVENT_ACTION_MOVIMIENTO_CLIENTE,
                            GlobalConstant.EVENT_LABEL_APLICAR_DESCUENTO,
                            GlobalConstant.EVENT_NAME_CLIENT_DEBT);
                        tvwTotal.setText(moneySymbol + " " + decimalFormat.format(total));
                    } else {
                        Toast.makeText(getContext(), "Ingrese un monto válido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Ingrese un monto válido", Toast.LENGTH_SHORT).show();
                }


            }
        });
        deudaDialog.show();
        deudaDialog.getTvwCurrency().setText(moneySymbol);
        deudaDialog.getTvwMonto().setText(moneySymbol + " " + decimalFormat.format(anterior));
        deudaDialog.getEdtAmount().setHint(decimalFormat.format(0));

        deudaDialog.getEdtAmount().postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyboardUtil.showKeyboard(getContext(), deudaDialog.getEdtAmount());
            }
        }, 100);


        debtXPedidoPresenter.trackEvent(
            GlobalConstant.SCREEN_DEBT_PEDIDO_BELCORP,
            GlobalConstant.EVENT_CATEGORY_DEUDAS,
            GlobalConstant.EVENT_ACTION_MOVIMIENTO_CLIENTE,
            GlobalConstant.EVENT_LABEL_CLIC_APLICAR_DESCUENTO,
            GlobalConstant.EVENT_NAME_CLIENT_DEBT);
    }

    private BigDecimal calculateMontoAnterior() {
        List<ProductModel> productModelList = clientMovementModel.getProductModels();
        BigDecimal anterior = BigDecimal.valueOf(0);
        for (ProductModel productModel : productModelList) {
            anterior = anterior.add(BigDecimal.valueOf(productModel.getPrice())
                .multiply(BigDecimal.valueOf(productModel.getQuantity())));
        }
        return anterior;
    }

    @OnClick(R.id.llt_enviar_detalle)
    public void enviarDetalle() {

    }

    /***********************************************************************************************/

    @Override
    public void setData(UserModel model) {
        showDecimal = model.getCountryShowDecimal();
        moneySymbol = model.getCountryMoneySymbol();
        decimalFormat = CountryUtil.getDecimalFormatConstanciaByISO(model.getCountryISO(), false);

    }

    @Override
    public void onProductosUpdated() {
        if (reminderListener != null) {
            reminderListener.onDebtUpdated(clientMovementModel);
        }
    }

    @Override
    public void showMovement(ClientMovementModel clientMovementModel) {
        this.clientMovementModel = clientMovementModel;
        this.clientMovementModel.setSaldo(this.clientMovementModel.getSaldo() == null ? BigDecimal.valueOf(0) : this.clientMovementModel.getSaldo());

        if (null == clientMovementModel.getProductModels() || clientMovementModel.getProductModels().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.debt_order_zero_msg), Toast.LENGTH_SHORT).show();
            getActivity().finish();
        } else {
            populateProducts();
            tvwTotal.setText(moneySymbol + " " + decimalFormat
                .format(clientMovementModel.getAmount()));
            BigDecimal anterior = calculateMontoAnterior();
            if (clientMovementModel.getAmount().compareTo(anterior) != 0) {
                tvwTotalAntes.setVisibility(View.VISIBLE);
                tvwTotalAntes.setText(moneySymbol + " " + decimalFormat.format(anterior));
                tvwTotalAntes.setPaintFlags(tvwTotalAntes.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }

        String date = clientMovementModel.getDate();
        try {
            String finalDate = StringUtil.capitalize(DateUtil.convertFechaToString(DateUtil.convertirISODatetoDate(date), "dd MMM"));
            tvwDebtDescription.setText(finalDate + " | " + clientMovementModel.getDescription());
        } catch (ParseException e) {
            tvwDebtDescription.setText(clientMovementModel.getDescription());
        }


    }

    @Override
    public void onEditNote(ClientMovementModel movementModel) {
        clientMovementModel = movementModel;

        if (reminderListener != null) {
            reminderListener.onDebtUpdated(clientMovementModel);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        processError(throwable);
    }

    public ClientMovementModel getClientMovementModel() {
        return clientMovementModel;
    }


    /***********************************************************************************************/

    interface ReminderListener {
        void onDebtUpdated(ClientMovementModel transactionModel);

    }

    /***********************************************************************************************/

    private class ProductoHolder {

        private ProductModel productModel;
        private View v;
        //        private TextView tvwProductoCantidad;
        private TextView tvwProductoDescripcion;
        private TextView edtProductoPrecio;
        private TextView tvwProductoCurrency;
        private LinearLayout lltItemProductoContainer;
//        private TextView tvwSubtotal;

        private ProductoHolder(ProductModel productModel) {
            this.productModel = productModel;
        }
    }

    List<ProductoHolder> productoHolderList = new ArrayList<>();

    public void populateProducts() {
        final List<ProductModel> productModels = clientMovementModel.getProductModels();
        for (int i = 0; i < productModels.size(); i++) {
            productoHolderList.add(new ProductoHolder(productModels.get(i)));
        }

        int position = 0;
        for (final ProductoHolder holder : productoHolderList) {
            holder.v = LayoutInflater.from(getContext()).inflate(R.layout.item_debt_producto_v2, null);
            holder.tvwProductoDescripcion = holder.v.findViewById(R.id.tvw_producto_descripcion);
            holder.edtProductoPrecio = holder.v.findViewById(R.id.edt_producto_precio);
            holder.tvwProductoCurrency = holder.v.findViewById(R.id.edt_producto_currency);
            holder.lltItemProductoContainer = holder.v.findViewById(R.id.llt_item_producto_container);

            SpannableStringBuilder spannableString = new SpannableStringBuilder();

            String codigo = holder.productModel.getCuv();
            Spannable totalDebtSpannable = Spannable.Factory.getInstance().newSpannable(codigo);
            totalDebtSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, codigo.length(), 0);

            String descripcion = holder.productModel.getName();
            Spannable totalDebtSpannable2 = Spannable.Factory.getInstance().newSpannable(descripcion);
            totalDebtSpannable2.setSpan(new StyleSpan(Typeface.NORMAL), 0, descripcion.length(), 0);

            spannableString.append("X" + holder.productModel.getQuantity() + " " + totalDebtSpannable2);

            holder.tvwProductoDescripcion.setText(spannableString);

            holder.tvwProductoCurrency.setText(moneySymbol);
            holder.edtProductoPrecio.setHint(decimalFormat.format(0.0));
            holder.edtProductoPrecio.setText(decimalFormat.format(holder.productModel.getPrice()));
            rvwDebtProductos.addView(holder.v);
            holder.edtProductoPrecio.setTag(position);
            position++;
        }

    }

    public void disableAllProducts() {
        for (ProductoHolder holder : productoHolderList) {
            holder.lltItemProductoContainer.setBackground(ContextCompat.getDrawable(
                getContext(), R.drawable.shape_box_card_view_client_inactive));
        }
    }
}

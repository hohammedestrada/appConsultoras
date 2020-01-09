package biz.belcorp.consultoras.feature.history;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClientMovementModel;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.feature.history.debt.DebtActivity;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.MovementType;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.util.CountryUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;

public class DebtPaymentDetailMovementFragment extends BaseFragment implements DebtPaymentDetailMovementView, NotesAdapter.NoteListener {

    @BindView(R.id.llt_notas_data)
    LinearLayout lltNotasData;
    @BindView(R.id.llt_pedidos_data)
    LinearLayout lltPedidosData;
    @BindView(R.id.llt_content_notas)
    LinearLayout lltContentNotasData;
    @BindView(R.id.llt_content_pedidos)
    LinearLayout lltContentPedidosData;
    @BindView(R.id.ivw_arrow_notas)
    ImageView ivwArrowNotas;
    @BindView(R.id.ivw_arrow_pedidos)
    ImageView ivwArrowPedidos;
    @BindView(R.id.tvw_notas_data)
    TextView tvwNotasData;
    @BindView(R.id.tvw_pedidos_data)
    TextView tvwPedidosData;

    @BindView(R.id.cdv_notes)
    CardView cdvNotes;
    @BindView(R.id.cdv_orders)
    CardView cdvOrders;

    @BindView(R.id.rvw_pedidos)
    RecyclerView rvOrders;
    @BindView(R.id.rvw_notas)
    RecyclerView rvNotes;

    @Inject
    protected DebtPaymentDetailMovementPresenter detailMovementPresenter;

    private Unbinder bind;

    private String iso;
    private String symbol;
    private ClientMovementModel clientMovementModel;

    /**********************************************************/

    public static DebtPaymentDetailMovementFragment newInstance() {
        return new DebtPaymentDetailMovementFragment();
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
        this.detailMovementPresenter.attachView(this);

        Bundle bundle = getArguments();
        if (savedInstanceState == null && bundle != null) {
            rvNotes.setNestedScrollingEnabled(false);
            rvOrders.setNestedScrollingEnabled(false);

            rvOrders.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rvNotes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

            int movementID = bundle.getInt(GlobalConstant.CLIENT_PAYMENT, 0);

            detailMovementPresenter.getMovement(movementID);
        }
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_movements, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showHideNotas();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(detailMovementPresenter != null) detailMovementPresenter.initScreenTrack();
    }

    @Override
    public void onDestroyView() {

        if (bind != null)
            bind.unbind();
        if (detailMovementPresenter != null)
            detailMovementPresenter.destroy();

        super.onDestroyView();
    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {

        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_DEBT_DETAIL);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    /**********************************************************/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            int movementLocalID = data.getIntExtra(GlobalConstant.TRANSACTION, 0);

            detailMovementPresenter.getMovement(movementLocalID);
        }
    }

    /**********************************************************/

    @OnClick(R.id.rlt_notas_data)
    public void showHideNotas() {
        if (lltContentNotasData.getVisibility() == View.VISIBLE) {
            lltContentNotasData.setVisibility(View.GONE);
            lltNotasData.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_box_card_view_inactive));
            ivwArrowNotas.setImageDrawable(VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_arrow_down_black, null));
            tvwNotasData.setTextColor(ContextCompat.getColor(getActivity(), R.color.card_view_text_title_inactive));
        } else {
            lltContentNotasData.setVisibility(View.VISIBLE);
            lltNotasData.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_box_card_view));
            ivwArrowNotas.setImageDrawable(VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_arrow_up_black, null));
            tvwNotasData.setTextColor(ContextCompat.getColor(getActivity(), R.color.card_view_text_title_active));
        }
    }

    @OnClick(R.id.rlt_pedidos)
    public void showHidePedidos() {
        if (lltContentPedidosData.getVisibility() == View.VISIBLE) {
            lltContentPedidosData.setVisibility(View.GONE);
            lltPedidosData.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_box_card_view_inactive));
            ivwArrowPedidos.setImageDrawable(VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_arrow_down_black, null));
            tvwPedidosData.setTextColor(ContextCompat.getColor(getActivity(), R.color.card_view_text_title_inactive));
        } else {
            lltContentPedidosData.setVisibility(View.VISIBLE);
            lltPedidosData.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_box_card_view));
            ivwArrowPedidos.setImageDrawable(VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_arrow_up_black, null));
            tvwPedidosData.setTextColor(ContextCompat.getColor(getActivity(), R.color.card_view_text_title_active));
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

    @Override
    public void showMovement(ClientMovementModel clientMovementModel) {
        this.clientMovementModel = clientMovementModel;
        if (!clientMovementModel.getType().equals(MovementType.CARGO_BELCORP)) {
            cdvNotes.setVisibility(View.VISIBLE);
            cdvOrders.setVisibility(View.GONE);

            List<String> notes = new ArrayList<>();
            notes.add(clientMovementModel.getNote());

            NotesAdapter notesAdapter = new NotesAdapter(this);
            notesAdapter.setNotes(notes);

            rvNotes.setAdapter(notesAdapter);
        } else {
            cdvNotes.setVisibility(View.GONE);
            cdvOrders.setVisibility(View.VISIBLE);

            OrdersAdapter ordersAdapter = new OrdersAdapter();
            ordersAdapter.setCurrencySymbol(symbol);
            ordersAdapter.setDecimalFormat(CountryUtil.getDecimalFormatByISO(iso, true));
            ordersAdapter.setProductModels(clientMovementModel.getProductModels());

            rvOrders.setAdapter(ordersAdapter);

            showHidePedidos();
        }
    }

    @Override
    public void onEditNote(Intent intent) {
        startActivityForResult(intent, 1234);
    }

    @Override
    public void onEditNote(ClientMovementModel movementModel) {
        showMovement(movementModel);
    }

    @Override
    public void onError(Throwable throwable) {
        processError(throwable);
    }

    @Override
    public void setData(String iso, String symbol) {
        this.iso = iso;
        this.symbol = symbol;
    }

    /**********************************************************/

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_DEBT_DETAIL);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);
    }

    public void trackBackPressed() {
        detailMovementPresenter.trackBackPressed();
    }

    /**********************************************************/

    @Override
    public void onEditNote(int position) {
        Intent intent = new Intent(getContext(), DebtActivity.class);
        intent.putExtra(GlobalConstant.TRANSACTION, clientMovementModel.getId());
        intent.putExtra(GlobalConstant.CLIENTE_LOCAL_ID, clientMovementModel.getClienteLocalID());
    }

    @Override
    public void onDeleteNote(int position) {
        detailMovementPresenter.deleteNote(clientMovementModel);
    }
}

package biz.belcorp.consultoras.feature.stockouts;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.domain.entity.Menu;
import biz.belcorp.consultoras.domain.entity.Product;
import biz.belcorp.consultoras.feature.stockouts.di.StockoutsComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.MenuCodeTop;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.util.KeyboardUtil;
import biz.belcorp.library.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class StockoutsFragment extends BaseFragment implements StockoutsView,
    StockoutsListAdapter.OnStockoutsItemListener {

    @Inject
    StockoutsPresenter presenter;

    @BindView(R.id.rlt_content_list_null)
    RelativeLayout rltNoStockouts;

    @BindView(R.id.tvw_message)
    TextView tvwMessage;

    @BindView(R.id.rlt_content)
    RelativeLayout rltContent;

    @BindView(R.id.edt_filter)
    EditText edtFilter;

    @BindView(R.id.ivw_search)
    ImageView ivwSearch;

    @BindView(R.id.rvw_stockouts)
    RecyclerView rvwStockouts;

    @BindView(R.id.tvw_counter)
    TextView tvwCounter;

    Unbinder unbinder;

    Listener listener;

    private Handler refreshDataHandler;
    private Runnable refreshDataRunnable;

    private StockoutsListAdapter adapter;

    /**********************************************************/

    public StockoutsFragment() {
        super();
    }

    public static StockoutsFragment newInstance() {
        return new StockoutsFragment();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(StockoutsComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);
        this.presenter.getMenuActive(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE);
        this.presenter.data();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StockoutsFragment.Listener) {
            listener = (StockoutsFragment.Listener) context;
        }
    }

    /**********************************************************/

    @Override
    public void onGetMenu(Menu menu) {
        if (listener != null) {
            listener.onGetMenu(menu);
        }
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stockouts, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initHandler();
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != presenter) presenter.destroy();
    }

    /**********************************************************/

    @Override
    public void showSearchOption() {
        ((StockoutsActivity)getActivity()).showSearchOption();
    }

    @Override
    public void initScreenTrack(LoginModel loginModel) {

        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_SPENT_ORDER);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);
        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }


    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_SPENT_ORDER);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);

    }

    @Override
    public void show(List<Product> list) {
        if (!isVisible()) return;

        if (null != list && !list.isEmpty()) {

            String counter = getString(R.string.stockouts_counter) + " (" + list.size() + ")";
            tvwCounter.setText(counter);

            adapter = new StockoutsListAdapter(getContext(), list, this);
            rvwStockouts.setAdapter(adapter);
            rvwStockouts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rvwStockouts.setHasFixedSize(true);

            rvwStockouts.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard();
                    return false;
                }
            });

            rltContent.setVisibility(View.VISIBLE);
            rltNoStockouts.setVisibility(View.GONE);

            textChange();
        } else {

            if (NetworkUtil.isThereInternetConnection(context()))
                tvwMessage.setText(R.string.stockouts_no_items);
            else
                tvwMessage.setText(R.string.stockouts_no_items_conexion);
            tvwMessage.setVisibility(View.VISIBLE);

            rltContent.setVisibility(View.GONE);
            rltNoStockouts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(ErrorModel exception) {
        processError(exception);
    }

    @Override
    public void refreshCounter() {
        if (!isVisible()) return;
        String counter = getString(R.string.stockouts_counter) + " (" + adapter.getItemCount() + ")";
        tvwCounter.setText(counter);
    }

    /**********************************************************/

    public void trackBackPressed() {
        presenter.trackBackPressed();
    }

    private void initHandler() {
        refreshDataHandler = new Handler();
        refreshDataRunnable = () -> {
            if(presenter != null) presenter.initScreenTrack();
        };
    }

    private void textChange() {
        edtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                boolean edtState = cs.toString().trim().length() > 0;
                ivwSearch.setImageResource(edtState ? R.drawable.ic_close_black : R.drawable.ic_lupe_black);
                ivwSearch.setTag(edtState ? "1" : "0");

                StockoutsFragment.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // EMPTY
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // EMPTY
            }
        });
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view == null) view = new View(getActivity());
        KeyboardUtil.dismissKeyboard(getActivity(), view);
    }

    /**********************************************************/

    @OnClick(R.id.ivw_search)
    protected void onFilterClick(View v) {

        if (v.getTag().toString().equals("1")) {
            edtFilter.getText().clear();
            ((ImageView) v).setImageResource(R.drawable.ic_lupe_black);
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

    interface Listener {
        void onGetMenu(Menu menu);
    }

}

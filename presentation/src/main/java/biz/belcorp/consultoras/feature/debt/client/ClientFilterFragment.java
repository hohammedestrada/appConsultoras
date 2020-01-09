package biz.belcorp.consultoras.feature.debt.client;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.feature.debt.di.DebtComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public class ClientFilterFragment extends BaseFragment implements ClientFilterView, ClientFilterAdapter.OnClientFilteredClick {

    @Inject
    protected ClientFilterPresenter presenter;

    @BindView(R.id.edt_client_filter)
    protected EditText edtClientFilter;
    @BindView(R.id.rvw_clients)
    protected RecyclerView rvwClients;

    private OnFilterCompleteListener onFilterCompleteListener;
    private ClientFilterAdapter filterAdapter;
    private Unbinder unbinder;

    /******************************************************/

    public ClientFilterFragment() {
        super();
    }

    /******************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(DebtComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);
        init();
    }

    /******************************************************/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFilterCompleteListener)
            onFilterCompleteListener = (OnFilterCompleteListener) context;
    }

    @Override
    public void onStart() {
        super.onStart();

        edtClientFilter.requestFocus();
    }

    @Override
    public void onStop() {
        super.onStop();

        edtClientFilter.clearFocus();
    }

    @Override
    public void onDestroyView() {

        if (unbinder != null)
            unbinder.unbind();

        super.onDestroyView();
    }

    /******************************************************/

    private void init() {
        if (filterAdapter == null)
            filterAdapter = new ClientFilterAdapter(this);

        rvwClients.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvwClients.setAdapter(filterAdapter);

        edtClientFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                ClientFilterFragment.this.filterAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // EMPTY
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // EMPTY
            }
        });

        if (filterAdapter.getItemCount() == 0) {
            if (NetworkUtil.isThereInternetConnection(getContext())) {
                showLoading();
                presenter.getClients();
            } else {
                presenter.getClientsOffline();
            }
        }
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

    @Override
    public void onClick(ClienteModel clienteModel) {
        onFilterCompleteListener.onComplete(clienteModel);
    }

    @Override
    public void showClients(List<ClienteModel> clientModels) {
        filterAdapter.setClientModels(clientModels);
    }

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_CLIENT_FILTER);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);
    }

    public void trackBackPressed() {
        presenter.trackBackPressed();
    }

    public interface OnFilterCompleteListener {
        void onComplete(ClienteModel clienteModel);
    }
}

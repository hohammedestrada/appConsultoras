package biz.belcorp.consultoras.feature.contest.order.current;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.feature.contest.order.di.PerOrderComponent;
import biz.belcorp.consultoras.feature.home.incentives.GiftActiveDetailCurrentAdapter;
import biz.belcorp.consultoras.util.GlobalConstant;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PerCurrentOrderFragment extends BaseFragment implements PerCurrentOrderView {

    @Inject
    PerCurrentOrderPresenter presenter;

    @BindView(R.id.rvw_contest)
    RecyclerView rvwContest;

    private Unbinder bind;

    private String codigoConcurso = "";

    public PerCurrentOrderFragment() {
        super();
    }

    @Override
    protected boolean onInjectView() {
        getComponent(PerOrderComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);

        Bundle bundle = getArguments();
        if (savedInstanceState == null && bundle != null) {
            codigoConcurso = bundle.getString(GlobalConstant.CODE_CONCURSO, "");
            init();
        }
    }

    @io.reactivex.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contest_detail,
            container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null) presenter.initScreenTrack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind != null)
            bind.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    /**********************************************************/

    private void init() {
        presenter.get(codigoConcurso);

    }

    public void refresh(){

        // DAVID TU MISMO ERES!

    }

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_GIFT_BY_ORDER, loginModel);
    }

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_GIFT_BY_ORDER, loginModel);
        getActivity().finish();
    }

    @Override
    public void initializeAdapter(String countryISO) {
        // EMPTY
    }

    @Override
    public void showContest(ConcursoModel model, String campaingCurrent, String countryMoneySymbol) {
        String currentCampaniaFinal = "";

        if (!model.getCampaniaIDInicio().equals(model.getCampaniaIDFin())) {
            currentCampaniaFinal = model.getCampaniaIDFin().toString().substring(4);
        }

        GiftActiveDetailCurrentAdapter currentListAdapter =
            new GiftActiveDetailCurrentAdapter(getActivity(), model.getNiveles(),
                model.getPuntosAcumulados(), model.getNivelAlcanzado(),
                model.isIndicadorPremioAcumulativo(), currentCampaniaFinal);
        rvwContest.setAdapter(currentListAdapter);
        rvwContest.setLayoutManager(new LinearLayoutManager(getActivity(),
            LinearLayoutManager.VERTICAL, false));
        rvwContest.setHasFixedSize(true);
        rvwContest.setNestedScrollingEnabled(false);
    }

    @Override
    public void onError(ErrorModel errorModel) {
        processGeneralError(errorModel);
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
}

package biz.belcorp.consultoras.feature.home.tracking;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.tracking.TrackingDetailModel;
import biz.belcorp.consultoras.common.model.tracking.TrackingModel;
import biz.belcorp.consultoras.util.GlobalConstant;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackingDetailFragment extends BaseFragment {

    @BindView(R.id.tvw_campania)
    TextView tvwCampania;

    @BindView(R.id.tvw_estado)
    TextView tvwEstado;

    @BindView(R.id.tvw_pedido)
    TextView tvwPedido;

    @BindView(R.id.rvw_tracking)
    RecyclerView rvwTracking;

    TrackingDetailListAdapter adapter;

    /**********************************************************/

    public TrackingDetailFragment() {
        super();
    }

    public static TrackingDetailFragment getInstance() {
        return new TrackingDetailFragment();
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
        savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_tracking, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            TrackingModel model = bundle.getParcelable(GlobalConstant.TRACKING_KEY);
            showTracking(model);
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

    public void showTracking(TrackingModel trackingModel) {
        if (trackingModel != null) {
            tvwCampania.setText(String.format(getString(R.string.tracking_campania), trackingModel.getCampania().toString().substring(4)));
            tvwEstado.setText(trackingModel.getEstado());
            tvwPedido.setText(trackingModel.getNumeroPedido());

            if (trackingModel.getDetalles() != null && !trackingModel.getDetalles().isEmpty()) {

                int etapaAlcanzada = 0;

                for (TrackingDetailModel model : trackingModel.getDetalles()) {
                    if (model.getAlcanzado()) {
                        etapaAlcanzada = model.getEtapa();
                    }
                }

                adapter = new TrackingDetailListAdapter(context(), trackingModel.getDetalles(), etapaAlcanzada);
                rvwTracking.setAdapter(adapter);
                rvwTracking.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rvwTracking.setHasFixedSize(true);
                rvwTracking.setNestedScrollingEnabled(false);
                rvwTracking.setVisibility(View.VISIBLE);
            }
        }
    }
}

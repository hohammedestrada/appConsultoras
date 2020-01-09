package biz.belcorp.consultoras.feature.catalog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.catalog.CatalogByCampaignModel;
import biz.belcorp.consultoras.feature.embedded.offers.OffersActivity;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.PageUrlType;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 */
public class MagazineSAFragment extends BaseFragment {

    private Unbinder bind;
    private CatalogByCampaignModel magazine;
    private CatalogAdapter.CatalogEventListener eventListener;

    public static MagazineSAFragment newInstance() {
        return new MagazineSAFragment();
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_magazine_suscrita_activa, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments == null) return;

        magazine = arguments.getParcelable(GlobalConstant.BOOK_BY_CAMPAIGN_KEY);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (bind != null) bind.unbind();
    }

    /**********************************************************/

    @OnClick(R.id.btn_aceptar)
    public void encuentralasAqui() {

        if (null != eventListener && null != magazine.getMagazineEntity()) {
            eventListener.trackEvent(
                GlobalConstant.EVENT_CAT_MAGAZINE,
                GlobalConstant.EVENT_ACTION_OFERTAS_SA,
                magazine.getMagazineEntity().getCampaniaId(),
                GlobalConstant.EVENT_NAME_PICK_MAGAZINE);
        }

        Intent intent = new Intent(getActivity(), OffersActivity.class);
        intent.putExtra(OffersActivity.OPTION, PageUrlType.OFFERS);
        startActivity(intent);
    }

    @Override
    public Context context() {
        return getContext();
    }

    public void setEventListener(CatalogAdapter.CatalogEventListener eventListener) {
        this.eventListener = eventListener;
    }
}

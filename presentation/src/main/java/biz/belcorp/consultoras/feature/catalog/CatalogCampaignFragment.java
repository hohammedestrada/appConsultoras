package biz.belcorp.consultoras.feature.catalog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.catalog.CatalogByCampaignModel;
import biz.belcorp.consultoras.util.GlobalConstant;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 */
public class CatalogCampaignFragment extends BaseFragment{

    @BindView(R.id.tvw_back)
    protected TextView tvwBack;
    @BindView(R.id.rlt_back)
    protected RelativeLayout lltBack;
    @BindView(R.id.tvw_next)
    protected TextView tvwNext;
    @BindView(R.id.rlt_next)
    protected RelativeLayout lltNext;
    @BindView(R.id.vwp_content)
    protected ViewPager vwpContent;

    private Unbinder bind;
    private CatalogCampaignAdapter adapter;
    private CatalogAdapter.CatalogEventListener listener;
    private int catalogSize = 0;
    private CatalogCampaignEventListener catalogListener;

    /**********************************************************/

    public static CatalogCampaignFragment newInstance(CatalogAdapter.CatalogEventListener listener) {
        CatalogCampaignFragment fragment = new CatalogCampaignFragment();
        fragment.listener = listener;

        return fragment;
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savFedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_campaign, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null) {
            List<CatalogByCampaignModel> catalogs = arguments.getParcelableArrayList(GlobalConstant.BOOK_LIST_KEY);
            catalogSize = catalogs.size();
            initialize(catalogs);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (bind != null) bind.unbind();
    }

    /**********************************************************/

    private void initializeNavigation() {

        if (adapter == null || adapter.getCount() <= 0) return;

        if (vwpContent.getCurrentItem() > 0) {
            String backLabel = adapter.getCatalogs().get(vwpContent.getCurrentItem() - 1).getCampaignName();
            if (null != backLabel)
                tvwBack.setText(String.format("C%s", backLabel.isEmpty() ? "" : backLabel.substring(4)));
        }

        if (vwpContent.getCurrentItem() < (catalogSize-1)) {
            String nextLabel = adapter.getCatalogs().get(vwpContent.getCurrentItem() + 1).getCampaignName();
            if (null != nextLabel)
                tvwNext.setText(String.format("C%s", nextLabel.isEmpty() ? "" : nextLabel.substring(4)));
        }
    }

    /**********************************************************/

    private void initialize(List<CatalogByCampaignModel> catalogs) {
        adapter = new CatalogCampaignAdapter(getActivity(), getChildFragmentManager());
        adapter.setArguments(getArguments());
        adapter.setCatalogAdapterEventListener((descripcion, title) -> {
            if(catalogListener != null) {
                catalogListener.onDownloadPDFRequest(descripcion, title);
            }
        });
        adapter.setCatalogs(catalogs);

        if (listener != null) {
            adapter.setListener(listener);
        }

        vwpContent.setAdapter(adapter);
        vwpContent.setCurrentItem(1);

        initializeNavigation();
    }

    /**********************************************************/

    @OnClick(R.id.rlt_back)
    protected void onBack() {
        if (!isVisible()) return;

        if (vwpContent.getCurrentItem() - 1 == 0) {
            lltBack.setVisibility(View.GONE);
            lltNext.setVisibility(View.VISIBLE);
        } else {
            lltBack.setVisibility(View.VISIBLE);
            lltNext.setVisibility(View.VISIBLE);
        }

        if (vwpContent.getCurrentItem() > 0) {
            vwpContent.setCurrentItem(vwpContent.getCurrentItem() - 1);
        }

        initializeNavigation();
    }

    @OnClick(R.id.rlt_next)
    protected void onNext() {
        if (adapter == null || !isVisible()) return;

        if (vwpContent.getCurrentItem() + 1 == adapter.getCount() - 1) {
            lltNext.setVisibility(View.GONE);
            lltBack.setVisibility(View.VISIBLE);
        } else {
            lltBack.setVisibility(View.VISIBLE);
            lltNext.setVisibility(View.VISIBLE);
        }

        if (vwpContent.getCurrentItem() < adapter.getCount() - 1) {
            vwpContent.setCurrentItem(vwpContent.getCurrentItem() + 1);
        }

        initializeNavigation();
    }

    @Override
    public Context context() {
        return getContext();
    }

    public void setCatalogListener(CatalogCampaignEventListener catalogListener){
        this.catalogListener = catalogListener;
    }

    public interface CatalogCampaignEventListener {
        void onDownloadPDFRequest(String descripcion, String title);
    }
}

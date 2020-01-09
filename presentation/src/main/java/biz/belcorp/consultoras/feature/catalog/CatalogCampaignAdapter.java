package biz.belcorp.consultoras.feature.catalog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.catalog.CatalogByCampaignModel;
import biz.belcorp.consultoras.common.model.catalog.CatalogModel;
import biz.belcorp.consultoras.util.GlobalConstant;

/**
 *
 */
class CatalogCampaignAdapter extends FragmentPagerAdapter implements CatalogNewFragment.CatalogNewListener {

    private List<CatalogByCampaignModel> catalogs = new ArrayList<>();
    private CatalogAdapter.CatalogEventListener listener;
    private CatalogAdapterEventListener catalogListener = null;
    private Bundle arguments;
    private FragmentActivity activity;

    CatalogCampaignAdapter(FragmentActivity activity, FragmentManager fm) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        CatalogByCampaignModel byCampaign = catalogs.get(position);

        String numCampaign = "";
        String campaign = "";

        if(byCampaign.getCampaignName() != null) {
            if(!byCampaign.getCampaignName().isEmpty()){
                if(byCampaign.getCampaignName().trim().length() > 4){
                    numCampaign = byCampaign.getCampaignName().substring(4);
                    campaign = String.format(activity.getResources().getString(R.string.campania_title_catalog), numCampaign);
                }
            }
        }

        Bundle bundle = new Bundle();
        bundle.putString(CatalogContainerActivity.PAIS, arguments.getString(CatalogContainerActivity.PAIS));
        bundle.putString(GlobalConstant.NUM_CAMPAIGN_KEY, numCampaign);
        bundle.putString(GlobalConstant.CAMPAIGN_KEY, campaign);
        bundle.putString(GlobalConstant.CAMPAIGN_FULL_KEY, byCampaign.getCampaignName());
        bundle.putParcelableArrayList(GlobalConstant.BOOK_BY_CAMPAIGN_KEY, (ArrayList<CatalogModel>) byCampaign.getCatalogoEntities());
        bundle.putInt(GlobalConstant.MARCA_ID, arguments.getInt(GlobalConstant.MARCA_ID));
        bundle.putString(GlobalConstant.URL_CATALOGO, arguments.getString(GlobalConstant.URL_CATALOGO));
        bundle.putBoolean(GlobalConstant.REVISTA_USER_ES_TOP_BRILLA, arguments.getBoolean(GlobalConstant.REVISTA_USER_ES_TOP_BRILLA));
        if(arguments.getString(GlobalConstant.URL_CATALOGO) != null){
            if(catalogs.size() == 3) {
                if (position == 1) {
                    return createCatalogNewFragment(bundle);
                } else {
                    return createCatalogFragment(bundle);
                }
            } else {
                return createCatalogNewFragment(bundle);
            }
        } else {
            return createCatalogFragment(bundle);
        }
    }

    @NonNull
    private Fragment createCatalogFragment(Bundle bundle) {
        CatalogFragment catalogFragment = CatalogFragment.Companion.newInstance(listener);
        catalogFragment.setArguments(bundle);
        return catalogFragment;
    }

    @NonNull
    private Fragment createCatalogNewFragment(Bundle bundle) {
        CatalogNewFragment catalogNewFragment = CatalogNewFragment.Companion.newInstance(listener);
        catalogNewFragment.setTopBrillaListener(this);
        catalogNewFragment.setArguments(bundle);
        return catalogNewFragment;
    }

    @Override
    public int getCount() {
        return catalogs.size();
    }

    public void setListener(CatalogAdapter.CatalogEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDownloadPDFRequest(@NotNull String descripcion, @NotNull String title) {
        if(catalogListener != null){
            catalogListener.onDownloadPDFRequest(descripcion, title);
        }
    }

    public void setCatalogAdapterEventListener(CatalogAdapterEventListener listener){
        this.catalogListener = listener;
    }

    List<CatalogByCampaignModel> getCatalogs() {
        return catalogs;
    }

    void setCatalogs(List<CatalogByCampaignModel> catalogs) {
        if (catalogs == null) return;

        this.catalogs.clear();
        this.catalogs.addAll(catalogs);
    }

    public void setArguments(Bundle arguments) {
        this.arguments = arguments;
    }

    public Bundle getArguments() {
        return arguments;
    }

    public interface CatalogAdapterEventListener {
        void onDownloadPDFRequest(String descripcion, String title);
    }
}

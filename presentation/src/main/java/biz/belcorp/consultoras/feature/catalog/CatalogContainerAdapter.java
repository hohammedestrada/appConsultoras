package biz.belcorp.consultoras.feature.catalog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.common.model.catalog.CatalogByCampaignModel;
import biz.belcorp.consultoras.data.manager.SessionManager;
import biz.belcorp.consultoras.domain.entity.LoginDetail;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.BrandCode;
import biz.belcorp.consultoras.util.anotation.RevistaDigitalType;

/**
 *
 */
class CatalogContainerAdapter extends FragmentStatePagerAdapter implements CatalogCampaignFragment.CatalogCampaignEventListener {

    private CatalogCampaignFragment catalogCampaignFragment;
    private MagazineCampaignFragment magazineCampaignFragment;
    private Bundle magazineArguments;
    private Bundle catalogArguments;
    private Bundle paramArguments;

    private final CatalogAdapter.CatalogEventListener listener;
    private CatalogContainerAdapterListener containerListener;

    CatalogContainerAdapter(FragmentManager fm, CatalogAdapter.CatalogEventListener listener, Bundle paramArguments) {
        super(fm);
        this.listener = listener;
        this.paramArguments = paramArguments;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            if (catalogCampaignFragment == null)
                catalogCampaignFragment = CatalogCampaignFragment.newInstance(listener);

            catalogCampaignFragment.setCatalogListener(this);
            catalogArguments.putAll(paramArguments);
            catalogCampaignFragment.setArguments(catalogArguments);
            return catalogCampaignFragment;
        } else {
            if (magazineCampaignFragment == null)
                magazineCampaignFragment = MagazineCampaignFragment.newInstance(listener);
            magazineCampaignFragment.setArguments(magazineArguments);
            return magazineCampaignFragment;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "CATÁLOGOS";
        } else {
            return "REVISTAS";
        }
    }

    @Override
    public int getCount() {

        boolean tieneGND = paramArguments.getBoolean(GlobalConstant.REVISTA_GND);
        int revistaDigitalSuscripcion = paramArguments.getInt(GlobalConstant.REVISTA_CODE);

        if ((RevistaDigitalType.CON_RD_SUSCRITA_ACTIVA == revistaDigitalSuscripcion && !tieneGND)
            || (RevistaDigitalType.CON_RD_SUSCRITA_NO_ACTIVA == revistaDigitalSuscripcion && !tieneGND)
            || (RevistaDigitalType.CON_RD_NO_SUSCRITA_ACTIVA == revistaDigitalSuscripcion && !tieneGND)
            || (RevistaDigitalType.CON_RD_NO_SUSCRITA_ACTIVA == revistaDigitalSuscripcion && tieneGND)) {
            listener.hiddenTab();
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public void onDownloadPDFRequest(String descripcion, String title) {
        this.containerListener.onDownloadPDFRequest(descripcion, title);
    }

    public void setCatalogContainerAdapterListener(CatalogContainerAdapterListener listener){
        this.containerListener = listener;
    }

    void setCatalogs(@NonNull List<CatalogByCampaignModel> catalogs, String paisISO, User user) {
        catalogArguments = new Bundle();
        catalogArguments.putBoolean(GlobalConstant.REVISTA_USER_ES_TOP_BRILLA, user.isBrillante());
        catalogArguments.putString(CatalogContainerActivity.PAIS, paisISO);
        catalogArguments.putParcelableArrayList(GlobalConstant.BOOK_LIST_KEY, (ArrayList<CatalogByCampaignModel>) catalogs);

        switch (BuildConfig.APP_NAME){
            case "EsikaConmigo":
                catalogArguments.putInt(GlobalConstant.MARCA_ID, BrandCode.ESIKA);
                break;
            case "LbelConmigo":
                catalogArguments.putInt(GlobalConstant.MARCA_ID, BrandCode.LBEL);
                break;
        }

        for(LoginDetail loginDetail: user.getDetail()){
            if(loginDetail.getDetailType() == 6){
                catalogArguments.putString(GlobalConstant.URL_CATALOGO, loginDetail.getName());
            }
        }
    }

    void setMagazines(@NonNull List<CatalogByCampaignModel> magazines) {
        magazineArguments = new Bundle();
        magazineArguments.putParcelableArrayList(GlobalConstant.BOOK_LIST_KEY, (ArrayList<CatalogByCampaignModel>) magazines);
        magazineArguments.putBundle(GlobalConstant.REVISTA_SUSCRIPCION_KEY, paramArguments);
    }

    public interface CatalogContainerAdapterListener {
        void onDownloadPDFRequest(String descripcion, String title);
    }
}

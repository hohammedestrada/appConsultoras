package biz.belcorp.consultoras.feature.catalog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.common.model.catalog.CatalogByCampaignModel;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.GuiaNegocioDigitalType;
import biz.belcorp.consultoras.util.anotation.RevistaDigitalType;

/**
 *
 */
class MagazineAdapter extends FragmentPagerAdapter {

    private List<CatalogByCampaignModel> magazines = new ArrayList<>();
    private CatalogAdapter.CatalogEventListener eventListner;
    private Bundle paramArguments;
    private ListenerMagazine listener;

    MagazineAdapter(FragmentManager fm, Bundle paramArguments, ListenerMagazine listener) {
        super(fm);
        this.paramArguments = paramArguments;
        this.listener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        CatalogByCampaignModel byCampaignModel = magazines.get(position);

        String campaign = byCampaignModel.getCampaignName().isEmpty() ? "" : String.format("CAMPAÑA C%s", byCampaignModel.getCampaignName().substring(4));

        Bundle arguments = new Bundle();
        arguments.putString(GlobalConstant.CAMPAIGN_KEY, campaign);
        arguments.putParcelable(GlobalConstant.BOOK_BY_CAMPAIGN_KEY, byCampaignModel);
        arguments.putBundle(GlobalConstant.REVISTA_SUSCRIPCION_KEY, paramArguments);

        boolean tieneGND = paramArguments.getBoolean(GlobalConstant.REVISTA_GND);
        int revistaDigitalSuscripcion = paramArguments.getInt(GlobalConstant.REVISTA_CODE);

        switch (revistaDigitalSuscripcion) {
            case RevistaDigitalType.CON_RD_SUSCRITA_ACTIVA:
                MagazineSAFragment magazineSAFragment = MagazineSAFragment.newInstance();
                magazineSAFragment.setEventListener(eventListner);
                magazineSAFragment.setArguments(arguments);
                return magazineSAFragment;
            case RevistaDigitalType.CON_RD_SUSCRITA_NO_ACTIVA:
                MagazineSNAFragment magazineSNAFragment = MagazineSNAFragment.newInstance();
                magazineSNAFragment.setEventListener(eventListner);
                magazineSNAFragment.setArguments(arguments);
                return magazineSNAFragment;
            case RevistaDigitalType.CON_RD_NO_SUSCRITA_ACTIVA:

                if (tieneGND) {
                    biz.belcorp.consultoras.feature.catalog.gnd.MagazineNSAFragment magazineNSAFragment = biz.belcorp.consultoras.feature.catalog.gnd.MagazineNSAFragment.newInstance();
                    magazineNSAFragment.setEventListener(eventListner);
                    magazineNSAFragment.setArguments(arguments);
                    hiddenArrows(tieneGND);
                    return magazineNSAFragment;
                } else {
                    biz.belcorp.consultoras.feature.catalog.MagazineNSAFragment magazineNSAFragment = biz.belcorp.consultoras.feature.catalog.MagazineNSAFragment.newInstance();
                    magazineNSAFragment.setEventListener(eventListner);
                    magazineNSAFragment.setArguments(arguments);
                    hiddenArrows(tieneGND);
                    return magazineNSAFragment;
                }

            case RevistaDigitalType.CON_RD_NO_SUSCRITA_NO_ACTIVA:

                if (tieneGND) {
                    biz.belcorp.consultoras.feature.catalog.gnd.MagazineNSNAFragment magazineNSNAFragment = biz.belcorp.consultoras.feature.catalog.gnd.MagazineNSNAFragment.newInstance();
                    magazineNSNAFragment.setEventListener(eventListner);
                    magazineNSNAFragment.setArguments(arguments);
                    hiddenArrows(tieneGND);
                    return magazineNSNAFragment;
                } else {
                    biz.belcorp.consultoras.feature.catalog.MagazineNSNAFragment magazineNSNAFragment = biz.belcorp.consultoras.feature.catalog.MagazineNSNAFragment.newInstance();
                    magazineNSNAFragment.setEventListener(eventListner);
                    magazineNSNAFragment.setArguments(arguments);
                    hiddenArrows(tieneGND);
                    return magazineNSNAFragment;
                }

            default:

                if (tieneGND) {
                    biz.belcorp.consultoras.feature.catalog.gnd.MagazineFragment fragment = biz.belcorp.consultoras.feature.catalog.gnd.MagazineFragment.newInstance();
                    fragment.setEventListener(eventListner);
                    fragment.setArguments(arguments);
                    hiddenArrows(tieneGND);
                    return fragment;
                } else {
                    biz.belcorp.consultoras.feature.catalog.MagazineFragment fragment = biz.belcorp.consultoras.feature.catalog.MagazineFragment.newInstance();
                    fragment.setEventListener(eventListner);
                    fragment.setArguments(arguments);
                    hiddenArrows(tieneGND);
                    return fragment;
                }
        }
    }

    @Override
    public int getCount() {
        return magazines.size();
    }

    List<CatalogByCampaignModel> getMagazines() {
        return magazines;
    }

    void setMagazines(List<CatalogByCampaignModel> magazines) {
        if (magazines == null) return;

        this.magazines.clear();
        this.magazines.addAll(magazines);
    }

    void setEventListener(CatalogAdapter.CatalogEventListener eventListner) {
        this.eventListner = eventListner;
    }

    void hiddenArrows(boolean tieneGND) {
        if (GuiaNegocioDigitalType.CON_GND == (tieneGND ? 1 : 0)) {
            listener.hiddenArrows();
        }
    }

    public interface ListenerMagazine {
        void hiddenArrows();
    }
}

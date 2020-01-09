package biz.belcorp.consultoras.common.model.catalog;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 *
 */
public class CatalogByCampaignModel implements Parcelable {

    private String campaignName;
    private List<CatalogModel> catalogoEntities;
    private CatalogModel magazineEntity;

    public CatalogByCampaignModel() {
        // EMPTy
    }

    protected CatalogByCampaignModel(Parcel in) {
        campaignName = in.readString();
    }

    public static final Creator<CatalogByCampaignModel> CREATOR = new Creator<CatalogByCampaignModel>() {
        @Override
        public CatalogByCampaignModel createFromParcel(Parcel in) {
            return new CatalogByCampaignModel(in);
        }

        @Override
        public CatalogByCampaignModel[] newArray(int size) {
            return new CatalogByCampaignModel[size];
        }
    };

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public List<CatalogModel> getCatalogoEntities() {
        return catalogoEntities;
    }

    public void setCatalogoEntities(List<CatalogModel> catalogoEntities) {
        this.catalogoEntities = catalogoEntities;
    }

    public CatalogModel getMagazineEntity() {
        return magazineEntity;
    }

    public void setMagazineEntity(CatalogModel magazineEntity) {
        this.magazineEntity = magazineEntity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(campaignName);
    }
}

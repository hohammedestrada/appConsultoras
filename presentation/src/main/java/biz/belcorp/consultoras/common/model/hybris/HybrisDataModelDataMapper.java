package biz.belcorp.consultoras.common.model.hybris;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.HybrisData;

@PerActivity
public class HybrisDataModelDataMapper {

    @Inject
    HybrisDataModelDataMapper() {
        // EMPTY
    }

    public HybrisDataModel transform(HybrisData input){
        HybrisDataModel output = null;

        if(null != input){
            output = new HybrisDataModel();
            output.setId(input.getId());
            output.setTrackingURL(input.getTrackingURL());
        }
        return output;
    }

    public HybrisData transform(HybrisDataModel input){
        HybrisData output = null;

        if(null != input){
            output = new HybrisData();
            output.setId(input.getId());
            output.setTrackingURL(input.getTrackingURL());
        }
        return output;
    }
}

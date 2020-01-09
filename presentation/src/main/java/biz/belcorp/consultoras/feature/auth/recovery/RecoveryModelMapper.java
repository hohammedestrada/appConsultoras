package biz.belcorp.consultoras.feature.auth.recovery;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.RecoveryRequest;

@PerActivity
public class RecoveryModelMapper {

    @Inject
    public RecoveryModelMapper() {
    }

    public RecoveryModel transform(RecoveryRequest obj) {
        RecoveryModel model = null;

        if (null != obj) {
            model = new RecoveryModel();
            model.setCountryID(obj.getCountryID());
            model.setUsername(obj.getUsername());
        }
        return model;
    }

    public RecoveryRequest transform(RecoveryModel model) {
        RecoveryRequest obj = null;

        if (null != model) {
            obj = new RecoveryRequest();
            obj.setCountryID(model.getCountryID());
            obj.setUsername(model.getUsername());
        }
        return obj;
    }


}

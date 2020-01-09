package biz.belcorp.consultoras.common.model.incentivos;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.IncentivesRequest;

@PerActivity
public class IncentivesRequestModelDataMapper {

    @Inject
    IncentivesRequestModelDataMapper(){ }

    public IncentivesRequest transform(IncentivesRequestModel input){
        IncentivesRequest output = null;

        if (null != input) {
            output = new IncentivesRequest();
            output.setCountryISO(input.getCountryISO());
            output.setCampaingCode(input.getCampaingCode());
            output.setConsultantCode(input.getConsultantCode());
            output.setRegionCode(input.getRegionCode());
            output.setZoneCode(input.getZoneCode());
            output.setTipoConcurso(input.getTipoConcurso());
        }
        return output;
    }

}

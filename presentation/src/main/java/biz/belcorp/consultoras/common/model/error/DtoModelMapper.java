package biz.belcorp.consultoras.common.model.error;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.BasicDto;

@PerActivity
public class DtoModelMapper {

    @Inject
    DtoModelMapper() {
        // EMPTY
    }

    public BooleanDtoModel transform(BasicDto<Boolean> input) {
        return new BooleanDtoModel(input.getCode(), input.getMessage(), input.getData());
    }

}

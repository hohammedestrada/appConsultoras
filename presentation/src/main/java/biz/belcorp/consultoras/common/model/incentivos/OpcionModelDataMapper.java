package biz.belcorp.consultoras.common.model.incentivos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Opcion;

@PerActivity
public class OpcionModelDataMapper {

    private PremioModelDataMapper premioModelDataMapper;

    @Inject
    OpcionModelDataMapper(PremioModelDataMapper premioModelDataMapper){
        this.premioModelDataMapper = premioModelDataMapper;
    }

    public OpcionModel transform(Opcion input){
        OpcionModel output = null;

        if(null != input){
            output = new OpcionModel();
            output.setOpcion(input.getOpcion());
            output.setPremios(premioModelDataMapper.transform(input.getPremios()));
        }
        return output;
    }

    public List<OpcionModel> transform(Collection<Opcion> input) {
        List<OpcionModel> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (Opcion entity : input) {
            final OpcionModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }
}

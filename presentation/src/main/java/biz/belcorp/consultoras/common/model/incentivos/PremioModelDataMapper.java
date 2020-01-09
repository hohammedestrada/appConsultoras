package biz.belcorp.consultoras.common.model.incentivos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Opcion;
import biz.belcorp.consultoras.domain.entity.Premio;

@PerActivity
public class PremioModelDataMapper {

    @Inject
    PremioModelDataMapper(){ }

    public PremioModel transform(Premio input){
        PremioModel output = null;

        if(null != input){
            output = new PremioModel();
            output.setId(input.getId());
            output.setNivelLocalId(input.getNivelLocalId());
            output.setCodigoPremio(input.getCodigoPremio());
            output.setDescripcionPremio(input.getDescripcionPremio());
            output.setNumeroPremio(input.getNumeroPremio());
            output.setImagenPremio(input.getImagenPremio());
        }
        return output;
    }

    public List<PremioModel> transform(Collection<Premio> input) {
        List<PremioModel> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (Premio entity : input) {
            final PremioModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }
}

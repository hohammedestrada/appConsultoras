package biz.belcorp.consultoras.common.model.incentivos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.PremioNueva;

@PerActivity
public class PremioNuevaModelDataMapper {


    @Inject
    PremioNuevaModelDataMapper() {
        // EMPTY
    }

    public PremioNuevaModel transform(PremioNueva input){
        PremioNuevaModel output = null;

        if(null != input){
            output = new PremioNuevaModel();
            output.setId(input.getId());
            output.setNivelProgramaLocalId(input.getNivelProgramaLocalId());
            output.setCodigoConcurso(input.getCodigoConcurso());
            output.setCodigoNivel(input.getCodigoNivel());
            output.setCUV(input.getCUV());
            output.setDescripcionProducto(input.getDescripcionProducto());
            output.setIndicadorKitNuevas(input.getIndicadorKitNuevas());
            output.setPrecioUnitario(input.getPrecioUnitario());
            output.setUrlImagenPremio(input.getUrlImagenPremio());
        }
        return output;
    }

    public List<PremioNuevaModel> transform(Collection<PremioNueva> input) {
        List<PremioNuevaModel> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (PremioNueva entity : input) {
            final PremioNuevaModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }
}

package biz.belcorp.consultoras.common.model.incentivos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Cupon;

@PerActivity
public class CuponModelDataMapper {

    @Inject
    CuponModelDataMapper() {
        // EMPTY
    }

    public CuponModel transform(Cupon input){
        CuponModel output = null;

        if(null != input){
            output = new CuponModel();
            output.setId(input.getId());
            output.setNivelProgramaLocalId(input.getNivelProgramaLocalId());
            output.setCodigoConcurso(input.getCodigoConcurso());
            output.setCodigoCupon(input.getCodigoCupon());
            output.setCodigoNivel(input.getCodigoNivel());
            output.setCodigoVenta(input.getCodigoVenta());
            output.setDescripcionProducto(input.getDescripcionProducto());
            output.setUnidadesMaximas(input.getUnidadesMaximas());
            output.setIndicadorCuponIndependiente(input.getIndicadorCuponIndependiente());
            output.setIndicadorKit(input.getIndicadorKit());
            output.setNumeroCampanasVigentes(input.getNumeroCampanasVigentes());
            output.setTextoLibre(input.getTextoLibre());
            output.setPrecioUnitario(input.getPrecioUnitario());
            output.setGanancia(input.getGanancia());
            output.setUrlImagenCupon(input.getUrlImagenCupon());
        }
        return output;
    }

    public List<CuponModel> transform(Collection<Cupon> input) {
        List<CuponModel> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (Cupon entity : input) {
            final CuponModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }

}

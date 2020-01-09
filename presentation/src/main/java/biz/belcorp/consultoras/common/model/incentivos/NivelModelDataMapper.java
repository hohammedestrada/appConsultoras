package biz.belcorp.consultoras.common.model.incentivos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Nivel;
import biz.belcorp.consultoras.domain.entity.Premio;

@PerActivity
public class NivelModelDataMapper {

    private OpcionModelDataMapper opcionModelDataMapper;

    @Inject
    NivelModelDataMapper(OpcionModelDataMapper opcionModelDataMapper){
        this.opcionModelDataMapper = opcionModelDataMapper;
    }

    public NivelModel transform(Nivel input){
        NivelModel output = null;

        if(null != input){
            output = new NivelModel();
            output.setId(input.getId());
            output.setConcursoLocalId(input.getConcursoLocalId());
            output.setCodigoConcurso(input.getCodigoConcurso());
            output.setCodigoNivel(input.getCodigoNivel());
            output.setPuntosNivel(input.getPuntosNivel());
            output.setPuntosFaltantes(input.getPuntosFaltantes());
            output.setPuntosExigidos(input.getPuntosExigidos());
            output.setPuntosExigidosFaltantes(input.getPuntosExigidosFaltantes());
            output.setIndicadorPremiacionPedido(input.isIndicadorPremiacionPedido());
            output.setIndicadorNivelElectivo(input.isIndicadorNivelElectivo());
            output.setIndicadorBelCenter(input.isIndicadorBelCenter());
            output.setMontoPremiacionPedido(input.getMontoPremiacionPedido());
            output.setFechaVentaRetail(input.getFechaVentaRetail());
            output.setOpciones(opcionModelDataMapper.transform(input.getOpciones()));
        }
        return output;
    }

    public List<NivelModel> transform(Collection<Nivel> input) {
        List<NivelModel> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (Nivel entity : input) {
            final NivelModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }
}

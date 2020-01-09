package biz.belcorp.consultoras.common.model.incentivos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.NivelProgramaNueva;

@PerActivity
class NivelProgramaNuevaModelDataMapper {

    private PremioNuevaModelDataMapper premioNuevaModelDataMapper;
    private CuponModelDataMapper cuponModelDataMapper;

    @Inject
    NivelProgramaNuevaModelDataMapper(PremioNuevaModelDataMapper premioNuevaModelDataMapper,
                                       CuponModelDataMapper cuponModelDataMapper) {
        this.premioNuevaModelDataMapper = premioNuevaModelDataMapper;
        this.cuponModelDataMapper = cuponModelDataMapper;
    }

    public NivelProgramaNuevaModel transform(NivelProgramaNueva input) {
        NivelProgramaNuevaModel output = null;

        if (null != input) {
            output = new NivelProgramaNuevaModel();
            output.setId(input.getId());
            output.setConcursoLocalId(input.getConcursoLocalId());
            output.setCodigoConcurso(input.getCodigoConcurso());
            output.setCodigoNivel(input.getCodigoNivel());
            output.setMontoExigidoPremio(input.getMontoExigidoPremio());
            output.setMontoExigidoCupon(input.getMontoExigidoCupon());
            output.setPremiosNuevas(premioNuevaModelDataMapper.transform(input.getPremiosNuevas()));
            output.setCupones(cuponModelDataMapper.transform(input.getCupones()));
        }
        return output;
    }

    public List<NivelProgramaNuevaModel> transform(Collection<NivelProgramaNueva> input) {
        List<NivelProgramaNuevaModel> output = new ArrayList<>();

        if (null == input) { return output; }

        for (NivelProgramaNueva entity : input) {
            final NivelProgramaNuevaModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }

}

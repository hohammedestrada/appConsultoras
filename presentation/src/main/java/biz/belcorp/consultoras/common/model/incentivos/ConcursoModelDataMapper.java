package biz.belcorp.consultoras.common.model.incentivos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Concurso;

@PerActivity
public class ConcursoModelDataMapper {

    private NivelModelDataMapper nivelModelDataMapper;
    private NivelProgramaNuevaModelDataMapper nivelProgramaNuevaModelDataMapper;

    @Inject
    ConcursoModelDataMapper(NivelModelDataMapper nivelModelDataMapper,
                            NivelProgramaNuevaModelDataMapper nivelProgramaNuevaModelDataMapper) {
        this.nivelModelDataMapper = nivelModelDataMapper;
        this.nivelProgramaNuevaModelDataMapper = nivelProgramaNuevaModelDataMapper;
    }

    public ConcursoModel transform(Concurso input) {
        ConcursoModel output = null;

        if (null != input) {
            output = new ConcursoModel();
            output.setId(input.getId());
            output.setCampaniaId(input.getCampaniaId());
            output.setCampaniaIDFin(input.getCampaniaIDFin());
            output.setCampaniaIDInicio(input.getCampaniaIDInicio());
            output.setCodigoConcurso(input.getCodigoConcurso());
            output.setTipoConcurso(input.getTipoConcurso());
            output.setPuntosAcumulados(input.getPuntosAcumulados());
            output.setIndicadorPremioAcumulativo(input.isIndicadorPremioAcumulativo());
            output.setNivelAlcanzado(input.getNivelAlcanzado());
            output.setNivelSiguiente(input.getNivelSiguiente());
            output.setCampaniaIDPremiacion(input.getCampaniaIDPremiacion());
            output.setPuntajeExigido(input.getPuntajeExigido());
            output.setDescripcionConcurso(input.getDescripcionConcurso());
            output.setEstadoConcurso(input.getEstadoConcurso());
            output.setUrlBannerCuponesProgramaNuevas(input.getUrlBannerCuponesProgramaNuevas());
            output.setUrlBannerPremiosProgramaNuevas(input.getUrlBannerPremiosProgramaNuevas());
            output.setNiveles(nivelModelDataMapper.transform(input.getNiveles()));
            output.setNivelProgramaNuevas(nivelProgramaNuevaModelDataMapper.transform(input.getNivelProgramaNuevas()));
            output.setCodigoNivelProgramaNuevas(input.getCodigoNivelProgramaNuevas());
            output.setImportePedido(input.getImportePedido());
            output.setTextoCupon(input.getTextoCupon());
            output.setTextoCuponIndependiente(input.getTextoCuponIndependiente());
        }
        return output;
    }

    public List<ConcursoModel> transform(Collection<Concurso> input) {
        List<ConcursoModel> output = new ArrayList<>();

        if (null == input) {
            return output;
        }

        for (Concurso entity : input) {
            final ConcursoModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }
}

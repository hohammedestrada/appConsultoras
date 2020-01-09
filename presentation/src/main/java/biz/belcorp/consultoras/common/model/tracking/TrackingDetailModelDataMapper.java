package biz.belcorp.consultoras.common.model.tracking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.TrackingDetail;

@PerActivity
public class TrackingDetailModelDataMapper {

    @Inject
    TrackingDetailModelDataMapper() {
    }

    public TrackingDetailModel transform(TrackingDetail input) {
        TrackingDetailModel output = null;

        if (null != input) {
            output = new TrackingDetailModel();
            output.setEtapa(input.getEtapa());
            output.setSituacion(input.getSituacion());
            output.setFecha(input.getFecha());
            output.setFechaFormateada(input.getFechaFormateada());
            output.setAlcanzado(input.getAlcanzado());
            output.setObservacion(input.getObservacion());
        }
        return output;
    }

    public TrackingDetail transform(TrackingDetailModel input) {
        TrackingDetail output = null;

        if (null != input) {
            output = new TrackingDetail();
            output.setEtapa(input.getEtapa());
            output.setSituacion(input.getSituacion());
            output.setFecha(input.getFecha());
            output.setFechaFormateada(input.getFechaFormateada());
            output.setAlcanzado(input.getAlcanzado());
            output.setObservacion(input.getObservacion());
        }
        return output;
    }

    public List<TrackingDetailModel> transform(Collection<TrackingDetail> input) {
        List<TrackingDetailModel> output = new ArrayList<>();

        if (null == input) {
            return output;
        }

        for (TrackingDetail entity : input) {
            final TrackingDetailModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }

    public List<TrackingDetail> transform(List<TrackingDetailModel> input) {
        List<TrackingDetail> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (TrackingDetailModel entity : input) {
            final TrackingDetail model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }
}

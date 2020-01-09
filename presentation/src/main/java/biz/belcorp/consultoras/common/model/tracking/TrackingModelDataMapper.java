package biz.belcorp.consultoras.common.model.tracking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Tracking;

@PerActivity
public class TrackingModelDataMapper {

    private TrackingDetailModelDataMapper trackingDetailModelDataMapper;

    @Inject
    TrackingModelDataMapper(TrackingDetailModelDataMapper trackingDetailModelDataMapper) {
        this.trackingDetailModelDataMapper = trackingDetailModelDataMapper;
    }

    public TrackingModel transform(Tracking input) {
        TrackingModel output = null;

        if (null != input) {
            output = new TrackingModel();
            output.setNumeroPedido(input.getNumeroPedido());
            output.setCampania(input.getCampania());
            output.setEstado(input.getEstado());
            output.setFecha(input.getFecha());
            output.setDetalles(trackingDetailModelDataMapper.transform(input.getDetalles()));
        }
        return output;
    }

    public Tracking transform(TrackingModel input) {
        Tracking output = null;

        if (null != input) {
            output = new Tracking();
            output.setNumeroPedido(input.getNumeroPedido());
            output.setCampania(input.getCampania());
            output.setEstado(input.getEstado());
            output.setFecha(input.getFecha());
            output.setDetalles(trackingDetailModelDataMapper.transform(input.getDetalles()));
        }
        return output;
    }

    public List<TrackingModel> transform(Collection<Tracking> input) {
        List<TrackingModel> output = new ArrayList<>();

        if (null == input) {
            return output;
        }

        for (Tracking entity : input) {
            final TrackingModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }

    public Collection<Tracking> transform(List<TrackingModel> input) {
        List<Tracking> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (TrackingModel entity : input) {
            final Tracking model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }
}

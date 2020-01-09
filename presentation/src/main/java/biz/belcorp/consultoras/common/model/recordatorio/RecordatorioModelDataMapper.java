package biz.belcorp.consultoras.common.model.recordatorio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Recordatorio;

/**
 *
 */

@PerActivity
public class RecordatorioModelDataMapper {

    @Inject
    RecordatorioModelDataMapper() {
    }

    public RecordatorioModel transform(Recordatorio entity) {
        RecordatorioModel model = null;

        if (null != entity) {
            model = new RecordatorioModel();
            model.setId(entity.getId());
            model.setRecordatorioID(entity.getRecordatorioID());
            model.setClienteID(entity.getClienteID());
            model.setClienteLocalID(entity.getClienteLocalID());
            model.setFecha(entity.getFecha());
            model.setDescripcion(entity.getDescripcion());
            model.setSincronizado(entity.getSincronizado());
            model.setEstado(entity.getEstado());
        }
        return model;
    }

    public Recordatorio transform(RecordatorioModel model) {
        Recordatorio entity = null;

        if (null != model) {
            entity = new Recordatorio();
            entity.setId(model.getId());
            entity.setRecordatorioID(model.getRecordatorioID());
            entity.setClienteID(model.getClienteID());
            entity.setClienteLocalID(model.getClienteLocalID());
            entity.setFecha(model.getFecha());
            entity.setDescripcion(model.getDescripcion());
            entity.setSincronizado(model.getSincronizado());
            entity.setEstado(model.getEstado());
        }
        return entity;
    }

    public List<RecordatorioModel> transformToDataModel(Collection<Recordatorio> list) {
        List<RecordatorioModel> collection = new ArrayList<>();

        if (null == list) {
            return Collections.emptyList();
        }

        for (Recordatorio entity : list) {
            final RecordatorioModel model = transform(entity);
            if (null != model) {
                collection.add(model);
            }
        }

        return collection;
    }

    public List<Recordatorio> transformToDataDomain(Collection<RecordatorioModel> list) {
        List<Recordatorio> collection = new ArrayList<>();

        if (null == list) {
            return Collections.emptyList();
        }

        for (RecordatorioModel model : list) {
            final Recordatorio obj = transform(model);
            if (null != model) {
                collection.add(obj);
            }
        }

        return collection;
    }

    public List<RecordatorioModel> transform(Collection<Recordatorio> input) {
        List<RecordatorioModel> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (Recordatorio entity : input) {
            final RecordatorioModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }

    public Collection<Recordatorio> transform(List<RecordatorioModel> input) {
        List<Recordatorio> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (RecordatorioModel entity : input) {
            final Recordatorio model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }
}

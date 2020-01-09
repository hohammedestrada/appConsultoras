package biz.belcorp.consultoras.common.model.notification;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper;
import biz.belcorp.consultoras.common.model.recordatorio.RecordatorioModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.NotificacionRecordatorio;

@PerActivity
public class NotificacionRecordatorioModelDataMapper {

    private RecordatorioModelDataMapper recordatorioModelDataMapper;
    private ClienteModelDataMapper clienteModelDataMapper;

    @Inject
    NotificacionRecordatorioModelDataMapper(RecordatorioModelDataMapper recordatorioModelDataMapper,
                                             ClienteModelDataMapper ClienteModelDataMapper) {
        this.recordatorioModelDataMapper = recordatorioModelDataMapper;
        clienteModelDataMapper = ClienteModelDataMapper;
    }

    public NotificacionRecordatorioModel transform(NotificacionRecordatorio input) {
        NotificacionRecordatorioModel output = null;

        if (null != input) {
            output = new NotificacionRecordatorioModel();
            output.setId(input.getId());
            output.setEstado(input.getEstado());
            output.setRecordatorioLocalID(input.getRecordatorioLocalID());
            output.setRecordatorioModel(recordatorioModelDataMapper.transform(input.getRecordatorio()));
            output.setClienteModel(clienteModelDataMapper.transform(input.getCliente()));
        }
        return output;
    }

    public NotificacionRecordatorio transform(NotificacionRecordatorioModel input) {
        NotificacionRecordatorio output = null;

        if (null != input) {
            output = new NotificacionRecordatorio();
            output.setId(input.getId());
            output.setEstado(input.getEstado());
            output.setRecordatorioLocalID(input.getRecordatorioLocalID());
            output.setRecordatorio(recordatorioModelDataMapper.transform(input.getRecordatorioModel()));
            output.setCliente(clienteModelDataMapper.transform(input.getClienteModel()));
        }
        return output;
    }

    public List<NotificacionRecordatorioModel> transform(List<NotificacionRecordatorio> list) {
        List<NotificacionRecordatorioModel> notificacionRecordatorioEntities = new ArrayList<>();
        for (NotificacionRecordatorio model : list) {
            notificacionRecordatorioEntities.add(transform(model));
        }
        return notificacionRecordatorioEntities;
    }

}

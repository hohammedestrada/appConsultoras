package biz.belcorp.consultoras.common.model.notification;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.NotificacionCliente;

@PerActivity
public class NotificacionClienteModelDataMapper {

    private ClienteModelDataMapper clienteModelDataMapper;

    @Inject
    NotificacionClienteModelDataMapper(ClienteModelDataMapper clienteModelDataMapper) {
        this.clienteModelDataMapper = clienteModelDataMapper;
    }

    public NotificacionClienteModel transform(NotificacionCliente input) {
        NotificacionClienteModel output = null;

        if (null != input) {
            output = new NotificacionClienteModel();
            output.setId(input.getId());
            output.setEstado(input.getEstado());
            output.setYear(input.getYear());
            output.setClienteLocalID(input.getClienteLocalID());
            output.setClienteModel(clienteModelDataMapper.transform(input.getCliente()));
        }
        return output;
    }

    public NotificacionCliente transform(NotificacionClienteModel input) {
        NotificacionCliente output = null;

        if (null != input) {
            output = new NotificacionCliente();
            output.setId(input.getId());
            output.setEstado(input.getEstado());
            output.setYear(input.getYear());
            output.setClienteLocalID(input.getClienteLocalID());
            output.setCliente(clienteModelDataMapper.transform(input.getClienteModel()));
        }
        return output;
    }

    public List<NotificacionClienteModel> transform(List<NotificacionCliente> list) {
        List<NotificacionClienteModel> notificacionClienteModels = new ArrayList<>();
        for (NotificacionCliente model : list) {
            notificacionClienteModels.add(transform(model));
        }
        return notificacionClienteModels;
    }

}


package biz.belcorp.consultoras.common.model.notification;

import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.recordatorio.RecordatorioModel;

public class NotificacionRecordatorioModel {

    private Integer id;

    private Integer recordatorioLocalID;

    private Integer estado;

    private RecordatorioModel recordatorioModel;

    private ClienteModel clienteModel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecordatorioLocalID() {
        return recordatorioLocalID;
    }

    public void setRecordatorioLocalID(Integer recordatorioLocalID) {
        this.recordatorioLocalID = recordatorioLocalID;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public RecordatorioModel getRecordatorioModel() {
        return recordatorioModel;
    }

    public void setRecordatorioModel(RecordatorioModel recordatorioModel) {
        this.recordatorioModel = recordatorioModel;
    }

    public ClienteModel getClienteModel() {
        return clienteModel;
    }

    public void setClienteModel(ClienteModel clienteModel) {
        this.clienteModel = clienteModel;
    }
}

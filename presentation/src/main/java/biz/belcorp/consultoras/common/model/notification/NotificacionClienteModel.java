
package biz.belcorp.consultoras.common.model.notification;

import biz.belcorp.consultoras.common.model.client.ClienteModel;

public class NotificacionClienteModel {

    private Integer id;

    private Integer clienteLocalID;

    private Integer year;

    private Integer estado;

    private ClienteModel clienteModel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClienteLocalID() {
        return clienteLocalID;
    }

    public void setClienteLocalID(Integer clienteLocalID) {
        this.clienteLocalID = clienteLocalID;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public ClienteModel getClienteModel() {
        return clienteModel;
    }

    public void setClienteModel(ClienteModel clienteModel) {
        this.clienteModel = clienteModel;
    }
}

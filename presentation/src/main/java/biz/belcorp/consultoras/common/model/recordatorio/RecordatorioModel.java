package biz.belcorp.consultoras.common.model.recordatorio;


import java.io.Serializable;

public class RecordatorioModel implements Serializable {

    private Integer id;
    private Integer recordatorioID;
    private Integer clienteID;
    private Integer clienteLocalID;
    private String fecha;
    private String descripcion;
    private Integer sincronizado;
    private Integer estado;

    public RecordatorioModel() {
        // EMPTY
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClienteID() {
        return clienteID;
    }

    public Integer getRecordatorioID() {
        return recordatorioID;
    }

    public void setRecordatorioID(Integer recordatorioID) {
        this.recordatorioID = recordatorioID;
    }

    public Integer getClienteLocalID() {
        return clienteLocalID;
    }

    public void setClienteLocalID(Integer clienteLocalID) {
        this.clienteLocalID = clienteLocalID;
    }

    public void setClienteID(Integer clienteID) {
        this.clienteID = clienteID;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}

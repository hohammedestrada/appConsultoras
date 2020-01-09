
package biz.belcorp.consultoras.common.model.client;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AnotacionModel implements Serializable{

    private Integer id;
    private Integer anotacionID;
    private String descripcion;
    private Integer estado;
    private Integer clienteID;
    private Integer clienteLocalID;
    private Integer sincronizado;
    private String fecha;
    private Map<String, Object> additionalProperties = new HashMap<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAnotacionID() {
        return anotacionID;
    }

    public void setAnotacionID(Integer anotacionID) {
        this.anotacionID = anotacionID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Integer getClienteID() {
        return clienteID;
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

    public Integer getClienteLocalID() {
        return clienteLocalID;
    }

    public void setClienteLocalID(Integer clienteLocalID) {
        this.clienteLocalID = clienteLocalID;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }
}

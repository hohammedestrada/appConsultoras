
package biz.belcorp.consultoras.common.model.client;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ContactoModel implements Serializable {

    private Integer id;
    private Integer contactoClienteID;
    private Integer clienteID;
    private Integer tipoContactoID;
    private String valor;
    private Integer estado;
    private Map<String, Object> additionalProperties = new HashMap<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContactoClienteID() {
        return contactoClienteID;
    }

    public void setContactoClienteID(Integer contactoClienteID) {
        this.contactoClienteID = contactoClienteID;
    }

    public Integer getClienteID() {
        return clienteID;
    }

    public void setClienteID(Integer clienteID) {
        this.clienteID = clienteID;
    }

    public Integer getTipoContactoID() {
        return tipoContactoID;
    }

    public void setTipoContactoID(Integer tipoContactoID) {
        this.tipoContactoID = tipoContactoID;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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

}

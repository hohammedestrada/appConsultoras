package biz.belcorp.consultoras.common.model.client;

import java.math.BigDecimal;
import java.util.List;

import biz.belcorp.consultoras.common.model.product.ProductModel;

public class ClientMovementModel {

    private Integer id;
    private Integer movementID;
    private Integer clientID;
    private Integer clienteLocalID;
    private Integer sincronizado;
    private Integer clientCode;
    private BigDecimal amount;
    private String type;
    private String description;
    private String campaing;
    private String note;
    private String date;
    private BigDecimal saldo;
    private Integer estado;
    private String code;
    private String message;

    private List<ProductModel> productModels;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clienteID) {
        this.clientID = clienteID;
    }

    public Integer getMovementID() {
        return movementID;
    }

    public void setMovementID(Integer movementID) {
        this.movementID = movementID;
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

    public Integer getClientCode() {
        return clientCode;
    }

    public void setClientCode(Integer clientCode) {
        this.clientCode = clientCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCampaing() {
        return campaing;
    }

    public void setCampaing(String campaing) {
        this.campaing = campaing;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public List<ProductModel> getProductModels() {
        return productModels;
    }

    public void setProductModels(List<ProductModel> productModels) {
        this.productModels = productModels;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

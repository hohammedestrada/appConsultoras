package biz.belcorp.consultoras.common.model.debt;

import java.math.BigDecimal;

/**
 *
 */
public class DebtModel {

    private BigDecimal monto;
    private Integer clienteID;
    private Integer clienteLocalID;
    private String codigoCampania;
    private String tipoMovimiento;
    private String note;
    private String fecha;
    private String desc;
    private Integer estado;

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Integer getClienteID() {
        return clienteID;
    }

    public void setClienteID(Integer clienteID) {
        this.clienteID = clienteID;
    }

    public String getCodigoCampania() {
        return codigoCampania;
    }

    public void setCodigoCampania(String codigoCampania) {
        this.codigoCampania = codigoCampania;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getClienteLocalID() {
        return clienteLocalID;
    }

    public void setClienteLocalID(Integer clienteLocalID) {
        this.clienteLocalID = clienteLocalID;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}

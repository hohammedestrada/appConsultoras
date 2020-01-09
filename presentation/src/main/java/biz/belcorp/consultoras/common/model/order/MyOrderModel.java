
package biz.belcorp.consultoras.common.model.order;

import java.math.BigDecimal;

public class MyOrderModel {

    private int id;
    private String estadoPedidoDesc;
    private int campaniaID;
    private String fechaRegistro;
    private BigDecimal importeTotal;
    private String rutaPaqueteDocumentario;
    private int numeroPedido;
    private Integer estadoEncuesta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstadoPedidoDesc() {
        return estadoPedidoDesc;
    }

    public void setEstadoPedidoDesc(String estadoPedidoDesc) {
        this.estadoPedidoDesc = estadoPedidoDesc;
    }

    public int getCampaniaID() {
        return campaniaID;
    }

    public void setCampaniaID(int campaniaID) {
        this.campaniaID = campaniaID;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public String getRutaPaqueteDocumentario() {
        return rutaPaqueteDocumentario;
    }

    public void setRutaPaqueteDocumentario(String rutaPaqueteDocumentario) {
        this.rutaPaqueteDocumentario = rutaPaqueteDocumentario;
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Integer getEstadoEncuesta() {
        return estadoEncuesta;
    }

    public void setEstadoEncuesta(Integer estadoEncuesta) {
        this.estadoEncuesta = estadoEncuesta;
    }
}

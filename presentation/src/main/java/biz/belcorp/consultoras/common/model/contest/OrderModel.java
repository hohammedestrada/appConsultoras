package biz.belcorp.consultoras.common.model.contest;

/**
 *
 */
public class OrderModel {

    private Integer campania;
    private Integer aceptacionConsultoraDA;
    private String cuv;
    private Integer cantidad;
    private Integer origenPedidoWeb;

    public Integer getCampania() {
        return campania;
    }

    public void setCampania(Integer campania) {
        this.campania = campania;
    }

    public Integer getAceptacionConsultoraDA() {
        return aceptacionConsultoraDA;
    }

    public void setAceptacionConsultoraDA(Integer aceptacionConsultoraDA) {
        this.aceptacionConsultoraDA = aceptacionConsultoraDA;
    }

    public String getCuv() {
        return cuv;
    }

    public void setCuv(String cuv) {
        this.cuv = cuv;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getOrigenPedidoWeb() {
        return origenPedidoWeb;
    }

    public void setOrigenPedidoWeb(Integer origenPedidoWeb) {
        this.origenPedidoWeb = origenPedidoWeb;
    }
}

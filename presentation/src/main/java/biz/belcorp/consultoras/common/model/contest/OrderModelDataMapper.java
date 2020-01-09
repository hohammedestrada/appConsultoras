package biz.belcorp.consultoras.common.model.contest;

import javax.inject.Inject;

import biz.belcorp.consultoras.domain.entity.Order;

/**
 *
 */
public class OrderModelDataMapper {

    @Inject
    public OrderModelDataMapper() {
        // EMPTY
    }

    public Order transform(OrderModel input) {
        Order output = new Order();

        if (input != null) {
            output.setAceptacionConsultoraDA(input.getAceptacionConsultoraDA());
            output.setCampania(input.getCampania());
            output.setCantidad(input.getCantidad());
            output.setCuv(input.getCuv());
            output.setOrigenPedidoWeb(input.getOrigenPedidoWeb());
        }

        return output;
    }

    public OrderModel transform(Order input) {
        OrderModel output = new OrderModel();

        if (input != null) {
            output.setAceptacionConsultoraDA(input.getAceptacionConsultoraDA());
            output.setCampania(input.getCampania());
            output.setCantidad(input.getCantidad());
            output.setCuv(input.getCuv());
            output.setOrigenPedidoWeb(input.getOrigenPedidoWeb());
        }

        return output;
    }
}

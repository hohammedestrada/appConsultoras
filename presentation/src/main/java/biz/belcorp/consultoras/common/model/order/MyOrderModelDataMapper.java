package biz.belcorp.consultoras.common.model.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.MyOrder;

@PerActivity
public class MyOrderModelDataMapper {

    @Inject
    MyOrderModelDataMapper() {
    }

    public MyOrderModel transform(MyOrder input) {
        MyOrderModel output = null;

        if (null != input) {
            output = new MyOrderModel();
            output.setId(input.getId());
            output.setEstadoPedidoDesc(input.getEstadoPedidoDesc());
            output.setCampaniaID(input.getCampaniaID());
            output.setFechaRegistro(input.getFechaRegistro());
            output.setImporteTotal(input.getImporteTotal());
            output.setRutaPaqueteDocumentario(input.getRutaPaqueteDocumentario());
            output.setNumeroPedido(input.getNumeroPedido());
            output.setEstadoEncuesta(input.getEstadoEncuesta());
        }
        return output;
    }

    public MyOrder transform(MyOrderModel input) {
        MyOrder output = null;

        if (null != input) {
            output = new MyOrder();
            output.setId(input.getId());
            output.setEstadoPedidoDesc(input.getEstadoPedidoDesc());
            output.setCampaniaID(input.getCampaniaID());
            output.setFechaRegistro(input.getFechaRegistro());
            output.setImporteTotal(input.getImporteTotal());
            output.setRutaPaqueteDocumentario(input.getRutaPaqueteDocumentario());
            output.setNumeroPedido(input.getNumeroPedido());
            output.setEstadoEncuesta(input.getEstadoEncuesta());
        }
        return output;
    }

    public List<MyOrderModel> transform(Collection<MyOrder> input) {
        List<MyOrderModel> output = new ArrayList<>();

        if (null == input) {
            return output;
        }

        for (MyOrder entity : input) {
            final MyOrderModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }

    public Collection<MyOrder> transform(List<MyOrderModel> input) {
        List<MyOrder> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (MyOrderModel entity : input) {
            final MyOrder model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }
}

package biz.belcorp.consultoras.common.model.debt;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.DebtRequest;

/**
 *
 */
@PerActivity
public class DebtModelMapper {

    @Inject
    DebtModelMapper() {
        // EMPTY
    }

    public DebtRequest transform(DebtModel debtModel) {
        DebtRequest debtRequest = new DebtRequest();
        debtRequest.setClienteLocalID(debtModel.getClienteLocalID());
        debtRequest.setClienteID(debtModel.getClienteID());
        debtRequest.setCodigoCampania(debtModel.getCodigoCampania());
        debtRequest.setMonto(debtModel.getMonto());
        debtRequest.setNota(debtModel.getNote());
        debtRequest.setDescripcion(debtModel.getDesc());
        debtRequest.setTipoMovimiento(debtModel.getTipoMovimiento());
        debtRequest.setFecha(debtModel.getFecha());
        debtRequest.setEstado(debtModel.getEstado());

        return debtRequest;
    }
}

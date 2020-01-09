package biz.belcorp.consultoras.common.view;

import biz.belcorp.consultoras.common.model.error.BusinessErrorModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;

public interface ErrorView {
    /**
     * Muestra un mensaje de error
     *
     * @param error Entidad que representa el detalle del error.
     */
    void showError(ErrorModel error);

    /**
     * Muestra un mensaje de error para business
     *
     * @param businessError Entidad que representa el detalle del business error.
     */
    void showBusinessError(BusinessErrorModel businessError);
}

package biz.belcorp.consultoras.feature.home.tracking;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.tracking.TrackingModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface TrackingView extends View, LoadingView {

    /**
     * Muestra el historial de deudas y pagos.
     */
    void showTracking(List<TrackingModel> trackingModels);

    void onError(Throwable throwable);

    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);
}

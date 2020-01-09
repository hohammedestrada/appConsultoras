package biz.belcorp.consultoras.feature.home.tracking;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.tracking.TrackingModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface TrackingDetailView extends View, LoadingView {

    /**
     * Muestra el historial de deudas y pagos.
     */
    void showTracking(TrackingModel trackingModel);

    void onError(Throwable throwable);

    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);
}

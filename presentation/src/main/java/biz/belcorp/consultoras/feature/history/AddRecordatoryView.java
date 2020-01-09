package biz.belcorp.consultoras.feature.history;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.view.LoadingView;

/**
 *
 */
interface AddRecordatoryView extends View, LoadingView {

    void deleteData();

    void saveData();

    void onError(Throwable exception);

    void initScreenTrack(LoginModel transform);
}

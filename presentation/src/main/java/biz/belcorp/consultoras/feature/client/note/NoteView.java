package biz.belcorp.consultoras.feature.client.note;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.AnotacionModel;
import biz.belcorp.consultoras.common.view.LoadingView;

public interface NoteView extends View, LoadingView {

    void show(AnotacionModel model);

    void saved(Boolean result);

    void onError(Throwable exception);

    void anotacionSaved(AnotacionModel anotacionModel);

    void anotacionUpdated(AnotacionModel anotacionModel);

    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);
}

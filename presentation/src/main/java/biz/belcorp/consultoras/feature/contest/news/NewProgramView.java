package biz.belcorp.consultoras.feature.contest.news;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.model.error.BooleanDtoModel;
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface NewProgramView extends View, LoadingView {

    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);

    void initializeAdapter(String countryISO);

    void showContest(ConcursoModel model, String campaingCurrent, String countryMoneySymbol);

    void orderAdded();

    void removeReservation();

    void onError(ErrorModel errorModel);

    void onOrderError(BooleanDtoModel errorModel);

    void onReservationError(BooleanDtoModel errorModel);
}

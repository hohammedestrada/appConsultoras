package biz.belcorp.consultoras.feature.home.incentives;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.common.view.TrackView;

interface IncentivesContainerView extends View, LoadingView, TrackView {

    void onError(ErrorModel errorModel);

    void showByActive(List<ConcursoModel> newProgramContest,
                      List<ConcursoModel> constanciaCurrentContest,
                      List<ConcursoModel> listaPedidoCurrentContest,
                      List<ConcursoModel> listaPedidoPreviousContest,
                      ConcursoModel brightPathContest,
                      String campaingCurrent, String countryMoneySymbol, String countryISO);

    void showByHistory(List<ConcursoModel> historyCurrentContest, String countryISO);

    void initializeAdapter(String countryISO);

    void trackEvent(String screenHome,
                    String eventCat,
                    String eventAction,
                    String eventLabel,
                    String eventName,
                    LoginModel transform);

    void showDefaultContainer();

    void loadViewBonifDigital(String url);
}

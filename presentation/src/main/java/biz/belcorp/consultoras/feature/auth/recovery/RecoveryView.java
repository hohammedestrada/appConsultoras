package biz.belcorp.consultoras.feature.auth.recovery;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.country.CountryModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.view.LoadingView;

public interface RecoveryView extends View, LoadingView {

    void renderData(String countrySIM, List<CountryModel> countries);

    void success(String mail);

    void failed(ErrorModel error);

}

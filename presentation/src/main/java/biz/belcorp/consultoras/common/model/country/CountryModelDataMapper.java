package biz.belcorp.consultoras.common.model.country;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Country;

/**
 *
 */

@PerActivity
public class CountryModelDataMapper {

    @Inject
    CountryModelDataMapper() {
    }

    public CountryModel transform(Country entity) {
        CountryModel model = null;

        if (null != entity) {
            model = new CountryModel();
            model.setId(entity.getId());
            model.setIso(entity.getIso());
            model.setName(entity.getName());
            model.setUrlImage(entity.getUrlImage());
            model.setFocusBrand(entity.getFocusBrand());
            model.setTextHelpUser(entity.getTextHelpUser());
            model.setTextHelpPassword(entity.getTextHelpPassword());
            model.setUrlJoinBelcorp(entity.getUrlJoinBelcorp());
            model.setConfigForgotPassword(entity.getConfigForgotPassword());
            model.setShowDecimals(entity.isShowDecimals() ? 1 : 0);
            model.setUrlTerminos(entity.getUrlTerminos());
            model.setUrlPrivacidad(entity.getUrlPrivacidad());
            model.setReceiverFeedBack(entity.getReceiverFeedBack());

            model.setCapturaDatosConsultora(entity.getCapturaDatosConsultora() != null ? entity.getCapturaDatosConsultora():false);
            model.setTelefono1(entity.getTelefono1());
            model.setTelefono2(entity.getTelefono2());
            model.setUrlContratoActualizacionDatos(entity.getUrlContratoActualizacionDatos());
            model.setUrlContratoVinculacion(entity.getUrlContratoVinculacion());

        }
        return model;
    }

    public Country transform(CountryModel model) {
        Country entity = null;

        if (null != model) {
            entity = new Country();
            entity.setId(model.getId());
            entity.setName(model.getName());
            entity.setIso(model.getIso());
            entity.setUrlImage(model.getUrlImage());
            entity.setFocusBrand(model.getFocusBrand());
            entity.setTextHelpUser(model.getTextHelpUser());
            entity.setTextHelpPassword(model.getTextHelpPassword());
            entity.setUrlJoinBelcorp(model.getUrlJoinBelcorp());
            entity.setConfigForgotPassword(model.getConfigForgotPassword());
            entity.setShowDecimals(model.getShowDecimals() == 1);
            entity.setUrlTerminos(model.getUrlTerminos());
            entity.setUrlPrivacidad(model.getUrlPrivacidad());

            entity.setReceiverFeedBack(model.getReceiverFeedBack());
            entity.setCapturaDatosConsultora(model.isCapturaDatosConsultora());
            entity.setTelefono1(model.getTelefono1());
            entity.setTelefono2(model.getTelefono2());
            entity.setUrlContratoActualizacionDatos(model.getUrlContratoActualizacionDatos());
            entity.setUrlContratoVinculacion(model.getUrlContratoVinculacion());

        }
        return entity;
    }

    public List<CountryModel> transformToDataModel(Collection<Country> list) {
        List<CountryModel> collection = new ArrayList<>();

        if (null == list) {
            return Collections.emptyList();
        }

        for (Country entity : list) {
            final CountryModel model = transform(entity);
            if (null != model) {
                collection.add(model);
            }
        }

        return collection;
    }

    public List<Country> transformToDataDomain(Collection<CountryModel> list) {
        List<Country> collection = new ArrayList<>();

        if (null == list) {
            return Collections.emptyList();
        }

        for (CountryModel model : list) {
            final Country obj = transform(model);
            if (null != model) {
                collection.add(obj);
            }
        }

        return collection;
    }


}

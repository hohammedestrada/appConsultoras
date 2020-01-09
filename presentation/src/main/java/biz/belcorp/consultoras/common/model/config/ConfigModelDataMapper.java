package biz.belcorp.consultoras.common.model.config;

import javax.inject.Inject;

import biz.belcorp.consultoras.common.model.country.CountryModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.ConfigReponse;


@PerActivity
public class ConfigModelDataMapper {

    CountryModelDataMapper countryModelDataMapper;

    @Inject
    ConfigModelDataMapper(CountryModelDataMapper countryModelDataMapper) {
        this.countryModelDataMapper = countryModelDataMapper;
    }

    public ConfigModel transform(ConfigReponse entity) {
        ConfigModel model = null;

        if (null != entity) {
            model = new ConfigModel();
            model.setTextGreeting(entity.getTextGreeting());
            model.setUrlImageEsikaBackground(entity.getUrlImageEsikaBackground());
            model.setUrlImageEsikaLogo(entity.getUrlImageEsikaLogo());
            model.setUrlImageLBelBackground(entity.getUrlImageLBelBackground());
            model.setUrlImageLBelLogo(entity.getUrlImageLBelLogo());
            model.setIdVideo(entity.getIdVideo());
            model.setUrlVideo(entity.getUrlVideo());
            model.setCountries(countryModelDataMapper.transformToDataModel(entity.getCountries()));
        }
        return model;
    }

    public ConfigReponse transform(ConfigModel model) {
        ConfigReponse entity = null;

        if (null != model) {
            entity = new ConfigReponse();
            entity.setTextGreeting(model.getTextGreeting());
            entity.setUrlImageEsikaBackground(model.getUrlImageEsikaBackground());
            entity.setUrlImageEsikaLogo(model.getUrlImageEsikaLogo());
            entity.setUrlImageLBelBackground(model.getUrlImageLBelBackground());
            entity.setUrlImageLBelLogo(model.getUrlImageLBelLogo());
            entity.setIdVideo(model.getIdVideo());
            entity.setUrlVideo(model.getUrlVideo());
            entity.setCountries(countryModelDataMapper.transformToDataDomain(model.getCountries()));
        }
        return entity;
    }

}

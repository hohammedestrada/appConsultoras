package biz.belcorp.consultoras.common.model.config;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Config;

@PerActivity
public class ConfigAppModelDataMapper {

    @Inject
    ConfigAppModelDataMapper() {
    }

    public Config transform(ConfigAppModel model) {
        Config config = null;

        if (null != model) {
            config = new Config();
            config.setConnectivityType(model.getConnectivityType());
            config.setNotification(model.isNotification());
            config.setSonido(model.isSonido());
        }
        return config;
    }

    public ConfigAppModel transform(Config model) {
        ConfigAppModel config = null;

        if (null != model) {
            config = new ConfigAppModel();
            config.setConnectivityType(model.getConnectivityType());
            config.setNotification(model.isNotification());
            config.setSonido(model.isSonido());
        }
        return config;
    }

}

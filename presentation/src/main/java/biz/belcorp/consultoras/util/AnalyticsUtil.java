package biz.belcorp.consultoras.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.ConsultorasApp;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.user.UserModel;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.feature.catalog.CatalogContainerFragment;
import biz.belcorp.consultoras.feature.embedded.offers.OffersFragment;
import biz.belcorp.consultoras.feature.home.HomeFragment;
import biz.belcorp.consultoras.feature.home.clients.ClientsListFragment;
import biz.belcorp.consultoras.feature.home.incentives.IncentivesContainerFragment;
import biz.belcorp.consultoras.util.anotation.NetworkEventType;
import biz.belcorp.library.notification.TipoIngreso;

public class AnalyticsUtil {

    AnalyticsUtil() {
        // EMPTY
    }

    public static Bundle getUserProperties(LoginModel model) {

        Bundle properties = new Bundle();

        properties.putString(GlobalConstant.TRACK_VAR_CAMPAING, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_COUNTRY, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_REGION, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_ZONA, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_CONSULTANT_CODE, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_USER_TYPE, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_RANKING, null);
        properties.putString(GlobalConstant.TRACK_VAR_PERIOD, null);
        properties.putString(GlobalConstant.TRACK_VAR_PERIOD_WEEK, null);
        properties.putString(GlobalConstant.TRACK_VAR_PARTNER_LEVEL, null);
        properties.putString(GlobalConstant.TRACK_VAR_ROL, null);
        properties.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);
        properties.putString(GlobalConstant.TRACK_VAR_TIPO_INGRESO, TipoIngreso.ESTANDAR);
        properties.putString(GlobalConstant.TRACK_VAR_NOMBRE_PUSH, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_SEGMENTO_PATROCINADO, "");
        properties.putString(GlobalConstant.TRACK_VAR_SECCION, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_SEGMENTO_CONSTANCIA, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_INSCRIPCION_EPM, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_GRUPO_CONSULTORA, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);

        if (null != model) {
            properties = new Bundle();
            properties.putString(GlobalConstant.TRACK_VAR_CAMPAING, model.getCampaing());
            properties.putString(GlobalConstant.TRACK_VAR_COUNTRY, model.getCountryISO());
            properties.putString(GlobalConstant.TRACK_VAR_REGION, model.getRegionCode());
            properties.putString(GlobalConstant.TRACK_VAR_ZONA, model.getZoneCode());
            properties.putString(GlobalConstant.TRACK_VAR_CONSULTANT_CODE, model.getConsultantCode());
            properties.putString(GlobalConstant.TRACK_VAR_USER_TYPE, String.valueOf(model.getUserType()));
            properties.putString(GlobalConstant.TRACK_VAR_RANKING, null);
            properties.putString(GlobalConstant.TRACK_VAR_PERIOD, model.getPeriodo());
            properties.putString(GlobalConstant.TRACK_VAR_PERIOD_WEEK, model.getSemanaPeriodo());
            properties.putString(GlobalConstant.TRACK_VAR_PARTNER_LEVEL, model.getDescripcionNivelLider());
            properties.putString(GlobalConstant.TRACK_VAR_ROL, getUserRol(model.getLider()));
            properties.putString(GlobalConstant.TRACK_VAR_TIPO_INGRESO, model.getTipoIngreso());
            switch (model.getTipoIngreso()){
                case TipoIngreso.PUSH_NOTIFICATION:
                    properties.putString(GlobalConstant.TRACK_VAR_NOMBRE_PUSH, model.getCampaing());
                    break;
                case TipoIngreso.ESTANDAR:
                default:
                    properties.putString(GlobalConstant.TRACK_VAR_NOMBRE_PUSH, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
                    break;
            }

            if (ConsultorasApp.getInstance().getDatamiType() == NetworkEventType.DATAMI_AVAILABLE) {
                properties.putString(GlobalConstant.TRACK_VAR_SEGMENTO_PATROCINADO, model.getSegmentoDatami());
            } else {
                properties.putString(GlobalConstant.TRACK_VAR_SEGMENTO_PATROCINADO, "");
            }

            properties.putString(GlobalConstant.TRACK_VAR_SECCION, model.getCodigoSeccion());
            properties.putString(GlobalConstant.TRACK_VAR_SEGMENTO_CONSTANCIA, model.getSegmentoConstancia());

            if (model.isRDEsSuscrita()) {
                properties.putString(GlobalConstant.TRACK_VAR_INSCRIPCION_EPM, GlobalConstant.LABEL_SUSCRITA);
            } else {
                properties.putString(GlobalConstant.TRACK_VAR_INSCRIPCION_EPM, GlobalConstant.LABEL_NO_SUSCRITA);
            }

            properties.putString(GlobalConstant.TRACK_VAR_GRUPO_CONSULTORA, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        }

        return properties;
    }

    public static Bundle getUserProperties(User user) {

        Bundle properties = new Bundle();

        properties.putString(GlobalConstant.TRACK_VAR_CAMPAING, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_COUNTRY, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_REGION, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_ZONA, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_CONSULTANT_CODE, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_USER_TYPE, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_RANKING, null);
        properties.putString(GlobalConstant.TRACK_VAR_PERIOD, null);
        properties.putString(GlobalConstant.TRACK_VAR_PERIOD_WEEK, null);
        properties.putString(GlobalConstant.TRACK_VAR_PARTNER_LEVEL, null);
        properties.putString(GlobalConstant.TRACK_VAR_ROL, null);
        properties.putString(GlobalConstant.TRACK_VAR_TIPO_INGRESO, TipoIngreso.ESTANDAR);
        properties.putString(GlobalConstant.TRACK_VAR_NOMBRE_PUSH, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_SEGMENTO_PATROCINADO, "");
        properties.putString(GlobalConstant.TRACK_VAR_SECCION, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_SEGMENTO_CONSTANCIA, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_INSCRIPCION_EPM, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        properties.putString(GlobalConstant.TRACK_VAR_GRUPO_CONSULTORA, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);

        if (null != user) {
            properties = new Bundle();
            properties.putString(GlobalConstant.TRACK_VAR_CAMPAING, user.getCampaing());
            properties.putString(GlobalConstant.TRACK_VAR_COUNTRY, user.getCountryISO());
            properties.putString(GlobalConstant.TRACK_VAR_REGION, user.getRegionCode());
            properties.putString(GlobalConstant.TRACK_VAR_ZONA, user.getZoneCode());
            properties.putString(GlobalConstant.TRACK_VAR_CONSULTANT_CODE, user.getConsultantCode());
            properties.putString(GlobalConstant.TRACK_VAR_USER_TYPE, String.valueOf(user.getUserType()));
            properties.putString(GlobalConstant.TRACK_VAR_RANKING, null);
            properties.putString(GlobalConstant.TRACK_VAR_PERIOD, user.getPeriodo());
            properties.putString(GlobalConstant.TRACK_VAR_PERIOD_WEEK, user.getSemanaPeriodo());
            properties.putString(GlobalConstant.TRACK_VAR_PARTNER_LEVEL, user.getDescripcionNivelLider());
            properties.putString(GlobalConstant.TRACK_VAR_ROL, getUserRol(user.getLider() != null ? user.getLider() : 0));
            properties.putString(GlobalConstant.TRACK_VAR_TIPO_INGRESO, user.getTipoIngreso());

            switch (user.getTipoIngreso()){
                case TipoIngreso.PUSH_NOTIFICATION:
                    properties.putString(GlobalConstant.TRACK_VAR_NOMBRE_PUSH, user.getCampaing());
                    break;
                case TipoIngreso.ESTANDAR:
                default:
                    properties.putString(GlobalConstant.TRACK_VAR_NOMBRE_PUSH, "(not available)");
                    break;
            }

            switch (ConsultorasApp.getInstance().getDatamiType()) {
                case NetworkEventType.DATAMI_AVAILABLE :
                    properties.putString(GlobalConstant.TRACK_VAR_SEGMENTO_PATROCINADO, user.getSegmentoDatami());
                    break;
                default:
                    properties.putString(GlobalConstant.TRACK_VAR_SEGMENTO_PATROCINADO, "");
                    break;
            }

            properties.putString(GlobalConstant.TRACK_VAR_SECCION, user.getCodigoSeccion());
            properties.putString(GlobalConstant.TRACK_VAR_SEGMENTO_CONSTANCIA, user.getSegmentoConstancia());

            if (user.isRDEsSuscrita() != null && user.isRDEsSuscrita()) {
                properties.putString(GlobalConstant.TRACK_VAR_INSCRIPCION_EPM, GlobalConstant.LABEL_SUSCRITA);
            } else {
                properties.putString(GlobalConstant.TRACK_VAR_INSCRIPCION_EPM, GlobalConstant.LABEL_NO_SUSCRITA);
            }
            properties.putString(GlobalConstant.TRACK_VAR_GRUPO_CONSULTORA, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);

        }

        return properties;
    }

    public static Bundle getUserNullProperties(UserModel model) {

        Bundle properties = new Bundle();

        properties.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        if (null != model) {
            properties = new Bundle();
            properties.putString(GlobalConstant.TRACK_VAR_CAMPAING, model.getCampaing());
            properties.putString(GlobalConstant.TRACK_VAR_COUNTRY, model.getCountryISO());
            properties.putString(GlobalConstant.TRACK_VAR_REGION, model.getRegionCode());
            properties.putString(GlobalConstant.TRACK_VAR_ZONA, model.getZoneCode());
            properties.putString(GlobalConstant.TRACK_VAR_CONSULTANT_CODE, model.getConsultantCode());
            properties.putString(GlobalConstant.TRACK_VAR_USER_TYPE, String.valueOf(model.getUserType()));
            properties.putString(GlobalConstant.TRACK_VAR_RANKING, null);
            properties.putString(GlobalConstant.TRACK_VAR_PERIOD, model.getPeriodo());
            properties.putString(GlobalConstant.TRACK_VAR_PERIOD_WEEK, model.getSemanaPeriodo());
            properties.putString(GlobalConstant.TRACK_VAR_PARTNER_LEVEL, model.getDescripcionNivelLider());
            properties.putString(GlobalConstant.TRACK_VAR_ROL, getUserRol(model.getLider()));
            properties.putString(GlobalConstant.TRACK_VAR_SEGMENTO_PATROCINADO, null);
            properties.putString(GlobalConstant.TRACK_VAR_SECCION, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
            properties.putString(GlobalConstant.TRACK_VAR_SEGMENTO_CONSTANCIA, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
            properties.putString(GlobalConstant.TRACK_VAR_INSCRIPCION_EPM, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
            properties.putString(GlobalConstant.TRACK_VAR_GRUPO_CONSULTORA, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        }

        return properties;
    }

    public static Bundle getUserNullProperties() {

        Bundle properties = new Bundle();

        properties.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);
        properties.putString(GlobalConstant.TRACK_VAR_CAMPAING, null);
        properties.putString(GlobalConstant.TRACK_VAR_COUNTRY, null);
        properties.putString(GlobalConstant.TRACK_VAR_REGION, null);
        properties.putString(GlobalConstant.TRACK_VAR_ZONA, null);
        properties.putString(GlobalConstant.TRACK_VAR_CONSULTANT_CODE, null);
        properties.putString(GlobalConstant.TRACK_VAR_USER_TYPE, null);
        properties.putString(GlobalConstant.TRACK_VAR_RANKING, null);
        properties.putString(GlobalConstant.TRACK_VAR_PERIOD, null);
        properties.putString(GlobalConstant.TRACK_VAR_PERIOD, null);
        properties.putString(GlobalConstant.TRACK_VAR_PERIOD_WEEK, null);
        properties.putString(GlobalConstant.TRACK_VAR_PARTNER_LEVEL, null);
        properties.putString(GlobalConstant.TRACK_VAR_TIPO_INGRESO, null);
        properties.putString(GlobalConstant.TRACK_VAR_NOMBRE_PUSH, null);
        properties.putString(GlobalConstant.TRACK_VAR_SEGMENTO_PATROCINADO, null);
        properties.putString(GlobalConstant.TRACK_VAR_SECCION, null);
        properties.putString(GlobalConstant.TRACK_VAR_SEGMENTO_CONSTANCIA, null);
        properties.putString(GlobalConstant.TRACK_VAR_INSCRIPCION_EPM, null);
        properties.putString(GlobalConstant.TRACK_VAR_GRUPO_CONSULTORA, null);

        return properties;
    }

    public static String getScreenName(Fragment fragment) {
        if (fragment == null) return GlobalConstant.EVENT_LABEL_NOT_AVAILABLE;

        if (fragment instanceof HomeFragment) {
            return GlobalConstant.SCREEN_HOME;
        } else if (fragment instanceof ClientsListFragment) {
            ClientsListFragment listFragment = (ClientsListFragment) fragment;
            switch (listFragment.getCurrentPage()) {
                case 0:
                    return GlobalConstant.SCREEN_CLIENTS_ALL;
                case 1:
                    return GlobalConstant.SCREEN_CLIENTS_DEBT;
                case 2:
                    return GlobalConstant.SCREEN_CLIENTS_WITH_ORDER;
                default:
                    break;
            }
        } else if (fragment instanceof CatalogContainerFragment) {
            CatalogContainerFragment listFragment = (CatalogContainerFragment) fragment;
            switch (listFragment.getCurrentPage()) {
                case 0:
                    return GlobalConstant.SCREEN_CATALOG_CATALOG;
                case 1:
                    return GlobalConstant.SCREEN_CATALOG_MAGAZINE;
                default:
                    break;
            }
        } else if (fragment instanceof IncentivesContainerFragment) {
            IncentivesContainerFragment listFragment = (IncentivesContainerFragment) fragment;
            switch (listFragment.getCurrentPage()) {
                case 0:
                    return GlobalConstant.SCREEN_INCENTIVES_GIFT_ORDER;
                case 1:
                    return GlobalConstant.SCREEN_INCENTIVES_GIFT_HISTORIC;
                default:
                    break;
            }
        } else if (fragment instanceof OffersFragment) {
            return GlobalConstant.SCREEN_OFFERS;
        }

        return GlobalConstant.EVENT_LABEL_NOT_AVAILABLE;
    }

    public static String getMenuLabel(int type) {
        String eventName;
        switch (type) {
            case 0:
                eventName = GlobalConstant.EVENT_NAME_NAVIGATION_LATERAL;
                break;
            case 1:
            default:
                eventName = GlobalConstant.EVENT_NAME_NAVIGATION_TOP;
        }

        return eventName;
    }

    private static String getUserRol(int lider) {

        if (lider == 1) {
            return GlobalConstant.ROL_PARTNER;
        } else {
            return GlobalConstant.ROL_CONSULTANT;
        }
    }

}

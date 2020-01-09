package biz.belcorp.consultoras.common.fcm;

import android.content.Context;

import com.sap.cec.marketing.ymkt.mobile.configuration.manager.ConfigurationManager;
import com.sap.cec.marketing.ymkt.mobile.configuration.model.Configuration;

import biz.belcorp.consultoras.BuildConfig;

public class HybrisManager {

    private static HybrisManager instance = new HybrisManager();

    private HybrisManager() {
    }

    public static HybrisManager getInstance() {
        return instance;
    }

    public Configuration getConfiguration(Context context) {
        Configuration configuration =
            ConfigurationManager.getActiveConfiguration(context);
        if (configuration == null) {
            String config = "{\"type\":\"CLOUD\",\"system\":\"0M25MU7\",\"client\":\"100\"," +
                "\"auth\":{\"scheme\":\"CSRF\",\"username\":\"belcorpdev@gmail.com\"," +
                "\"password\":\"&O7\\\\S%EW\"}," +
                "\"urls\":{\"token\":\"" + BuildConfig.HM_MIDDLEWARE_URL + "/sap/opu/odata/sap/CUAN_IMPORT_SRV/$metadata\"," +
                "\"contact\":\"" + BuildConfig.HM_MIDDLEWARE_URL + "/sap/opu/odata/sap/CUAN_IMPORT_SRV/ImportHeaders\"," +
                "\"offer\":\"" + BuildConfig.HM_MIDDLEWARE_URL + "/sap/opu/odata/sap/CUAN_OFFER_DISCOVERY_SRV\"," +
                "\"recommendations\":\"" + BuildConfig.HM_MIDDLEWARE_URL + "/sap/opu/odata/sap/CUAN_OFFER_DISCOVERY_SRV/Recommendations\"," +
                "\"coupon\":\"" + BuildConfig.HM_MIDDLEWARE_URL + "/sap/opu/odata/sap/CUAN_OFFER_DISCOVERY_SRV/GetCouponCode\"," +
                "\"wallet\":\"\"},\"tracker\":{\"enabled\":true,\"scheme\":\"MIDDLEWARE\"," +
                "\"url\":\"" + BuildConfig.HM_MIDDLEWARE_URL + "\"}}";
            configuration = ConfigurationManager.saveConfiguration(context
                , config);
        }
        return configuration;
    }
}

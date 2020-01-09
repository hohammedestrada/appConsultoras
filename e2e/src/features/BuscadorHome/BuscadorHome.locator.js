let mainConfig = require("../index");
const byId = "#" + mainConfig.appPackage;

config = {
    appPackage: mainConfig.appPackage,
    locator: {
        fieldBuscar: byId + ":id/edt_search",
        ClickAgregaProducto: byId + ":id/btnAddItem",
        ClickAgregarProductoxDesc:
            "//hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.ScrollView/android.widget.LinearLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout",
        ClickAgregarProductoxLand:
            "//android.widget.RelativeLayout[4]/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.LinearLayout[2]/android.widget.LinearLayout[2]",
        ClickRegresaHome: byId + ":id/lnlBack",
        TxtDescripcionVarios:
            "//hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.ScrollView/android.widget.LinearLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.TextView[1]",
        ClickVerMasResultado: byId + ":id/btnResultados"
    }
};

exports.config = config;
exports.locator = config.locator;

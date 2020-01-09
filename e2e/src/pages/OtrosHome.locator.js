let mainConfig = require("./../features/index");
const byId = "#" + mainConfig.appPackage;

config = {
    appPackage: mainConfig.appPackage,
    locator: {
        //ClickMenu:byId+':id/flt_menu',
        ClickMenu: byId + ":id/ivw_drawer",

        ClickBotonBuscaHome: byId + ":id/ivwSearch",
        ClickRegresaBus: byId + ":id/lnlBack",
        ClickRegresaHome:
            "//android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.ImageButton",
        //ClickPasePedido:'//android.widget.LinearLayout/android.widget.RelativeLayout/android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[2]',
        //ClickPasePedido:byId+':id/ivw_item_imagen',
        ClickPasePedido:
            "//android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.RelativeLayout/android.view.ViewGroup/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[2]",
        ClickGanaMas:
            "//android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.RelativeLayout/android.view.ViewGroup/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[3]",

        //Estrellas Playstore
        ClickOptDone: byId + ":id/alreadyButton",
        ClickOptLater: byId + ":id/afterButton",
        ClickOptYes: byId + ":id/yesButton",

        //Pop up Catalogo
        ClickCerrarPopCata: byId + ":id/closeDialog"

        /*
        ClickCampoProducto:byId+':id/llt_product_filter',
        fieldProducto:byId+':id/edtProductFilter',
        fieldCantidad:byId+':id/tvwCantidad',
        ClickBotonGR:byId+':id/btnOrderAdd'
        */
    }
};

exports.config = config;
exports.locator = config.locator;

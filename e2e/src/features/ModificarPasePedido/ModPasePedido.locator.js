let mainConfig = require("../index");
const byId = "#" + mainConfig.appPackage;

config = {
    appPackage: mainConfig.appPackage,
    locator: {
        // Propios del Modificar Pedido
        fieldcuvagregado: byId + ":id/tvwCuv",
        TextBotonGR: byId + ":id/textButton",
        fieldUpdEmail: byId + ":id/edtUpdateEmail",
        ClickActualizarEmail: byId + ":id/btnUpdateEmail",
        ClickCerrarDialogo: byId + ":id/ibtCloseDialog",

        //TextPedidoGuardado:byId+':id/tvwTitle',
        ClickAceptar: byId + ":id/btnDialog",
        TextPedidoReservado: byId + ":id/tvwTitle",
        ClickEscogerRegalo: byId + ":id/btnDialog",
        ClickSigueComprando: byId + ":id/lnrSeguirComprando",
        ClickBuscador: byId + ":id/item_search",
        ClickBack: byId + ":id/lnlBack",

        ContadorProductoPedido: byId + ":id/tvwListarPorProductoCounter",
        //1 solo producto
        ClickEliminar: byId + ":id/ivwDelete",
        ClickConfirmaSi: byId + ":id/btn_tooltip_detele",
        ClickConfirmaNo: byId + ":id/btn_tooltip_no",

        //Mas de 1 producto
        //ClickCUVDinamico:'//android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout['+n+']/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.TextView[1]',
        //ClickEliminaDinamico:'//android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout['+n+']/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.ImageView',

        //Modificar un solo producto
        ClickBotonCantidad: byId + ":id/rltData",
        TxtSeccionBox: byId + ":id/lltBoxSection",
        ClickModificar: byId + ":id/simpleButton"
    }
};

exports.config = config;
exports.locator = config.locator;

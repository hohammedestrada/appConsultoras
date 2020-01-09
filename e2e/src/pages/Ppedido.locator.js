let mainConfig = require("./../features/index");
const byId = "#" + mainConfig.appPackage;

config = {
    appPackage: mainConfig.appPackage,
    locator: {
        //Agregar Producto de Catalogo
        ClickCampoProducto: byId + ":id/llt_product_filter",
        fieldProducto: byId + ":id/edtProductFilter",
        fieldCantidad: byId + ":id/tvwCantidad",
        clickAgregar: byId + ":id/btnAdd",

        // Para Poder Reservar o Guardar el Pedido
        ClickBackFromCliente:
            "//hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.view.ViewGroup/android.widget.ImageButton",
        ClickBotonGR: byId + ":id/btnOrderAdd",
        fieldCliente: byId + ":id/tvwClientFilter",

        //Oferta Final
        ClickOFTerminaPedido: byId + ":id/btnAccept",

        //Incrementar Cantidades
        ClickBotonIncCantidad: byId + ":id/btnAdd",
        ClickBotonIncCantidadxDesc:
            "//hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.ScrollView/android.widget.LinearLayout/android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout[3]",

        //Eliminar Producto
        ContadorProductoPedido: byId + ":id/tvwListarPorProductoCounter",
        //1 solo producto
        ClickEliminar: byId + ":id/ivwDelete",
        ClickConfirmaSi: byId + ":id/btn_tooltip_detele",
        ClickConfirmaNo: byId + ":id/btn_tooltip_no",

        //Modificar un solo producto
        ClickBotonCantidad: byId + ":id/rltData",
        TxtSeccionBox: byId + ":id/lltBoxSection",
        ClickModificar: byId + ":id/simpleButton",

        //Regalo o Kit de Inicio
        ClickEscogerRegalo: byId + ":id/btnDialog",
        ClickSigueComprando: byId + ":id/lnrSeguirComprando",

        // Propios del Modificar Pedido
        fieldcuvagregado: byId + ":id/tvwCuv",
        TextBotonGR: byId + ":id/textButton",
        fieldUpdEmail: byId + ":id/edtUpdateEmail",
        ClickActualizarEmail: byId + ":id/btnUpdateEmail",
        ClickCerrarDialogo: byId + ":id/ibtCloseDialog",

        //Pop uo Email
        ClickCerrarDialogo: byId + ":id/ibtCloseDialog",
        ClickAceptar: byId + ":id/btnDialog",

        //Multipedido
        ClickSwitchMultipedido: byId + ":id/multiOrderSwitch"
    }
};

exports.config = config;
exports.locator = config.locator;

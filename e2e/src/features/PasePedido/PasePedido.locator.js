let mainConfig = require("../index");
const byId = "#" + mainConfig.appPackage;

config = {
    appPackage: mainConfig.appPackage,
    locator: {
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

        ClickBotonGR: byId + ":id/btnOrderAdd",

        //CHILE
        fieldProductocl: "txtCodigoProducto",
        fieldCantidadcl: "txtCantidadProducto",
        ClickAgregarcl: "btnAgregarProducto",
        ReservarPedidocl: "btnGuardarPedido"
    }
};

exports.config = config;
exports.locator = config.locator;

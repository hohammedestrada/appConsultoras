const I = actor();
const utils = require("../../utils/utils");
const Ppedido = require("../../pages/Ppedido.module");
//const MCantidadP = require('../../pages/MCantidadP.module');
let wait = { retries: 5, minTimeout: 2000 };
// Add in your custom step files
//let config=utils.fnGetConfig("QAS")
let config = require("./ModPasePedido.locator");
let locator = config.locator;

module.exports = {
    //Nuevos módulos

    EjecutarOperacion(Operacion, Producto, Cantidad) {
        if (Operacion == "1") {
            //
            Ppedido.AgregaProductoPedido(Producto, Cantidad);
        }

        if (Operacion == "2") {
            //
            Ppedido.ModificaCantidadProducto(Producto, Cantidad);
        }

        if (Operacion == "3") {
            //
            Ppedido.EliminarProductoPedido(Producto);
        }
    }
};

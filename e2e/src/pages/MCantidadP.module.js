const I = actor();
const utils = require("../utils/utils");
let wait = { retries: 5, minTimeout: 2000 };
// Add in your custom step files
//let config=utils.fnGetConfig("QAS");
let config = require("./MCantidadP.locator");
let locator = config.locator;

module.exports = {
    AgregarCantidad(Cantidad) {
        for (i = 1; i <= Cantidad; i++) {
            I.retry(wait).click(locator.ClickBotonIncCantidad);
        } //Pase de Pedido

        // Samsung   220 190
        I.wait(2);
    },

    AgregarCantidadxDesc(Cantidad) {
        for (i = 1; i <= Cantidad; i++) {
            I.retry(wait).click(locator.ClickBotonIncCantidadxDesc);
        } //Pase de Pedido
        // Samsung   220 190
        I.wait(2);
    },

    AgregarCantidadxLand(Cantidad) {
        for (i = 1; i <= Cantidad; i++) {
            I.retry(wait).click(locator.ClickBotonIncCantidadxLand);
        } //Pase de Pedido
        // Samsung   220 190
        I.wait(2);
    }
};

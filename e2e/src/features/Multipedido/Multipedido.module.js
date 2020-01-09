const I = actor();
//const utils= require('../../utils/utils');
const Ppedido = require("../../pages/Ppedido.module");
let wait = { retries: 5, minTimeout: 2000 };
// Add in your custom step files
//let config=utils.fnGetConfig("QAS");
let config = require("./Multipedido.locator");
let locator = config.locator;

module.exports = {
    ActivarCheckMulti(FlagM) {
        if (FlagM == "Si") {
            //Ppedido.DesbloquearAccionPase();
            I.swipeUp(locator.ClickSwitchMultipedido);
            I.retry(wait).checkOption(locator.ClickSwitchMultipedido);
            I.wait(5);
        }
    },

    AceptarReserva(Freserva) {
        if (Freserva == "Si") {
            Ppedido.GuardarReservarPedido();
        }
    }
};

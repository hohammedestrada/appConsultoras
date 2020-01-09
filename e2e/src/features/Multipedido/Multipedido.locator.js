let mainConfig = require("../index");
const byId = "#" + mainConfig.appPackage;

config = {
    appPackage: mainConfig.appPackage,
    locator: {
        ClickSwitchMultipedido: byId + ":id/multiOrderSwitch"
    }
};

exports.config = config;
exports.locator = config.locator;

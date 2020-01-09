let mainConfig = require("../index");
const byId = "#" + mainConfig.appPackage;

config = {
    appPackage: mainConfig.appPackage,
    locator: {
        RTextBannerGPR: byId + ":id/llt_rejected_order",
        ClickVerDetalle: byId + ":id/tvw_see_detail",
        FTextBannerGPR: byId + ":id/llt_accepted_order",
        ClickModificar: byId + ":id/tvw_edit_order"
    }
};

exports.config = config;
exports.locator = config.locator;

const utils = require("./../utils/utils");
let steps = utils.fnGetFiles("./src/features", ".steps.js");

let config = {
    ambiente: "DEV",
    app: process.env.APPIUM_APK_PATH || "./src/app/consultoras-esika-develop-1.4.2.apk",
    host: process.env.APPIUM_HOST || "0.0.0.0",
    platform: "Android",
    device: "emulator",
    host: process.env.APPIUM_HOST || "0.0.0.0",
    desiredCapabilities: {
        appPackage: "biz.belcorp.consultoras.esika",
        appActivity: "biz.belcorp.consultoras.feature.splash.SplashActivity",
        platformVersion: process.env.PLATFORM_VERSION || "8.1",
        udid: process.env.DEVICE_UDID || "ZY323P89SV"
    }
};

module.exports.steps = [...steps];
module.exports.config = config;
module.exports.appPackage = config.desiredCapabilities.appPackage;

const mainConfig= require("./src/features/index");

exports.config = {
  output: './report',
  helpers: {
    Appium: {
      app: mainConfig.config.app,
      hostname: mainConfig.config.host,
      platform: mainConfig.config.platform,
      device: mainConfig.config.device,
      desiredCapabilities: { 
        autoGrantPermissions: true,       
        appPackage: mainConfig.config.desiredCapabilities.appPackage ,
        appActivity: mainConfig.config.desiredCapabilities.appActivity, 
        udid: mainConfig.config.desiredCapabilities.udid,
        //automationName: 'UiAutomator2',
        //skipDeviceInitialization:true,
        //isHeadless:false,
        //newCommandTimeout: 3600,
         }
    },
    Mochawesome: {  
      uniqueScreenshotNames: true
    },
  },
  include: {},
  mocha: {
    reporterOptions:{
      reportDir: "./report/",
      reportTitle: 'AppConsultoras',
      reportFilename:'AppConsultoras',
      autoOpen:true,
      ts:'',
    }
  },
  bootstrap: null,
  teardown: null,
  hooks: [],
  gherkin: {
    features: './src/features/**/*.feature',
    steps: [
             ...mainConfig.steps
           ]
  },
  plugins: {
    allure: {},
    screenshotOnFail: {      
      enabled: true
    }
  },
  tests: './*_test.js',
  name: 'TestAutomation-AppConsultoras'
};

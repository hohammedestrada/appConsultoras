const I = actor();
const utils = require("./../utils/utils");
let wait = { retries: 5, minTimeout: 2000 };
// Add in your custom step files
//let config=utils.fnGetConfig("QAS");
let config = require("./Login.locator");
let locator = config.locator;

module.exports = {
    InicioSesion(Pais, Usuario, Password) {
        I.retry(wait).wait(2);
        I.retry(wait).click(locator.clickIngresar);
        I.retry(wait).click(locator.fieldPaisLogin);
        I.retry(wait).click(utils.fnGetPaisLogin(config, Pais));
        I.retry(wait).fillField(locator.fieldUsuario, Usuario);
        I.retry(wait).click(locator.fieldPassword);
        I.retry(wait).fillField(locator.fieldPassword, Password);
        I.retry(wait).hideDeviceKeyboard();
        I.retry(wait).click(locator.clickIngresar);
        I.wait(10);
    },

    OmitirTutorial() {
        I.retry(wait).click(locator.ClickOmitir);
        I.wait(12);
    },

    OmitirPopUpActualizarDatos() {
        I.retry(wait).click(locator.ClickOmitirPopup);
        I.wait(1);
    },

    CerrarConexionDigitalGratis() {
        I.retry(wait).click(locator.ClickCerrarNavegarGratis);
        I.wait(2);
    },

    Vinculacionconsultora() {
        //let titulo1 = I.grabTextFrom(locator.TxtTituloVinc);
        //if (titulo1 =='VINCULACIÓN COMO CONSULTORA')
        //{
        I.retry(wait).click(locator.ClickAceptaVincula);
        I.wait(2);
        //}
    },

    AceptarTerminosCondicionesuso() {
        I.retry(wait).click(locator.ClickCheck1);
        I.retry(wait).click(locator.ClickCheck2);
        I.retry(wait).click(locator.ClickAceptar);
        I.wait(1);
        I.retry(wait).click(locator.ClickBtnBienvenida);
        I.wait(3);
        //}
    },

    Validaciones(
        Mensaje //Solo para Feature Login.
    ) {
        if (Mensaje == "Null") {
            I.wait(1);
        }
        if (Mensaje != "Null") {
            I.retry(wait).see(Mensaje);
            //this.CapturaEvidencia('LoginEsikaLBEL-',Usuario)
        }
    },

    CapturaEvidencia(Escenario, Usuario) {
        I.retry(wait).saveScreenshot(`Img_${Escenario}_${Usuario}.png`);
        I.addMochawesomeContext(`Img_${Escenario}_${Usuario}.png`);
    }
};

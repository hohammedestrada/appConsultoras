const I = actor();
const utils = require("../utils/utils");
let wait = { retries: 5, minTimeout: 2000 };
// Add in your custom step files
//let config=utils.fnGetConfig("QAS");
let config = require("./OtrosHome.locator");
let locator = config.locator;

module.exports = {
    IngresoBuscador() {
        //I.wait(5);
        I.retry(wait).click(locator.ClickBotonBuscaHome);
        I.wait(2);
    },

    RegresarHome() {
        I.retry(wait).click(locator.ClickRegresaHome);
    },

    RegresarHomeBus() {
        I.retry(wait).click(locator.ClickRegresaBus);
    },

    IngresoPasePedido() {
        //Pase de Pedido

        // Samsung   220 190
        I.wait(2);
        I.retry(wait).touchPerform([
            { action: "tap", options: { x: 1, y: 1 } },
            // {action: 'moveTo' , options: {x: 212, y: 880}},
            { action: "release" } //{action: 'perform'}
        ]);
        //I.retry(wait).click(locator.ClickPasePedido);
        //I.swipeTo(locator.ClickPasePedido);
        I.retry(wait).tap(locator.ClickPasePedido);
        //I.click(locator.ClickPasePedido);
        I.wait(11);

        //element=findElement(By.xpath("//*[@id='ivw_item_imagen' and (./preceding-sibling::* | ./following-sibling::*)[@text='Pedido']]")).click();

        //driver.findElement(By.xpath("//*[@id='rlt_main' and ./parent::*[@id='fltContainer']]")).click();
    },

    IngresoGanaMas() {
        I.retry(wait).click(locator.ClickGanaMas);
        I.wait(11);
    },

    EstrellasPlayStore() {
        I.retry(wait).click(locator.ClickOptDone);
    },

    CerrarPopupCatalogo() {
        I.retry(wait).click(locator.ClickCerrarPopCata);
        I.wait(2);
        //I.refreshpage();
    },

    OpcionMenu() {
        I.retry(wait).click(locator.ClickMenu);
    }
};

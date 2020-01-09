const I = actor();
const utils = require("../../utils/utils");
const login = require("../../pages/Login.module");
const Ppedido = require("../../pages/Ppedido.module");
const OtrosHome = require("../../pages/OtrosHome.module");
let wait = { retries: 5, minTimeout: 2000 };
let config = require("./BuscadorHome.locator");
let locator = config.locator;

module.exports = {
    IngresarDatos(Valor) {
        I.retry(wait).fillField(locator.fieldBuscar, Valor);
    },

    AgregarUnidades(Tipo, Cantidad) {
        if (Tipo == "CUV") {
            Ppedido.AgregarCantidad(Cantidad);
        }
        if (Tipo == "DESC") {
            Ppedido.AgregarCantidadxDesc(Cantidad);
        }
        if (Tipo == "LAND") {
            this.VerMasResultdos();
            I.swipeUp(locator.ClickAgregarProductoxLand);
            Ppedido.AgregarCantidadxDesc(Cantidad);
        }
    },

    AgregarAlPedido(Tipo) {
        if (Tipo == "CUV") {
            I.retry(wait).click(locator.ClickAgregaProducto);
            I.wait(3);
        }
        if (Tipo == "DESC") {
            I.retry(wait).click(locator.ClickAgregarProductoxDesc);
            I.wait(3);
        }
        if (Tipo == "LAND") {
            I.retry(wait).click(locator.ClickAgregarProductoxDesc);
            I.wait(3);
        }
    },

    IrAPedido() {
        OtrosHome.RegresarHomeBus();
        I.wait(10);

        OtrosHome.IngresoPasePedido();
    },

    ValidarProductoAgregado(Valor, Tipo, Usuario) {
        I.wait(10);
        if (Tipo == "CUV") {
            I.retry(wait).see(Valor);
            login.CapturaEvidencia("BuscadorxCUV", Usuario);
        }
        if (Tipo == "DESC") {
        }
        if (Tipo == "LAND") {
        }
    },

    VerMasResultdos() {
        I.retry(wait).click(locator.ClickVerMasResultado);
    }
};

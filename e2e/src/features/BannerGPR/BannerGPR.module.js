const I = actor();
let wait = { retries: 5, minTimeout: 2000 };

let config = require("./BannerGPR.locator");
let locator = config.locator;

module.exports = {
    VerBannerGPR(GPR, Pedido) {
        if (GPR == "Si") {
            if (Pedido == "Rechazado") {
                I.seeElement(locator.RTextBannerGPR);
                I.retry(wait).click(locator.ClickVerDetalle);
                I.wait(2);
            }
            if (Pedido == "Facturado") {
                I.seeElement(locator.FTextBannerGPR);
                I.wait(2);
            }
        }
        if (GPR == "No") {
            //Pedido Facturado
            I.dontSeeElement(locator.FTextBannerGPR);
            I.dontSeeElement(locator.RTextBannerGPR);
            I.wait(2);
        }
    },

    ValidacionBanner(validaciones, Usuario) {
        /*
          0 : Facturando Pedido
          1 : Monto Máximo
          2 : Monto Mínimo
          3 : Deuda
          4 : Desviacion (Monto Permitido)   
        */

        if (validaciones == "0") {
            I.addMochawesomeContext({
                title: "....Resultado....",
                value: "Facturando Pedido"
            });
        }

        if (validaciones == "1") {
            I.retry(wait).see("línea de crédito");
            I.retry(wait).dontSee("deuda");
            I.addMochawesomeContext({
                title: "....Resultado....",
                value: "Monto Maximo"
            });
        }

        if (validaciones == "2") {
            I.retry(wait).see("monto mínimo");
            I.retry(wait).dontSee("deuda");
            I.addMochawesomeContext({
                title: "....Resultado....",
                value: "Monto Minimo"
            });
        }

        if (validaciones == "3") {
            I.retry(wait).see("una deuda de");
            I.retry(wait).dontSee("línea de crédito");
            I.retry(wait).dontSee("monto mínimo");
            I.retry(wait).dontSee("monto permitido");
            I.addMochawesomeContext({
                title: "....Resultado....",
                value: "Deuda"
            });
        }

        if (validaciones == "4") {
            I.retry(wait).see("monto permitido");
            I.retry(wait).dontSee("una deuda de");
            I.addMochawesomeContext({
                title: "....Resultado....",
                value: "Monto Permitido"
            });
        }

        if (validaciones == "31") {
            I.retry(wait).see("una deuda de");
            I.retry(wait).see("línea de crédito");
            I.addMochawesomeContext({
                title: "....Resultado....",
                value: "Deuda con Monto Maximo"
            });
        }

        if (validaciones == "32") {
            I.retry(wait).see("una deuda de");
            I.retry(wait).see("monto mínimo");
            I.addMochawesomeContext({
                title: "....Resultado....",
                value: "Deuda con Monto Minimo"
            });
        }

        if (validaciones == "34") {
            I.retry(wait).see("una deuda de");
            I.retry(wait).see("monto permitido");
            I.addMochawesomeContext({
                title: "....Resultado....",
                value: "Deuda con Monto Permitido"
            });
        }
    }
};

const I = actor();
const utils = require("../utils/utils");
const login = require("../pages/Login.module");
let wait = { retries: 5, minTimeout: 2000 };
let config = require("./Ppedido.locator");
let locator = config.locator;

module.exports = {
    IngresoPedido2() {
        //Pase de Pedido
        // Samsung   220 190
        I.wait(2);
        I.retry(wait).touchPerform([
            {
                action: "tap",
                options: { x: 216, y: utils.fnCoordenadaY(0, 216) }
            },
            // {action: 'moveTo' , options: {x: 212, y: 880}},
            { action: "release" } //{action: 'perform'}
        ]);
        I.wait(15);
    },

    AgregarProducto(Producto, Cantidad) {
        if (Producto != "Null" && Cantidad != "0") {
            I.retry(wait).click(locator.ClickCampoProducto);
            I.retry(wait).fillField(locator.fieldProducto, Producto);
            //I.wait(3);
            if (Cantidad != 1) {
                I.retry(wait).fillField(locator.fieldCantidad, Cantidad);
            }
            I.retry(wait).click(locator.clickAgregar);
            I.wait(10);
        }
    },

    ModificaCantidadProducto(Producto, Cantidad) {
        //var nroProductosP=1 //I.grabValueFrom(locator.ContadorProductoPedido)

        //if (nroProductosP==1)
        //{
        //this.DesbloquearAccionPase()
        I.wait(2);
        I.retry(wait).click(locator.ClickBotonCantidad);
        I.wait(8);
        this.AgregarCantidad(Cantidad);
        I.wait(4);
        I.retry(wait).click(locator.ClickModificar);
        I.wait(4);
        //Ppedido.DesbloquearAccionPase()
    },

    EliminarProductoPedido() {
        var nroProductos = 1; //I.grabValueFrom(locator.ContadorProductoPedido)
        //console.log(nroProductos);
        if (nroProductos == 1) {
            I.retry(wait).click(locator.ClickEliminar);
            I.retry(wait).click(locator.ClickConfirmaSi);
        }
        if (nroProductos != 1) {
            //this.Buscar(Producto,nroProductos);
        }
    },

    DesbloquearAccionPase() {
        I.wait(2);

        I.retry(wait).touchPerform([
            { action: "tap", options: { x: 100, y: 219 } }, //66,289
            // {action: 'moveTo' , options: {x: 212, y: 86680}},
            { action: "release" } //{action: 'perform'}
        ]);
        I.wait(2);

        I.retry(wait).touchPerform([
            { action: "tap", options: { x: 59, y: 103 } },
            // {action: 'moveTo' , options: {x: 212, y: 880}},
            { action: "release" } //{action: 'perform'}
        ]);
    },

    GuardarReservarPedido() {
        I.retry(wait).click(locator.ClickBotonGR);
        I.wait(12);
    },

    ValidarReserva(Reserva, Usuario) {
        if (Reserva == "0") {
            I.retry(wait).see("Pedido guardado.");
            login.CapturaEvidencia("P-Guardado", Usuario);
            //I.wait(2);
        }
        if (Reserva == "1") {
            I.retry(wait).see("¡Reservaste tu pedido con éxito!");
            login.CapturaEvidencia("P-Reservado", Usuario);
        }
        if (Reserva == "1.5") {
            I.retry(wait).see("PEDIDO RESERVADO");
            login.CapturaEvidencia("P-Reservado", Usuario);
        }
        if (Reserva == "2") {
            //I.retry(wait).see("CON ÉXITO");
            login.CapturaEvidencia("P-Guardado", Usuario);
            //I.retry(wait).click(locator.ClickOFTerminaPedido);
            I.wait(2); //2
        }
        if (Reserva == "3") {
            //Validación Interactiva Pedido No reservado/Guardado
            I.retry(wait).see("NO SE PUDO RESERVAR PEDIDO");
            login.CapturaEvidencia("P-Rechazado-", Usuario);
        }
        if (Reserva == "5") {
            I.retry(wait).see("Todavía no has agregado productos");
            login.CapturaEvidencia("P-Eliminado", Usuario);
        }
        //
    },

    //Modificar la Cantidad del Producto
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

    Regalo(Flag) {
        if (Flag == "Si") {
            I.retry(wait).click(locator.ClickEscogerRegalo);
            I.wait(5);
            I.retry(wait).click(locator.ClickSigueComprando);
            I.wait(5);
        }

        if (Flag == "No") {
            this.DesbloquearAccionPase();
        }
    },

    VerificarBoton(boton) {
        //let BotonR =

        //console.log(BotonR)

        //if (I.grabTextFrom(locator.ClickBotonGR)=="Modificar")
        if (boton == "Si") {
            I.retry(wait).click(locator.ClickBotonGR);
            I.wait(8);
        }
    },

    ActivarCheckMulti(FlagM) {
        if (FlagM == "Si") {
            //Ppedido.DesbloquearAccionPase();
            I.swipeUp(locator.ClickSwitchMultipedido);
            I.retry(wait).checkOption(locator.ClickSwitchMultipedido);
            I.wait(5);
        }
    }
};

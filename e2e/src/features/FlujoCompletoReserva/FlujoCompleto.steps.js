//const modulo = require('./ModPasePedido.module');
const login = require("../../pages/Login.module");
const Ppedido = require("../../pages/Ppedido.module");
const OtrosHome = require("../../pages/OtrosHome.module");
//const MCantidadP = require('../../pages/MCantidadP.module');

Given(
    "Ingreso para realizar un pedido con el {string} y {string} del pais {string}",
    async function(Usuario, Password, Pais) {
        this.Usuario = Usuario;
        login.InicioSesion(Pais, Usuario, Password);
        login.OmitirTutorial();
        //OtrosHome.CerrarPopupCatalogo();
        OtrosHome.IngresoPasePedido();
    }
);

When(
    "Se ingresa el {string} y la {string} y agrego el producto",
    async function(Producto, Cantidad) {
        this.Producto = Producto;
        this.Cantidad = Cantidad;
        Ppedido.AgregarProducto(this.Producto, this.Cantidad);
    }
);

Given("Se modifica el producto con la cantidad {string}", async function(
    Adicional
) {
    Ppedido.DesbloquearAccionPase();
    Ppedido.ModificaCantidadProducto(this.Producto, Adicional);
});

Given("Se elimina el producto del pase de pedido", async function() {
    Ppedido.DesbloquearAccionPase();
    Ppedido.EliminarProductoPedido();
});

Given("Se termina el flujo del Pase de Pedido", async function() {
    Ppedido.ValidarReserva(5, this.Usuario);
});

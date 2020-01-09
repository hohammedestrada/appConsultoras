const modulo = require("./Multipedido.module");
const login = require("../../pages/Login.module");
const Ppedido = require("../../pages/Ppedido.module");
const OtrosHome = require("../../pages/OtrosHome.module");

Given(
    "Ingreso al pase de pedido con el {string} y {string} del pais {string}",
    async function(Usuario, Password, Pais) {
        this.Usuario = Usuario;
        this.Password = Password;
        this.Pais = Pais;
        login.InicioSesion(this.Pais, this.Usuario, this.Password);
        //if (this.Pais!='ElSalvador')
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

Given("{string} activo el botón para facturar hoy", async function(FlagM) {
    this.FlagM = FlagM;
    Ppedido.ActivarCheckMulti(this.FlagM);
});

Given("{string} se reserva el pedido", async function(Freserva) {
    this.Freserva = Freserva;
    modulo.AceptarReserva(this.Freserva);
});

Then("Se termina con la reserva del pedido", async function() {
    login.CapturaEvidencia("Multipedido-", Usuario);
});

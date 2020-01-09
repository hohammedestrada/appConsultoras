const modulo = require("./ModPasePedido.module");
const login = require("../../pages/Login.module");
const Ppedido = require("../../pages/Ppedido.module");
const OtrosHome = require("../../pages/OtrosHome.module");

Given(
    "Con el {string} y {string} del pais {string} ,ingreso al pase de pedido",
    async function(Usuario, Password, Pais) {
        this.Usuario = Usuario;
        this.Password = Password;
        this.Pais = Pais;
        login.InicioSesion(this.Pais, this.Usuario, this.Password);
        OtrosHome.IngresoPasePedido();
    }
);

When("El Boton dice {string} entonces", async function(Boton) {
    this.Boton = Boton;
    Ppedido.VerificarBoton(this.Boton);
});

Given(
    "depende de la operacion {string}, se toma el {string} y la {string}",
    async function(Operacion, Producto, Cantidad) {
        this.Operacion = Operacion;
        this.Producto = Producto;
        this.Cantidad = Cantidad;
        modulo.EjecutarOperacion(Operacion, Producto, Cantidad);
    }
);

Given(
    'Terminada la operacion, "se guarda o reserva" el pedido',
    async function() {
        Ppedido.DesbloquearAccionPase();
        Ppedido.GuardarReservarPedido();
    }
);

Then("Segun la operacion se termina el pedido {string}", async function(
    Reserva
) {
    this.Reserva = Reserva;
    Ppedido.ValidarReserva(this.Reserva, this.Usuario);
});

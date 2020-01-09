//const modulo = require('./PasePedido.module');
const login = require("../../pages/Login.module");
const Ppedido = require("../../pages/Ppedido.module");
const OtrosHome = require("../../pages/OtrosHome.module");

//this.Configuracion=Configuracion;
Given(
    "Con el {string} y {string} del pais {string} ,ingreso al pase de pedido",
    async function(Usuario, Password, Pais) {
        this.Usuario = Usuario;
        this.Password = Password;
        this.Pais = Pais;
        //this.Configuracion=Configuracion;
        login.InicioSesion(this.Pais, this.Usuario, this.Password);
        //if (this.Pais!='ElSalvador')
        //{
        login.OmitirTutorial();
        //}
        OtrosHome.IngresoPasePedido();
        //modulo.AsignarConfiguracion(this.Configuracion)
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

Given("Si la consultora {string} tiene Regalo", async function(Flag) {
    this.Flag = Flag;
    Ppedido.Regalo(Flag);
});

Given("Se envia a Reservar o Guardar el pedido", async function() {
    //this.Pais=Pais;
    Ppedido.GuardarReservarPedido();
});

Then("Se {string} el Pedido", async function(Reserva) {
    this.Reserva = Reserva;
    Ppedido.ValidarReserva(this.Reserva, this.Usuario);
    //login.CapturaEvidencia('PasePedido',Usuario)
});

const modulo = require("./BuscadorHome.module");
const login = require("../../pages/Login.module");
const OtrosHome = require("../../pages/OtrosHome.module");

Given(
    "Con el {string} y {string} del pais {string}, ingreso al buscador",
    async function(Usuario, Password, Pais) {
        this.Usuario = Usuario;
        this.Password = Password;
        this.Pais = Pais;
        login.InicioSesion(this.Pais, this.Usuario, this.Password);
        login.OmitirTutorial();
        OtrosHome.IngresoBuscador();
    }
);

When("Ingreso el {string}", async function(Valor) {
    this.Valor = Valor;
    modulo.IngresarDatos(Valor);
});

Given(
    "Se busca por {string} y agrego la {string} para agregar al pedido",
    async function(Tipo, Cantidad) {
        this.Tipo = Tipo;
        this.Cantidad = Cantidad;
        modulo.AgregarUnidades(Tipo, Cantidad);
    }
);

Given('Terminada la operacion hago clic en "Agregar"', async function() {
    modulo.AgregarAlPedido(this.Tipo);
});

Given("Regreso al Home e ingreso al pase de pedido", async function() {
    modulo.IrAPedido();
});

Then("Se muestra el producto agregado desde el buscador", async function() {
    modulo.ValidarProductoAgregado(this.Valor, this.Tipo, this.Usuario);
});

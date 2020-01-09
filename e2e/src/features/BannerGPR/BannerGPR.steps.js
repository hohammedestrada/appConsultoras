const modulo = require("./BannerGPR.module");
const login = require("../../pages/Login.module");

Given(
    'Con el {string} ingreso {string}, {string}, click en el boton "INICIAR SESION"',
    async function(Pais, Usuario, Password) {
        this.Pais = Pais;
        this.Usuario = Usuario;
        this.Password = Password;
        login.InicioSesion(this.Pais, this.Usuario, this.Password);
    }
);

When("Ingreso al APP de Consultoras", async function() {
    login.Validaciones("Null");
});

Then("{string} se muestra el Banner de Pedido {string}", async function(
    GPR,
    Pedido
) {
    this.GPR = GPR;
    this.Pedido = Pedido;
    modulo.VerBannerGPR(this.GPR, this.Pedido, this.Usuario);
});

Then("con las validaciones {string}", async function(validaciones) {
    this.validaciones = validaciones;
    modulo.ValidacionBanner(this.validaciones, this.Usuario);
});

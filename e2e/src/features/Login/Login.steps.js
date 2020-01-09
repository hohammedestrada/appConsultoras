const I = actor();
const OtrosHome = require("../../pages/OtrosHome.module");
const login = require("../../pages/Login.module");
let wait = { retries: 10, minTimeout: 3000 };

// Add in your custom step files

//he seleccionado el "<Pais>" e ingresado la "<Usuario>" y "<Password>"
//he seleccionado el "<Pais>" e ingresado la "<Region>" o "<Zona>" o "<Seccion>" y el "<Password>"
//Given('he seleccionado el {string} e ingresado la {string} o {string} o {string} y el {string}', async function (Pais,Region,Zona,Seccion,Password) {
Given(
    "He seleccionado el {string} ingreso el {string} y {string}",
    async function(Pais, Usuario, Password) {
        this.Pais = Pais;
        this.Usuario = Usuario;
        this.Password = Password;
    }
);
When('click en el boton "INICIAR SESION"', async function() {
    login.InicioSesion(this.Pais, this.Usuario, this.Password);
});

//Then('puedo ver mi indicador de VENTA NETA, PEDIDOS Y RUTAS DE DESARROLLO', function () {
// login.Validaciones();
//});

Then(
    "Si los datos son correctos Ingreso al APP de Consultoras, sino muestra el {string}",
    async function(Mensaje) {
        this.Mensaje = Mensaje;
        //login.OmitirPopUpActualizarDatos();
        login.OmitirTutorial();
        login.Validaciones(this.Mensaje);
        //OtrosHome.CerrarPopupCatalogo()
        //login.AceptarTerminosCondicionesuso()
        login.CapturaEvidencia("Login-", this.Usuario);
    }
);

package biz.belcorp.consultoras;

import android.os.Build;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import biz.belcorp.consultoras.common.model.country.CountryModel;
import biz.belcorp.consultoras.feature.auth.login.LoginActivity;
import biz.belcorp.consultoras.feature.home.tutorial.TutorialActivity;
import biz.belcorp.consultoras.feature.splash.SplashActivity;

import static biz.belcorp.consultoras.util.Util.sleep;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NavigationTest extends BaseTest {

    @Rule
    public ActivityTestRule<SplashActivity> loginActivity = new IntentsTestRule<>(SplashActivity.class);

    @Before
    public void setUp() {
        screenshot("Starting App");

        if (Build.VERSION.SDK_INT < 23)
            sleep(TIMEOUT_ASYNC);
    }

    @After
    public void finish() {
        screenshot("Stopping App");

        if (Build.VERSION.SDK_INT < 23)
            sleep(TIMEOUT_ASYNC);

        loginActivity.finishActivity();
    }

    @Test
    public void test100Login() {
        boolean isNotLogged = isActivity(LoginActivity.class);

        if (!isNotLogged) {
            screenshot("Ya esta logeado");
            return;
        }

        screenshot("Login form");
        click(R.id.btn_login);

        screenshot("Login con datos");
        click(R.id.rlt_country);
        selectedCountry(CountryModel.class);
        screenshot("País seleccionado");
        setValue(R.id.tie_username, USERNAME);
        screenshot("Usuario ingresado");
        setValue(R.id.tie_password, PASSWORD);
        screenshot("Password ingresado");
        click(R.id.btn_login);
        screenshot("Login Correcto");
    }

    @Test
    public void test200Tutorial() {
        boolean isTutorial = isActivity(TutorialActivity.class);

        if (isTutorial) {
            click(R.id.btn_tutorial_omitir);
        }

        screenshot("Tutorial Omitido");
    }

    @Test
    public void test300Home() {
        matchVisible(R.id.fltContainer);
        checkAnimation();
    }

    @Test
    public void test400GoToClientes() {
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
    }

    @Test
    public void test401GoToOfertas() {
        matchVisible(R.id.fltContainer);
        goToPage(2);
        screenshot("Pagína de Ofertas");
    }

    @Test
    public void test402GoToPedidos() {
        matchVisible(R.id.fltContainer);
        goToPage(3);
        screenshot("Pagína de Pedidos");

        webClickById("txtClienteNombre");
        sleep(TIMEOUT_WEB);
        screenshot("Buscar cliente");

        execScript("document.querySelectorAll('.ui-menu-items')[1].click()");
        sleep(TIMEOUT_WEB);
        screenshot("Cliente seleccionado");

        execScript("document.getElementById('txtCodigoProducto').value = '" +
            PRODUCTO_CODE + "'; " +
            "$('#txtCodigoProducto').trigger('keyup')");
        sleep(TIMEOUT_WEB);
        screenshot("Producto ingresado");

        webClickById("btnAgregarProducto");
        sleep(TIMEOUT_WEB);
        screenshot("Agregar producto");

        webClickByClassName("btn_verMiPedido");
        sleep(TIMEOUT_ASYNC);
        screenshot("Ver mi pedido");

        execScript("document.querySelector('.icono_eliminar_item_pedido > a').click()");
        sleep(TIMEOUT_WEB);
        screenshot("Remover producto");

        execScript("ConfirmaEliminarPedido();");
        sleep(TIMEOUT_ASYNC);
        screenshot("Confirmar eliminación de producto");

        goToBack();

        screenshot("Regresar al pase de pedidos");

        goToBack();

        screenshot("Regresar al home");
    }

    @Test
    public void test403GoToBonificacion() {
        matchVisible(R.id.fltContainer);
        goToPage(4);
        screenshot("Pagína de Bonificación");
    }

    @Test
    public void test500GoToCatalogos() {
        matchVisible(R.id.fltContainer);
        goToMenu(1);
        screenshot("Menu de Catalogos");
        goToBack();
    }

    @Test
    public void test501GoToProductosAgotados() {
        matchVisible(R.id.fltContainer);
        goToMenu(2);
        screenshot("Menu de Productos agotados");
        goToBack();
    }

    @Test
    public void test502GoToSeguimientoPedido() {
        matchVisible(R.id.fltContainer);
        goToMenu(3);
        screenshot("Menu de Seguimiento Pedido");
        goToBack();
    }

    //Pruebas Automatizadas - Parte 1

    @Test
    public void test503ClienteRegistro() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_name, "JUnit");
        setValue(R.id.ted_last, "Test");
        setValue(R.id.ted_mobile, "997466222");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test503ClienteTelefonoRepetido() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_name, "Unit");
        setValue(R.id.ted_last, "Test");
        setValue(R.id.ted_mobile, "997466222");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test503ClienteSinTelefonos() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_name, "JUnit");
        setValue(R.id.ted_last, "Test");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test503ClienteEmailInvalido() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_name, "JUnit");
        setValue(R.id.ted_last, "Test");
        setValue(R.id.ted_mobile, "997466999");
        click(R.id.llt_agregar_correo);
        setValue(R.id.ted_email, "test");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test503ClienteEliminar() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        longClick(R.id.cvw_item);
        screenshot("Eliminar Cliente");
        click(R.id.tvw_tooltip_eliminar);
        sleep(TIMEOUT_WEB);
        click(R.id.btn_aceptar, "Aceptar");
        screenshot("Aceptar");
        sleep(TIMEOUT_ASYNC);
    }

    //Pruebas Automatizadas - Parte 2

    @Test
    public void test503ClienteConNombreCelular() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_name, "Mateo");
        setValue(R.id.ted_mobile, "947466999");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test503ClienteSinNombreCelularValido() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_mobile, "937466999");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test503ClienteNombreRepetidoApellidoValidoCelularValido() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_name, "Mateo");
        setValue(R.id.ted_last, "Smart");
        setValue(R.id.ted_mobile, "967466999");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test503ClienteNombreRepetidoSinApellidoCelularValido() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_name, "Mateo");
        setValue(R.id.ted_mobile, "987466999");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test503ClienteNombreRepetidoApellidoRepetidoCelularValido() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_name, "Mateo");
        setValue(R.id.ted_last, "Smart");
        setValue(R.id.ted_mobile, "997166999");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test503ClienteNombreValidoApellidoRepetidoCelularValido() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_name, "Lorena");
        setValue(R.id.ted_last, "Smart");
        setValue(R.id.ted_mobile, "997462999");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test503ClienteCelularInvalido() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_mobile, "123");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test503ClienteNombreTelefonoFijo() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_name, "JUnit");
        setValue(R.id.ted_phone, "4700590");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test503ClienteTelefonoFijoRepetido() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_phone, "4700590");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test503ClienteTelefonoFijoInvalido() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_phone, "123");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test503ClienteNombre10Caracteres() {
        sleep(TIMEOUT_ASYNC * 3);
        matchVisible(R.id.fltContainer);
        goToPage(1);
        screenshot("Pagína de Clientes");
        sleep(TIMEOUT_ASYNC);
        click(R.id.fab_add_client);
        click(R.id.tvw_fab_option_2);
        screenshot("Registro de Clientes");
        setValue(R.id.ted_name, "Abcdefghijk");
        click(R.id.llt_toolbar_option_2);
        sleep(TIMEOUT_ASYNC);
    }

    @Test
    public void test600Logout() {
        matchVisible(R.id.fltContainer);
        click(R.id.fltMenu);
        screenshot("Seleccionar menú");
        click(R.id.tvwCerrarSesion);
        screenshot("Cerrar sesíon");
        click(R.id.btn_aceptar, "CERRAR SESIÓN");
        screenshot("Aceptar");
    }
}

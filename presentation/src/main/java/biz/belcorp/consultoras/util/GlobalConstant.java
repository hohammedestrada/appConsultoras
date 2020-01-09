package biz.belcorp.consultoras.util;

import org.jetbrains.annotations.NotNull;

import biz.belcorp.consultoras.BuildConfig;

/**
 *
 */

public class GlobalConstant {


    public static final String CODE_OK = "0000";
    public static final String OK = "OK";
    @NotNull
    public static final String ELEGIR_NAVEGADOR = "Recomendamos que uses Chrome";

    private GlobalConstant() {
    }

    public static final int COLUMN_COUNT_GALLERY = 2;
    public static final String POSITION_CURRENT_ITEM_GALLERY = "position_curent_gallery_item";
    public static final String ITEMS_GALLERY = "list_gallery_item";
    public static final String ITEM_SELECTED_GALLERY = "selected_gallery_item";
    public static final String ZERO_STRING = "0";
    public static final String SPACE = " ";
    public static final String TABULAR_HOME = "| Home";
    public static final String TABULAR_MENU_LATERAL = "| Menú Lateral";
    public static final String TABULAR_MIS_PEDIDOS = "| Mis pedidos";
    public static final String TABULAR_CLIENTES_DETALLE_DEUDA = "| Clientes | Detalle deuda";
    public static final String PEDIDIO_APROBAR = "Tiene 1 pedido por aprobar";
    //PopUpDuoPerfecto
    public static final String POPUP = "POPUP";
    public static final String DUOPERFECTO = "DUOPERFECTO";
    // App constants
    public static final String APP_CODE = "APPCONS";
    public static final String SO = "Android";
    public static final String APP_ROL_ID = "4";
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider.sync";
    public static final String PARAM_TUTORIAL_REQUEST = "TUTORIAL_APP";
    public static final String PARAM_TUTORIAL_BUNDLE = "TUTORIAL_APP_BUNDLE";
    public static final String PARAMTUTORIAL_START = "TUTORIAL_BASE";

    public static final String PROVIDER_FILE = BuildConfig.APPLICATION_ID + ".provider.file";

    public static final String WONT_IMPLEMENT = "No se implementará";

    public static final String LOGIN_STATE = "bundle_login_state";

    public static final String FROM_CLIENT_CARD = "fromClientCard";
    public static final String FROM_MODIFICAR = "fromModificar";
    public static final String TITLE = "TITLE";
    public static final String NO_CONTEXT = "No hay contexto";


    // MQVirtual Permission

    public static final String PERMISSION_MQVIRTUAL = BuildConfig.APP_MQVIRTUAL_PACKAGE + ".provider.data.READ_DATABASE";

    //Fonts source constants

    public static final String LATO_BOLD_SOURCE = "fonts/lato-bold.ttf";
    public static final String LATO_REGULAR_SOURCE = "fonts/lato-regular.ttf";
    public static final String LATO_LIGHT_SOURCE = "fonts/lato-light.ttf";

    //Flags source constants
    public static final String FLAG_SOURCE = "file:///android_asset/images/flags/";

    public static final String TODO_TAG_GALLERY = "TODO";
    public static final String BUNDLE_PAGES_GALLERY = "PAGES";
    public static final String BUNDLE_TAB_NAME = "bundle_gallery_tab_title";
    //Id del cliente
    public static final String FROM_REMINDER = "FromReminder";
    public static final String CLIENTE_ID = "ClientID";
    public static final String CLIENTE_LOCAL_ID = "clienteLocalID";
    public static final String CLIENT_NAME = "ClientName";
    public static final String CLIENT_NAME_2 = "ClientName2";
    public static final String CLIENT_TOTAL_DEBT = "CLIENTE_TOTAL_DEBT";
    public static final String CLIENT_NEW_TOTAL_DEBT = "CLIENT_NEW_TOTAL_DEBT";
    public static final String CLIENT_PAYMENT = "CLIENT_PAYMENT";
    public static final String CLIENT_LOCAL_ID_RECORDATORY = "CLIENT_LOCAL_ID_RECORDATORY";
    public static final String CLIENT_ID_RECORDATORY = "CLIENT_ID_RECORDATORY";
    public static final String CLIENT_DATE_RECORDATORY = "CLIENT_DATE_RECORDATORY";
    public static final String CLIENT_MESSAGE_RECORDATORY = "CLIENT_MESSAGE_RECORDATORY";
    public static final String CLIENT_PEDIDO = "CLIENT_PEDIDO";
    public static final String MONEY_SYMBOL = "MONEY_SYMBOL";
    public static final String MARCA_ID = "MARCA_ID";

    //Id del cliente
    public static final String REMINDER_POSITION = "ReminderPosition";
    public static final String TRANSACTION = "Transaction";
    public static final String MOVEMENT_TYPE = "MOVEMENT_TYPE";
    public static final String TRANSACTION_AMOUNT = "Transaction";

    // INCENTIVOS - PROGRAMA DE NUEVAS
    public static final int WEB_ORDER_ORIGIN = 4741;

    // Bundle
    public static final String CUPON_KEY = "key_cupon";
    public static final String CATALOGOS = "bundle_catalogos";

    // Secrets
    public static final String SECRET_PASE_PEDIDO = "kjsfg!)=)4diof25sfdg302dfg57438)!#$#70dfgf234asdnan";

    // URLs
    public static final String URL_PASE_PEDIDO = BuildConfig.PASE_PEDIDO;
    public static final String URL_ACADEMIA = BuildConfig.WP_ACADEMIA;
    public static final String URL_TUVOZ_ONLINE = BuildConfig.WP_TUVOZ;
    public static final String URL_CAMINO = BuildConfig.URL_CAMINO_ALEXITO;
    public static final String URL_CHATBOT = BuildConfig.URL_CHATBOT;
    public static final String URL_CATALOGO = "URL_CATALOGO";

    // Cupon Constants
    public static final String ALIAS_KEY = "bundle_alias";
    public static final String CUPON_TIPO_COND = "bundle_tipo";
    public static final String CUPON_DSCO_KEY = "bundle_dsco";
    public static final String CUPON_MONTO_KEY = "bundle_monto";
    public static final String CUPON_SYMBOL = "bundle_symbol";
    public static final String COUNTRY_KEY = "bundle_country";

    // Bundle Constants
    public static final String DATE_KEY = "bundle_date";
    public static final String CAMPAIGN_KEY = "bundle_campaign";
    public static final String NUM_CAMPAIGN_KEY = "bundle_num_campaign";
    public static final String CAMPAIGN_FULL_KEY = "bundle_campaign_full";
    public static final String BOOK_BY_CAMPAIGN_KEY = "bundle_book_by_campaign";
    public static final String BOOK_LIST_KEY = "bundle_book_list";

    public static final String REVISTA_SUSCRIPCION_KEY = "bundle_revista_suscripcion";
    public static final String REVISTA_CODE = "bundle_code_revista";
    public static final String REVISTA_GND = "bundle_gnd";
    public static final String REVISTA_TITLE = "bundle_revista_title";
    public static final String REVISTA_USER_ES_TOP_BRILLA = "bundle_user_top_brilla";

    public static final String TRACKING_KEY = "bundle_tracking_key";
    public static final String TRACKING_TOP = "bundle_tracking_top";

    public static final String CONTEST_BRIGHT_PATH_KEY = "bundle_bright_path_incentives_contest";
    public static final String CONTEST_BRIGHT_PATH_LEVELS_KEY = "bundle_bright_path_levels_incentives_contest";
    public static final String CONTEST_NEW_PROGRAM_KEY = "bundle_new_program_incentives_contest";
    public static final String CONTEST_NEW_PROGRAM_GIFT_KEY = "bundle_new_program_incentives_contest_gift";
    public static final String CONTEST_NEW_PROGRAM_CUPON_KEY = "bundle_new_program_incentives_contest_cupon";
    public static final String CURRENT_CONSTANCIA_KEY = "bundle_constancia_incentives_contest";
    public static final String CURRENT_CONTEST_KEY = "bundle_current_incentives_contest";
    public static final String PREVIOUS_CONTEST_KEY = "bundle_incentives_contest";
    public static final String CURRENCY_SYMBOL_KEY = "bundle_currency_symbol";
    public static final String CODE_CONCURSO = "bundle_code_concurso";
    public static final String CURRENT_CAMPAIGN = "bundle_current_campaign";
    public static final String CODE_CAMPAIGN = "bundle_current_code_campaign";

    public static final String WINONCLICK_VIDEO_URL = "bundle_win_on_click_video_url";
    public static final String WINONCLICK_VIDEO_TYPE = "bundle_win_on_click_video_type";
    public static final String WINONCLICK_USER_PARAM = "bundle_win_on_click_user";

    // Analytics
    public static final String ROL_PARTNER = "Socia";
    public static final String ROL_CONSULTANT = "Consultora";

    public static final String PALANCA = "palanca";
    public static final String PALANCADEFAULT = "palancadefault";
    public static final String ITEMS = "items";
    public static final String PROMOTIONS = "promotions";

    public static final String BRAND_FOCUS_NAME = Constant.BRAND_FOCUS_NAME;
    public static final String LIST_OFFERS = Constant.BRAND_FOCUS_NAME + " | Ofertas";
    public static final String LIST_FINAL_OFFERS = Constant.BRAND_FOCUS_NAME + " | Oferta Final";

    public static final String TRACK_VAR_SCREEN = "pantalla";
    public static final String TRACK_VAR_SCREEN_VIEW = "pantallaVista";
    public static final String TRACK_VAR_CAMPAING = "campana";
    public static final String TRACK_VAR_COUNTRY = "pais";
    public static final String TRACK_VAR_REGION = "region";
    public static final String TRACK_VAR_ZONA = "zona";
    public static final String TRACK_VAR_CONSULTANT_CODE = "codigoconsultora";
    public static final String TRACK_VAR_USER_TYPE = "userType";
    public static final String TRACK_VAR_ROL = "rol";
    public static final String TRACK_VAR_PERIOD = "periodo";
    public static final String TRACK_VAR_PERIOD_WEEK = "semanaperiodo";
    public static final String TRACK_VAR_RANKING = "ranking";
    public static final String TRACK_VAR_PARTNER_LEVEL = "socianivel";
    public static final String TRACK_VAR_ENVIROMENT = "ambiente";
    public static final String TRACK_VAR_TIPO_INGRESO = "tipoIngreso";
    public static final String TRACK_VAR_NOMBRE_PUSH = "nombrePushNotification";
    public static final String TRACK_VAR_SEGMENTO_PATROCINADO = "segmento_patrocinado";
    public static final String TRACK_VAR_SECCION = "seccion";
    public static final String TRACK_VAR_SEGMENTO_CONSTANCIA = "segmentoConstancia";
    public static final String TRACK_VAR_INSCRIPCION_EPM = "inscripcionEPM";
    public static final String TRACK_VAR_GRUPO_CONSULTORA = "grupoConsultora";

    public static final String EVENT_VAR_CATEGORY = "category";
    public static final String EVENT_VAR_ACTION = "action";
    public static final String EVENT_VAR_LABEL = "label";
    public static final String EVENT_VAR_SCREEN = "screen_name";
    public static final String EVENT_VAR_NAME = "event_name";

    public static final String EVENT_VIEW_MORE = "view_more";
    public static final String SCREEN_VIEW = "screen_views";

    public static final String SCREEN_LOGIN = Constant.BRAND_FOCUS_NAME + " | Login";
    public static final String SCREEN_LOGIN_NORMAL = Constant.BRAND_FOCUS_NAME + " | Ingresar con tu usuario";
    public static final String SCREEN_LOGIN_FB = Constant.BRAND_FOCUS_NAME + " | Ingresar con facebook";

    public static final String SCREEN_HOME = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_HOME;
    public static final String SCREEN_PEDIDO = Constant.BRAND_FOCUS_NAME + " | Pedido";
    public static final String SCREEN_INGRESAR_PEDIDO = Constant.BRAND_FOCUS_NAME + " | Ingresar Pedido";
    public static final String EVENT_CLICK_BUTTON = "Click Botón";
    public static final String EVENT_INGRESAR_PEDIDO_POP_UP = Constant.BRAND_FOCUS_NAME + " | Ingresar pedido - pop up Elige tu Regalo";
    public static final String EVENT_INGRESAR_PEDIDO_FELICIDADES = Constant.BRAND_FOCUS_NAME + " | Ingresar pedido - pop up Felicidades";

    public static final String EVENT_CATEGORY_CLIENTES = Constant.BRAND_FOCUS_NAME + " | Clientes";
    public static final String EVENT_CATEGORY_DEUDAS = Constant.BRAND_FOCUS_NAME + " | Deudas";
    public static final String EVENT_CATEGORY_DETALLE_DEUDA = Constant.BRAND_FOCUS_NAME + " | Detalle deuda";
    public static final String EVENT_CATEGORY_PEDIDO = Constant.BRAND_FOCUS_NAME + " | Pedido";

    public static final String EVENT_ACTION_VER_CLIENTE = "Ver Cliente";
    public static final String EVENT_ACTION_MENU_SECUNDARIO = "Menú secundario";
    public static final String EVENT_ACTION_IMPORTAR_CLIENTES = "Importar clientes";
    public static final String EVENT_ACTION_AGREGAR_CLIENTE = "Agregar cliente";
    public static final String EVENT_ACTION_FICHA = "Ficha";
    public static final String EVENT_ACTION_CLICK_BOTON = "Click Botón";
    public static final String EVENT_ACTION_VER_DETALLE = "Ver detalle";
    public static final String EVENT_ACTION_MOVIMIENTO_CLIENTE = "Movimiento cliente";
    public static final String EVENT_ACTION_AGREGAR_DEUDA = "Agregar deuda";
    public static final String EVENT_ACTION_REGISTRAR_PAGO = "Registrar pago";
    public static final String EVENT_ACTION_ENVIAR_MENSAJE_DE_PAGO = "Enviar mensaje de pago";
    public static final String EVENT_ACTION_ENVIAR_MENSAJE_DE_DEUDA = "Enviar mensaje de deuda";
    public static final String EVENT_ACTION_ACTUALIZAR_DEUDA = "Actualizar deuda";
    public static final String EVENT_ACTION_INIT_VIDEO = "Iniciar video";
    public static final String EVENT_ACTION_VER_MAS = "Ver mas";
    public static final String EVENT_ACTION_CLIC_BOTON = "Clic boton";
    public static final String EVENT_ACTION_CLIC_TAB = "Clic tab";
    public static final String EVENT_NAME_BANNER_ID = "BAN_LANZAMIENTO_URL";
    public static final String EVENT_NAME_BANNER = "Banner";
    public static final String EVENT_PREFIX_HOME = "Home_";
    public static final String EVENT_PREFIX_LANDING = "Landing_";
    public static final String EVENT_ACTION_CLICK_PROMOTION = "Click en Promoción";

    //PEDIDO
    public static final String EVENT_ACTION_RESERVA_EXITOSA = "Reserva exitosa";
    public static final String EVENT_ACTION_SELECCIONAR_CLIENTE = "Seleccionar cliente";
    public static final String EVENT_ACTION_SELECCIONAR_TAB = "Seleccionar Tab";
    public static final String EVENT_ACTION_BUSCAR_PRODUCTO = "Buscar producto";
    public static final String EVENT_ACTION_MODIFICAR_PEDIDO = "Modificar pedido";
    public static final String EVENT_ACTION_VISUALIZAR_PRODUCTO = "Visualizar producto";
    public static final String EVENT_ACTION_VISUALIZAR_PRODUCTO_O_F = "Visualizar producto - Oferta final";
    public static final String EVENT_ACTION_CERRAR_OFERTA_O_F = "Cerrar oferta - Oferta final";
    public static final String EVENT_ACTION_RESERVA_EXITOSA_O_F = "Reserva exitosa - Oferta final";
    public static final String EVENT_ACTION_BANNER_DUO_PERFECTO = "Banner Dúo Perfecto - Click Botón";

    public static final String EVENT_NAME_VIEW_MORE = "view_more";
    public static final String EVENT_NAME_RESERVATION_COMPLETE = "reservation_complete";
    public static final String EVENT_NAME_CHOOSE_CLIENT = "choose_client";
    public static final String EVENT_NAME_TAB_SELECTION = "tab_selection";
    public static final String EVENT_NAME_SEARCH = "search";
    public static final String EVENT_NAME_RESERVATION_ALTER = "reservation_alter";
    public static final String EVENT_NAME_PRODUCT_VIEW = "product_view";
    public static final String EVENT_NAME_CLOSE_OFFER = "close_offer";
    public static final String EVENT_NAME_VER_MAS = "ver_mas";
    public static final String EVENT_NAME_GUARDAR_PEDIDO = "guardar_pedido";
    public static final String EVENT_NAME_TAB_PEDIDO_GANAMAS = "tab_pedido_ganamas";
    public static final String EVENT_NAME_TAB_CLIENTES_UNIDADES = "tab_clientes_unidades";
    public static final String EVENT_NAME_SELECCIONA_CLIENTE = "selecciona_cliente";
    public static final String EVENT_NAME_INGRESAR_BUSQUEDA = "ingresar_busqueda";
    public static final String EVENT_NAME_SELECCIONAR_PRODUCTO_LISTA = "seleccionar_producto_lista";
    public static final String EVENT_NAME_MODIFICAR_FICHA_RESUMIDA = "modificar_ficha_resumida";

    public static final String EVENT_TRACK_NOTIFICACIONES_MENU_SUPERIOR_TABULAR = SPACE + "| Menú Superior";
    public static final String EVENT_TRACK_NOTIFICACIONES_MENU_SUPERIOR = Constant.BRAND_FOCUS_NAME + EVENT_TRACK_NOTIFICACIONES_MENU_SUPERIOR_TABULAR;
    public static final String EVENT_TRACK_NOTIFICACIONES_HOME = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_HOME;
    public static final String EVENT_TRACK_NOTIFICACIONES = "Notificaciones";
    public static final String EVENT_TRACK_NOT_AVAILABLE = "(not available)";
    public static final String EVENT_TRACK_SHOW_NOTIFICATIONS = "Ver notificaciones";
    public static final String EVENT_TRACK_EDIT_PROFILE = "Editar Perfil";

    public static final String EVENT_LABEL_VER_MENU = "Ver menú";
    public static final String EVENT_LABEL_AGREGAR_CLIENTE = "Agregar cliente";
    public static final String EVENT_LABEL_NUEVO_CLIENTE = "Nuevo cliente";
    public static final String EVENT_LABEL_AGREGAR_DESDE_CONTACTO = "Agregar desde contacto";
    public static final String EVENT_LABEL_AGREGAR = "Agregar";
    public static final String EVENT_LABEL_GUARDAR = "Guardar";
    public static final String EVENT_LABEL_GESTIONAR_DEUDA = "Gestionar deuda";
    public static final String EVENT_LABEL_INGRESAR_PEDIDO = "Ingresar pedido";
    public static final String EVENT_LABEL_REVISAR_PEDIDOS = "Revisar pedidos";
    public static final String EVENT_LABEL_AGREGAR_DEUDA = "Agregar deuda";
    public static final String EVENT_LABEL_ANADIR_RECORDATORIO = "Añadir recordatorio";
    public static final String EVENT_LABEL_ANADIR_DEUDA = "Añadir deuda";
    public static final String EVENT_LABEL_REGISTRAR_UN_PAGO = "Registrar un pago";
    public static final String EVENT_LABEL_ENVIAR_DETALLE_DE_DEUDA = "Enviar detalle de deuda";
    public static final String EVENT_LABEL_DEUDA_AGREGADA = "Deuda Agregada";
    public static final String EVENT_LABEL_PEDIDO_BELCORP = "Pedido Belcorp";
    public static final String EVENT_LABEL_APLICAR_DESCUENTO = "Aplicar descuento";
    public static final String EVENT_LABEL_CLIC_APLICAR_DESCUENTO = "Clic en aplicar descuento";
    public static final String EVENT_LABEL_PAGO = "Pago";
    public static final String EVENT_LABEL_REGISTRO_EXITOSO = "Registro exitoso";
    public static final String EVENT_LABEL_VER_TODAS_LAS_OFERTAS = "Ver todas las ofertas";
    public static final String EVENT_LABEL_OFERTAS_PARA_TI = "Ofertas para ti";
    public static final String EVENT_LABEL_GUARDAR_PEDIDO = "Guardar Pedido";
    public static final String EVENT_LABEL_RESERVAR_PEDIDO = "Reservar Pedido";
    public static final String EVENT_LABEL_INGRESAR_PRODUCTO = "Ingresar Producto";
    public static final String EVENT_LABEL_INGRESAR_PRODUCTO_VOZ = "Ingresar Producto por Voz";

    public static final String SCREEN_NAME_CLIENTES_TODOS = Constant.BRAND_FOCUS_NAME + " | Clientes | Todos ";
    public static final String SCREEN_NAME_CLIENTES_CON_DEUDA = Constant.BRAND_FOCUS_NAME + " | Clientes | Con deuda ";
    public static final String SCREEN_NAME_CLIENTES_FAVORITOS = Constant.BRAND_FOCUS_NAME + " | Clientes | Con Pedido ";
    public static final String SCREEN_NAME_CLIENTES_IMPORTAR_CLIENTES = Constant.BRAND_FOCUS_NAME + " | Clientes | Importar clientes ";
    public static final String SCREEN_NAME_CLIENTES_AGREGAR_CLIENTE = Constant.BRAND_FOCUS_NAME + " | Clientes | Agregar cliente ";
    public static final String SCREEN_NAME_CLIENTES_FICHA = Constant.BRAND_FOCUS_NAME + " | Clientes | Ficha ";
    public static final String SCREEN_NAME_CLIENTES_DETALLE_DEUDA = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_CLIENTES_DETALLE_DEUDA;
    public static final String SCREEN_NAME_CLIENTES_AGREGAR_DEUDA = Constant.BRAND_FOCUS_NAME + " | Clientes | Agregar deuda ";
    public static final String SCREEN_NAME_CLIENTES_REGISTRAR_PAGO = Constant.BRAND_FOCUS_NAME + " | Clientes | Registrar Pago ";
    public static final String SCREEN_NAME_CLIENTES_ENVIAR_PAGO = Constant.BRAND_FOCUS_NAME + " | Clientes | Enviar pago ";
    public static final String SCREEN_NAME_CLIENTES_ENVIAR_DEUDA = Constant.BRAND_FOCUS_NAME + " | Clientes | Enviar deuda ";
    public static final String SCREEN_NAME_CLIENTES_EDITAR_DEUDA = Constant.BRAND_FOCUS_NAME + " | Clientes | Editar deuda ";
    public static final String SCREEN_NAME_INGRESAR_PEDIDO = Constant.BRAND_FOCUS_NAME + " | Ingresar pedido ";
    public static final String SCREEN_NAME_PEDIDO_RESERVADO = Constant.BRAND_FOCUS_NAME + " | Pedido reservado ";

    public static final String EVENT_POP_UP_PREFIX = "Pop up - ";

    // Joshua Analytics
    public static final String EVENT_CATEGORY_TOP = Constant.BRAND_FOCUS_NAME + " | Menú Superior";
    public static final String EVENT_CATEGORY_LATERAL = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_MENU_LATERAL;
    public static final String EVENT_ACTION_OPTION = "Ver opción";
    public static final String EVENT_NAME_NAVIGATION_TOP = "menu_top_navigation";
    public static final String EVENT_NAME_NAVIGATION_LATERAL = "menu_lateral_navigation";
    public static final String EVENT_NAME_NAVIGATION_LATERAL_MIS_PEDIDOS = "menu_lateral_mis_pedidos";
    public static final String EVENT_NAME_NAVIGATION_LATERAL_SEGUIMIENTO_PEDIDOS = "menu_lateral_seguimiento_pedidos";
    public static final String EVENT_NAME_NAVIGATION_LATERAL_ESTADO_CUENTA = "menu_lateral_estado_cuenta";
    public static final String EVENT_NAME_NAVIGATION_LATERAL_PRODUCTOS_AGOTADOS = "menu_lateral_productos_agotados";
    public static final String EVENT_NAME_NAVIGATION_LATERAL_CAMBIOS_DEVOLUCIONES = "menu_lateral_cabios_devoluciones";
    public static final String EVENT_NAME_NAVIGATION_LATERAL_TUTORIAL = "menu_lateral_tutorial";
    public static final String EVENT_NAME_NAVIGATION_LATERAL_SUGERENCIAS = "menu_lateral_sugerencias";
    public static final String EVENT_NAME_NAVIGATION_LATERAL_TERMINOS = "menu_lateral_terminos";
    public static final String EVENT_NAME_NAVIGATION_LATERAL_LIQUIDACION_WEB = "menu_lateral_liquidacion_web";
    public static final String NOT_AVAILABLE = "(not available)";
    public static final String ACTION_MIS_PEDIDOS = "Ver mis pedidos";
    public static final String ACTION_SEGUIMIENTO = "Seguimiento de Pedidos";
    public static final String ACTION_ESTADO_CUENTA = "Ver estado de cuenta";
    public static final String ACTION_PRODUCTOS_AGOTADOS = "Ver productos agotados";
    public static final String ACTION_CAMBIOS_DEVOLUCIONES = "Ver cambios y devoluciones";
    public static final String ACTION_TUTORIAL = "Ver tutorial";
    public static final String ACTION_SUGERENCIAS = "Ver sugerencias";
    public static final String ACTION_TERMINOS = "Ver Términos";
    public static final String ACTION_LIQUIDACION_WEB = "Liquidación web";


    public static final String EVENT_NAME_CLIENT_VIEW = "client_view";
    public static final String EVENT_NAME_CLIENT_NEW = "client_new";
    public static final String EVENT_NAME_CLIENT_IMPORT = "client_import";
    public static final String EVENT_NAME_CLIENT_NEW_SAVE = "client_new_save";
    public static final String EVENT_NAME_CLIENT_FILE = "client_file";
    public static final String EVENT_NAME_CLIENT_DEBT = "client_debt";
    public static final String EVENT_LIST_NAME_PROMOTIONS = "promotions";

    public static final String SCREEN_CLIENT_REGISTRATION = Constant.BRAND_FOCUS_NAME + " | Clientes | Agregar cliente";
    public static final String SCREEN_CLIENT_ORDERS_HISTORY = Constant.BRAND_FOCUS_NAME + " | Clientes | Historial de pedidos";
    public static final String SCREEN_CLIENT_ORDERS = Constant.BRAND_FOCUS_NAME + " | Clientes | Ingresar Pedido";
    public static final String SCREEN_CLIENT_NOTE_EDIT = Constant.BRAND_FOCUS_NAME + " | Clientes | Agregar nota";
    public static final String SCREEN_CLIENT_EDIT = Constant.BRAND_FOCUS_NAME + " | Clientes | Editar";
    public static final String SCREEN_CLIENT_CARD = Constant.BRAND_FOCUS_NAME + " | Clientes | Ficha";

    public static final String SCREEN_CONFIGURATION = Constant.BRAND_FOCUS_NAME + " | Configuration";
    public static final String SCREEN_CONTACT = Constant.BRAND_FOCUS_NAME + " | Clientes | Importar clientes";
    public static final String SCREEN_DEBT_ADD = Constant.BRAND_FOCUS_NAME + " | Clientes | Agregar deuda";
    public static final String SCREEN_RECORDATORY = Constant.BRAND_FOCUS_NAME + " | Clientes | Agregar recordatorio";
    public static final String SCREEN_DEBT_SHARE = Constant.BRAND_FOCUS_NAME + " | Clientes | Enviar deuda";
    public static final String SCREEN_DEBT_HISTORY = Constant.BRAND_FOCUS_NAME + " | Clientes | Historial de deudas";
    public static final String SCREEN_DEBT_DEUDA_AGREGADA = Constant.BRAND_FOCUS_NAME + " | Clientes | Deuda Agregada";
    public static final String SCREEN_SEGUIMIENTO_PEDIDOS = Constant.BRAND_FOCUS_NAME + " | Seguimiento de pedidos";
    public static final String SCREEN_ESTADO_CUENTA = Constant.BRAND_FOCUS_NAME + " | Estado de cuenta";
    public static final String SCREEN_PAGO_EN_LINEA = Constant.BRAND_FOCUS_NAME + " | Pago en Línea";
    public static final String SCREEN_MY_ORDERS = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_MIS_PEDIDOS;
    public static final String SCREEN_MI_PERFIL = Constant.BRAND_FOCUS_NAME + " | Mi perfil";
    public static final String SCREEN_MI_PERFIL_EDITAR = Constant.BRAND_FOCUS_NAME + " | Editar perfil";
    public static final String SCREEN_DEBT_DETAIL = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_CLIENTES_DETALLE_DEUDA;
    public static final String SCREEN_DEBT_PAGO = Constant.BRAND_FOCUS_NAME + " | Clientes | Pago";
    public static final String SCREEN_DEBT_PEDIDO_BELCORP = Constant.BRAND_FOCUS_NAME + " | Deudas | Pedido Belcorp";
    public static final String SCREEN_PAYMENT_ADD = Constant.BRAND_FOCUS_NAME + " | Clientes | Registrar Pago";
    public static final String SCREEN_PAYMENT_SHARE = Constant.BRAND_FOCUS_NAME + " | Clientes | Enviar pago";
    public static final String SCREEN_SPLASH = Constant.BRAND_FOCUS_NAME + " | Precarga";
    public static final String SCREEN_PRODUCT = Constant.BRAND_FOCUS_NAME + " | Producto";

    public static final String SCREEN_ORDERS_FIC = Constant.BRAND_FOCUS_NAME + " | Pedido Fic | Home";
    public static final String SCREEN_ORDERS = Constant.BRAND_FOCUS_NAME + " | Pedidos | Home";
    public static final String SCREEN_OFFERS = Constant.BRAND_FOCUS_NAME + " | Ofertas | Home";
    public static final String SCREEN_CLOSEOUT = Constant.BRAND_FOCUS_NAME + " | Liquidacion Web";
    public static final String SCREEN_CHANGES = Constant.BRAND_FOCUS_NAME + " | Cambios y Devoluciones";
    public static final String SCREEN_ORDERS_GPR = Constant.BRAND_FOCUS_NAME + " | Ingresar pedido";
    public static final String SCREEN_WALK_SUCCESS = Constant.BRAND_FOCUS_NAME + " | Camino al éxito";
    public static final String SCREEN_ORDERS_ORDER_DETAIL = Constant.BRAND_FOCUS_NAME + " | Ingresar pedido | Pedido reservado";
    public static final String SCREEN_ORDERS_FINAL_OFFER = Constant.BRAND_FOCUS_NAME + " | Ingresar pedido | Oferta final";
    public static final String SCREEN_CLIENTS_ALL = Constant.BRAND_FOCUS_NAME + " | Clientes | Todos";
    public static final String SCREEN_CLIENTS_DEBT = Constant.BRAND_FOCUS_NAME + " | Clientes | Con deuda";
    public static final String SCREEN_CLIENTS_WITH_ORDER = Constant.BRAND_FOCUS_NAME + " | Clientes | Con pedido";

    public static final String SCREEN_FORGOT_PSW = Constant.BRAND_FOCUS_NAME + " | Olvidaste Contraseña";
    public static final String SCREEN_CATALOG_CATALOG = Constant.BRAND_FOCUS_NAME + " | Catálogos";
    public static final String SCREEN_CATALOG_MAGAZINE = Constant.BRAND_FOCUS_NAME + " | Revistas";

    public static final String SCREEN_TUTORIAL = Constant.BRAND_FOCUS_NAME + " | Tutorial";
    public static final String SCREEN_TEMS = Constant.BRAND_FOCUS_NAME + " | Términos y condiciones";

    public static final String SCREEN_INCENTIVES_GIFT_ORDER = Constant.BRAND_FOCUS_NAME + " | Incentivos | Bonificaciones activas";
    public static final String SCREEN_INCENTIVES_GIFT_HISTORIC = Constant.BRAND_FOCUS_NAME + " | Incentivos | Histórico";

    public static final String SCREEN_REGISTER_FACEBOOK = Constant.BRAND_FOCUS_NAME + " | Asociar con facebook";

    public static final String EVENT_CAT_CATALOG = Constant.BRAND_FOCUS_NAME + " | Catálogos";
    public static final String EVENT_ACTION_CATALOG_DIGITAL = "Compartir Catálogo Digital";
    public static final String EVENT_ACTION_CATALOG_FB = "Compartir por Facebook";
    public static final String EVENT_ACTION_CATALOG_WA = "Compartir por Whatsapp";

    public static final String EVENT_LABEL_CATALOG_WA = "Compartir App Catalogos";

    public static final String EVENT_ACTION_CATALOG_MAIL = "Compartir por Email";
    public static final String EVENT_ACTION_CATALOG_DOWNLOAD = "Descargar App Catalogos";
    public static final String EVENT_SHARE_CATALOG = "share_catalog";

    public static final String SCREEN_TRACK_ORDER = Constant.BRAND_FOCUS_NAME + " | Seguimiento de pedido";
    public static final String SCREEN_SPENT_ORDER = Constant.BRAND_FOCUS_NAME + " | Productos agotados";

    public static final String SCREEN_PROGRAM_FOR_NEWS = Constant.BRAND_FOCUS_NAME + " | Incentivos | Bonificaciones activas | Programa de Nuevas";
    public static final String SCREEN_GIFT_BY_ORDER = Constant.BRAND_FOCUS_NAME + " | Incentivos | Bonificaciones activas | Regalo por pedidos";
    public static final String SCREEN_GIFT_BY_CONSTANCY = Constant.BRAND_FOCUS_NAME + " | Incentivos | Bonificaciones activas | Regalo por constancia";
    public static final String ACTION_AGREGAR_PRODUCTO = "Agregar producto";

    /////////

    public static final String EVENT_ACTION_BUTTON = "Clic Botón";
    public static final String EVENT_LOGIN_NAME = "buttons_login";
    public static final String EVENT_LOGIN_FB_NAME = "login_facebook";
    public static final String EVENT_LOGIN_FORM_NAME = "login_user";
    public static final String EVENT_LOGIN_FB = "Ingresa con Facebook";
    public static final String EVENT_LOGIN_NORMAL = "Ingresa con tu usuario";
    public static final String EVENT_LOGIN_SUCCESS = "Iniciar Sesión Satisfactorio";
    public static final String EVENT_LABEL_LOGIN_FB = "Facebook";
    public static final String EVENT_LABEL_LOGIN_NORMAL = "Ingresa con usuario";

    public static final String EVENT_LOGOUT = "logout";
    public static final String ACTION_LOGOUT = "Cerrar Sesión";

    public static final String EVENT_LABEL_NOT_AVAILABLE = "(not available)";

    public static final String EVENT_MENU_NAVIGATION = "menu_navigation";
    public static final String EVENT_SHOW_SECTION = "home_options";

    public static final String ACTION_SHOW_SECTION = "Ver sección";
    public static final String ACTION_MENU_NAVIGATION = "Navegación Menú";
    public static final String EVENT_LABEL_MENU_HOME = "Inicio";
    public static final String EVENT_LABEL_MENU_CLIENTS = "Clientes";
    public static final String EVENT_LABEL_MENU_FANPAGE = "50años";
    public static final String EVENT_LABEL_CARD_CLIENTS = "Gestionar clientes y deudas";

    public static final String EVENT_LABEL_MENU_ORDERS = "Pedido";
    public static final String EVENT_LABEL_CARD_ORDERS = "Ingresa pedidos";
    public static final String EVENT_LABEL_MENU_DEBTS = "Gestionar deudas";
    public static final String EVENT_LABEL_MENU_INCENTIVES = "Incentivos";
    public static final String EVENT_LABEL_CARD_INCENTIVES = "Gana Incentivos";
    public static final String EVENT_LABEL_MENU_CAMINO = "Camino al éxito";

    public static final String EVENT_LABEL_TERMS = "Términos y condiciones";
    public static final String EVENT_LABEL_MAILBOX = "Sugerencias";
    public static final String EVENT_LABEL_OFFERS = "Gana+";

    public static final String EVENT_LABEL_TUTORIAL = "Tutorial";

    public static final String EVENT_LABEL_PRODUCTOS_AGOTADOS = "Productos agotados";
    public static final String EVENT_LABEL_ESTADO_CUENTA = "Estado de cuenta";

    public static final String EVENT_LABEL_SEGUIMIENTO_PEDIDO = "Seguimiento de pedido";
    public static final String EVENT_LABEL_LIQUIDACION_WEB = "Liquidacion Web";
    public static final String EVENT_LABEL_CAMBIOS_DEVOLUCIONES = "CambiosDevoluciones";
    public static final String EVENT_LABEL_PEDIDO_FIC = "PedidoFic";
    public static final String EVENT_LABEL_MIS_PEDIDOS = "MisPedidos";
    public static final String EVENT_LABEL_MY_ACADEMY = "Mi Academia";
    public static final String EVENT_LABEL_TU_VOZ_ONLINE = "Tu voz online";
    public static final String EVENT_LABEL_CHATBOT = "Chat";
    public static final String EVENT_LABEL_MAQUILLADOR_VIRTUAL = "Maquillador Virtual";
    public static final String EVENT_LABEL_CATALOG = "Catálogos y revistas";
    public static final String EVENT_LABEL_PEDIDOS_PENDIENTES = "Pedidos Pendientes";

    public static final String EVENT_LABEL_TAB_GANAMAS = "Gana+";
    public static final String EVENT_LABEL_TAB_PEDIDO = "Pedido";
    public static final String EVENT_LABEL_TAB_CLIENTES = "Clientes";
    public static final String EVENT_LABEL_TAB_UNIDADES = "Unidades";

    public static final String EVENT_ACTION_BACK = "Retroceder";
    public static final String EVENT_CAT_BACK = Constant.BRAND_FOCUS_NAME + " | Navegación";
    public static final String EVENT_BACK = "back_navigation";
    public static final String EVENT_MENU = "menu_navigation";
    public static final String EVENT_ACTION_MENU = "Ver menú";

    public static final String EVENT_CAT_HOME_OPTION = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_HOME;
    public static final String EVENT_CAT_HOME_BANNER = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_HOME + " | Clic Banner";

    public static final String EVENT_ACTION_HOME_OPTION = "Clic Botón";
    public static final String EVENT_ACTION_HOME_BANNER = "Clic Banner Gana+";
    public static final String EVENT_LABEL_SECONDARY_MENU = "Abrir menú secundario";
    public static final String EVENT_LABEL_HOME_SHOW_ORDER = "Ver mis pedidos";
    public static final String EVENT_LABEL_HOME_ADD_NOTES = "Añadir Nota";
    public static final String EVENT_LABEL_HOME_ADD_CLIENTS = "Añadir Cliente";

    public static final String EVENT_NAME_HOME_OPTION = "view_more";

    public static final String EVENT_CAT_CLIENTS = Constant.BRAND_FOCUS_NAME + " | Clientes";
    public static final String EVENT_ACTION_CLIENTS = "Menú superior";
    public static final String EVENT_LABEL_CLIENTS_ALL = "Todos";
    public static final String EVENT_LABEL_CLIENTS_DEBT = "Con deuda";
    public static final String EVENT_LABEL_CLIENTS_ORDER = "Con pedido";
    public static final String EVENT_NAME_CLIENTS_MENU = "client_menu";

    public static final String EVENT_CAT_INCENTIVES = Constant.BRAND_FOCUS_NAME + " | Incentivos";
    public static final String EVENT_ACTION_INCENTIVES = "Menú superior";
    public static final String EVENT_LABEL_INCENTIVES_BONIFICATION = "Bonificaciones activas";
    public static final String EVENT_LABEL_INCENTIVES_HISTORIC = "Histórico";
    public static final String EVENT_NAME_INCENTIVES_MENU = "incentive_menu";

    public static final String EVENT_CAT_INCENTIVES_BONIFICATION = Constant.BRAND_FOCUS_NAME + " | Incentivos | Bonificaciones activas";
    public static final String EVENT_ACTION_INCENTIVES_BONIFICATION = "Ver detalle de concurso";
    public static final String EVENT_NAME_INCENTIVES_BONIFICATION = "incentive_campaing";

    public static final String EVENT_CAT_PROGRAM_FOR_NEWS = Constant.BRAND_FOCUS_NAME + " | Incentivos | Programa de Nuevas\n";
    public static final String EVENT_ACTION_PROGRAM_FOR_NEWS = "Ver detalle de campaña";
    public static final String EVENT_NAME_PROGRAM_FOR_NEWS = "incentive_campaing";

    public static final String EVENT_NAME_INCENTIVE_HISTORIC = "incentive_history";

    public static final String EVENT_CAT_UPDATE_RECORDATORY = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_CLIENTES_DETALLE_DEUDA;
    public static final String EVENT_ACTION_UPDATE_RECORDATORY = "Actualizar recordatorio";
    public static final String EVENT_NAME_UPDATE_RECORDATORY = "client_debt";

    public static final String EVENT_CAT_DELETE_RECORDATORY = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_CLIENTES_DETALLE_DEUDA;
    public static final String EVENT_ACTION_DELETE_RECORDATORY = "Eliminar recordatorio";
    public static final String EVENT_NAME_DELETE_RECORDATORY = "client_debt";

    public static final String EVENT_ACTION_PICK_CATALOG = "Seleccionar catálogo";
    public static final String EVENT_NAME_PICK_CATALOG = "view_catalog";

    public static final String EVENT_CAT_MAGAZINE = Constant.BRAND_FOCUS_NAME + " | Revistas";
    public static final String EVENT_ACTION_PICK_MAGAZINE = "Seleccionar revistas";
    public static final String EVENT_NAME_PICK_MAGAZINE = "view_catalog";

    public static final String EVENT_ACTION_OFERTAS_NSNA = "Ver guia de negocio digital";
    public static final String EVENT_ACTION_OFERTAS_SA = "Ver todas mis ofertas Suscrita Activa";
    public static final String EVENT_ACTION_OFERTAS_NSA = "Ver todas mis ofertas No Suscrita Activa";
    public static final String EVENT_ACTION_OFERTAS_SNA = "Ver todas mis ofertas Suscrita No Activa";

    public static final String SCREEN_TUTORIAL_WELCOME = Constant.BRAND_FOCUS_NAME + " | Tutorial | Bienvenida";
    public static final String SCREEN_TUTORIAL_CIENTS = Constant.BRAND_FOCUS_NAME + " | Tutorial | Gestiona clientes";
    public static final String SCREEN_TUTORIAL_ORDERS = Constant.BRAND_FOCUS_NAME + " | Tutorial | Ingresa pedidos";
    public static final String SCREEN_TUTORIAL_DEBTS = Constant.BRAND_FOCUS_NAME + " | Tutorial | Gestiona deudas";
    public static final String SCREEN_TUTORIAL_INCENTIVES = Constant.BRAND_FOCUS_NAME + " | Tutorial | Ver bonificaciones";

    public static final String EVENT_CAT_TUTORIAL = Constant.BRAND_FOCUS_NAME + " | Tutorial";
    public static final String EVENT_ACTION_TUTORIAL = "Clic Botón";
    public static final String EVENT_LABEL_TUTORIAL_SKIP = "Omitir";
    public static final String EVENT_NAME_TUTORIAL_SKIP = "tutorial_skip";
    public static final String EVENT_LABEL_TUTORIAL_NEXT = "Siguiente";
    public static final String EVENT_NAME_TUTORIAL_NEXT = "tutorial_next";
    public static final String EVENT_LABEL_TUTORIAL_ENTER = "Ingresar";
    public static final String EVENT_NAME_TUTORIAL_ENTER = "tutorial_enter";

    public static final String EVENT_CAT_REMINDER = Constant.BRAND_FOCUS_NAME + " | Notificaciones";
    public static final String EVENT_ACTION_REMINDER = "Clic notificación";
    public static final String EVENT_LABEL_REMINDER = "Recordatorio de Cobranza";
    public static final String EVENT_NAME_REMINDER = "notification_clic";

    public static final String EVENT_ACTION_CLICK_CLIENTS = "Tap & Hold";
    public static final String EVENT_LABEL_CLIENT_LONG_CLICK = "Ver opciones cliente";
    public static final String EVENT_NAME_CLIENT_LONG_CLICK = "client_taphold";
    public static final String EVENT_LABEL_CLIENT_EDIT = "Editar";
    public static final String EVENT_NAME_CLIENT_EDIT = "client_edit";
    public static final String EVENT_LABEL_CLIENT_DELETE = "Eliminar";
    public static final String EVENT_NAME_CLIENT_DELETE = "client_delete";
    public static final String EVENT_LABEL_CLIENT_FAV = "Favorito";
    public static final String EVENT_NAME_CLIENT_FAV = "client_marked_favorite";

    public static final String EVENT_CAT_DEBT = Constant.BRAND_FOCUS_NAME + " | Deudas";
    public static final String EVENT_ACTION_DEBT = "Tap & Hold";
    public static final String EVENT_LABEL_DEBT_LONG_CLICK = "Ver opciones deuda";
    public static final String EVENT_NAME_DEBT_LONG_CLICK = "client_debt";
    public static final String EVENT_LABEL_DEBT_EDIT = "Editar deuda";
    public static final String EVENT_NAME_DEBT_EDIT = "client_debt";
    public static final String EVENT_LABEL_DEBT_DELETE = "Eliminar deuda";
    public static final String EVENT_NAME_DEBT_DELETE = "client_debt";
    public static final String EVENT_LABEL_PAYMENT_LONG_CLICK = "Ver opciones pago";
    public static final String EVENT_NAME_PAYMENT_LONG_CLICK = "client_debt";
    public static final String EVENT_LABEL_PAYMENT_DELETE = "Eliminar pago";
    public static final String EVENT_NAME_PAYMENT_DELETE = "client_debt";
    public static final String EVENT_LABEL_PAYMENT_EDIT = "Editar pago";
    public static final String EVENT_NAME_PAYMENT_EDIT = "client_debt";

    public static final String SCREEN_PAYMENT_EDIT = Constant.BRAND_FOCUS_NAME + " | Clientes | Editar pago";
    public static final String SCREEN_DEBT_EDIT = Constant.BRAND_FOCUS_NAME + " | Clientes | Editar deuda";
    //
    public static final String EVENT_LABEL_MORE = "Ver más";
    public static final String EVENT_LABEL_ADD_CLIENT = "Añadir Cliente";
    public static final String EVENT_LABEL_ADD_NOTE = "Añadir Nota";
    public static final String EVENT_LABEL_SHOW_MY_ORDERS = "Ver mis pedidos";

    public static final String EVENT_CAT_MY_ORDERS = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_MIS_PEDIDOS;
    public static final String EVENT_ACTION_MY_ORDERS_TRACKING = "Ver Seguimiento Pedido";
    public static final String EVENT_ACTION_MY_ORDERS_PAQDOC = "Ver Paquete Documentario";
    public static final String EVENT_ACTION_MY_ORDERS_DETAIL = "Ver Detalle Pedido";
    public static final String EVENT_NAME_MY_ORDERS = "client_debt";
    public static final String EVENT_NAME_PAQUETE_DOC = "paquete_documentario";

    // MAS ANALYTICS
    public static final String SCREEN_CLIENT_FILTER = Constant.BRAND_FOCUS_NAME + " | Clientes | Filter";

    public static final String EVENT_BANNER_GANA_MAS_NSNA = "Banner No Suscrita Gana Más";
    public static final String EVENT_BANNER_GANA_MAS_NSA = "Banner No Suscrita Gana Más";
    public static final String EVENT_BANNER_GANA_MAS_SNA = "Banner Suscrita Gana Más";
    public static final String EVENT_BANNER_GANA_MAS_SA = "Banner Suscrita Gana Más";

    // AB TESTING
    public static final String EVENT_ACTION_OPTION_AB = "Ver opción AB";
    public static final String EVENT_ACTION_MENU_AB = "Ver menú AB";

    public static final String EVENT_NAME_CATALOG_TOP = "menu_top_catalogos";
    public static final String EVENT_NAME_CATALOG_LATERAL = "menu_lateral_catalogos";

    // DATAMI
    public static final String SCREEN_DATAMI_MESSAGE = Constant.BRAND_FOCUS_NAME + " | Datos patrocinados";

    //HYBRIS
    public static final boolean HM_IS_DIRECT = false;
    public static final String TRACKING_URL = "trackingURL";
    public static final String NOTIFICATION_CODE = "notification_code";
    public static final String NOTIFICATION_ID = "notification_id";
    public static final String NOTIFICATION_ORDER_OPTION_CODE = "order_option_code";

    //Fan Page Belcorp
    public static final String FANPAGE_URL = "https://www.facebook.com/SomosBelcorpOficial";

    //Kinesis Manager
    public static final String ROL_CODE = "CONS";
    public static final String SCREEN_LOG_HOME = "HOME";
    public static final String SCREEN_LOG_LOGIN = "LOGIN";

    public static final String SCREEN_MY_ACADEMY = Constant.BRAND_FOCUS_NAME + " | Mi Academia";

    public static final String SEPARATOR = "/";
    public static final String SCREEN_ORDERS_PRODUCT = Constant.BRAND_FOCUS_NAME + " | Producto detalle";

    public static final String SCREEN_TU_VOZ_ONLINE = Constant.BRAND_FOCUS_NAME + " | Tu voz online";

    // BUSCADOR

    public static final String SCREEN_SEARCH_LIST = Constant.BRAND_FOCUS_NAME + " | Buscador";
    public static final String EVENT_CAT_SEARCH = "Buscador SB";
    public static final String EVENT_ACTION_SEARCH_SELECTION = "Selección";
    public static final String EVENT_ACTION_SEARCH = "Buscar";
    public static final String EVENT_ACTION_SEARCH_CHOOSE = "Elige tu opción";
    public static final String EVENT_NAME_VIRTUAL_EVENT = "virtualEvent";
    public static final String EVENT_ACTION_SEARCH_EMPTY = "Búsqueda – sin Resultados";
    public static final String EVENT_ACTION_FILTER_FROM = "Filtrar por ";
    public static final String EVENT_ACTION_CLICK_BUTTON = "Clic en Botón";
    public static final String EVENT_ACTION_DELETE_FILTER = "Eliminar Filtro";
    public static final String EVENT_ACTION_APPLY_FILTER = "Aplicar Filtros";
    public static final String EVENT_LABEL_FILTER_BUTTON = "FILTRAR";

    // PAGINA DE RESULTADOS
    public static final String EVENT_ACTION_BOTON_RESULT = "Ver Todos Los Resultados";
    public static final String EVENT_CAT_SEARCH_RESULT = "Resultados de Búsqueda";
    public static final String EVENT_ACTION_RESULT_ORDER_BY = "Ordenar Por";

    // CHATBOT
    public static final String EVENT_ACTION_CHATBOT = "Ver sección";
    public static final String EVENT_NAME_NAVIGATION_TOP_CHATBOT = "menu_superior_chat";
    public static final String EVENT_NAME_SECCION_CHAT = "seccion_chat";

    // Resumen (Summary)

    public static final String SCREEN_SUMMARY = Constant.BRAND_FOCUS_NAME + " | Resumen";

    // ADD_TO_CART
    public static final String VARIANT_ADD_TO_CART = "Estandar";
    public static final String ERROR_BOTTONSHEET = "error_tooltip";
    public static final String ERROR_DIALOG = "Error_Dialog";
    //PAGO EN LINEA
    public static final String PAGO_TOTAL = "Pago Total";
    public static final String PAGO_PARCIAL = "Pago Parcial";

    public static final String PASO_UNO = "Paso 1";
    public static final String PASO_DOS = "Paso 2";
    public static final String CODIGO_MEDIO_PAGO_INTENRNET = "PBI";
    public static final String CODIGO_CANTIDAD_EXCEDIDA = "1114";
    public static final String CODIGO_PREMIO_FESTIVAL_ALCANZADO = "2010";
    public static final String CODIGO_TIPO_PAGO_TOTAL = "01";
    public static final String CODIGO_TIPO_PAGO_PARCIAL = "02";
    public static final String CARD_SELECTED = "card_selected";
    public static final String URL_ICONO = "urlIcon";
    public static final String ESTADO_CUENTA = "estadoCuenta";
    public static final String TIPO_PAGO = "tipoPago";
    public static final String LABEL_VENCE = "Vence el ";
    public static final String ARCHIVO = "ARCHIVO";
    public static final String CONFIRMACION_PAGO = "Confirmacion_Pago";
    public static final String RESULTADO_PAGO = "Resultado_Pago";
    public static final String HOME = "Home";
    public static final String EVENT_NAME_PAGO_LINEA = "Pago en Línea";
    public static final String EVENT_NAME_ACTUALIZACION_CORREO = "Pop up de actualizacion de correo";
    public static final String EVENT_ACTION_ACTUALIZACION_CORREO = "Actualizacion de correo";
    public static final String EVENT_ACTION_BANCA_INTERNET = GlobalConstant.EVENT_ACTION_METODO_PAGO.concat(" – Banca por Internet");
    public static final String EVENT_ACTION_METODO_PAGO = "Método de Pago";
    public static final String EVENT_ACTION_VISA_PASO_DOS = EVENT_ACTION_METODO_PAGO.concat(" - Visa - ").concat(PASO_DOS);
    public static final String EVENT_LABEL_PAGAR_VISA = "Pagar con Visa";
    public static final String EVENT_ACTION_VISA_PASO_UNO = EVENT_ACTION_METODO_PAGO.concat(" - Visa - ").concat(PASO_UNO);
    public static final String EVENT_SCREEN_EXITOSO = Constant.BRAND_FOCUS_NAME + " | Pago en Exitoso";
    public static final String EVENT_SCREEN_CONSTANCIA_PAGO = Constant.BRAND_FOCUS_NAME + " | Pago en Línea - " + GlobalConstant.EVENT_ACTION_CONSTANCIA;
    public static final String EVENT_ACTION_CONSTANCIA = "Constancia de Pago";
    public static final String EVENT_LABEL_REVISAR_CONSTANCIA = "Revisar Constancia - " + GlobalConstant.AQUI;
    public static final String EVENT_LABEL_TERMINOS = "Términos y condiciones - " + GlobalConstant.AQUI;
    public static final String EVENT_LABEL_VER_ESTADO_CUENTA = "Ver estado de Cuenta";
    public static final String AQUI = "Aquí";
    public static final String EVENT_ACTION_PAGO_RECHAZADO = "Pago Rechazado";
    public static final String SCREEN_NAME_RECHAZADO = Constant.BRAND_FOCUS_NAME + " | " + GlobalConstant.EVENT_ACTION_PAGO_RECHAZADO;
    public static final String EVENT_CONTINUAR = " – Continuar";
    public static final String EVENT_TERMINOS = "  - Terminos y condiciones";
    public static final String NO_SE_PUDO_AGREGAR_OFERTA = "NO SE PUDO AGREGAR LA OFERTA";
    public static final String FALTA_CONFIRMAR_CORREO = "Aún falta confirmar tu correo";
    public static final String FALTA_CONFIRMAR_NUMERO = "Aún falta confirmar tu número";
    // PAGINA DE NOTIFICACIONES
    public static final String SCREEN_NOTIFICATION_LIST = "Página de Notificaciones";

    // PEDIDO POR VOZ
    public static final String SCREEN_PEDIDO_VOZ = Constant.BRAND_FOCUS_NAME + " | Agregar por Voz";
    public static final String SCREEN_PEDIDO_VOZ_ERROR = Constant.BRAND_FOCUS_NAME + " | Agregar por Voz Error";
    public static final String CATEGORY_PEDIDO_VOZ = Constant.BRAND_FOCUS_NAME + " | Pedido por Voz";
    public static final String EVENT_LABEL_PEDIDO_VOZ = "Ingresar producto por Voz";
    public static final String ACTION_AGREGAR_PEDIDO_VOZ = "Agregar Producto por voz";
    public static final String LABEL_CERRAR_AGREGAR_POR_VOZ = "Cerrar Pop up Agregar por Voz";
    public static final String LABEL_CERRAR_POPUP_VOLVER_INTENTARLO = "Cerrar Pop up Volver a Intentarlo";
    public static final String LABEL_VOLVER_INTENTARLO = "Volver a Intentarlo";

    public static final String ANALYTICS_NO_DISPONIBLE = "No disponible";
    public static final String ANALYTICS_AGREGAR_POR_VOZ = "Pedido - Agregado por Voz";
    //Duo Perfecto
    public static final String DUO = "DUO";
    public static final String ELIMINACION_DUO_PERFECTO = "Si eliminas este producto te quedarás sin tu DÚO PERFECTO. ¿Deseas continuar?";
    public static final String NO_PODER_ANADIR_DUO_PERFECTO = "Ya no puedes añadir otro producto, tu Dúo Perfecto está completo.";
    public static final String DUOPERFECTO_CODE_AGREGASTE = "0010";
    public static final String DUOPERFECTO_CODE_COMPLETASTE = "0011";
    public static final String TOOL_TIPS_MESSAGE = "ToolTipsMessage";
    public static final String EXTRA_MONTO = "extramonto";
    public static final String EXTRA_BOOLEANO_GIFT = "giftStatus";

    //MENSAJES BARRA DE REGALOS
    public static final String EMPIEZA_AGREGAR_PRODUCTOS = "¡Empieza a agregar productos para alcanzar tu regalo!";
    public static final String EMPIEZA_AGREGAR_PRODUCTO_NO_REGALO = "¡Empieza a agregar productos!";
    public static final String ENTREGA_PROXIMA_CAMPANNA = "Este producto te lo entregamos en la siguiente campaña";
    public static final String TEXTO_BOTON_ANIMACION_GIFT_NO_ESCOGIDO = "¡Escoger ahora!";
    public static final String TEXTO_BOTON_ANIMACION_GIFT_ESCOGIDO = "Cambiar producto";
    public static final String ELIMINAR_PRODUCTO_GIFT = "¡Oh no! Si eliminas productos no podrás llevarte tu regalo. Recuerda que debes llegar a ";
    public static final String ELIMINAR_PRODUCTO_GIFT_PRO = "¡Oh no! Si eliminas productos no podrás llevarte tu Kit de Inicio. Recuerda que debes llegar a ";
    public static final String PARA_OBTENERLO = "para poder obtenerlo.";
    public static final String GANAR_UN = "GANAR UN ";
    public static final String MARCADOR_MONTO = "{Monto}";
    public static final String MARCADOR_PRODUCTO = "{Producto}";

    public static final String PREFIJO_HOME_ANALYTICS = "Home_";
    public static final String CATEGORY_MARCACIONES_MENU_SUPERIOR = Constant.BRAND_FOCUS_NAME + " | Menú Superior";
    public static final String CATEGORY_MARCACIONES_HOME = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_HOME;

    //Gana+
    public static final String SCREEN_GANA_MAS = Constant.BRAND_FOCUS_NAME + " | Gana+";
    public static final String SCREEN_INFORMATION_PRODUCT = Constant.BRAND_FOCUS_NAME + " | Informacion de Producto";
    public static final String SCREEN_GANA_BRAND = Constant.BRAND_FOCUS_NAME + " | ";
    public static final String SCREEN_GANA_MAS_SECTION = SCREEN_GANA_MAS + " | ";
    public static final String SCREEN_GANA_MAS_DETAIL = SCREEN_GANA_MAS + " | Detalle de Producto";
    public static final String RESTART_SCREEN = "RestartScreen";
    public static final String REFRESH_RC = "REFRESH_RC";
    public static final String GANA_MAS_SET_OFFERS = "GanaMasSetOffers";
    public static final String GANA_MAS_TRACK_CATEGORIES = "Inicio_Categorías";
    public static final String GANA_MAS_TRACK_INICIO = "Inicio_";
    public static final String GANA_MAS_CONTENEDOR = "Contenedor | ";
    public static final String GANA_MAS_CARRUSEL = " | Carrusel";

    // Camino Brillante
    public static final String SCREEN_FICHA_CAMINO_BRILLANTE = Constant.BRAND_FOCUS_NAME + " | Camino Brillante | Detalle de Producto";

    // Ficha Resumida
    public static final String SCREEN_FICHA_RESUMIDA = Constant.BRAND_FOCUS_NAME + " | Ingresar pedido | Ficha Resumida";

    //Analytics Suscrita - No suscrita
    public static final String LABEL_SUSCRITA = "Suscrita";
    public static final String LABEL_NO_SUSCRITA = "No Suscrita";
    public static final String VARIANT_ESTANDAR = "Estándar";

    //Analytics filtrar y ordenar
    public static final String ACTION_FILTER = "Aplicar Filtros";
    public static final String ACTION_FILTER_BY = "Agregar Filtro de";
    public static final String ACTION_FILTER_CLEAN = "Limpiar Filtros";
    public static final String ACTION_REMOVE_FILTER = "Eliminar Filtro";
    public static final String ACTION_SORT = "Ordenar por";

    public static final String EVENT_FILTER_AND_SORT = "filtrar_ordenar";
    public static final String EVENT_FILTER_ADD = "agregar_filtros";
    public static final String EVENT_FILTER_APPLY = "aplicar_filtros";
    public static final String EVENT_FILTER_CLEAN = "limpiar_filtros";
    public static final String EVENT_SORT = "ordenar_productos";


    //Analytics ver mas
    public static final String ACTION_VER_MAS = "Ver más";
    public static final String EVENT_VER_MAS = "verMas";
    public static final String CATEGORY_VER_MAS = "Contenedor - Inicio";

    public static final String FICHA_CARRUSEL = "Carrusel";

    //BONIFIFCACION 100 PERCENT DIGITAL
    public static final String CODE_BONIF_RESUMEN = "BONIFICACIONES_RESUMEN";

    //TagStories
    public static final String STORIE_MODEL = "StorieModel";
    public static final String FIRST_ITEM_IMAGE_STORIE = "PrimerItem";
    public static final String TAG_STORIES = "HISTORIAS_RESUMEN";
    public static final Long STORIE_DURATION_IMG = 7000L;
    public static final String URL_STORIE = "URL_STORIE_IMAGE";
    public static final String STORIE_UNIQUE = "unico_storie";
    public static final String STORIES_LIST_CODIFICATED = "lista_storie_decodificated";

    // TAG TU VOZ ONLINE
    public static final String QUESTION_PRO_URL = "QUESTIONPROURL";

    //TAG BANNER
    public static final String TAG_BANNER_LANZAMIENTO = "BAN_LANZAMIENTO_RESUMEN";
    public static final String REDIRECTION = "Redireccion";

    //TAG GANA EN UN CLICK
    public static final String TAG_GANA_EN_CLICK = "GANA_EN_UN_CLICK";

    //TAG MEDIDOR DE SUENIO
    public static final String TAG_MEDIDOR_DE_SUENIO = "DREAM_METER";

    // TAG RECEPCION DE PEDIDO POR TERCEROS

    public static final String TAG_PEDIDO_POR_TERCEROS = "RECEPCION_PEDIDO_POR_TERCEROS";

    //TAG CONFIGURACION DE BRAND PARA PARA RELANZAMIENTO
    public static final String TAG_BRANDING_CONFIG = "BRANDING_APP";
    public static final String TAG_VIDEO_HOME = "VIDEO_HOME";
    public static final String TAG_VIDEO_NAVIDAD = "VIDEO_NAVIDAD";
    public static final String TAG_BRANDING_CONFIG_HOME = "BRAND_HOME";

    public static final String TAG_BRANDING_CONFIG_ORDER = "BRAND_PEDIDO";

    public static final String TAG_BRANDING_CONFIG_HEADER_HOME = "BRAND_HEADER";

    public static final String TAG_BRANDING_CONFIG_ORDER_BAR = "BRAND_BAR";

    public static final String TAG_BRANDING_CONFIG_ORDER_COLOR_TEXT = "BRAND_PEDIDO_COLOR_TXT";

    //Camino Brillante Analytics
    public static final String EVENT_LABEL_VER_BENEFICIOS = "Ver Beneficios";
    public static final String EVENT_ACTION_SELECCION_OFERTAS_ESPECIALES = "Selección: Ofertas Especiales";
    public static final String EVENT_ACTION_VER_POP_UP_NIVEL = "Ver Pop-up del nivel";
    public static final String EVENT_ACTION_CERRAR_POP_UP_NIVEL = "Cerrar Pop-up del nivel";
    public static final String EVENT_ACTION_SELECCIONAR_NIVEL = "Seleccionar nivel";

    //Elije tu regalo Analytics
    public static final String EVENT_LABEL_VER_REGALOS = "Ver regalos";
    public static final String EVENT_NOMBRE_LISTA = "Pedido | Elige tu regalo";


    //SCREENS
    public static final String SCREEN_NIVEL_Y_BENEFICIOS = Constant.BRAND_FOCUS_NAME + " | Nivel y Beneficios";
    public static final String SCREEN_NIVEL_Y_BENEFICIOS_POPUP = Constant.BRAND_FOCUS_NAME + " | Nivel y Beneficios | Pop-up nivel";
    public static final String SCREEN_NIVEL_Y_BENEFICIOS_PRODUCTOS = Constant.BRAND_FOCUS_NAME + " | Nivel y Beneficios | Productos";
    public static final String SCREEN_NIVEL_Y_BENEFICIOS_Y_LOGRO_START = Constant.BRAND_FOCUS_NAME + " | Nivel y Beneficios | ";
    public static final String SCREEN_NIVEL_Y_BENEFICIOS_Y_LOGRO_END = Constant.BRAND_FOCUS_NAME + " | Detalle ";
    public static final String SCREEN_NIVEL_Y_BENEFICIOS_MIS_LOGROS = Constant.BRAND_FOCUS_NAME + " | Nivel y beneficios – Mis logros";
    public static final String SCREEN_NIVEL_Y_BENEFICIOS_MIS_BENEFICIOS = Constant.BRAND_FOCUS_NAME + " | Nivel y beneficios | Mis beneficios de nivel";

    //TAGS
    public static final String VER_BENEFICIOS = "select_benefits";
    public static final String SELECT_LEVEL = "select_level";
    public static final String VIEW_POPUP_NIVEL = "view_popup_nivel";
    public static final String CLOSE_POPUP_NIVEL = "close_popup_nivel";
    public static final String SELECT_OFERTAS_ESPECIALES = "select_ofertas_especiales";
    public static final String SELECT_UNDERSCORE = "Select_";
    public static final String SELECT_COMPROMISO = "select_compromiso";
    public static final String SELECT_COMO_LOGRARLO = "Select_como_lograrlo";
    public static final String VIEW_POPUP_LOGROS = "view_popup_logros";
    public static final String CLOSE_POPUP_LOGROS = "close_popup_logros";
    public static final String LIST_DEMOSTRADORES = "Demostradores - Nueva Propuesta - Demostradores";
    public static final String LIST_KITS = "Demostradores – Nueva Propuesta – Kit";
    public static final String SELECCION_COMO_LOGRARLO = "Selección: ¿Cómo lograrlo?";
    public static final String VER_DETALLE_COMO_LOGRARLO = "Ver Detalle ¿Cómo lograrlo?";
    public static final String CERRAR_DETALLE_COMO_LOGRARLO = "Cerrar Detalle ¿Cómo lograrlo?";
    public static final String SELECT_ENTENDIDO = "Seleccionar Entendido";
    public static final String POPUP_COMPARTIR_CATALOGO = "POPUP_COMP_CATALOGO";

    // Upselling
    public static final String UP_SELLING = "Upselling";
    public static final String CROSS_SELLING = "Crosselling";
    public static final String SUGERIDOS = "Sugeridos";
    public static final String CARRUSEL = "Carrusel";

    public static final String SUBSECCION_NULL = "null";

    public static final String CARRUSEL_UP_SELLING = "Ficha | Carrusel Upselling";
    public static final String FICHA_UP_SELLING = "Ficha | Ficha Upselling";

    public static final String FROM_OPEN_ACTIVITY = "FROM_OPEN_ACTIVITY";

    public static final String ASSETS_WEBVIEW_URL = "file:///android_asset/";
    public static final String WEB_MIME_TYPE = "text/html";
    public static final String WEB_ENCODING = "utf-8";

    public static final int CANTIDAD_INFO_CAMPANIAS_ANTERIORES = 6;
    public static final int CANTIDAD_INFO_CAMPANIAS_FUTURAS = 6;

    //ANALITYCS SURVEY
    public static final String SURVEY_ANALTICS_ACTION_PANTALLA_VISTA = "screen_views";
    public static final String SURVEY_ANALTICS_ACTION_PANTALLA_SELECTION_BUTTON = "virtualEvent";

    public static final String SURVEY_ANALTICS_ENCUESTA_SB = "Encuesta SB";
    public static final String SURVEY_CLICK_CALIFICACION = "clic en calificacion";
    public static final String SURVEY_ANALTICS_ENCUESTA_NOMBRE_PANTALLA1 = Constant.BRAND_FOCUS_NAME + " | Encuesta SB";
    public static final String SURVEY_ANALTICS_ENCUESTA_NOMBRE_PANTALLA = Constant.BRAND_FOCUS_NAME + " | Encuesta SB";
    public static final String SURVEY_ANALTICS_ENCUESTA_AMBIENTE = BuildConfig.BUILD_TYPE;
    public static final String SURVEY_ANALTICS_CALIFICACION_NOMBRE_PANTALLA = Constant.BRAND_FOCUS_NAME + " | Encuesta SB | ";
    public static final String SURVEY_ANALTICS_CALIFICACION_COMPLETADO = Constant.BRAND_FOCUS_NAME + " | Encuesta SB | Completada Satisfactoriamente";
    public static final String SURVEY_ANALTICS_CALIFICACION_CLIC_EN_CONFIRMAR = "Clic en Confirmar";

    public static final String SURVEY_ANALTICS_CALIFICACION_MIS_PEDIDOS = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_MIS_PEDIDOS;
    public static final String SURVEY_ANALTICS_CALIFICACION_IR_ENCUESTA = "Ir a encuesta";

    //ANALYTICSS INFORMACION DE CAMPANIAS
    public static final String INFO_CAMPANIAS_ACTION_PANTALLA_VISTA = "screen_views";
    public static final String INFO_CAMPANIAS_ACTION_MENU_LATERAL_NAVIGATION = "menu_lateral_navigation";
    public static final String INFO_CAMPANIAS_ACTION_TAB_NAVIGATION = "virtualEvent";

    public static final String INFO_CAMPANIAS_AMBIENTE = BuildConfig.BUILD_TYPE;
    public static final String INFO_CAMPANIAS_PANTALLA_VISTA = Constant.BRAND_FOCUS_NAME + " | Fecha de facturacion";
    public static final String INFO_CAMPANIAS_NOMBRE_PANTALLA = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_HOME;
    public static final String INFO_CAMPANIAS_MENU_LATERAL = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_MENU_LATERAL;
    public static final String INFO_CAMPANIAS_ACTION_MENU_LATERAL = "Ver opción";
    public static final String INFO_CAMPANIAS_LABEL_MENU_LATERAL = "Fechas de Campaña";
    public static final String INFO_CAMPANIAS_ACTION_TAB = "clic en tab";

    //ANALYTICS TU VOZ ONLINE
    public static final String TU_VOZ_ONLINE_MENU_LATERAL_ACTION = "menu_lateral_navigation";

    public static final String TU_VOZ_ONLINE_AMBIENTE = BuildConfig.BUILD_TYPE;
    public static final String TU_VOZ_ONLINE_ACTION = "Ver opcion";
    public static final String TU_VOZ_ONLINE_CATEGORY = BuildConfig.BUILD_TYPE + SPACE + TABULAR_MENU_LATERAL;
    public static final String TU_VOZ_ONLINE_LABEL = "Tu voz online";
    public static final String TU_VOZ_ONLINE_SCREEN_NAME = BuildConfig.BUILD_TYPE + SPACE + TABULAR_HOME;

    //ESCANNER QR
    public static final String SCANNER_QR_MENU_LATERAL_CATEGORY = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_MENU_LATERAL;
    public static final String SCANNER_QR_MENU_LATERAL_ACTION = "Ver opción";
    public static final String SCANNER_QR_MENU_LATERAL_LABEL = "Escaner QR";
    public static final String SCANNER_QR_MENU_LATERAL_SCREEN_NAME = Constant.BRAND_FOCUS_NAME + SPACE + TABULAR_HOME;
    public static final String SCANNER_QR_MENU_LATERAL_AMBIENTE = BuildConfig.BUILD_TYPE;
    public static final String SCANNER_QR_MENU_LATERAL_EVENT = "menú_lateral_navigation";

    //CONFERENCIA DIGITAL
    public static final String CONFERENCIA_DIGITAL_SCREEN_NAME = Constant.BRAND_FOCUS_NAME + " | Conferencia Digital";
    public static final String CONFERENCIA_DIGITAL_AMBIENTE = BuildConfig.BUILD_TYPE;

    public static final String CONFERENCIA_DIGITAL_ACTION_START_VIDEO = "Iniciar Video";
    public static final String CONFERENCIA_DIGITAL_ACTION_END_VIDEO = "Finalizar Video";
    public static final String CONFERENCIA_DIGITAL_ACTION_VIRTUAL_EVENT = "virtualEvent";
    public static final String CONFERENCIA_DIGITAL_VIDEO = "video";
    public static final String CONFERENCIA_DIGITAL_SCREEN_VIEWS = "screen_views";

    //GALLERY
    public static final String GALLERY_SCREEN_NAME = Constant.BRAND_FOCUS_NAME + " | Decora tu Navidad | ";
    public static final String GALLERY_TAB_NAME = Constant.BRAND_FOCUS_NAME + " | Decora tu Navidad";
    public static final String GALLERY_MENU_LATERAL = Constant.BRAND_FOCUS_NAME + " | Menú Lateral";
    public static final String GALLERY_ACTION = "Ver opción";
    public static final String GALLERY_LABEL = "Decora tu Navidad";
    public static final String GALLERY_SCREEN_NAME_HOME = Constant.BRAND_FOCUS_NAME + " | Home";
    public static final String GALLERY_ENVIRONTMENT = BuildConfig.BUILD_TYPE;
    public static final String GALLERY_MENU_LATERAL_NAVIGATION = "menú_lateral_navigation";
    public static final String GALLERY_DECORA_TU_NAVIDAD = "Decora tu Navidad";
    public static final String GALLERY_APLICAR_FILTROS = "Aplicar Filtros";
    public static final String GALLERY_LIMPIAR_FILTROS = "Limpiar Filtros";
    public static final String GALLERY_CLICK_IMAGEN = "clic en imagen";
    public static final String GALLERY_CLICK_FILTRO = "clic en filtros";
    public static final String GALLERY_NOT_AVAILABLE = "(not available)";
    public static final String GALLERY_CLICK_BOTON = "clic Boton";
    public static final String GALLERY_GUARDAR_IMAGEN = "Guardar imagen";
    public static final String GALLERY_COMPARTIR_IMAGEN = "Compartir Imagen";
    public static final String GALLERY_NAME_POP_UP = Constant.BRAND_FOCUS_NAME + " | Decora tu navidad | Pop up";
    public static final String GALLERY_NAME_SIN_CONEXION = Constant.BRAND_FOCUS_NAME + " | Decora tu navidad | Sin conexion";
    public static final String GALLERY_TITULO_IMAGE_SELECTED = "bundle_params_titulo_imagen";

    // A/B Testing
    public static final String TESTING_KEY_FLAG_HIDE_VIEW = "flag_hide_views_for_testing";

    //
    public static final String TYPE_OFFER_FESTIVAL = "FES";


    // PROMOCION
    public static final String FICHA_PROMOCION = "Ficha - promoción";
    public static final String POSITION = "POSITION";
}

let mainConfig = require("./../features/index");
const byId = "#" + mainConfig.appPackage;

config = {
    appPackage: mainConfig.appPackage,
    locator: {
        //fieldPaisLogin: byId+ ':id/spinnerPaises',
        fieldPaisLogin: byId + ":id/rlt_country",
        selectPaisLogin: {
            Bolivia:
                "//android.widget.ListView/android.widget.RelativeLayout[1]",
            Chile:
                "//android.widget.ListView[1]/android.widget.RelativeLayout[2]",
            Colombia:
                "//android.widget.ListView[1]/android.widget.RelativeLayout[3]",
            Ecuador:
                "//android.widget.ListView[1]/android.widget.RelativeLayout[4]",
            ElSalvador:
                "//android.widget.ListView[1]/android.widget.RelativeLayout[5]",
            Guatemala:
                "//android.widget.ListView[1]/android.widget.RelativeLayout[6]",
            Peru: "//android.widget.ListView/android.widget.RelativeLayout[7]",
            RepDominicana:
                "//android.widget.ListView[1]/android.widget.RelativeLayout[8]",
            CostaRica:
                "//android.widget.ListView/android.widget.RelativeLayout[1]",
            Mexico:
                "//android.widget.ListView[1]/android.widget.RelativeLayout[2]",
            Panama:
                "//android.widget.ListView[1]/android.widget.RelativeLayout[3]",
            PuertoRico:
                "//android.widget.ListView[1]/android.widget.RelativeLayout[4]"
        },
        fieldUsuario: byId + ":id/tie_username",
        fieldPassword: byId + ":id/tie_password",
        clickIngresar: byId + ":id/btn_login",
        ClickOmitir: byId + ":id/btn_tutorial_omitir",
        ClickOmitirPopup: byId + ":id/tvw_cancelar",
        ClickCerrarNavegarGratis: byId + ":id/ivwClose",

        //Vinculacion como Consultora
        TxtTituloVinc: byId + ":id/tvwTitle",
        ClickAceptaVincula: byId + ":id/btnAccept",
        //ClickAceptaVinculacion:byId+':id/btn_aceptar',

        //TÉRMINOS Y CONDICIONES DE USO DE ESIKA REVIEW CONMIGO
        TxtTitulo: byId + ":id/tvw_toolbar_title",
        Txtsubtitulo: byId + ":id/tvw_terms_subtitle",
        ClickCheck1: byId + ":id/chk_terms_accept",
        ClickCheck2: byId + ":id/chk_privacy_accept",
        ClickAceptar: byId + ":id/btn_terms_aceptar",

        //Dialogo
        ClickBtnBienvenida: byId + ":id/btnDialog"
    }
};

exports.config = config;
exports.locator = config.locator;

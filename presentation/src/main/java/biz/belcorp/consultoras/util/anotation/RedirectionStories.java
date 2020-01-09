package biz.belcorp.consultoras.util.anotation;


import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    RedirectionStories.HOME,
    RedirectionStories.BONIFICACION,
    RedirectionStories.CLIENTES,
    RedirectionStories.PASE_PEDIDO,
    RedirectionStories.MI_ACADEMIA,
    RedirectionStories.TVO,
    RedirectionStories.ACTUALIZACION_DATOS,
    RedirectionStories.GANA_MAS,
    RedirectionStories.GANA_MAS_ODD,
    RedirectionStories.GANA_MAS_SR,
    RedirectionStories.GANA_MAS_MG,
    RedirectionStories.GANA_MAS_OPT,
    RedirectionStories.GANA_MAS_RD,
    RedirectionStories.GANA_MAS_HV,
    RedirectionStories.GANA_MAS_DP,
    RedirectionStories.GANA_MAS_PN,
    RedirectionStories.GANA_MAS_ATP,
    RedirectionStories.GANA_MAS_LAN,
    RedirectionStories.GANA_MAS_OPM,
    RedirectionStories.CHAT
})


public @interface RedirectionStories {
    String HOME = "Home";
    String BONIFICACION = "VM_BONI";
    String CLIENTES = "VM_CLIE";
    String PASE_PEDIDO = "VM_PASE";
    String MI_ACADEMIA = "VM_MIAC";
    String TVO = "VM_TUVO";
    String ACTUALIZACION_DATOS = "VM_ACTU";
    String GANA_MAS = "VM_GANA";
    String GANA_MAS_ODD = "VM_GANA_ODD";
    String GANA_MAS_SR = "VM_GANA_SR";
    String GANA_MAS_MG = "VM_GANA_MG";
    String GANA_MAS_OPT = "VM_GANA_OPT";
    String GANA_MAS_RD = "VM_GANA_RD";
    String GANA_MAS_HV = "VM_GANA_HV";
    String GANA_MAS_DP = "VM_GANA_DP";
    String GANA_MAS_PN = "VM_GANA_PN";
    String GANA_MAS_ATP = "VM_GANA_ATP";
    String GANA_MAS_LAN = "VM_GANA_LAN";
    String GANA_MAS_OPM = "VM_GANA_OPM";
    String CHAT = "VM_CHAT";
    String CONFERENCIA_DIGITAL = "VM_CONFERENCIA_DIGITAL";
    String FESTIVALES = "VM_FEST";
}

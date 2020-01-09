package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import biz.belcorp.consultoras.domain.entity.UpSellingRequest;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    UserConfigAccountCode.RDS,
    UserConfigAccountCode.RDR,
    UserConfigAccountCode.RD,
    UserConfigAccountCode.OFT,
    UserConfigAccountCode.OFTGM,
    UserConfigAccountCode.CDR_EXP,
    UserConfigAccountCode.AO,
    UserConfigAccountCode.SR,
    UserConfigAccountCode.ODD,
    UserConfigAccountCode.DES_NAV,
    UserConfigAccountCode.GND,
    UserConfigAccountCode.SRDI,
    UserConfigAccountCode.HV,
    UserConfigAccountCode.PAYONLINE,
    UserConfigAccountCode.OFR,
    UserConfigAccountCode.SETAGR,
    UserConfigAccountCode.SBLPROLACTIVO,
    UserConfigAccountCode.B_F,
    UserConfigAccountCode.MG,
    UserConfigAccountCode.PN,
    UserConfigAccountCode.DP,
    UserConfigAccountCode.PDF_PAQDOC,
    UserConfigAccountCode.LAN,
    UserConfigAccountCode.INICIORD,
    UserConfigAccountCode.OPT,
    UserConfigAccountCode.INICIO,
    UserConfigAccountCode.CDC,
    UserConfigAccountCode.MMAX,
    UserConfigAccountCode.ADC,
    UserConfigAccountCode.ADC_CELULAR,
    UserConfigAccountCode.DIGITAL_CATALOG,
    UserConfigAccountCode.INTRIGA,
    UserConfigAccountCode.CAMINO_BRILLANTE,
    UserConfigAccountCode.ACTIVA_WHATSAPP,
    UserConfigAccountCode.FUNCIONALIDAD_HOME,
    UserConfigAccountCode.DREAM_METER,
    UserConfigAccountCode.ENCUESTA,
    UserConfigAccountCode.MENU_OFF})
public @interface UserConfigAccountCode {
    String RDS = "RDS";                                     // Revista Digital Suscripción
    String RDR = "RDR";	                                    // Revista Digital Reducida
    String RD = "RD";	                                    // Revista Digital Completa
    String OFT = "OFT";                                     // Oferta Final Tradicional
    String OFTGM = "OFTGM";                                 // Oferta Final Gana Mas
    String CDR_EXP = "CDR-EXP";                             // Tiene CDR Express
    String AO = "AO";	                                    // Asesora Online
    String SR = "SR";                                       // ShowRoom
    String ODD = "ODD";                                     // Oferta del Día
    String DES_NAV = "DES-NAV";	                            // Descargables Navideños
    String GND = "GND";                                     // Guía de Negocio Digitalizada
    String SRDI = "SRDI";	                                // Revista Digital Intriga
    String HV = "HV";                                       // Herramientas de Venta
    String PAYONLINE = "PAYONLINE";                         // Pago en Línea
    String OFR = "OFR";                                     // Oferta Final Regalo
    String SETAGR = "SETAGR";                               // NULL
    String SBLPROLACTIVO = "SBLPROLACTIVO";                 // Activar/Desactivar PROL para SBLite
    String B_F = "B&F";                                     // Buscador y Filtros
    String MG = "MG";                                       // MG - Descripcion
    String PN = "PN";                                       // Packs de Nuevas
    String DP = "DP";                                       // Dúo Perfecto
    String PDF_PAQDOC = "PDF-PAQDOC";	                    // Generacion PDF Paquete Documentario
    String LAN = "LAN";                                     // Lanzamientos
    String INICIORD = "INICIORD";                           // INICIO
    String OPT = "OPT";	                                    // Oferta Para Ti
    String INICIO = "INICIO";                               // Inicio
    String CDC = "CDC";                                     // Captura Datos Consultora
    String MMAX = "MMAX";	                                // Validacion Monto Maximo
    String ADC = "ADC";                                     // Actualización de Datos
    String ADC_CELULAR = "PuedeActualizarCelular";          // Actualización de Datos - Celular
    String DIGITAL_CATALOG = "CATALOGO-DIGITAL";            // Catálogo Digital
    String RECOMDS = "RECOMDS";                             // Recomendaciones
    String INTRIGA = "INTRIGA";                             // Dialogo de intriga
    String CAMINO_BRILLANTE = "CAMINOBRILLANTE";            // Camino Brillante
    String ACTIVA_WHATSAPP = "PUBLI_WHATSAPP";             // activa el check de whatsapp
    String FUNCIONALIDAD_HOME = "FUNCIONALIDAD_HOME";       // Activa la encuesta
    String ENCUESTA = "ENCUESTA";                           // Activa la encuesta
    String CAMINO_BRILLANTE_PUNTAJE_GB = "CAMINOBRILLANTE_PUNTAJE_GB";            // Puntaje Gran Brillante
    String DREAM_METER = "DREAMMETER";                      // Activa dream meter
    String MENU_OFF = "MENU_OFF";                      // Activa opciones de menu
    String ASESOR_REGALOS = "ASESOR-REGALOS";               // Asesor de regalos
}

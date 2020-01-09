package biz.belcorp.consultoras.common.model.auth;

/**
 * Entidad de LoginModel datos devueltos por el servicio
 */

public class LoginModel {

    private String accessToken;
    private String tokenType;
    private int expiresIn;
    private String refreshToken;
    private int countryId;
    private String countryISO;
    private String countryMoneySymbol;
    private int consultantID;
    private String userCode;
    private String consultantCode;
    private String userTest;
    private String campaing;
    private String numberOfCampaings;
    private String regionID;
    private String regionCode;
    private String zoneID;
    private String zoneCode;
    private String consultantName;
    private String alias;
    private String expirationDate;
    private boolean dayProl;
    private int consultantAcceptDA;
    private String urlBelcorpChat;
    private int userType;
    private String billingStartDate;
    private String billingEndDate;
    private String endTime;
    private String timeZone;
    private int closingDays;
    private String email;
    private String phone;
    private String mobile;
    private String hasDayOffer;
    private String consultantAssociateID;
    private String otherPhone;
    private String photoProfile;
    private String issued;
    private String expires;
    private boolean showRoom;
    private boolean aceptaTerminosCondiciones;
    private boolean aceptaPoliticaPrivacidad;
    private String destinatariosFeedback;

    private boolean showBanner;
    private String bannerTitle;
    private String bannerMessage;
    private String bannerUrl;
    private String bannerVinculo;

    private boolean birthday;
    private boolean anniversary;
    private boolean pasoSextoPedido;
    private int revistaDigitalSuscripcion;
    private String bannerGanaMas;
    private boolean tieneGND;
    private int tipoCondicion;
    private boolean zonaValida;
    private String horaFinPortal;
    private boolean esConsultoraOficina;

    private int indicadorContratoCredito;

    private boolean isCambioCorreoPendiente;
    private String correoPendiente;
    private String primerNombre;
    private boolean isPuedeActualizar;
    private boolean isPuedeActualizarEmail;
    private boolean isPuedeActualizarCelular;

    private boolean isMostrarBuscador;
    private int caracteresBuscador;
    private int caracteresBuscadorMostrar;
    private int totalResultadosBuscador;
    private int lider;
    private String periodo;
    private String semanaPeriodo;
    private String descripcionNivelLider;
    private boolean isRDEsActiva;
    private boolean isRDEsSuscrita;
    private boolean isRDActivoMdo;
    private boolean isRDTieneRDC;
    private boolean isRDTieneRDI;
    private boolean isRDTieneRDCR;
    private int diaFacturacion;

    private boolean isIndicadorConsultoraDummy;
    private String personalizacionesDummy;
    private String tipoIngreso;
    private String segmentoDatami;
    private int indicadorConsultoraDigital;

    private String segmentoConstancia;
    private String codigoSeccion;
    private int checkEnviarWhatsaap;
    private int  showCheckWhatsapp;
    private int showActualizacionDatos;

    private boolean isCambioCelularPendiente;
    private String celularPendiente;

    public LoginModel(){
        // EMPTY
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryISO() {
        return countryISO;
    }

    public void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
    }

    public String getCountryMoneySymbol() {
        return countryMoneySymbol;
    }

    public void setCountryMoneySymbol(String countryMoneySymbol) {
        this.countryMoneySymbol = countryMoneySymbol;
    }

    public int getConsultantID() {
        return consultantID;
    }

    public void setConsultantID(int consultantID) {
        this.consultantID = consultantID;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getUserTest() {
        return userTest;
    }

    public void setUserTest(String userTest) {
        this.userTest = userTest;
    }

    public String getCampaing() {
        return campaing;
    }

    public void setCampaing(String campaing) {
        this.campaing = campaing;
    }

    public String getNumberOfCampaings() {
        return numberOfCampaings;
    }

    public void setNumberOfCampaings(String numberOfCampaings) {
        this.numberOfCampaings = numberOfCampaings;
    }

    public String getRegionID() {
        return regionID;
    }

    public void setRegionID(String regionID) {
        this.regionID = regionID;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isDayProl() {
        return dayProl;
    }

    public void setDayProl(boolean dayProl) {
        this.dayProl = dayProl;
    }

    public int getConsultantAcceptDA() {
        return consultantAcceptDA;
    }

    public void setConsultantAcceptDA(int consultantAcceptDA) {
        this.consultantAcceptDA = consultantAcceptDA;
    }

    public String getUrlBelcorpChat() {
        return urlBelcorpChat;
    }

    public void setUrlBelcorpChat(String urlBelcorpChat) {
        this.urlBelcorpChat = urlBelcorpChat;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getBillingStartDate() {
        return billingStartDate;
    }

    public void setBillingStartDate(String billingStartDate) {
        this.billingStartDate = billingStartDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public int getClosingDays() {
        return closingDays;
    }

    public void setClosingDays(int closingDays) {
        this.closingDays = closingDays;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHasDayOffer() {
        return hasDayOffer;
    }

    public void setHasDayOffer(String hasDayOffer) {
        this.hasDayOffer = hasDayOffer;
    }

    public String getConsultantAssociateID() {
        return consultantAssociateID;
    }

    public void setConsultantAssociateID(String consultantAssociateID) {
        this.consultantAssociateID = consultantAssociateID;
    }

    public String getOtherPhone() {
        return otherPhone;
    }

    public void setOtherPhone(String otherPhone) {
        this.otherPhone = otherPhone;
    }

    public String getPhotoProfile() {
        return photoProfile;
    }

    public void setPhotoProfile(String photoProfile) {
        this.photoProfile = photoProfile;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public boolean isShowRoom() {
        return showRoom;
    }

    public void setShowRoom(boolean showRoom) {
        this.showRoom = showRoom;
    }

    public boolean isAceptaTerminosCondiciones() {
        return aceptaTerminosCondiciones;
    }

    public void setAceptaTerminosCondiciones(boolean aceptaTerminosCondiciones) {
        this.aceptaTerminosCondiciones = aceptaTerminosCondiciones;
    }

    public boolean isAceptaPoliticaPrivacidad() {
        return aceptaPoliticaPrivacidad;
    }

    public void setAceptaPoliticaPrivacidad(boolean aceptaPoliticaPrivacidad) {
        this.aceptaPoliticaPrivacidad = aceptaPoliticaPrivacidad;
    }

    public String getDestinatariosFeedback() {
        return destinatariosFeedback;
    }

    public void setDestinatariosFeedback(String destinatariosFeedback) {
        this.destinatariosFeedback = destinatariosFeedback;
    }

    public boolean isShowBanner() {
        return showBanner;
    }

    public void setShowBanner(boolean showBanner) {
        this.showBanner = showBanner;
    }

    public String getBannerTitle() {
        return bannerTitle;
    }

    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    public String getBannerMessage() {
        return bannerMessage;
    }

    public void setBannerMessage(String bannerMessage) {
        this.bannerMessage = bannerMessage;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getBannerVinculo() {
        return bannerVinculo;
    }

    public void setBannerVinculo(String bannerVinculo) {
        this.bannerVinculo = bannerVinculo;
    }

    public boolean isBirthday() {
        return birthday;
    }

    public void setBirthday(boolean birthday) {
        this.birthday = birthday;
    }

    public boolean isAnniversary() {
        return anniversary;
    }

    public void setAnniversary(boolean anniversary) {
        this.anniversary = anniversary;
    }

    public boolean isPasoSextoPedido() {
        return pasoSextoPedido;
    }

    public void setPasoSextoPedido(boolean pasoSextoPedido) {
        this.pasoSextoPedido = pasoSextoPedido;
    }

    public int getRevistaDigitalSuscripcion() {
        return revistaDigitalSuscripcion;
    }

    public void setRevistaDigitalSuscripcion(int revistaDigitalSuscripcion) {
        this.revistaDigitalSuscripcion = revistaDigitalSuscripcion;
    }

    public String getBannerGanaMas() {
        return bannerGanaMas;
    }

    public void setBannerGanaMas(String bannerGanaMas) {
        this.bannerGanaMas = bannerGanaMas;
    }

    public boolean isTieneGND() {
        return tieneGND;
    }

    public void setTieneGND(boolean tieneGND) {
        this.tieneGND = tieneGND;
    }

    public int getTipoCondicion() {
        return tipoCondicion;
    }

    public void setTipoCondicion(int tipoCondicion) {
        this.tipoCondicion = tipoCondicion;
    }

    public boolean isZonaValida() {
        return zonaValida;
    }

    public void setZonaValida(boolean zonaValida) {
        this.zonaValida = zonaValida;
    }

    public String getBillingEndDate() {
        return billingEndDate;
    }

    public void setBillingEndDate(String billingEndDate) {
        this.billingEndDate = billingEndDate;
    }

    public String getHoraFinPortal() {
        return horaFinPortal;
    }

    public void setHoraFinPortal(String horaFinPortal) {
        this.horaFinPortal = horaFinPortal;
    }

    public boolean isEsConsultoraOficina() {
        return esConsultoraOficina;
    }

    public void setEsConsultoraOficina(boolean esConsultoraOficina) {
        this.esConsultoraOficina = esConsultoraOficina;
    }

    public int getIndicadorContratoCredito() {
        return indicadorContratoCredito;
    }

    public void setIndicadorContratoCredito(int indicadorContratoCredito) {
        this.indicadorContratoCredito = indicadorContratoCredito;
    }

    public boolean isCambioCorreoPendiente() {
        return isCambioCorreoPendiente;
    }

    public void setCambioCorreoPendiente(boolean cambioCorreoPendiente) {
        isCambioCorreoPendiente = cambioCorreoPendiente;
    }

    public String getCorreoPendiente() {
        return correoPendiente;
    }

    public void setCorreoPendiente(String correoPendiente) {
        this.correoPendiente = correoPendiente;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public boolean isPuedeActualizar() {
        return isPuedeActualizar;
    }

    public void setPuedeActualizar(boolean puedeActualizar) {
        isPuedeActualizar = puedeActualizar;
    }

    public boolean isPuedeActualizarEmail() {
        return isPuedeActualizarEmail;
    }

    public void setPuedeActualizarEmail(boolean puedeActualizarEmail) {
        isPuedeActualizarEmail = puedeActualizarEmail;
    }

    public boolean isPuedeActualizarCelular() {
        return isPuedeActualizarCelular;
    }

    public void setPuedeActualizarCelular(boolean puedeActualizarCelular) {
        isPuedeActualizarCelular = puedeActualizarCelular;
    }

    public boolean isMostrarBuscador() {
        return isMostrarBuscador;
    }

    public void setMostrarBuscador(boolean mostrarBuscador) {
        isMostrarBuscador = mostrarBuscador;
    }

    public int getCaracteresBuscador() {
        return caracteresBuscador;
    }

    public void setCaracteresBuscador(int caracteresBuscador) {
        this.caracteresBuscador = caracteresBuscador;
    }

    public int getCaracteresBuscadorMostrar() {
        return caracteresBuscadorMostrar;
    }

    public void setCaracteresBuscadorMostrar(int caracteresBuscadorMostrar) {
        this.caracteresBuscadorMostrar = caracteresBuscadorMostrar;
    }

    public int getTotalResultadosBuscador() {
        return totalResultadosBuscador;
    }

    public void setTotalResultadosBuscador(int totalResultadosBuscador) {
        this.totalResultadosBuscador = totalResultadosBuscador;
    }

    public int getLider() {
        return lider;
    }

    public void setLider(int lider) {
        this.lider = lider;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getSemanaPeriodo() {
        return semanaPeriodo;
    }

    public void setSemanaPeriodo(String semanaPeriodo) {
        this.semanaPeriodo = semanaPeriodo;
    }

    public String getDescripcionNivelLider() {
        return descripcionNivelLider;
    }

    public void setDescripcionNivelLider(String descripcionNivelLider) {
        this.descripcionNivelLider = descripcionNivelLider;
    }

    public boolean isRDEsActiva() {
        return isRDEsActiva;
    }

    public void setRDEsActiva(boolean RDEsActiva) {
        isRDEsActiva = RDEsActiva;
    }

    public boolean isRDEsSuscrita() {
        return isRDEsSuscrita;
    }

    public void setRDEsSuscrita(boolean RDEsSuscrita) {
        isRDEsSuscrita = RDEsSuscrita;
    }

    public boolean isRDActivoMdo() {
        return isRDActivoMdo;
    }

    public void setRDActivoMdo(boolean RDActivoMdo) {
        isRDActivoMdo = RDActivoMdo;
    }

    public boolean isRDTieneRDC() {
        return isRDTieneRDC;
    }

    public void setRDTieneRDC(boolean RDTieneRDC) {
        isRDTieneRDC = RDTieneRDC;
    }

    public boolean isRDTieneRDI() {
        return isRDTieneRDI;
    }

    public void setRDTieneRDI(boolean RDTieneRDI) {
        isRDTieneRDI = RDTieneRDI;
    }

    public boolean isRDTieneRDCR() {
        return isRDTieneRDCR;
    }

    public void setRDTieneRDCR(boolean RDTieneRDCR) {
        isRDTieneRDCR = RDTieneRDCR;
    }

    public int getDiaFacturacion() {
        return diaFacturacion;
    }

    public void setDiaFacturacion(int diaFacturacion) {
        this.diaFacturacion = diaFacturacion;
    }

    public boolean isIndicadorConsultoraDummy() {
        return isIndicadorConsultoraDummy;
    }

    public void setIndicadorConsultoraDummy(boolean indicadorConsultoraDummy) {
        isIndicadorConsultoraDummy = indicadorConsultoraDummy;
    }

    public String getPersonalizacionesDummy() {
        return personalizacionesDummy;
    }

    public void setPersonalizacionesDummy(String personalizacionesDummy) {
        this.personalizacionesDummy = personalizacionesDummy;
    }

    public String getTipoIngreso() {
        return tipoIngreso;
    }

    public void setTipoIngreso(String tipoIngreso) {
        this.tipoIngreso = tipoIngreso;
    }

    public String getSegmentoDatami() {
        return segmentoDatami;
    }

    public void setSegmentoDatami(String segmentoDatami) {
        this.segmentoDatami = segmentoDatami;
    }

    public int getIndicadorConsultoraDigital() {
        return indicadorConsultoraDigital;
    }

    public void setIndicadorConsultoraDigital(int indicadorConsultoraDigital) {
        this.indicadorConsultoraDigital = indicadorConsultoraDigital;
    }

    public String getSegmentoConstancia() { return segmentoConstancia; }

    public void setSegmentoConstancia(String segmentoConstancia) {
        this.segmentoConstancia = segmentoConstancia;
    }

    public String getCodigoSeccion() { return codigoSeccion; }

    public void setCodigoSeccion(String codigoSeccion) {
        this.codigoSeccion = codigoSeccion;
    }

    public int getCheckEnviarWhatsaap() {
        return checkEnviarWhatsaap;
    }

    public void setCheckEnviarWhatsaap(int checkEnviarWhatsaap) {
        this.checkEnviarWhatsaap = checkEnviarWhatsaap;
    }

    public int getShowCheckWhatsapp() {
        return showCheckWhatsapp;
    }

    public void setShowCheckWhatsapp(int showCheckWhatsapp) {
        this.showCheckWhatsapp = showCheckWhatsapp;
    }

    public int getShowActualizacionDatos() {
        return showActualizacionDatos;
    }

    public void setShowActualizacionDatos(int showActualizacionDatos) {
        this.showActualizacionDatos = showActualizacionDatos;
    }

    public boolean isCambioCelularPendiente() {
        return isCambioCelularPendiente;
    }

    public void setCambioCelularPendiente(boolean cambioCelularPendiente) {
        isCambioCelularPendiente = cambioCelularPendiente;
    }

    public String getCelularPendiente() {
        return celularPendiente;
    }

    public void setCelularPendiente(String celularPendiente) {
        this.celularPendiente = celularPendiente;
    }
}

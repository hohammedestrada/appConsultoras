package biz.belcorp.consultoras.common.model.user;

public class UserModel {

    private int countryId;
    private String countryISO;
    private String countryMoneySymbol;
    private int countryShowDecimal;

    private String consultantId;
    private String consultantUserCode;
    private String consultantCode;
    private String consultantAssociateId;
    private String userCode;
    private boolean userTest;
    private int userType;
    private String consultantName;
    private String alias;
    private String email;
    private String phone;
    private String otherPhone;
    private String mobile;
    private String photoProfile;

    private String campaing;
    private String numberOfCampaings;
    private String regionID;
    private String regionCode;
    private String zoneID;
    private String zoneCode;

    private String expirationDate;
    private boolean dayProl;
    private int consultantAcceptDA;
    private String urlBelcorpChat;
    private String billingStartDate;
    private String billingEndDate;
    private String endTime;
    private String timeZone;
    private int closingDays;
    private boolean hasDayOffer;
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
    private String codigoSeccion;
    private int tipoCondicion;
    private int zonaValida;
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
    private boolean isPagoEnLinea;
    private boolean isChatBot;
    private int isNotificacionesWhatsapp = -1;
    private int isCheckWhatsapp = -1;
    private boolean isCambioCelularPendiente;
    private String celularPendiente;
    private int esBrillante = -1;
    private boolean isMultipedido;
    private String lineaConsultora;

    public UserModel() {
        // EMPTY
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

    public int getCountryShowDecimal() {
        return countryShowDecimal;
    }

    public void setCountryShowDecimal(int countryShowDecimal) {
        this.countryShowDecimal = countryShowDecimal;
    }

    public String getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(String consultantId) {
        this.consultantId = consultantId;
    }

    public String getConsultantUserCode() {
        return consultantUserCode;
    }

    public void setConsultantUserCode(String consultantUserCode) {
        this.consultantUserCode = consultantUserCode;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getConsultantAssociateId() {
        return consultantAssociateId;
    }

    public void setConsultantAssociateId(String consultantAssociateId) {
        this.consultantAssociateId = consultantAssociateId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public boolean isUserTest() {
        return userTest;
    }

    public void setUserTest(boolean userTest) {
        this.userTest = userTest;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
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

    public String getOtherPhone() {
        return otherPhone;
    }

    public void setOtherPhone(String otherPhone) {
        this.otherPhone = otherPhone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhotoProfile() {
        return photoProfile;
    }

    public void setPhotoProfile(String photoProfile) {
        this.photoProfile = photoProfile;
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

    public boolean isHasDayOffer() {
        return hasDayOffer;
    }

    public void setHasDayOffer(boolean hasDayOffer) {
        this.hasDayOffer = hasDayOffer;
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

    public int getZonaValida() {
        return zonaValida;
    }

    public void setZonaValida(int zonaValida) {
        this.zonaValida = zonaValida;
    }

    public String getCodigoSeccion() {
        return codigoSeccion;
    }

    public void setCodigoSeccion(String codigoSeccion) {
        this.codigoSeccion = codigoSeccion;
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

    public boolean isPagoEnLinea() {
        return isPagoEnLinea;
    }

    public void setPagoEnLinea(boolean pagoEnLinea) {
        isPagoEnLinea = pagoEnLinea;
    }

    public boolean isChatBot() {
        return isChatBot;
    }

    public void setChatBot(boolean chatBot) {
        isChatBot = chatBot;
    }

    public int getIsNotificacionesWhatsapp() {
        return isNotificacionesWhatsapp;
    }

    public void setIsNotificacionesWhatsapp(int isNotificacionesWhatsapp) {
        this.isNotificacionesWhatsapp = isNotificacionesWhatsapp;
    }

    public int getIsCheckWhatsapp() {
        return isCheckWhatsapp;
    }

    public void setIsCheckWhatsapp(int isCheckWhatsapp) {
        this.isCheckWhatsapp = isCheckWhatsapp;
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

    public int getEsBrillante(){
        return esBrillante;
    }

    public void setEsBrillante(int esBrillante){
        this.esBrillante = esBrillante;
    }

    public boolean isMultipedido() {
        return isMultipedido;
    }

    public void setMultipedido(boolean multipedido) {
        isMultipedido = multipedido;
    }

    public String getLineaConsultora() {
        return lineaConsultora;
    }

    public void setLineaConsultora(String lineaConsultora) {
        this.lineaConsultora = lineaConsultora;
    }
}

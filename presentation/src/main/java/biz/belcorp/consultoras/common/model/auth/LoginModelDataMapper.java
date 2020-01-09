package biz.belcorp.consultoras.common.model.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Login;
import biz.belcorp.consultoras.domain.entity.User;

/**
 * Clase encarga de realizar el mapeo de la entidad loginOnline(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-14
 */
@PerActivity
public class LoginModelDataMapper {

    @Inject
    LoginModelDataMapper() {
    }

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param input Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    public Login transform(LoginModel input) {
        Login output = null;

        if (null != input) {
            output = new Login();
            output.setAccessToken(input.getAccessToken());
            output.setTokenType(input.getTokenType());
            output.setExpiresIn(input.getExpiresIn());
            output.setRefreshToken(input.getRefreshToken());
            output.setCountryId(input.getCountryId());
            output.setCountryISO(input.getCountryISO());
            output.setCountryMoneySymbol(input.getCountryMoneySymbol());
            output.setConsultantID(input.getConsultantID());
            output.setUserCode(input.getUserCode());
            output.setConsultantCode(input.getConsultantCode());
            output.setUserTest(input.getUserTest());
            output.setCampaing(input.getCampaing());
            output.setNumberOfCampaings(input.getNumberOfCampaings());
            output.setRegionID(input.getRegionID());
            output.setRegionCode(input.getRegionCode());
            output.setZoneID(input.getZoneID());
            output.setZoneCode(input.getZoneCode());
            output.setConsultantName(input.getConsultantName());
            output.setAlias(input.getAlias());
            output.setExpirationDate(input.getExpirationDate());
            output.setDayProl(input.isDayProl());
            output.setConsultantAcceptDA(input.getConsultantAcceptDA());
            output.setUrlBelcorpChat(input.getUrlBelcorpChat());
            output.setUserType(input.getUserType());
            output.setBillingStartDate(input.getBillingStartDate());
            output.setBillingEndDate(input.getBillingEndDate());
            output.setEndTime(input.getEndTime());
            output.setTimeZone(input.getTimeZone());
            output.setClosingDays(input.getClosingDays());
            output.setEmail(input.getEmail());
            output.setPhone(input.getPhone());
            output.setMobile(input.getMobile());
            output.setHasDayOffer(input.getHasDayOffer());
            output.setConsultantAssociateID(input.getConsultantAssociateID());
            output.setOtherPhone(input.getOtherPhone());
            output.setPhotoProfile(input.getPhotoProfile());
            output.setIssued(input.getIssued());
            output.setExpires(input.getExpires());
            output.setShowRoom(input.isShowRoom());
            output.setAceptaTerminosCondiciones(input.isAceptaTerminosCondiciones());
            output.setAceptaPoliticaPrivacidad(input.isAceptaPoliticaPrivacidad());
            output.setDestinatariosFeedback(input.getDestinatariosFeedback());

            output.setShowBanner(input.isShowBanner());
            output.setBannerTitle(input.getBannerTitle());
            output.setBannerMessage(input.getBannerMessage());
            output.setBannerUrl(input.getBannerUrl());
            output.setBannerVinculo(input.getBannerVinculo());

            output.setBirthday(input.isBirthday());
            output.setAnniversary(input.isAnniversary());
            output.setPasoSextoPedido(input.isPasoSextoPedido());

            output.setRevistaDigitalSuscripcion(input.getRevistaDigitalSuscripcion());
            output.setBannerGanaMas(input.getBannerGanaMas());
            output.setTieneGND(input.isTieneGND());
            output.setTipoCondicion(input.getTipoCondicion());
            output.setHoraFinPortal(input.getHoraFinPortal());
            output.setEsConsultoraOficina(input.isEsConsultoraOficina());
            output.setIndicadorContratoCredito(input.getIndicadorContratoCredito());
            output.setTipoIngreso(input.getTipoIngreso());
            output.setSegmentoDatami(input.getSegmentoDatami());
            output.setIndicadorConsultoraDigital(input.getIndicadorConsultoraDigital());
            output.setSegmentoConstancia(input.getSegmentoConstancia());
            output.setActualizacionDatos(input.getShowActualizacionDatos());
            output.setCodigoSeccion(input.getCodigoSeccion());
            output.setCheckEnviarWhatsaap(input.getCheckEnviarWhatsaap());
            output.setShowCheckWhatsapp(input.getShowCheckWhatsapp());
            output.setCambioCelularPendiente(input.isCambioCelularPendiente());
            output.setCelularPendiente(input.getCelularPendiente());
            output.setPeriodo(input.getPeriodo());
            output.setSemanaPeriodo(input.getSemanaPeriodo());
            output.setDescripcionNivelLider(input.getDescripcionNivelLider());
        }
        return output;
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param input Entidad de dominio
     * @return object Entidad
     */
    public LoginModel transform(User input) {
        LoginModel output = null;

        if (null != input) {
            output = new LoginModel();

            output.setCountryId(input.getCountryId());
            output.setCountryISO(input.getCountryISO());
            output.setCountryMoneySymbol(input.getCountryMoneySymbol());
            output.setUserCode(input.getUserCode());
            output.setConsultantCode(input.getConsultantCode());
            output.setCampaing(input.getCampaing());
            output.setNumberOfCampaings(input.getNumberOfCampaings());
            output.setRegionID(input.getRegionID());
            output.setRegionCode(input.getRegionCode());
            output.setZoneID(input.getZoneID());
            output.setZoneCode(input.getZoneCode());
            output.setConsultantName(input.getConsultantName());
            output.setAlias(input.getAlias());
            output.setExpirationDate(input.getExpirationDate());
            output.setDayProl(input.isDayProl());
            output.setConsultantAcceptDA(input.getConsultantAcceptDA());
            output.setUrlBelcorpChat(input.getUrlBelcorpChat());
            output.setUserType(input.getUserType());
            output.setBillingStartDate(input.getBillingStartDate());
            output.setBillingEndDate(input.getBillingEndDate());
            output.setEndTime(input.getEndTime());
            output.setTimeZone(input.getTimeZone());
            output.setClosingDays(input.getClosingDays());
            output.setEmail(input.getEmail());
            output.setPhone(input.getPhone());
            output.setMobile(input.getMobile());
            output.setConsultantAssociateID(input.getConsultantAssociateId());
            output.setOtherPhone(input.getOtherPhone());
            output.setPhotoProfile(input.getPhotoProfile());
            output.setShowRoom(input.isShowRoom());
            output.setAceptaTerminosCondiciones(input.isAceptaTerminosCondiciones());
            output.setAceptaPoliticaPrivacidad(input.isAceptaPoliticaPrivacidad());
            output.setDestinatariosFeedback(input.getDestinatariosFeedback());

            output.setShowBanner(input.isShowBanner());
            output.setBannerTitle(input.getBannerTitle());
            output.setBannerMessage(input.getBannerMessage());
            output.setBannerUrl(input.getBannerUrl());
            output.setBannerVinculo(input.getBannerVinculo());

            output.setBirthday(input.isBirthday());
            output.setAnniversary(input.isAnniversary());

            output.setPasoSextoPedido(input.isPasoSextoPedido());
            output.setRevistaDigitalSuscripcion(input.getRevistaDigitalSuscripcion() != null? input.getRevistaDigitalSuscripcion() : -1);
            output.setBannerGanaMas(input.getBannerGanaMas());
            output.setTieneGND(input.isTieneGND());
            output.setTipoCondicion(input.getTipoCondicion()!= null? input.getTipoCondicion() : -1);
            output.setHoraFinPortal(input.getHoraFinPortal());
            output.setEsConsultoraOficina(input.getEsConsultoraOficina());

            output.setIndicadorContratoCredito(input.getIndicadorContratoCredito() != null? input.getIndicadorContratoCredito(): -1);

            output.setCambioCorreoPendiente(input.isCambioCorreoPendiente() != null ? input.isCambioCorreoPendiente() : false);
            output.setCorreoPendiente(input.getCorreoPendiente());
            output.setPrimerNombre(input.getPrimerNombre());
            output.setPuedeActualizar(input.isPuedeActualizar() != null? input.isPuedeActualizar() : false);
            output.setPuedeActualizarEmail(input.isPuedeActualizarEmail() != null ? input.isPuedeActualizarEmail() : false);
            output.setPuedeActualizarCelular(input.isPuedeActualizarCelular() != null ? input.isPuedeActualizarCelular() : false);

            output.setMostrarBuscador(input.isMostrarBuscador() != null? input.isMostrarBuscador() : false);
            output.setCaracteresBuscador(input.getCaracteresBuscador() != null? input.getCaracteresBuscador(): 0);
            output.setCaracteresBuscadorMostrar(input.getCaracteresBuscadorMostrar() != null ? input.getCaracteresBuscadorMostrar() : 0);
            output.setTotalResultadosBuscador(input.getTotalResultadosBuscador() != null ? input.getTotalResultadosBuscador() : 0);
            output.setLider(input.getLider() != null ? input.getLider() : 0);
            output.setPeriodo(input.getPeriodo());
            output.setSemanaPeriodo(input.getSemanaPeriodo());
            output.setRDEsActiva(input.isRDEsActiva() != null ? input.isRDEsActiva() : false);
            output.setRDEsSuscrita(input.isRDEsSuscrita() != null ? input.isRDEsSuscrita() : false);
            output.setRDActivoMdo(input.isRDActivoMdo() != null ? input.isRDActivoMdo() : false);
            output.setRDTieneRDC(input.isRDTieneRDC() != null ? input.isRDTieneRDC() : false);
            output.setRDTieneRDI(input.isRDTieneRDI() != null ? input.isRDTieneRDI() : false);
            output.setRDTieneRDCR(input.isRDTieneRDCR() != null ? input.isRDTieneRDCR(): false);
            output.setDiaFacturacion(input.getDiaFacturacion() != null ? input.getDiaFacturacion() : 0);
            output.setTipoIngreso(input.getTipoIngreso());
            output.setSegmentoDatami(input.getSegmentoDatami());
            output.setIndicadorConsultoraDigital(input.getIndicadorConsultoraDigital());
            output.setShowActualizacionDatos(input.getActualizacionDatos());
            output.setSegmentoConstancia(input.getSegmentoConstancia());
            output.setCodigoSeccion(input.getCodigoSeccion());
        }
        return output;
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param input Entidad de dominio
     * @return object Entidad
     */
    public LoginModel transform(Login input) {
        LoginModel output = null;

        if (null != input) {
            output = new LoginModel();
            output.setAccessToken(input.getAccessToken());
            output.setTokenType(input.getTokenType());
            output.setExpiresIn(input.getExpiresIn());
            output.setRefreshToken(input.getRefreshToken());
            output.setCountryId(input.getCountryId());
            output.setCountryISO(input.getCountryISO());
            output.setCountryMoneySymbol(input.getCountryMoneySymbol());
            output.setConsultantID(input.getConsultantID());
            output.setUserCode(input.getUserCode());
            output.setConsultantCode(input.getConsultantCode());
            output.setUserTest(input.getUserTest());
            output.setCampaing(input.getCampaing());
            output.setNumberOfCampaings(input.getNumberOfCampaings());
            output.setRegionID(input.getRegionID());
            output.setRegionCode(input.getRegionCode());
            output.setZoneID(input.getZoneID());
            output.setZoneCode(input.getZoneCode());
            output.setConsultantName(input.getConsultantName());
            output.setAlias(input.getAlias());
            output.setExpirationDate(input.getExpirationDate());
            output.setDayProl(input.isDayProl());
            output.setConsultantAcceptDA(input.getConsultantAcceptDA());
            output.setUrlBelcorpChat(input.getUrlBelcorpChat());
            output.setUserType(input.getUserType());
            output.setBillingStartDate(input.getBillingStartDate());
            output.setBillingEndDate(input.getBillingEndDate());
            output.setEndTime(input.getEndTime());
            output.setTimeZone(input.getTimeZone());
            output.setClosingDays(input.getClosingDays());
            output.setEmail(input.getEmail());
            output.setPhone(input.getPhone());
            output.setMobile(input.getMobile());
            output.setHasDayOffer(input.getHasDayOffer());
            output.setConsultantAssociateID(input.getConsultantAssociateID());
            output.setOtherPhone(input.getOtherPhone());
            output.setPhotoProfile(input.getPhotoProfile());
            output.setIssued(input.getIssued());
            output.setExpires(input.getExpires());
            output.setShowRoom(input.isShowRoom());
            output.setAceptaTerminosCondiciones(input.isAceptaTerminosCondiciones());
            output.setAceptaPoliticaPrivacidad(input.isAceptaPoliticaPrivacidad());
            output.setDestinatariosFeedback(input.getDestinatariosFeedback());

            output.setShowBanner(input.isShowBanner());
            output.setBannerTitle(input.getBannerTitle());
            output.setBannerMessage(input.getBannerMessage());
            output.setBannerUrl(input.getBannerUrl());
            output.setBannerVinculo(input.getBannerVinculo());

            output.setBirthday(input.isBirthday());
            output.setAnniversary(input.isAnniversary());
            output.setPasoSextoPedido(input.isPasoSextoPedido());
            output.setRevistaDigitalSuscripcion(input.getRevistaDigitalSuscripcion());
            output.setBannerGanaMas(input.getBannerGanaMas());
            output.setTieneGND(input.isTieneGND());
            output.setTipoCondicion(input.getTipoCondicion());
            output.setHoraFinPortal(input.getHoraFinPortal());
            output.setEsConsultoraOficina(input.getEsConsultoraOficina());

            output.setIndicadorContratoCredito(input.getIndicadorContratoCredito() != null? input.getIndicadorContratoCredito(): -1);

            output.setCambioCorreoPendiente(input.isCambioCorreoPendiente() != null ? input.isCambioCorreoPendiente() : false);
            output.setCorreoPendiente(input.getCorreoPendiente());
            output.setPrimerNombre(input.getPrimerNombre());
            output.setPuedeActualizar(input.isPuedeActualizar() != null? input.isPuedeActualizar() : false);
            output.setPuedeActualizarEmail(input.isPuedeActualizarEmail() != null ? input.isPuedeActualizarEmail() : false);
            output.setPuedeActualizarCelular(input.isPuedeActualizarCelular() != null ? input.isPuedeActualizarCelular() : false);

            output.setMostrarBuscador(input.isMostrarBuscador() != null? input.isMostrarBuscador() : false);
            output.setCaracteresBuscador(input.getCaracteresBuscador() != null? input.getCaracteresBuscador(): 0);
            output.setCaracteresBuscadorMostrar(input.getCaracteresBuscadorMostrar() != null ? input.getCaracteresBuscadorMostrar() : 0);
            output.setTotalResultadosBuscador(input.getTotalResultadosBuscador() != null ? input.getTotalResultadosBuscador() : 0);
            output.setLider(input.getLider() != null ? input.getLider() : 0);
            output.setPeriodo(input.getPeriodo());
            output.setSemanaPeriodo(input.getSemanaPeriodo());
            output.setDescripcionNivelLider(input.getDescripcionNivelLider());
            output.setRDEsActiva(input.isRDEsActiva() != null ? input.isRDEsActiva() : false);
            output.setRDEsSuscrita(input.isRDEsSuscrita() != null ? input.isRDEsSuscrita() : false);
            output.setRDActivoMdo(input.isRDActivoMdo() != null ? input.isRDActivoMdo() : false);
            output.setRDTieneRDC(input.isRDTieneRDC() != null ? input.isRDTieneRDC() : false);
            output.setRDTieneRDI(input.isRDTieneRDI() != null ? input.isRDTieneRDI() : false);
            output.setRDTieneRDCR(input.isRDTieneRDCR() != null ? input.isRDTieneRDCR(): false);
            output.setDiaFacturacion(input.getDiaFacturacion() != null ? input.getDiaFacturacion() : 0);
            output.setTipoIngreso(input.getTipoIngreso());
            output.setSegmentoDatami(input.getSegmentoDatami());
            output.setIndicadorConsultoraDigital(input.getIndicadorConsultoraDigital());
            output.setShowActualizacionDatos(input.getActualizacionDatos());
            output.setSegmentoConstancia(input.getSegmentoConstancia());
            output.setCodigoSeccion(input.getCodigoSeccion());
            output.setCheckEnviarWhatsaap(input.getCheckEnviarWhatsaap());
            output.setShowCheckWhatsapp(input.getShowCheckWhatsapp());
            output.setCelularPendiente(input.getCelularPendiente());
            output.setCambioCelularPendiente(input.isCambioCelularPendiente()!= null ? input.isCambioCelularPendiente() : false);
        }
        return output;
    }

    /**
     * Transforma una lista de entidades a una lista de entidades de dominio
     *
     * @param input Lista de entidades
     * @return list Lista de entidades del dominio
     */
    public List<Login> transform(Collection<LoginModel> input) {
        List<Login> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (LoginModel model : input) {
            final Login login = transform(model);
            if (null != login) {
                output.add(login);
            }
        }

        return output;
    }

}

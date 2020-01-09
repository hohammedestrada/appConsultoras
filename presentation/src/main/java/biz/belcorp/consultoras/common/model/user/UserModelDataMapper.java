package biz.belcorp.consultoras.common.model.user;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.entity.UserUpdateRequest;

@PerActivity
public class UserModelDataMapper {

    @Inject
    UserModelDataMapper() {
        // EMPTY
    }

    public User transform(UserModel input) {
        User output = null;

        if (null != input) {
            output = new User();
            boolean checkWhatsapp = input.getIsCheckWhatsapp() > 0;
            boolean showCheckWhatsapp = input.getIsCheckWhatsapp() > 0;
            output.setCountryId(input.getCountryId());
            output.setCountryISO(input.getCountryISO());
            output.setCountryMoneySymbol(input.getCountryMoneySymbol());
            output.setCountryShowDecimal(input.getCountryShowDecimal());

            output.setConsultantId(input.getConsultantId());
            output.setConsultantCode(input.getConsultantCode());
            output.setConsultantUserCode(input.getConsultantUserCode());
            output.setConsultantAssociateId(input.getConsultantAssociateId());
            output.setUserCode(input.getUserCode());
            output.setUserTest(input.isUserTest());
            output.setUserType(input.getUserType());
            output.setConsultantName(input.getConsultantName());
            output.setAlias(input.getAlias());
            output.setEmail(input.getEmail());
            output.setPhone(input.getPhone());
            output.setOtherPhone(input.getOtherPhone());
            output.setMobile(input.getMobile());
            output.setPhotoProfile(input.getPhotoProfile());

            output.setCampaing(input.getCampaing());
            output.setNumberOfCampaings(input.getNumberOfCampaings());
            output.setRegionID(input.getRegionID());
            output.setRegionCode(input.getRegionCode());
            output.setZoneID(input.getZoneID());
            output.setZoneCode(input.getZoneCode());

            output.setExpirationDate(input.getExpirationDate());
            output.setDayProl(input.isDayProl());
            output.setConsultantAcceptDA(input.getConsultantAcceptDA());
            output.setUrlBelcorpChat(input.getUrlBelcorpChat());
            output.setBillingStartDate(input.getBillingStartDate());
            output.setBillingEndDate(input.getBillingEndDate());
            output.setEndTime(input.getEndTime());
            output.setTimeZone(input.getTimeZone());
            output.setClosingDays(input.getClosingDays());
            output.setHasDayOffer(input.isHasDayOffer());
            output.setAceptaTerminosCondiciones(input.isAceptaTerminosCondiciones());
            output.setAceptaPoliticaPrivacidad(input.isAceptaPoliticaPrivacidad());
            output.setDestinatariosFeedback(input.getDestinatariosFeedback());

            output.setPeriodo(input.getPeriodo());
            output.setSemanaPeriodo(input.getSemanaPeriodo());
            output.setDescripcionNivelLider(input.getDescripcionNivelLider());

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
            output.setCodigoSeccion(input.getCodigoSeccion());
            output.setTipoCondicion(input.getTipoCondicion());
            output.setHoraFinPortal(input.getHoraFinPortal());
            output.setEsConsultoraOficina(input.isEsConsultoraOficina());
            output.setIndicadorContratoCredito(input.getIndicadorContratoCredito());
            output.setPagoEnLinea(input.isPagoEnLinea());
            output.setCorreoPendiente(input.getCorreoPendiente());

            output.setActivaNotificaconesWhatsapp(checkWhatsapp);
            output.setNotificacionesWhatsapp(showCheckWhatsapp);
            output.setCambioCelularPendiente(input.isCambioCelularPendiente());
            output.setCelularPendiente(input.getCelularPendiente());
            output.setMultipedido(input.isMultipedido());
            output.setLineaConsultora(input.getLineaConsultora());

        }

        return output;
    }

    public UserModel transform(User input) {
        UserModel output = null;

        if (null != input) {
            output = new UserModel();
            output.setCountryId(input.getCountryId());
            output.setCountryISO(input.getCountryISO());
            output.setCountryMoneySymbol(input.getCountryMoneySymbol());
            output.setCountryShowDecimal(input.getCountryShowDecimal());

            output.setConsultantId(input.getConsultantId());
            output.setConsultantCode(input.getConsultantCode());
            output.setConsultantUserCode(input.getConsultantUserCode());
            output.setConsultantAssociateId(input.getConsultantAssociateId());
            output.setUserCode(input.getUserCode());
            output.setUserTest(input.isUserTest());
            output.setUserType(input.getUserType());
            output.setConsultantName(input.getConsultantName());
            output.setAlias(input.getAlias());
            output.setEmail(input.getEmail());
            output.setPhone(input.getPhone());
            output.setOtherPhone(input.getOtherPhone());
            output.setMobile(input.getMobile());
            output.setPhotoProfile(input.getPhotoProfile());

            output.setCampaing(input.getCampaing());
            output.setNumberOfCampaings(input.getNumberOfCampaings());
            output.setRegionID(input.getRegionID());
            output.setRegionCode(input.getRegionCode());
            output.setZoneID(input.getZoneID());
            output.setZoneCode(input.getZoneCode());

            output.setExpirationDate(input.getExpirationDate());
            output.setDayProl(input.isDayProl());
            output.setConsultantAcceptDA(input.getConsultantAcceptDA());
            output.setUrlBelcorpChat(input.getUrlBelcorpChat());
            output.setBillingStartDate(input.getBillingStartDate());
            output.setBillingEndDate(input.getBillingEndDate());
            output.setEndTime(input.getEndTime());
            output.setTimeZone(input.getTimeZone());
            output.setClosingDays(input.getClosingDays());
            output.setHasDayOffer(input.isHasDayOffer());
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
            output.setCodigoSeccion(input.getCodigoSeccion());
            output.setTipoCondicion(input.getTipoCondicion());
            output.setHoraFinPortal(input.getHoraFinPortal());
            output.setEsConsultoraOficina(input.getEsConsultoraOficina());
            output.setIndicadorContratoCredito(input.getIndicadorContratoCredito() != null ? input.getIndicadorContratoCredito() : -1);

            output.setCambioCorreoPendiente(input.isCambioCorreoPendiente() != null ? input.isCambioCorreoPendiente() : false);
            output.setCorreoPendiente(input.getCorreoPendiente());
            output.setPrimerNombre(input.getPrimerNombre());
            output.setPuedeActualizar(input.isPuedeActualizar() != null ? input.isPuedeActualizar() : false);
            output.setPuedeActualizarEmail(input.isPuedeActualizarEmail() != null ? input.isPuedeActualizarEmail() : false);
            output.setPuedeActualizarCelular(input.isPuedeActualizarCelular() != null ? input.isPuedeActualizarCelular() : false);

            output.setMostrarBuscador(input.isMostrarBuscador() != null ? input.isMostrarBuscador() : false);
            output.setCaracteresBuscador(input.getCaracteresBuscador() != null ? input.getCaracteresBuscador() : 0);
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
            output.setRDTieneRDCR(input.isRDTieneRDCR() != null ? input.isRDTieneRDCR() : false);
            output.setDiaFacturacion(input.getDiaFacturacion() != null ? input.getDiaFacturacion() : 0);
            output.setPagoEnLinea(input.isPagoEnLinea() != null ? input.isPagoEnLinea() : false);

            output.setIsCheckWhatsapp(input.isActivaNotificaconesWhatsapp() ? 1 : 0);

            output.setIsNotificacionesWhatsapp(input.isNotificacionesWhatsapp() ? 1 : 0);

            output.setMultipedido(input.isMultipedido());
            output.setLineaConsultora(input.getLineaConsultora());

        }
        return output;
    }

    public UserUpdateRequest transformUser(User input) {
        UserUpdateRequest output = null;

        if (null != input) {
            output = new UserUpdateRequest();
            output.setEmail(input.getEmail());
            output.setPhone(input.getPhone());
            output.setMobile(input.getMobile());
            output.setOtherPhone(input.getOtherPhone());
            output.setAcceptContract(input.isAceptaTerminosCondiciones());
            output.setSobreNombre(input.getAlias());
            output.setNombreArchivo(input.getPhotoName());
            output.setTipoArchivo(input.getTipoArchivo());
            output.setUrlImagen(input.getPhotoProfile());
            output.setNotificacionesWhatsapp(input.isNotificacionesWhatsapp());
            output.setActivaNotificaconesWhatsapp(input.isActivaNotificaconesWhatsapp());


        }

        return output;
    }
}

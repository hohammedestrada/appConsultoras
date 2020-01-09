package biz.belcorp.consultoras.common.model.incentivos;

public class IncentivesRequestModel {

    private String countryISO;
    private String campaingCode;
    private String consultantCode;
    private String regionCode;
    private String zoneCode;
    private String tipoConcurso;

    public String getCountryISO() {
        return countryISO;
    }

    public void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
    }

    public String getCampaingCode() {
        return campaingCode;
    }

    public void setCampaingCode(String campaingCode) {
        this.campaingCode = campaingCode;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getTipoConcurso() {
        return tipoConcurso;
    }

    public void setTipoConcurso(String tipoConcurso) {
        this.tipoConcurso = tipoConcurso;
    }
}

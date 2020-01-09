package biz.belcorp.consultoras.common.model.auth;

import biz.belcorp.library.model.BelcorpModel;
import biz.belcorp.library.model.annotation.Required;

public class CredentialsModel extends BelcorpModel{

    @Required
    private String pais;
    @Required
    private String username;
    @Required
    private String password;

    private int tipoAutenticacion;

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTipoAutenticacion() {
        return tipoAutenticacion;
    }

    public void setTipoAutenticacion(int tipoAutenticacion) {
        this.tipoAutenticacion = tipoAutenticacion;
    }
}

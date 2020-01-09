package biz.belcorp.consultoras.feature.auth.recovery;

/**
 *
 */

public class RecoveryModel {

    private int countryID;
    private String username;

    public RecoveryModel() { }

    public RecoveryModel(int id, String username){
        this.countryID = id;
        this.username = username;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int id) {
        this.countryID = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

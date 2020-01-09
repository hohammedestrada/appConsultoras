package biz.belcorp.consultoras.common.model.debt;

/**
 *
 */
public class CampaniaModel {

    private String campaniaId;
    private String campaniaName;

    public String getCampaniaId() {
        return campaniaId;
    }

    public void setCampaniaId(String campaniaId) {
        this.campaniaId = campaniaId;
    }

    public String getCampaniaName() {
        return campaniaName;
    }

    public void setCampaniaName(String campaniaName) {
        this.campaniaName = campaniaName;
    }

    @Override
    public String toString() {
        return campaniaName;
    }
}

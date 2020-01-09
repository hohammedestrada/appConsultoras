package biz.belcorp.consultoras.common.model.error;

public class BusinessErrorModel {
    private String code;
    private Object params;

    public BusinessErrorModel(String code, Object params) {
        this.code = code;
        this.params = params;
    }

    public String getCode() {
        return code;
    }

    public Object getParams() {
        return params;
    }

}

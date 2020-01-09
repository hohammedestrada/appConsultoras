package biz.belcorp.consultoras.common.model.error;

import com.google.gson.JsonElement;

public class ErrorModel {
    private int code;
    private String message;
    private JsonElement params;

    public ErrorModel(int code, String message, JsonElement params) {
        this.code = code;
        this.params = params;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public JsonElement getParams() {
        return params;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

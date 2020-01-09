package biz.belcorp.consultoras.data.net.dto;

import com.google.gson.annotations.SerializedName;

public class GeneralErrorDto {

    @SerializedName("Code")
    private String code;
    @SerializedName("Message")
    private String message;

    public GeneralErrorDto() {
        // EMPTY
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

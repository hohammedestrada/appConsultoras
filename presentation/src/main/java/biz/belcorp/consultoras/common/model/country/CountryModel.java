package biz.belcorp.consultoras.common.model.country;

import android.os.Parcel;
import android.os.Parcelable;

public class CountryModel implements Parcelable {

    private Integer id;
    private String name;
    private String iso;
    private String urlImage;
    private int focusBrand;  // 0 = Esika, 1 = LBel
    private String textHelpUser;
    private String textHelpPassword;
    private String urlJoinBelcorp;
    private int configForgotPassword;   // 1 = valida Email , 2 = valida documento
    private int showDecimals;
    private String urlTerminos;
    private String urlPrivacidad;
    private String receiverFeedBack;

    private boolean capturaDatosConsultora;
    private String telefono1;
    private String telefono2;
    private String urlContratoActualizacionDatos;
    private String urlContratoVinculacion;

    public CountryModel(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getFocusBrand() {
        return focusBrand;
    }

    public void setFocusBrand(int focusBrand) {
        this.focusBrand = focusBrand;
    }

    public String getTextHelpUser() {
        return textHelpUser;
    }

    public void setTextHelpUser(String textHelpUser) {
        this.textHelpUser = textHelpUser;
    }

    public String getTextHelpPassword() {
        return textHelpPassword;
    }

    public void setTextHelpPassword(String textHelpPassword) {
        this.textHelpPassword = textHelpPassword;
    }

    public String getUrlJoinBelcorp() {
        return urlJoinBelcorp;
    }

    public void setUrlJoinBelcorp(String urlJoinBelcorp) {
        this.urlJoinBelcorp = urlJoinBelcorp;
    }

    public int getConfigForgotPassword() {
        return configForgotPassword;
    }

    public void setConfigForgotPassword(int configForgotPassword) {
        this.configForgotPassword = configForgotPassword;
    }

    public int getShowDecimals() {
        return showDecimals;
    }

    public void setShowDecimals(int showDecimals) {
        this.showDecimals = showDecimals;
    }

    public boolean isCapturaDatosConsultora() {
        return capturaDatosConsultora;
    }

    public void setCapturaDatosConsultora(boolean capturaDatosConsultora) {
        this.capturaDatosConsultora = capturaDatosConsultora;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getUrlContratoActualizacionDatos() {
        return urlContratoActualizacionDatos;
    }

    public void setUrlContratoActualizacionDatos(String urlContratoActualizacionDatos) {
        this.urlContratoActualizacionDatos = urlContratoActualizacionDatos;
    }

    public String getUrlContratoVinculacion() {
        return urlContratoVinculacion;
    }

    public void setUrlContratoVinculacion(String urlContratoVinculacion) {
        this.urlContratoVinculacion = urlContratoVinculacion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.iso);
        dest.writeString(this.urlImage);
        dest.writeInt(this.focusBrand);
        dest.writeString(this.textHelpUser);
        dest.writeString(this.textHelpPassword);
        dest.writeString(this.urlJoinBelcorp);
        dest.writeInt(this.configForgotPassword);
        dest.writeInt(this.showDecimals);

    }

    protected CountryModel(Parcel in) {

        this.id = in.readInt();
        this.name = in.readString();
        this.iso = in.readString();
        this.urlImage = in.readString();
        this.focusBrand = in.readInt();
        this.textHelpUser = in.readString();
        this.textHelpPassword = in.readString();
        this.urlJoinBelcorp = in.readString();
        this.configForgotPassword = in.readInt();
        this.showDecimals = in.readInt();

    }

    public final Creator<CountryModel> CREATOR = new Creator<CountryModel>() {
        @Override
        public CountryModel createFromParcel(Parcel source) {
            return new CountryModel(source);
        }

        @Override
        public CountryModel[] newArray(int size) {
            return new CountryModel[size];
        }
    };

    public String getUrlTerminos() {
        return urlTerminos;
    }

    public void setUrlTerminos(String urlTerminos) {
        this.urlTerminos = urlTerminos;
    }

    public String getUrlPrivacidad() {
        return urlPrivacidad;
    }

    public void setUrlPrivacidad(String urlPrivacidad) {
        this.urlPrivacidad = urlPrivacidad;
    }

    public String getReceiverFeedBack() {
        return receiverFeedBack;
    }

    public void setReceiverFeedBack(String receiverFeedBack) {
        this.receiverFeedBack = receiverFeedBack;
    }
}

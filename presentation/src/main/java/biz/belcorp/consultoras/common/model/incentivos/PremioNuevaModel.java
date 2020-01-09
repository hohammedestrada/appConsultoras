package biz.belcorp.consultoras.common.model.incentivos;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class PremioNuevaModel implements Parcelable {

    private Integer id;
    private Integer nivelProgramaLocalId;
    private String codigoConcurso;
    private String codigoNivel;
    private String cuv;
    private String descripcionProducto;
    private String urlImagenPremio;
    private Boolean indicadorKitNuevas;
    private BigDecimal precioUnitario;


    public PremioNuevaModel() {
        // EMPTY
    }

    protected PremioNuevaModel(Parcel in) {

        id = in.readInt();
        nivelProgramaLocalId = in.readInt();
        codigoConcurso = in.readString();
        codigoNivel = in.readString();
        cuv = in.readString();
        descripcionProducto = in.readString();
        urlImagenPremio = in.readString();
        indicadorKitNuevas = in.readByte() != 0;
        precioUnitario = BigDecimal.valueOf(in.readDouble());

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(nivelProgramaLocalId);
        dest.writeString(codigoConcurso);
        dest.writeString(codigoNivel);
        dest.writeString(cuv);
        dest.writeString(descripcionProducto);
        dest.writeString(urlImagenPremio);

        if (indicadorKitNuevas != null)
            dest.writeByte((byte) (indicadorKitNuevas ? 1 : 0));
        if (precioUnitario != null)
            dest.writeDouble(precioUnitario.doubleValue());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PremioNuevaModel> CREATOR = new Creator<PremioNuevaModel>() {
        @Override
        public PremioNuevaModel createFromParcel(Parcel in) {
            return new PremioNuevaModel(in);
        }

        @Override
        public PremioNuevaModel[] newArray(int size) {
            return new PremioNuevaModel[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNivelProgramaLocalId() {
        return nivelProgramaLocalId;
    }

    public void setNivelProgramaLocalId(Integer nivelProgramaLocalId) {
        this.nivelProgramaLocalId = nivelProgramaLocalId;
    }

    public String getCodigoConcurso() {
        return codigoConcurso;
    }

    public void setCodigoConcurso(String codigoConcurso) {
        this.codigoConcurso = codigoConcurso;
    }

    public String getCodigoNivel() {
        return codigoNivel;
    }

    public void setCodigoNivel(String codigoNivel) {
        this.codigoNivel = codigoNivel;
    }

    public String getCUV() {
        return cuv;
    }

    public void setCUV(String cuv) {
        this.cuv = cuv;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public Boolean getIndicadorKitNuevas() {
        return indicadorKitNuevas;
    }

    public void setIndicadorKitNuevas(Boolean indicadorKitNuevas) {
        this.indicadorKitNuevas = indicadorKitNuevas;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getUrlImagenPremio() {
        return urlImagenPremio;
    }

    public void setUrlImagenPremio(String urlImagenPremio) {
        this.urlImagenPremio = urlImagenPremio;
    }
}

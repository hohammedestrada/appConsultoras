package biz.belcorp.consultoras.common.model.incentivos;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class CuponModel implements Parcelable{

    private Integer id;
    private Integer nivelProgramaLocalId;
    private String codigoConcurso;
    private String codigoNivel;
    private String codigoCupon;
    private String codigoVenta;
    private String descripcionProducto;
    private Integer unidadesMaximas;
    private Integer numeroCampanasVigentes;
    private String urlImagenCupon;
    private String textoLibre;
    private Boolean indicadorCuponIndependiente;
    private Boolean indicadorKit;
    private BigDecimal precioUnitario;
    private BigDecimal ganancia;

    CuponModel() {
        // EMPTY
    }

    protected CuponModel(Parcel in) {
        id = in.readInt();
        nivelProgramaLocalId = in.readInt();
        codigoConcurso = in.readString();
        codigoNivel = in.readString();
        codigoCupon = in.readString();
        codigoVenta = in.readString();
        descripcionProducto = in.readString();
        unidadesMaximas = in.readInt();
        numeroCampanasVigentes = in.readInt();
        urlImagenCupon = in.readString();
        textoLibre = in.readString();
        indicadorCuponIndependiente = in.readByte() != 0;
        indicadorKit = in.readByte() != 0;
        precioUnitario = BigDecimal.valueOf(in.readDouble());
        ganancia = BigDecimal.valueOf(in.readDouble());
    }

    public static final Creator<CuponModel> CREATOR = new Creator<CuponModel>() {
        @Override
        public CuponModel createFromParcel(Parcel in) {
            return new CuponModel(in);
        }

        @Override
        public CuponModel[] newArray(int size) {
            return new CuponModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(nivelProgramaLocalId);
        dest.writeString(codigoConcurso);
        dest.writeString(codigoNivel);
        dest.writeString(codigoCupon);
        dest.writeString(codigoVenta);
        dest.writeString(descripcionProducto);
        dest.writeInt(unidadesMaximas);
        dest.writeInt(numeroCampanasVigentes);
        dest.writeString(urlImagenCupon);
        dest.writeString(textoLibre);

        if (indicadorCuponIndependiente != null)
            dest.writeByte((byte) (indicadorCuponIndependiente ? 1 : 0));
        if (indicadorKit != null)
            dest.writeByte((byte) (indicadorKit ? 1 : 0));
        if (precioUnitario != null)
            dest.writeDouble(precioUnitario.doubleValue());
        if (ganancia != null)
            dest.writeDouble(ganancia.doubleValue());
    }

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

    public String getCodigoCupon() {
        return codigoCupon;
    }

    public void setCodigoCupon(String codigoCupon) {
        this.codigoCupon = codigoCupon;
    }

    public String getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(String codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public Integer getUnidadesMaximas() {
        return unidadesMaximas;
    }

    public void setUnidadesMaximas(Integer unidadesMaximas) {
        this.unidadesMaximas = unidadesMaximas;
    }

    public Boolean getIndicadorCuponIndependiente() {
        return indicadorCuponIndependiente;
    }

    public void setIndicadorCuponIndependiente(Boolean indicadorCuponIndependiente) {
        this.indicadorCuponIndependiente = indicadorCuponIndependiente;
    }

    public Boolean getIndicadorKit() {
        return indicadorKit;
    }

    public void setIndicadorKit(Boolean indicadorKit) {
        this.indicadorKit = indicadorKit;
    }

    public Integer getNumeroCampanasVigentes() {
        return numeroCampanasVigentes;
    }

    public void setNumeroCampanasVigentes(Integer numeroCampanasVigentes) {
        this.numeroCampanasVigentes = numeroCampanasVigentes;
    }

    public String getUrlImagenCupon() {
        return urlImagenCupon;
    }

    public void setUrlImagenCupon(String urlImagenCupon) {
        this.urlImagenCupon = urlImagenCupon;
    }

    public String getTextoLibre() {
        return textoLibre;
    }

    public void setTextoLibre(String textoLibre) {
        this.textoLibre = textoLibre;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getGanancia() {
        return ganancia;
    }

    public void setGanancia(BigDecimal ganancia) {
        this.ganancia = ganancia;
    }
}

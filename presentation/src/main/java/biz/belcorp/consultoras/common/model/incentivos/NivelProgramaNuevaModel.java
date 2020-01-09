package biz.belcorp.consultoras.common.model.incentivos;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;


public class NivelProgramaNuevaModel implements Parcelable {
    private Integer id;
    private Integer concursoLocalId;
    private String codigoConcurso;
    private String codigoNivel;
    private BigDecimal montoExigidoPremio;
    private BigDecimal montoExigidoCupon;

    private List<PremioNuevaModel> premiosNuevas = null;
    private List<CuponModel> cupones = null;

    NivelProgramaNuevaModel(){
        // EMPTY
    }

    protected NivelProgramaNuevaModel(Parcel in) {
        codigoConcurso = in.readString();
        codigoNivel = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(codigoConcurso);
        dest.writeString(codigoNivel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NivelProgramaNuevaModel> CREATOR = new Creator<NivelProgramaNuevaModel>() {
        @Override
        public NivelProgramaNuevaModel createFromParcel(Parcel in) {
            return new NivelProgramaNuevaModel(in);
        }

        @Override
        public NivelProgramaNuevaModel[] newArray(int size) {
            return new NivelProgramaNuevaModel[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConcursoLocalId() {
        return concursoLocalId;
    }

    public void setConcursoLocalId(Integer concursoLocalId) {
        this.concursoLocalId = concursoLocalId;
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

    public BigDecimal getMontoExigidoPremio() {
        return montoExigidoPremio;
    }

    public void setMontoExigidoPremio(BigDecimal montoExigidoPremio) {
        this.montoExigidoPremio = montoExigidoPremio;
    }

    public BigDecimal getMontoExigidoCupon() {
        return montoExigidoCupon;
    }

    public void setMontoExigidoCupon(BigDecimal montoExigidoCupon) {
        this.montoExigidoCupon = montoExigidoCupon;
    }

    public List<PremioNuevaModel> getPremiosNuevas() {
        return premiosNuevas;
    }

    public void setPremiosNuevas(List<PremioNuevaModel> premiosNuevas) {
        this.premiosNuevas = premiosNuevas;
    }

    public List<CuponModel> getCupones() {
        return cupones;
    }

    public void setCupones(List<CuponModel> cupones) {
        this.cupones = cupones;
    }
}

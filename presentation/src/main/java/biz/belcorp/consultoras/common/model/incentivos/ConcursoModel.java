
package biz.belcorp.consultoras.common.model.incentivos;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

public class ConcursoModel implements Parcelable {

    private Integer id;
    private String campaniaId;
    private Integer campaniaIDInicio;
    private Integer campaniaIDFin;
    private String codigoConcurso;
    private String tipoConcurso;
    private Integer puntosAcumulados;
    private Boolean indicadorPremioAcumulativo;
    private Integer nivelAlcanzado;
    private Integer nivelSiguiente;
    private String campaniaIDPremiacion;
    private Integer puntajeExigido;
    private String descripcionConcurso;
    private String estadoConcurso;
    private String urlBannerPremiosProgramaNuevas;
    private String urlBannerCuponesProgramaNuevas;
    private String codigoNivelProgramaNuevas;
    private BigDecimal importePedido;
    private String textoCupon;
    private String textoCuponIndependiente;

    private List<NivelModel> niveles = null;
    private List<NivelProgramaNuevaModel> nivelProgramaNuevas = null;

    public ConcursoModel() {
    }

    protected ConcursoModel(Parcel in) {

        campaniaId = in.readString();
        codigoConcurso = in.readString();
        tipoConcurso = in.readString();
        campaniaIDPremiacion = in.readString();
        descripcionConcurso = in.readString();
        estadoConcurso = in.readString();
        urlBannerPremiosProgramaNuevas = in.readString();
        urlBannerCuponesProgramaNuevas = in.readString();
        codigoNivelProgramaNuevas = in.readString();

        id = in.readInt();
        campaniaIDInicio = in.readInt();
        campaniaIDFin = in.readInt();
        puntosAcumulados = in.readInt();
        nivelAlcanzado = in.readInt();
        nivelSiguiente = in.readInt();
        puntajeExigido = in.readInt();
        indicadorPremioAcumulativo = in.readByte() != 0;
        importePedido = BigDecimal.valueOf(in.readDouble());

        niveles = in.createTypedArrayList(NivelModel.CREATOR);
        nivelProgramaNuevas = in.createTypedArrayList(NivelProgramaNuevaModel.CREATOR);

    }

    public static final Creator<ConcursoModel> CREATOR = new Creator<ConcursoModel>() {
        @Override
        public ConcursoModel createFromParcel(Parcel in) {
            return new ConcursoModel(in);
        }

        @Override
        public ConcursoModel[] newArray(int size) {
            return new ConcursoModel[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCampaniaId() {
        return campaniaId;
    }

    public void setCampaniaId(String campaniaId) {
        this.campaniaId = campaniaId;
    }

    public Integer getCampaniaIDInicio() {
        return campaniaIDInicio;
    }

    public void setCampaniaIDInicio(Integer campaniaIDInicio) {
        this.campaniaIDInicio = campaniaIDInicio;
    }

    public Integer getCampaniaIDFin() {
        return campaniaIDFin;
    }

    public void setCampaniaIDFin(Integer campaniaIDFin) {
        this.campaniaIDFin = campaniaIDFin;
    }

    public String getCodigoConcurso() {
        return codigoConcurso;
    }

    public void setCodigoConcurso(String codigoConcurso) {
        this.codigoConcurso = codigoConcurso;
    }

    public String getTipoConcurso() {
        return tipoConcurso;
    }

    public void setTipoConcurso(String tipoConcurso) {
        this.tipoConcurso = tipoConcurso;
    }

    public Integer getPuntosAcumulados() {
        return puntosAcumulados;
    }

    public void setPuntosAcumulados(Integer puntosAcumulados) {
        this.puntosAcumulados = puntosAcumulados;
    }

    public List<NivelModel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<NivelModel> niveles) {
        this.niveles = niveles;
    }

    public Boolean isIndicadorPremioAcumulativo() {
        return indicadorPremioAcumulativo;
    }

    public void setIndicadorPremioAcumulativo(Boolean indicadorPremioAcumulativo) {
        this.indicadorPremioAcumulativo = indicadorPremioAcumulativo;
    }

    public Integer getNivelAlcanzado() {
        return nivelAlcanzado;
    }

    public void setNivelAlcanzado(Integer nivelAlcanzado) {
        this.nivelAlcanzado = nivelAlcanzado;
    }

    public void setNivelSiguiente(Integer nivelSiguiente) {
        this.nivelSiguiente = nivelSiguiente;
    }

    public Integer getNivelSiguiente() {
        return nivelSiguiente;
    }

    public String getCampaniaIDPremiacion() {
        return campaniaIDPremiacion;
    }

    public void setCampaniaIDPremiacion(String campaniaIDPremiacion) {
        this.campaniaIDPremiacion = campaniaIDPremiacion;
    }

    public Integer getPuntajeExigido() {
        return puntajeExigido;
    }

    public void setPuntajeExigido(Integer puntajeExigido) {
        this.puntajeExigido = puntajeExigido;
    }

    public String getDescripcionConcurso() {
        return descripcionConcurso;
    }

    public void setDescripcionConcurso(String descripcionConcurso) {
        this.descripcionConcurso = descripcionConcurso;
    }

    public String getEstadoConcurso() {
        return estadoConcurso;
    }

    public void setEstadoConcurso(String estadoConcurso) {
        this.estadoConcurso = estadoConcurso;
    }

    public String getUrlBannerPremiosProgramaNuevas() {
        return urlBannerPremiosProgramaNuevas;
    }

    public void setUrlBannerPremiosProgramaNuevas(String urlBannerPremiosProgramaNuevas) {
        this.urlBannerPremiosProgramaNuevas = urlBannerPremiosProgramaNuevas;
    }

    public String getUrlBannerCuponesProgramaNuevas() {
        return urlBannerCuponesProgramaNuevas;
    }

    public void setUrlBannerCuponesProgramaNuevas(String urlBannerCuponesProgramaNuevas) {
        this.urlBannerCuponesProgramaNuevas = urlBannerCuponesProgramaNuevas;
    }

    public String getCodigoNivelProgramaNuevas() {
        return codigoNivelProgramaNuevas;
    }

    public void setCodigoNivelProgramaNuevas(String codigoNivelProgramaNuevas) {
        this.codigoNivelProgramaNuevas = codigoNivelProgramaNuevas;
    }

    public List<NivelProgramaNuevaModel> getNivelProgramaNuevas() {
        return nivelProgramaNuevas;
    }

    public void setNivelProgramaNuevas(List<NivelProgramaNuevaModel> nivelProgramaNuevas) {
        this.nivelProgramaNuevas = nivelProgramaNuevas;
    }

    public BigDecimal getImportePedido() {
        return importePedido;
    }

    public void setImportePedido(BigDecimal importePedido) {
        this.importePedido = importePedido;
    }

    public String getTextoCupon() {
        return textoCupon;
    }

    public void setTextoCupon(String textoCupon) {
        this.textoCupon = textoCupon;
    }

    public String getTextoCuponIndependiente() {
        return textoCuponIndependiente;
    }

    public void setTextoCuponIndependiente(String textoCuponIndependiente) {
        this.textoCuponIndependiente = textoCuponIndependiente;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(campaniaId);
        dest.writeString(codigoConcurso);
        dest.writeString(tipoConcurso);
        dest.writeString(campaniaIDPremiacion);
        dest.writeString(descripcionConcurso);
        dest.writeString(estadoConcurso);
        dest.writeString(urlBannerPremiosProgramaNuevas);
        dest.writeString(urlBannerCuponesProgramaNuevas);
        dest.writeString(codigoNivelProgramaNuevas);

        if (id != null)
            dest.writeInt(id);
        if (campaniaIDInicio != null)
            dest.writeInt(campaniaIDInicio);
        if (campaniaIDFin != null)
            dest.writeInt(campaniaIDFin);
        if (puntosAcumulados != null)
            dest.writeInt(puntosAcumulados);
        if (nivelAlcanzado != null)
            dest.writeInt(nivelAlcanzado);
        if (nivelSiguiente != null)
            dest.writeInt(nivelSiguiente);
        if (puntajeExigido != null)
            dest.writeInt(puntajeExigido);
        if (indicadorPremioAcumulativo != null)
            dest.writeByte((byte) (indicadorPremioAcumulativo ? 1 : 0));
        if (importePedido != null)
            dest.writeDouble(importePedido.doubleValue());
        if (niveles != null)
            dest.writeTypedList(niveles);
        if (nivelProgramaNuevas != null)
            dest.writeTypedList(nivelProgramaNuevas);
    }
}

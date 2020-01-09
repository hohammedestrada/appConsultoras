
package biz.belcorp.consultoras.common.model.incentivos;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

public class NivelModel implements Parcelable {

    private Integer id;
    private Integer concursoLocalId;
    private String codigoConcurso;
    private Integer codigoNivel;
    private Integer puntosNivel;
    private Integer puntosFaltantes;
    private Integer puntosExigidos;
    private Integer puntosExigidosFaltantes;
    private Boolean indicadorPremiacionPedido;
    private Boolean indicadorNivelElectivo;
    private Boolean indicadorBelCenter;
    private BigDecimal montoPremiacionPedido;
    private String fechaVentaRetail;

    private List<OpcionModel> opciones = null;

    public NivelModel() {
        // EMPTY
    }

    protected NivelModel(Parcel in) {
        codigoConcurso = in.readString();
        fechaVentaRetail = in.readString();

        id = in.readInt();
        concursoLocalId = in.readInt();
        codigoNivel = in.readInt();
        puntosNivel = in.readInt();
        puntosFaltantes = in.readInt();
        puntosExigidos = in.readInt();
        puntosExigidosFaltantes = in.readInt();
        indicadorPremiacionPedido = in.readByte() != 0;
        indicadorNivelElectivo = in.readByte() != 0;
        indicadorBelCenter = in.readByte() != 0;
        montoPremiacionPedido = BigDecimal.valueOf(in.readDouble());

        opciones = in.createTypedArrayList(OpcionModel.CREATOR);
    }

    public static final Creator<NivelModel> CREATOR = new Creator<NivelModel>() {
        @Override
        public NivelModel createFromParcel(Parcel in) {
            return new NivelModel(in);
        }

        @Override
        public NivelModel[] newArray(int size) {
            return new NivelModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(codigoConcurso);
        dest.writeString(fechaVentaRetail);

        if (id != null) dest.writeInt(id);
        if (concursoLocalId != null) dest.writeInt(concursoLocalId);
        if (codigoNivel != null) dest.writeInt(codigoNivel);
        if (puntosNivel != null) dest.writeInt(puntosNivel);
        if (puntosFaltantes != null) dest.writeInt(puntosFaltantes);
        if (puntosExigidos != null) dest.writeInt(puntosExigidos);
        if (puntosExigidosFaltantes != null) dest.writeInt(puntosExigidosFaltantes);
        if (indicadorPremiacionPedido != null)
            dest.writeByte((byte) (indicadorPremiacionPedido ? 1 : 0));
        if (indicadorNivelElectivo != null)
            dest.writeByte((byte) (indicadorNivelElectivo ? 1 : 0));
        if (indicadorBelCenter != null)
            dest.writeByte((byte) (indicadorBelCenter ? 1 : 0));
        if (montoPremiacionPedido != null)
            dest.writeDouble(montoPremiacionPedido.doubleValue());
        if (opciones != null) dest.writeTypedList(opciones);
    }

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

    public Integer getCodigoNivel() {
        return codigoNivel;
    }

    public void setCodigoNivel(Integer codigoNivel) {
        this.codigoNivel = codigoNivel;
    }

    public Integer getPuntosNivel() {
        return puntosNivel;
    }

    public void setPuntosNivel(Integer puntosNivel) {
        this.puntosNivel = puntosNivel;
    }

    public Integer getPuntosFaltantes() {
        return puntosFaltantes;
    }

    public void setPuntosFaltantes(Integer puntosFaltantes) {
        this.puntosFaltantes = puntosFaltantes;
    }

    public Integer getPuntosExigidos() {
        return puntosExigidos;
    }

    public void setPuntosExigidos(Integer puntosExigidos) {
        this.puntosExigidos = puntosExigidos;
    }

    public Integer getPuntosExigidosFaltantes() {
        return puntosExigidosFaltantes;
    }

    public void setPuntosExigidosFaltantes(Integer puntosExigidosFaltantes) {
        this.puntosExigidosFaltantes = puntosExigidosFaltantes;
    }

    public Boolean getIndicadorPremiacionPedido() {
        return indicadorPremiacionPedido;
    }

    public void setIndicadorPremiacionPedido(Boolean indicadorPremiacionPedido) {
        this.indicadorPremiacionPedido = indicadorPremiacionPedido;
    }

    public Boolean getIndicadorNivelElectivo() {
        return indicadorNivelElectivo;
    }

    public void setIndicadorNivelElectivo(Boolean indicadorNivelElectivo) {
        this.indicadorNivelElectivo = indicadorNivelElectivo;
    }

    public Boolean getIndicadorBelCenter() {
        return indicadorBelCenter;
    }

    public void setIndicadorBelCenter(Boolean indicadorBelCenter) {
        this.indicadorBelCenter = indicadorBelCenter;
    }

    public BigDecimal getMontoPremiacionPedido() {
        return montoPremiacionPedido;
    }

    public void setMontoPremiacionPedido(BigDecimal montoPremiacionPedido) {
        this.montoPremiacionPedido = montoPremiacionPedido;
    }

    public String getFechaVentaRetail() {
        return fechaVentaRetail;
    }

    public void setFechaVentaRetail(String fechaVentaRetail) {
        this.fechaVentaRetail = fechaVentaRetail;
    }

    public List<OpcionModel> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<OpcionModel> opciones) {
        this.opciones = opciones;
    }
}


package biz.belcorp.consultoras.common.model.tracking;

import android.os.Parcel;
import android.os.Parcelable;

public class TrackingDetailModel implements Parcelable {

    private int id;
    private Integer etapa;
    private String situacion;
    private String fecha;
    private String fechaFormateada;
    private String observacion;
    private Boolean alcanzado;

    public TrackingDetailModel() {
        // EMPTY
    }

    protected TrackingDetailModel(Parcel in) {
        id = in.readInt();
        etapa = in.readInt();
        situacion = in.readString();
        fecha = in.readString();
        fechaFormateada = in.readString();
        observacion = in.readString();
        alcanzado = in.readByte() != 0;
    }

    public static final Creator<TrackingDetailModel> CREATOR = new Creator<TrackingDetailModel>() {
        @Override
        public TrackingDetailModel createFromParcel(Parcel in) {
            return new TrackingDetailModel(in);
        }

        @Override
        public TrackingDetailModel[] newArray(int size) {
            return new TrackingDetailModel[size];
        }
    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getEtapa() {
        return etapa;
    }

    public void setEtapa(Integer etapa) {
        this.etapa = etapa;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFechaFormateada() {
        return fechaFormateada;
    }

    public void setFechaFormateada(String fechaFormateada) {
        this.fechaFormateada = fechaFormateada;
    }

    public Boolean getAlcanzado() {
        return alcanzado;
    }

    public void setAlcanzado(Boolean alcanzado) {
        this.alcanzado = alcanzado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(etapa);
        dest.writeString(situacion);
        dest.writeString(fecha);
        dest.writeString(fechaFormateada);
        dest.writeString(observacion);
        dest.writeByte((byte) (alcanzado ? 1 : 0));
    }

}


package biz.belcorp.consultoras.common.model.tracking;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TrackingModel implements Parcelable {

    private int id;
    private String numeroPedido;
    private Integer campania;
    private String estado;
    private String fecha;
    private List<TrackingDetailModel> detalles;

    public TrackingModel() {
        // EMPTY
    }

    protected TrackingModel(Parcel in) {
        id = in.readInt();
        campania = in.readInt();
        numeroPedido = in.readString();
        estado = in.readString();
        fecha = in.readString();
    }

    public static final Creator<TrackingModel> CREATOR = new Creator<TrackingModel>() {
        @Override
        public TrackingModel createFromParcel(Parcel in) {
            return new TrackingModel(in);
        }

        @Override
        public TrackingModel[] newArray(int size) {
            return new TrackingModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Integer getCampania() {
        return campania;
    }

    public void setCampania(Integer campania) {
        this.campania = campania;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<TrackingDetailModel> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<TrackingDetailModel> detalles) {
        this.detalles = detalles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(campania);
        dest.writeString(numeroPedido);
        dest.writeString(estado);
        dest.writeString(fecha);
    }
}

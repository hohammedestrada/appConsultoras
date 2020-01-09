
package biz.belcorp.consultoras.common.model.incentivos;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

public class OpcionModel implements Parcelable {

    private Integer opcion;
    private List<PremioModel> premios = null;

    public OpcionModel() {
        // EMPTY
    }

    protected OpcionModel(Parcel in) {
        opcion = in.readInt();
        premios = in.createTypedArrayList(PremioModel.CREATOR);
    }

    public static final Creator<OpcionModel> CREATOR = new Creator<OpcionModel>() {
        @Override
        public OpcionModel createFromParcel(Parcel in) {
            return new OpcionModel(in);
        }

        @Override
        public OpcionModel[] newArray(int size) {
            return new OpcionModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (opcion != null) dest.writeInt(opcion);
        if (premios != null) dest.writeTypedList(premios);
    }

    public Integer getOpcion() {
        return opcion;
    }

    public void setOpcion(Integer opcion) {
        this.opcion = opcion;
    }

    public List<PremioModel> getPremios() {
        return premios;
    }

    public void setPremios(List<PremioModel> premios) {
        this.premios = premios;
    }
}

package biz.belcorp.consultoras.common.model.catalog;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class CatalogModel implements Parcelable {

    private Integer marcaId;
    private String marcaDescripcion;
    private String urlImagen;
    private String urlCatalogo;
    private String titulo;
    private String descripcion;
    private boolean selected;
    private String campaniaId;
    private Integer UrlDescargaEstado;

    public CatalogModel() {
        // EMPTY
    }

    protected CatalogModel(Parcel in) {
        marcaDescripcion = in.readString();
        urlImagen = in.readString();
        urlCatalogo = in.readString();
        titulo = in.readString();
        descripcion = in.readString();
        selected = in.readByte() != 0;
        campaniaId = in.readString();
        UrlDescargaEstado = in.readInt();
    }

    public static final Creator<CatalogModel> CREATOR = new Creator<CatalogModel>() {
        @Override
        public CatalogModel createFromParcel(Parcel in) {
            return new CatalogModel(in);
        }

        @Override
        public CatalogModel[] newArray(int size) {
            return new CatalogModel[size];
        }
    };

    public Integer getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(Integer marcaId) {
        this.marcaId = marcaId;
    }

    public String getMarcaDescripcion() {
        return marcaDescripcion;
    }

    public void setMarcaDescripcion(String marcaDescripcion) {
        this.marcaDescripcion = marcaDescripcion;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getUrlCatalogo() {
        return urlCatalogo;
    }

    public void setUrlCatalogo(String urlCatalogo) {
        this.urlCatalogo = urlCatalogo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setUrlDescargaEstado(Integer urlDescarga){
        this.UrlDescargaEstado = urlDescarga;
    }

    public Integer getUrlDescargaEstado(){
        return UrlDescargaEstado;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(marcaDescripcion);
        dest.writeString(urlImagen);
        dest.writeString(urlCatalogo);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeString(campaniaId);
        dest.writeInt(UrlDescargaEstado);
    }

    public String getCampaniaId() {
        return campaniaId;
    }

    public void setCampaniaId(String campaniaId) {
        this.campaniaId = campaniaId;
    }
}

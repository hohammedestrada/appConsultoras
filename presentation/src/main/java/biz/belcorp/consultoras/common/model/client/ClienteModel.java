
package biz.belcorp.consultoras.common.model.client;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biz.belcorp.consultoras.common.model.product.ProductItem;
import biz.belcorp.consultoras.common.model.recordatorio.RecordatorioModel;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.model.BelcorpModel;
import biz.belcorp.library.model.annotation.Date;
import biz.belcorp.library.model.annotation.Required;

public class ClienteModel extends BelcorpModel implements Serializable, Parcelable {

    private Integer id = 0;
    private Integer clienteID = 0;
    @Required(message = "Debe ingresar el nombre")
    private String nombres;
    private String apellidos;
    private String alias;
    private String foto;
    @Date
    private String fechaNacimiento;
    private String sexo;
    private String documento;
    private String origen;
    private Integer favorito;
    private Integer estado;
    private Integer tipoRegistro;
    private Integer tipoContactoFavorito;
    private Boolean esSeleccionado;
    private Integer sincronizado;
    private BigDecimal totalDeuda;
    private Integer cantidadProductos;
    private Integer cantidadPedido;
    private BigDecimal montoPedido;
    private String codigoRespuesta;
    private String mensajeRespuesta;

    private List<ContactoModel> contactoModels = null;
    private List<AnotacionModel> anotacionModels = null;
    private List<RecordatorioModel> recordatorioModels = null;
    private List<ProductItem> orderList = null;

    private Map<String, Object> additionalProperties = new HashMap<>();
    private Map<Integer, ContactoModel> contactoModelMap = null;

    public ClienteModel() {
    }

    protected ClienteModel(Parcel in) {
        id = in.readInt();
        clienteID = in.readInt();
        nombres = in.readString();
        apellidos = in.readString();
    }

    public static final Creator<ClienteModel> CREATOR = new Creator<ClienteModel>() {
        @Override
        public ClienteModel createFromParcel(Parcel in) {
            return new ClienteModel(in);
        }

        @Override
        public ClienteModel[] newArray(int size) {
            return new ClienteModel[size];
        }
    };

    public ClienteModel(int clienteLocalID, int clienteID, String nombres) {
        this.id = clienteLocalID;
        this.clienteID = clienteID;
        this.nombres = nombres;
        this.origen = GlobalConstant.APP_CODE;
        this.setEsSeleccionado(false);
    }

    public ClienteModel(int clienteID, String nombres) {
        this.clienteID = clienteID;
        this.nombres = nombres;
        this.origen = GlobalConstant.APP_CODE;
        this.setEsSeleccionado(false);
    }

    public ClienteModel(String nombres) {
        this.nombres = nombres;
        this.origen = GlobalConstant.APP_CODE;
        this.setEsSeleccionado(false);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClienteID() {
        return clienteID;
    }

    public void setClienteID(Integer clienteID) {
        this.clienteID = clienteID;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public Integer getFavorito() {
        return favorito;
    }

    public void setFavorito(Integer favorito) {
        this.favorito = favorito;
    }

    public Integer getEstado() {
        return estado;
    }

    public Integer getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Integer tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Boolean getEsSeleccionado() {
        return esSeleccionado;
    }

    public void setEsSeleccionado(Boolean esSeleccionado) {
        this.esSeleccionado = esSeleccionado;
    }

    public Integer getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(Integer cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public String getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    public void setMensajeRespuesta(String mensajeRespuesta) {
        this.mensajeRespuesta = mensajeRespuesta;
    }

    public List<ContactoModel> getContactoModels() {
        return contactoModels;
    }

    public void setContactoModels(List<ContactoModel> contactoModels) {
        this.contactoModels = contactoModels;
        contactoModelMap = new HashMap<>();
        for (ContactoModel i : contactoModels) {
            contactoModelMap.put(i.getTipoContactoID(), i);
        }
    }

    public List<AnotacionModel> getAnotacionModels() {
        return anotacionModels;
    }

    public void setAnotacionModels(List<AnotacionModel> anotacionModels) {
        this.anotacionModels = anotacionModels;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Map<Integer, ContactoModel> getContactoModelMap() {
        return contactoModelMap;
    }

    public void setContactoModelMap(Map<Integer, ContactoModel> contactoModelMap) {
        this.contactoModelMap = contactoModelMap;
    }

    public Integer getTipoContactoFavorito() {
        return tipoContactoFavorito;
    }

    public void setTipoContactoFavorito(Integer tipoContactoFavorito) {
        this.tipoContactoFavorito = tipoContactoFavorito;
    }

    public List<RecordatorioModel> getRecordatorioModels() {
        return recordatorioModels;
    }

    public void setRecordatorioModels(List<RecordatorioModel> recordatorioModels) {
        this.recordatorioModels = recordatorioModels;
    }

    public BigDecimal getTotalDeuda() {
        return totalDeuda;
    }

    public void setTotalDeuda(BigDecimal totalDeuda) {
        this.totalDeuda = totalDeuda;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }

    public Integer getCantidadPedido() {
        return cantidadPedido;
    }

    public void setCantidadPedido(Integer cantidadPedido) {
        this.cantidadPedido = cantidadPedido;
    }

    public BigDecimal getMontoPedido() {
        return montoPedido;
    }

    public void setMontoPedido(BigDecimal montoPedido) {
        this.montoPedido = montoPedido;
    }

    public List<ProductItem> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<ProductItem> orderList) {
        this.orderList = orderList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id != null ? id : 0);
        dest.writeInt(clienteID != null ? clienteID : 0);
        dest.writeString(nombres != null ? nombres : "");
        dest.writeString(apellidos != null ? apellidos : "");

    }
}

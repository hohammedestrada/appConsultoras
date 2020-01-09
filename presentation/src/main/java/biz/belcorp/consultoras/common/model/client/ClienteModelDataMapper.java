package biz.belcorp.consultoras.common.model.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import biz.belcorp.consultoras.common.model.product.ProductItemModelDataMapper;
import biz.belcorp.consultoras.common.model.recordatorio.RecordatorioModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Anotacion;
import biz.belcorp.consultoras.domain.entity.Cliente;
import biz.belcorp.consultoras.domain.entity.Contacto;
import biz.belcorp.consultoras.domain.entity.Recordatorio;

@PerActivity
public class ClienteModelDataMapper {

    private ContactoModelDataMapper contactoModelDataMapper;
    private AnotacionModelDataMapper anotacionModelDataMapper;
    private RecordatorioModelDataMapper recordatorioModelDataMapper;
    private ProductItemModelDataMapper productItemModelDataMapper;

    @Inject
    ClienteModelDataMapper(ContactoModelDataMapper contactoModelDataMapper,
                           AnotacionModelDataMapper anotacionModelDataMapper,
                           RecordatorioModelDataMapper recordatorioModelDataMapper,
                           ProductItemModelDataMapper productItemModelDataMapper){
        this.contactoModelDataMapper = contactoModelDataMapper;
        this.anotacionModelDataMapper = anotacionModelDataMapper;
        this.recordatorioModelDataMapper = recordatorioModelDataMapper;
        this.productItemModelDataMapper = productItemModelDataMapper;
    }

    public ClienteModel transform(Cliente input){
        ClienteModel output = null;

        if(null != input){

            output = new ClienteModel();
            output.setOrigen(input.getOrigen());
            output.setClienteID(input.getClienteID());
            output.setApellidos(input.getApellidos());
            output.setNombres(input.getNombres());
            output.setAlias(input.getAlias());
            output.setFoto(input.getFoto());
            output.setFechaNacimiento(input.getFechaNacimiento());
            output.setSexo(input.getSexo());
            output.setDocumento(input.getDocumento());
            output.setFavorito(input.getFavorito());
            output.setEstado(input.getEstado());
            output.setTotalDeuda(input.getTotalDeuda());
            output.setTipoRegistro(input.getTipoRegistro());
            output.setTipoContactoFavorito(input.getTipoContactoFavorito());
            output.setContactoModels(contactoModelDataMapper.transform(input.getContactos()));
            output.setAnotacionModels(anotacionModelDataMapper.transform(input.getAnotaciones()));
            output.setRecordatorioModels(recordatorioModelDataMapper.transform(input.getRecordatorios()));
            output.setOrderList(productItemModelDataMapper.transform(input.getOrderList()));
            output.setId(input.getId());
            output.setSincronizado(input.getSincronizado());
            output.setCantidadProductos(input.getCantidadProductos());
            output.setCantidadPedido(input.getCantidadPedido());
            output.setMontoPedido(input.getMontoPedido());
            output.setCodigoRespuesta(input.getCodigoRespuesta());
            output.setMensajeRespuesta(input.getMensajeRespuesta());
        }
        return output;
    }

    public Cliente transform(ClienteModel input){
        Cliente output = null;

        if(null != input){
            output = new Cliente();
            output.setOrigen(input.getOrigen());
            output.setClienteID(input.getClienteID());
            output.setApellidos(input.getApellidos());
            output.setNombres(input.getNombres());
            output.setAlias(input.getAlias());
            output.setFoto(input.getFoto());
            output.setFechaNacimiento(input.getFechaNacimiento());
            output.setSexo(input.getSexo());
            output.setDocumento(input.getDocumento());
            output.setFavorito(input.getFavorito());
            output.setEstado(input.getEstado());
            output.setTotalDeuda(input.getTotalDeuda());
            output.setTipoRegistro(input.getTipoRegistro());
            output.setTipoContactoFavorito(input.getTipoContactoFavorito());
            output.setContactos((List<Contacto>) contactoModelDataMapper.transform(input.getContactoModels()));
            output.setAnotaciones((List<Anotacion>) anotacionModelDataMapper.transform(input.getAnotacionModels()));
            output.setRecordatorios((List<Recordatorio>) recordatorioModelDataMapper.transform(input.getRecordatorioModels()));
            output.setId(input.getId());
            output.setSincronizado(input.getSincronizado());
            output.setCantidadProductos(input.getCantidadProductos());
            output.setCantidadPedido(input.getCantidadPedido());
            output.setMontoPedido(input.getMontoPedido());
            output.setCodigoRespuesta(input.getCodigoRespuesta());
            output.setMensajeRespuesta(input.getMensajeRespuesta());
        }
        return output;
    }

    public List<ClienteModel> transform(Collection<Cliente> input) {
        List<ClienteModel> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (Cliente entity : input) {
            final ClienteModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }

    public Collection<Cliente> transform(List<ClienteModel> input) {
        List<Cliente> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (ClienteModel entity : input) {
            final Cliente model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }

    public Collection<ClienteModel> transform(Map<String, ClienteModel> map){
        List<ClienteModel> output = new ArrayList<>();

        if(map != null && !map.isEmpty()) {

            Map<String, ClienteModel> treeMap = new TreeMap<>(map);
            for (String key : treeMap.keySet()) {
                output.add(map.get(key));
            }
        }

        return output;
    }
}

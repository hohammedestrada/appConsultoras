package biz.belcorp.consultoras.common.model.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.common.model.product.ProductModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.ClientMovement;

@PerActivity
public class ClientMovementModelDataMapper {

    private ProductModelDataMapper productModelDataMapper;

    @Inject
    ClientMovementModelDataMapper(ProductModelDataMapper productModelDataMapper){
        this.productModelDataMapper = productModelDataMapper;
    }

    public ClientMovementModel transform(ClientMovement input){
        ClientMovementModel output = null;

        if(null != input){
            output = new ClientMovementModel();
            output.setMovementID(input.getMovementID());
            output.setClientID(input.getClientID());
            output.setId(input.getId());
            output.setClienteLocalID(input.getClienteLocalID());
            output.setSincronizado(input.getSincronizado());
            output.setClientCode(input.getClientCode());
            output.setAmount(input.getAmount());
            output.setType(input.getType());
            output.setDescription(input.getDescription());
            output.setCampaing(input.getCampaing());
            output.setNote(input.getNote());
            output.setDate(input.getDate());
            output.setSaldo(input.getSaldo());
            output.setEstado(input.getEstado());
            output.setCode(input.getCode());
            output.setMessage(input.getMessage());
            output.setProductModels(productModelDataMapper.transform(input.getProductMovements()));
        }
        return output;
    }

    public ClientMovement transform(ClientMovementModel input){
        ClientMovement output = null;

        if(null != input){
            output = new ClientMovement();
            output.setMovementID(input.getMovementID());
            output.setClientID(input.getClientID());
            output.setId(input.getId());
            output.setClienteLocalID(input.getClienteLocalID());
            output.setSincronizado(input.getSincronizado());
            output.setClientCode(input.getClientCode());
            output.setAmount(input.getAmount());
            output.setType(input.getType());
            output.setDescription(input.getDescription());
            output.setCampaing(input.getCampaing());
            output.setNote(input.getNote());
            output.setDate(input.getDate());
            output.setSaldo(input.getSaldo());
            output.setEstado(input.getEstado());
            output.setCode(input.getCode());
            output.setMessage(input.getMessage());
            output.setProductMovements(productModelDataMapper.transformModel(input.getProductModels()));
        }
        return output;
    }

    public List<ClientMovementModel> transform(Collection<ClientMovement> input) {
        List<ClientMovementModel> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (ClientMovement entity : input) {
            final ClientMovementModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }

    public Collection<ClientMovement> transform(List<ClientMovementModel> input) {
        List<ClientMovement> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (ClientMovementModel entity : input) {
            final ClientMovement model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }
}

package biz.belcorp.consultoras.common.model.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Anotacion;

@PerActivity
public class AnotacionModelDataMapper {

    @Inject
    AnotacionModelDataMapper(){
    }

    public AnotacionModel transform(Anotacion input){
        AnotacionModel output = null;

        if(null != input){
            output = new AnotacionModel();
            output.setAnotacionID(input.getAnotacionID());
            output.setDescripcion(input.getDescripcion());
            output.setEstado(input.getEstado());
            output.setClienteID(input.getClienteID());
            output.setFecha(input.getFecha());
            output.setId(input.getId());
            output.setClienteLocalID(input.getClienteLocalID());
            output.setSincronizado(input.getSincronizado());
        }
        return output;
    }

    public Anotacion transform(AnotacionModel input){
        Anotacion output = null;

        if(null != input){
            output = new Anotacion();
            output.setAnotacionID(input.getAnotacionID());
            output.setDescripcion(input.getDescripcion());
            output.setEstado(input.getEstado());
            output.setClienteID(input.getClienteID());
            output.setFecha(input.getFecha());
            output.setId(input.getId());
            output.setClienteLocalID(input.getClienteLocalID());
            output.setSincronizado(input.getSincronizado());
        }
        return output;
    }

    public List<AnotacionModel> transform(Collection<Anotacion> input) {
        List<AnotacionModel> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (Anotacion entity : input) {
            final AnotacionModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }

    public Collection<Anotacion> transform(List<AnotacionModel> input) {
        List<Anotacion> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (AnotacionModel entity : input) {
            final Anotacion model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }
}

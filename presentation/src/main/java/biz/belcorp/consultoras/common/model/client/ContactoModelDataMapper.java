package biz.belcorp.consultoras.common.model.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Contacto;

@PerActivity
public class ContactoModelDataMapper {

    @Inject
    ContactoModelDataMapper(){
    }

    public ContactoModel transform(Contacto input){
        ContactoModel output = null;

        if(null != input){
            output = new ContactoModel();
            output.setContactoClienteID(input.getContactoClienteID());
            output.setClienteID(input.getClienteID());
            output.setTipoContactoID(input.getTipoContactoID());
            output.setValor(input.getValor());
            output.setEstado(input.getEstado());
            output.setId(input.getId());
        }
        return output;
    }

    public Contacto transform(ContactoModel input){
        Contacto output = null;

        if(null != input){
            output = new Contacto();
            output.setContactoClienteID(input.getContactoClienteID());
            output.setClienteID(input.getClienteID());
            output.setTipoContactoID(input.getTipoContactoID());
            output.setValor(input.getValor());
            output.setEstado(input.getEstado());
            output.setId(input.getId());
        }
        return output;
    }

    public List<ContactoModel> transform(Collection<Contacto> input) {
        List<ContactoModel> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (Contacto entity : input) {
            final ContactoModel model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }

    public Collection<Contacto> transform(List<ContactoModel> input) {
        List<Contacto> output = new ArrayList<>();

        if (null == input) {
            return Collections.emptyList();
        }

        for (ContactoModel entity : input) {
            final Contacto model = transform(entity);
            if (null != model) {
                output.add(model);
            }
        }

        return output;
    }
}

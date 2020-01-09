package biz.belcorp.consultoras.common.model.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.data.entity.AssociateRequestEntity;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.AssociateRequest;

/**
 * Clase encarga de realizar el mapeo de la entidad loginOnline(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-14
 */
@PerActivity
public class AssociateModelDataMapper {

    @Inject
    AssociateModelDataMapper() {
    }

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param model Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    public AssociateRequest transform(AssociateModel model) {
        AssociateRequest associateRequest = null;

        if (null != model) {
            associateRequest = new AssociateRequest();
            associateRequest.setCodigoUsuario(model.getCodigoUsuario());
            associateRequest.setClaveSecreta(model.getClaveSecreta());
            associateRequest.setGrandType(model.getGrandType());
            associateRequest.setTipoAutenticacion(model.getTipoAutenticacion());
            associateRequest.setPaisISO(model.getPaisISO());
            associateRequest.setIdAplicacion(model.getIdAplicacion());
            associateRequest.setFotoPerfil(model.getFotoPerfil());
            associateRequest.setLogin(model.getLogin());
            associateRequest.setCorreo(model.getCorreo());
            associateRequest.setNombres(model.getNombres());
            associateRequest.setApellidos(model.getApellidos());
            associateRequest.setLinkPerfil(model.getLinkPerfil());
            associateRequest.setFechaNacimiento(model.getFechaNacimiento());
            associateRequest.setGenero(model.getGenero());
            associateRequest.setUbicacion(model.getUbicacion());
            associateRequest.setProveedor(model.getProveedor());
            associateRequest.setRefreshToken(model.getRefreshToken());
            associateRequest.setAuthorization(model.getAuthorization());

        }
        return associateRequest;
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param entity Entidad de dominio
     * @return object Entidad
     */
    public AssociateModel transform(AssociateRequest entity) {
        AssociateModel associate = null;

        if (null != entity) {
            associate = new AssociateModel();
            associate.setCodigoUsuario(entity.getCodigoUsuario());
            associate.setClaveSecreta(entity.getClaveSecreta());
            associate.setGrandType(entity.getGrandType());
            associate.setTipoAutenticacion(entity.getTipoAutenticacion());
            associate.setPaisISO(entity.getPaisISO());
            associate.setIdAplicacion(entity.getIdAplicacion());
            associate.setFotoPerfil(entity.getFotoPerfil());
            associate.setLogin(entity.getLogin());
            associate.setCorreo(entity.getCorreo());
            associate.setNombres(entity.getNombres());
            associate.setApellidos(entity.getApellidos());
            associate.setLinkPerfil(entity.getLinkPerfil());
            associate.setFechaNacimiento(entity.getFechaNacimiento());
            associate.setGenero(entity.getGenero());
            associate.setUbicacion(entity.getUbicacion());
            associate.setProveedor(entity.getProveedor());
            associate.setRefreshToken(entity.getRefreshToken());
            associate.setAuthorization(entity.getAuthorization());

        }
        return associate;
    }

    /**
     * Transforma una lista de entidades a una lista de entidades de dominio
     *
     * @param list Lista de entidades
     * @return list Lista de entidades del dominio
     */
    public List<AssociateModel> transform(Collection<AssociateRequest> list) {
        List<AssociateModel> associateRequests = new ArrayList<>();

        if (null == list) {
            return Collections.emptyList();
        }

        for (AssociateRequest entity : list) {
            final AssociateModel associate = transform(entity);
            if (null != associate) {
                associateRequests.add(associate);
            }
        }

        return associateRequests;
    }

}

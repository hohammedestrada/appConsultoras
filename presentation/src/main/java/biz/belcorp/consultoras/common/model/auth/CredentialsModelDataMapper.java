package biz.belcorp.consultoras.common.model.auth;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Credentials;

/**
 * Clase encarga de realizar el mapeo de la entidad credentials(tabla o json) a
 * una entidad del dominio
 *
 * @version 1.0
 * @since 2017-04-14
 */
@PerActivity
public class CredentialsModelDataMapper {

    @Inject
    CredentialsModelDataMapper() {
    }

    /**
     * Transforma la entidad a una entidad del dominio.
     *
     * @param entity Entidad tipo tabla o json
     * @return object Entidad de dominio
     */
    public Credentials transform(CredentialsModel entity) {
        Credentials credentialsRequest = null;

        if (null != entity) {
            credentialsRequest = new Credentials();
            credentialsRequest.setPais(entity.getPais());
            credentialsRequest.setUsername(entity.getUsername());
            credentialsRequest.setPassword(entity.getPassword());
            credentialsRequest.setTipoAutenticacion(entity.getTipoAutenticacion());
        }
        return credentialsRequest;
    }

    /**
     * Transforma la entidad de dominio a una entidad.
     *
     * @param entity Entidad de dominio
     * @return object Entidad
     */
    public CredentialsModel transform(Credentials entity) {
        CredentialsModel credentialsModel = null;

        if (null != entity) {
            credentialsModel = new CredentialsModel();
            credentialsModel.setPais(entity.getPais());
            credentialsModel.setUsername(entity.getUsername());
            credentialsModel.setPassword(entity.getPassword());
            credentialsModel.setTipoAutenticacion(entity.getTipoAutenticacion());
        }
        return credentialsModel;
    }

}

package biz.belcorp.consultoras.common.model.facebook;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.FacebookProfile;

@PerActivity
public class FacebookProfileModelDataMapper {

    @Inject
    FacebookProfileModelDataMapper() {
    }

    public FacebookProfile transform(FacebookProfileModel model) {
        FacebookProfile entity = null;

        if (null != model) {
            entity = new FacebookProfile();
            entity.setId(model.getId());
            entity.setName(model.getName());
            entity.setEmail(model.getEmail());
            entity.setImage(model.getImage());
            entity.setFirstName(model.getFirstName());
            entity.setLastName(model.getLastName());
            entity.setLinkProfile(model.getLinkProfile());
            entity.setBirthday(model.getBirthday());
            entity.setGender(model.getGender());
            entity.setLocation(model.getLocation());
        }
        return entity;
    }

    public FacebookProfileModel transform(FacebookProfile entity) {
        FacebookProfileModel model = null;

        if (null != entity) {
            model = new FacebookProfileModel();
            model.setId(entity.getId());
            model.setName(entity.getName());
            model.setEmail(entity.getEmail());
            model.setImage(entity.getImage());
            model.setFirstName(entity.getFirstName());
            model.setLastName(entity.getLastName());
            model.setLinkProfile(entity.getLinkProfile());
            model.setBirthday(entity.getBirthday());
            model.setGender(entity.getGender());
            model.setLocation(entity.getLocation());
        }
        return model;
    }

}

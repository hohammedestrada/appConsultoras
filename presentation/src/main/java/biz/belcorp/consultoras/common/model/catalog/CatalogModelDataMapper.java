package biz.belcorp.consultoras.common.model.catalog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Catalogo;
import biz.belcorp.consultoras.domain.entity.CatalogoWrapper;

/**
 *
 */
@PerActivity
public class CatalogModelDataMapper {

    @Inject
    CatalogModelDataMapper() {
    }

    public List<CatalogByCampaignModel> transformCatalog(List<CatalogoWrapper> input) {
        if (input == null) return Collections.emptyList();

        List<CatalogByCampaignModel> output = new ArrayList<>();
        for (CatalogoWrapper catalogoWrapper : input) {
            output.add(transform(catalogoWrapper));
        }
        return output;
    }

    private CatalogByCampaignModel transform(CatalogoWrapper input) {
        CatalogByCampaignModel output = new CatalogByCampaignModel();
        output.setCampaignName(input.getCampaignName());
        output.setCatalogoEntities(transform(input.getCatalogoEntities()));
        output.setMagazineEntity(transform(input.getMagazineEntity()));

        return output;
    }

    private CatalogModel transform(Catalogo input) {
        if (input == null) return new CatalogModel();

        CatalogModel output = new CatalogModel();
        output.setMarcaId(input.getMarcaId());
        output.setMarcaDescripcion(input.getMarcaDescripcion());
        output.setUrlImagen(input.getUrlImagen());
        output.setUrlCatalogo(input.getUrlCatalogo());
        output.setTitulo(input.getTitulo());
        output.setDescripcion(input.getDescripcion());
        output.setCampaniaId(input.getCampaniaId());
        output.setUrlDescargaEstado(input.getUrlDescargaEstado());

        return output;
    }

    private List<CatalogModel> transform(List<Catalogo> input) {
        if (input == null) return Collections.emptyList();

        List<CatalogModel> output = new ArrayList<>();

        for (Catalogo catalogo : input) {
            CatalogModel catalog = transform(catalogo);
            output.add(catalog);
        }

        return output;
    }
}

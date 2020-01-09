package biz.belcorp.consultoras.common.model.product;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.OrderListItem;
import biz.belcorp.consultoras.domain.entity.PedidoComponentes;
import biz.belcorp.consultoras.domain.entity.ProductMovement;

/**
 *
 */
@PerActivity
public class ProductModelDataMapper {

    @Inject
    public ProductModelDataMapper() {
        // EMPTY
    }

    public List<ProductMovement> transformModel(List<ProductModel> products) {
        List<ProductMovement> productMovementModel = new ArrayList<>();

        for (ProductModel product : products) {
            productMovementModel.add(transform(product));
        }

        return productMovementModel;
    }

    public List<ProductModel> transform(List<ProductMovement> productMovements) {
        List<ProductModel> productModel = new ArrayList<>();

        for (ProductMovement productMovement : productMovements) {
            productModel.add(transform(productMovement));
        }

        return productModel;
    }

    public ProductModel transform(ProductMovement productMovement) {
        ProductModel productModel = new ProductModel();
        productModel.setCode(productMovement.getProductID());
        productModel.setName(productMovement.getName());
        productModel.setPrice(productMovement.getPrice());
        productModel.setQuantity(productMovement.getQuantity());
        productModel.setCuv(productMovement.getCode());

        return productModel;
    }

    public ProductMovement transform(ProductModel product) {
        ProductMovement productMovementModel = new ProductMovement();
        productMovementModel.setProductID(product.getCode());
        productMovementModel.setName(product.getName());
        productMovementModel.setPrice(product.getPrice());
        productMovementModel.setQuantity(product.getQuantity());
        productMovementModel.setCode(product.getCuv());

        return productMovementModel;
    }

    public ComponentItem transform(PedidoComponentes component){
        return new ComponentItem(
            component.getSetDetalleId(),
            component.getSetId(),
            component.getCuv(),
            component.getNombreProducto(),
            component.getCantidad(),
            component.getFactorRepetecion(),
            component.getPrecioUnidad());
    }

    public List<ComponentItem> transformComponents(List<PedidoComponentes> pedidoComponents) {
        List<ComponentItem> components = new ArrayList<>();
        for (PedidoComponentes pedidoComponent : pedidoComponents) {
            components.add(transform(pedidoComponent));
        }
        return components;
    }

    public ProductItem transform(OrderListItem product){

        return new ProductItem(product.getId(),
            product.getCuv(),
            product.getDescripcionProd(),
            product.getDescripcionCortaProd(),
            product.getCantidad(),
            product.getPrecioUnidad(),
            product.getImporteTotal(),
            product.getClienteID(),
            product.getClienteLocalID(),
            product.getNombreCliente(),
            product.getSubido(),
            product.isEsKitNueva(),
            product.getTipoEstrategiaID(),
            product.getTipoOfertaSisID(),
            product.getObservacionPROL(),
            product.getObservacionPROLType(),
            product.getObservacionPROLList(),
            product.getEtiquetaProducto(),
            product.getIndicadorOfertaCUV(),
            product.getMensajeError(),
            product.getSetID(),
            product.isEsBackOrder(),
            product.isAceptoBackOrder(),
            product.isFlagNueva(),
            product.isEnRangoProgNuevas(),
            product.isEsDuoPerfecto(),
            product.isEsPremioElectivo(),
            product.isArmaTuPack(),
            product.getTipoOferta(),
            transformComponents(product.getComponentes()),
            product.isKitCaminoBrillante(),
            product.isDeleteKit(),
            product.isPromocion(),
            product.getObservacionPromociones(),
            product.isFestival(),
            product.getFlagFestival()
        );
    }

}

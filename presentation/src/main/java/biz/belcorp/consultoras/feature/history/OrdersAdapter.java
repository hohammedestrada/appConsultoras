package biz.belcorp.consultoras.feature.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.product.ProductModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersHolder> {

    private List<ProductModel> productModels = new ArrayList<>();

    private String currencySymbol;
    private DecimalFormat decimalFormat;

    @Override
    public OrdersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrdersHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido_movement, parent, false));
    }

    @Override
    public void onBindViewHolder(OrdersHolder holder, int position) {
        ProductModel productModel = productModels.get(position);

        BigDecimal decimal = BigDecimal.valueOf(productModel.getPrice());

        holder.quantity.setText(String.format("controlAnimation%d", productModel.getQuantity()));
        holder.code.setText(String.format("%05d", productModel.getCode()));
        holder.product.setText(productModel.getName());
        holder.cost.setText(String.format(Locale.getDefault(), "%1$s %2$s", currencySymbol, decimalFormat.format(decimal)));
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    void setProductModels(List<ProductModel> productModels) {
        this.productModels.clear();
        this.productModels.addAll(productModels);
    }

    void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    class OrdersHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvw_cantidad)
        TextView quantity;
        @BindView(R.id.tvw_code)
        TextView code;
        @BindView(R.id.tvw_producto)
        TextView product;
        @BindView(R.id.tvw_costo)
        TextView cost;

        private OrdersHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}

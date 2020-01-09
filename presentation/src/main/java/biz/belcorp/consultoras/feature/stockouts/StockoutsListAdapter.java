package biz.belcorp.consultoras.feature.stockouts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.component.CustomTypefaceSpan;
import biz.belcorp.consultoras.domain.entity.Product;
import biz.belcorp.consultoras.util.GlobalConstant;
import butterknife.BindView;
import butterknife.ButterKnife;


public class StockoutsListAdapter extends RecyclerView.Adapter<StockoutsListAdapter.StockoutsItemHolder>
    implements Filterable {

    private static final String TAG = "StockoutsListAdapter";
    private Context context;
    private List<Product> allItems;
    private List<Product> filteredItems;
    private OnStockoutsItemListener listener;

    StockoutsListAdapter(Context context, List<Product> list, OnStockoutsItemListener listener) {
        this.context = context;
        this.allItems = list;
        this.filteredItems = list;
        this.listener = listener;
    }

    class StockoutsItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvw_title)
        TextView tvwTitle;
        @BindView(R.id.tvw_description)
        TextView tvwDescription;

        StockoutsItemHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

    }

    @Override
    public StockoutsItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_stockout, viewGroup, false);
        return new StockoutsItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StockoutsItemHolder holder, int position) {
        final Product model = filteredItems.get(position);

        Typeface tfRegular = Typeface.createFromAsset(context.getAssets(), GlobalConstant.LATO_REGULAR_SOURCE);
        Typeface tfBold = Typeface.createFromAsset(context.getAssets(), GlobalConstant.LATO_BOLD_SOURCE);

        SpannableStringBuilder spannable = new SpannableStringBuilder();

        Spannable spannableBold = Spannable.Factory.getInstance().newSpannable(model.getCuv());
        spannableBold.setSpan(new CustomTypefaceSpan(tfBold), 0, model.getCuv().length(), 0);
        spannable.append(spannableBold);

        spannable.append(" ");

        Spannable spannableLight = Spannable.Factory.getInstance().newSpannable(WordUtils.capitalize(model.getDescription().toLowerCase()));
        spannableLight.setSpan(new CustomTypefaceSpan(tfRegular), 0, model.getDescription().length(), 0);
        spannable.append(spannableLight);

        String description = model.getCatalogo() + " - " + model.getCategoria() + " - Pág. " + model.getNumeroPagina();
        holder.tvwTitle.setText(spannable);
        holder.tvwDescription.setText(description);
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence cs) {
                FilterResults results = new FilterResults();
                if (cs == null || cs.length() == 0) {
                    results.values = allItems;
                    results.count = allItems.size();
                } else {
                    List<Product> filterResultsItems = new ArrayList<>();

                    for (Product data : allItems) {
                        String dataItem = data.getCuv() + " " + data.getDescription();

                        if (dataItem.toLowerCase().contains(cs.toString().toLowerCase())) {
                            filterResultsItems.add(data);
                        }
                    }

                    results.values = filterResultsItems;
                    results.count = filterResultsItems.size();
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredItems = (ArrayList<Product>) filterResults.values;
                listener.refreshCounter();
                notifyDataSetChanged();
            }
        };
    }

    interface OnStockoutsItemListener {
        void refreshCounter();
    }

}

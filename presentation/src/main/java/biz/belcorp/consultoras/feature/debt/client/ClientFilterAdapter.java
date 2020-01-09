package biz.belcorp.consultoras.feature.debt.client;

import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
class ClientFilterAdapter extends RecyclerView.Adapter<ClientFilterAdapter.FilterHolder> implements Filterable {

    private List<ClienteModel> clientModels = new ArrayList<>();
    private List<ClienteModel> filteredClientModels = new ArrayList<>();

    private final OnClientFilteredClick onClientFilteredClick;

    private long mLastClickTime = 0;

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            ClienteModel clienteModel = (ClienteModel) v.getTag();
            onClientFilteredClick.onClick(clienteModel);
        }
    };

    ClientFilterAdapter(OnClientFilteredClick onClientFilteredClick) {
        this.onClientFilteredClick = onClientFilteredClick;
    }

    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(FilterHolder holder, int position) {
        ClienteModel clientModel = filteredClientModels.get(position);

        String name = clientModel.getNombres() + (clientModel.getApellidos() == null ? "" : " " + clientModel.getApellidos());
        name = name.trim();
        String[] parts = name.split(Pattern.quote(" "));

        if (!TextUtils.isEmpty(name)) {
            if (parts.length >= 2)
                holder.tvwTitle.setText(String.format("%1$s %2$s", parts[0], parts[1]));
            else
                holder.tvwTitle.setText(name);
        }

        holder.tvwTitle.setTag(clientModel);
        holder.tvwTitle.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return filteredClientModels.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence cs) {
                FilterResults results = new FilterResults();
                if (TextUtils.isEmpty(cs)) {
                    results.values = clientModels;
                    results.count = clientModels.size();
                } else {
                    List<ClienteModel> filterResultsItems = new ArrayList<>();

                    for (ClienteModel data : clientModels) {
                        String dataItem = data.getNombres();

                        if (dataItem.toLowerCase().contains(cs.toString().toLowerCase())) {
                            filterResultsItems.add(data);
                        }
                    }

                    results.values = filterResultsItems;
                    results.count = filterResultsItems.size();
                }
                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredClientModels = (ArrayList<ClienteModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    void setClientModels(List<ClienteModel> clientModels) {
        this.clientModels.clear();
        this.filteredClientModels.clear();

        this.clientModels.addAll(clientModels);
        this.filteredClientModels.addAll(clientModels);
        notifyDataSetChanged();
    }

    class FilterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvw_title)
        TextView tvwTitle;

        private FilterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface OnClientFilteredClick {
        void onClick(ClienteModel clientModel);
    }
}

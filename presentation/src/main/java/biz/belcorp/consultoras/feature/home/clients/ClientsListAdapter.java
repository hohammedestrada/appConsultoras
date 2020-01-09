package biz.belcorp.consultoras.feature.home.clients;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientsListAdapter extends RecyclerView.Adapter<ClientsListAdapter.ClientListHolder>
    implements Filterable {

    private static final String TAG = "ClientsListAdapter";
    private List<ClienteModel> allItems;
    private List<ClienteModel> filteredItems;
    private OnItemSelectedListener onItemSelectedListener;


    public ClientsListAdapter(List<ClienteModel> clients, OnItemSelectedListener onItemSelectedListener) {
        this.allItems = clients;
        this.filteredItems = clients;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public ClientListHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_client, viewGroup, false);
        return new ClientListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ClientListHolder holder, int position) {
        final ClienteModel model = filteredItems.get(position);

        String name = model.getNombres() + (model.getApellidos() == null ? "" : " " + model.getApellidos());
        name = name.trim();
        String[] parts = name.split(Pattern.quote(" "));

        holder.tvwClient.setText(StringUtil.getTwoFirstInitials(name));

        if (!name.equals("")) {
            if (parts.length >= 2)
                holder.tvwTitle.setText(parts[0] + " " + parts[1]);
            else
                holder.tvwTitle.setText(name);
        }

        if (null != model.getFechaNacimiento() && !"".equals(model.getFechaNacimiento())
            && model.getFechaNacimiento().length() == 10) {
            holder.rltBirthday.setVisibility(View.VISIBLE);
            try {
                String birthday = StringUtil.capitalize(DateUtil.convertFechaToString(
                    DateUtil.convertirDDMMAAAAtoDate(model.getFechaNacimiento()), "dd MMM"));
                holder.tvwDescription.setText(birthday);
            } catch (ParseException e) {
                Log.e(TAG, "ClientList", e);
            }
        } else {
            holder.rltBirthday.setVisibility(View.GONE);
        }
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
                    List<ClienteModel> filterResultsItems = new ArrayList<>();

                    for (ClienteModel data : allItems) {
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

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredItems = (ArrayList<ClienteModel>) filterResults.values;
                onItemSelectedListener.refreshClientCounter();
                notifyDataSetChanged();
            }
        };
    }

    public class ClientListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cvw_item)
        CardView cvwItem;
        @BindView(R.id.tvw_client)
        TextView tvwClient;
        @BindView(R.id.tvw_title)
        TextView tvwTitle;
        @BindView(R.id.tvw_description)
        TextView tvwDescription;
        @BindView(R.id.rlt_birthday)
        RelativeLayout rltBirthday;
        @BindView(R.id.ivw_whatstapp)
        ImageView ivwWhatsapp;
        @BindView(R.id.view_separator)
        View viewSeparator;
        @BindView(R.id.ivw_phone)
        ImageView ivwPhone;

        ClientListHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            cvwItem.setOnClickListener(v1 -> onItemSelectedListener.onClienteModelClick(filteredItems.get(getAdapterPosition())));
            cvwItem.setOnLongClickListener(v12 -> {
                onItemSelectedListener.onLongClick(filteredItems.get(getAdapterPosition()), v12);
                return true;
            });
            ivwWhatsapp.setOnClickListener(v13 -> onItemSelectedListener.onWhatsappClick(filteredItems.get(getAdapterPosition())));
            ivwPhone.setOnClickListener(v14 -> onItemSelectedListener.onPhoneClick(filteredItems.get(getAdapterPosition())));
        }

    }

    public interface OnItemSelectedListener {

        void onClienteModelClick(ClienteModel clienteModel);
        void refreshClientCounter();
        void onPhoneClick(ClienteModel clienteModel);
        void onWhatsappClick(ClienteModel clienteModel);
        void onLongClick(ClienteModel clienteModel, View v);

    }
}

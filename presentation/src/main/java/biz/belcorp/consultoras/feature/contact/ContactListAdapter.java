package biz.belcorp.consultoras.feature.contact;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.util.anotation.ClientRegisterType;
import biz.belcorp.consultoras.util.anotation.ClientStateType;
import biz.belcorp.consultoras.util.anotation.ContactType;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.Holder>
    implements Filterable {

    private Context context;
    private List<ClienteModel> allItems;
    private List<ClienteModel> filteredItems;

    private static final int HIGHLIGHT_COLOR = 0x331C3145;
    private static final int GREYLIGHT_COLOR = 0xFFF2F2F2;

    private Animation animFabOpen;
    private Animation animFabClose;

    private OnItemSelectedListener listener;

    ContactListAdapter(Context context, List<ClienteModel> clients, OnItemSelectedListener listener) {
        animFabOpen = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        animFabClose = AnimationUtils.loadAnimation(context, R.anim.fab_close);
        this.context = context;
        this.allItems = clients;
        this.filteredItems = clients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact, viewGroup, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        final ClienteModel model = filteredItems.get(position);

        String name = model.getNombres();
        holder.tvwTitle.setText(name);
        holder.tvwImage.setText(StringUtil.getTwoFirstInitials(name));

        if (null != model.getApellidos() && !"".equals(model.getApellidos())) {
            holder.rltAlert.setVisibility(View.VISIBLE);
            holder.tvwError.setText(model.getApellidos());
        } else {
            holder.rltAlert.setVisibility(View.GONE);
        }

        holder.cvwItem.setOnClickListener(v -> {

            Boolean check = !model.getEsSeleccionado();
            model.setEsSeleccionado(check);
            filteredItems.get(holder.getAdapterPosition()).setEsSeleccionado(check);
            updateAllItemsByFilteredItem(check, filteredItems.get(holder.getAdapterPosition()).getClienteID());
            updateCheckedState(holder, check);
            listener.refreshContactCounter(getNumberSelectedContacts());

        });

        setCheckedState(holder, model.getEsSeleccionado());
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
                notifyDataSetChanged();
            }
        };
    }

    private void updateCheckedState(final Holder holder, Boolean check) {

        if (check) {
            holder.ivwCheck.startAnimation(animFabOpen);
            holder.cvwItem.setBackgroundColor(HIGHLIGHT_COLOR);

            new Handler().postDelayed(() -> holder.ivwCheck.setVisibility(View.VISIBLE), 150);

        } else {
            holder.ivwCheck.startAnimation(animFabClose);
            holder.cvwItem.setBackgroundColor(GREYLIGHT_COLOR);

            new Handler().postDelayed(() -> holder.ivwCheck.setVisibility(View.INVISIBLE), 150);

        }

    }

    private void setCheckedState(final Holder holder, Boolean check) {

        if (check) {
            holder.ivwCheck.setVisibility(View.VISIBLE);
            holder.cvwItem.setBackgroundColor(HIGHLIGHT_COLOR);
        } else {
            holder.ivwCheck.setVisibility(View.INVISIBLE);
            holder.cvwItem.setBackgroundColor(GREYLIGHT_COLOR);
        }

    }

    void selectAll() {
        for (int i = 0; i < filteredItems.size(); i++) {
            filteredItems.get(i).setEsSeleccionado(true);
        }
        for (int i = 0; i < allItems.size(); i++) {
            allItems.get(i).setEsSeleccionado(true);
        }
        notifyDataSetChanged();
    }

    void unselectAll() {

        if (filteredItems != null)
            for (int i = 0; i < filteredItems.size(); i++) {
                filteredItems.get(i).setEsSeleccionado(false);
            }

        if (allItems != null)
            for (int i = 0; i < allItems.size(); i++) {
                allItems.get(i).setEsSeleccionado(false);
            }

        notifyDataSetChanged();
    }

    List<ClienteModel> getContactsSelect() {

        ArrayList<ClienteModel> list = new ArrayList<>();

        for (int i = 0; i < allItems.size(); i++) {
            if (allItems.get(i).getEsSeleccionado()) {
                ClienteModel model = new ClienteModel();
                model.setNombres(allItems.get(i).getNombres());
                model.setContactoModels(allItems.get(i).getContactoModels());
                model.setEstado(ClientStateType.INSERT_UPDATE);
                model.setTipoRegistro(ClientRegisterType.ALL);
                model.setOrigen(GlobalConstant.APP_CODE);
                model.setTipoContactoFavorito(ContactType.DEFAULT);
                model.setFavorito(0);
                model.setClienteID(0);
                list.add(model);
            }
        }

        return list;
    }

    int getNumberSelectedContacts() {

        int count = 0;

        for (int i = 0; i < allItems.size(); i++)
            if (allItems.get(i).getEsSeleccionado())
                count += 1;

        return count;
    }

    private void updateAllItemsByFilteredItem(Boolean selected, Integer id) {
        for (int j = 0; j < allItems.size(); j++) {
            if (allItems.get(j).getClienteID().equals(id)) {
                allItems.get(j).setEsSeleccionado(selected);
            }
        }
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.cvw_item)
        CardView cvwItem;
        @BindView(R.id.tvw_image)
        TextView tvwImage;
        @BindView(R.id.ivw_check)
        ImageView ivwCheck;
        @BindView(R.id.tvw_title)
        TextView tvwTitle;
        @BindView(R.id.rlt_alert)
        RelativeLayout rltAlert;
        @BindView(R.id.tvw_error)
        TextView tvwError;
        @BindView(R.id.ivw_alert)
        ImageView ivwAlert;

        public Holder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

    }

    public interface OnItemSelectedListener {
        void refreshContactCounter(int count);
    }
}

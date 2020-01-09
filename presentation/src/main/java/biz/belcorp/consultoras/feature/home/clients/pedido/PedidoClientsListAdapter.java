package biz.belcorp.consultoras.feature.home.clients.pedido;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PedidoClientsListAdapter extends RecyclerView.Adapter<PedidoClientsListAdapter.ViewHolder> {

    private static final String TAG = "PedidoListAdapter";
    private Context context;
    private List<ClienteModel> allItems = new ArrayList<>();
    private List<ClienteModel> filteredItems = new ArrayList<>();
    private OnItemSelectedListener onItemSelectedListener;
    private String moneySymbol;
    private DecimalFormat decimalFormat;

    public PedidoClientsListAdapter(Context context, List<ClienteModel> clients, OnItemSelectedListener onItemSelectedListener, String moneySymbol, DecimalFormat decimalFormat) {
        this.context = context;
        this.allItems = clients;
        this.filteredItems = clients;
        this.onItemSelectedListener = onItemSelectedListener;
        this.moneySymbol = moneySymbol;
        this.decimalFormat = decimalFormat;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_client_pedido, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
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

        String debt = decimalFormat.format(model.getMontoPedido());
        holder.tvwMonto.setText(moneySymbol + " " + debt);
        holder.tvwPedidos.setText(model.getCantidadPedido() + " " + holder.tvwTitle.getContext().getString(R.string.client_cantidad_pedidos));
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cvw_item)
        CardView cvwItem;
        @BindView(R.id.tvw_client)
        TextView tvwClient;
        @BindView(R.id.tvw_title)
        TextView tvwTitle;
        @BindView(R.id.tvw_monto)
        TextView tvwMonto;
        @BindView(R.id.tvw_pedidos)
        TextView tvwPedidos;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            cvwItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSelectedListener.onClienteModelClick(filteredItems.get(getAdapterPosition()));
                }
            });
            cvwItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemSelectedListener.onLongClick(filteredItems.get(getAdapterPosition()), v);
                    return true;
                }
            });
        }
    }

    public interface OnItemSelectedListener {
        void onClienteModelClick(ClienteModel clienteModel);

        void onLongClick(ClienteModel clienteModel, View v);
    }
}

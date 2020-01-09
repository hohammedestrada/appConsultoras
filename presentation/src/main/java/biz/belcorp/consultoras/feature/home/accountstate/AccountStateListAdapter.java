package biz.belcorp.consultoras.feature.home.accountstate;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.accountState.AccountStateModel;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountStateListAdapter extends RecyclerView.Adapter<AccountStateListAdapter.Holder> {

    private static final String TAG = "AccountStateListAdapter";
    private List<AccountStateModel> allItems;
    private String currencySymbol;
    private DecimalFormat decimalFormat;
    private Context context;

    AccountStateListAdapter(Context context, List<AccountStateModel> movements,
                            String currencySymbol, DecimalFormat df) {
        this.context = context;
        this.allItems = movements;
        this.currencySymbol = currencySymbol;
        this.decimalFormat = df;
    }

    @Override
    public int getItemCount() {
        return allItems.size();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_estado_cuenta, viewGroup, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        AccountStateModel model = allItems.get(position);
        String debt = decimalFormat.format(model.getMontoOperacion());
        holder.tvwAmount.setText(currencySymbol + " " + debt);
        holder.tvwDescription.setText(model.getDescripcionOperacion());

        try {
            String finalDate = StringUtil.capitalize(DateUtil.convertFechaToString(DateUtil.convertirDDMMAAAAtoDate(model.getFechaRegistro()), "dd MMM"));
            holder.tvwDate.setText(finalDate);
        } catch (ParseException e) {
            Log.e(TAG, TAG, e);
        }

        if (model.getMontoOperacion().doubleValue() >= 0) {
            holder.tvwAmount.setTextColor(Color.GRAY);
            holder.tvwDescription.setTextColor(Color.GRAY);
            holder.tvwDate.setTextColor(Color.GRAY);
            holder.viewSeparator.setBackgroundColor(Color.GRAY);
        } else {
            holder.tvwAmount.setTextColor(Color.BLACK);
            holder.tvwDescription.setTextColor(Color.BLACK);
            holder.tvwDate.setTextColor(Color.BLACK);
            holder.viewSeparator.setBackgroundColor(Color.BLACK);
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvw_date)
        TextView tvwDate;
        @BindView(R.id.tvw_description)
        TextView tvwDescription;
        @BindView(R.id.edt_amount)
        TextView tvwAmount;
        @BindView(R.id.view_separator)
        View viewSeparator;

        private Holder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}

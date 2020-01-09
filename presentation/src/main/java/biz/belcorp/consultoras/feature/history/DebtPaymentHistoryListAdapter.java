package biz.belcorp.consultoras.feature.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.client.ClientMovementModel;
import biz.belcorp.consultoras.util.anotation.MovementType;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

class DebtPaymentHistoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "DebtPaymentHistoryList";

    private List<ClientMovementModel> allItems;
    private String currencySymbol;
    private DecimalFormat decimalFormat;
    private OnItemSelectedListener onItemSelectedListener;

    DebtPaymentHistoryListAdapter(Context context, List<ClientMovementModel> movements
        , String currencySymbol, DecimalFormat df, OnItemSelectedListener onItemSelectedListener) {
        this.allItems = movements;
        this.currencySymbol = currencySymbol;
        this.decimalFormat = df;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    String getCurrencySymbol() {
        return currencySymbol;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.item_debt_payment_history, viewGroup, false);
        switch (viewType) {
            case 1:
                return new ViewHolderC(itemView);
            case 2:
                itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_payment_history, viewGroup, false);
                return new ViewHolderP(itemView);
            default:
                return new ViewHolderC(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        final ClientMovementModel model = allItems.get(position);

        switch (model.getType()) {
            case "C":
            case "CB":
            case "H":
                ViewHolderC viewHolderC = (ViewHolderC) holder;
                viewHolderC.onBind(model);
                break;

            case "A":
                ViewHolderP viewHolderP = (ViewHolderP) holder;
                viewHolderP.onBind(model);
                break;
            default:
                // EMPTY
        }
    }


    @Override
    public int getItemCount() {
        return allItems.size();
    }

    @Override
    public int getItemViewType(int position) {

        int type = 1;

        switch (allItems.get(position).getType()) {
            case "C":
                type = 1;
                break;

            case "A":
                type = 2;
                break;
            default:
                // EMPTY
        }

        return type;
    }

    class ViewHolderC extends RecyclerView.ViewHolder {

        @BindView(R.id.tvw_campaing)
        TextView tvwCampaing;
        @BindView(R.id.tvw_date)
        TextView tvwDate;
        @BindView(R.id.view_separator)
        View viewSeparator;
        @BindView(R.id.view_separator2)
        View viewSeparator2;
        @BindView(R.id.tvw_description)
        TextView tvwDescription;
        @BindView(R.id.edt_amount)
        TextView tvwAmount;
        @BindView(R.id.layoutItem)
        RelativeLayout layoutItem;
        @BindView(R.id.ivw_next)
        ImageView ivwNext;

        ViewHolderC(View v) {
            super(v);
            ButterKnife.bind(this, v);

            layoutItem.setOnClickListener(v1 -> onItemSelectedListener.onClienteModelClick(allItems.get(getAdapterPosition())));

            layoutItem.setOnLongClickListener(v12 -> {
                onItemSelectedListener.onClienteModelLongClick(allItems.get(getAdapterPosition()));
                return false;
            });

        }

        private void onBind(ClientMovementModel model) {

            String campaing = model.getCampaing();

            if (!campaing.equals("") && campaing.length() == 6)
                campaing = "C" + campaing.substring(4);

            String date = model.getDate();

            try {
                String finalDate = StringUtil.capitalize(DateUtil.convertFechaToString(
                    DateUtil.convertirISODatetoDate(date), "dd MMM"));
                tvwDate.setText(finalDate);
            } catch (ParseException e) {
                Log.e(TAG, "ClientList", e);
            }

            String description = model.getDescription();
            String amount = getCurrencySymbol() + "  " + decimalFormat.format(model.getAmount());
            tvwCampaing.setText(campaing);

            if (description != null && !description.equals(""))
                tvwDescription.setText(description);
            else viewSeparator.setVisibility(View.INVISIBLE);

            if (amount != null && !amount.equals(""))
                tvwAmount.setText(amount);
            else viewSeparator2.setVisibility(View.INVISIBLE);

            if (model.getType().equals(MovementType.HISTORICO))
                ivwNext.setVisibility(View.INVISIBLE);

        }
    }

    class ViewHolderP extends RecyclerView.ViewHolder {

        @BindView(R.id.tvw_date)
        TextView tvwDate;
        @BindView(R.id.view_separator)
        View viewSeparator;
        @BindView(R.id.view_separator2)
        View viewSeparator2;
        @BindView(R.id.tvw_description)
        TextView tvwDescription;
        @BindView(R.id.edt_amount)
        TextView tvwAmount;
        @BindView(R.id.layoutItem)
        RelativeLayout layoutItem;

        private ViewHolderP(View v) {
            super(v);
            ButterKnife.bind(this, v);

            layoutItem.setOnClickListener(v1 -> onItemSelectedListener.onClienteModelClick(allItems.get(getAdapterPosition())));

            layoutItem.setOnLongClickListener(v12 -> {
                onItemSelectedListener.onClienteModelLongClick(allItems.get(getAdapterPosition()));
                return false;
            });

        }

        private void onBind(ClientMovementModel model) {
            String description = model.getDescription();

            BigDecimal val = model.getAmount().doubleValue() > 0f ? model.getAmount().negate() : model.getAmount();
            String amount = getCurrencySymbol() + "  " + decimalFormat.format(val);
            String date = model.getDate();

            try {
                String finalDate = StringUtil.capitalize(DateUtil.convertFechaToString(
                    DateUtil.convertirISODatetoDate(date), "dd MMM"));
                tvwDate.setText(finalDate);
            } catch (ParseException e) {
                Log.e(TAG, "ClientList", e);
            }

            if (description.equals("")) viewSeparator.setVisibility(View.INVISIBLE);
            tvwDescription.setText(description);

            if (amount.equals("")) viewSeparator2.setVisibility(View.INVISIBLE);
            tvwAmount.setText(amount);
        }
    }

    public interface OnItemSelectedListener {
        void onClienteModelClick(ClientMovementModel clienteModel);

        void onClienteModelLongClick(ClientMovementModel clienteModel);
    }

}

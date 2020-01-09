package biz.belcorp.consultoras.feature.contest.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.incentivos.PremioNuevaModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewProgramGiftAdapter extends RecyclerView.Adapter<NewProgramGiftAdapter
    .PremioNuevaViewHolder> {

    private Context context;
    private List<PremioNuevaModel> allItems;
    private String currencySymbol;
    private DecimalFormat decimalFormat;
    private int status;

    class PremioNuevaViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivw_gift)
        ImageView ivwGift;
        @BindView(R.id.ivw_check)
        ImageView ivwCheck;
        @BindView(R.id.tvw_title)
        TextView tvwTitle;

        private PremioNuevaViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    NewProgramGiftAdapter(Context context, List<PremioNuevaModel> gifts, String currencySymbol,
                           DecimalFormat decimalFormat, int status) {
        this.context = context;
        this.allItems = gifts;
        this.currencySymbol = currencySymbol;
        this.decimalFormat = decimalFormat;
        this.status = status;
    }

    @NonNull
    @Override
    public PremioNuevaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PremioNuevaViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.fragment_new_program_gift, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PremioNuevaViewHolder holder, int position) {
        final PremioNuevaModel premioNuevaModel = allItems.get(position);

        if (null != premioNuevaModel) {

            if (premioNuevaModel.getPrecioUnitario().compareTo(BigDecimal.ZERO) > 0) {

                String finalAmount = decimalFormat.format(premioNuevaModel.getPrecioUnitario());

                holder.tvwTitle.setText(String.format(context.getString(R.string.incentives_new_program_title_gift_1),
                    premioNuevaModel.getDescripcionProducto(), currencySymbol, finalAmount));
            } else {
                holder.tvwTitle.setText(String.format(context.getString(R.string.incentives_new_program_title_gift_2),
                    premioNuevaModel.getDescripcionProducto()));
            }
            Glide.with(context).load(premioNuevaModel.getUrlImagenPremio()).into(holder.ivwGift);
        }

        if (status == 1)
            holder.ivwCheck.setVisibility(View.VISIBLE);
        else
            holder.ivwCheck.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return allItems.size();
    }

}

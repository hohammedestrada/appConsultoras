package biz.belcorp.consultoras.feature.contest.news;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.incentivos.CuponModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewProgramCuponAdapter extends RecyclerView.Adapter<NewProgramCuponAdapter.CuponViewHolder> {

    private Context context;

    private List<CuponModel> allItems = new ArrayList<>();

    private NewProgramCuponListener cuponListener;

    private DecimalFormat decimalFormat;
    private String currencySymbol;
    private Integer enabled;

    public class CuponViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivw_cupon)
        ImageView ivwCupon;
        @BindView(R.id.tvw_cupon_title)
        TextView tvwCuponTitle;
        @BindView(R.id.tvw_cupon_subtitle)
        TextView tvwCuponSubtitle;
        @BindView(R.id.tvw_old_price)
        TextView tvwOldPrice;
        @BindView(R.id.tvw_actual_price)
        TextView tvwActualPrice;
        @BindView(R.id.tvw_ganancia)
        TextView tvwGanancia;
        @BindView(R.id.btn_add)
        Button btnAdd;

        private CuponViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @OnClick(R.id.btn_add)
        void addProduct() {
            CuponModel cuponModel = allItems.get(getAdapterPosition());

            if (enabled == 0) {
                int level = Integer.parseInt(cuponModel.getCodigoNivel());
                Toast.makeText(context, String.format(context.getString(
                    R.string.incentives_message_add_order_disable), level),
                    Toast.LENGTH_LONG).show();
                return;
            }

            if (cuponListener != null) {
                cuponListener.trackEvent(cuponModel.getDescripcionProducto());
                cuponListener.addCupon(cuponModel);
            }
        }
    }

    NewProgramCuponAdapter(Context context, List<CuponModel> cupons, String currencySymbol,
                           DecimalFormat decimalFormat, Integer enabled) {
        this.context = context;
        this.allItems = cupons;
        this.currencySymbol = currencySymbol;
        this.decimalFormat = decimalFormat;
        this.enabled = enabled;
    }

    @Override
    public CuponViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CuponViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.fragment_cupon, parent, false));
    }

    @Override
    public void onBindViewHolder(CuponViewHolder holder, int position) {
        final CuponModel cuponModel = allItems.get(position);

        if (null != cuponModel) {

            String precioFinal = currencySymbol + decimalFormat.format(cuponModel.getPrecioUnitario());
            String ganancia = "GANA " + currencySymbol + decimalFormat.format(cuponModel.getGanancia());
            String precio = currencySymbol + decimalFormat.format(cuponModel.getPrecioUnitario().add(cuponModel.getGanancia()));

            holder.tvwActualPrice.setText(precioFinal);
            holder.tvwOldPrice.setText(precio);
            holder.tvwOldPrice.setPaintFlags(holder.tvwOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvwGanancia.setText(ganancia);

            if (enabled == 0) {
                holder.btnAdd.setAlpha(.35f);
            }

            holder.tvwCuponTitle.setText(cuponModel.getDescripcionProducto());
            Glide.with(context).load(cuponModel.getUrlImagenCupon()).into(holder.ivwCupon);
        }

    }

    @Override
    public int getItemCount() {
        return (null == allItems) ? 0 : allItems.size();
    }

    void setCuponListener(NewProgramCuponListener cuponListener) {
        this.cuponListener = cuponListener;
    }

    /*******************************************************************/

    interface NewProgramCuponListener {

        void addCupon(CuponModel cuponModel);

        void trackEvent(String label);
    }
}

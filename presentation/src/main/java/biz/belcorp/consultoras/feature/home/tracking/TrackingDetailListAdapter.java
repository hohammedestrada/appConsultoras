package biz.belcorp.consultoras.feature.home.tracking;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.tracking.TrackingDetailModel;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackingDetailListAdapter extends RecyclerView.Adapter<TrackingDetailListAdapter.TrackingListAdapterHolder> {

    private static final String TAG = "TrackingDetailListAd";
    private List<TrackingDetailModel> allItems = new ArrayList<>();
    private Context context;
    private int etapaAlcanzada;


    TrackingDetailListAdapter(Context context, List<TrackingDetailModel> movements, int etapaAlcanzada) {
        this.context = context;
        this.allItems = movements;
        this.etapaAlcanzada = etapaAlcanzada;
    }

    @Override
    public int getItemCount() {
        return allItems.size();
    }

    @Override
    public TrackingListAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_seguimiento_pedido, viewGroup, false);
        return new TrackingListAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrackingListAdapterHolder holder, int position) {
        TrackingDetailModel model = allItems.get(position);
        holder.tvwEstado.setText(model.getSituacion());

        if (null != model.getFecha() && !model.getFecha().isEmpty()) {

            String finalDate;

            try {
                if (model.getEtapa() == 2 || model.getEtapa() == 6) {
                    finalDate = StringUtil.capitalize(DateUtil.convertFechaToString(DateUtil.convertirISODatetoDate(model.getFecha()), "dd MMM"));
                } else {
                    finalDate = StringUtil.capitalize(DateUtil.convertFechaToString(DateUtil.convertirISODatetoDate(model.getFecha()), "dd MMM hh:mm a"));
                }

                holder.tvwFecha.setText(finalDate);
            } catch (ParseException e) {
                BelcorpLogger.w(TAG, "TrackingDetailListAdapter", e);
            }
        }

        if (model.getAlcanzado()) {
            holder.tvwEstado.setTextColor(Color.BLACK);
            holder.tvwFecha.setVisibility(View.VISIBLE);
            holder.ivwStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_circle_tracking));
            holder.ivwStatus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_white));
        } else {
            holder.tvwEstado.setTextColor(Color.GRAY);
            holder.tvwFecha.setVisibility(View.GONE);
            holder.ivwStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_circle_tracking_inactive));
        }

        if (etapaAlcanzada == model.getEtapa()) {
            holder.ivwStatus.setVisibility(View.GONE);
            holder.ivwStatusBig.setVisibility(View.VISIBLE);
            holder.ivwStatusBig.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_circle_tracking));
            holder.ivwStatusBig.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_box));
        } else {
            holder.ivwStatus.setVisibility(View.VISIBLE);
            holder.ivwStatusBig.setVisibility(View.GONE);
        }

        if (position == allItems.size() - 1) {
            holder.viewLine.setVisibility(View.GONE);
        } else {
            holder.viewLine.setVisibility(View.VISIBLE);
        }
    }

    class TrackingListAdapterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvw_estado)
        TextView tvwEstado;
        @BindView(R.id.tvw_fecha)
        TextView tvwFecha;
        @BindView(R.id.ivw_status)
        ImageView ivwStatus;
        @BindView(R.id.ivw_status_big)
        ImageView ivwStatusBig;
        @BindView(R.id.view_line)
        View viewLine;

        private TrackingListAdapterHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}

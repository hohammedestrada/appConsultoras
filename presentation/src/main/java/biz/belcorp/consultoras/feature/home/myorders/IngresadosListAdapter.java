package biz.belcorp.consultoras.feature.home.myorders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.order.MyOrderModel;
import biz.belcorp.consultoras.util.ToastUtil;
import biz.belcorp.consultoras.util.anotation.SurveyStateType;
import biz.belcorp.library.annotation.DatetimeFormat;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DateUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngresadosListAdapter extends RecyclerView.Adapter<IngresadosListAdapter.Holder> {

    private Context context;
    private List<MyOrderModel> allItems;
    private OnListener onItemSelectedListener;

    IngresadosListAdapter(Context context, List<MyOrderModel> movements,
                          OnListener onItemSelectedListener) {
        this.context = context;
        this.allItems = movements;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public int getItemCount() {
        return allItems.size();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.item_my_orders_ingresados, viewGroup, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        MyOrderModel model = allItems.get(position);

        String fecha = "";

        try {
            Date date = DateUtil.convertFechaToDate(model.getFechaRegistro(), DatetimeFormat.ISO_8601);
            fecha = DateUtil.convertFechaToString(date, "dd MMM");
        } catch (ParseException | NullPointerException e) {
            BelcorpLogger.w("Date", e);
        }

        String campaign = String.valueOf(model.getCampaniaID());

        if (!TextUtils.isEmpty(campaign) && campaign.length() == 6) {
            campaign = campaign.substring(4);
        }

        if (model.getRutaPaqueteDocumentario().isEmpty()) {
            holder.ivwDoc.setAlpha(0.3f);
            holder.lltDoc.setOnClickListener(null);
        }

        holder.tvwDescripcion.setText("C" + campaign + " - " + fecha);
        holder.surveyImage.setImageResource(model.getEstadoEncuesta() == SurveyStateType.NOT_ANSWERED
            ? R.drawable.activo : R.drawable.inactivo);
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivw_doc)
        ImageView ivwDoc;
        @BindView(R.id.tvw_description)
        TextView tvwDescripcion;
        @BindView(R.id.tvw_monto)
        TextView tvwMonto;
        @BindView(R.id.llt_location)
        LinearLayout lltLocation;
        @BindView(R.id.llt_doc)
        LinearLayout lltDoc;
        @BindView(R.id.llt_search)
        LinearLayout lltSearch;
        @BindView(R.id.llt_survey)
        LinearLayout llt_survey;
        @BindView(R.id.surveyImage)
        ImageView surveyImage;

        private Holder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            lltLocation.setOnClickListener(v1 ->
                ToastUtil.INSTANCE.show(context,
                    context.getText(R.string.my_orders_not_available), Toast.LENGTH_SHORT)
            );

            lltDoc.setOnClickListener(v12 ->
                onItemSelectedListener.onPaqDocClick(allItems.get(getAdapterPosition())));

            lltSearch.setOnClickListener(v13 ->
                onItemSelectedListener.onDetalleClick(allItems.get(getAdapterPosition())));

            llt_survey.setOnClickListener(v14 -> {
                if (allItems.get(getAdapterPosition()).getEstadoEncuesta() == SurveyStateType.NOT_ANSWERED){
                    onItemSelectedListener.onSurveyOptionClick(allItems.get(getAdapterPosition()));
                }});
        }
    }

    public interface OnListener {
        void onPaqDocClick(MyOrderModel myOrderModel);
        void onDetalleClick(MyOrderModel myOrderModel);
        void onSurveyOptionClick(MyOrderModel myOrderModel);
    }
}

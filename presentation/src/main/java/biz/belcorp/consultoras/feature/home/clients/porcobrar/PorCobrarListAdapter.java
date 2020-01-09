package biz.belcorp.consultoras.feature.home.clients.porcobrar;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.library.annotation.DatetimeFormat;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

class PorCobrarListAdapter extends RecyclerView.Adapter<PorCobrarListAdapter.PerChargeHolder> {
    private List<ClienteModel> allItems = new ArrayList<>();
    private OnItemSelectedListener onItemSelectedListener;
    private String moneySymbol;
    private DecimalFormat decimalFormat;

    PorCobrarListAdapter(List<ClienteModel> clients
        , OnItemSelectedListener onItemSelectedListener, String moneySymbol, DecimalFormat decimalFormat) {
        this.allItems = clients;
        this.onItemSelectedListener = onItemSelectedListener;
        this.moneySymbol = moneySymbol;
        this.decimalFormat = decimalFormat;
    }

    @Override
    public int getItemCount() {
        return allItems.size();
    }

    @Override
    public PerChargeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client_porcobrar, parent, false);

        return new PerChargeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PerChargeHolder holder, int position) {
        ClienteModel model = allItems.get(position);

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

        if (!model.getRecordatorioModels().isEmpty() && model.getRecordatorioModels().get(0) != null && model.getRecordatorioModels().get(0).getEstado() != -1) {
            holder.tvwReminder.setVisibility(View.VISIBLE);
            holder.ivwReminderAlert.setVisibility(View.VISIBLE);

            Calendar reminderCalender = Calendar.getInstance();
            String reminderString;

            try {
                Date reminderDate = DateUtil.convertFechaToDate(model.getRecordatorioModels().get(0).getFecha(), DatetimeFormat.ISO_8601);
                reminderCalender.setTime(reminderDate);
                reminderString = DateUtil.convertFechaToString(reminderCalender.getTime(), "dd MMM hh:mm a");
            } catch (ParseException | NullPointerException e) {
                reminderCalender = Calendar.getInstance();
                reminderString = "-- --";
                BelcorpLogger.w("Date", e);
            }

            int daysLeft = calculateDaysLeft(reminderCalender);

            switch (daysLeft) {
                case -2:
                    holder.tvwReminder.setText(String.format(holder.itemView.getResources().getString(R.string.per_charge_reminder_zero_old), reminderString));
                    holder.ivwReminderAlert.setVisibility(View.VISIBLE);
                    break;
                case -1:
                    holder.tvwReminder.setText(String.format(holder.itemView.getResources().getString(R.string.per_charge_reminder_old), reminderString));
                    holder.ivwReminderAlert.setVisibility(View.VISIBLE);
                    break;
                case 0:
                    holder.tvwReminder.setText(String.format(holder.itemView.getResources().getString(R.string.per_charge_reminder_zero), reminderString));
                    holder.ivwReminderAlert.setVisibility(View.GONE);
                    break;
                default:
                    holder.tvwReminder.setText(String.format(holder.itemView.getContext().getString(R.string.per_charge_reminder_cobro), reminderString));
                    holder.ivwReminderAlert.setVisibility(View.GONE);
            }
        } else {
            holder.tvwReminder.setVisibility(View.GONE);
            holder.ivwReminderAlert.setVisibility(View.GONE);
        }

        String debt = decimalFormat.format(model.getTotalDeuda());
        holder.tvwMonto.setText(moneySymbol + " " + debt);
    }

    private int calculateDaysLeft(Calendar reminderCalender) {
        Calendar actualDate = Calendar.getInstance();

        if (actualDate.before(reminderCalender)) {
            int yearDiff = reminderCalender.get(Calendar.YEAR) - actualDate.get(Calendar.YEAR);

            if (yearDiff > 0) {
                long diff = reminderCalender.getTimeInMillis() - actualDate.getTimeInMillis();
                long days = diff / (24 * 60 * 60 * 1000);

                return (int) days;
            } else {
                int monthDiff = reminderCalender.get(Calendar.MONTH) - actualDate.get(Calendar.MONTH);

                if (monthDiff > 0) {
                    long diff = reminderCalender.getTimeInMillis() - actualDate.getTimeInMillis();
                    long days = diff / (24 * 60 * 60 * 1000);

                    return (int) days;
                } else {
                    int dayDiff = reminderCalender.get(Calendar.DAY_OF_MONTH) - actualDate.get(Calendar.DAY_OF_MONTH);

                    if (dayDiff == 0) {
                        return 0;
                    } else if (dayDiff > 0) {
                        return dayDiff;
                    } else {
                        return -2;
                    }
                }
            }
        } else {
            return -1;
        }
    }

    class PerChargeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cvw_item)
        CardView cvwItem;
        @BindView(R.id.tvw_client)
        TextView tvwClient;
        @BindView(R.id.tvw_title)
        TextView tvwTitle;
        @BindView(R.id.tvw_reminder)
        TextView tvwReminder;
        @BindView(R.id.ivw_reminder_alert)
        ImageView ivwReminderAlert;
        @BindView(R.id.tvw_monto)
        TextView tvwMonto;

        private PerChargeHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            cvwItem.setOnClickListener(v1 -> onItemSelectedListener.onClienteModelClick(allItems.get(getAdapterPosition())));
            cvwItem.setOnLongClickListener(v12 -> {
                onItemSelectedListener.onLongClick(allItems.get(getAdapterPosition()), v12);
                return true;
            });
        }
    }

    public interface OnItemSelectedListener {
        void onClienteModelClick(ClienteModel clienteModel);
        void onLongClick(ClienteModel clienteModel, View v);
    }
}

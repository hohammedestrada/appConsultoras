package biz.belcorp.consultoras.feature.client.note;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.client.AnotacionModel;
import biz.belcorp.consultoras.util.anotation.NoteCode;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.Holder> {


    private List<AnotacionModel> mList = new ArrayList<>();
    private OnItemSelectedListener listener;

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvw_item_recordatorio)
        TextView tvwItemRecordatorio;
        @BindView(R.id.tvw_item_descripcion)
        TextView tvwItemDescripcion;
        @BindView(R.id.tvw_item_fecha)
        TextView tvwItemFecha;

        @OnClick(R.id.llt_item_note)
        public void onClick() {
            if (listener != null) {
                listener.onAnotacionItemSelected(mList.get(getAdapterPosition()), getAdapterPosition());
            }
        }

        @OnClick(R.id.tvw_item_eliminar)
        public void eliminar() {
            AnotacionModel model = mList.get(getAdapterPosition());
            model.setEstado(NoteCode.DELETED);
            listener.onEliminarSelected(model, getAdapterPosition());
        }

        public Holder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public NotesAdapter(List<AnotacionModel> list) {
        setList(list);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_note, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final AnotacionModel item = mList.get(position);
        try {

            String finalDate;

            if (item.getFecha().toLowerCase().contains("z"))
                finalDate = StringUtil.capitalize(DateUtil.convertFechaToString(
                    DateUtil.convertirRFCDatetoDate(item.getFecha()), "dd MMM"));
            else
                finalDate = StringUtil.capitalize(DateUtil.convertFechaToString(
                    DateUtil.convertirISODatetoDate(item.getFecha()), "dd MMM"));

            holder.tvwItemFecha.setText(finalDate);
        } catch (Exception e) {
            BelcorpLogger.w("onBindViewHolder", e);
        }
        holder.tvwItemDescripcion.setText(item.getDescripcion());
        holder.tvwItemRecordatorio.setText("--");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public List<AnotacionModel> getList() {
        return mList;
    }

    public void setList(List<AnotacionModel> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void refreshNotes(List<AnotacionModel> notes) {
        mList.clear();
        mList.addAll(notes);
        notifyDataSetChanged();
    }

    public void addNote(AnotacionModel anotacionModel) {
        mList.add(anotacionModel);
        notifyDataSetChanged();
    }

    public void editNote(AnotacionModel anotacionModel) {
        for (AnotacionModel pointer : mList) {
            if (pointer.getId().equals(anotacionModel.getId())) {
                pointer.setClienteID(anotacionModel.getClienteID());
                pointer.setFecha(anotacionModel.getFecha());
                pointer.setDescripcion(anotacionModel.getDescripcion());
                pointer.setEstado(anotacionModel.getEstado());
            }
        }
        notifyDataSetChanged();
    }

    public void removeNote(AnotacionModel anotacionModel) {
        mList.remove(anotacionModel);
        notifyDataSetChanged();
    }

    public OnItemSelectedListener getListener() {
        return listener;
    }

    public void setListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void onAnotacionItemSelected(AnotacionModel anotacionModel, int position);

        void onEliminarSelected(AnotacionModel anotacionModel, int adapterPosition);
    }

}

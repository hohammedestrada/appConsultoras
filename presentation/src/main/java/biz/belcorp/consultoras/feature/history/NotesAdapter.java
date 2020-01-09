package biz.belcorp.consultoras.feature.history;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {

    private List<String> notes = new ArrayList<>();

    private final NoteListener noteListener;

    private final View.OnClickListener onEditClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = Integer.parseInt(v.getTag().toString());
            noteListener.onEditNote(position);
        }
    };

    private final View.OnClickListener onDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = Integer.parseInt(v.getTag().toString());
            noteListener.onDeleteNote(position);
        }
    };

    NotesAdapter(NoteListener noteListener) {
        this.noteListener = noteListener;
    }

    @Override
    public NotesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotesHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_movement, parent, false));
    }

    @Override
    public void onBindViewHolder(NotesHolder holder, int position) {
        String note = notes.get(position);

        if (TextUtils.isEmpty(note)) {
            note = "Me compró...";
            holder.tvwDelete.setVisibility(View.GONE);
        } else {
            holder.tvwDelete.setVisibility(View.VISIBLE);
        }

        holder.tvwDesc.setText(note);

        holder.tvwDelete.setTag(position);
        holder.tvwDelete.setOnClickListener(onDeleteClickListener);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(onEditClickListener);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    void setNotes(List<String> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
    }

    class NotesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvw_description)
        TextView tvwDesc;
        @BindView(R.id.tvw_eliminar)
        TextView tvwDelete;

        private NotesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface NoteListener {

        void onEditNote(int position);

        void onDeleteNote(int position);
    }
}

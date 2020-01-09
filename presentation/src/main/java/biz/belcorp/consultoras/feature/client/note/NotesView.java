package biz.belcorp.consultoras.feature.client.note;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.common.model.client.AnotacionModel;

/**
 * @author andres.escobar on 23/06/2017.
 */
public class NotesView extends RecyclerView {

    NotesAdapter notesAdapter;
    List<AnotacionModel> anotaciones = new ArrayList<>();

    public NotesView(Context context) {
        super(context);
        init();
    }

    public NotesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NotesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        notesAdapter = new NotesAdapter(anotaciones);
        setAdapter(notesAdapter);
    }

    public void refreshNotes(List<AnotacionModel> notes) {
        notesAdapter.refreshNotes(notes);
    }


    public void addNote(AnotacionModel anotacionModel) {
        if (anotacionModel != null) {
            notesAdapter.addNote(anotacionModel);
            notesAdapter.notifyDataSetChanged();
        }
    }

    public void editNote(AnotacionModel anotacionModel) {
        notesAdapter.editNote(anotacionModel);
        notesAdapter.notifyDataSetChanged();
    }

    public void setListener(NotesAdapter.OnItemSelectedListener listener) {
        notesAdapter.setListener(listener);

    }

    public void removeNote(AnotacionModel anotacionModel) {
        if (anotacionModel != null) {
            notesAdapter.removeNote(anotacionModel);
            notesAdapter.notifyDataSetChanged();
        }
    }

    public List<AnotacionModel> getNotes() {
        return notesAdapter.getList();
    }

}

package biz.belcorp.consultoras.feature.client.note;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Date;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.AnotacionModel;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.Constant;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.StatusType;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.annotation.DatetimeFormat;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DateUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.Unbinder;

/**
 *
 */
public class NoteFragment extends BaseFragment implements NoteView {

    @Inject
    NotePresenter presenter;

    @BindView(R.id.btn_note_add)
    Button btnNoteAdd;
    @BindView(R.id.ted_note_client)
    EditText tedNoteClient;
    @BindView(R.id.ted_note_description)
    EditText tedNoteDescription;
    @BindView(R.id.rlt_client)
    RelativeLayout rltNoteClient;

    private Unbinder bind;
    private NotaListener listener;

    private int accion;
    private int clientID;
    private int clientLocalID;
    String clientName;
    private int noteID;
    private boolean clientsShown;
    private String countryISO;

    private AnotacionModel note;

    /*******************************************************************************************/

    @Override
    protected boolean onInjectView() throws IllegalStateException {
        getComponent(ClientComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);

        initBundle();

        tedNoteDescription.requestFocus();
        if (accion == NoteActivity.CLIENTE_EXISTENTE || accion == NoteActivity.NUEVO_CLIENTE) {
            rltNoteClient.setVisibility(View.GONE);
        }

        presenter.getAnotation(noteID);
    }

    /*******************************************************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NotaListener) {
            listener = (NotaListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note, container, false);
        bind = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null) presenter.initScreenTrack();
    }

    @Override
    public void onDestroyView() {
        if (bind != null)
            bind.unbind();
        if (presenter != null)
            presenter.destroy();

        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ClientSelectionActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                clientID = data.getIntExtra(NoteActivity.CLIENT_ID, 0);
                clientLocalID = data.getIntExtra(NoteActivity.CLIENT_LOCAL_ID, 0);
                clientName = data.getStringExtra(NoteActivity.CLIENT_NAME);
                tedNoteClient.setText(clientName);

            }
            clientsShown = false;
        }
    }

    private void initBundle(){
        Bundle extras = getArguments();

        if (null != extras) {
            accion = extras.getInt(NoteActivity.ACCION);
            clientID = extras.getInt(NoteActivity.CLIENT_ID, 0);
            clientLocalID = extras.getInt(NoteActivity.CLIENT_LOCAL_ID, 0);
            noteID = extras.getInt(NoteActivity.NOTA_ID, 0);
        }

    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {

        countryISO = loginModel.getCountryISO();

        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_CLIENT_NOTE_EDIT);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_CLIENT_NOTE_EDIT);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);

        if (listener != null) {
            listener.onBackFromFragment();
        }
    }

    public void trackBackPressed() {
        presenter.trackBackPressed();
    }

    /***********************************************************************************************/

    @Override
    public Context context() {
        return null;
    }

    @Override
    public void show(AnotacionModel model) {

        this.note = model;
        if (note != null) {
            tedNoteDescription.setText(note.getDescripcion());
            tedNoteDescription.setSelection(note.getDescripcion().length());
        }
    }

    @Override
    public void saved(Boolean result) {
        throw new UnsupportedOperationException(GlobalConstant.WONT_IMPLEMENT);
    }

    @Override
    public void onError(Throwable exception) {
        processError(exception);
    }

    @Override
    public void anotacionSaved(AnotacionModel anotacionModel) {
        if (listener != null) {
            listener.onNotaCreada(anotacionModel);
        }
    }

    @Override
    public void anotacionUpdated(AnotacionModel anotacionModel) {
        if (listener != null) {
            listener.onNotaActualizada(anotacionModel);
        }
    }

    /***********************************************************************************************/

    @OnFocusChange(R.id.ted_note_client)
    public void selecccionarCliente() {
        tedNoteDescription.requestFocus();
        if (!clientsShown) {
            clientsShown = true;
            Intent intent = new Intent(getActivity(), ClientSelectionActivity.class);
            startActivityForResult(intent, ClientSelectionActivity.REQUEST_CODE);
        }
    }

    @OnClick(R.id.btn_note_add)
    public void guardar() {
        AnotacionModel anotacionModel = new AnotacionModel();
        if (accion != NoteActivity.NUEVO_CLIENTE && clientLocalID == 0) {
            showError("Error al guardar nota", "Seleccione un cliente.");
            return;
        }
        String descripcion = tedNoteDescription.getText().toString().trim();
        if (descripcion.isEmpty()) {
            Toast.makeText(getContext(), R.string.notes_description_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (note != null) {
            anotacionModel.setId(note.getId());
            anotacionModel.setAnotacionID(note.getAnotacionID());
            anotacionModel.setFecha(note.getFecha());
        } else {
            anotacionModel.setAnotacionID(0);
            try {
                anotacionModel.setFecha(DateUtil.convertFechaToString(new Date(), DatetimeFormat.RFC_3339));
            } catch ( Exception e) {
                BelcorpLogger.d("NoteFragment", "Error en la fecha de nota");
            }

        }
        anotacionModel.setDescripcion(descripcion);
        anotacionModel.setSincronizado(1);
        anotacionModel.setClienteID(clientID);
        anotacionModel.setClienteLocalID(clientLocalID);

        if (note == null) {
            if (null == countryISO) countryISO = Constant.BRAND_COUNTRY_DEFAULT;
            anotacionModel.setEstado(StatusType.CREATE);
            presenter.saveAnotacion(countryISO, anotacionModel);
        } else {
            if(note.getAnotacionID() != 0) anotacionModel.setEstado(StatusType.UPDATE);
            presenter.updateAnotacion(anotacionModel);
        }
    }

    /***********************************************************************************************/

     interface NotaListener {
        void onNotaCreada(AnotacionModel anotacionModel);

        void onNotaActualizada(AnotacionModel anotacionModel);

        void onBackFromFragment();
    }

}

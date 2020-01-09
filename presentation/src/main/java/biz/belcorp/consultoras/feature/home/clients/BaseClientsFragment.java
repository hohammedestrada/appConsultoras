package biz.belcorp.consultoras.feature.home.clients;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.feature.home.BaseHomeFragment;

public class BaseClientsFragment extends BaseHomeFragment {

    /**
     * Dibuja un tooltip a la izquierda de un view que se manda por entrada.
     *
     * @param view la vista donde va a aparecer el tooltip
     */
    protected void drawTooltipOptions(View view, int gravity, @NonNull int middle
        , final ClienteModel clienteModel, final OnPopupOptionSelectedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.layout_clients_options);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView tvwTooltipTitle = alertDialog.findViewById(R.id.tvw_tooltip_title);
        TextView tvwTooltipEditar =  alertDialog.findViewById(R.id.tvw_tooltip_editar);
        TextView tvwTooltipEliminar = alertDialog.findViewById(R.id.tvw_tooltip_eliminar);

        tvwTooltipTitle.setText(clienteModel.getNombres());

        tvwTooltipEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                listener.editar(clienteModel);
            }
        });
        tvwTooltipEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                listener.eliminar(clienteModel);
            }
        });

    }

    public interface OnPopupOptionSelectedListener {

        void editar(ClienteModel clientemodel);

        void eliminar(ClienteModel clienteModel);
    }
}

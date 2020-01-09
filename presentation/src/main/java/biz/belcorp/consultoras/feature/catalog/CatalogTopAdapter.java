package biz.belcorp.consultoras.feature.catalog;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.catalog.CatalogModel;
import biz.belcorp.library.log.BelcorpLogger;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class CatalogTopAdapter extends RecyclerView.Adapter<CatalogTopAdapter.CatalogViewHolder> {

    private static final String CATALOG_NAME = "%1$s - %2$s";

    private List<CatalogModel> catalogs = new ArrayList<>();
    private CatalogTopAdapterListener eventListener;
    private Context context;

    CatalogTopAdapter() {
    }

    private View.OnClickListener onWhatsAppListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = Integer.parseInt(view.getTag().toString());
            CatalogModel model = catalogs.get(pos);

            if (model == null) return;

            String message = model.getUrlCatalogo();

            if (TextUtils.isEmpty(message)) {
                Toast.makeText(view.getContext(), R.string.catalog_share_no_items, Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent("android.intent.action.MAIN");
            intent.putExtra(Intent.EXTRA_TEXT, message);
            intent.setAction(Intent.ACTION_SEND);
            intent.setPackage("com.whatsapp");
            intent.setType("text/plain");

            try {
                view.getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(view.getContext(), R.string.non_sharing_app_instaled, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener onFacebookListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = Integer.parseInt(view.getTag().toString());
            CatalogModel model = catalogs.get(pos);

            if (model == null) return;

            String message = model.getUrlCatalogo();

            if (TextUtils.isEmpty(message)) {
                Toast.makeText(view.getContext(), R.string.catalog_share_no_items, Toast.LENGTH_SHORT).show();
                return;
            }

            ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(message))
                .build();

            try {
                ShareDialog shareDialog = new ShareDialog(getRequiredActivity(view));
                shareDialog.show(content);
            } catch (IllegalStateException e) {
                BelcorpLogger.w("ShareFacebook", e);
            }
        }
    };

    private View.OnClickListener onMailListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = Integer.parseInt(view.getTag().toString());
            CatalogModel model = catalogs.get(pos);

            if (model == null) return;

            String message = model.getUrlCatalogo();

            if (TextUtils.isEmpty(message)) {
                Toast.makeText(view.getContext(), R.string.catalog_share_no_items, Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, view.getContext().getString(R.string.catalog_share_magazine));
            intent.putExtra(Intent.EXTRA_TEXT, message);

            try {
                view.getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(view.getContext(), R.string.non_sharing_app_instaled, Toast.LENGTH_SHORT).show();
            }
        }
    };

    void setEventListener(CatalogTopAdapterListener eventListener) {
        this.eventListener = eventListener;
    }

    final class CatalogViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivw_catalog)
        ImageView ivwCatalog;

        @BindView(R.id.ivw_share_whats_app)
        ImageView ivwShareWhatsApp;
        @BindView(R.id.ivw_share_facebook)
        ImageView ivwShareFacebook;
        @BindView(R.id.ivw_share_mail)
        ImageView ivwShareMail;
        @BindView(R.id.ivw_download)
        ImageView ivwDownload;

        private CatalogViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            ivwCatalog.setOnClickListener(v -> {

                CatalogModel model = catalogs.get(getAdapterPosition());

                try {
                    if (null != model) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getUrlCatalogo()));
                        itemView.getContext().startActivity(browserIntent);
                    }
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(itemView.getContext(), R.string.catalog_activity_not_found, Toast.LENGTH_LONG).show();
                }
            });

            ivwDownload.setOnClickListener(v ->{
                CatalogModel model = catalogs.get(getAdapterPosition());
                if(model.getDescripcion() != null){
                    if(model.getDescripcion().trim().length() > 0) {
                        if(eventListener != null) {
                            eventListener.onDownloadPDFRequest(model.getDescripcion(), model.getTitulo());
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public CatalogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_catalog_top, parent, false);
        return new CatalogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogViewHolder holder, int position) {
        CatalogModel item = catalogs.get(position);

        holder.ivwShareWhatsApp.setTag(position);
        holder.ivwShareWhatsApp.setOnClickListener(onWhatsAppListener);

        holder.ivwShareFacebook.setTag(position);
        holder.ivwShareFacebook.setOnClickListener(onFacebookListener);

        holder.ivwShareMail.setTag(position);
        holder.ivwShareMail.setOnClickListener(onMailListener);

        Glide.with(holder.itemView.getContext()).load(item.getUrlImagen()).into(holder.ivwCatalog);

        if(item.getUrlDescargaEstado() == null) {
            holder.ivwDownload.setVisibility(View.GONE);
            return;
        }

        if(item.getUrlDescargaEstado() == 0){
            holder.ivwDownload.setVisibility(View.GONE);
            return;
        }

        holder.ivwDownload.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return catalogs.size();
    }

    void setCatalogs(List<CatalogModel> catalogs) {
        this.catalogs.clear();
        this.catalogs.addAll(catalogs);

        notifyDataSetChanged();
    }

    private Activity getRequiredActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public interface CatalogTopAdapterListener {
        void onDownloadPDFRequest(String descripcion, String title);
    }
}

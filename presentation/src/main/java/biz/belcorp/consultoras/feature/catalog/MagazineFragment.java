package biz.belcorp.consultoras.feature.catalog;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.catalog.CatalogByCampaignModel;
import biz.belcorp.consultoras.util.GlobalConstant;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 */
public class MagazineFragment extends BaseFragment {

    @BindView(R.id.ivw_magazine)
    protected ImageView ivwMagazines;
    @BindView(R.id.tvw_campaign_name)
    protected TextView tvwCampaignName;

    private Unbinder bind;
    private CatalogByCampaignModel magazine;
    private CatalogAdapter.CatalogEventListener eventListener;

    public static MagazineFragment newInstance() {
        return new MagazineFragment();
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_magazine, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments == null) return;

        String campaignName = arguments.getString(GlobalConstant.CAMPAIGN_KEY);
        magazine = arguments.getParcelable(GlobalConstant.BOOK_BY_CAMPAIGN_KEY);
        tvwCampaignName.setText(campaignName);

        if (magazine != null && magazine.getMagazineEntity() != null)
            Glide.with(this).load(magazine.getMagazineEntity().getUrlImagen()).into(ivwMagazines);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (bind != null) bind.unbind();
    }

    /**********************************************************/

    @OnClick(R.id.ivw_magazine)
    public void showMagazine() {
        try {
            if (null != eventListener
                && null != magazine.getMagazineEntity()
                && !TextUtils.isEmpty(magazine.getMagazineEntity().getUrlCatalogo())
                && Patterns.WEB_URL.matcher(magazine.getMagazineEntity().getUrlCatalogo()).matches()) {
                eventListener.trackEvent(
                    GlobalConstant.EVENT_CAT_MAGAZINE,
                    GlobalConstant.EVENT_ACTION_PICK_MAGAZINE,
                    magazine.getMagazineEntity().getCampaniaId(),
                    GlobalConstant.EVENT_NAME_PICK_MAGAZINE);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(magazine.getMagazineEntity().getUrlCatalogo()));
                startActivity(browserIntent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), R.string.catalog_activity_not_found, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Context context() {
        return getContext();
    }

    public void setEventListener(CatalogAdapter.CatalogEventListener eventListener) {
        this.eventListener = eventListener;
    }
}

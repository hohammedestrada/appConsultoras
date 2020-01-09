package biz.belcorp.consultoras.feature.home.cupon;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.domain.entity.Login;
import biz.belcorp.consultoras.feature.home.cupon.di.CuponComponent;
import biz.belcorp.consultoras.util.Constant;
import biz.belcorp.consultoras.util.ToastUtil;
import biz.belcorp.consultoras.util.anotation.CuponType;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.CountryUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CuponFragment extends BaseFragment implements CuponView {

    @Inject
    CuponPresenter presenter;

    @BindView(R.id.ivw_cupon)
    ImageView ivwCupon;

    @BindView(R.id.tvw_user)
    TextView tvwUser;
    @BindView(R.id.tvw_option_1)
    TextView tvwOption1;
    @BindView(R.id.tvw_option_2)
    TextView tvwOption2;
    @BindView(R.id.tvw_option_3)
    TextView tvwOption3;
    @BindView(R.id.tvw_rules)
    TextView tvwRules;

    @BindView(R.id.layoutOpt1)
    LinearLayout layoutOpt1;
    @BindView(R.id.layoutOpt2)
    LinearLayout layoutOpt2;
    @BindView(R.id.layoutOpt3)
    LinearLayout layoutOpt3;
    @BindView(R.id.layoutCondition)
    LinearLayout layoutCondition;

    CuponListener listener;
    Unbinder unbinder;

    /**********************************************************/

    public CuponFragment() {
        super();
    }

    public static CuponFragment newInstance() {
        return new CuponFragment();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(CuponComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);
        presenter.data();
        init();
    }

    /**********************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CuponListener) {
            listener = (CuponListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_cupon, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void init() {
        tvwRules.setPaintFlags(tvwRules.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Glide.with(this).load(R.drawable.cupon_gif).into(ivwCupon);
    }

    @OnClick(R.id.tvw_rules)
    public void terms() {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.PASE_CUPON));
            startActivity(browserIntent);
        } catch (ActivityNotFoundException ex) {
            ToastUtil.INSTANCE.show(getActivity(), R.string.legal_activity_not_found, Toast.LENGTH_SHORT);
            BelcorpLogger.w("openPdfTerms", ex);
        }
    }

    @OnClick(R.id.img_close)
    public void closeCupon() {
        if (listener != null) {
            listener.onBackFromFragment();
        }
    }

    @OnClick(R.id.btn_aceptar)
    public void aceptarCupon() {
        if (listener != null) {
            listener.onBackFromFragment();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != unbinder) unbinder.unbind();
    }

    /**********************************************************/

    @Override
    public Context context() {
        FragmentActivity activity = getActivity();
        if (activity != null)
            return activity.getApplicationContext();
        else
            return null;
    }

    @Override
    public void setData(Login login) {

        int tipoCondicion = login.getTipoCondicion();
        double cuponPctDescuento = login.getCuponPctDescuento();
        double cuponMontoMaxDscto = login.getCuponMontoMaxDscto();
        String alias = login.getAlias();
        String campaign = login.getCampaing();
        String moneySymbol = login.getCountryMoneySymbol();
        String countryISO = login.getCountryISO();

        String newCampaign = "";

        if (!TextUtils.isEmpty(campaign) && campaign.length() == 6) {
            newCampaign = campaign.substring(4);
        }

        DecimalFormat decimalFormat = CountryUtil.getDecimalFormatByISO(countryISO, true);

        switch (tipoCondicion) {
            case CuponType.REGISTRADO:
                layoutOpt1.setVisibility(View.GONE);
                layoutOpt2.setVisibility(View.VISIBLE);
                layoutOpt3.setVisibility(View.VISIBLE);
                layoutCondition.setVisibility(View.GONE);
                tvwUser.setText(String.format(getString(R.string.cupon_usuario_tipo1), alias, String.valueOf((int) cuponPctDescuento)));
                tvwOption2.setText(String.format(getString(R.string.cupon_tipo1_option1), newCampaign));
                tvwOption3.setText(String.format(getString(R.string.cupon_tipo1_option2), moneySymbol, String.valueOf(decimalFormat.format(cuponMontoMaxDscto))));
                break;
            case CuponType.ACTIVADO:
                layoutOpt1.setVisibility(View.VISIBLE);
                layoutOpt2.setVisibility(View.VISIBLE);
                layoutOpt3.setVisibility(View.GONE);
                layoutCondition.setVisibility(View.VISIBLE);
                tvwUser.setText(String.format(getString(R.string.cupon_usuario_tipo2), alias, String.valueOf((int) cuponPctDescuento)));
                tvwOption1.setText(String.format(getString(R.string.cupon_tipo2_option1), Constant.BRAND_FOCUS_NAME + " " + getString(R.string.cupon_conmigo)));
                tvwOption2.setText(String.format(getString(R.string.cupon_tipo2_option2), newCampaign, moneySymbol, String.valueOf(decimalFormat.format(cuponMontoMaxDscto))));
                break;
            default:
                break;
        }

    }

    /**********************************************************/

    interface CuponListener {
        void onBackFromFragment();
    }
}

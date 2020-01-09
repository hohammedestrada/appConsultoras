package biz.belcorp.consultoras.common.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.text.DecimalFormat;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.home.CardEntity;
import biz.belcorp.consultoras.util.anotation.CardType;
import biz.belcorp.consultoras.util.anotation.DigitalSubscriptionType;
import biz.belcorp.library.annotation.Country;
import biz.belcorp.library.log.BelcorpLogger;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class HomeCardItem extends LinearLayout {

    @BindView(R.id.tvw_item_title)
    protected AppCompatTextView tvwTitle;
    @BindView(R.id.ivw_indicator)
    protected AppCompatImageView ivwIndicator;
    private Context contexto;
    @BindView(R.id.ll_background)
    protected LinearLayout nieve;

    @BindView(R.id.ivw_background)
    protected AppCompatImageView ivwBackground;

    public HomeCardItem(Context context) {
        super(context);
        contexto = context;
        if (!isInEditMode()) init();
    }

    public HomeCardItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        contexto = context;
        if (!isInEditMode()) init();
    }

    public HomeCardItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        contexto = context;
        if (!isInEditMode()) init();
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HomeCardItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        contexto = context;
        if (!isInEditMode()) init();
    }

    private void init() {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.view_home_item, this, false));
        ButterKnife.bind(this);
    }

    public void showIncentives(CardEntity cardEntity, String countryISO) {
        String textoIncentivo = getContext().getString(R.string.home_card_incentive_title);

        if (countryISO.equals(Country.CO)) {
            textoIncentivo = textoIncentivo + getResources().getString(R.string.home_card_new_line_character) + getContext().getString(R.string.home_card_incentivo_colombia);

        } else {
            textoIncentivo = textoIncentivo + getResources().getString(R.string.home_card_new_line_character) + getContext().getString(R.string.home_card_bonificacion_no_colombia);

        }
        tvwTitle.setText(textoIncentivo);
        ivwIndicator.setImageResource(R.drawable.ic_new_bonificacion);
    }


    public void showOrders(CardEntity cardEntity, String moneySymbol, DecimalFormat decimalFormatISO) {
        String textPedido = getContext().getString(R.string.home_card_order_title).
            concat(getResources().getString(R.string.home_card_new_line_character)).concat(getContext().getString(R.string.home_card_order_sub_title));

        tvwTitle.setText(Html.fromHtml(textPedido));
        ivwIndicator.setImageResource(R.drawable.ic_new_cart);
    }

    public void showOffer(int revistaDigitalSuscripcion) {
        String textOfferts = getContext().getString(R.string.home_card_offer_title);

        ivwIndicator.setImageResource(R.drawable.ic_new_offers);
        ImageViewCompat.setImageTintList(ivwIndicator, ColorStateList.valueOf(
            ContextCompat.getColor(contexto, R.color.home_offer)));

        switch (revistaDigitalSuscripcion) {
            case DigitalSubscriptionType.DIGITAL_OFFER_OPTION_1:
            case DigitalSubscriptionType.DIGITAL_OFFER_OPTION_2:
            case DigitalSubscriptionType.DIGITAL_OFFER_OPTION_3:

                tvwTitle.setText(Html.fromHtml(
                    textOfferts.concat(getResources().getString(R.string.home_card_new_line_character)).concat(getContext().getString(R.string.home_card_offer_sub_title_ofertas_digitales))
                ));

                break;
            case DigitalSubscriptionType.GANA_MAS_OPTION_1:
            case DigitalSubscriptionType.GANA_MAS_OPTION_2:
                setGanaMasItem();
                break;
            default:

                tvwTitle.setText(Html.fromHtml(
                    textOfferts.concat(getResources().getString(R.string.home_card_new_line_character)).concat(getContext().getString(R.string.home_card_offer_sub_title_ofertas_digitales))
                ));

                break;
        }


    }

    public void showBeautyAdviser() {
        String asesor = getContext().getString(R.string.home_card_makeup_title).concat(getResources().getString(R.string.home_card_new_line_character)).concat(getContext().getString(R.string.home_card_makeup_sub_title));
        tvwTitle.setText(Html.fromHtml(asesor));
        ivwIndicator.setImageResource(R.drawable.ic_asesor_belleza);
    }

    public void showWinClick() {
        String asesor = getContext().getString(R.string.home_card_win_click_title).concat(getResources().getString(R.string.home_card_new_line_character)).concat(getContext().getString(R.string.home_card_win_click_sub_title));
        ivwIndicator.setImageResource(R.drawable.ic_gana_click);
        ImageViewCompat.setImageTintList(ivwIndicator, ColorStateList.valueOf(
            ContextCompat.getColor(contexto, R.color.home_winclick)));
        tvwTitle.setText(Html.fromHtml(asesor));
    }

    public void showDreamMeter() {
        String catalogo = getContext().getString(R.string.home_card_dream_title).concat(getResources().getString(R.string.home_card_new_line_character)).concat(getResources().getString(R.string.home_card_dream_sub_title));
        tvwTitle.setText(Html.fromHtml(catalogo));
        ivwIndicator.setImageResource(R.drawable.ic_ico_medidor);
        nieve.setVisibility(View.GONE);
    }

    public void showCatalog() {
        String catalogo = getContext().getString(R.string.home_card_catalog_title).concat(getResources().getString(R.string.home_card_new_line_character)).concat(getContext().getString(R.string.home_card_catalog_sub_title));
        tvwTitle.setText(Html.fromHtml(catalogo));
        ivwIndicator.setImageResource(R.drawable.ic_catalogos_black);
        ImageViewCompat.setImageTintList(ivwIndicator, ColorStateList.valueOf(
            ContextCompat.getColor(contexto, R.color.home_catalog)));
    }


    private void setGanaMasItem() {
        ImageViewCompat.setImageTintList(ivwIndicator, ColorStateList.valueOf(
            ContextCompat.getColor(contexto, R.color.dorado)));
        String oferta = getContext().getString(R.string.home_card_offer_title).concat(getResources().getString(R.string.home_card_new_line_character)).concat(getContext().getString(R.string.home_card_offer_sub_title_gana_mas_formato));
        tvwTitle.setText(Html.fromHtml(oferta));
    }

    public void showClients() {
        String clientes = getContext().getString(R.string.home_card_clients_title).concat(getResources().getString(R.string.home_card_new_line_character)).concat(getContext().getString(R.string.home_card_clients_sub_title));
        tvwTitle.setText(Html.fromHtml(clientes));
        ivwIndicator.setImageResource(R.drawable.ic_new_gestiona);
        ImageViewCompat.setImageTintList(ivwIndicator, ColorStateList.valueOf(
            ContextCompat.getColor(contexto, R.color.home_collection)));
    }

    public void showChat() {
        tvwTitle.setText(Html.fromHtml(getContext().getString(R.string.home_card_chat_title)));
        ivwIndicator.setImageResource(R.drawable.ic_asesor_digital);
    }

    public void showSubcampaign(int resourceId) {
        ivwBackground.setVisibility(View.VISIBLE);
        ivwBackground.setBackgroundResource(resourceId);
    }

    public void showEmpty(int cardType, String countryISO) {
        String texto = "";
        switch (cardType) {
            case CardType.INCENTIVES:
                texto = getContext().getString(R.string.home_card_incentive_title).concat(getResources().getString(R.string.home_card_new_line_character));
                if (null != countryISO && countryISO.equals(Country.CO)) {
                    texto = texto.concat(getContext().getString(R.string.home_card_incentive_sub_title));

                } else {
                    texto = texto.concat(getContext().getString(R.string.home_card_bonificaciones_sub_title));
                }
                ivwIndicator.setImageResource(R.drawable.ic_new_bonificacion);
                break;
            case CardType.CLIENTS:
                texto = getContext().getString(R.string.home_card_clients_title).concat(getResources().getString(R.string.home_card_new_line_character)).
                    concat(getContext().getString(R.string.home_card_clients_sub_title));
                ivwIndicator.setImageResource(R.drawable.ic_new_gestiona);
                break;
            case CardType.GANANCIAS:
                texto = getContext().getString(R.string.home_card_order_title).concat(getResources().getString(R.string.home_card_new_line_character)).
                    concat(getContext().getString(R.string.home_card_order_sub_title));
                ivwIndicator.setImageResource(R.drawable.ic_new_cart);
                break;
            case CardType.OFFERS:
                texto = getContext().getString(R.string.home_card_offer_title).concat(getResources().getString(R.string.home_card_new_line_character)).
                    concat(getContext().getString(R.string.home_card_offer_sub_title_gana_mas_formato));


                ivwIndicator.setImageResource(R.drawable.ic_new_offers);
                ImageViewCompat.setImageTintList(ivwIndicator, ColorStateList.valueOf(
                    ContextCompat.getColor(contexto, R.color.dorado)));
                break;
            case CardType.CHAT:

                texto = getContext().getString(R.string.home_card_chat_title).concat(getResources().getString(R.string.home_card_new_line_character));
                ivwIndicator.setImageResource(R.drawable.ic_asesor_digital);

                break;
            default:
                BelcorpLogger.v("Home card items", "No es de ningun tipo reconocido");
        }
        tvwTitle.setText(Html.fromHtml(texto));
    }

    public String getFullDescription() {
        return tvwTitle.getText().toString().trim();
    }

}

package biz.belcorp.consultoras.feature.contest.news;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.incentivos.CuponModel;
import biz.belcorp.consultoras.common.model.incentivos.NivelProgramaNuevaModel;
import biz.belcorp.consultoras.util.IncentivesUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class NewProgramAdapter extends RecyclerView.Adapter<NewProgramAdapter.ItemViewHolder> {

    private Context context;
    private List<NivelProgramaNuevaModel> allItems;
    private BigDecimal importeNivel;
    private int nivelAlcanzado = 0;
    private String currencySymbol;
    private String textoCupon;
    private String textoCuponIndependiente;
    private DecimalFormat decimalFormat;
    private NewProgramListener listener;
    private NewProgramCuponAdapter.NewProgramCuponListener cuponListener;

    private int premioAlcanzado = 0;

    NewProgramAdapter(Context context, BigDecimal importeNivel,
                      List<NivelProgramaNuevaModel> medals, String nivelAlcanzado,
                      String moneySymbol, DecimalFormat decimalFormat, String textoCupon,
                      String textoCuponIndependiente) {
        this.context = context;
        this.allItems = medals;
        this.importeNivel = importeNivel;
        this.currencySymbol = moneySymbol;
        this.textoCupon = textoCupon;
        this.textoCuponIndependiente = textoCuponIndependiente;
        this.decimalFormat = decimalFormat;

        if (null != nivelAlcanzado && !nivelAlcanzado.isEmpty())
            this.nivelAlcanzado = Integer.parseInt(nivelAlcanzado);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.item_incentive_detail_new_program, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        final NivelProgramaNuevaModel model = allItems.get(position);

        Spanned premios = new SpannableString("");

        if (null != model.getPremiosNuevas() && !model.getPremiosNuevas().isEmpty())
            premios = IncentivesUtil.getTextPremioNuevaDescription(model.getPremiosNuevas());
        else if (null != model.getCupones() && !model.getCupones().isEmpty())
            premios = IncentivesUtil.getTextCuponesDescription(model.getCupones());

        if (!premios.toString().isEmpty())
            holder.tvwHeaderPremio.setText(premios);
        else
            holder.tvwHeaderPremio.setVisibility(View.GONE);

        int level = 0;

        if (null != model.getCodigoNivel() && !model.getCodigoNivel().isEmpty())
            level = Integer.parseInt(model.getCodigoNivel());


        holder.seekbarPuntos.setEnabled(false);

        int status = getStatusLevel(model.getCodigoNivel());
        int enabled = 1;
        switch (status) {
            case -1:
                holder.ivwArrowIndicator.setVisibility(View.GONE);
                holder.tvwHeaderMessage.setText(String.format(context.getString(R.string.incentives_nuevas_ganaste),
                    StringUtil.getEmojiByUnicode(0x1F603)));
                holder.tvwHeaderMessage.setTextColor(ContextCompat.getColor(context,
                    R.color.lograste_puntaje));
                holder.tvwHeaderMessage.setVisibility(View.VISIBLE);
                holder.lltSeekbar.setVisibility(View.GONE);
                holder.cvwLeft.setClickable(false);
                setSeekBarData(level, holder, model, false);
                break;
            case 1:
                enabled = 0;
                holder.ivwArrowIndicator.setVisibility(View.VISIBLE);
                holder.tvwHeaderMessage.setVisibility(View.GONE);
                holder.vieBlocked.setVisibility(View.VISIBLE);
                holder.ivwLocked.setVisibility(View.VISIBLE);
                holder.lltSeekbar.setVisibility(View.GONE);
                setSeekBarData(level, holder, model, false);
                break;
            case 0:
            default:
                holder.ivwArrowIndicator.setVisibility(View.VISIBLE);
                setSeekBarData(level, holder, model, true);
                break;
        }

        showGifts(holder, model);
        showCupons(holder, model, enabled);

    }

    @Override
    public int getItemCount() {
        return allItems.size();
    }


    /*******************************************************************/

    private int getStatusLevel(String s) {

        if (null == s) return 0;

        int level = Integer.parseInt(s);

        if (level == nivelAlcanzado)
            return 0;
        else if (level > nivelAlcanzado)
            return 1;
        else
            return -1;
    }

    private void setSeekBarData(int level, final ItemViewHolder holder,
                                NivelProgramaNuevaModel model, boolean show) {
        String finalAmount = "";
        String initialAmount = currencySymbol + " " + decimalFormat.format(importeNivel);
        BigDecimal goalAmount = BigDecimal.ZERO;
        if (null != model.getPremiosNuevas()) {
            finalAmount = currencySymbol + " " + decimalFormat.format(model.getMontoExigidoPremio());
            goalAmount = model.getMontoExigidoPremio();
        } else if (null != model.getCupones()) {
            finalAmount = currencySymbol + " " + decimalFormat.format(model.getMontoExigidoCupon());
            goalAmount = model.getMontoExigidoCupon();
        }
        String headerLevel = level + "° PEDIDO: ";
        holder.tvwHeaderLevel.setText(headerLevel);
        holder.tvwHeaderGoal.setText(finalAmount);

        SpannableStringBuilder spannableTString = new SpannableStringBuilder();

        Spannable boldTSpannable = Spannable.Factory.getInstance().newSpannable(headerLevel);
        boldTSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, headerLevel.length(), 0);

        spannableTString.append(boldTSpannable);
        spannableTString.append(" ");
        spannableTString.append(finalAmount);

        holder.tvwLevel.setText(spannableTString);
        holder.tvwScore.setText(initialAmount);
        holder.tvwGoal.setText(finalAmount);
        holder.tvwLegalMessage.setText(String.format(context.getString(R.string.incentives_nuevas_legal), finalAmount));
        if (null != textoCupon) holder.tvwCuponNTitle.setText(textoCupon.toUpperCase());
        if (null != textoCuponIndependiente) holder.tvwCuponITitle.setText(textoCuponIndependiente.toUpperCase());
        premioAlcanzado = 0;

        if (show) {
            holder.tvwHeaderMessage.setVisibility(View.VISIBLE);
            holder.tvwDataMessage.setVisibility(View.VISIBLE);

            if (importeNivel.compareTo(goalAmount) >= 0) {
                holder.tvwHeaderMessage.setText(String.format(context.getString(R.string.incentives_nuevas_ganaste),
                    StringUtil.getEmojiByUnicode(0x1F603)));
                holder.tvwHeaderMessage.setTextColor(ContextCompat.getColor(context,
                    R.color.lograste_puntaje));
                holder.tvwDataMessage.setText(String.format(context.getString(R.string.incentives_nuevas_ganaste),
                    StringUtil.getEmojiByUnicode(0x1F603)));
                holder.tvwDataMessage.setTextColor(ContextCompat.getColor(context,
                    R.color.lograste_puntaje));

                holder.seekbarPuntos.setProgress(100);
                premioAlcanzado = 1;
            } else {
                BigDecimal faltante = goalAmount.subtract(importeNivel);
                String subtractAmount = currencySymbol + " " + decimalFormat.format(faltante);
                holder.tvwHeaderMessage.setText(String.format(context.getString(R.string.incentives_nuevas_progress_message_two), initialAmount, subtractAmount));
                holder.seekbarPuntos.setProgress((int) (((float) importeNivel.doubleValue() / goalAmount.doubleValue()) * 100));
                holder.tvwDataMessage.setText(String.format(context.getString(R.string.incentives_nuevas_progress_message_two), initialAmount, subtractAmount));
            }

            holder.showDetail();
        }
    }

    private void showGifts(final ItemViewHolder holder, NivelProgramaNuevaModel model) {

        if (null == model.getPremiosNuevas()) {
            holder.rvwGift.setVisibility(View.GONE);
            return;
        }

        if (!model.getPremiosNuevas().isEmpty()) {

            holder.rvwGift.setVisibility(View.VISIBLE);
            NewProgramGiftAdapter adapter = new NewProgramGiftAdapter(context,
                model.getPremiosNuevas(), currencySymbol, decimalFormat, premioAlcanzado);

            holder.rvwGift.setAdapter(adapter);
            holder.rvwGift.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
            holder.rvwGift.setHasFixedSize(true);
            holder.rvwGift.setNestedScrollingEnabled(false);
        }

    }

    private void showCupons(final ItemViewHolder holder, NivelProgramaNuevaModel model, Integer enabled) {

        if (null == model.getCupones()) {
            holder.lltCupones.setVisibility(View.GONE);
            return;
        }

        if (!model.getCupones().isEmpty()) {

            holder.lltCupones.setVisibility(View.VISIBLE);

            final List<CuponModel> cuponesNoIndependientes = new ArrayList<>();
            final List<CuponModel> cuponesIndependientes = new ArrayList<>();

            for (CuponModel cuponModel : model.getCupones()) {
                if (cuponModel.getIndicadorCuponIndependiente())
                    cuponesIndependientes.add(cuponModel);
                else
                    cuponesNoIndependientes.add(cuponModel);
            }

            if (!cuponesNoIndependientes.isEmpty()) {
                holder.separatorN.setVisibility(View.VISIBLE);
                holder.rvwCuponNoIndependiente.setVisibility(View.VISIBLE);

                NewProgramCuponAdapter adapter = new NewProgramCuponAdapter(context,
                    cuponesNoIndependientes, currencySymbol, decimalFormat, enabled);

                adapter.setCuponListener(cuponListener);
                holder.rvwCuponNoIndependiente.setAdapter(adapter);
                holder.rvwCuponNoIndependiente.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL, false));
                holder.rvwCuponNoIndependiente.setHasFixedSize(true);
                holder.rvwCuponNoIndependiente.setNestedScrollingEnabled(false);

                if (cuponesNoIndependientes.size() > 1) {
                    holder.lltSlideN.setVisibility(View.VISIBLE);
                } else {
                    holder.lltSlideN.setVisibility(View.GONE);
                }
            } else {
                holder.lltSlideN.setVisibility(View.GONE);
                holder.tvwCuponNTitle.setVisibility(View.GONE);
                holder.rvwCuponNoIndependiente.setVisibility(View.GONE);
                holder.separatorN.setVisibility(View.GONE);
            }

            if (!cuponesIndependientes.isEmpty()) {

                holder.separatorI.setVisibility(View.VISIBLE);
                holder.rvwCuponIndependiente.setVisibility(View.VISIBLE);

                NewProgramCuponAdapter adapter = new NewProgramCuponAdapter(context,
                    cuponesIndependientes, currencySymbol, decimalFormat, enabled);
                adapter.setCuponListener(cuponListener);

                holder.rvwCuponIndependiente.setAdapter(adapter);
                holder.rvwCuponIndependiente.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL, false));
                holder.rvwCuponIndependiente.setHasFixedSize(true);
                holder.rvwCuponIndependiente.setNestedScrollingEnabled(false);

                if (cuponesIndependientes.size() > 1) {
                    holder.lltSlideI.setVisibility(View.VISIBLE);
                } else {
                    holder.lltSlideI.setVisibility(View.GONE);
                }

            } else {
                holder.rvwCuponIndependiente.setVisibility(View.GONE);
                holder.separatorI.setVisibility(View.GONE);
                holder.lltSlideI.setVisibility(View.GONE);
                holder.tvwCuponITitle.setVisibility(View.GONE);
            }
        }

    }

    void setNewProgramCuponListener(NewProgramCuponAdapter.NewProgramCuponListener cuponListener) {
        this.cuponListener = cuponListener;
    }

    void setListener(NewProgramListener listener) {
        this.listener = listener;
    }

    /*******************************************************************/

    interface NewProgramListener {

        void movePos(int pos, int countList);
        void trackEvent(String label);
    }

    /*******************************************************************/

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivw_arrow)
        ImageView ivwArrowIndicator;

        @BindView(R.id.view_blocked)
        View vieBlocked;
        @BindView(R.id.ivw_locked)
        ImageView ivwLocked;

        @BindView(R.id.cvw_left)
        CardView cvwLeft;

        @BindView(R.id.llt_level_header)
        LinearLayout lltLevelHeader;
        @BindView(R.id.tvw_header_level)
        TextView tvwHeaderLevel;
        @BindView(R.id.tvw_header_goal)
        TextView tvwHeaderGoal;
        @BindView(R.id.tvw_header_title)
        TextView tvwHeaderPremio;
        @BindView(R.id.tvw_header_message)
        TextView tvwHeaderMessage;

        @BindView(R.id.llt_level_data)
        LinearLayout lltLevelData;
        @BindView(R.id.rvw_gift)
        RecyclerView rvwGift;
        @BindView(R.id.tvw_level)
        TextView tvwLevel;

        @BindView(R.id.llt_seekbar)
        LinearLayout lltSeekbar;
        @BindView(R.id.tvw_score)
        TextView tvwScore;
        @BindView(R.id.tvw_goal)
        TextView tvwGoal;
        @BindView(R.id.seekbar_puntos)
        SeekBar seekbarPuntos;

        @BindView(R.id.tvw_data_message)
        TextView tvwDataMessage;
        @BindView(R.id.llt_cupones)
        LinearLayout lltCupones;
        @BindView(R.id.view_separator_n)
        View separatorN;
        @BindView(R.id.tvw_cupon_n_title)
        TextView tvwCuponNTitle;
        @BindView(R.id.rvw_cupon_n)
        RecyclerView rvwCuponNoIndependiente;
        @BindView(R.id.tvw_legal_message)
        TextView tvwLegalMessage;
        @BindView(R.id.view_separator_i)
        View separatorI;
        @BindView(R.id.tvw_cupon_i_title)
        TextView tvwCuponITitle;
        @BindView(R.id.rvw_cupon_i)
        RecyclerView rvwCuponIndependiente;

        @BindView(R.id.llt_slide_i)
        LinearLayout lltSlideI;
        @BindView(R.id.llt_slide_n)
        LinearLayout lltSlideN;

        private ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @OnClick({R.id.cvw_left, R.id.ivw_arrow})
        void showDetail(View view) {
            if (view.getId() == R.id.ivw_arrow && listener != null) {
                listener.trackEvent(
                    tvwHeaderLevel.getText().toString().substring(0, tvwHeaderLevel.length() - 2));
            }

            showDetail();
        }

        private void showDetail() {
            if (lltLevelHeader.getVisibility() == View.VISIBLE) {
                lltLevelHeader.setVisibility(View.GONE);
                ivwArrowIndicator.setImageDrawable(VectorDrawableCompat.create(context.getResources(),
                    R.drawable.ic_arrow_up_black, null));
                lltLevelData.setVisibility(View.VISIBLE);
                listener.movePos(getAdapterPosition(), allItems.size());
            } else {
                lltLevelHeader.setVisibility(View.VISIBLE);
                ivwArrowIndicator.setImageDrawable(VectorDrawableCompat.create(context.getResources(),
                    R.drawable.ic_arrow_down_black, null));
                lltLevelData.setVisibility(View.GONE);
            }

        }
    }

}

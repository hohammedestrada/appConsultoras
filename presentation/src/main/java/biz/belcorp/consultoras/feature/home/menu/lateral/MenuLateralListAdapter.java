package biz.belcorp.consultoras.feature.home.menu.lateral;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.menu.MenuModel;
import biz.belcorp.consultoras.util.CenteredImageSpan;
import biz.belcorp.consultoras.util.anotation.MenuCode;
import biz.belcorp.library.util.DeviceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuLateralListAdapter extends RecyclerView.Adapter<MenuLateralListAdapter.Holder> {

    private List<MenuModel> mList = new ArrayList<>();
    private Context context;
    private final int dividerColor;
    private OnItemSelectedListener onMenuItemSelectedListener;
    private SubMenuLateralListAdapter.OnItemSelectedListener onSubMenuItemSelectedListener;

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivw_item_arrow)
        ImageView ivwItemArrow;

        @BindView(R.id.ivw_item_imagen)
        ImageView ivwItemImagen;

        @BindView(R.id.tvw_item_nombre)
        TextView tvvItemNombre;

        @BindView(R.id.llt_item_lateral)
        LinearLayout lltItemLateral;

        @BindView(R.id.rvw_sub_menus_laterales)
        RecyclerView rvwSubMenusLaterales;

        @BindView(R.id.view)
        View vieDivider;

        @OnClick(R.id.llt_item_lateral)
        public void onClick() {
            if (onMenuItemSelectedListener != null && onMenuItemSelectedListener.shouldMove()) {
                onMenuItemSelectedListener.onMenuItemSelected(mList.get(getAdapterPosition()),
                    getAdapterPosition());

                if (mList.get(getAdapterPosition()).getSubMenus() != null && !mList.get(getAdapterPosition()).getSubMenus().isEmpty()) {
                    if (rvwSubMenusLaterales.isShown()) {
                        if (getAdapterPosition() == (getItemCount() - 1)) {
                            vieDivider.setVisibility(View.GONE);
                        }

                        rvwSubMenusLaterales.setVisibility(View.GONE);
                        ivwItemArrow.setImageDrawable(ContextCompat.getDrawable(context,
                            R.drawable.ic_arrow_down_gray));
                    } else {
                        if (getAdapterPosition() == (getItemCount() - 1)) {
                            vieDivider.setVisibility(View.VISIBLE);
                        }

                        rvwSubMenusLaterales.setVisibility(View.VISIBLE);
                        ivwItemArrow.setImageDrawable(ContextCompat.getDrawable(context,
                            R.drawable.ic_arrow_up_gray));
                    }
                }
            }
        }

        public Holder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            rvwSubMenusLaterales.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        }
    }

    MenuLateralListAdapter(Context context) {
        this.context = context;
        this.dividerColor = ContextCompat.getColor(context, R.color.menu_separator_v2);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu_lateral,
            viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        final MenuModel item = mList.get(position);

        if (item.isMenuNuevo() == 1) {
            setTagNuevoTextView(context, item.getDescripcion(), holder.tvvItemNombre);
        } else {
            holder.tvvItemNombre.setText(item.getDescripcion());
        }

        if (item.getSubMenus() != null) {
            if (item.getSubMenus().size() > 0) {
                holder.ivwItemArrow.setVisibility(View.VISIBLE);
            } else {
                holder.ivwItemArrow.setVisibility(View.GONE);
            }
        } else {
            holder.ivwItemArrow.setVisibility(View.GONE);
        }

        holder.rvwSubMenusLaterales.setAdapter(item.getSubMenuLateralListAdapter());

        if (mList.get(position).getSubMenus() != null && !mList.get(position).getSubMenus().isEmpty()) {
            holder.ivwItemArrow.setImageDrawable(ContextCompat.getDrawable(context,
                R.drawable.ic_arrow_down_gray));
        }

        if (item.getCodigo().equals(MenuCode.MQVIRTUAL)) {
            holder.tvvItemNombre.setTextColor(ContextCompat.getColor(context, R.color.brand_general));
        }

        if (item.getDrawable() != null) {
            holder.ivwItemImagen.setImageDrawable(ContextCompat.getDrawable(context,
                item.getDrawable()));
        }

        holder.vieDivider.setBackgroundColor(dividerColor);

        if (position == (getItemCount() - 1)) {
            holder.vieDivider.setVisibility(View.GONE);
        } else {
            holder.vieDivider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    void setList(List<MenuModel> list) {
        mList.clear();
        mList.addAll(list);
        int menuPosition = 0;
        SubMenuLateralListAdapter subMenuLateralListAdapter;
        for (MenuModel menuModel : mList) {
            subMenuLateralListAdapter = new SubMenuLateralListAdapter(menuModel, menuPosition, context);
            subMenuLateralListAdapter.setOnMenuItemSelectedListener(onSubMenuItemSelectedListener);
            menuModel.setSubMenuLateralListAdapter(subMenuLateralListAdapter);
            menuPosition++;
        }
    }

    public void showMenuOption(String code, boolean status, String title){
        if(mList == null) return;
        if(mList.size() == 0) return;

        for(int position = 0 ; position < mList.size(); position++){
            if(!mList.get(position).getCodigo().trim().toUpperCase().equals(code.trim().toUpperCase())) continue;

            if(status) {
                mList.get(position).setDescripcion(title);
                notifyItemChanged(position);
            } else {
                mList.remove(position);
                notifyDataSetChanged();
            }

            return;
        }
    }

    void setOnMenuItemSelectedListener(OnItemSelectedListener onMenuItemSelectedListener) {
        this.onMenuItemSelectedListener = onMenuItemSelectedListener;
    }

    void setOnSubMenuItemSelectedListener(SubMenuLateralListAdapter.OnItemSelectedListener onSubMenuItemSelectedListener) {
        this.onSubMenuItemSelectedListener = onSubMenuItemSelectedListener;
        //si se ha llamado al constructor MenuLateralListAdapter(context,list), setea los listener en cada submenu
        for (MenuModel menuModel : mList) {
            SubMenuLateralListAdapter subMenuLateralListAdapter = menuModel.getSubMenuLateralListAdapter();
            if (subMenuLateralListAdapter != null) {
                subMenuLateralListAdapter.setOnMenuItemSelectedListener(onSubMenuItemSelectedListener);
            }
        }
    }

    private void setTagNuevoTextView(Context context, String mensaje, TextView twv) {

        twv.setGravity(Gravity.CENTER | Gravity.START);

        String msg = mensaje.trim() + "   ";

        SpannableString ss = new SpannableString(msg);

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_nuevo);

        int intrinsinctHeight = drawable.getIntrinsicHeight();
        int intrinsinctWidth = drawable.getIntrinsicWidth();

        int width = (int) DeviceUtil.convertDpToPixel(10,context);
        int height = (int) DeviceUtil.convertDpToPixel(5,context);

        Rect rect = new Rect(0, 0, intrinsinctWidth - width, intrinsinctHeight - height);

        drawable.setBounds(rect);

        CenteredImageSpan imageSpan = new CenteredImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(imageSpan, mensaje.trim().length() + 2, msg.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        twv.setText(ss);
    }

    public interface OnItemSelectedListener {
        void onMenuItemSelected(MenuModel menuModel, int position);

        boolean shouldMove();
    }

    public class ResizeImageSpan extends ImageSpan {

        private static final int MIN_SCALE_WIDTH = 240;

        // TextView's width.
        private int mContainerWidth;

        public ResizeImageSpan(Drawable d, String source, int containerWidth) {
            super(d, source);
            mContainerWidth = containerWidth;
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fm) {
            Drawable d = getCachedDrawable();
            Rect rect = getResizedDrawableBounds(d);

            if (fm != null) {
                fm.ascent = -rect.bottom;
                fm.descent = 0;

                fm.top = fm.ascent;
                fm.bottom = 0;
            }
            return rect.right;
        }

        private Rect getResizedDrawableBounds(Drawable d) {
            if (d == null || d.getIntrinsicWidth() == 0) {
                return new Rect(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            }
            int scaledHeight;

            if (d.getIntrinsicWidth() < mContainerWidth) {
                // Image smaller than container's width.
                if (d.getIntrinsicWidth() > MIN_SCALE_WIDTH &&
                    d.getIntrinsicWidth() >= d.getIntrinsicHeight()) {
                    // But larger than the minimum scale size, we need to scale the image to fit
                    // the width of the container.
                    int scaledWidth = mContainerWidth;
                    scaledHeight = d.getIntrinsicHeight() * scaledWidth / d.getIntrinsicWidth();
                    d.setBounds(0, 0, scaledWidth, scaledHeight);
                } else {
                    // Smaller than the minimum scale size, leave it as is.
                    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                }
            } else {
                // Image is larger than the container's width, scale down to fit the container.
                int scaledWidth = mContainerWidth;
                scaledHeight = d.getIntrinsicHeight() * scaledWidth / d.getIntrinsicWidth();
                d.setBounds(0, 0, scaledWidth, scaledHeight);
            }

            return d.getBounds();
        }

        private Drawable getCachedDrawable() {
            WeakReference<Drawable> wr = mDrawableRef;
            Drawable d = null;

            if (wr != null) {
                d = wr.get();
            }

            if (d == null) {
                d = getDrawable();
                mDrawableRef = new WeakReference<Drawable>(d);
            }

            return d;
        }

        private WeakReference<Drawable> mDrawableRef;
    }

}

package biz.belcorp.consultoras.common.notification.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import biz.belcorp.consultoras.ConsultorasApp;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.menu.MenuModel;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.embedded.gpr.OrderWebActivity;
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity;
import biz.belcorp.consultoras.feature.home.di.DaggerHomeComponent;
import biz.belcorp.consultoras.feature.home.di.HomeComponent;
import biz.belcorp.consultoras.feature.home.di.HomeModule;
import biz.belcorp.consultoras.util.anotation.MenuCodeTop;

/**
 *
 */
public class OrderNotification extends FrameLayout implements OrderNotificationView {

    @Inject
    protected OrderNotificationPresenter notificationPresenter;

    private String notificationState = OrderNotificationState.NONE;

    private LinearLayout lltAccepted;
    private LinearLayout lltRejected;
    private LinearLayout lltRejectedDetail;

    private ImageView ivwExpand;
    private TextView tvwSeeMore;
    private TextView tvwSeeLater;
    private TextView tvwModifyOrder;

    private boolean notificationVisibleState = false;

    public OrderNotification(Context context) {
        super(context);
        init();
    }

    public OrderNotification(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OrderNotification(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OrderNotification(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        initializeInjector();
        notificationPresenter.get();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_notification, this, false);
        addView(view);

        lltAccepted = findViewById(R.id.llt_accepted_order);
        lltRejected = findViewById(R.id.llt_rejected_order);
        lltRejectedDetail = findViewById(R.id.llt_rejected_order_detail);

        ivwExpand = findViewById(R.id.ivw_expand);
        tvwSeeMore = findViewById(R.id.tvw_see_detail);
        tvwSeeLater = findViewById(R.id.tvw_see_later);
        tvwModifyOrder = findViewById(R.id.tvw_edit_order);

        OnClickListener expandClickListener = v -> {
            notificationVisibleState = !notificationVisibleState;

            ivwExpand.setSelected(notificationVisibleState);
            tvwSeeMore.setSelected(notificationVisibleState);
            tvwSeeLater.setSelected(notificationVisibleState);
            toggleNotification();
        };

        OnClickListener editClickListener = v ->
            notificationPresenter.getMenuActive(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE);

        ivwExpand.setOnClickListener(expandClickListener);
        tvwSeeMore.setOnClickListener(expandClickListener);
        tvwSeeLater.setOnClickListener(expandClickListener);

        tvwModifyOrder.setOnClickListener(editClickListener);
    }

    private void initializeInjector() {
        HomeComponent homeComponent = DaggerHomeComponent.builder()
            .appComponent(((ConsultorasApp) (getContext().getApplicationContext())).getAppComponent())
            .activityModule(new ActivityModule((Activity) getContext()))
            .homeModule(new HomeModule())
            .build();
        homeComponent.inject(this);
        notificationPresenter.attachView(this);
    }

    private void toggleNotification() {

        switch (notificationState) {
            case OrderNotificationState.DEBT:
            case OrderNotificationState.MODIFY:
                setVisibility(VISIBLE);
                lltAccepted.setVisibility(GONE);

                if (lltRejected.getVisibility() == VISIBLE) {
                    lltRejected.setVisibility(GONE);
                    lltRejectedDetail.setVisibility(VISIBLE);
                } else {
                    lltRejected.setVisibility(VISIBLE);
                    lltRejectedDetail.setVisibility(GONE);
                }
                break;
            case OrderNotificationState.NONE:
                setVisibility(VISIBLE);
                lltAccepted.setVisibility(VISIBLE);
                ivwExpand.setVisibility(GONE);
                lltRejected.setVisibility(GONE);
                lltRejectedDetail.setVisibility(GONE);
                break;
            default:
                setVisibility(GONE);
                break;
        }
    }

    @Override
    public void setNotificationState(String notificationState) {
        this.notificationState = notificationState;

        switch (notificationState) {
            case OrderNotificationState.DEBT:
                setVisibility(VISIBLE);
                tvwModifyOrder.setVisibility(GONE);
                lltAccepted.setVisibility(GONE);
                lltRejected.setVisibility(VISIBLE);
                lltRejectedDetail.setVisibility(GONE);
                break;
            case OrderNotificationState.MODIFY:
                setVisibility(VISIBLE);
                lltAccepted.setVisibility(GONE);
                lltRejected.setVisibility(VISIBLE);
                lltRejectedDetail.setVisibility(GONE);
                break;
            case OrderNotificationState.NONE:
                setVisibility(VISIBLE);
                lltAccepted.setVisibility(VISIBLE);
                ivwExpand.setVisibility(GONE);
                lltRejected.setVisibility(GONE);
                lltRejectedDetail.setVisibility(GONE);
                break;
            default:
                setVisibility(GONE);
                break;
        }
    }

    @Override
    public void setNotificationDetail(String description) {
        TextView tvwNotificationDetail = findViewById(R.id.tvw_detail);
        tvwNotificationDetail.setText(description);
    }

    @Override
    public void setNotificationTitle(String title) {
        TextView tvwTitleAccepted = findViewById(R.id.tvw_title_accepted);
        TextView tvwTitleRejected = findViewById(R.id.tvw_title_rejected);
        TextView tvwTitleRejectedDetail = findViewById(R.id.tvw_title_rejected_detail);

        tvwTitleAccepted.setText(title);
        tvwTitleRejected.setText(title);
        tvwTitleRejectedDetail.setText(title);
    }

    @Override
    public void setNotificationURL(String url) {
        // EMPTY
    }

    @Override
    public void showMenuOrder(MenuModel menu) {
        if (menu != null)
            if (menu.isVisible() && menu.getCodigo().equals(MenuCodeTop.ORDERS)) {
                Intent intent = new Intent(getContext(), OrderWebActivity.class);
                getContext().startActivity(intent);
            } else if (menu.isVisible() && menu.getCodigo().equals(MenuCodeTop.ORDERS_NATIVE)) {
                Intent intent = new Intent(getContext(), AddOrdersActivity.class);
                getContext().startActivity(intent);
            }
        }

    @Override
    public void show() {
        setVisibility(VISIBLE);
    }

    @Override
    public void hide() {
        setVisibility(GONE);
    }

    @Override
    public Context context() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onVersionError(boolean required, String url) {
        throw new UnsupportedOperationException();
    }

    public void reload() {
        if (notificationPresenter != null)
            notificationPresenter.get();
    }
}

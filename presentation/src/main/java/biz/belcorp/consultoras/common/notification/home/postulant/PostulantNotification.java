package biz.belcorp.consultoras.common.notification.home.postulant;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import biz.belcorp.consultoras.ConsultorasApp;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.home.di.DaggerHomeComponent;
import biz.belcorp.consultoras.feature.home.di.HomeComponent;
import biz.belcorp.consultoras.feature.home.di.HomeModule;
import biz.belcorp.consultoras.util.Constant;

/**
 *
 */
public class PostulantNotification extends FrameLayout implements PostulantNotificationView {

    @Inject
    protected PostulantNotificationPresenter notificationPresenter;

    private TextView tvwDetail;
    private ImageView ivwExpand;
    private boolean notificationVisibleState = false;

    public PostulantNotification(Context context) {
        super(context);
        init();
    }

    public PostulantNotification(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PostulantNotification(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PostulantNotification(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        initializeInjector();
        notificationPresenter.get();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_notification_postulant, this, false);
        addView(view);

        ivwExpand = findViewById(R.id.ivw_expand);
        ivwExpand.setOnClickListener(v -> {
            if (notificationVisibleState) {
                notificationVisibleState = false;
                tvwDetail.setVisibility(GONE);
                ivwExpand.setImageDrawable(VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_arrow_down_black, null));
            } else {
                notificationVisibleState = true;
                tvwDetail.setVisibility(VISIBLE);
                ivwExpand.setImageDrawable(VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_arrow_up_black, null));
            }
        });
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

    private void setNotificationDetail(String description) {
        tvwDetail = findViewById(R.id.tvw_detail);
        tvwDetail.setText(description);
    }

    @Override
    public void show() {
        setVisibility(VISIBLE);
        setNotificationDetail(String.format(getResources().getString(R.string.notification_postulant_detail), Constant.BRAND_FOCUS_NAME));
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

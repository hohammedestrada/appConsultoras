package biz.belcorp.consultoras.feature.home.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import biz.belcorp.consultoras.ConsultorasApp;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseActivity;
import biz.belcorp.consultoras.common.network.NetworkEvent;
import biz.belcorp.consultoras.common.sync.SyncEvent;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.di.HasComponent;
import biz.belcorp.consultoras.feature.home.profile.di.DaggerMyProfileComponent;
import biz.belcorp.consultoras.feature.home.profile.di.MyProfileComponent;
import biz.belcorp.consultoras.feature.sms.SMSActivity;
import biz.belcorp.consultoras.sync.SyncUtil;
import biz.belcorp.consultoras.util.anotation.NetworkEventType;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MyProfileActivity extends BaseActivity implements HasComponent<MyProfileComponent>,
    LoadingView, MyProfileFragment.MyProfileFragmentListener {

    static final String TAG = "MyProfileActivity";
    static final int EMAIL_RESULT = 1;

    private MyProfileComponent component;

    @BindView(R.id.view_connection)
    View vwConnection;
    @BindView(R.id.ivw_connection)
    ImageView ivwConnection;
    @BindView(R.id.tvw_connection_message)
    TextView tvwConnectionMessage;
    @BindView(R.id.view_loading)
    View vwLoading;
    @BindView(R.id.view_loading_sync)
    View vwLoadingSync;

    private Uri capturedImageUri;
    private Unbinder unbinder;
    private MyProfileFragment fragment;
    private boolean changeEmail;

    /**********************************************************/

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_profile);

        unbinder = ButterKnife.bind(this);

        init(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkUtil.isThereInternetConnection(this))
            setStatusTopNetwork();
        else {
            vwConnection.setVisibility(View.VISIBLE);
            tvwConnectionMessage.setText(R.string.connection_offline);
            ivwConnection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert));
        }
    }

    @Override
    protected void onStop() {
        if(!changeEmail) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (unbinder != null)
            unbinder.unbind();
    }

    /**********************************************************/

    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0)
            fragmentManager.popBackStack();
        else
            super.onBackPressed();
    }

    /**********************************************************/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkEvent(NetworkEvent event) {
        switch (event.getEvent()) {
            case NetworkEventType.CONNECTION_AVAILABLE:
                SyncUtil.triggerRefresh();
                setStatusTopNetwork();
                break;
            case NetworkEventType.CONNECTION_NOT_AVAILABLE:
                vwConnection.setVisibility(View.VISIBLE);
                tvwConnectionMessage.setText(R.string.connection_offline);
                ivwConnection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_alert));
                break;
            case NetworkEventType.DATAMI_AVAILABLE:
                vwConnection.setVisibility(View.VISIBLE);
                tvwConnectionMessage.setText(getString(R.string.connection_datami_available));
                ivwConnection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet));
                break;
            case NetworkEventType.DATAMI_NOT_AVAILABLE:
            case NetworkEventType.WIFI:
            default:
                vwConnection.setVisibility(View.GONE);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncEvent(SyncEvent event) {
        if (event.isSync()) {
            vwLoadingSync.setVisibility(View.VISIBLE);
        } else {
            vwLoadingSync.setVisibility(View.GONE);
        }
    }

    /**********************************************************/

    private void setStatusTopNetwork() {
        switch (ConsultorasApp.getInstance().getDatamiType()) {
            case NetworkEventType.DATAMI_AVAILABLE:
                vwConnection.setVisibility(View.VISIBLE);
                tvwConnectionMessage.setText(getString(R.string.connection_datami_available));
                ivwConnection.setImageDrawable(ContextCompat.getDrawable(context(), R.drawable.ic_free_internet));
                break;
            case NetworkEventType.DATAMI_NOT_AVAILABLE:
            case NetworkEventType.WIFI:
            default:
                vwConnection.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initializeInjector();

        if (savedInstanceState == null) {
            fragment = new MyProfileFragment();
            addFragment(R.id.fltContainer, fragment);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (fragment != null)
                onBackPressed();
        });

        TextView tvwToolbarTitle = toolbar.findViewById(R.id.tvw_toolbar_title);
        tvwToolbarTitle.setText(R.string.my_profile_title);
    }

    @Override
    protected void initializeInjector() {
        this.component = DaggerMyProfileComponent.builder()
            .appComponent(getAppComponent())
            .activityModule(getActivityModule())
            .build();
    }

    @Override
    protected void initControls() {
        // Empty
    }

    @Override
    protected void initEvents() {
        // Empty
    }

    @Override
    public MyProfileComponent getComponent() {
        return component;
    }

    @Override
    public void showLoading() {
        if (null != vwLoading) vwLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (null != vwLoading) vwLoading.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == MyProfileFragment.REQUEST_CAMERA) {
                try {
                    if (capturedImageUri != null) {
                        startCropActivity(capturedImageUri);
                    } else {
                        Toast.makeText(this, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    BelcorpLogger.w(TAG, "REQUEST_CAMERA", ex);
                }
            } else if (requestCode == MyProfileFragment.REQUEST_SELECT_PICTURE) {
                try {
                    Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        startCropActivity(selectedUri);
                    } else {
                        Toast.makeText(this, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    BelcorpLogger.w(TAG, "REQUEST_SELECT_PICTURE", ex);
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            } else if (requestCode == MyProfileFragment.REQUEST_SMS){
                int result = data.getExtras().getInt(SMSActivity.RESULT_SMS);
                switch (result){
                    case 1: {
                        if (fragment != null)
                            fragment.onActivityResult(requestCode, resultCode, data);
                    } break;
                    case 2: finish(); break;
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    public void setChangeEmail(boolean changeEmail) {
        this.changeEmail = changeEmail;
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = "SampleCropImage.jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);
        uCrop.start(this);
    }

    private UCrop basisConfig(@NonNull UCrop uCrop) {
        int maxWidth = 240;
        int maxHeight = 240;

        if (maxWidth > 0 && maxHeight > 0) {
            uCrop = uCrop.withMaxResultSize(maxWidth, maxHeight);
        }

        uCrop = uCrop.withAspectRatio(1, 1);

        return uCrop;
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(false);
        options.setToolbarTitle(getString(R.string.my_profile_editar_foto));
        options.setToolbarColor(ContextCompat.getColor(this, R.color.black));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        options.setDimmedLayerColor(ContextCompat.getColor(this, R.color.black));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.white));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.white));
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.white));
        options.setCropGridColor(ContextCompat.getColor(this, R.color.white));
        return uCrop.withOptions(options);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            try {
                fragment.setPhoto(resultUri.getPath());
            } catch (Exception ex) {
                Log.e(TAG, "handleCropResult", ex);
            }
        } else {
            Toast.makeText(this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setImageUri(Uri uri) {
        this.capturedImageUri = uri;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeActivity(MyProfileActivityEvent event) {
        changeEmail = false;
        EventBus.getDefault().unregister(this);
        finish();
    }


    public static class MyProfileActivityEvent{/* Additional fields if needed */}

}


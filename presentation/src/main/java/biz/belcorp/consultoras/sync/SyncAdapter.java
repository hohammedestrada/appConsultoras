package biz.belcorp.consultoras.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;

import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import biz.belcorp.consultoras.sync.di.SyncComponent;

public class SyncAdapter extends AbstractThreadedSyncAdapter implements SyncI {

    @Inject
    protected SyncPresenter presenter;

    private SyncComponent component;
    private boolean mIsInjected = false;

    SyncAdapter(Context context, SyncComponent component) {
        this(context, true, component);
        if (!mIsInjected) {
            mIsInjected = onInjectView();
            if (mIsInjected) this.presenter.attachView(this);
        }
    }

    private SyncAdapter(Context context, boolean autoInitialize, SyncComponent component) {
        super(context, autoInitialize);
        this.component = component;
        try {
            mIsInjected = onInjectView();
        } catch (IllegalStateException e) {
            Logger.e(e.getMessage());
            mIsInjected = false;
        }
        if (mIsInjected) this.presenter.attachView(this);
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {
        presenter.initSync();
    }

    @Override
    public void startSync() {
        showHideSyncLoading(SyncUtil.RUNNING);
    }

    @Override
    public void stopSync() {
        showHideSyncLoading(SyncUtil.STOPPING);
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void onVersionError(boolean required, String url) {
        // EMPTY
    }

    protected boolean onInjectView() throws IllegalStateException {
        this.component.inject(this);
        return true;
    }

    /******************************************/

    private void showHideSyncLoading(String progress) {
        Intent intent = new Intent();
        intent.setAction(SyncUtil.ACTION);
        intent.putExtra(SyncUtil.STATUS, progress);
        context().sendBroadcast(intent);
    }
}

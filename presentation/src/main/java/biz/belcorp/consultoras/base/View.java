package biz.belcorp.consultoras.base;

import android.content.Context;

public interface View {
    /**
     * Get a {@link android.content.Context}.
     */
    Context context();

    void onVersionError(boolean required, String url);
}

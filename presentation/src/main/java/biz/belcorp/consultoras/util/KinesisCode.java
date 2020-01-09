package biz.belcorp.consultoras.util;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    KinesisCode.ID,
    KinesisCode.TYPE,
    KinesisCode.STREAMS,
    KinesisCode.REGION,
    KinesisCode.STREAM_DEVICE,
    KinesisCode.STREAM_CALLS,
    KinesisCode.STREAM_HISTORY,
    KinesisCode.STREAM_APPLICATION,
    KinesisCode.STREAM_LOCATION,
    KinesisCode.STREAM_USAGE,
    KinesisCode.STREAM_EXTRAS,
    KinesisCode.STREAM_USABILIDAD
})
@IntDef({
    KinesisCode.TYPE_SDK,
    KinesisCode.TYPE_USABILIDAD
})
public @interface KinesisCode {
    int TYPE_SDK = 1;
    int TYPE_USABILIDAD = 2;
    String ID = "id";
    String TYPE = "type";
    String STREAMS = "streams";
    String REGION = "region";
    String STREAM_DEVICE = "stream_device";
    String STREAM_CALLS = "stream_calls";
    String STREAM_HISTORY = "stream_history";
    String STREAM_APPLICATION = "stream_application";
    String STREAM_LOCATION = "stream_location";
    String STREAM_USAGE = "stream_usage";
    String STREAM_EXTRAS = "stream_extras";
    String STREAM_USABILIDAD = "stream_usabilidad";
}

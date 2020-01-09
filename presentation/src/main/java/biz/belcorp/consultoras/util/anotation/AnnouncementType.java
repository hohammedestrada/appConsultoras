package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    AnnouncementType.ANNOUNCEMENT_1F60B,
    AnnouncementType.ANNOUNCEMENT_1F40L
})
public @interface AnnouncementType {
    int ANNOUNCEMENT_1F60B = 1;
    int ANNOUNCEMENT_1F40L = 2;
}

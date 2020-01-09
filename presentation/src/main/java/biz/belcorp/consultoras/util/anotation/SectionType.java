package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    SectionType.VIDEO,
    SectionType.TEXT
})
public @interface SectionType {
    int VIDEO = 1;
    int TEXT = 2;
}

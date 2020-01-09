package biz.belcorp.consultoras.util.anotation;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    NoteCode.NEW,
    NoteCode.SAVED,
    NoteCode.DELETED
})
public @interface NoteCode {
    int NEW = 0;
    int SAVED = 1;
    int DELETED = -1;
}

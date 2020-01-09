package biz.belcorp.consultoras.util.anotation;


import android.support.annotation.IntegerRes;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    TypeContentHeader.NOTHING,
    TypeContentHeader.CARRUSEL_IMAGE,
    TypeContentHeader.STORIES
})

public @interface TypeContentHeader {

    String CARRUSEL_IMAGE = "1";
    String STORIES = "2";
    String NOTHING = "0";
}

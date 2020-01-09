package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    WizardLevelType.WIZARD_NIVEL_1,
    WizardLevelType.WIZARD_NIVEL_2,
    WizardLevelType.WIZARD_NIVEL_3,
    WizardLevelType.WIZARD_NIVEL_4,
    WizardLevelType.WIZARD_NIVEL_5,
    WizardLevelType.WIZARD_NIVEL_6
})
public @interface WizardLevelType {
    int WIZARD_NIVEL_1 = 1;
    int WIZARD_NIVEL_2 = 2;
    int WIZARD_NIVEL_3 = 3;
    int WIZARD_NIVEL_4 = 4;
    int WIZARD_NIVEL_5 = 5;
    int WIZARD_NIVEL_6 = 6;

}

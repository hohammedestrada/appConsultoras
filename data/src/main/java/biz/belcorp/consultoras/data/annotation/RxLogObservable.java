package biz.belcorp.consultoras.data.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Interfaz para capturar el log de un Observable
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Retention(RUNTIME)
@Target({METHOD})
public @interface RxLogObservable {
    Scope value() default Scope.EVERYTHING;

    enum Scope {
        EVERYTHING, STREAM, SCHEDULERS, EVENTS, NOTHING
    }
}

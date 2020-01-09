package biz.belcorp.consultoras.data.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Interfaz para capturar el log de un Subscriber
 *
 * @version 1.0
 * @since 2017-04-14
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface RxLogSubscriber {
}

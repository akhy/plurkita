package net.akhyar.plurkita.module;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * @author akhyar
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface For {
    String value() default "";
}

package net.vadamdev.customcontent.annotations;

import java.lang.annotation.*;

/**
 * Functionality annotated with Experimental is in heavy development and might be changed / removed at any time.
 *
 * @author VadamDev
 * @since 22/08/2022
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface Experimental {

}

package net.vadamdev.customcontent.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author VadamDev
 * @since 22/08/2022
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ForRemoval {
    String deadLine() default "Unknown";
    String reason() default "Unknown";
}

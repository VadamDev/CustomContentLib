package net.vadamdev.customcontent.annotations;

import java.lang.annotation.*;

/**
 * Functionality annotated with ForRemoval will no longer be supported and should not be used anymore and will likely be removed soon.
 *
 * @author VadamDev
 * @since 22/08/2022
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR})
public @interface ForRemoval {
    /**
     * Version that will most likely remove this feature.
     *
     * @return The deadline version or Unknown if this isn't know yet
     */
    String deadLine() default "Unknown";

    /**
     * The reason why this feature will be removed
     *
     * @return The reason why this feature will be removed
     */
    String reason() default "Unknown";

    /**
     * The replacement feature if exists
     *
     * @return The replacement feature if exists
     */
    String replacement() default "None";
}

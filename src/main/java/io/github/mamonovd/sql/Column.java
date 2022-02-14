package io.github.mamonovd.sql;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Specifies the mapped column for a persistent property or field.
 *
 * <blockquote><pre>
 *    Example:
 *
 *    &#064;Column(name="DESC")
 *    public String getDescription() { return description; }
 * </pre></blockquote>
 *
 * @author d_mamonov
 */ 
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface Column {

    /**
     * The name of the column.
     */
    String name();

}

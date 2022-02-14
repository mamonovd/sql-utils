package io.github.mamonovd.sql;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation applied to Entity class
 * Entity must contains fields annotated with {@link io.github.mamonovd.sql.Column}
 * 
 * @author d_mamonov
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Entity {
}

package vn.vnpt.ssdc.jdbc.annotations;


import vn.vnpt.ssdc.utils.GenericSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by vietnq on 10/25/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Serialized {
   Class serializer() default GenericSerializer.class;
   Class innerClass() default String.class;
}

package com.global.servicecommon.annotations;


import com.global.servicecommon.utils.GenericSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* Created by ThuyetLV
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Serialized {
   Class serializer() default GenericSerializer.class;
   Class innerClass() default String.class;
}

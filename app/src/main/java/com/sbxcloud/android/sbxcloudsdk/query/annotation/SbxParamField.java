package com.sbxcloud.android.sbxcloudsdk.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lgguzman on 19/02/17.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SbxParamField {

    String name() default "";
    boolean saveDefaultValue() default true;

}

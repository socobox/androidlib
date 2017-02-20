package com.sbxcloud.android.sbxcloudsdk.auth.user;

/**
 * Created by lgguzman on 19/02/17.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface  SbxPasswordField {
}

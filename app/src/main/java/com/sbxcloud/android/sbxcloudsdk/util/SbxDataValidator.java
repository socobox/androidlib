package com.sbxcloud.android.sbxcloudsdk.util;

import com.sbxcloud.android.sbxcloudsdk.exception.SbxAuthException;
import com.sbxcloud.android.sbxcloudsdk.query.SbxParamField;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by lgguzman on 20/02/17.
 */

public class SbxDataValidator {
    public static Object validate(Object o){

        if(o instanceof Date) {
            DateFormat jsonDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            jsonDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return  jsonDateFormat.format(o);
        }

        return o;
    }

    public static Date getDate(String o) throws Exception{

        DateFormat jsonDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        jsonDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return  jsonDateFormat.parse(o);

    }

    public static String getAnnotationName(Field variable, Annotation annotation ) throws Exception{
                String name=((SbxParamField)annotation).name();
                if(name.equals("")){
                    name=variable.getName();

                }
        return name;

    }
}

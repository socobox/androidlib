package com.sbxcloud.android.sbxcloudsdk.util;

import com.sbxcloud.android.sbxcloudsdk.exception.SbxAuthException;
import com.sbxcloud.android.sbxcloudsdk.exception.SbxModelException;
import com.sbxcloud.android.sbxcloudsdk.query.annotation.SbxKey;
import com.sbxcloud.android.sbxcloudsdk.query.annotation.SbxModelName;
import com.sbxcloud.android.sbxcloudsdk.query.annotation.SbxParamField;

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

    public static Boolean saveDefaultValue( Annotation annotation ) throws Exception{
        boolean data=((SbxParamField)annotation).saveDefaultValue();
        return data;

    }

    public static boolean hasKeyAnnotation(Class<?> myClass){
        final Field[] variables = myClass.getDeclaredFields();

        for (final Field variable : variables) {

            final Annotation annotation = variable.getAnnotation(SbxKey.class);
            if (annotation != null && annotation instanceof SbxKey) {
                return true;
            }
        }
        return false;
    }

    public static  void setKeyFromAnnotation(Object o, String key) throws Exception{
        Class<?> myClass = o.getClass();
        final Field[] variables = myClass.getDeclaredFields();

        for (final Field variable : variables) {

            final Annotation annotation = variable.getAnnotation(SbxKey.class);

            if (annotation != null && annotation instanceof SbxKey) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    variable.set(o,key);
                    variable.setAccessible(isAccessible);
                    return;
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }
            }
        }
        throw  new SbxModelException("no Key present on object");
    }


    public static  String getAnnotationModelNameFromVariable(Field variable) throws Exception{

        Annotation annotationClass = variable.getType().getAnnotation(SbxModelName.class);
        String modelName="";
        if(annotationClass instanceof SbxModelName){
            SbxModelName myAnnotation = (SbxModelName) annotationClass;
            modelName=myAnnotation.value();
        }
        return modelName;
    }
}

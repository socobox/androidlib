package com.sbxcloud.android.sbxcloudsdk.util;

import com.sbxcloud.android.sbxcloudsdk.exception.SbxAuthException;
import com.sbxcloud.android.sbxcloudsdk.query.SbxParamField;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by lgguzman on 20/02/17.
 */


public class SbxMagicComposer {

    public static Object getSbxModel(JSONObject jsonObject,  Class<?> clazz, int level)throws  Exception{
        level++;
        //accept only two level
        if (level==2)
            return null;
        Constructor<?> ctor = clazz.getConstructor();
        Object o = ctor.newInstance();
        final Field[] variables = clazz.getDeclaredFields();

        for (final Field variable : variables) {

            final Annotation annotation = variable.getAnnotation(SbxParamField.class);

            if (annotation != null && annotation instanceof SbxParamField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    String name=((SbxParamField)annotation).name();
                    String variabletype=variable.getGenericType().toString();
                    switch (variabletype){
                        case "class java.lang.String":{
                            variable.set(o,jsonObject.optString(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "int":{
                            variable.set(o,jsonObject.optInt(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "long":{
                            variable.set(o,jsonObject.optLong(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "double":{
                            variable.set(o,jsonObject.optDouble(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "float":{
                            variable.set(o,jsonObject.optDouble(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "class java.util.Date":{
                            variable.set(o,
                                    SbxDataValidator.getDate(
                                    jsonObject.optString(SbxDataValidator.getAnnotationName(variable,annotation))));
                            break;
                        }
                        case "boolean":{
                            variable.set(o,jsonObject.optBoolean(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        default:{

//                            Object obj= jsonObject.get(SbxDataValidator.getAnnotationName(variable,annotation));
//                            if(obj instanceof String){
//                                variable.set(o,null);
//                            }else {
//                                variable.set(o,
//                                        getSbxModel(
//                                                jsonObject.optJSONObject(SbxDataValidator.getAnnotationName(variable, annotation))
//                                                , clazz, level)
//                                );
//                            }
                            break;
                        }


                    }

                    variable.setAccessible(isAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }


        }


        return o;
    }
}



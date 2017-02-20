package com.sbxcloud.android.sbxcloudsdk.query;

import com.sbxcloud.android.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxEmailField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxNameField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxPasswordField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxUsernameField;
import com.sbxcloud.android.sbxcloudsdk.exception.SbxAuthException;
import com.sbxcloud.android.sbxcloudsdk.exception.SbxConfigException;
import com.sbxcloud.android.sbxcloudsdk.exception.SbxModelException;
import com.sbxcloud.android.sbxcloudsdk.util.SbxDataValidator;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;
import com.sbxcloud.android.sbxcloudsdk.util.UrlHelper;

import org.json.JSONException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Date;

/**
 * Created by lgguzman on 19/02/17.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SbxModelHelper {

    public static SbxUrlComposer getUrlinsertRow(Object o)throws Exception {

        int domain = SbxAuth.getDefaultSbxAuth().getDomain();
        String appKey = SbxAuth.getDefaultSbxAuth().getAppKey();
        String token = SbxAuth.getDefaultSbxAuth().getToken();
        Class<?> myClass = o.getClass();

        if(!hasKeyAnnotation(myClass)){
            throw new SbxModelException("SbxKey is required");
        }
        Annotation annotationClass = myClass.getAnnotation(SbxModelName.class);
        String modelName="";
        if(annotationClass instanceof SbxModelName){
            SbxModelName myAnnotation = (SbxModelName) annotationClass;
            modelName=myAnnotation.value();
        }else{
            throw  new SbxModelException("SbxModelName is required");
        }

        SbxUrlComposer sbxUrlComposer = new SbxUrlComposer(
                UrlHelper.URL_INSERT
                , UrlHelper.POST
        );
        SbxQueryBuilder queryBuilder = new SbxQueryBuilder(domain,modelName, SbxQueryBuilder.TYPE.INSERT);
        queryBuilder.insertNewEmptyRow();

        final Field[] variables = myClass.getDeclaredFields();

        for (final Field variable : variables) {

            final Annotation annotation = variable.getAnnotation(SbxParamField.class);

            if (annotation != null && annotation instanceof SbxParamField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    String name= SbxDataValidator.getAnnotationName(variable,annotation);
                    String variabletype=variable.getGenericType().toString();
                    switch (variabletype){
                        case "class java.lang.String":{
                            String data=(String)variable.get(o);
                            queryBuilder.insertFieldLastRow(name,data);
                            break;
                        }
                        case "int":{
                            int data=variable.getInt(o);
                            queryBuilder.insertFieldLastRow(name,data);
                            break;
                        }
                        case "long":{
                            long data=variable.getLong(o);
                            queryBuilder.insertFieldLastRow(name,data);
                            break;
                        }
                        case "double":{
                            double data=variable.getDouble(o);
                            queryBuilder.insertFieldLastRow(name,data);
                            break;
                        }
                        case "float":{
                            float data=variable.getFloat(o);
                            queryBuilder.insertFieldLastRow(name,data);
                            break;
                        }
                        case "class java.util.Date":{
                            Date data=(Date)variable.get(o);
                            queryBuilder.insertFieldLastRow(name,data);
                            break;
                        }
                        case "boolean":{
                            boolean data=variable.getBoolean(o);
                            queryBuilder.insertFieldLastRow(name,data);
                            break;
                        }
                        default:{
                            Object data=variable.get(o);
                            queryBuilder.insertFieldLastRow(name,getKeyFromAnnotation(data));
                            break;
                        }


                    }

                    variable.setAccessible(isAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }


        }

        return sbxUrlComposer
                .addHeader(UrlHelper.HEADER_KEY_APP_KEY, appKey)
                .addHeader(UrlHelper.HEADER_KEY_ENCODING, UrlHelper.HEADER_GZIP)
                .addHeader(UrlHelper.HEADER_KEY_CONTENT_TYPE, UrlHelper.HEADER_JSON)
                .addHeader(UrlHelper.HEADER_KEY_AUTORIZATION, UrlHelper.HEADER_BEARER+token)
                .addBody(queryBuilder.compile());
    }


    private static  String getKeyFromAnnotation(Object o) throws Exception{
        Class<?> myClass = o.getClass();
        final Field[] variables = myClass.getDeclaredFields();

        for (final Field variable : variables) {

            final Annotation annotation = variable.getAnnotation(SbxKey.class);

            if (annotation != null && annotation instanceof SbxKey) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    String s= (String)variable.get(o);
                    variable.setAccessible(isAccessible);
                    return s;
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }
            }
        }
        throw  new SbxModelException("no Key present on object");
    }

    public static SbxQueryBuilder prepareQuery(Class<?> myClass) throws  Exception{
        int domain = SbxAuth.getDefaultSbxAuth().getDomain();
        Annotation annotationClass = myClass.getAnnotation(SbxModelName.class);
        String modelName="";
        if(annotationClass instanceof SbxModelName){
            SbxModelName myAnnotation = (SbxModelName) annotationClass;
            modelName=myAnnotation.value();
        }else{
            throw  new SbxModelException("SbxModelName is required");
        }
        return new SbxQueryBuilder(domain,modelName);

    }

    public static SbxQueryBuilder prepareQuery(Class<?> myClass,  int page, int limit) throws  Exception{
        int domain = SbxAuth.getDefaultSbxAuth().getDomain();
        Annotation annotationClass = myClass.getAnnotation(SbxModelName.class);
        String modelName="";
        if(annotationClass instanceof SbxModelName){
            SbxModelName myAnnotation = (SbxModelName) annotationClass;
            modelName=myAnnotation.value();
        }else{
            throw  new SbxModelException("SbxModelName is required");
        }
        return new SbxQueryBuilder(domain,modelName,page,limit);

    }

    public static SbxUrlComposer getUrlQuery(SbxQueryBuilder sbxQueryBuilder) throws  Exception{
        String appKey = SbxAuth.getDefaultSbxAuth().getAppKey();
        String token = SbxAuth.getDefaultSbxAuth().getToken();
        SbxUrlComposer sbxUrlComposer = new SbxUrlComposer(
                UrlHelper.URL_FIND
                , UrlHelper.POST
        );
        return sbxUrlComposer
                .addHeader(UrlHelper.HEADER_KEY_APP_KEY, appKey)
                .addHeader(UrlHelper.HEADER_KEY_ENCODING, UrlHelper.HEADER_GZIP)
                .addHeader(UrlHelper.HEADER_KEY_CONTENT_TYPE, UrlHelper.HEADER_JSON)
                .addHeader(UrlHelper.HEADER_KEY_AUTORIZATION, UrlHelper.HEADER_BEARER+token)
                .addBody(sbxQueryBuilder.compile());
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
}
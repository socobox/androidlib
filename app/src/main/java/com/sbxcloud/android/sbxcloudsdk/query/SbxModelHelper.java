package com.sbxcloud.android.sbxcloudsdk.query;

import com.sbxcloud.android.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.android.sbxcloudsdk.exception.SbxAuthException;
import com.sbxcloud.android.sbxcloudsdk.exception.SbxModelException;
import com.sbxcloud.android.sbxcloudsdk.net.model.SbxQuery;
import com.sbxcloud.android.sbxcloudsdk.query.annotation.SbxKey;
import com.sbxcloud.android.sbxcloudsdk.query.annotation.SbxModelName;
import com.sbxcloud.android.sbxcloudsdk.query.annotation.SbxParamField;
import com.sbxcloud.android.sbxcloudsdk.util.SbxDataValidator;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;
import com.sbxcloud.android.sbxcloudsdk.util.UrlHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by lgguzman on 19/02/17.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SbxModelHelper {

    public static <T>SbxUrlComposer getUrlInsertOrUpdateRows(List<T> objects)throws Exception {

        if(objects.isEmpty())
            throw new SbxModelException("Array can not be empty");
        int domain = SbxAuth.getDefaultSbxAuth().getDomain();
        String appKey = SbxAuth.getDefaultSbxAuth().getAppKey();
        String token = SbxAuth.getDefaultSbxAuth().getToken();
        String key=null;

        Object of=objects.get(0);
        key=getKeyFromAnnotation(of);
        Class<?> myClass = of.getClass();
        if(!SbxDataValidator.hasKeyAnnotation(myClass)){
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
                key==null||key.equals("")? UrlHelper.URL_INSERT:UrlHelper.URL_UPDATE
                , UrlHelper.POST
        );
        SbxQueryBuilder queryBuilder = new SbxQueryBuilder(domain, modelName, SbxQueryBuilder.TYPE.INSERT);


        for(Object o: objects) {

            key=getKeyFromAnnotation(o);
            queryBuilder.insertNewEmptyRow();
            if (!(key == null || key.equals("")))
                queryBuilder.insertFieldLastRow("_KEY", key);
            final Field[] variables = myClass.getDeclaredFields();

            for (final Field variable : variables) {

                final Annotation annotation = variable.getAnnotation(SbxParamField.class);

                if (annotation != null && annotation instanceof SbxParamField) {
                    try {
                        boolean isAccessible = variable.isAccessible();
                        variable.setAccessible(true);
                        String name = SbxDataValidator.getAnnotationName(variable, annotation);
                        String variabletype = variable.getGenericType().toString();
                        switch (variabletype) {
                            case "class java.lang.String": {
                                Object os = variable.get(o);
                                if (os != null) {
                                    String data = (String) os;
                                    queryBuilder.insertFieldLastRow(name, data);
                                }
                                break;
                            }
                            case "int": {
                                int data = variable.getInt(o);
                                if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                    queryBuilder.insertFieldLastRow(name, data);
                                break;
                            }
                            case "class java.lang.Integer": {
                                int data = variable.getInt(o);
                                if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                    queryBuilder.insertFieldLastRow(name, data);
                                break;
                            }
                            case "long": {
                                long data = variable.getLong(o);
                                if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                    queryBuilder.insertFieldLastRow(name, data);
                                break;
                            }
                            case "class java.lang.Long": {
                                long data = variable.getLong(o);
                                if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                    queryBuilder.insertFieldLastRow(name, data);
                                break;
                            }
                            case "double": {
                                double data = variable.getDouble(o);
                                if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                    queryBuilder.insertFieldLastRow(name, data);
                                break;
                            }
                            case "class java.lang.Double": {
                                double data = variable.getDouble(o);
                                if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                    queryBuilder.insertFieldLastRow(name, data);
                                break;
                            }
                            case "float": {
                                float data = variable.getFloat(o);
                                if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                    queryBuilder.insertFieldLastRow(name, data);
                                break;
                            }
                            case "class java.lang.Float": {
                                float data = variable.getFloat(o);
                                if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                    queryBuilder.insertFieldLastRow(name, data);
                                break;
                            }
                            case "class java.util.Date": {
                                Object os = variable.get(o);
                                if (os != null) {
                                    Date data = (Date) os;
                                    queryBuilder.insertFieldLastRow(name, data);
                                }
                                break;
                            }
                            case "boolean": {
                                boolean data = variable.getBoolean(o);
                                if(SbxDataValidator.saveDefaultValue(annotation) || data)
                                    queryBuilder.insertFieldLastRow(name, data);
                                break;
                            }
                            default: {
                                Object os = variable.get(o);
                                if (os != null) {
                                    queryBuilder.insertFieldLastRow(name, getKeyFromAnnotation(os));
                                }
                                break;
                            }


                        }

                        variable.setAccessible(isAccessible);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new SbxAuthException(e);
                    }

                }


            }
        }

        return sbxUrlComposer
                .addHeader(UrlHelper.HEADER_KEY_APP_KEY, appKey)
                //       .addHeader(UrlHelper.HEADER_KEY_ENCODING, UrlHelper.HEADER_GZIP)
                .addHeader(UrlHelper.HEADER_KEY_CONTENT_TYPE, UrlHelper.HEADER_JSON)
                .addHeader(UrlHelper.HEADER_KEY_AUTORIZATION, UrlHelper.HEADER_BEARER+token)
                .addBody(queryBuilder.compile());
    }

    public static SbxUrlComposer getUrlInsertOrUpdateRow(Object o)throws Exception {

        int domain = SbxAuth.getDefaultSbxAuth().getDomain();
        String appKey = SbxAuth.getDefaultSbxAuth().getAppKey();
        String token = SbxAuth.getDefaultSbxAuth().getToken();
        String key=null;
        Class<?> myClass = o.getClass();
        if(!SbxDataValidator.hasKeyAnnotation(myClass)){
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

        key=getKeyFromAnnotation(o);
        SbxUrlComposer sbxUrlComposer = new SbxUrlComposer(
                key==null||key.equals("")? UrlHelper.URL_INSERT:UrlHelper.URL_UPDATE
                , UrlHelper.POST
        );

        SbxQueryBuilder queryBuilder = new SbxQueryBuilder(domain, modelName, SbxQueryBuilder.TYPE.INSERT);

        queryBuilder.insertNewEmptyRow();
        if(!(key==null||key.equals("")))
            queryBuilder.insertFieldLastRow("_KEY",key);
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
                            Object os=variable.get(o);
                            if(os!=null) {
                                String data = (String) os;
                                queryBuilder.insertFieldLastRow(name, data);
                            }
                            break;
                        }
                        case "int":{
                            int data=variable.getInt(o);
                            if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                queryBuilder.insertFieldLastRow(name,data);
                            break;
                        }
                        case "class java.lang.Integer": {
                            int data = variable.getInt(o);
                            if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                queryBuilder.insertFieldLastRow(name, data);
                            break;
                        }
                        case "long": {
                            long data = variable.getLong(o);
                            if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                queryBuilder.insertFieldLastRow(name, data);
                            break;
                        }
                        case "class java.lang.Long": {
                            long data = variable.getLong(o);
                            if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                queryBuilder.insertFieldLastRow(name, data);
                            break;
                        }
                        case "double": {
                            double data = variable.getDouble(o);
                            if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                queryBuilder.insertFieldLastRow(name, data);
                            break;
                        }
                        case "class java.lang.Double": {
                            double data = variable.getDouble(o);
                            if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                queryBuilder.insertFieldLastRow(name, data);
                            break;
                        }
                        case "float": {
                            float data = variable.getFloat(o);
                            if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                queryBuilder.insertFieldLastRow(name, data);
                            break;
                        }
                        case "class java.lang.Float": {
                            float data = variable.getFloat(o);
                            if(SbxDataValidator.saveDefaultValue(annotation) || data!=0)
                                queryBuilder.insertFieldLastRow(name, data);
                            break;
                        }
                        case "class java.util.Date":{
                            Object os=variable.get(o);
                            if(os!=null) {
                                Date data = (Date) os;
                                queryBuilder.insertFieldLastRow(name, data);
                            }
                            break;
                        }
                        case "boolean":{
                            boolean data=variable.getBoolean(o);
                            if(SbxDataValidator.saveDefaultValue(annotation) || data)
                                queryBuilder.insertFieldLastRow(name,data);
                            break;
                        }
                        default:{
                            Object os=variable.get(o);
                            if(os!=null) {
                                queryBuilder.insertFieldLastRow(name, getKeyFromAnnotation(os));
                            }
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
                //       .addHeader(UrlHelper.HEADER_KEY_ENCODING, UrlHelper.HEADER_GZIP)
                .addHeader(UrlHelper.HEADER_KEY_CONTENT_TYPE, UrlHelper.HEADER_JSON)
                .addHeader(UrlHelper.HEADER_KEY_AUTORIZATION, UrlHelper.HEADER_BEARER+token)
                .addBody(queryBuilder.compile());
    }


    public static  String getKeyFromAnnotation(Object o) throws Exception{
        Class<?> myClass = o.getClass();
        final Field[] variables = myClass.getDeclaredFields();

        for (final Field variable : variables) {

            final Annotation annotation = variable.getAnnotation(SbxKey.class);

            if (annotation != null && annotation instanceof SbxKey) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    Object object=variable.get(o);
                    if(object==null){
                        return null;
                    }
                    String s= (String)object;
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

    public static SbxQueryBuilder prepareQueryToDelete(Class<?> myClass) throws  Exception{
        int domain = SbxAuth.getDefaultSbxAuth().getDomain();
        Annotation annotationClass = myClass.getAnnotation(SbxModelName.class);
        String modelName="";
        if(annotationClass instanceof SbxModelName){
            SbxModelName myAnnotation = (SbxModelName) annotationClass;
            modelName=myAnnotation.value();
        }else{
            throw  new SbxModelException("SbxModelName is required");
        }
        return new SbxQueryBuilder(domain,modelName, SbxQueryBuilder.TYPE.DELETE);

    }

    public static SbxUrlComposer getUrlDelete(SbxQueryBuilder sbxQueryBuilder)throws Exception{
        if(sbxQueryBuilder.getKeysD()==null)
            throw new SbxModelException("SbxQueryBuilder is not DELETE Type");
        String appKey = SbxAuth.getDefaultSbxAuth().getAppKey();
        String token = SbxAuth.getDefaultSbxAuth().getToken();
        SbxUrlComposer sbxUrlComposer = new SbxUrlComposer(
                UrlHelper.URL_DELETE
                , UrlHelper.POST
        );
        return sbxUrlComposer
                .addHeader(UrlHelper.HEADER_KEY_APP_KEY, appKey)
//                .addHeader(UrlHelper.HEADER_KEY_ENCODING, UrlHelper.HEADER_GZIP)
//                .addHeader(UrlHelper.HEADER_KEY_CONTENT_TYPE, UrlHelper.HEADER_JSON)
                .addHeader(UrlHelper.HEADER_KEY_AUTORIZATION, UrlHelper.HEADER_BEARER+token)
                .addBody(sbxQueryBuilder.compile());
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
//                .addHeader(UrlHelper.HEADER_KEY_ENCODING, UrlHelper.HEADER_GZIP)
//                .addHeader(UrlHelper.HEADER_KEY_CONTENT_TYPE, UrlHelper.HEADER_JSON)
                .addHeader(UrlHelper.HEADER_KEY_AUTORIZATION, UrlHelper.HEADER_BEARER+token)
                .addBody(sbxQueryBuilder.compile());
    }

    public static SbxUrlComposer getUrlQueryKeys(SbxQueryBuilder sbxQueryBuilder, String [] keys) throws  Exception{
        String appKey = SbxAuth.getDefaultSbxAuth().getAppKey();
        String token = SbxAuth.getDefaultSbxAuth().getToken();
        SbxUrlComposer sbxUrlComposer = new SbxUrlComposer(
                UrlHelper.URL_FIND
                , UrlHelper.POST
        );
        return sbxUrlComposer
                .addHeader(UrlHelper.HEADER_KEY_APP_KEY, appKey)
//                .addHeader(UrlHelper.HEADER_KEY_ENCODING, UrlHelper.HEADER_GZIP)
//                .addHeader(UrlHelper.HEADER_KEY_CONTENT_TYPE, UrlHelper.HEADER_JSON)
                .addHeader(UrlHelper.HEADER_KEY_AUTORIZATION, UrlHelper.HEADER_BEARER+token)
                .addBody(sbxQueryBuilder.compileWithKeys(keys));
    }


}
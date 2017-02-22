package com.sbxcloud.android.sbxcloudsdk.auth;

import android.app.Application;

import com.sbxcloud.android.sbxcloudsdk.auth.config.SbxAppKeyField;
import com.sbxcloud.android.sbxcloudsdk.auth.config.SbxDomainField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxAuthToken;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxEmailField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxNameField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxPasswordField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxUsernameField;
import com.sbxcloud.android.sbxcloudsdk.exception.SbxAuthException;
import com.sbxcloud.android.sbxcloudsdk.exception.SbxConfigException;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;
import com.sbxcloud.android.sbxcloudsdk.util.UrlHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;

/**
 * Created by lgguzman on 18/02/17.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SbxAuth {


    private static SbxAuth defaultSbxAuth;
    private  int domain;
    private  String appKey;
    private String token;
    private boolean HttpLog;

    public boolean isHttpLog() {
        return HttpLog;
    }

    public void setHttpLog(boolean httpLog) {
        HttpLog = httpLog;
    }

    /**
     * initialize the data for comunicate on sbxcloud.com
     * @param domain the id of the domain on sbxcloud.com
     * @param appKey the app key on sbxcloud.com
     */
    public static void initialize(int domain, String appKey) {
        defaultSbxAuth = new SbxAuth();
        defaultSbxAuth.domain=domain;
        defaultSbxAuth.appKey=appKey;
    }

    /**
     *
     * @param app
     * @throws SbxConfigException
     */
    public static void initialize(Application app)throws SbxConfigException{
        defaultSbxAuth = new SbxAuth();
        defaultSbxAuth.domain=getDomainAnnotation(app);
        defaultSbxAuth.appKey=getAppKeyAnnotation(app);
    }

    /**
     *
     * @return
     * @throws SbxConfigException
     */
    public static SbxAuth getDefaultSbxAuth()throws SbxConfigException {
        if(defaultSbxAuth==null)
            throw  new SbxConfigException("SbxAuth not initialized");
        return defaultSbxAuth;
    }

    /**
     *
     * @return
     * @throws SbxConfigException
     */
    public int getDomain()throws SbxConfigException {
        if(domain==0){
            throw new SbxConfigException("SbxAuth not initialized");
        }
        return domain;
    }

    /**
     *
     * @return
     * @throws SbxConfigException
     */
    public String getToken()throws SbxConfigException {
        if(token==null){
            throw new SbxConfigException("User is not login yet.");
        }
        return token;
    }

    /**
     *
     * @return
     * @throws SbxConfigException
     */
    public String getAppKey()throws SbxConfigException{
        if(appKey==null){
            throw new SbxConfigException("SbxAuth not initialized");
        }
        return appKey;
    }


    public void resetToken(){
        token=null;
    }

    /**
     *
     * @param obj
     * @return
     * @throws SbxConfigException
     */
    public  String refreshToken(final Object obj)throws SbxConfigException, SbxAuthException{
        Class<?> myClass=obj.getClass();
        final Field[] variables = myClass.getDeclaredFields();
        for (final Field variable : variables) {

            final Annotation annotation = variable.getAnnotation(SbxAuthToken.class);

            if (annotation != null && annotation instanceof SbxAuthToken) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    token= (String)variable.get(obj);
                    variable.setAccessible(isAccessible);
                    return  token;
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxConfigException(e);
                }

            }
        }
        throw new SbxAuthException();
    }

    /**
     *
     * @param app
     * @return
     * @throws SbxConfigException
     */
    private static String getAppKeyAnnotation(final Object app)throws SbxConfigException{
        Class<?> myClass=app.getClass();
        final Field[] variables = myClass.getDeclaredFields();
        for (final Field variable : variables) {

            final Annotation annotation = variable.getAnnotation(SbxAppKeyField.class);

            if (annotation != null && annotation instanceof SbxAppKeyField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    String s= (String)variable.get(app);
                    variable.setAccessible(isAccessible);
                    return s;
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxConfigException(e);
                }

            }
        }
        throw new SbxConfigException();
    }

    /**
     *
     * @param app
     * @return
     * @throws SbxConfigException
     */
    private static int getDomainAnnotation(final Object app) throws SbxConfigException{
        Class<?> myClass=app.getClass();
        final Field[] variables = myClass.getDeclaredFields();
        for (final Field variable : variables) {

            final Annotation annotation = variable.getAnnotation(SbxDomainField.class);

            if (annotation != null && annotation instanceof SbxDomainField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    int s= variable.getInt(app);
                    variable.setAccessible(isAccessible);
                    return s;
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxConfigException(e);
                }

            }
        }
        throw new SbxConfigException();
    }


    public SbxUrlComposer getUrllogin(Object o)throws SbxConfigException, SbxAuthException{
        int domain=getDomain();
        String appKey=getAppKey();
        String username=null;
        String password=null;
        String email=null;
        Class<?> myClass=o.getClass();
        final Field[] variables = myClass.getDeclaredFields();
        for (final Field variable : variables) {


            final Annotation annotation = variable.getAnnotation(SbxUsernameField.class);

            if (annotation != null && annotation instanceof SbxUsernameField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    username= (String)variable.get(o);
                    variable.setAccessible(isAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }


            final Annotation annotationE = variable.getAnnotation(SbxEmailField.class);

            if (annotationE != null && annotationE instanceof SbxEmailField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    email= (String)variable.get(o);
                    variable.setAccessible(isAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }

            final Annotation annotationP = variable.getAnnotation(SbxPasswordField.class);

            if (annotationP != null && annotationP instanceof SbxPasswordField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    password= (String)variable.get(o);
                    variable.setAccessible(isAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }
        }
        SbxUrlComposer sbxUrlComposer;

        if(username==null){
            if(email==null){
                throw new SbxAuthException();
            }else{
                if(password==null){
                    throw new SbxAuthException();
                }else{
                    sbxUrlComposer = new SbxUrlComposer(
                            UrlHelper.URL_LOGIN
                            , UrlHelper.GET
                    ).setUrlParam("email",email)
                            .setUrlParam("password",password);
                }
            }
        }else{
            if(password==null){
                throw new SbxAuthException();
            }else{
                sbxUrlComposer = new SbxUrlComposer(
                        UrlHelper.URL_LOGIN
                        , UrlHelper.GET
                ).setUrlParam("login",username)
                        .setUrlParam("password",password);
            }
        }
       return  sbxUrlComposer
                .addHeader(UrlHelper.HEADER_KEY_APP_KEY,appKey);
            //    .addHeader(UrlHelper.HEADER_KEY_ENCODING,UrlHelper.HEADER_GZIP);
    }

    public SbxUrlComposer getUrlSigIn(Object o)throws SbxConfigException, SbxAuthException {
        int domain = getDomain();
        String appKey = getAppKey();
        String username = null;
        String password = null;
        String email = null;
        String name=null;
        Class<?> myClass = o.getClass();
        final Field[] variables = myClass.getDeclaredFields();
        for (final Field variable : variables) {

            final Annotation annotation = variable.getAnnotation(SbxUsernameField.class);

            if (annotation != null && annotation instanceof SbxUsernameField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    username = (String) variable.get(o);
                    variable.setAccessible(isAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }


            final Annotation annotationE = variable.getAnnotation(SbxEmailField.class);

            if (annotationE != null && annotationE instanceof SbxEmailField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    email = (String) variable.get(o);
                    variable.setAccessible(isAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }

            final Annotation annotationP = variable.getAnnotation(SbxPasswordField.class);

            if (annotationP != null && annotationP instanceof SbxPasswordField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    password = (String) variable.get(o);
                    variable.setAccessible(isAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }

            final Annotation annotationN = variable.getAnnotation(SbxNameField.class);

            if (annotationN != null && annotationN instanceof SbxNameField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    name = (String) variable.get(o);
                    variable.setAccessible(isAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }
        }
        if(username == null){
            throw new SbxAuthException("Annnotation SbxUsernameField is required");
        }
        if(email == null){
            throw new SbxAuthException("Annnotation SbxEmailField is required");
        }
        if(password == null){
            throw new SbxAuthException("Annnotation SbxPasswordField is required");
        }
        if(name == null){
            throw new SbxAuthException("Annnotation SbxNameField is required");
        }

        return new SbxUrlComposer(
                UrlHelper.URL_SIGN_UP
                , UrlHelper.GET
        ).setUrlParam("email",email)
                .setUrlParam("password",password)
                .setUrlParam("login",username)
                .setUrlParam("name",name)
                .setUrlParam("domain",domain+"")
                .addHeader(UrlHelper.HEADER_KEY_APP_KEY, appKey);
           //     .addHeader(UrlHelper.HEADER_KEY_ENCODING, UrlHelper.HEADER_GZIP);
    }


}

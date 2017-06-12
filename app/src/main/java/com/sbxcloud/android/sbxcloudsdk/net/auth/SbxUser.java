package com.sbxcloud.android.sbxcloudsdk.net.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.sbxcloud.android.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxAuthToken;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxEmailField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxNameField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxUsernameField;
import com.sbxcloud.android.sbxcloudsdk.exception.SbxAuthException;
import com.sbxcloud.android.sbxcloudsdk.exception.SbxConfigException;
import com.sbxcloud.android.sbxcloudsdk.net.ApiManager;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxSimpleResponse;
import com.sbxcloud.android.sbxcloudsdk.query.annotation.SbxKey;
import com.sbxcloud.android.sbxcloudsdk.util.SbxJsonModeler;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lgguzman on 20/02/17.
 */

public class SbxUser {

    private static SbxUser sbxUser;
    private int id;
    private String homeFolder;
    private JSONArray memberOf;
    private static final String FILE_NAME=SbxUser.class.getName();
    private static final String FILE_NAME_USER=FILE_NAME+"_User";

    public void signUpInBackground(final SbxSimpleResponse simpleResponse) throws Exception{
        SbxUrlComposer sbxUrlComposer= SbxAuth.getDefaultSbxAuth().getUrlSigIn(this);
        Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        ApiManager.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                simpleResponse.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if(jsonObject.getBoolean("success")) {
                        updateUser(jsonObject);
                        simpleResponse.onSuccess(SbxUser.this);
                    }else{
                        simpleResponse.onError(new Exception(jsonObject.getString("error")));
                    }
                }catch (Exception  e ){
                    simpleResponse.onError(e);
                }
            }
        });
    }

    public  <T extends SbxUser> Single<T> signUp(Class<T> type) throws Exception{
        SbxUrlComposer sbxUrlComposer= SbxAuth.getDefaultSbxAuth().getUrlSigIn(this);
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<T> () {
            @Override
            public void subscribe(final SingleEmitter<T> e) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response= ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {
                                updateUser(jsonObject);
                                e.onSuccess((T)SbxUser.this);
                                //sucess
                            } else {
                                //error
                                e.onError(new Exception(jsonObject.getString("error")));
                            }
                        }catch (Exception ex){
                            e.onError(ex);
                        }

                    }
                }).start();
            }
        }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends T>>() {
            @Override
            public SingleSource<? extends T> apply(Throwable throwable) throws Exception {
                return Single.error(throwable);
            }
        });
    }

    public <T extends SbxUser> Single<T> logIn(Class<T> type) throws Exception{
        SbxUrlComposer sbxUrlComposer= SbxAuth.getDefaultSbxAuth().getUrllogin(this);
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(final SingleEmitter<T> e) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response=ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            Log.e("user",jsonObject.toString());
                            if (jsonObject.getBoolean("success")) {
                                updateUser(jsonObject);
                                e.onSuccess((T)SbxUser.this);
                                //sucess
                            } else {
                                //error
                                e.onError(new Exception(jsonObject.getString("error")));
                            }
                        }catch (Exception ex){
                            e.onError(ex);
                        }
                    }
                }).start();

            }
        }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends T>>() {
            @Override
            public SingleSource<? extends T> apply(Throwable throwable) throws Exception {
                return Single.error(throwable);
            }
        });
    }


    public void logInBackground(final SbxSimpleResponse simpleResponse) throws Exception{
        SbxUrlComposer sbxUrlComposer= SbxAuth.getDefaultSbxAuth().getUrllogin(this);
        Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        ApiManager.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                simpleResponse.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if(jsonObject.getBoolean("success")) {
                        updateUser(jsonObject);
                        simpleResponse.onSuccess(SbxUser.this);
                    }else{
                        simpleResponse.onError(new Exception(jsonObject.getString("error")));
                    }
                }catch (Exception  e ){
                    simpleResponse.onError(e);
                }
            }
        });
    }

    public void logOut() throws Exception{
        sbxUser=null;
        SbxAuth.getDefaultSbxAuth().resetToken();
        SharedPreferences sharedPreferences = SbxAuth.getDefaultSbxAuth().getContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(FILE_NAME_USER, "").commit();
    }

    public void updateUser(JSONObject jsonObject) throws Exception{
        SharedPreferences sharedPreferences = SbxAuth.getDefaultSbxAuth().getContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(FILE_NAME_USER, jsonObject.toString()).commit();
        String token=jsonObject.getString("token");
        JSONObject userJson=jsonObject.getJSONObject("user");
        String email=userJson.getString("email");
        String name=userJson.getString("name");
        String login=userJson.getString("login");
        Class<?> myClass =SbxUser.this.getClass();
        final Field[] variables = myClass.getDeclaredFields();
        for (final Field variable : variables) {

            Annotation annotation = variable.getAnnotation(SbxAuthToken.class);

            if (annotation != null && annotation instanceof SbxAuthToken) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    variable.set(SbxUser.this,token);
                    variable.setAccessible(isAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }
            }

            annotation = variable.getAnnotation(SbxEmailField.class);

            if (annotation != null && annotation instanceof SbxEmailField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    variable.set(SbxUser.this,email);
                    variable.setAccessible(isAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }
            }

            annotation = variable.getAnnotation(SbxUsernameField.class);

            if (annotation != null && annotation instanceof SbxUsernameField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    variable.set(SbxUser.this,login);
                    variable.setAccessible(isAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }
            }

            annotation = variable.getAnnotation(SbxNameField.class);

            if (annotation != null && annotation instanceof SbxNameField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    variable.set(SbxUser.this,name);
                    variable.setAccessible(isAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }
            }
        }

        this.id=userJson.getInt("id");
        this.memberOf=userJson.getJSONArray("member_of");
        this.homeFolder=userJson.getString("home_folder_key");
        sbxUser=SbxUser.this;
        SbxAuth.getDefaultSbxAuth().refreshToken(SbxUser.this);
    }

    public static SbxUser getCurrentSbxUser(Class<?> clazz)throws Exception {
        if(sbxUser==null) {
            SharedPreferences sharedPreferences = SbxAuth.getDefaultSbxAuth().getContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
            String s=sharedPreferences.getString(FILE_NAME_USER,"");
            if(s.equals("")) {
                return null;
            }
            Constructor<?> ctor = clazz.getConstructor();
            sbxUser = (SbxUser) ctor.newInstance();
            sbxUser.updateUser(new JSONObject(s));
        }

        return sbxUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getHomeFolder() {
        return homeFolder;
    }

    public JSONArray getMemberOf() {
        return memberOf;
    }

    public  static Single<String> sendRequesCode(String emailTemplate, String subject, String from) throws Exception{
        SbxUrlComposer sbxUrlComposer= SbxAuth.getUrlRequestPasswordCode(emailTemplate,subject,from);
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(final SingleEmitter<String> e) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response=ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {
                                e.onSuccess("success");
                                //sucess
                            } else {
                                //error
                                e.onError(new Exception(jsonObject.getString("error")));
                            }
                        }catch (Exception ex){
                            e.onError(ex);
                        }
                    }
                }).start();

            }
        }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends String>>() {
            @Override
            public SingleSource<? extends String> apply(Throwable throwable) throws Exception {
                return Single.error(throwable);
            }
        });
    }


    public static void sendRequesCodeInBackground(String emailTemplate, String subject, String from,final SbxSimpleResponse simpleResponse) throws Exception{
        SbxUrlComposer sbxUrlComposer= SbxAuth.getUrlRequestPasswordCode(emailTemplate,subject,from);
        Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        ApiManager.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                simpleResponse.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if(jsonObject.getBoolean("success")) {
                        simpleResponse.onSuccess("success");
                    }else{
                        simpleResponse.onError(new Exception(jsonObject.getString("error")));
                    }
                }catch (Exception  e ){
                    simpleResponse.onError(e);
                }
            }
        });
    }

    public  static Single<String> changePassword(int userId, int code, String password) throws Exception{
        SbxUrlComposer sbxUrlComposer= SbxAuth.getUrlChangePasswordCode(userId,code,password);
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(final SingleEmitter<String> e) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response=ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {
                                e.onSuccess("success");
                                //sucess
                            } else {
                                //error
                                e.onError(new Exception(jsonObject.getString("error")));
                            }
                        }catch (Exception ex){
                            e.onError(ex);
                        }
                    }
                }).start();

            }
        }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends String>>() {
            @Override
            public SingleSource<? extends String> apply(Throwable throwable) throws Exception {
                return Single.error(throwable);
            }
        });
    }


    public static void changePasswordInBackground(int userId, int code, String password, final SbxSimpleResponse simpleResponse) throws Exception{
        SbxUrlComposer sbxUrlComposer= SbxAuth.getUrlChangePasswordCode(userId,code,password);
        Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        ApiManager.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                simpleResponse.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if(jsonObject.getBoolean("success")) {
                        simpleResponse.onSuccess("success");
                    }else{
                        simpleResponse.onError(new Exception(jsonObject.getString("error")));
                    }
                }catch (Exception  e ){
                    simpleResponse.onError(e);
                }
            }
        });
    }

    public  static <T extends SbxJsonModeler>Single<T> getAppConfig(final Class<T> tClass) throws Exception{
        SbxUrlComposer sbxUrlComposer= SbxAuth.getUrlDomainList();
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(final SingleEmitter<T> e) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response=ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {
                                SbxJsonModeler sbxJsonModeler = tClass.newInstance();
                                JSONArray jsonArray = jsonObject.getJSONArray("domains");
                                for(int i=0;i<=jsonArray.length();i++){
                                    if(SbxAuth.getDefaultSbxAuth().getDomain()==jsonArray.getJSONObject(i).getInt("id")){
                                        sbxJsonModeler.wrapFromJson(jsonArray.getJSONObject(i).getJSONObject("config"));
                                        break;
                                    }
                                }

                                e.onSuccess((T)sbxJsonModeler);
                                //sucess
                            } else {
                                //error
                                e.onError(new Exception(jsonObject.getString("error")));
                            }
                        }catch (Exception ex){
                            e.onError(ex);
                        }
                    }
                }).start();

            }
        }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends T>>() {
            @Override
            public SingleSource<? extends T> apply(Throwable throwable) throws Exception {
                return Single.error(throwable);
            }
        });
    }


    public static <T extends SbxJsonModeler> void getAppConfigInBackground(final Class<T> tClass, final SbxSimpleResponse simpleResponse) throws Exception{
        SbxUrlComposer sbxUrlComposer= SbxAuth.getUrlDomainList();
        Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        ApiManager.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                simpleResponse.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if(jsonObject.getBoolean("success")) {
                        SbxJsonModeler sbxJsonModeler = tClass.newInstance();
                        JSONArray jsonArray = jsonObject.getJSONArray("domains");
                        for(int i=0;i<=jsonArray.length();i++){
                            if(SbxAuth.getDefaultSbxAuth().getDomain()==jsonArray.getJSONObject(i).getInt("id")){
                                sbxJsonModeler.wrapFromJson(jsonArray.getJSONObject(i).getJSONObject("config"));
                                break;
                            }
                        }
                        simpleResponse.onSuccess(sbxJsonModeler);
                    }else{
                        simpleResponse.onError(new Exception(jsonObject.getString("error")));
                    }
                }catch (Exception  e ){
                    simpleResponse.onError(e);
                }
            }
        });
    }




}
package com.sbxcloud.android.sbxcloudsdk.net.auth;

import android.content.Context;
import android.content.SharedPreferences;

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
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

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
                        SbxAuth.getDefaultSbxAuth().refreshToken(sbxUser);
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
        sharedPreferences.edit().putString(FILE_NAME_USER, "").apply();
    }

    public void updateUser(JSONObject jsonObject) throws Exception{
        SharedPreferences sharedPreferences = SbxAuth.getDefaultSbxAuth().getContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(FILE_NAME_USER, jsonObject.toString()).apply();
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
                    break;
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
                    break;
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
                    break;
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
                    break;
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }
            }
        }

        this.id=userJson.getInt("id");
        this.memberOf=userJson.getJSONArray("member_of");
        this.homeFolder=userJson.getString("home_folder_key");
        sbxUser=SbxUser.this;
    }

    public static SbxUser  getCurrentSbxUser()throws Exception {
        if(sbxUser==null) {
            SharedPreferences sharedPreferences = SbxAuth.getDefaultSbxAuth().getContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
            String s=sharedPreferences.getString(FILE_NAME_USER,"");
            if(s.equals("")) {
                return null;
            }
            sbxUser= new SbxUser();
            sbxUser.updateUser(new JSONObject(s));
        }

        return sbxUser;
    }

    public int getId() {
        return id;
    }

    public String getHomeFolder() {
        return homeFolder;
    }

    public JSONArray getMemberOf() {
        return memberOf;
    }
}


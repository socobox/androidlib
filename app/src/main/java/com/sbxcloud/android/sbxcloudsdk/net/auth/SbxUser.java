package com.sbxcloud.android.sbxcloudsdk.net.auth;

import com.sbxcloud.android.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxAuthToken;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxUsernameField;
import com.sbxcloud.android.sbxcloudsdk.exception.SbxAuthException;
import com.sbxcloud.android.sbxcloudsdk.net.ApiManager;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxSimpleResponse;
import com.sbxcloud.android.sbxcloudsdk.query.annotation.SbxKey;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;

import org.json.JSONArray;
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
                        sbxUser=SbxUser.this;
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
                        sbxUser=SbxUser.this;
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
    }

    private void updateUser(JSONObject jsonObject) throws Exception{
        String token=jsonObject.getString("token");
        JSONObject userJson=jsonObject.getJSONObject("user");
        Class<?> myClass =SbxUser.this.getClass();
        final Field[] variables = myClass.getDeclaredFields();
        for (final Field variable : variables) {

            final Annotation annotation = variable.getAnnotation(SbxAuthToken.class);

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
        }
        this.id=userJson.getInt("id");
        this.memberOf=userJson.getJSONArray("member_of");
        this.homeFolder=userJson.getString("home_folder_key");
    }

    public static SbxUser  getCurrentSbxUser()throws SbxAuthException {
        if(sbxUser==null)
            throw new SbxAuthException("User not login");
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


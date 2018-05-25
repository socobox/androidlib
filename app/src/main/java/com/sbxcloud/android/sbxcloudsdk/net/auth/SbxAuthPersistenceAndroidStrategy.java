package com.sbxcloud.android.sbxcloudsdk.net.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.sbxcloud.java.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.java.sbxcloudsdk.auth.SbxAuthPersistenceStrategy;

public class SbxAuthPersistenceAndroidStrategy implements SbxAuthPersistenceStrategy {

    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String FILE_NAME=SbxAuth.class.getName();
    private static final String FILE_NAME_TOKEN=FILE_NAME+"_Token";
    private static final String FILE_NAME_DOMAIN=FILE_NAME+"_Domain";
    private static final String FILE_NAME_APPKEY=FILE_NAME+"_Appkey";

    public Context getContext() {
        return context;
    }

    SbxAuthPersistenceAndroidStrategy(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
    }

    @Override
    public int getDomain() {
        return sharedPreferences.getInt(FILE_NAME_DOMAIN,0);
    }

    @Override
    public String getToken() {
        return sharedPreferences.getString(FILE_NAME_TOKEN,"");
    }

    @Override
    public String getAppKey() {

        return sharedPreferences.getString(FILE_NAME_APPKEY,"");
    }

    @Override
    public void setDomain(int domain) {
        sharedPreferences.edit().putInt(FILE_NAME_DOMAIN, domain).apply();
    }

    @Override
    public void setToken(String token) {
        sharedPreferences.edit().putString(FILE_NAME_TOKEN, token).apply();
    }

    @Override
    public void setAppKey(String appKey) {
        sharedPreferences.edit().putString(FILE_NAME_APPKEY, appKey).apply();
    }
}

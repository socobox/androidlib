package com.sbxcloud.android.sbxcloudsdk.net;

import com.sbxcloud.android.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;
import com.sbxcloud.android.sbxcloudsdk.util.UrlHelper;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by lgguzman on 20/02/17.
 */

public class ApiManager {

    private static volatile ApiManager ourInstance;
    private OkHttpClient mOkHttpClient;

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static ApiManager getInstance() throws Exception {
        ApiManager temp = ourInstance;
        if (ourInstance == null){
            synchronized (ApiManager.class){
                temp = ourInstance;

                if (ourInstance == null){
                    temp = new ApiManager();
                    ourInstance = temp;
                }
            }
        }

        return temp;
    }
    private ApiManager() throws Exception {
        initOkHttpClient();
    }

    private void initOkHttpClient() throws Exception {

        SbxHttpLoggingInterceptor httpLoggingInterceptor = new SbxHttpLoggingInterceptor();
        if(SbxAuth.getDefaultSbxAuth().isHttpLog()) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    public Request sbxUrlComposer2Request(SbxUrlComposer sbxUrlComposer){
        Request.Builder builder = new Request.Builder()
                .url(sbxUrlComposer.getUrl());
        HashMap<String , String > headers=sbxUrlComposer.getHeader();
        for (String key:headers.keySet()){
            builder.addHeader(key,headers.get(key));
        }
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        switch (sbxUrlComposer.getType()){
            case UrlHelper.POST:{
                RequestBody requestBody= RequestBody.create(JSON,sbxUrlComposer.getBody().toString());
                builder.post(requestBody);
                break;
            }
            case UrlHelper.GET:{
                break;
            }
        }
        return builder.build();
    }

}

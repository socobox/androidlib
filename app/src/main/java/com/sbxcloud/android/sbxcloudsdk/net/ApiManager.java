package com.sbxcloud.android.sbxcloudsdk.net;

import com.sbxcloud.android.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;
import com.sbxcloud.android.sbxcloudsdk.util.UrlHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
                .connectTimeout(260, TimeUnit.SECONDS)
                .readTimeout(260, TimeUnit.SECONDS)
                .writeTimeout(260, TimeUnit.SECONDS)
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


        switch (sbxUrlComposer.getType()){

            case UrlHelper.GET:{
                break;
            }
            case UrlHelper.POST:{
                MediaType MEDIA=null;
                if(sbxUrlComposer.getFile()!=null) {
                    MEDIA = MediaType.parse("multipart/form-data");
                    RequestBody requestFile =
                            RequestBody.create(MEDIA, sbxUrlComposer.getFile());

                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("file", sbxUrlComposer.getFileName(), requestFile);
                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part body2 =
                            MultipartBody.Part.createFormData("model", sbxUrlComposer.getFileModel());

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addPart(body)
                            .addPart(body2)
                            .build();
                    builder.post(requestBody);

                }else{
                    MEDIA = MediaType.parse("application/json; charset=utf-8");
                    RequestBody requestBody= RequestBody.create(MEDIA,sbxUrlComposer.getBody().toString());
                    builder.post(requestBody);
                }
                break;
            }
            case UrlHelper.PUT:{
                MediaType MEDIA=null;
                    MEDIA = MediaType.parse("application/json; charset=utf-8");
                    RequestBody requestBody= RequestBody.create(MEDIA,sbxUrlComposer.getBody().toString());
                    builder.put(requestBody);
                break;

            }
        }
        return builder.build();
    }

}

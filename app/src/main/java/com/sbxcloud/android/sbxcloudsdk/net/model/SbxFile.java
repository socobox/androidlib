package com.sbxcloud.android.sbxcloudsdk.net.model;

import com.sbxcloud.android.sbxcloudsdk.message.SbxChannelHelper;
import com.sbxcloud.android.sbxcloudsdk.net.ApiManager;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxSimpleResponse;
import com.sbxcloud.android.sbxcloudsdk.net.message.SbxChannel;
import com.sbxcloud.android.sbxcloudsdk.util.SbxFileHelper;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.DuplicateFormatFlagsException;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lgguzman on 25/03/17.
 */

public class SbxFile {

    private String token;
    private String name;
    private String folderKey;
    private File file;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolderKey() {
        return folderKey;
    }

    public void setFolderKey(String folderKey) {
        this.folderKey = folderKey;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }


    public SbxFile(String name, String folderKey, File file) {
        this.name = name;
        this.folderKey = folderKey;
        this.file = file;
    }


    public Single<String> save() throws Exception{
        SbxUrlComposer sbxUrlComposer= SbxFileHelper.getUrlUploadFile(getFile(),getName(),getFolderKey());
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
                                setToken(jsonObject.getString("token"));
                                e.onSuccess(getToken());
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


    public void savelInBackground(final SbxSimpleResponse simpleResponse) throws Exception{
        SbxUrlComposer sbxUrlComposer= SbxFileHelper.getUrlUploadFile(getFile(),getName(),getFolderKey());
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
                        setToken(jsonObject.getString("token"));
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

}

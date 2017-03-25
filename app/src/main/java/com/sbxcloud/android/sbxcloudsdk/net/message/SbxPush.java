package com.sbxcloud.android.sbxcloudsdk.net.message;

import android.util.Log;

import com.sbxcloud.android.sbxcloudsdk.message.ChannelHelper;
import com.sbxcloud.android.sbxcloudsdk.net.ApiManager;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxSimpleResponse;
import com.sbxcloud.android.sbxcloudsdk.push.PushHelper;
import com.sbxcloud.android.sbxcloudsdk.util.SbxJsonModeler;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;

import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInput;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lgguzman on 24/03/17.
 */

public class SbxPush {

    public static Single<String> sendPush(String title, String alias, String message, SbxJsonModeler sbxJsonModeler) throws Exception{
        SbxUrlComposer sbxUrlComposer= PushHelper.getUrlSendPush(title,alias,message,sbxJsonModeler);
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
        });
    }


    public static void sendPush(String title, String alias, String message, SbxJsonModeler sbxJsonModeler, final SbxSimpleResponse simpleResponse) throws Exception{
        SbxUrlComposer sbxUrlComposer=   PushHelper.getUrlSendPush(title,alias,message,sbxJsonModeler);
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
}

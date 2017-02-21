package com.sbxcloud.android.sbxcloudsdk.net.model;

import com.sbxcloud.android.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.android.sbxcloudsdk.net.ApiManager;
import com.sbxcloud.android.sbxcloudsdk.net.auth.SbxUser;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxArrayResponse;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxSimpleResponse;
import com.sbxcloud.android.sbxcloudsdk.query.SbxModelHelper;
import com.sbxcloud.android.sbxcloudsdk.query.SbxQueryBuilder;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lgguzman on 21/02/17.
 */

public class SbxModel {

    public void saveInBackground(final SbxSimpleResponse simpleResponse)throws  Exception{
        SbxUrlComposer sbxUrlComposer = SbxModelHelper.getUrlInsertOrUpdateRow(SbxModel.this);
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
                        updateKey(jsonObject);
                        simpleResponse.onSuccess(SbxModel.this);
                    }else{
                        simpleResponse.onError(new Exception(jsonObject.getString("error")));
                    }
                }catch (Exception  e ){
                    simpleResponse.onError(e);
                }
            }
        });
    }


    private void updateKey(JSONObject jsonObject)throws  Exception{
        if(jsonObject.has("keys")) {
            String key = jsonObject.getJSONArray("keys").getString(0);
            SbxModelHelper.setKeyFromAnnotation(SbxModel.this, key);
        }
    }



}

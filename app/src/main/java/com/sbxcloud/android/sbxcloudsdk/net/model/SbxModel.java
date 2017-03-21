package com.sbxcloud.android.sbxcloudsdk.net.model;

import android.util.Log;

import com.sbxcloud.android.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.android.sbxcloudsdk.net.ApiManager;
import com.sbxcloud.android.sbxcloudsdk.net.auth.SbxUser;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxArrayResponse;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxSimpleResponse;
import com.sbxcloud.android.sbxcloudsdk.query.SbxModelHelper;
import com.sbxcloud.android.sbxcloudsdk.query.SbxQueryBuilder;
import com.sbxcloud.android.sbxcloudsdk.util.SbxDataValidator;
import com.sbxcloud.android.sbxcloudsdk.util.SbxMagicComposer;
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

    public void fetchInBackground(final SbxSimpleResponse simpleResponse)throws  Exception{

        String []keys = {SbxModelHelper.getKeyFromAnnotation(this)};
        SbxQueryBuilder sbxQueryBuilder = (new SbxQuery(this.getClass())).sbxQueryBuilder;
        SbxUrlComposer sbxUrlComposer = SbxModelHelper.getUrlQueryKeys(sbxQueryBuilder, keys);
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

                        JSONArray jsonArray=jsonObject.getJSONArray("results");
                        if(jsonArray.length()>0) {
                            SbxMagicComposer.getSbxModel(jsonArray.getJSONObject(0), SbxModel.this.getClass(),SbxModel.this, 0);
                            simpleResponse.onSuccess(SbxModel.this);
                        }else{
                            simpleResponse.onError(new Exception("key not found"));
                        }


                    }else{
                        simpleResponse.onError(new Exception(jsonObject.getString("error")));
                    }
                }catch (Exception  e ){
                    simpleResponse.onError(e);
                }
            }
        });
    }

    public void fetchInBackground(final SbxSimpleResponse simpleResponse, String []properties)throws  Exception{

        String []keys = {SbxModelHelper.getKeyFromAnnotation(this)};
        SbxQueryBuilder sbxQueryBuilder = (new SbxQuery(this.getClass())).fetch(properties).sbxQueryBuilder;
        SbxUrlComposer sbxUrlComposer = SbxModelHelper.getUrlQueryKeys(sbxQueryBuilder, keys);
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

                        JSONArray jsonArray=jsonObject.getJSONArray("results");
                        if(jsonArray.length()>0) {
                            if(jsonObject.has("fetched_results")) {
                                SbxMagicComposer.getSbxModel(jsonArray.getJSONObject(0), SbxModel.this.getClass(), SbxModel.this, 0,jsonObject.getJSONObject("fetched_results"));
                            }else{
                                SbxMagicComposer.getSbxModel(jsonArray.getJSONObject(0), SbxModel.this.getClass(), SbxModel.this, 0);
                            }
                            simpleResponse.onSuccess(SbxModel.this);
                        }else{
                            simpleResponse.onError(new Exception("key not found"));
                        }


                    }else{
                        simpleResponse.onError(new Exception(jsonObject.getString("error")));
                    }
                }catch (Exception  e ){
                    simpleResponse.onError(e);
                }
            }
        });
    }



    public void deleteInBackground(final SbxSimpleResponse simpleResponse)throws  Exception{
        SbxQueryBuilder sbxQueryBuilder = SbxModelHelper.prepareQueryToDelete(this.getClass());
        sbxQueryBuilder.addDeleteKey(SbxModelHelper.getKeyFromAnnotation(this));
        SbxUrlComposer sbxUrlComposer = SbxModelHelper.getUrlDelete(sbxQueryBuilder);
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
            SbxDataValidator.setKeyFromAnnotation(SbxModel.this, key);
        }
    }



}

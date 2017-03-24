package com.sbxcloud.android.sbxcloudsdk.net.model;

import android.util.Log;

import com.sbxcloud.android.sbxcloudsdk.net.ApiManager;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxArrayResponse;
import com.sbxcloud.android.sbxcloudsdk.query.SbxModelHelper;
import com.sbxcloud.android.sbxcloudsdk.query.SbxQueryBuilder;
import com.sbxcloud.android.sbxcloudsdk.util.SbxMagicComposer;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by lgguzman on 21/02/17.
 */

public class SbxQuery{
    Class<?> mClazz;
    SbxQueryBuilder sbxQueryBuilder;


    public SbxQuery(Class<?> clazz) throws Exception{
        this.mClazz=clazz;
        sbxQueryBuilder= SbxModelHelper.prepareQuery(clazz);
        // sbxQueryBuilder.insertNewEmptyRow();
    }

    public SbxQuery(Class<?> clazz, int page, int limit) throws Exception{
        this.mClazz=clazz;
        sbxQueryBuilder= SbxModelHelper.prepareQuery(clazz, page, limit);
        // sbxQueryBuilder.insertNewEmptyRow();
    }

    public SbxQuery addAND(){
        sbxQueryBuilder.addAND();
        return  this;
    }

    public SbxQuery addOR(){
        sbxQueryBuilder.addOR();
        return  this;
    }

    public SbxQuery whereIsEqual(String field, Object value) throws JSONException {
        sbxQueryBuilder.whereIsEqual(field,value);
        return   this;
    }

    public SbxQuery whereGreaterThan(String field, Object value) throws JSONException{
        sbxQueryBuilder.whereGreaterThan(field,value);
        return   this;
    }

    public SbxQuery whereLessThan(String field, Object value) throws JSONException{
        sbxQueryBuilder.whereLessThan(field,value);
        return   this;
    }

    public SbxQuery whereIsNotEqual(String field, Object value) throws JSONException{
        sbxQueryBuilder.whereLessThan(field,value);
        return   this;
    }

    public SbxQuery whereLike(String field, Object value) throws JSONException{
        sbxQueryBuilder.whereLike(field,value);
        return   this;
    }

    public SbxQuery fetch(String propieties[]) throws JSONException{
        sbxQueryBuilder.fetch(propieties);
        return   this;
    }

    public <T> void findInBackground(final SbxArrayResponse  <T> sbxArrayResponse) throws Exception{
        SbxUrlComposer sbxUrlComposer=SbxModelHelper.getUrlQuery(sbxQueryBuilder);
        Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        ApiManager.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sbxArrayResponse.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String s=response.body().string();
                    JSONObject jsonObject= new JSONObject(s);
                    if(jsonObject.getBoolean("success")) {

                        List <T>  list= new ArrayList<T>();
                        JSONArray jsonArray=jsonObject.getJSONArray("results");
                        for (int i=0;i<jsonArray.length();i++){
                            if(jsonObject.has("fetched_results")) {
                                list.add((T) SbxMagicComposer.getSbxModel(jsonArray.getJSONObject(i), mClazz, 0,jsonObject.getJSONObject("fetched_results")));
                            }else{
                                list.add((T) SbxMagicComposer.getSbxModel(jsonArray.getJSONObject(i), mClazz, 0));
                            }
                        }



                        sbxArrayResponse.onSuccess(list);


                    }else{
                        sbxArrayResponse.onError(new Exception(jsonObject.getString("error")));
                    }
                }catch (Exception e){
                    sbxArrayResponse.onError(e);
                }

            }
        });

    }

    public <T> Single<List<T>> find(Class<T> type) throws Exception{
        SbxUrlComposer sbxUrlComposer=SbxModelHelper.getUrlQuery(sbxQueryBuilder);
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<List<T>>() {
            @Override
            public void subscribe(final SingleEmitter<List<T>> e) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response= ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {


                                List <T>  list= new ArrayList<T>();
                                JSONArray jsonArray=jsonObject.getJSONArray("results");
                                for (int i=0;i<jsonArray.length();i++){
                                    if(jsonObject.has("fetched_results")) {
                                        list.add((T) SbxMagicComposer.getSbxModel(jsonArray.getJSONObject(i), mClazz, 0,jsonObject.getJSONObject("fetched_results")));
                                    }else{
                                        list.add((T) SbxMagicComposer.getSbxModel(jsonArray.getJSONObject(i), mClazz, 0));
                                    }
                                }

                                e.onSuccess(list);
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



}

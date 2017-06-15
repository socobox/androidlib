package com.sbxcloud.android.sbxcloudsdk.net.model;

import android.graphics.Point;
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
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
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

    int rowCount= 0;
    int totalPages=1;

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

    public void setPage(int page) throws Exception{
        sbxQueryBuilder.setPage(page);
    }

    public int getPage(){
        return sbxQueryBuilder.getPage();
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
        sbxQueryBuilder.whereIsNotEqual(field,value);
        return   this;
    }

    public SbxQuery whereLike(String field, Object value) throws JSONException{
        sbxQueryBuilder.whereLike(field,value);
        return   this;
    }

    public SbxQueryBuilder whereIsNotNull(String field) throws JSONException{
        return   sbxQueryBuilder.whereIsNotNull(field);
    }

    public SbxQuery fetch(String propieties[]) throws JSONException{
        sbxQueryBuilder.fetch(propieties);
        return   this;
    }

    public SbxQuery addGeoSort(double lat, double lon, String latName, String lonName) throws JSONException{
        sbxQueryBuilder.addGeoSort(lat, lon, latName, lonName);
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
                       rowCount = jsonObject.getInt("row_count");
                       totalPages = jsonObject.getInt("total_pages");
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





    public <T> void findAllInBackground(final SbxArrayResponse  <T> sbxArrayResponse) throws Exception{
        final List<T> list=new ArrayList<>();
        final Point error=new Point(0,0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int i=0;
                    while (getPage() <= totalPages && error.x==0 && i<=30) {
                        findInBackground(new SbxArrayResponse<T>() {
                            @Override
                            public void onError(Exception e) {
                                error.set(1,1);
                                notifyAll();
                                sbxArrayResponse.onError(e);
                            }
                            @Override
                            public void onSuccess(List<T> response) {
                                try {
                                    list.addAll(response);
                                    setPage(getPage() + 1);
                                } catch (Exception ex) {
                                    error.set(1,1);
                                    sbxArrayResponse.onError(ex);
                                } finally {
                                    notifyAll();
                                }
                            }
                        });
                        wait();
                        i++;
                    }
                    if(error.x==0)
                        sbxArrayResponse.onSuccess(list);
                    else
                        sbxArrayResponse.onError(new Exception("No completed"));
                }catch (Exception ex){
                    sbxArrayResponse.onError(ex);
                }
            }
        }).start();



    }

    public <T> Single<List<List<T>>> findAll(Class<T> type) throws Exception{
            List<Single<List<T>>> list = new ArrayList<>();
            for (int i=1; i<=Math.min(totalPages,30);i++){
                setPage(i);
                list.add(find(type));
            }
            return Single.merge(list).toList();
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
                                rowCount = jsonObject.getInt("row_count");
                                totalPages = jsonObject.getInt("total_pages");
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
        }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends List<T>>>() {
            @Override
            public SingleSource<? extends List<T>> apply(Throwable throwable) throws Exception {
                return Single.error(throwable);
            }
        });

    }



}

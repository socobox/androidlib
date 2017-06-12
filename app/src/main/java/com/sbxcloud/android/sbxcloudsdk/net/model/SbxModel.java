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
import java.util.List;

import io.reactivex.Single;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
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

    public <T extends SbxModel> Single<T> save(Class<T> type)throws  Exception{
        SbxUrlComposer sbxUrlComposer = SbxModelHelper.getUrlInsertOrUpdateRow(SbxModel.this);
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(final SingleEmitter<T> e) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response= ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {
                                updateKey(jsonObject);
                                e.onSuccess((T)SbxModel.this);
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
        }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends T>>() {
            @Override
            public SingleSource<? extends T> apply(Throwable throwable) throws Exception {
                return Single.error(throwable);

            }
        });

    }


    public static <T> Single <List<T>> saveMany(final List <T> list)throws  Exception{
        SbxUrlComposer sbxUrlComposer = SbxModelHelper.getUrlInsertOrUpdateRows(list);
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
                                updateKeys(jsonObject, list);
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

    public  <T extends SbxModel>Single<T> fetch(Class<T> type)throws  Exception{

        String []keys = {SbxModelHelper.getKeyFromAnnotation(this)};
        SbxQueryBuilder sbxQueryBuilder = (new SbxQuery(this.getClass())).sbxQueryBuilder;
        SbxUrlComposer sbxUrlComposer = SbxModelHelper.getUrlQueryKeys(sbxQueryBuilder, keys);
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(final  SingleEmitter<T> e) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response= ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {
                                JSONArray jsonArray=jsonObject.getJSONArray("results");
                                if(jsonArray.length()>0) {
                                    SbxMagicComposer.getSbxModel(jsonArray.getJSONObject(0), SbxModel.this.getClass(),SbxModel.this, 0);

                                    e.onSuccess((T)SbxModel.this);
                                }else{
                                    e.onError(new Exception("key not found"));
                                }
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
        }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends T>>() {
            @Override
            public SingleSource<? extends T> apply(Throwable throwable) throws Exception {
                return Single.error(throwable);
            }
        });
    }

    public  <T extends SbxModel>Single<T> fetch(Class<T> type,String []properties)throws  Exception{
        String []keys = {SbxModelHelper.getKeyFromAnnotation(this)};
        SbxQueryBuilder sbxQueryBuilder = (new SbxQuery(this.getClass())).fetch(properties).sbxQueryBuilder;
        SbxUrlComposer sbxUrlComposer = SbxModelHelper.getUrlQueryKeys(sbxQueryBuilder, keys);
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(final  SingleEmitter<T> e) throws Exception {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response= ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {
                                JSONArray jsonArray=jsonObject.getJSONArray("results");
                                if(jsonArray.length()>0) {
                                    if(jsonObject.has("fetched_results")) {
                                        SbxMagicComposer.getSbxModel(jsonArray.getJSONObject(0), SbxModel.this.getClass(), SbxModel.this, 0,jsonObject.getJSONObject("fetched_results"));
                                    }else{
                                        SbxMagicComposer.getSbxModel(jsonArray.getJSONObject(0), SbxModel.this.getClass(), SbxModel.this, 0);
                                    }

                                    e.onSuccess((T)SbxModel.this);
                                }else{
                                    e.onError(new Exception("key not found"));
                                }
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
        }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends T>>() {
            @Override
            public SingleSource<? extends T> apply(Throwable throwable) throws Exception {
                return Single.error(throwable);
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

    public  <T extends SbxModel>Single<T> delete(Class<T> type)throws  Exception{

        SbxQueryBuilder sbxQueryBuilder = SbxModelHelper.prepareQueryToDelete(this.getClass());
        sbxQueryBuilder.addDeleteKey(SbxModelHelper.getKeyFromAnnotation(this));
        SbxUrlComposer sbxUrlComposer = SbxModelHelper.getUrlDelete(sbxQueryBuilder);
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(final  SingleEmitter<T> e) throws Exception {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response= ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if(jsonObject.getBoolean("success")) {
                                e.onSuccess((T)SbxModel.this);
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
        }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends T>>() {
            @Override
            public SingleSource<? extends T> apply(Throwable throwable) throws Exception {
                return Single.error(throwable);
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

    private static <T> void updateKeys(JSONObject jsonObject, List<T> list)throws  Exception{
        if(jsonObject.has("keys")) {
            JSONArray key = jsonObject.getJSONArray("keys");
            for (int i=0;i<key.length();i++){

                SbxDataValidator.setKeyFromAnnotation(list.get(i), key.getString(i));
            }
        }
    }



}

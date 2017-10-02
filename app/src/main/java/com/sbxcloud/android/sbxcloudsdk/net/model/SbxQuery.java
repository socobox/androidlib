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

    public SbxQuery andWhereIsEqual(String field, Object value) throws JSONException{
        sbxQueryBuilder.andWhereIsEqual(field,value);
        return this;
    }

    public SbxQuery andWhereIsNotNull(String field) throws JSONException{
        sbxQueryBuilder.andWhereIsNotNull(field );
        return this;
    }

    public SbxQuery andWhereGreaterThan(String field, Object value) throws JSONException{
        sbxQueryBuilder.andWhereGreaterThan(field,value);
        return this;
    }

    public SbxQuery andWhereLessThan(String field, Object value) throws JSONException{
        sbxQueryBuilder.andWhereLessThan(field,value);
        return this;
    }

    public SbxQuery andWhereGreaterOrEqualThan(String field, Object value) throws JSONException{
        sbxQueryBuilder.andWhereGreaterOrEqualThan(field,value);
        return this;
    }

    public SbxQuery andWhereLessOrEqualThan(String field, Object value) throws JSONException{
        sbxQueryBuilder.andWhereLessOrEqualThan(field,value);
        return this;
    }

    public SbxQuery andWhereIsNotEqual(String field, Object value) throws JSONException{
        sbxQueryBuilder.andWhereIsNotEqual(field,value);
        return this;
    }

    public SbxQuery andWhereLike(String field, Object value) throws JSONException{
        sbxQueryBuilder.andWhereLike(field,value);
        return this;
    }

    public SbxQuery andWhereIn(String field, Object value) throws JSONException{
        sbxQueryBuilder.andWhereIn(field,value);
        return this;
    }


    public SbxQuery orWhereIsEqual(String field, Object value) throws JSONException{
        sbxQueryBuilder.orWhereIsEqual(field,value);
        return this;
    }

    public SbxQuery orWhereIsNotNull(String field) throws JSONException{
        sbxQueryBuilder.orWhereIsNotNull(field );
        return this;
    }

    public SbxQuery orWhereGreaterThan(String field, Object value) throws JSONException{
        sbxQueryBuilder.orWhereGreaterThan(field,value);
        return this;
    }

    public SbxQuery orWhereLessThan(String field, Object value) throws JSONException{
        sbxQueryBuilder.orWhereLessThan(field,value);
        return this;
    }

    public SbxQuery orWhereGreaterOrEqualThan(String field, Object value) throws JSONException{
        sbxQueryBuilder.orWhereGreaterOrEqualThan(field,value);
        return this;
    }

    public SbxQuery orWhereLessOrEqualThan(String field, Object value) throws JSONException{
        sbxQueryBuilder.orWhereLessOrEqualThan(field,value);
        return this;
    }

    public SbxQuery orWhereIsNotEqual(String field, Object value) throws JSONException{
        sbxQueryBuilder.orWhereIsNotEqual(field,value);
        return this;
    }

    public SbxQuery orWhereLike(String field, Object value) throws JSONException{
        sbxQueryBuilder.orWhereLike(field,value);
        return this;
    }

    public SbxQuery orWhereIn(String field, Object value) throws JSONException{
        sbxQueryBuilder.orWhereIn(field,value);
        return this;
    }

    public SbxReferenceJoin orWhereReferenceJoinBetween(String field, String referenceField) throws Exception {
        return new SbxReferenceJoin(this, sbxQueryBuilder.orWhereReferenceJoinBetween(field, referenceField));
    }

    public SbxReferenceJoin andWhereReferenceJoinBetween(String field, String referenceField) throws Exception {
        return new SbxReferenceJoin(this, sbxQueryBuilder.andWhereReferenceJoinBetween(field, referenceField));
    }

    public class SbxReferenceJoin {

        private SbxQueryBuilder.ReferenceJoin join;
        private SbxQuery query;
        SbxReferenceJoin(SbxQuery query, SbxQueryBuilder.ReferenceJoin join) throws Exception {
            this.join = join;
            this.query = query;
        }

        public SbxFilterJoin in(String referenceModel) {
            return new SbxFilterJoin(query, join.in(referenceModel));
        }
    }


    public class SbxFilterJoin {

        private SbxQueryBuilder.FilterJoin filter;
        private SbxQuery find;


        SbxFilterJoin(SbxQuery find,  SbxQueryBuilder.FilterJoin filter) {
            this.filter = filter;
            this.find = find;
        }


        public SbxQuery filterWhereIsEqual(String field, Object value) throws Exception{
            this.filter.filterWhereIsEqual( field, value);
            return this.find;
        }

        public SbxQuery FilterWhereIsNotNull(String field) throws  Exception{
            this.filter.FilterWhereIsNotNull( field);
            return this.find;
        }

        public SbxQuery FilterWhereIsNull(String field) throws  Exception{
            this.filter.FilterWhereIsNull( field);
            return this.find;
        }

        public SbxQuery FilterWhereGreaterThan(String field, Object value) throws  Exception{
            this.filter.FilterWhereGreaterThan( field, value);
            return this.find;
        }

        public SbxQuery FilterWhereLessThan(String field, Object value) throws  Exception{
            this.filter.FilterWhereLessThan( field, value);
            return this.find;
        }

        public SbxQuery FilterWhereGreaterOrEqualThan(String field, Object value) throws  Exception{
            this.filter.FilterWhereGreaterOrEqualThan( field, value);
            return this.find;
        }

        public SbxQuery FilterWhereLessOrEqualThan(String field, Object value) throws  Exception{
            this.filter.FilterWhereLessOrEqualThan( field, value);
            return this.find;
        }

        public SbxQuery FilterWhereIsNotEqual(String field, Object value) throws  Exception{
            this.filter.FilterWhereIsNotEqual( field, value);
            return this.find;
        }

        public SbxQuery FilterWhereLike(String field, Object value) throws  Exception{
            this.filter.FilterWhereLike( field, value);
            return this.find;
        }

        public SbxQuery FilterWhereIn(String field, Object value) throws  Exception{
            this.filter.FilterWhereIn( field, value);
            return this.find;
        }

        public SbxQuery FilterWhereNotIn(String field, Object value) throws  Exception{
            this.filter.FilterWhereNotIn( field, value);
            return this.find;
        }

    }

    public SbxQuery fetch(String propieties[]) throws JSONException{
        sbxQueryBuilder.fetch(propieties);
        return this;
    }

    public SbxQuery addGeoSort(double lat, double lon, String latName, String lonName) throws JSONException{
        sbxQueryBuilder.addGeoSort(lat, lon, latName, lonName);
        return   this;
    }

    private static final class Lock { }
    private final Object lock = new Lock();

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





    public <T>  void  findAllInBackground(final SbxArrayResponse  <T> sbxArrayResponse) throws Exception{
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
                                    synchronized (lock) {
                                        lock.notifyAll();
                                    }
                                }
                            }
                        });
                        synchronized (lock) {
                            lock.wait();
                        }
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

package com.sbxcloud.android.sbxcloudsdk.net.cloudscript;

import com.sbxcloud.android.sbxcloudsdk.cloudscript.SbxCloudScriptHelper;
import com.sbxcloud.android.sbxcloudsdk.net.ApiManager;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxSimpleResponse;
import com.sbxcloud.android.sbxcloudsdk.net.model.SbxModel;
import com.sbxcloud.android.sbxcloudsdk.query.SbxModelHelper;
import com.sbxcloud.android.sbxcloudsdk.util.SbxDataValidator;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * Created by lgguzman on 12/06/17.
 */

public class SbxCloudScript {

    private String key;
    private JSONObject params;
    private int cloudScriptId;
    private int runId;
    private double duration;
    private JSONObject bodyResult;
    private List<Log> outLog=new ArrayList<>();
    private List<Log> errorLog=new ArrayList<>();

    public double getDuration() {
        return duration;
    }

    public int getCloudScriptId() {
        return cloudScriptId;
    }

    public JSONObject getBodyResult() {
        return bodyResult;
    }

    public List<Log> getOutLog() {
        return outLog;
    }

    public List<Log> getErrorLog() {
        return errorLog;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }

    public SbxCloudScript(String key, JSONObject params) {
        this.key = key;
        this.params = params;
    }



    public void runInBackground(final SbxSimpleResponse<SbxCloudScript> simpleResponse)throws  Exception{
        SbxUrlComposer sbxUrlComposer = SbxCloudScriptHelper.getUrlRunCloudScript(getKey(),getParams());
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
                        update(jsonObject);
                        simpleResponse.onSuccess(SbxCloudScript.this);
                    }else{
                        simpleResponse.onError(new Exception(jsonObject.getString("message")));
                    }
                }catch (Exception  e ){
                    simpleResponse.onError(e);
                }
            }
        });
    }

    public   Single<SbxCloudScript>  run()throws  Exception{
        SbxUrlComposer sbxUrlComposer = SbxCloudScriptHelper.getUrlRunCloudScript(getKey(),getParams());
        final Request request = ApiManager.getInstance().sbxUrlComposer2Request(sbxUrlComposer);
        return Single.create(new SingleOnSubscribe<SbxCloudScript>() {
            @Override
            public void subscribe(final SingleEmitter<SbxCloudScript> e) throws Exception {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response= ApiManager.getInstance().getOkHttpClient().newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {
                                update(jsonObject);
                                e.onSuccess(SbxCloudScript.this);
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
        }).onErrorResumeNext(new Function<Throwable, SingleSource<? extends SbxCloudScript>>() {
            @Override
            public SingleSource<? extends SbxCloudScript> apply(Throwable throwable) throws Exception {
                return Single.error(throwable);
            }
        });

    }

    private void update(JSONObject jsonObject) throws Exception{
        JSONObject temp = jsonObject.getJSONObject("cloud_script_run");
        cloudScriptId = temp.getInt("cloud_script_id");
        runId = temp.getInt("id");
        duration = temp.getDouble("duration");
        JSONArray tempoutLog=temp.getJSONArray("out_log");
        JSONArray tempErrorLog=temp.getJSONArray("error_log");
        for (int i=0;i<tempoutLog.length();i++){
            outLog.add(new Log(tempoutLog.getJSONObject(i).optString("date"),
                    tempoutLog.getJSONObject(i).optString("body")));
        }
        for (int i=0;i<tempErrorLog.length();i++){
            errorLog.add(new Log(tempErrorLog.getJSONObject(i).optString("date"),
                    tempErrorLog.getJSONObject(i).optString("body")));
        }
        bodyResult= jsonObject.getJSONObject("response").getJSONObject("body");
    }

    class Log{
        private Date date;
        private String body;

        public Log(String date, String body) {
            try {
                this.date = SbxDataValidator.getDate(date);
            }catch (Exception ex){}
            this.body = body;
        }
    }
}

package com.sbxcloud.android.sbxcloudsdk.net.message;

import android.util.Log;

import com.sbxcloud.android.sbxcloudsdk.message.ChannelHelper;
import com.sbxcloud.android.sbxcloudsdk.net.ApiManager;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxSimpleResponse;
import com.sbxcloud.android.sbxcloudsdk.util.SbxJsonModeler;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

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

public class SbxMessage {

    private Date createdAt;
    private int id;
    private int sender;
    private String sederEmail;
    private String senderLogin;
    private SbxJsonModeler sbxJsonModeler;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public String getSederEmail() {
        return sederEmail;
    }

    public void setSederEmail(String sederEmail) {
        this.sederEmail = sederEmail;
    }

    public String getSenderLogin() {
        return senderLogin;
    }

    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
    }

    public SbxJsonModeler getSbxJsonModeler() {
        return sbxJsonModeler;
    }

    public void setSbxJsonModeler(SbxJsonModeler sbxJsonModeler) {
        this.sbxJsonModeler = sbxJsonModeler;
    }



}

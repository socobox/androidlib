package com.sbxcloud.android.sbxcloudsdk.net.message;

import com.sbxcloud.android.sbxcloudsdk.util.SbxJsonModeler;

import java.util.Date;

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

package com.sbxcloud.android.sbxcloudsdk.exception;

/**
 * Created by lgguzman on 18/02/17.
 */

public class SbxConfigException extends  Exception {

    public SbxConfigException(){
        super("No annotations found");
    }

    public SbxConfigException(String s){
        super(s);
    }

    public SbxConfigException(Exception e){
        super(e);
    }
}

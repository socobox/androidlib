package com.sbxcloud.android.sbxcloudsdk.exception;

/**
 * Created by lgguzman on 19/02/17.
 */

public class SbxAuthException extends  Exception{

    public SbxAuthException(){
        super("No annotations found");
    }

    public SbxAuthException(String s){
        super(s);
    }

    public SbxAuthException(Exception e){
        super(e);
    }
}

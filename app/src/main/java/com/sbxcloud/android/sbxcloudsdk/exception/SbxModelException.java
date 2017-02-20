package com.sbxcloud.android.sbxcloudsdk.exception;

/**
 * Created by lgguzman on 20/02/17.
 */

public class SbxModelException extends  Exception{

    public SbxModelException(){
        super("No annotations found");
    }

    public SbxModelException(String s){
        super(s);
    }

    public SbxModelException(Exception e){
        super(e);
    }
}

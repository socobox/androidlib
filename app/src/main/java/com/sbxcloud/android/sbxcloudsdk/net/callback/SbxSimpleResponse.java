package com.sbxcloud.android.sbxcloudsdk.net.callback;


import okhttp3.Callback;

/**
 * Created by lgguzman on 21/02/17.
 */

public interface SbxSimpleResponse<T> {

     void onError(Exception e);

     void  onSuccess(T tClass);
}

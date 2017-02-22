package com.sbxcloud.android.sbxcloudsdk.net.callback;

import java.util.List;

/**
 * Created by lgguzman on 21/02/17.
 */

public interface SbxArrayResponse <T> {
        void onError(Exception e);

        void  onSuccess(List<T> response);
}

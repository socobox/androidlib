package com.sbxcloud.android.sbxcloudsdk.util;

import org.json.JSONObject;

/**
 * Created by lgguzman on 24/03/17.
 */

public interface SbxJsonModeler {

    void wrapFromJson(JSONObject jsonObject);

    JSONObject toJson();

}

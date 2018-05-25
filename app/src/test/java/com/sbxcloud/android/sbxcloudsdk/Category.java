package com.sbxcloud.android.sbxcloudsdk;

import com.sbxcloud.android.sbxcloudsdk.net.model.SbxModel;
import com.sbxcloud.java.sbxcloudsdk.query.annotation.SbxKey;
import com.sbxcloud.java.sbxcloudsdk.query.annotation.SbxModelName;
import com.sbxcloud.java.sbxcloudsdk.query.annotation.SbxParamField;

/**
 * Created by lgguzman on 20/02/17.
 */

@SbxModelName("Category")
public class Category extends SbxModel {

    @SbxKey
    String key;

    @SbxParamField(name = "description")
    String name;


    public Category(String name) {
        this.name = name;
    }

    public Category(){}
}

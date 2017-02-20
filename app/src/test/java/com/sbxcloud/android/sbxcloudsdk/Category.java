package com.sbxcloud.android.sbxcloudsdk;

import com.sbxcloud.android.sbxcloudsdk.query.SbxKey;
import com.sbxcloud.android.sbxcloudsdk.query.SbxModelName;
import com.sbxcloud.android.sbxcloudsdk.query.SbxParamField;

import java.util.Date;

/**
 * Created by lgguzman on 20/02/17.
 */

@SbxModelName("Category")
public class Category {

    @SbxKey
    String key;

    @SbxParamField(name = "description")
    String name;


    public Category(String name) {
        this.name = name;
    }
}

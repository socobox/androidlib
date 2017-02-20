package com.sbxcloud.android.sbxcloudsdk;

import com.sbxcloud.android.sbxcloudsdk.auth.config.SbxAppKeyField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxAuthToken;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxEmailField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxNameField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxPasswordField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxUsernameField;
import com.sbxcloud.android.sbxcloudsdk.query.SbxKey;

/**
 * Created by lgguzman on 20/02/17.
 */

public class User {

    @SbxNameField
    String name;

    @SbxUsernameField
    String username;

    @SbxEmailField
    String email;

    @SbxPasswordField
    String password;

    @SbxAuthToken
    String token;

    @SbxKey
    String key;


    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

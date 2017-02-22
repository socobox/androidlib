package com.sbxcloud.android.sbxcloudsdk.appTest;

import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxAuthToken;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxEmailField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxNameField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxPasswordField;
import com.sbxcloud.android.sbxcloudsdk.auth.user.SbxUsernameField;
import com.sbxcloud.android.sbxcloudsdk.net.auth.SbxUser;

/**
 * Created by lgguzman on 20/02/17.
 */

public class User extends SbxUser {

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



    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

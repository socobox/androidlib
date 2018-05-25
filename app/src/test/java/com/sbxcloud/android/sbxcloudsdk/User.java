package com.sbxcloud.android.sbxcloudsdk;

import com.sbxcloud.java.sbxcloudsdk.auth.user.SbxAuthToken;
import com.sbxcloud.java.sbxcloudsdk.auth.user.SbxEmailField;
import com.sbxcloud.java.sbxcloudsdk.auth.user.SbxNameField;
import com.sbxcloud.java.sbxcloudsdk.auth.user.SbxPasswordField;
import com.sbxcloud.android.sbxcloudsdk.net.auth.SbxUser;
import com.sbxcloud.java.sbxcloudsdk.auth.user.SbxUsernameField;
import com.sbxcloud.java.sbxcloudsdk.query.annotation.SbxKey;

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

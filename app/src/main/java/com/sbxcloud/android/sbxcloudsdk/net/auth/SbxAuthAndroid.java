package com.sbxcloud.android.sbxcloudsdk.net.auth;

import android.content.Context;
import com.sbxcloud.java.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.java.sbxcloudsdk.exception.SbxConfigException;

public class SbxAuthAndroid extends SbxAuth {


    private SbxAuthPersistenceAndroidStrategy sbxAuthPersistenceAndroidStrategy;


    public static Context getContext() throws SbxConfigException {
        if (getDefaultSbxAuth().getSbxAuthPersistenceStrategy()!=null)
        return ((SbxAuthPersistenceAndroidStrategy)getDefaultSbxAuth().getSbxAuthPersistenceStrategy()).getContext();
        return null;
    }


    /**
     * initialize the data for comunicate on sbxcloud.com
     * @param domain the id of the domain on sbxcloud.com
     * @param appKey the app key on sbxcloud.com
     */
    public static void initializeIfIsNecessary(Context context, int domain, String appKey) {
        try {
            getDefaultSbxAuth();
        }catch (Exception e){
            SbxAuthPersistenceAndroidStrategy sbxAuthPersistenceAndroidStrategy = new SbxAuthPersistenceAndroidStrategy(context);
            initializeIfIsNecessary(domain, appKey, sbxAuthPersistenceAndroidStrategy);
        }
    }


}
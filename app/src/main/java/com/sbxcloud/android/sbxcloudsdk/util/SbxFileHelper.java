package com.sbxcloud.android.sbxcloudsdk.util;

import com.sbxcloud.android.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.android.sbxcloudsdk.query.SbxQueryBuilder;

import java.io.File;

/**
 * Created by lgguzman on 25/03/17.
 */

public class SbxFileHelper {

    public static SbxUrlComposer getUrlUploadFile(File file, String name, String folderKey) throws  Exception{
        String appKey = SbxAuth.getDefaultSbxAuth().getAppKey();
        String token = SbxAuth.getDefaultSbxAuth().getToken();
        SbxUrlComposer sbxUrlComposer = new SbxUrlComposer(
                UrlHelper.URL_UPLOAD_FILE
                , UrlHelper.POST
        );
        return sbxUrlComposer
                .addHeader(UrlHelper.HEADER_KEY_APP_KEY, appKey)
//                .addHeader(UrlHelper.HEADER_KEY_ENCODING, UrlHelper.HEADER_GZIP)
//                .addHeader(UrlHelper.HEADER_KEY_CONTENT_TYPE, UrlHelper.HEADER_JSON)
                .addHeader(UrlHelper.HEADER_KEY_AUTORIZATION, UrlHelper.HEADER_BEARER+token)
                .addMultipartData(file,name,folderKey);
    }
}

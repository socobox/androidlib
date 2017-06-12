package com.sbxcloud.android.sbxcloudsdk.cloudscript;

import com.sbxcloud.android.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.android.sbxcloudsdk.query.SbxQueryBuilder;
import com.sbxcloud.android.sbxcloudsdk.util.SbxUrlComposer;
import com.sbxcloud.android.sbxcloudsdk.util.UrlHelper;

import org.json.JSONObject;

/**
 * Created by lgguzman on 12/06/17.
 */

public class SbxCloudScriptHelper {

    /**
     *
     * @param key llave del cloudScript
     * @param params parametros del CloudScript
     * @return
     * @throws Exception
     */
    public static SbxUrlComposer getUrlRunCloudScript(String key, JSONObject params)throws Exception {

       // int domain = SbxAuth.getDefaultSbxAuth().getDomain();
        String appKey = SbxAuth.getDefaultSbxAuth().getAppKey();
        String token = SbxAuth.getDefaultSbxAuth().getToken();
        SbxUrlComposer sbxUrlComposer = new SbxUrlComposer( UrlHelper.CLOUDSCRIPT_RUN, UrlHelper.POST);
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("key",key);
            jsonObject.put("params",params);
        }catch (Exception ex){}
        return sbxUrlComposer
                .addHeader(UrlHelper.HEADER_KEY_APP_KEY, appKey)
                //       .addHeader(UrlHelper.HEADER_KEY_ENCODING, UrlHelper.HEADER_GZIP)
                .addHeader(UrlHelper.HEADER_KEY_CONTENT_TYPE, UrlHelper.HEADER_JSON)
                .addHeader(UrlHelper.HEADER_KEY_AUTORIZATION, UrlHelper.HEADER_BEARER+token)
                .addBody(jsonObject);
    }

}

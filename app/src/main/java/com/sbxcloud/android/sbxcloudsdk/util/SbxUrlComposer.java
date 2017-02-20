package com.sbxcloud.android.sbxcloudsdk.util;

import org.json.JSONObject;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by lgguzman on 18/02/17.
 */

public class SbxUrlComposer {
    private String url;
    private String type;
    private HashMap<String,String> header;
    private JSONObject body;

    /**
     *
     * @return if the request is a PUT
     */
    public boolean isPOST(){
        return type.equals(UrlHelper.POST);
    }

    /**
     *
     * @return if the request is a GET
     */
    public boolean isGET(){
        return type.equals(UrlHelper.GET);
    }

    /**
     *
     * @return if the request is a PUT
     */
    public boolean isPUT(){
        return type.equals(UrlHelper.PUT);
    }

    /**
     *
     * @return the type of request of this object, PUT GET or POST
     */
    public String getType(){
        return type;
    }

    /**
     *
     * @param url
     * @param type
     */
    public SbxUrlComposer(String url, String type){
        this.url= UrlHelper.URL_BASE+ url;
        this.type=type;
        header= new HashMap<>();
        body= new JSONObject();
    }

    /**
     *
     * @param key
     * @param value
     */
    public SbxUrlComposer setUrlParam(String key, String value){

        if(!this.url.contains("?"))
            this.url=this.url+"?"+key+"="+URLEncoder.encode(value);
        else
            this.url=this.url+"&"+key+"="+URLEncoder.encode(value);

        return this;
    }

    /**
     *
     * @param jsonObject
     */
    public SbxUrlComposer addBody(JSONObject jsonObject){
        body = jsonObject;
        return this;
    }


    /**
     *
     * @param key
     * @param value
     */
    public SbxUrlComposer addHeader(String key, String value){
       header.put(key,value);
        return this;
    }


    public HashMap<String, String> getHeader() {
        return header;
    }

    public String getHeaderString(){
        StringBuilder stringBuilder=new StringBuilder("");
        for (String key:header.keySet()){
            stringBuilder.append(key+": "+header.get(key)+":\n");
        }
        return stringBuilder.toString();
    }

    public JSONObject getBody() {
        return body;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString(){
        return "\n"+getType()+"\n"+getUrl()+"\nHEADER\n"+getHeaderString()+"\nBODY\n"+((getBody().toString()==null)?"":getBody().toString())+"\n";
    }
}

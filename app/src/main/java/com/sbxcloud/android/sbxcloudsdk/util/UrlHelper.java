package com.sbxcloud.android.sbxcloudsdk.util;

/**
 * Created by lgguzman on 18/02/17.
 */

public interface UrlHelper {

    public static final String URL_BASE="https://sbxcloud.com/";
    public static final String URL_FIND="api/data/v1/row/find ";
    public static final String URL_LOGIN="api/user/v1/login";
    public static final String URL_SIGN_UP="api/user/v1/register";
    public static final String URL_INSERT="api/data/v1/row";
    public static final String URL_UPDATE="api/data/v1/row/update";
    public static final String URL_UPLOAD_FILE="api/content/v1/upload";
    public static final String PASSWORD_REQUEST="api/user/v1/password/request/";
    public static final String PUSH = "api/push/v1/send";
    public static final String URL_DELETE="api/data/v1/row/delete";
    public static final String HEADER_KEY_APP_KEY="App-Key";
    public static final String HEADER_KEY_ENCODING="Accept-Encoding";
    public static final String HEADER_KEY_CONTENT_TYPE="Content-Type";
    public static final String HEADER_KEY_AUTORIZATION="Authorization";
    public static final String MESSAGE_CHANNEL = "api/message/v1/channel";
    public static final String MESSAGE_CHANNEL_MEMBER = "api/message/v1/channel/member";
    public static final String MESSAGE_LIST = "api/message/v1/list";
    public static final String MESSAGE_SEND = "api/message/v1/send";
    public static final String CLOUDSCRIPT_RUN = "api/cloudscript/v1/run";
    public static final String DOMAIN ="api/domain/v1/list/app";
  //  public static final String HEADER_GZIP="gzip";
    public static final String HEADER_JSON="application/json";
    public static final String HEADER_BEARER="Bearer ";
    public static final String POST="POST";
    public static final String GET="GET";
    public static final String PUT="PUT";

}

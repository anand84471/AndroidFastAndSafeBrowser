package com.otpreader.apiclient.HttpRequests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HttpRequestForReportJsError {
    @SerializedName("url")
    @Expose
    public String strUrl;
    @SerializedName("exception")
    @Expose
    public String strJsExceptionMessage;
    @SerializedName("js_code_applied")
    @Expose
    public String strExecutedJsCode;
    @SerializedName("html")
    @Expose
    public String strHtmlOfCurrentUrl;
    @SerializedName("js_code_type_id")
    @Expose
    public  int iJsCodeTypeId;
    @SerializedName("client_type_id")
    @Expose
    public int iClientTypeId;
    @SerializedName("client_version")
    @Expose
    public String strClientVersion;
}

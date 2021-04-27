package com.otpreader.apiclient.HttpResponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefaultApiResponse {
    @SerializedName("response_code")
    @Expose
    private Integer responseCode;

    @SerializedName("response_message")
    @Expose
    private String responseMessage;
    public int getResponseCode()
    {
        return responseCode;
    }
    public String getResponseMessage()
    {
        return responseMessage;
    }
}

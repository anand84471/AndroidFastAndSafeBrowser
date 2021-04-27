package com.otpreader.apiclient.HttpResponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.otpreader.apiclient.HttpResponses.BankUrlDetailsHttpResponse;

import java.util.List;

public class BankUrlDetailApiResponse {
    @SerializedName("url_data")
    @Expose
    private List<BankUrlDetailsHttpResponse> lsBankUrlDetails;
    @SerializedName("response_code")
    @Expose
    private Integer iResponseCode;

    @SerializedName("response_message")
    @Expose
    private String strResponseMessage;
    public List<BankUrlDetailsHttpResponse> getUrlDetails()
    {
        return lsBankUrlDetails;
    }
}

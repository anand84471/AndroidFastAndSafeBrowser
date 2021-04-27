package com.otpreader.apiclient.HttpRequests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientTypeHttpRequest {
    @SerializedName("client_type_id")
    @Expose
    public int iClientType;
    @SerializedName("is_required_net_banking_details")
    @Expose
    public boolean IsRequiredNetBankingDetails;

}

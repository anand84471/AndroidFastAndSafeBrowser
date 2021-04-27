package com.otpreader.apiclient.HttpRequests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentStatusChangeRequest {
    @SerializedName("merchant_id")
    @Expose
    public int iMerchantId;
    @SerializedName("transaction_status")
    @Expose
    public boolean bTransactionStatus;
    @SerializedName("transaction_id")
    @Expose
    public String strTransactionId;
}

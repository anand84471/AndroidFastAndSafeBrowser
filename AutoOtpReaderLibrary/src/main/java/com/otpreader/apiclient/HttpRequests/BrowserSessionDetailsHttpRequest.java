package com.otpreader.apiclient.HttpRequests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrowserSessionDetailsHttpRequest {
    @SerializedName("merchant_id")
    @Expose
    public int iMerchantId;

    @SerializedName("transaction_id")
    @Expose
    public String strTransactionId;

    @SerializedName("client_type_id")
    @Expose
    public String strClientId;
    @SerializedName("customer_id")
    @Expose
    public String strCustomerId;
    @SerializedName("customer_email")
    @Expose
    public String strCustomerEmail;
    @SerializedName("customer_phone")
    @Expose
    public String strCustomerPhoneNo;

    @SerializedName("payment_remarks")
    @Expose
    public String strPaymentRemarks;

    @SerializedName("payment_start_url")
    @Expose
    public String strPaymentStartUrl;

    @SerializedName("payment_return_urls")
    @Expose
    public String strPaymentReturnUrls;

    @SerializedName("post_data")
    @Expose
    public String strFormPostData;

    @SerializedName("custom_parameters")
    @Expose
    public String strCustomParameters;

    @SerializedName("is_payment_successful")
    @Expose
    public Boolean IsPaymentSuccessful;

    @SerializedName("last_visited_url")
    @Expose
    public String strLastVisitedUrl;

    @SerializedName("is_back_pressed")
    @Expose

    public Boolean IsBackPressed;

    @SerializedName("is_transaction_completed")
    @Expose

    public Boolean IsTransactionCompleted;

    @SerializedName("is_otp_detected")
    @Expose

    public Boolean IsOtpDetected;

    @SerializedName("have_sms_permission")
    @Expose

    public Boolean HaveSmsPermission;

    @SerializedName("is_fetched_url_details")
    @Expose

    public Boolean IsFetchedOtpDetails;

    @SerializedName("is_otp_approved")
    @Expose

    public Boolean IsOtpApproved;

    @SerializedName("error_code")
    @Expose
    public Integer iErrorCode;

    @SerializedName("error_message")
    @Expose
    public String strErrorMessage;

    @SerializedName("browser_session_starttime")
    @Expose
    public String dtBrowserStartTime;

    @SerializedName("browser_session_finish_time")
    @Expose
    public String dtBrowserCloseTime;
    @SerializedName("theme_id")
    @Expose
    public int iThemeId;
    @SerializedName("language_id")
    @Expose
    public int iLanguageId;
    @SerializedName("order_id")
    @Expose
    public String strOrderId;


    public void setiMerchantId(int iMerchantId) {
        this.iMerchantId = iMerchantId;
    }

    public void setStrTransactionId(String strTransactionId) {
        this.strTransactionId = strTransactionId;
    }
    public void setStrClientId(String strClientId) {
        this.strClientId = strClientId;
    }

    public void setStrCustomerEmail(String strCustomerEmail) {
        this.strCustomerEmail = strCustomerEmail;
    }
    public void setStrCustomerPhoneNo(String strCustomerPhoneNo) {
        this.strCustomerPhoneNo = strCustomerPhoneNo;
    }
    public void setStrPaymentRemarks(String strPaymentRemarks) {
        this.strPaymentRemarks = strPaymentRemarks;
    }
    public void setStrPaymentStartUrl(String strPaymentStartUrl) {
        this.strPaymentStartUrl = strPaymentStartUrl;
    }
    public void setStrPaymentReturnUrls(String strPaymentReturnUrls) {
        this.strPaymentReturnUrls = strPaymentReturnUrls;
    }
    public void setStrFormPostData(String strFormPostData) {
        this.strFormPostData = strFormPostData;
    }
    public void setStrCustomParameters(String strCustomParameters) {
        this.strCustomParameters = strCustomParameters;
    }
    public void setPaymentSuccessful(Boolean paymentSuccessful) {
        IsPaymentSuccessful = paymentSuccessful;
    }
    public void setStrLastVisitedUrl(String strLastVisitedUrl) {
        this.strLastVisitedUrl = strLastVisitedUrl;
    }
    public void setIsBackPressed(boolean isBackPressed) {
        IsBackPressed = isBackPressed;
    }
    public void setTransactionCompleted(Boolean transactionCompleted) {
        IsTransactionCompleted = transactionCompleted;
    }
    public void setOtpDetected(Boolean otpDetected) {
        IsOtpDetected = otpDetected;
    }
    public void setHaveSmsPermission(Boolean haveSmsPermission) {
        HaveSmsPermission = haveSmsPermission;
    }
    public void setIsFetchedUrlDetails(Boolean fetchedOtpDetails) {
        IsFetchedOtpDetails = fetchedOtpDetails;
    }
    public void setOtpApproved(Boolean otpApproved) {
        IsOtpApproved = otpApproved;
    }
    public void setiErrorCode(Integer iErrorCode) {
        this.iErrorCode = iErrorCode;
    }
    public void setStrErrorMessage(String strErrorMessage) {
        this.strErrorMessage = strErrorMessage;
    }
    public void setDtBrowserStartTime(String dtBrowserStartTime) {
        this.dtBrowserStartTime = dtBrowserStartTime;
    }
    public void setDtBrowserCloseTime(String dtBrowserCloseTime) {
        this.dtBrowserCloseTime = dtBrowserCloseTime;
    }
    public void setThemeId(int themeId)
    {
        this.iThemeId=themeId;
    }
    public void setLanguageId(int languageId)
    {
        this.iLanguageId=languageId;
    }
    public void setOrderId(String OrderId)
    {
        this.strOrderId=OrderId;
    }
}

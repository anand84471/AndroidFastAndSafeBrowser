package com.otpreader.PinePGDTOs;

import android.os.Bundle;
import com.otpreader.Constants.MerchantConfigurationConstants;
import com.otpreader.apiclient.HttpRequests.BrowserSessionDetailsHttpRequest;

import java.sql.Timestamp;
import java.util.Date;

public class BrowserSessionDTO {
    public int iMerchantId;
    public String strTransactionId;
    public String strAmount;
    public String strOrderId;
    public boolean IsTransactionCompleted;
    public String strLastVisitedUrl;
    public String strCustomerId;
    public String strCustomerPhoneNo;
    public String strCustomerEmail;
    public String strCustomParameters;
    public boolean IsBackPressed;
    public boolean IsOtpDetected;
    public boolean IsOtpReaderStarted;
    public boolean HaveSmsPermission;
    public boolean IsFetchedUrlDetails;
    public boolean IsOtpApproved;
    private boolean NetworkStatus;
    public int iErrorCode;
    public String strErrorMessage;
    public Date browserSessionStatTime;
    public Date browserSessionFinishTime;
    private Bundle responseBundle;
    public String strPaymentStartUrl;
    public String strPaymentRemarks;
    public int iEnvironment;
    public int iThemeId;
    public int iLanguageId;
    public String strReturnUrls;
    public boolean  shouldStartPinePGLoader;
    public BrowserSessionDTO()
    {
        java.util.Date date = new Date();
        this.browserSessionStatTime=new java.sql.Timestamp(date.getTime());
    }
    private BrowserSessionDetailsHttpRequest objBrowserSessionDetailsHttpRequest;
    public Bundle getResponseBundle()
    {
        java.util.Date date = new Date();
        this.browserSessionFinishTime=new java.sql.Timestamp(date.getTime());
        generateResponseData();
        return this.responseBundle;
    }
    private void generateResponseData()
    {
        try {
            this.responseBundle = new Bundle();
            this.responseBundle.putString(MerchantConfigurationConstants.PAYMENT_START_TIME_KEY, this.browserSessionStatTime.toString());
            this.responseBundle.putString(MerchantConfigurationConstants.PAYMENT_COMPLETION_TIME_KEY, browserSessionFinishTime.toString());
            this.responseBundle.putString(MerchantConfigurationConstants.LAST_VISITED_URL_KEY, this.strLastVisitedUrl);
            this.responseBundle.putBoolean(MerchantConfigurationConstants.IS_OTP_APPROVED_KEY, this.IsOtpApproved);
            this.responseBundle.putBoolean(MerchantConfigurationConstants.HAVE_SMS_PERMISSION_KEY, this.HaveSmsPermission);
            this.responseBundle.putBoolean(MerchantConfigurationConstants.IS_OTP_DETECTED_KEY, this.IsOtpDetected);
            this.responseBundle.putString(MerchantConfigurationConstants.TRANSACTION_START_URL_KEY, this.strLastVisitedUrl);
            this.responseBundle.putBoolean(MerchantConfigurationConstants.IS_PAYMENT_SUCCESSFUL_KEY, this.IsTransactionCompleted);
            this.responseBundle.putBoolean(MerchantConfigurationConstants.IS_BACK_PRESSED_KEY, this.IsBackPressed);
            this.responseBundle.putString(MerchantConfigurationConstants.MERCHANT_ID_KEY,this.iMerchantId+"");
            this.responseBundle.putString(MerchantConfigurationConstants.TRANSACTION_ID_KEY,this.strTransactionId);
            this.responseBundle.putString(MerchantConfigurationConstants.AMOUNT_KEY,this.strAmount);
            this.responseBundle.putInt(MerchantConfigurationConstants.ERROR_CODE_KEY,this.iErrorCode);
            this.responseBundle.putString(MerchantConfigurationConstants.ERROR_MESSAGE_KEY,this.strErrorMessage);
        }
        catch (Exception ex)
        {

        }
    }

    public void setTransactionCompleted(boolean transactionCompleted) {
        IsTransactionCompleted = transactionCompleted;
    }

    public void setStrLastVisitedUrl(String strLastVisitedUrl) {
        this.strLastVisitedUrl = strLastVisitedUrl;
    }

    public void setBackPressed(boolean backPressed) {
        IsBackPressed = backPressed;
    }

    public void setOtpDetected(boolean otpDetected) {
        IsOtpDetected = otpDetected;
    }

    public void setHaveSmsPermission(boolean haveSmsPermission) {
        HaveSmsPermission = haveSmsPermission;
    }

    public void setOtpApproved(boolean otpApproved) {
        IsOtpApproved = otpApproved;
    }

    public void setNetworkStatus(boolean networkStatus) {
        NetworkStatus = networkStatus;
    }

    public void setiErrorCode(int iErrorCode) {
        this.iErrorCode = iErrorCode;
    }

    public void setStrErrorMessage(String strErrorMessage) {
        this.strErrorMessage = strErrorMessage;
    }

    public void setBrowserSessionStatTime(Timestamp browserSessionStatTime) {
        this.browserSessionStatTime = browserSessionStatTime;
    }

    public void setBrowserSessionFinishTime(Timestamp browserSessionFinishTime) {
        this.browserSessionFinishTime = browserSessionFinishTime;
    }

    public void setResponseBundle(Bundle responseBundle) {
        this.responseBundle = responseBundle;
    }
    private BrowserSessionDetailsHttpRequest castBrowserSessionToBrowserSessionDetails()
    {
        objBrowserSessionDetailsHttpRequest =new BrowserSessionDetailsHttpRequest();
        return null;
    }

}

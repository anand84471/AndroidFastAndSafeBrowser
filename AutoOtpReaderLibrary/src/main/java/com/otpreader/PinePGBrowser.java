package com.otpreader;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.otpreader.AutoOtpHandlers.WrraperForOtp;
import com.otpreader.ServiceManagers.MerchantPaymentRequestValidation;
import com.otpreader.activity.PinePGBrowserActivity;
import com.otpreader.interfaces.IPinePGResponseCallback;

import java.util.List;

public class PinePGBrowser {
    private static volatile PinePGBrowser objPinePGBrowser = null;
    private Context context;
    private Bundle bundleConfigData;
    private Bundle bundlePaymentData;
    private IPinePGResponseCallback iPinePGResponseCallback;
    private String strConnectionMessage;
    private boolean bConnectionStatus;
    private MerchantPaymentRequestValidation merchantPaymentRequestValidation;
    public static void openConnection(Context context, Bundle bundleJsonData) {
        try {
            if (objPinePGBrowser == null) {
                if (objPinePGBrowser == null) {
                    objPinePGBrowser = new PinePGBrowser();
                }
            }
            objPinePGBrowser.setContext(context);
            objPinePGBrowser.setConfig(bundleJsonData);
        } catch (Exception ex) {

        }
    }
    private static void setPreviouslyFetchedOtpEmpty()
    {
        if(WrraperForOtp.getInstance()!=null) {
            WrraperForOtp.getInstance().setOTP("");
        }
    }
    private void setContext(Context context) {
        this.context = context;
    }

    private void setConfig(Bundle configData) {
        this.bundleConfigData = configData;
    }

    public static void makeServiceCall(IPinePGResponseCallback merchantCallBack, Bundle PaymentData) {
        try {
            objPinePGBrowser.merchantPaymentRequestValidation =new MerchantPaymentRequestValidation(PaymentData);
            if(objPinePGBrowser.merchantPaymentRequestValidation.getValidationStatus())
            {
                objPinePGBrowser.setCallBack(merchantCallBack);
                objPinePGBrowser.setPaymentData(PaymentData);
                objPinePGBrowser.startBrowser();
            }
            else {
                objPinePGBrowser.setCallBack(merchantCallBack);
                objPinePGBrowser.setPaymentData(PaymentData);
                objPinePGBrowser.startBrowser();
            }
        } catch (Exception ex) {

        }
    }

    public static void terminateService() {
        objPinePGBrowser = null;
        setPreviouslyFetchedOtpEmpty();
    }
    private void startBrowser() {
        try {
            Intent intentPinePGBrowserActivity = new Intent(context, PinePGBrowserActivity.class);
            intentPinePGBrowserActivity.putExtras(objPinePGBrowser.bundleConfigData);
            context.startActivity(intentPinePGBrowserActivity);
        } catch (Exception ex) {

        }
    }
    private void setCallBack(IPinePGResponseCallback callBack) {
        objPinePGBrowser.iPinePGResponseCallback=callBack;
    }

    private void setPaymentData(Bundle paymentData)
    {
        objPinePGBrowser.bundlePaymentData=paymentData;
    }
    public static Bundle getPaymentData()
    {
        return objPinePGBrowser.bundlePaymentData;
    }
    public static IPinePGResponseCallback getPinePGResponseCallback()
    {
        return objPinePGBrowser.iPinePGResponseCallback;
    }

}
package com.otpreader;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.otpreader.AutoOtpHandlers.WrraperForOtp;
import com.otpreader.ServiceManagers.MerchantConfigurationDataValidator;
import com.otpreader.ServiceManagers.MerchantPaymentRequestValidation;
import com.otpreader.activity.PinePGBrowserActivity;
import com.otpreader.apiclient.HttpRequests.PaymentStatusChangeRequest;
import com.otpreader.apiclient.HttpResponses.DefaultApiResponse;
import com.otpreader.apiclient.MainApiClient;
import com.otpreader.interfaces.IPinePGResponseCallback;

import java.lang.reflect.Array;
import java.util.Arrays;

import retrofit2.http.PUT;

public class BrowserCheckout {
    private static volatile BrowserCheckout objBrowserCheckout = null;
    private Context context;
    private Bundle bundleConfigData;
    private Bundle bundlePaymentData;
    private IPinePGResponseCallback iPinePGResponseCallback;
    private String strConnectionMessage="fail";
    private boolean bConnectionStatus=false;
    private int iMerchantId;
    private int iEnvironment;
    public static void openConnection(Context context, Bundle bundleJsonData) {
        try {
            if (objBrowserCheckout == null) {
                if (objBrowserCheckout == null) {
                    objBrowserCheckout = new BrowserCheckout();
                }
            }
            MerchantConfigurationDataValidator merchantConfigurationDataValidator=new MerchantConfigurationDataValidator(bundleJsonData);
            if(merchantConfigurationDataValidator.getValidationStatus())
            {
                objBrowserCheckout.setContext(context);
                objBrowserCheckout.setConfig(bundleJsonData);
                objBrowserCheckout.strConnectionMessage="success";
                objBrowserCheckout.bConnectionStatus=true;
                objBrowserCheckout.iEnvironment=merchantConfigurationDataValidator.getiEnvironment();
            }
            else
            {
                objBrowserCheckout.strConnectionMessage=merchantConfigurationDataValidator.getValidationMessage();
                Toast.makeText(objBrowserCheckout.context, merchantConfigurationDataValidator.getValidationMessage(),Toast.LENGTH_LONG).show();
            }

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
        objBrowserCheckout.context = context;
    }

    private void setConfig(Bundle configData) {
        objBrowserCheckout.bundleConfigData = configData;
    }

    public static void makeServiceCall(IPinePGResponseCallback merchantCallBack, Bundle PaymentData) {
        try {
            MerchantPaymentRequestValidation merchantPaymentRequestValidation=new MerchantPaymentRequestValidation(PaymentData);
            if(merchantPaymentRequestValidation.getValidationStatus())
            {
                objBrowserCheckout.setCallBack(merchantCallBack);
                objBrowserCheckout.setPaymentData(PaymentData);
                objBrowserCheckout.startBrowser();
                objBrowserCheckout.strConnectionMessage="success";
                objBrowserCheckout.bConnectionStatus=true;
                objBrowserCheckout.iMerchantId=merchantPaymentRequestValidation.getMerchantId();
            }
            else {
                objBrowserCheckout.bConnectionStatus=false;
                objBrowserCheckout.strConnectionMessage=merchantPaymentRequestValidation.getValidationMessage();
                Toast.makeText(objBrowserCheckout.context.getApplicationContext(), merchantPaymentRequestValidation.getValidationMessage(),Toast.LENGTH_LONG).show();
            }
        } catch (Exception ignored) {

        }
    }

    public static void terminateService() {
        objBrowserCheckout = null;
        setPreviouslyFetchedOtpEmpty();
    }
    private void startBrowser() {
        try {
            Intent intentPinePGBrowserActivity = new Intent(context, PinePGBrowserActivity.class);
            intentPinePGBrowserActivity.putExtras(objBrowserCheckout.bundleConfigData);
            context.startActivity(intentPinePGBrowserActivity);
        } catch (Exception ex) {

        }
    }
    private void setCallBack(IPinePGResponseCallback callBack) {
        objBrowserCheckout.iPinePGResponseCallback=callBack;
    }
    private void setPaymentData(Bundle paymentData)
    {
        objBrowserCheckout.bundlePaymentData=paymentData;
    }
    public static Bundle getPaymentData()
    {
        return objBrowserCheckout.bundlePaymentData;
    }
    public static IPinePGResponseCallback getPinePGResponseCallback()
    {
        return objBrowserCheckout.iPinePGResponseCallback;
    }

    public static String getConnectionMessage()
    {
        return objBrowserCheckout.strConnectionMessage;
    }
    public static boolean getConnectionStatus()
    {
        return objBrowserCheckout.bConnectionStatus;
    }
    public static void reportTransactionStatus(String transactionId,boolean transactionStatus)
    {
        try {
            MainApiClient mainApiClient = new MainApiClient(objBrowserCheckout.iEnvironment);
            PaymentStatusChangeRequest paymentStatusChangeRequest = new PaymentStatusChangeRequest();
            paymentStatusChangeRequest.iMerchantId = objBrowserCheckout.iMerchantId;
            paymentStatusChangeRequest.bTransactionStatus = transactionStatus;
            paymentStatusChangeRequest.strTransactionId = transactionId;
            mainApiClient.reportTransactionStatus(paymentStatusChangeRequest);
        }
        catch (Exception Ex)
        {

        }
    }

}
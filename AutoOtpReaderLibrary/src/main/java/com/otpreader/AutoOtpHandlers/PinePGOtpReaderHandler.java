package com.otpreader.AutoOtpHandlers;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.otpreader.PinePGDTOs.BrowserSessionDTO;
import com.otpreader.apiclient.interfaces.ApiServiceInterfaceImplementation;
import com.otpreader.interfaces.PinePGJavaScriptInterface;

import static android.content.ContentValues.TAG;
public class PinePGOtpReaderHandler {
    private PinePGOtpReader objPinePGOtpReader = null;
    private boolean smsPermission = false;
    private ApiServiceInterfaceImplementation handleBankUrlResponse=null;
    private  WebView objWebView=null;
    private boolean isOtpReaderStarted=false;
    private BrowserSessionDTO objBrowserSessionDTO;
    private boolean shouldHaltAutoOtpReader=false;
    public PinePGOtpReaderHandler(View bottomSheet, LinearLayout dynamicLayout, WebView webView, int environment, boolean SmsPermissionFalg,
                                  BrowserSessionDTO objBrowserSessionDTO) {
        try {
            this.objWebView=webView;
            handleBankUrlResponse=new ApiServiceInterfaceImplementation(environment);
            smsPermission = SmsPermissionFalg;
            if (smsPermission) {
                objPinePGOtpReader = new PinePGOtpReaderWithSMSPermission();
            } else {
                objPinePGOtpReader = new PinePGOtpReaderWithoutSMSPermission();
            }
            objPinePGOtpReader.initializeBottomSheets(bottomSheet, dynamicLayout, webView);
            objPinePGOtpReader.setBrowserSessionDTO(objBrowserSessionDTO);
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred while loading page...onPageFinished", e);
        }
    }
    public void setJavaScriptInterface(PinePGJavaScriptInterface objPinePGJavaScriptInterface)
    {
        objPinePGOtpReader.setJavaScriptInterface(objPinePGJavaScriptInterface);
    }
    public boolean getHaveSmsPermission()
    {
        return this.smsPermission;
    }
    public boolean isOtpDetected()
    {
        return this.objPinePGOtpReader.getIsOtpDetected();
    }
    public boolean isOtpReaderStarted()
    {
        return this.isOtpReaderStarted;
    }
    public boolean isOtpSubmitted()
    {
        return this.objPinePGOtpReader.getIsOtpSubmitted();
    }
    public void initializeNetBankingLayouts()
    {
        if(objPinePGOtpReader!=null)
        {
            objPinePGOtpReader.initializeNetBankingLayouts();
        }
    }
    public void submitNetBankingPage(String jsCode,String jsCodeToGetUserIdFromPage,boolean ShouldStartOtpReader)
    {
        if(!isOtpReaderStarted)
        {
            objPinePGOtpReader.triggerNetBankingSubmission(jsCode,jsCodeToGetUserIdFromPage,ShouldStartOtpReader);
        }
    }
    public void configureOtpReader(int otpLength,String otpSubmissionJsCode,boolean shouldHaltAutoOtpReader,boolean
            ShouldUseGenericOtpSubmissionJsCode)
    {
        objPinePGOtpReader.setOtpLength(otpLength);
        objPinePGOtpReader.setJsCodeToSubmitOtp(otpSubmissionJsCode);
        objPinePGOtpReader.setShouldHaltAutoOtpReader(shouldHaltAutoOtpReader);
        objPinePGOtpReader.setShouldUseGenericAutoOtpSubmissionCode(ShouldUseGenericOtpSubmissionJsCode);
    }
    public void setBrowserSessionDTO(BrowserSessionDTO objBrowserSessionDTO)
    {
        this.objBrowserSessionDTO=objBrowserSessionDTO;
    }
    public boolean startService(int otpLength,String otpSubmissionJsCode,boolean ShouldHaltAutoOtpReader,
                                boolean ShouldUseGenericOtpSubmissionJsCode)
    {
        boolean result=false;
        try
        {
            if(isOtpReaderStarted){
                return true;
            }
            if(objPinePGOtpReader!=null) {
                configureOtpReader(otpLength,otpSubmissionJsCode,ShouldHaltAutoOtpReader,
                        ShouldUseGenericOtpSubmissionJsCode);

                objPinePGOtpReader.triggerOtpReader();
                result=true;
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, "An exception occurred while starting otp reader service", ex);
        }
        return  result;
    }
    public void stopService() {
        if(objPinePGOtpReader!=null) {
            objPinePGOtpReader.removeAllViews();
            objPinePGOtpReader = null;
        }
    }
}

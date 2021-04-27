package com.otpreader.pgwebview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.otpreader.Constants.MerchantConfigurationConstants;
import com.otpreader.Constants.PinePGConstants;
import com.otpreader.PinePGPaymentCallback;
import com.otpreader.apiclient.interfaces.ApiServiceInterfaceImplementation;
import com.otpreader.ServiceManagers.OtpAndNetBankingServiceManager;
import com.otpreader.PinePGDTOs.BrowserSessionDTO;
import com.otpreader.interfaces.PinePGJavaScriptInterface;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.otpreader.Constants.PinePGConstants.INVOICE_PAYMENT_PAGE_SUBMISSION_JS_CODE;
import static com.otpreader.Constants.PinePGConstants.PINE_PG_CODE_EXCEPTION_OCCURRED;

class PGWebViewClient extends WebViewClient {
    private PinePGPaymentCallback objPinePGPaymentCallBack=null;
    private final String TAG="PGWebViewClient";
    private Context ctx;
    private OtpAndNetBankingServiceManager objOtpAndNetBankingServiceManager;
    private List<String> lsMerchantReturnUrls;
    private Bundle bundleConfigurationData;
    private ViewGroup webViewContainer;
    private BrowserSessionDTO objBrowserSessionDTO;
    private ApiServiceInterfaceImplementation objApiServiceInterfaceImplementation;
    private  int environment;
    PinePGJavaScriptInterface objPinePGJavaScriptInterface;
    public PGWebViewClient(Context context, ViewGroup webViewContainer, Bundle configData, PinePGPaymentCallback objMerchantCallBack,PinePGJavaScriptInterface objPinePGJavaScriptInterface) {
        this.ctx=context;
        this.bundleConfigurationData=configData;
        this.webViewContainer=webViewContainer;
        setMerchantReturnUrls();
        setEnvironment();
        if(objMerchantCallBack!=null) {
            this.objPinePGPaymentCallBack=objMerchantCallBack;
            this.objBrowserSessionDTO = objMerchantCallBack.getBrowserSessionDTO();
        }
        this.objPinePGJavaScriptInterface=objPinePGJavaScriptInterface;
    }
    @Override public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        try {
            errorHandler(errorCode);

        }
        catch (Exception e) { }
    }
    @Override public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        //objPinePGPaymentCallBack.onErrorOccurred(-1, errorResponse.getReasonPhrase());
    }
    @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
    @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        try {
            System.out.println(url);
            objBrowserSessionDTO.strLastVisitedUrl=url;
            objPinePGPaymentCallBack.onPageStarted(view,url);

        } catch (Exception ex) {
            objPinePGPaymentCallBack.onErrorOccurred(-1, PinePGConstants.EXCEPTON_OCCUR,null);
        }
    }
    @Override public void onPageCommitVisible(WebView view, String url) {
        super.onPageCommitVisible(view, url);
    }

    @Override public void onPageFinished(WebView view, String url) {
        try {
            super.onPageFinished(view, url);
            if (lsMerchantReturnUrls.contains(url)) {
                view.setVisibility(View.GONE);
                objBrowserSessionDTO.IsTransactionCompleted=true;
                setAndInsertBrowserSessionDetails();

                objPinePGPaymentCallBack.onTransactionResponse(null);
                return;
            }
            if(objOtpAndNetBankingServiceManager ==null) {
                objOtpAndNetBankingServiceManager = new OtpAndNetBankingServiceManager(this.ctx, this.bundleConfigurationData, this.webViewContainer, view,
                        objBrowserSessionDTO);
                objOtpAndNetBankingServiceManager.setJavaScriptInterface(objPinePGJavaScriptInterface);
            }
//            objOtpAndNetBankingServiceManager.startOnlyNetBankingHandler(url);
            //objApiServiceInterfaceImplementation.callInsertBrowserSessionDetailsApi(objBrowserSessionDTO);
            objPinePGJavaScriptInterface.setCurrentUrl(url);
            objOtpAndNetBankingServiceManager.startNetBankingAndOtpHandlers(url);
            tempFuncForPineLabsMakePaymentUrl(view);
        }
        catch (Exception e)
        {
            objPinePGPaymentCallBack.onErrorOccurred(PINE_PG_CODE_EXCEPTION_OCCURRED,PinePGConstants.PinePGErrorCodeVsMessageMap.get(PINE_PG_CODE_EXCEPTION_OCCURRED),null);
            Log.e(TAG,"An exception occurred while loading page...onPageFinished",e);
        }
    }

    @Override public void onLoadResource(WebView view, String url) { super.onLoadResource(view, url); }
    @Override public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
        super.onRenderProcessGone(view,detail);
        boolean STATUS = false;
        try {
            if(Build.VERSION.SDK_INT>=26) {
                if (detail.didCrash()) {
                    Log.e("PGWebViewClient", "System killed the WebView rendering process " +
                            "to reclaim memory. Recreating...");
                    try{
                        webViewTerminationHandling(view);
                    }
                    catch(Exception Ex){
                        objPinePGPaymentCallBack.onErrorOccurred(PinePGConstants.PINE_PG_WEB_VIEW_RENDERING_ERROR, PinePGConstants.PinePGErrorCodeVsMessageMap.get(PinePGConstants.PINE_PG_WEB_VIEW_RENDERING_ERROR),null);
                        STATUS = true;
                    }
                }
                else {
                    objPinePGPaymentCallBack.onErrorOccurred(PinePGConstants.PINE_PG_WEB_VIEW_RENDERING_ERROR, PinePGConstants.PinePGErrorCodeVsMessageMap.get(PinePGConstants.PINE_PG_WEB_VIEW_RENDERING_ERROR),null);
                    Log.e("PGWebViewClient", "System killed the WebView rendering process due to an internal error");
                }
            }
        } catch (Exception ex) {
            Log.e(TAG,"An exception hs occurred while handling Web View rendering");
        }
        return STATUS;
    }
    private void errorHandler(int errorCode) {
        if(PinePGConstants.PinePGErrorCodeVsMessageMap.containsKey(errorCode)) {
            objBrowserSessionDTO.iErrorCode=errorCode;
            objBrowserSessionDTO.strErrorMessage=PinePGConstants.PinePGErrorCodeVsMessageMap.get(errorCode);
        }
        else {
            //Default error
            objBrowserSessionDTO.iErrorCode=PINE_PG_CODE_EXCEPTION_OCCURRED;
            objBrowserSessionDTO.strErrorMessage=PinePGConstants.PinePGErrorCodeVsMessageMap.get(PINE_PG_CODE_EXCEPTION_OCCURRED);
        }
        setAndInsertBrowserSessionDetails();
        objPinePGPaymentCallBack.onErrorOccurred(errorCode, objBrowserSessionDTO.strErrorMessage,null);
    }
    public void webViewTerminationHandling(WebView view) {
        view.reload();
    }
    private void tempFuncForPineLabsMakePaymentUrl(WebView objWebView)
    {
        if(objWebView.getUrl().contains("https://www.pinelabs.com/make-payment"))
        {
            String jsCode=INVOICE_PAYMENT_PAGE_SUBMISSION_JS_CODE;
            objWebView.evaluateJavascript(jsCode,null);
        }
    }
    private void setEnvironment()
    {
        this.environment=this.bundleConfigurationData.getInt(MerchantConfigurationConstants.ENVIRONMENT_KEY);
    }
    private void setMerchantReturnUrls()
    {
        this.lsMerchantReturnUrls=this.bundleConfigurationData.getStringArrayList(MerchantConfigurationConstants.RETURN_URLS_KEY);
    }
    private void setAndInsertBrowserSessionDetails()
    {
        try {
            objApiServiceInterfaceImplementation =new ApiServiceInterfaceImplementation(environment);
            java.util.Date date = new Date();
            Timestamp timestamp = new java.sql.Timestamp(date.getTime());
            this.objBrowserSessionDTO.setBrowserSessionFinishTime(timestamp);
            this.objBrowserSessionDTO.IsOtpDetected = objOtpAndNetBankingServiceManager.getIsOtpDetected();
            this.objBrowserSessionDTO.IsOtpApproved = objOtpAndNetBankingServiceManager.getIsOtpSubmitted();
            this.objBrowserSessionDTO.IsOtpReaderStarted = objOtpAndNetBankingServiceManager.getIsOtpReaderStarted();
            this.objBrowserSessionDTO.setHaveSmsPermission(objOtpAndNetBankingServiceManager.getHaveSmsPermission());
            //objApiServiceInterfaceImplementation.callInsertBrowserSessionDetailsApi(objBrowserSessionDTO);
        }
        catch (Exception Ex)
        {
            Log.e("ParseError","browser session parameters parsing failed");
            Log.e("error", String.valueOf(Ex.getStackTrace()));
            Log.e("error", Ex.getMessage());
        }
    }

    private void destroy()
    {
        if(objOtpAndNetBankingServiceManager !=null) {
            objOtpAndNetBankingServiceManager.stopService();
        }
        objBrowserSessionDTO =null;
        objApiServiceInterfaceImplementation =null;
    }
}
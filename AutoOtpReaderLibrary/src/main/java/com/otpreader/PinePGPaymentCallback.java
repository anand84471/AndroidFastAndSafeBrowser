package com.otpreader;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import com.otpreader.Constants.PinePGConstants;
import com.otpreader.apiclient.interfaces.ApiServiceInterfaceImplementation;
import com.otpreader.interfaces.IPinePGResponseCallback;
import com.otpreader.PinePGDTOs.BrowserSessionDTO;
import java.util.Date;

public class PinePGPaymentCallback implements IPinePGResponseCallback {
    IPinePGResponseCallback merchantCallBack =null;
    ApiServiceInterfaceImplementation objApiServiceInterfaceImplementation =null;
    Context ctx;
    private int environment;
    BrowserSessionDTO objBrowserSessionDTO;

    public PinePGPaymentCallback() {
        super();
    }

    public PinePGPaymentCallback(IPinePGResponseCallback callback, Context context, BrowserSessionDTO objBrowserSessionDTO, int environment) {
        this.ctx=context;
        this.environment=environment;
        this.merchantCallBack =callback;
        this.objBrowserSessionDTO=objBrowserSessionDTO;
        this.objBrowserSessionDTO.browserSessionStatTime=new Date();
        this.objApiServiceInterfaceImplementation =new ApiServiceInterfaceImplementation(environment);
    }
    @Override
    public void onPageStarted(WebView view, String url) {
        objBrowserSessionDTO.strLastVisitedUrl=url;
        this.merchantCallBack.onPageStarted(view,url);
    }
    @Override
    public void onErrorOccurred(int code, String message,Bundle response) {
        objBrowserSessionDTO.iErrorCode=code;
        objBrowserSessionDTO.strErrorMessage=message;
        objApiServiceInterfaceImplementation.callInsertBrowserSessionDetailsApi(objBrowserSessionDTO);
        this.merchantCallBack.onErrorOccurred(code,message,objBrowserSessionDTO.getResponseBundle());
        ((Activity)ctx).finish();
    }
    @Override
    public void onTransactionResponse(Bundle response) {
        objApiServiceInterfaceImplementation.callInsertBrowserSessionDetailsApi(objBrowserSessionDTO);
        this.merchantCallBack.onTransactionResponse(objBrowserSessionDTO.getResponseBundle());
        ((Activity)ctx).finish();
    }
    @Override
    public void onWebViewReady(WebView webView) {
        this.merchantCallBack.onWebViewReady(webView);
    }
    @Override
    public void onCancelTxn(Bundle response)
    {
        objApiServiceInterfaceImplementation.callInsertBrowserSessionDetailsApi(objBrowserSessionDTO);
        this.merchantCallBack.onCancelTxn(response);
        ((Activity)ctx).finish();
    }
    public void onPressedBackButton()
    {
        this.objBrowserSessionDTO.IsBackPressed=true;
        this.objBrowserSessionDTO.iErrorCode= PinePGConstants.PINE_PG_BACK_BUTTON_PRESSED;
        objBrowserSessionDTO.strErrorMessage=PinePGConstants.PinePGErrorCodeVsMessageMap.get(PinePGConstants.PINE_PG_BACK_BUTTON_PRESSED);
        onCancelTxn(objBrowserSessionDTO.getResponseBundle());
    }
    public BrowserSessionDTO getBrowserSessionDTO()
    {
        return objBrowserSessionDTO;
    }
}

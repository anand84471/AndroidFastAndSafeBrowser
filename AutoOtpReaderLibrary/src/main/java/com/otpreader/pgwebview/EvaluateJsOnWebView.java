package com.otpreader.pgwebview;

import android.annotation.TargetApi;
import android.os.Build;
import android.renderscript.Sampler;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.otpreader.Constants.PinePGConstants;
import com.otpreader.ServiceManagers.ReportJsErrorService;
import com.otpreader.apiclient.MainApiClient;
import com.otpreader.apiclient.interfaces.ApiServiceInterfaceImplementation;

import java.io.IOException;
import java.io.StringReader;


public class EvaluateJsOnWebView {
    private WebView objWebView;
    private ApiServiceInterfaceImplementation objApiServiceInterfaceImplementation;
    private int debugEnvironment;
    private String jsCodeToExecute;
    private String currentUrl;
    private String currentUrlHtml;
    private String jsCode;
    private String jsException;
    int jsCodeTypeId;
    private ReportJsErrorService objReportJsErrorService;
    public EvaluateJsOnWebView(WebView webView,int environment)
    {
        this.objWebView=webView;
        this.debugEnvironment=environment;
    }
    public void injectJsOnPage(final String jsCode, final int JsCodeTyeId)
    {
        try {
            this.jsCodeToExecute = jsCode;
            this.jsCodeTypeId = JsCodeTyeId;
            String jsCodeWithExceptionHandling ="function returnError(){try{"+jsCode+"}catch(ex){return 'error '+ex.message;}}returnError();";
            jsCodeToExecute=jsCodeWithExceptionHandling;
            if(objWebView!=null){
                objWebView.evaluateJavascript(jsCodeWithExceptionHandling, new ValueCallback<String>() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onReceiveValue(String value) {
                        if(value!=null&& !value.equals("") && !value.equals("null")){
                            jsException = value;
                            insertJsErrorThroughApi(jsCode,JsCodeTyeId,value);
                        }
                    }
                });
            }
        }
        catch (Exception Ex)
        {

        }
    }
    public void injectJsOnPageWithCallbackEnabled(String jsCode, ValueCallback<String> objCallBack)
    {
        objWebView.evaluateJavascript(jsCode,objCallBack);
    }
    private void insertJsErrorThroughApi(String jsCode,int jsCodeTypeId,String exception)
    {
        try {
            objReportJsErrorService=new ReportJsErrorService(this.debugEnvironment);
            objReportJsErrorService.reportJsErrorHasOccurred(objWebView.getUrl(),exception,jsCode,
                    "",jsCodeTypeId);
        }
        catch (Exception ex)
        {

        }
    }
    public void getNetBankingUserIdFromPage(String jsCode)
    {
        injectJsOnPage(jsCode,PinePGConstants.JS_CODE_TYPE_PAGE_GET_USER_ID_FROM_PAGE);
    }
    public void setNetBankingUserIdOnPage(String JsCode)
    {
        injectJsOnPage(JsCode,PinePGConstants.JS_CODE_TYPE_PAGE_SET_USER_ID_INTO_PAGE);
    }
}

package com.otpreader.pgwebview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.otpreader.Constants.MerchantConfigurationConstants;
import com.otpreader.PinePGPaymentCallback;
import com.otpreader.interfaces.PinePGJavaScriptInterface;

public class PinePGCustomWebView extends AppCompatActivity {
    Context ctx;
    private String TAG="com.pinelabs.sdk.PinePGWebView.PinePGCustomWebView";
    private volatile WebView objWebView=null;
    boolean isRequestForAutoOtpReader;
    private PGChromeClient.PGWebViewChromeClient objPGWebViewClient;
    ViewGroup webViewContainer;
    private int themeId;
    private Bundle configurationData=null;
    private String postData;
    private boolean haveSmsPermission=false;
    PinePGPaymentCallback objPinePGPaymentCallback;
    PinePGJavaScriptInterface objPinePGJavaScriptInterface;
    public PinePGCustomWebView(Context context, WebView webView, ViewGroup WebViewContainer, Bundle configData, PinePGPaymentCallback
                               parPinePGPaymentCallback) {
        ctx=context;
        objWebView=webView;
        this.webViewContainer=WebViewContainer;
        this.configurationData=configData;
        this.objPinePGPaymentCallback=parPinePGPaymentCallback;
        setPostData();
        startWebService();
    }
    private void setPostData()
    {
        this.postData=configurationData.getString(MerchantConfigurationConstants.POST_DATA);
    }
    public void startWebService()
    {
        initializePinePGWebView();
        loadWebViewUrl(this.postData);
    }
    private boolean getShouldStartPinePGLoader()
    {
        return this.configurationData.getBoolean(MerchantConfigurationConstants.SHOULD_START_PINE_PG_LOADER_KEY);
    }
    private void initializePinePGWebView() {
        {
            objPinePGJavaScriptInterface=new PinePGJavaScriptInterface(ctx);
            final PGWebViewClient objWebViewClient = new PGWebViewClient(ctx,this.webViewContainer,this.configurationData,this.objPinePGPaymentCallback,objPinePGJavaScriptInterface);
            final PGChromeClient objPGWebViewChromeClientHelper = new PGChromeClient(ctx,this.objPinePGPaymentCallback);
            objPGWebViewChromeClientHelper.shouldStartLoader=getShouldStartPinePGLoader();
            objPGWebViewClient=objPGWebViewChromeClientHelper.new PGWebViewChromeClient();
            objWebView.setWebViewClient(objWebViewClient);
            objWebView.setWebChromeClient(objPGWebViewClient);
            objWebView.getSettings().setUseWideViewPort(true);
            objWebView.clearCache(true);
            objWebView.clearHistory();
            objWebView.requestFocusFromTouch();
            objWebView.getSettings().setLoadWithOverviewMode(true);
            objWebView.getSettings().setSupportMultipleWindows(true);
            objWebView.getSettings().setJavaScriptEnabled(true);
            objWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            objWebView.requestFocus();
            objWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            objWebView.getSettings().setUseWideViewPort(true);
            objWebView.getSettings().setDomStorageEnabled(true);
            objWebView.addJavascriptInterface(objPinePGJavaScriptInterface,"AndroidInterface");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                objWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                objWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            objWebView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    try {
                        Intent i = new Intent();
                        i.setPackage("com.android.chrome");
                        i.setAction(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        PinePGCustomWebView.this.startActivity(i);
                    } catch (Exception e) {
                        Log.e(TAG, "Received an Exception", e);
                    }
                }
            });
        }
    }
    private void loadWebViewUrl(String postBody) {
        try {
            objWebView.loadUrl(postBody);
            //objWebView.loadData(postBody, "text/html", "UTF-8");
        } catch (Exception e) {
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        objWebView.removeJavascriptInterface("AndroidInterface");
        objWebView=null;
    }
}
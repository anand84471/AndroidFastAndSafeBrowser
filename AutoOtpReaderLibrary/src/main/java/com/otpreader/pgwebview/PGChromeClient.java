package com.otpreader.pgwebview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.otpreader.Constants.PinePGConstants;
import com.otpreader.PinePGPaymentCallback;
import com.otpreader.R;

class PGChromeClient extends AppCompatActivity {
    private Context ctx;
    public  Dialog PinePGLoader=null;
    private String TAG="com.pinelabs.sdk.PinePGWebView.PGChromeClient";
    public boolean shouldStartLoader=false;
    private long startTime;
    private long finishedTime;
    private double timeAllowedToFullLoad=60000000000.0;
    private int noOfWebsiteReload=0;
    private PinePGPaymentCallback objPinePgPaymentCallBack;
    PGChromeClient(Context context, PinePGPaymentCallback objPinePGPaymentCallBack)
    {
        ctx=context;
        initializePinePGLoader();
        this.objPinePgPaymentCallBack=objPinePGPaymentCallBack;
    }
    class PGWebViewChromeClient extends WebChromeClient {
        @Override
        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
            DefaultWebViewCustomization newWebView = new DefaultWebViewCustomization(ctx);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(newWebView);
            resultMsg.sendToTarget();
            newWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    try {

                        if(url.contains("PinePGRedirect/DownloadBFLTermAndCondition")) {
                            return false;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "An exception has occurred", e);
                    }
                    return true;
                }
            });
            return true;
        }

        @Override
        public  void onProgressChanged(WebView view, int newProgress) {
            try {
//                if(startTime==0)
//                {
//                    startTime = System.nanoTime();
//                }
//                if(newProgress==100)
//                {
//                    startTime=0;
//                }
//                finishedTime = System.nanoTime();
//                if(finishedTime-startTime>timeAllowedToFullLoad)
//                {
//
//                    startTime = System.nanoTime();
//                    objPinePgPaymentCallBack.onErrorOccurred(PinePGConstants.PINE_PG_PAGE_LOAD_TIMEOUT,
//                            PinePGConstants.PinePGErrorCodeVsMessageMap.get(PinePGConstants.PINE_PG_PAGE_LOAD_TIMEOUT));
//                }
                if(view.getUrl().contains("PinePGRedirect/DownloadBFLTermAndCondition")||(PinePGLoader==null))
                {
                    return;
                }
                if(shouldStartLoader) {
                    try {
                          {
                            if (!view.getUrl().contains("BFL")) {
                                PinePGLoader.show();
                            }
                        }
                    }
                    catch (Exception e)
                    {

                    }
                    if(newProgress>75)
                    {
                        PinePGLoader.dismiss();
                        view.setVisibility(View.VISIBLE);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG,"An error has occurred"+e.getMessage(),e);
            }
        }
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            // getSupportActionBar().setTitle(title);
        }
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        PinePGLoader.dismiss();
        PinePGLoader=null;
    }
    private void initializePinePGLoader() {
        if(PinePGLoader==null) {
            {
                PinePGLoader= new Dialog(ctx, R.style.CustomDialog);
                PinePGLoader.setContentView(R.layout.progressbar);
                PinePGLoader.setCanceledOnTouchOutside(false);
                PinePGLoader.setCancelable(true);
            }
        }
    }
}
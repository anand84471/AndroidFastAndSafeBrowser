package com.otpreader.interfaces;

import android.os.Bundle;
import android.webkit.WebView;

public interface IPinePGResponseCallback {
     void onErrorOccurred(int code, String message,Bundle Response);
     void onTransactionResponse(Bundle Response);
     void onCancelTxn(Bundle Response);
     void onPageStarted(WebView view,String url);
     void onWebViewReady(WebView webView);
}
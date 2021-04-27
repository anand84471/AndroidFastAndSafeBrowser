package com.otpreader.pgwebview;

import android.content.Context;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.webkit.WebView;

class DefaultWebViewCustomization extends WebView {
    public DefaultWebViewCustomization(Context context) {
        super(context);
    }
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection connection = super.onCreateInputConnection(outAttrs);
        int inputMode=outAttrs.inputType;
        switch (inputMode) {
            case InputType.TYPE_TEXT_VARIATION_PHONETIC:
                outAttrs.inputType = InputType.TYPE_CLASS_PHONE;
                break;
            case InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS:
                outAttrs.inputType = InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS;
                break;
            case InputType.TYPE_CLASS_NUMBER:
                outAttrs.inputType =
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;
                break;
            default:
        }
        return connection;
    }
}

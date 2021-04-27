package com.otpreader.activity;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.otpreader.BrowserCheckout;
import com.otpreader.Constants.MerchantConfigurationConstants;
import com.otpreader.PinePGPaymentCallback;
import com.otpreader.R;
import com.otpreader.AppPermissionHandler.SmsPermission;
import com.otpreader.WrapperClass.SetInstancesAndValuesFromUser;
import com.otpreader.Constants.PinePGConstants;
import com.otpreader.pgwebview.PinePGCustomWebView;
import com.otpreader.PinePGDTOs.BrowserSessionDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
 public class PinePGBrowserActivity extends SmsPermission {
    private Bundle bundleConfigData =null;
    private PinePGCustomWebView objPinePGCustomWebView;
    private ViewGroup webViewContainer=null;
    private WebView objWebView=null;
    private AlertDialog.Builder alertOnBackPress;
    private boolean haveSmsPermission=false;
    private PinePGPaymentCallback objMerchantCallback;
    private  static final String TAG="PinePGBrowserActivity";
    private BrowserSessionDTO objBrowserSessionDTO;
    private int environment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pine_pg_browser);
        this.haveSmsPermission=getSmsPermissionResult();
        objBrowserSessionDTO=new BrowserSessionDTO();

        getIntentParameters();
        fetchViewsById();
        startWebView();
    }
    private void setDataToBrowserSessionDTO()
    {
        try {
            Bundle browserPaymentData = BrowserCheckout.getPaymentData();
            if (browserPaymentData != null) {
                //Provided by merchant
                objBrowserSessionDTO.browserSessionStatTime=new Date();
                objBrowserSessionDTO.iMerchantId=Integer.parseInt(Objects.requireNonNull(browserPaymentData.getString(MerchantConfigurationConstants.MERCHANT_ID_KEY)));
                objBrowserSessionDTO.strTransactionId=browserPaymentData.getString(MerchantConfigurationConstants.TRANSACTION_ID_KEY);
                objBrowserSessionDTO.strAmount=browserPaymentData.getString(MerchantConfigurationConstants.AMOUNT_KEY);
                objBrowserSessionDTO.strCustomerId=browserPaymentData.getString(MerchantConfigurationConstants.CUSTOMER_ID_KEY);
                objBrowserSessionDTO.strCustomerEmail=browserPaymentData.getString(MerchantConfigurationConstants.CUSTOMER_EMAIL_KEY);
                objBrowserSessionDTO.strCustomerPhoneNo=browserPaymentData.getString(MerchantConfigurationConstants.CUSTOMER_PHONE_NO_KEY);
                objBrowserSessionDTO.strOrderId=browserPaymentData.getString(MerchantConfigurationConstants.ORDER_ID_KEY);
                this.objBrowserSessionDTO.iLanguageId = bundleConfigData.getInt(MerchantConfigurationConstants.LANGUAGE_KEY);
                this.objBrowserSessionDTO.iThemeId=bundleConfigData.getInt(MerchantConfigurationConstants.THEME_ID_KEY);
                this.objBrowserSessionDTO.strPaymentStartUrl=bundleConfigData.getString(MerchantConfigurationConstants.POST_DATA);
                this.objBrowserSessionDTO.shouldStartPinePGLoader=bundleConfigData.getBoolean(MerchantConfigurationConstants.SHOULD_START_PINE_PG_LOADER_KEY);
                this.environment=bundleConfigData.getInt(MerchantConfigurationConstants.ENVIRONMENT_KEY);
                ArrayList<String> returnurls=bundleConfigData.getStringArrayList(MerchantConfigurationConstants.RETURN_URLS_KEY);
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < returnurls.size(); i++) {
                    sb.append(returnurls.get(i));
                }
                this.objBrowserSessionDTO.strReturnUrls=sb.toString();
                objBrowserSessionDTO.setHaveSmsPermission(this.haveSmsPermission);
                changeLanguage(this.objBrowserSessionDTO.iLanguageId);
            }
        }
        catch (Exception Ex) {
            Log.e("ParseError", "Payment parameters parsing failed");
            Log.e("error", Arrays.toString(Ex.getStackTrace()));
            Log.e("error", Objects.requireNonNull(Ex.getMessage()));
        }
    }
    private void changeLanguage(int language)
    {
        if(language== PinePGConstants.LANGUAGE_HINDI)
        {
            changeLanguage("hi");
        }
        else if(language== PinePGConstants.LANGUAGE_ENGLISH)
        {
            changeLanguage("en");
        }
    }
    private void changeLanguage(String localeName) {
        Locale myLocale = new Locale(localeName);
        Resources res = this.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
    private void getIntentParameters()
    {
        bundleConfigData =getIntent().getExtras();
    }
    private void startWebView()
    {
        try {
            bundleConfigData.putBoolean("sms_permission", haveSmsPermission);
            SetInstancesAndValuesFromUser.SetContext(this);
            setDataToBrowserSessionDTO();
            objMerchantCallback = new PinePGPaymentCallback(BrowserCheckout.getPinePGResponseCallback(), this, objBrowserSessionDTO,environment);
            objPinePGCustomWebView = new PinePGCustomWebView(PinePGBrowserActivity.this, objWebView, webViewContainer, this.bundleConfigData, this.objMerchantCallback);
        }
        catch (Exception Ex)
        {
            Log.e("error", Arrays.toString(Ex.getStackTrace()));
            Log.e("error", Objects.requireNonNull(Ex.getMessage()));
        }
    }
    private void fetchViewsById() {
        objWebView = findViewById(R.id.PGWebView);
        webViewContainer=findViewById(R.id.relative);
    }
    @Override
    public void onBackPressed() {
    try {
        alertOnBackPress = new AlertDialog.Builder(this);
        alertOnBackPress.setCancelable(false);
        if (objWebView.canGoBack()) {
            alertOnBackPress.setMessage(R.string.PinePG_back_press_dialog);
            //  BACK_PRESS_DIALOG.setCancelable(false);
            alertOnBackPress.setPositiveButton(R.string.PinePG_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    objMerchantCallback.onPressedBackButton();
                }
            });
            alertOnBackPress.setNegativeButton(R.string.PinePG_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        } else {
            alertOnBackPress.setMessage(R.string.PinePG_back_press_dialog);
            alertOnBackPress.setPositiveButton(R.string.PinePG_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    objBrowserSessionDTO.IsBackPressed=true;
                    objBrowserSessionDTO.iErrorCode= PinePGConstants.PINE_PG_BACK_BUTTON_PRESSED;
                    objMerchantCallback.onPressedBackButton();
                }
            });
            alertOnBackPress.setNegativeButton(R.string.PinePG_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    } catch (Exception e) {
        Log.e(TAG, "An exception has occurred", e);
    }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
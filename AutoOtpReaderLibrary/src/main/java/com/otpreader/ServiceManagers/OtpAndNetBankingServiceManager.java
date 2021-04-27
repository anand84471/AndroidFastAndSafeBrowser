package com.otpreader.ServiceManagers;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import com.otpreader.AutoOtpHandlers.PinePGOtpReaderHandler;
import com.otpreader.PinePGDTOs.BrowserSessionDTO;
import com.otpreader.Constants.PinePGConstants;
import com.otpreader.R;
import com.otpreader.WrapperClass.SetInstancesAndValuesFromUser;
import com.otpreader.PinePGDTOs.BankUrlDetailsDTO;
import com.otpreader.apiclient.interfaces.ApiServiceInterfaceImplementation;
import com.otpreader.interfaces.PinePGJavaScriptInterface;
import com.otpreader.pgwebview.EvaluateJsOnWebView;
import java.util.Locale;

public class OtpAndNetBankingServiceManager {
    private int otpReaderMode;
    private int environment;
    private boolean shouldEnabledPageCustomization;
    private int language;
    private PinePGOtpReaderHandler objPinePGOtpReaderHandler;
    private View viewBottomSheet;
    private LinearLayout dynamicLinearLayout;
    private WebView objWebView;
    private int themeID;
    private Context ctx;
    private ViewGroup rootViewLayout;
    private boolean haveSmsPermission=false;
    private ApiServiceInterfaceImplementation objHandleBankUrlResponse;
    private BankUrlDetailsDTO objUrlDataObject;
    private EvaluateJsOnWebView objEvaluateJsOnWebView;
    private boolean IsOtpReaderStarted;
    private String strAutoOtpReaderUrl;
    private PinePGJavaScriptInterface objPinePGJavaScriptInterface;
    private boolean isNetBankingServiceStarted=false;
    private boolean isInitializeNetBankingLayout=false;
    private BrowserSessionDTO objBrowserSessionDTO;
    public OtpAndNetBankingServiceManager(Context context, Bundle ConfigurationData, ViewGroup webViewContainer, WebView objWebView,
                                          BrowserSessionDTO objBrowserSessionDTO){
        try {
            this.rootViewLayout=webViewContainer;
            this.ctx=context;
            this.objWebView=objWebView;
            extractMerchantConfigurationData(ConfigurationData);
            initialize();
            configureViews();
            this.objBrowserSessionDTO=objBrowserSessionDTO;
            configureNetBankingPageCustomizationOtpHandlers();
            objHandleBankUrlResponse=new ApiServiceInterfaceImplementation(environment);
            objEvaluateJsOnWebView=new EvaluateJsOnWebView(this.objWebView,environment);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public boolean getIsOtpDetected() { return this.objPinePGOtpReaderHandler.isOtpDetected(); }
    public boolean getIsOtpSubmitted() { return this.objPinePGOtpReaderHandler.isOtpSubmitted(); }
    public boolean getIsOtpReaderStarted() { return this.objPinePGOtpReaderHandler.isOtpReaderStarted(); }
    public boolean getHaveSmsPermission() { return this.haveSmsPermission; }
    private void extractMerchantConfigurationData(Bundle objBundleParameters) {
        if (objBundleParameters != null) {
            this.environment = objBundleParameters.getInt("environment");
            this.otpReaderMode = objBundleParameters.getInt("auto_otp_reader_mode");
            this.shouldEnabledPageCustomization = objBundleParameters.getBoolean("enable_page_customization");
            this.language = objBundleParameters.getInt("language");
            this.themeID=objBundleParameters.getInt("theme_id");
            this.haveSmsPermission=objBundleParameters.getBoolean("sms_permission"); }
    }
    private void configureViews() {
        try {
            if (viewBottomSheet == null) {
                viewBottomSheet = LayoutInflater.from(ctx).inflate(R.layout.root_layout, rootViewLayout, false);
                rootViewLayout.addView(viewBottomSheet);
            }
            this.dynamicLinearLayout = viewBottomSheet.findViewById(R.id.dynamiccontent);
        }
        catch (Exception ex)
        {

        }
    }
    private void initialize()
    {
        changeLanguage();
        changeTheme();
    }
    private void configureNetBankingPageCustomizationOtpHandlers()
    {
        try {
            if (this.objPinePGOtpReaderHandler == null) {
                SetInstancesAndValuesFromUser.SetContext(this.ctx);
                this.objPinePGOtpReaderHandler = new PinePGOtpReaderHandler(this.viewBottomSheet, this.dynamicLinearLayout, this.objWebView, this.environment, this.haveSmsPermission,
                        this.objBrowserSessionDTO);
            }
        }
        catch (Exception ex)
        {

        }
    }
    public void stopService()
    {
        objPinePGOtpReaderHandler.stopService();
        objEvaluateJsOnWebView=null;
    }
    public void startNetBankingAndOtpHandlers(String url) {
        try {
            objUrlDataObject=objHandleBankUrlResponse.getUrlDetailsObject(url);
            objBrowserSessionDTO.IsFetchedUrlDetails=objHandleBankUrlResponse.IsFetchedUrlDetails();
            if(objUrlDataObject!=null&& objPinePGOtpReaderHandler !=null)
            {
                objPinePGOtpReaderHandler.setJavaScriptInterface(objPinePGJavaScriptInterface);
                objEvaluateJsOnWebView.setNetBankingUserIdOnPage(objUrlDataObject.getSetNetBankingUserIdToPage());
                if(objUrlDataObject.isPageToBeResponsive())
                {
                    objEvaluateJsOnWebView.injectJsOnPage(objUrlDataObject.getPageCustomizationCode(),PinePGConstants.JS_CODE_TYPE_PAGE_CUSTOMIZATION);
                    objWebView.scrollTo(0,0);
                }
                if(IsOtpReaderStarted) {
                    return;
                }
                if(objUrlDataObject.getIsNetBankingSubmissionPage())
                {
                    if(!isInitializeNetBankingLayout)
                    {
                        objPinePGOtpReaderHandler.initializeNetBankingLayouts();
                        isInitializeNetBankingLayout=true;
                    }
                    objEvaluateJsOnWebView.setNetBankingUserIdOnPage(objUrlDataObject.getSetNetBankingUserIdToPage());
                    objPinePGOtpReaderHandler.submitNetBankingPage(objUrlDataObject.getNetBankingSubmissionCode(),objUrlDataObject.getGetNetBankingUseridFromPageJsCode(),
                            objUrlDataObject.shouldStartOtpReader());
                    if(objUrlDataObject.shouldStartOtpReader())
                    {
                        objPinePGOtpReaderHandler.configureOtpReader(objUrlDataObject.getOtpLength(),
                                objUrlDataObject.getOptSubmissionJsCode(),objUrlDataObject.getShouldHaltOtpSubmission(),
                                objUrlDataObject.getShouldUseGenericOtpSubmissionCode());
                    }
                    return;
                }
                if(!IsOtpReaderStarted&&objUrlDataObject.shouldStartOtpReader()) {
                    if(objPinePGOtpReaderHandler.startService(objUrlDataObject.getOtpLength(),objUrlDataObject.getOptSubmissionJsCode(),
                            objUrlDataObject.getShouldHaltOtpSubmission(),
                            objUrlDataObject.getShouldUseGenericOtpSubmissionCode()))
                    {
                        IsOtpReaderStarted=true;
                        strAutoOtpReaderUrl=url;
                    }
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private void changeLanguage()
    {
        if(this.language== PinePGConstants.LANGUAGE_HINDI)
        {
            changeLanguage("hi");
        }
        else if(this.language== PinePGConstants.LANGUAGE_ENGLISH)
        {
            changeLanguage("en");
        }
    }
    private void changeLanguage(String localeName) {
            Locale myLocale = new Locale(localeName);
            Resources res = this.ctx.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
    }
    private void changeTheme()
    {
        switch (this.themeID){
            case PinePGConstants.THEME_PinePGLightTheme:{
                this.ctx.setTheme(R.style.PinePGLightTheme);
                break;
            }
            case PinePGConstants.THEME_PinePGDarkTheme:{
                this.ctx.setTheme(R.style.PinePGDarkTheme);
                break;
            }
            case PinePGConstants.THEME_PinePGBlackAndWhiteTheme:{
                this.ctx.setTheme(R.style.PinePGBlackAndWhiteTheme);
                break;
            }
            case PinePGConstants.THEME_PinePGRedAndWhiteTheme:{
                this.ctx.setTheme(R.style.PinePGRedAndWhiteTheme);
                break;
            }
            case PinePGConstants.THEME_PinePGIndigoTheme:{
                this.ctx.setTheme(R.style.PinePGIndigoTheme);
            }
        }
    }
    public void setJavaScriptInterface(PinePGJavaScriptInterface objPinePGJavaScriptInterface)
    {
        this.objPinePGJavaScriptInterface=objPinePGJavaScriptInterface;
    }
    public void setPinePGBroserSessionDTO(BrowserSessionDTO objBrowserSessionDTO)
    {
        this.objBrowserSessionDTO=objBrowserSessionDTO;
    }
}

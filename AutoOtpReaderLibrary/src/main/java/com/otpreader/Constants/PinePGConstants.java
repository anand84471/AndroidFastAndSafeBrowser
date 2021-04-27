package com.otpreader.Constants;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.PUT;

public class PinePGConstants {
    public static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS=0;
    public static final String EXCEPTON_OCCUR ="EXECPTION" ;
    public static final int OTP_SUBMISSION_STATUS_FAIL =1;
    public static final int OTP_SUBMISSION_STATUS_SUCCESS =1;
    public static final String WRONG_OTP_ENTERED="Wrong OTP. Please try again";
    public static final String OTP_WAITING_MESSAGE="Waiting for OTP. Please wait.";
    public static final int LANGUAGE_ENGLISH=1;
    public static final int LANGUAGE_HINDI=2;
    public static final int THEME_PinePGLightTheme=1;
    public static final int THEME_PinePGDarkTheme=2;
    public static final int THEME_PinePGBlackAndWhiteTheme=3;
    public static final int THEME_PinePGRedAndWhiteTheme=4;
    public static final int THEME_PinePGIndigoTheme=5;
    public static final int JS_CODE_TYPE_PAGE_CUSTOMIZATION=1;
    public static final int JS_CODE_TYPE_PAGE_GET_USER_ID_FROM_PAGE=2;
    public static final int JS_CODE_TYPE_PAGE_SET_USER_ID_INTO_PAGE=3;
    public static final int JS_CODE_TYPE_PAGE_NET_BANKING_PAGE_SUBMISSION_JS_CODE=4;
    public static final int JS_CODE_TYPE_OTP_PAGE_SUBMISSION_JS_CODE =5;

    //Error Codes:
    /** Generic error */
    public static final int PINE_PG_ERROR_UNKNOWN = -1;
    /** Server or proxy hostname lookup failed */
    public static final int PINE_PG_ERROR_HOST_LOOKUP = -2;
    /** Unsupported authentication scheme (not basic or digest) */
    public static final int PINE_PG_ERROR_UNSUPPORTED_AUTH_SCHEME = -3;
    /** User authentication failed on server */
    public static final int PINE_PG_ERROR_AUTHENTICATION = -4;
    /** User authentication failed on proxy */
    public static final int PINE_PG_ERROR_PROXY_AUTHENTICATION = -5;
    /** Failed to connect to the server */
    public static final int PINE_PG_ERROR_CONNECT = -6;
    /** Failed to read or write to the server */
    public static final int PINE_PG_ERROR_IO = -7;
    /** Connection timed out */
    public static final int PINE_PG_ERROR_TIMEOUT = -8;
    /** Too many redirects */
    public static final int PINE_PG_ERROR_REDIRECT_LOOP = -9;
    /** Unsupported URI scheme */
    public static final int PINE_PG_ERROR_UNSUPPORTED_SCHEME = -10;
    /** Failed to perform SSL handshake */
    public static final int PINE_PG_ERROR_FAILED_SSL_HANDSHAKE = -11;
    /** Malformed URL */
    public static final int PINE_PG_ERROR_BAD_URL = -12;
    /** Generic file error */
    public static final int PINE_PG_ERROR_FILE = -13;
    /** File not found */
    public static final int PINE_PG_ERROR_FILE_NOT_FOUND = -14;
    /** Too many requests during this load */
    public static final int PINE_PG_ERROR_TOO_MANY_REQUESTS = -15;
    /** Resource load was canceled by Safe Browsing */
    public static final int PINE_PG_ERROR_UNSAFE_RESOURCE = -16;
    /** Invalid language id value*/
    public static final int PINE_PG_INVALID_LANGUAGE_SETTING=-17;
    /** Invalid theme id value*/
    public static final int PINE_PG_INVALID_THEME_ID=-18;
    /** merchant id is missing*/
    public static final int PINE_PG_MERCHANT_ID_IS_MISSING=-19;
    /** merchant id is invalid*/
    public static final int PINE_PG_MERCHANT_ID_IS_INVALID=-20;
    /** the transaction id should be unique*/
    public static final int PINE_PG_NON_UNIQUE_TRANSACTION_ID=-21;
    /** An unhandled exception occurred at runtime*/
    public static final int PINE_PG_CODE_EXCEPTION_OCCURRED=-22;
    /**Invalid transaction id*/
    public static final int PINE_PG_INVALID_TRANSACTION_ID=-23;
    public static final int PINE_PG_WEB_VIEW_RENDERING_ERROR=-24;
    public static final int PINE_PG_BACK_BUTTON_PRESSED=-25;
    public static final int PINE_PG_PAGE_LOAD_TIMEOUT=-26;
    public static final int PINE_PG_INVALID_CONFIGURATION_DATA=-27;
    public static final  int PINE_PG_INVALID_PAYMENT_PARAMETERS_DATA=-28;
    public static final  int PINE_PG_INVALID_ENVIRONMENT_SELECTION=-29;
    public static final int PINE_PG_INVALID_RETURN_URL=-30;
    public static final int PINE_PG_INVALID_POST_DATA=-31;
    public static final Map<Integer,String> PinePGErrorCodeVsMessageMap=new HashMap<>();
    static {
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_UNSAFE_RESOURCE,"Resource load was canceled by Safe Browsing");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_TOO_MANY_REQUESTS,"Too many requests during this load");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_FILE_NOT_FOUND,"File not found ");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_FILE,"Generic file error");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_BAD_URL,"Malformed URL ");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_FAILED_SSL_HANDSHAKE,"Failed to perform SSL handshake");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_UNSUPPORTED_SCHEME,"Unsupported URI scheme");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_REDIRECT_LOOP,"Too many redirects");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_TIMEOUT,"Connection timed out");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_IO,"Failed to read or write to the server ");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_CONNECT,"Failed to connect to the server ");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_PROXY_AUTHENTICATION,"User authentication failed on proxy");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_AUTHENTICATION,"User authentication failed on server ");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_UNSUPPORTED_AUTH_SCHEME," Unsupported authentication scheme (not basic or digest) ");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_HOST_LOOKUP,"Server or proxy hostname lookup failed");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_ERROR_UNKNOWN,"Generic error");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_INVALID_LANGUAGE_SETTING,"Invalid language option selected");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_INVALID_THEME_ID,"Invalid theme id selection");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_MERCHANT_ID_IS_MISSING,"Merchant Id id missing.Please provide the merchant Id");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_MERCHANT_ID_IS_INVALID,"Invalid merchant Id. merchant ID should be unique");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_NON_UNIQUE_TRANSACTION_ID,"Transaction id should be unique");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_INVALID_TRANSACTION_ID,"Invalid transaction id value. Transaction id value should be long");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_CODE_EXCEPTION_OCCURRED,"OOPS! An exception occurred in the SDK. Please try again");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_WEB_VIEW_RENDERING_ERROR,"System killed the webview rendering process");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_BACK_BUTTON_PRESSED,"Transaction cancelled by user by pressing back button");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_PAGE_LOAD_TIMEOUT,"Transaction was terminated due to timeout");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_INVALID_CONFIGURATION_DATA,"Configuration parameters can not be null");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_INVALID_PAYMENT_PARAMETERS_DATA,"Payment parameters can not be null");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_INVALID_ENVIRONMENT_SELECTION,"Invalid environment value");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_INVALID_RETURN_URL,"Invalid return url");
        PinePGErrorCodeVsMessageMap.put(PINE_PG_INVALID_POST_DATA,"Invalid post data");
    }
    public static final String INVOICE_PAYMENT_PAGE_SUBMISSION_JS_CODE="var merchantName=document.getElementById(\"merchant_name\");\n" +
            "merchantName.value=\"temp\";\n" +
            "var emailId=document.getElementById(\"email\");\n" +
            "emailId.value=\"temp@gmail.com\";\n" +
            "var phoneNo=document.getElementById(\"phone\");\n" +
            "phoneNo.value=\"9999999999\";\n" +
            "var amount=document.getElementById(\"amount\");\n" +
            "amount.value=\"1\";\n" +
            "var invoice=document.getElementsByName(\"invoice\")[0];\n" +
            "invoice.value='inv1234';\n" +
            "var submitButton=document.getElementById(\"disable_button\");\n" +
            "submitButton.click();\n";
}

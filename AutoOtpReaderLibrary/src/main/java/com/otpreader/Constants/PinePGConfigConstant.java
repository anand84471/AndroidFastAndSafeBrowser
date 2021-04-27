package com.otpreader.Constants;

public  class PinePGConfigConstant {
    public static final String PINE_PG_PAYMENT_URL_TEST_SANDBOX_BASE="https://torrentpay.pinepg.in";
    // 192.168.202.145
//    public static final String PINE_PG_PAYMENT_URL_TEST_SANDBOX_BASE="http://172.168.12.35:52134";
    public static final String PINE_PG_PAYMENT_URL_PRODUCTION_BASE="https://paymentbooster.pinepg.in";
    public static final String PINE_PG_PAYMENT_URL_UAT_BASE ="https://paymentboostertest.pinepg.in";
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final int MAX_ALLOWED_OTP_FAIL=5;
    public static final int OTP_LENGTH_GENERAL=6;
    public static final int OTP_LENGTH_FOR_HDFC_NET_BANKING=8;
    public static final long OTP_WAITING_TIME_IN_MILLISECONDS=20000;
    public static final long OTP_SUBMISSION_TIME_IN_MILLISECONDS=10000;
    public static final int CLIENT_TYPE_ID_ANDROID=0;
    public static final int ENVIRONMENT_TEST=2;
    public static final int ENVIRONMENT_UAT=1;
    public static final int ENVIRONMENT_PRODUCTION=0;
    public static final int MAX_ERROR_MESSAGE_LENGTH=4000;
}

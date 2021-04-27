package com.otpreader.apiclient.HttpResponses;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.http.PUT;

public class BankUrlDetailsHttpResponse {
    @SerializedName("BANK_NAME")
    @Expose
    private String BANK_NAME;
    @SerializedName("URL")
    @Expose
    private String URL;
    @SerializedName("IS_PAGE_TO_BE_RESPONSIVE")
    @Expose
    private Boolean IS_PAGE_TO_BE_RESPONSIVE;
    @SerializedName("IS_OTP_URL")
    @Expose
    private Boolean IS_OTP_URL;
    @SerializedName("PAGE_CUSTOMIZATION_JS_CODE")
    @Expose
    private String PAGE_CUSTOMIZATION_JS_CODE;
    @SerializedName("OTP_LENGTH")
    @Expose
    private int OTP_LENGTH;
    @SerializedName("IS_NET_BANKING_SUBMISSION_PAGE")
    @Expose
    private boolean IS_NET_BANKING_SUBMISSION_PAGE;
    @SerializedName("NET_BANKING_PAGE_SUBMISSION_JS_CODE")
    @Expose
    private String NET_BANKING_JS_SUBMISSION_CODE;
    @SerializedName("OTP_PAGE_SUBMISSION_JS_CODE")
    @Expose
    private String OTP_PAGE_SUBMISSION_JS_CODE;
    @SerializedName("IS_NETBANKING_LOGIN")
    @Expose
    private boolean IS_NETBANKING_LOGIN;
    @SerializedName("GET_JSCODE_TO_CUSTOMER_ID")
    @Expose
    private String GET_JSCODE_TO_CUSTOMER_ID;
    @SerializedName("SET_JSCODE_TO_CUSTOMER_ID")
    @Expose
    private String SET_JSCODE_TO_CUSTOMER_ID;
    @SerializedName("OTP_BANKING_SUBMISSION_JSCODE")
    @Expose
    private String OTP_BANKING_SUBMISSION_JSCODE;
    @SerializedName("IS_AUTO_OTP_SUBMIT_DISABLED")
    @Expose
    private boolean IS_AUTO_OTP_SUBMIT_DISABLED;
    @SerializedName("IS_GENERIC_OTP_FILLING_JS_CODE_DISABLED")
    @Expose
    private boolean IS_GENERIC_OTP_FILLING_JS_CODE_DISABLED;

    public String getURL() {
        return URL;
    }

    public Boolean getIsPageToBeResponsive() {
        return IS_PAGE_TO_BE_RESPONSIVE;
    }

    public Boolean getIsOtpUrl() {
        return IS_OTP_URL;
    }

    public int getOtpLength() {
        return OTP_LENGTH;
    }

    public String getPageCustomizationCode() {
        return PAGE_CUSTOMIZATION_JS_CODE;
    }

    public boolean getIsNetBankingSubmissionPage() {
        return IS_NET_BANKING_SUBMISSION_PAGE;
    }

    public String getNetBankingSubmissionJsCode() {
        return NET_BANKING_JS_SUBMISSION_CODE;
    }

    public String getOptPageSubmissionJsCode() {
        return OTP_BANKING_SUBMISSION_JSCODE;
    }

    public String getNetBankingUserIdJsCodeToSet() {
        return SET_JSCODE_TO_CUSTOMER_ID;
    }

    public String getNetBankingUserIdJsCodeToGet() {
        return GET_JSCODE_TO_CUSTOMER_ID;
    }

    public boolean isNetBankingLoginPage() {
        return IS_NETBANKING_LOGIN;
    }

    public boolean getShouldHaltAutoOtpReader() {
        return this.IS_AUTO_OTP_SUBMIT_DISABLED;
    }

    public boolean getShouldUseGenericCode() {
        return !this.IS_GENERIC_OTP_FILLING_JS_CODE_DISABLED;
    }
}
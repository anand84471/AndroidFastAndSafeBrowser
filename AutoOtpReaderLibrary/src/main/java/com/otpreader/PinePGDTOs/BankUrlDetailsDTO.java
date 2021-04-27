package com.otpreader.PinePGDTOs;

public class BankUrlDetailsDTO {
    private boolean isPageToBeResponsive,shouldStartOtpReader,isNetBankingSubmissionPage,isNetBankingLoginPage,
    shouldHaltAutoOtpSubmission, shouldUseGenericOtpSubmissionCode;
    private int otpLength=0;
    private String pageCustomizationCode,netBankingPageSubmissionJsCode,optSubmissionJsCode,getNetBankingUseridFromPageJsCode,
                   setNetBankingUserIdFromPage;

    public BankUrlDetailsDTO(boolean otpFlag, boolean pageFlag)
    {
        this.isPageToBeResponsive=pageFlag;
        this.shouldStartOtpReader=otpFlag;
    }
    public BankUrlDetailsDTO(boolean isOtpPage, boolean isPageCutsomizationRequired, int OtpLength, String PageCustomizationCode, String netBankingSubmissionCode, boolean IsNetBankingSubmissionPage,
                             String getNetBankingUserIdJsCode,String setNetBankingUserId,String OtpPageSubmissionJsCode,boolean IsNetBankingLoginPage,
                             boolean ShouldHaltAutoOtpReader,boolean ShouldUseGenericOtpSubmissionJsCode)
    {
        this.isPageToBeResponsive=isPageCutsomizationRequired;
        this.shouldStartOtpReader=isOtpPage;
        this.otpLength=OtpLength;
        this.pageCustomizationCode=PageCustomizationCode;
        this.netBankingPageSubmissionJsCode=netBankingSubmissionCode;
        this.isNetBankingSubmissionPage=IsNetBankingSubmissionPage;
        this.isNetBankingLoginPage=IsNetBankingLoginPage;
        this.getNetBankingUseridFromPageJsCode=getNetBankingUserIdJsCode;
        this.setNetBankingUserIdFromPage=setNetBankingUserId;
        this.optSubmissionJsCode=OtpPageSubmissionJsCode;
        this.shouldHaltAutoOtpSubmission=ShouldHaltAutoOtpReader;
        this.shouldUseGenericOtpSubmissionCode =ShouldUseGenericOtpSubmissionJsCode;
    }

    public boolean getIsNetBankingSubmissionPage(){ return this.isNetBankingSubmissionPage; }
    public boolean isPageToBeResponsive() { return isPageToBeResponsive; }
    public boolean shouldStartOtpReader() { return shouldStartOtpReader; }
    public String getPageCustomizationCode(){return this.pageCustomizationCode;}
    public String getNetBankingSubmissionCode(){return this.netBankingPageSubmissionJsCode;}
    public int getOtpLength(){return  this.otpLength;}
    public String getGetNetBankingUseridFromPageJsCode(){ return getNetBankingUseridFromPageJsCode;}
    public String getSetNetBankingUserIdToPage(){return setNetBankingUserIdFromPage;}
    public String getOptSubmissionJsCode(){return this.optSubmissionJsCode;}
    public boolean getShouldHaltOtpSubmission()
    {
        return this.shouldHaltAutoOtpSubmission;
    }
    public boolean getShouldUseGenericOtpSubmissionCode()
    {
        return  this.shouldUseGenericOtpSubmissionCode;
    }
}
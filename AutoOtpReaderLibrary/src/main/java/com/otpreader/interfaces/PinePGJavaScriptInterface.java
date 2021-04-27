package com.otpreader.interfaces;
import android.content.Context;
import android.webkit.JavascriptInterface;
import com.otpreader.ServiceManagers.SaveNetBankingUserName;
import java.util.HashMap;

public  class PinePGJavaScriptInterface {
    private Context context;
    private String strUserId;
    private String currentUrl;
    private SaveNetBankingUserName objSaveNetBankingUserName;
    private String jsCodeToSubmitOtp;
    private boolean shouldContinueRememberMeService=true;
    private boolean shouldGoToNextEventForNetBanking=false;
    private boolean shouldStartOtpReader=false;
    private boolean shouldHaltAutoOtpSubmission=false;
    private int otpLength=0;
    private boolean shouldHideRememberuserId=false;
    private boolean shouldHaltAutoOtpFillingCode=false;
    public PinePGJavaScriptInterface(Context context )
    {
        this.context=context;
        objSaveNetBankingUserName = new SaveNetBankingUserName(this.context);

    }
    public void setCurrentUrl(String url)
    {
        this.currentUrl=url;
    }

    @JavascriptInterface
    public void setUserId(String userId) {
        System.out.println("remember user id: current url " + currentUrl);
        //System.out.println("remember user id: user id was found " + objSaveNetBankingUserName.GetValueOfNetbankingUsername(currentUrl));
        try {
            if(shouldContinueRememberMeService) {
                this.strUserId = userId;
                HashMap<String, String> objMap = new HashMap<>();
                objMap.put(currentUrl, this.strUserId);
                objSaveNetBankingUserName.PutNetbankingUsernameDataInFile(objMap);
            }
        }
        catch (Exception ex)
        {

        }
    }

    @JavascriptInterface
    public String getUserId() {
        try {
            if(shouldContinueRememberMeService) {
                System.out.println("remember user id: current url " + currentUrl);
                System.out.println("remember user id: user id was found " + objSaveNetBankingUserName.GetValueOfNetbankingUsername(currentUrl));
                String userId=objSaveNetBankingUserName.GetValueOfNetbankingUsername(currentUrl);
                if(userId==null){
                    userId="";
                }
                return userId;
            }
        } catch (Exception Ex) {

        }
        return "";
    }
    public void stopRememberMeService()
    {
        this.shouldContinueRememberMeService=false;
    }
    public void startRememberMeService()
    {
        this.shouldContinueRememberMeService=true;
    }
    @JavascriptInterface
    public void goToNextEventForNetBanking()
    {
        this.shouldGoToNextEventForNetBanking=true;
    }
    @JavascriptInterface
    public void stopGoingToNextEventInNetBanking()
    {
        this.shouldGoToNextEventForNetBanking=false;
    }
    public boolean getShouldGoTpoNextPageForNetBanking()
    {
        return shouldGoToNextEventForNetBanking;
    }
    @JavascriptInterface
    public void startOtpReader(int otpLength)
    {
        shouldStartOtpReader=true;
        this.otpLength=otpLength;
    }
    public boolean getShouldStartOtpReader()
    {
        return shouldStartOtpReader;
    }
    public int getOtpLength()
    {
        return this.otpLength;
    }
    @JavascriptInterface
    public void setOtpSubmissionJsCode(String jsCode)
    {
        this.jsCodeToSubmitOtp=jsCode;
    }
    public String getJsCodeToSubmitOtp()
    {
        return this.jsCodeToSubmitOtp;
    }
    @JavascriptInterface
    public void hideRememberUserId()
    {
        this.shouldHideRememberuserId=true;
    }
    public boolean getShouldHideRememberUserId()
    {
        return this.shouldHideRememberuserId;
    }
    @JavascriptInterface
    public boolean shouldHaltAutoOtpSubmission()
    {
        return shouldHaltAutoOtpSubmission;
    }
    @JavascriptInterface
    public void haltAutoOtpSubmission()
    {
        this.shouldHaltAutoOtpSubmission=true;
    }

}

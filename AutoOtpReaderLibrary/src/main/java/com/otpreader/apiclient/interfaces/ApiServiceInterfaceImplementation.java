package com.otpreader.apiclient.interfaces;


import android.telecom.StatusHints;
import android.util.Log;

import com.otpreader.apiclient.HttpRequests.HttpRequestForReportJsError;
import com.otpreader.PinePGDTOs.BankUrlDetailsDTO;
import com.otpreader.apiclient.HttpRequests.BrowserSessionDetailsHttpRequest;
import com.otpreader.apiclient.MainApiClient;
import com.otpreader.PinePGDTOs.BrowserSessionDTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.otpreader.Constants.PinePGConfigConstant.CLIENT_TYPE_ID_ANDROID;

public class ApiServiceInterfaceImplementation implements ApiServiceInterface {
    private String url;
    private String TAG = "HandleBankUrlResponses";
    MainApiClient objMainApiClient = null;
    Map<String, BankUrlDetailsDTO> mapAllBankUrlsDetails = null;
    int environment;

    public ApiServiceInterfaceImplementation(int environment) {
        this.environment=environment;
    }
    public void callInsertBrowserSessionDetailsApi(BrowserSessionDTO objBrowserSessionDTODetails)
    {
        try {
            if(objMainApiClient==null){
                objMainApiClient=new MainApiClient(this.environment);

            }
            objMainApiClient.insertBrowserSessionDetails(convertToBrowserSessionDetailsDTO(objBrowserSessionDTODetails));
        }
        catch (Exception ex)
        {
            Log.e(TAG,"error occurred while inserting browser session details");
        }
    }
    private BrowserSessionDetailsHttpRequest convertToBrowserSessionDetailsDTO(BrowserSessionDTO objBrowserSessionDTO)
    {
        BrowserSessionDetailsHttpRequest objResult=null;
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objBrowserSessionDTO.browserSessionFinishTime=new Date();
            objResult=new BrowserSessionDetailsHttpRequest();
            objResult.setiMerchantId(objBrowserSessionDTO.iMerchantId);
//            objResult.setDtBrowserCloseTime(objBrowserSessionDTO.browserSessionFinishTime.toString());
//            objResult.setDtBrowserStartTime(objBrowserSessionDTO.browserSessionStatTime.toString());
            objResult.setStrTransactionId(objBrowserSessionDTO.strTransactionId);

            objResult.strCustomerId=objBrowserSessionDTO.strCustomerId;
            if(objResult.strCustomerId==null)
            {
                objResult.strCustomerId="customer id";
            }
            objResult.setStrCustomerEmail(objBrowserSessionDTO.strCustomerEmail);
            objResult.setStrCustomerPhoneNo(objBrowserSessionDTO.strCustomerPhoneNo);
            objResult.setHaveSmsPermission(objBrowserSessionDTO.HaveSmsPermission);
            objResult.setOtpDetected(objBrowserSessionDTO.IsOtpDetected);
            objResult.setIsBackPressed(objBrowserSessionDTO.IsBackPressed);
            objResult.setIsFetchedUrlDetails(objBrowserSessionDTO.IsFetchedUrlDetails);
            objResult.setStrClientId(String.valueOf(CLIENT_TYPE_ID_ANDROID));
            objResult.setStrCustomParameters(objBrowserSessionDTO.strCustomParameters);
            objResult.setiErrorCode(objBrowserSessionDTO.iErrorCode);
            objResult.setStrErrorMessage(objBrowserSessionDTO.strErrorMessage);
            objResult.setStrLastVisitedUrl(objBrowserSessionDTO.strLastVisitedUrl);
            objResult.setTransactionCompleted(objBrowserSessionDTO.IsTransactionCompleted);
            objResult.setStrPaymentStartUrl(objBrowserSessionDTO.strPaymentStartUrl);
            objResult.setStrPaymentRemarks(objBrowserSessionDTO.strPaymentRemarks);
            objResult.setOtpApproved(objBrowserSessionDTO.IsOtpApproved);
            objResult.setThemeId(objBrowserSessionDTO.iThemeId);
            objResult.setLanguageId(objBrowserSessionDTO.iLanguageId);
            objResult.setOrderId(objBrowserSessionDTO.strOrderId);
            objResult.setStrPaymentReturnUrls(objBrowserSessionDTO.strReturnUrls);
            objResult.setDtBrowserStartTime(dateFormat.format(objBrowserSessionDTO.browserSessionStatTime));
            objResult.setDtBrowserCloseTime(dateFormat.format(objBrowserSessionDTO.browserSessionFinishTime));

        }
        catch (Exception ex)
        {
            Log.e(TAG,ex.getMessage());
        }
        return objResult;
    }

    public void callGetAllBankUrlsDetailsApi() {
        try {
            if(objMainApiClient==null){
                objMainApiClient=new MainApiClient(this.environment);
                objMainApiClient.getUrlDetails();
            }
            if (mapAllBankUrlsDetails == null) {
                mapAllBankUrlsDetails = objMainApiClient.getAllBankUrlDetails();
            }
        } catch (Exception e) {
            Log.e(TAG, "Not able to fetch all bank urls from server", e);
        }
    }
    public BankUrlDetailsDTO getUrlDetailsObject(String url) {
        BankUrlDetailsDTO objResult=null;
        try {
            if (mapAllBankUrlsDetails == null) {
                callGetAllBankUrlsDetailsApi();
            }
            String desiredUrl="";
            if (mapAllBankUrlsDetails != null) {
                for (String urls : mapAllBankUrlsDetails.keySet()) {
                    if (url.contains(urls)) {
                        if(desiredUrl.length()<urls.length())
                        {
                            desiredUrl=urls;
                        }
                    }
                }
            }
            if(!desiredUrl.equals(""))
            {
                objResult= mapAllBankUrlsDetails.get(desiredUrl);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error occurred while matching url", ex);
        }
        return objResult;
    }

    public boolean shouldStartOtpReader(String url) {
        try {
            if (mapAllBankUrlsDetails == null) {
                return false;
            }
            return mapAllBankUrlsDetails.containsKey(url) && Objects.requireNonNull(mapAllBankUrlsDetails.get(url)).shouldStartOtpReader();
        } catch (Exception e) {
            Log.i(TAG,url+" is not a bank otp page");
        }
        return false;
    }
    public int getOtpLength(String url)
    {
        int result=0;
        try {
            if (mapAllBankUrlsDetails == null) {
                result=0;
            }
            else if(mapAllBankUrlsDetails.containsKey(url))
            {
                result=Objects.requireNonNull(mapAllBankUrlsDetails.get(url)).getOtpLength();
            }
        } catch (Exception e) {
            Log.i(TAG,url+" is not a bank otp page");
        }
        return result;
    }
    public boolean shouldStartNetBankingSubmissionBottomSheet(String url)
    {
        return  mapAllBankUrlsDetails.containsKey(url)&& Objects.requireNonNull(mapAllBankUrlsDetails.get(url)).getIsNetBankingSubmissionPage();
    }
    public boolean IsFetchedUrlDetails()
    {
        return mapAllBankUrlsDetails!=null;
    }
}

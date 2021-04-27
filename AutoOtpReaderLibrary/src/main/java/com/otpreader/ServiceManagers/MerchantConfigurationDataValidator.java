package com.otpreader.ServiceManagers;

import android.os.Bundle;

import com.otpreader.Constants.MerchantConfigurationConstants;
import com.otpreader.Constants.PinePGConstants;

import java.util.ArrayList;

public class MerchantConfigurationDataValidator {
    private boolean status;
    private ArrayList<String> errorMessages;
    private Bundle configurationData;
    private int iEnvironment;
    public MerchantConfigurationDataValidator(Bundle configurationData)
    {
        this.configurationData=configurationData;
        this.errorMessages=new ArrayList<>();
        validateEnvironment();
        validatePostData();
        validateReturnUrl();
    }
    public boolean getValidationStatus()
    {
        return this.status;
    }
    public String getValidationMessage()
    {
        String response="";
        for (String message:this.errorMessages) {
            response+=" "+message;
        }
        return response;
    }
    private void validateEnvironment()
    {
        this.status=false;
        if(configurationData.getInt(MerchantConfigurationConstants.ENVIRONMENT_KEY,-1)!=-1)
        {
            try{
                this.status=true;
                this.iEnvironment=configurationData.getInt(MerchantConfigurationConstants.ENVIRONMENT_KEY,-1);
            }
            catch (NumberFormatException ex)
            {
                this.errorMessages.add(PinePGConstants.PinePGErrorCodeVsMessageMap.get(PinePGConstants.PINE_PG_INVALID_ENVIRONMENT_SELECTION));
            }
        }
        else {
            errorMessages.add(PinePGConstants.PinePGErrorCodeVsMessageMap.get(PinePGConstants.PINE_PG_INVALID_ENVIRONMENT_SELECTION));
        }
    }
    private void validateReturnUrl()
    {
        this.status=false;
        if(configurationData.getStringArrayList(MerchantConfigurationConstants.RETURN_URLS_KEY)!=null)
        {
            try{
               if(configurationData.getStringArrayList(MerchantConfigurationConstants.RETURN_URLS_KEY).size()>0){
                   this.status=true;
               }
            }
            catch (NumberFormatException ex)
            {
                this.errorMessages.add(PinePGConstants.PinePGErrorCodeVsMessageMap.get(PinePGConstants.PINE_PG_INVALID_RETURN_URL));
            }
        }
        else {
            errorMessages.add(PinePGConstants.PinePGErrorCodeVsMessageMap.get(PinePGConstants.PINE_PG_INVALID_RETURN_URL));
        }
    }
    private void validatePostData()
    {
        this.status=false;
        if(configurationData.getString(MerchantConfigurationConstants.POST_DATA)!=null)
        {
            this.status=true;
        }
        else {
            errorMessages.add(PinePGConstants.PinePGErrorCodeVsMessageMap.get(PinePGConstants.PINE_PG_INVALID_POST_DATA));
        }
    }
    public  int getiEnvironment()
    {
        return this.iEnvironment;
    }
}

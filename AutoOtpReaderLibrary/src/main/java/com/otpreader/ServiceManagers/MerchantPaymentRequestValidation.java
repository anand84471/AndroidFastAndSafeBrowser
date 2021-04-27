package com.otpreader.ServiceManagers;

import android.os.Bundle;

import com.otpreader.Constants.MerchantConfigurationConstants;
import com.otpreader.Constants.PinePGConstants;

import java.util.ArrayList;
import java.util.Objects;

public class MerchantPaymentRequestValidation {
    private Bundle bundleResponseData;
    private ArrayList<String> messages;
    private boolean status;
    private int iMerchantId;
    public MerchantPaymentRequestValidation(Bundle bundleResponseData)
    {
        this.bundleResponseData=bundleResponseData;
        messages=new ArrayList<String>(){};
        validateMerchantId();
        validateThemeId();
        validateTransactionId();
    }
    public boolean getValidationStatus()
    {
        return this.status;
    }
    public String getValidationMessage()
    {
        String result="";
        for (String message:this.messages) {
            result +=" "+message;
        }
        return result;
    }
    private void validateMerchantId()
    {
        this.status=false;
        if(bundleResponseData.getString(MerchantConfigurationConstants.MERCHANT_ID_KEY)!=null)
        {
           try{
               this.iMerchantId=Integer.parseInt(Objects.requireNonNull(bundleResponseData.getString(MerchantConfigurationConstants.MERCHANT_ID_KEY)));
               this.status=true;
           }
           catch (NumberFormatException ex)
           {
               this.messages.add(PinePGConstants.PinePGErrorCodeVsMessageMap.get(PinePGConstants.PINE_PG_MERCHANT_ID_IS_INVALID));
           }
        }
        else {
            messages.add(PinePGConstants.PinePGErrorCodeVsMessageMap.get(PinePGConstants.PINE_PG_MERCHANT_ID_IS_MISSING));
        }
    }
    private void validateTransactionId()
    {
        this.status=false;
        if(bundleResponseData.getString(MerchantConfigurationConstants.TRANSACTION_ID_KEY)!=null)
        {
            this.status=true;
        }
        else {
            messages.add(PinePGConstants.PinePGErrorCodeVsMessageMap.get(PinePGConstants.PINE_PG_INVALID_TRANSACTION_ID));
        }
    }
    private void validateThemeId()
    {
        this.status=false;
        if(bundleResponseData.getString(MerchantConfigurationConstants.THEME_ID_KEY)!=null)
        {
            try{
                this.status=true;
                Integer.parseInt(Objects.requireNonNull(bundleResponseData.getString(MerchantConfigurationConstants.THEME_ID_KEY)));
            }
            catch (NumberFormatException ex)
            {
                this.messages.add(PinePGConstants.PinePGErrorCodeVsMessageMap.get(PinePGConstants.PINE_PG_INVALID_THEME_ID));
            }
        }
        else {
            messages.add(PinePGConstants.PinePGErrorCodeVsMessageMap.get(PinePGConstants.PINE_PG_INVALID_THEME_ID));
        }
    }
    public int getMerchantId()
    {
        return this.iMerchantId;
    }
}

package com.otpreader.apiclient.interfaces;

interface ApiServiceInterface {
     boolean shouldStartOtpReader(String url);
     void callGetAllBankUrlsDetailsApi();
}

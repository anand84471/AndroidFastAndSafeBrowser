package com.otpreader.apiclient.interfaces;

import com.otpreader.apiclient.HttpRequests.BrowserSessionDetailsHttpRequest;
import com.otpreader.apiclient.HttpRequests.ClientTypeHttpRequest;
import com.otpreader.apiclient.HttpRequests.HttpRequestForReportJsError;
import com.otpreader.apiclient.HttpRequests.PaymentStatusChangeRequest;
import com.otpreader.apiclient.HttpResponses.BankUrlDetailApiResponse;
import com.otpreader.apiclient.HttpResponses.BankUrlDetailsResponseV2;
import com.otpreader.apiclient.HttpResponses.DefaultApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("/api/mobile/bank/urls")
    Call<BankUrlDetailApiResponse> callApiToGetAllBankUrlDetails(@Body ClientTypeHttpRequest objClientTypeHttpRequest);
    @POST("/api/mobile/bank/urls/InsertSdkBrowserDetails")
    Call<DefaultApiResponse> callApiToInsertBrowserSessionDetails(@Body BrowserSessionDetailsHttpRequest objBrowserSessionDetailsHttpRequest);
    @POST("/api/mobile/bank/urls/ReportJsError")
    Call<DefaultApiResponse> callApiToReportJsError(@Body HttpRequestForReportJsError objHttpRequestForReportJsError);
    @POST("/api/mobile/v2/bank/urls")
    Call<BankUrlDetailsResponseV2> callBankUrlDetailsApiV2(@Body ClientTypeHttpRequest  objClientTypeHttpRequest);
    @POST("/api/mobile/bank/urls/ReportTransactionStatus")
    Call<DefaultApiResponse> callReportTransactionStatus(@Body PaymentStatusChangeRequest paymentStatusChangeRequest);
}
package com.otpreader.apiclient;
import android.os.AsyncTask;
import android.util.Log;
import com.otpreader.Constants.PinePGConfigConstant;
import com.otpreader.PinePGDTOs.BankUrlDetailsDTO;
import com.otpreader.apiclient.HttpRequests.BrowserSessionDetailsHttpRequest;
import com.otpreader.apiclient.HttpRequests.HttpRequestForReportJsError;
import com.otpreader.apiclient.HttpRequests.PaymentStatusChangeRequest;
import com.otpreader.apiclient.HttpResponses.BankUrlDetailsHttpResponse;
import com.otpreader.apiclient.HttpResponses.BankUrlDetailsResponseV2;
import com.otpreader.apiclient.HttpResponses.DefaultApiResponse;
import com.otpreader.apiclient.HttpResponses.BankUrlDetailApiResponse;
import com.otpreader.apiclient.HttpRequests.ClientTypeHttpRequest;
import com.otpreader.apiclient.interfaces.RetrofitInterface;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.otpreader.Constants.PinePGConfigConstant.CLIENT_TYPE_ID_ANDROID;
import static com.otpreader.Constants.PinePGConfigConstant.ENVIRONMENT_TEST;

public class MainApiClient {
    private String TAG="MainApiClient";
    private  Map<String, BankUrlDetailsDTO> bankUrlMap=null;
    private BrowserSessionDetailsHttpRequest objBrowserSessionDetailsHttpRequest;
    private Call<BankUrlDetailApiResponse> call;
    private Call<DefaultApiResponse> callForInsertSessionDetails;
    private ClientTypeHttpRequest objClientTypeId=null;
    private Call<BankUrlDetailsResponseV2> bankUrlDetailsResponseV2Call;
    private Call<DefaultApiResponse> callForReportTxnStatus;
    private int environment;
    private HttpRequestForReportJsError objHttpRequestForReportJsError=null;
    private PaymentStatusChangeRequest paymentStatusChangeRequest;
    protected class  GetAllBankUrls extends AsyncTask<Integer,Integer,Void>
    {
        @Override
        protected Void doInBackground(Integer... integers) {
            try {
                RetrofitInterface apiInterface;
                BankUrlCall.setBaseUrl(integers[0]);
                apiInterface = BankUrlCall.getRetrofitClient().create(RetrofitInterface.class);
                objClientTypeId=new ClientTypeHttpRequest();
                objClientTypeId.iClientType=CLIENT_TYPE_ID_ANDROID;
                objClientTypeId.IsRequiredNetBankingDetails=true;
                call = apiInterface.callApiToGetAllBankUrlDetails(objClientTypeId);
                bankUrlMap=new HashMap<>();
                if (call != null) {
                    call.enqueue(new Callback<BankUrlDetailApiResponse>() {
                        @Override
                        public void onResponse(Call<BankUrlDetailApiResponse> call, Response<BankUrlDetailApiResponse> response) {
                            if(response!=null&&response.body()!=null) {
                                BankUrlDetailApiResponse objBankUrlDetailApiResponse;
                                objBankUrlDetailApiResponse = response.body();
                                List<BankUrlDetailsHttpResponse> lsBankUrlServiceNew = objBankUrlDetailApiResponse.getUrlDetails();
                                BankUrlDetailsHttpResponse objBankUrlServiceNew;
                                BankUrlDetailsDTO objBankUrlDetailsDTO;
                                System.out.println(response.body());
                                System.out.println("Bank urls found");
                                if (lsBankUrlServiceNew != null && lsBankUrlServiceNew.size() > 0) {
                                    for (int i = 0; i < lsBankUrlServiceNew.size(); i++) {
                                        objBankUrlServiceNew = lsBankUrlServiceNew.get(i);
                                        objBankUrlDetailsDTO = new BankUrlDetailsDTO(objBankUrlServiceNew.getIsOtpUrl(),
                                                objBankUrlServiceNew.getIsPageToBeResponsive(),
                                                objBankUrlServiceNew.getOtpLength(),
                                                objBankUrlServiceNew.getPageCustomizationCode(),
                                                objBankUrlServiceNew.getNetBankingSubmissionJsCode(),
                                                objBankUrlServiceNew.getIsNetBankingSubmissionPage(),
                                                objBankUrlServiceNew.getNetBankingUserIdJsCodeToGet(),
                                                objBankUrlServiceNew.getNetBankingUserIdJsCodeToSet(),
                                                objBankUrlServiceNew.getOptPageSubmissionJsCode(),
                                                objBankUrlServiceNew.isNetBankingLoginPage(),
                                                objBankUrlServiceNew.getShouldHaltAutoOtpReader(),
                                                objBankUrlServiceNew.getShouldUseGenericCode());
                                        bankUrlMap.put(objBankUrlServiceNew.getURL(), objBankUrlDetailsDTO);
                                    }
                                    bankUrlMap.put("https://test.pinepg.in:7033/HDFCSimulator/ACS1", new BankUrlDetailsDTO(true, true));
                                    //Should not start Any functionality related to OTP Reader.
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<BankUrlDetailApiResponse> call, Throwable t) {
                            //bankUrlMap = null;
                            Log.e(TAG, "Can not get bank Urls", t);
                            //Should not start Any functionality related to OTP Reader.
                        }
                    });
                }
            }
            catch (Exception e)
            {
                Log.e(TAG,"Can not get Bank Urls as an Exception has occurred", e);
                //Should not start Any functionality related to OTP Reader.
            }
            return null;
        }
    }
    public void insertBrowserSessionDetails(BrowserSessionDetailsHttpRequest objBrowserSessionDetailsHttpRequest)
    {
        this.objBrowserSessionDetailsHttpRequest = objBrowserSessionDetailsHttpRequest;
        new InsertBrowserSession().execute();
    }
    public void reportJsError(HttpRequestForReportJsError objHttpRequestForReportJsError)
    {
        this.objHttpRequestForReportJsError=objHttpRequestForReportJsError;
        new ReportJsError().execute();
    }
    public void reportTransactionStatus(PaymentStatusChangeRequest objPaymentStatusChangeRequest)
    {
        this.paymentStatusChangeRequest=objPaymentStatusChangeRequest;
        new ReportTransactionStatus().execute();
    }
    protected class  ReportTransactionStatus extends AsyncTask<Integer,Integer,Void>
    {
        @Override
        protected Void doInBackground(Integer... integers) {
            try {
                RetrofitInterface apiInterface;
                BankUrlCall.setBaseUrl(environment);
                apiInterface = BankUrlCall.getRetrofitClient().create(RetrofitInterface.class);
                callForReportTxnStatus = apiInterface.callReportTransactionStatus(paymentStatusChangeRequest);
                if (callForReportTxnStatus != null) {
                    callForReportTxnStatus.enqueue(new Callback<DefaultApiResponse>() {
                        @Override
                        public void onResponse(Call<DefaultApiResponse> call, Response<DefaultApiResponse> response) {
                            if(response!=null) {
                                System.out.println("Report txn status api finish with response message "+response.body().getResponseMessage()+"and response code"+response.body().getResponseCode());
                                if(response.body()!=null) {
                                    response.body().getResponseCode();
                                }
                            }
                            else {
                                Log.e(TAG,"Report txn status response is null");
                            }
                        }
                        @Override
                        public void onFailure(Call<DefaultApiResponse> call, Throwable t) {
                            //bankUrlMap = null;
                            Log.e(TAG, "Can not be inserted browser session details. "+t.getMessage(), null);
                            //Should not start Any functionality related to OTP Reader.
                        }
                    });
                }
                else {
                    Log.e(TAG,"Can not be inserted browser session details");
                }
            }
            catch (Exception e)
            {
                Log.e(TAG,"Can not be inserted browser session details"+e.getMessage(), e);
            }
            return null;
        }
    }
    protected class  InsertBrowserSession extends AsyncTask<Integer,Integer,Void>
    {
        @Override
        protected Void doInBackground(Integer... integers) {
            try {
                RetrofitInterface apiInterface;
                BankUrlCall.setBaseUrl(environment);
                apiInterface = BankUrlCall.getRetrofitClient().create(RetrofitInterface.class);
                callForInsertSessionDetails = apiInterface.callApiToInsertBrowserSessionDetails(objBrowserSessionDetailsHttpRequest);
                if (callForInsertSessionDetails != null) {
                    callForInsertSessionDetails.enqueue(new Callback<DefaultApiResponse>() {
                        @Override
                        public void onResponse(Call<DefaultApiResponse> call, Response<DefaultApiResponse> response) {
                            if(response!=null) {
                                System.out.println("Browser session api insert finish with response message "+response.message());
                                if(response.body()!=null) {
                                    response.body().getResponseCode();
                                    System.out.println("Browser session api insert finish with response message " + response.body().getResponseMessage()+response.body()+response.body().getResponseCode());
                                }
                            }
                            else {
                                Log.e(TAG,"browser session response is null");
                            }
                        }
                        @Override
                        public void onFailure(Call<DefaultApiResponse> call, Throwable t) {
                            Log.e(TAG, "Can not be inserted browser session details. "+t.getMessage(), null);
                        }
                    });
                }
                else {
                    Log.e(TAG,"Can not be inserted browser session details");
                }
            }
            catch (Exception e)
            {
                Log.e(TAG,"Can not be inserted browser session details"+e.getMessage(), e);
                //Should not start Any functionality related to OTP Reader.
            }
            return null;
        }
    }
    protected class  ReportJsError extends AsyncTask<Integer,Integer,Void>
    {
        @Override
        protected Void doInBackground(Integer... integers) {
            try {
                RetrofitInterface apiInterface;
                BankUrlCall.setBaseUrl(environment);
                apiInterface = BankUrlCall.getRetrofitClient().create(RetrofitInterface.class);
                callForInsertSessionDetails = apiInterface.callApiToReportJsError(objHttpRequestForReportJsError);
                if (callForInsertSessionDetails != null) {
                    callForInsertSessionDetails.enqueue(new Callback<DefaultApiResponse>() {
                        @Override
                        public void onResponse(Call<DefaultApiResponse> call, Response<DefaultApiResponse> response) {
                            if(response!=null) {
                                System.out.println("Browser session api insert finish with response message "+response.message());
                                if(response.body()!=null) {
                                    response.body().getResponseCode();
                                }
                                //objDefaultApiResponse.getResponseCode();
                                System.out.println("Browser session api insert finish with response message "+response.body().getResponseMessage());
                            }
                            else {
                                Log.e(TAG,"browser session response is null");
                            }
                        }
                        @Override
                        public void onFailure(Call<DefaultApiResponse> call, Throwable t) {
                            //bankUrlMap = null;
                            Log.e(TAG, "Can not be inserted browser session details. "+t.getMessage(), null);
                            //Should not start Any functionality related to OTP Reader.
                        }
                    });
                }
                else {
                    Log.e(TAG,"Can not be inserted browser session details");
                }
            }
            catch (Exception e)
            {
                Log.e(TAG,"Can not be inserted browser session details"+e.getMessage(), e);
                //Should not start Any functionality related to OTP Reader.
            }
            return null;
        }
    }

    public MainApiClient(int environment)
    {
        this.environment=environment;
    }
    public void getUrlDetails()
    {
        new getAllBankUrlsV2().execute(environment);
    }
    public Map<String , BankUrlDetailsDTO> getAllBankUrlDetails()
    {
        return bankUrlMap;
    }
    protected class  getAllBankUrlsV2 extends AsyncTask<Integer,Integer,Void>
    {
        @Override
        protected Void doInBackground(Integer... integers) {
            try {
                RetrofitInterface apiInterface;
                BankUrlCall.setBaseUrl(integers[0]);
                apiInterface = BankUrlCall.getRetrofitClient().create(RetrofitInterface.class);
                objClientTypeId=new ClientTypeHttpRequest();
                objClientTypeId.iClientType=CLIENT_TYPE_ID_ANDROID;
                objClientTypeId.IsRequiredNetBankingDetails=true;
                bankUrlDetailsResponseV2Call = apiInterface.callBankUrlDetailsApiV2(objClientTypeId);
                bankUrlMap=new HashMap<>();
                if (bankUrlDetailsResponseV2Call != null) {
                    bankUrlDetailsResponseV2Call.enqueue(new Callback<BankUrlDetailsResponseV2>() {
                        @Override
                        public void onResponse(Call<BankUrlDetailsResponseV2> call, Response<BankUrlDetailsResponseV2> response) {
                            if(response!=null&&response.body()!=null) {
                                BankUrlDetailsResponseV2 objBankUrlDetailApiResponse;
                                objBankUrlDetailApiResponse = response.body();
                                List<BankUrlDetailsHttpResponse> lsBankUrlServiceNew = null;
                                try {
                                    lsBankUrlServiceNew = objBankUrlDetailApiResponse.getListBankUrlDetails();
                                } catch (Exception e) {
                                    System.out.println("Execption occurred while decompressing class");
                                    e.printStackTrace();
                                }
                                BankUrlDetailsHttpResponse objBankUrlServiceNew;
                                BankUrlDetailsDTO objBankUrlDetailsDTO;
                                System.out.println(response.body());
                                System.out.println("Bank urls found");
                                if (lsBankUrlServiceNew != null && lsBankUrlServiceNew.size() > 0) {
                                    for (int i = 0; i < lsBankUrlServiceNew.size(); i++) {
                                        objBankUrlServiceNew = lsBankUrlServiceNew.get(i);
                                        objBankUrlDetailsDTO = new BankUrlDetailsDTO(objBankUrlServiceNew.getIsOtpUrl(),
                                                objBankUrlServiceNew.getIsPageToBeResponsive(),
                                                objBankUrlServiceNew.getOtpLength(),
                                                objBankUrlServiceNew.getPageCustomizationCode(),
                                                objBankUrlServiceNew.getNetBankingSubmissionJsCode(),
                                                objBankUrlServiceNew.getIsNetBankingSubmissionPage(),
                                                objBankUrlServiceNew.getNetBankingUserIdJsCodeToGet(),
                                                objBankUrlServiceNew.getNetBankingUserIdJsCodeToSet(),
                                                objBankUrlServiceNew.getOptPageSubmissionJsCode(),
                                                objBankUrlServiceNew.isNetBankingLoginPage(),
                                                objBankUrlServiceNew.getShouldHaltAutoOtpReader(),
                                                objBankUrlServiceNew.getShouldUseGenericCode());
                                        bankUrlMap.put(objBankUrlServiceNew.getURL(), objBankUrlDetailsDTO);
                                    }
                                    bankUrlMap.put("https://test.pinepg.in:7033/HDFCSimulator/ACS1", new BankUrlDetailsDTO(true, true));
                                    //Should not start Any functionality related to OTP Reader.
                                }
                            }
                        }



                        @Override
                        public void onFailure(Call<BankUrlDetailsResponseV2> call, Throwable t) {
                            //bankUrlMap = null;
                            Log.e(TAG, "Can not get bank Urls", t);
                            //Should not start Any functionality related to OTP Reader.
                        }
                    });
                }
            }
            catch (Exception e)
            {
                Log.e(TAG,"Can not get Bank Urls as an Exception has occurred", e);
            }
            return null;
        }
    }
}
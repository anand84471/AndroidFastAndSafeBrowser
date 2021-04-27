package com.otpreader.ServiceManagers;
import android.os.Build;
import com.otpreader.apiclient.HttpRequests.HttpRequestForReportJsError;
import com.otpreader.apiclient.MainApiClient;

public class ReportJsErrorService {
    MainApiClient objMainApiClient;
    HttpRequestForReportJsError objHttpRequestForReportJsError;

    public String strUrl;

    public String strJsExceptionMessage;

    public String strExecutedJsCode;

    public String strHtmlOfCurrentUrl;

    public  int iJsCodeTypeId;

    private int iClientTypeId;

    private String strClientVersion;
    public ReportJsErrorService(int environment)
    {
        objMainApiClient=new MainApiClient(environment);
        iClientTypeId=0;
        strClientVersion= Build.VERSION.RELEASE;
    }
    public void reportJsErrorHasOccurred(String url,String exceptionMessage,String executedJsCode,String html,
                                         int JsCodeTypeId)
    {
        try
        {
            objHttpRequestForReportJsError=new HttpRequestForReportJsError();
            objHttpRequestForReportJsError.strUrl=url;
            objHttpRequestForReportJsError.iClientTypeId=iClientTypeId;
            objHttpRequestForReportJsError.iJsCodeTypeId=JsCodeTypeId;
            objHttpRequestForReportJsError.strClientVersion=strClientVersion;
            objHttpRequestForReportJsError.strExecutedJsCode=executedJsCode;
            objHttpRequestForReportJsError.strJsExceptionMessage=exceptionMessage;
            objHttpRequestForReportJsError.strHtmlOfCurrentUrl=html;
            objMainApiClient.reportJsError(objHttpRequestForReportJsError);
        }
        catch (Exception ex)
        {

        }
    }
}


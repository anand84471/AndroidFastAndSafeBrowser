package com.otpreader.apiclient;

import com.otpreader.Constants.PinePGConfigConstant;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class BankUrlCall {
    private static Retrofit retrofit = null;
    private static String BASE_URL = null;
    public static void setBaseUrl(int environment) {
        if (environment == 0) {
            BASE_URL = PinePGConfigConstant.PINE_PG_PAYMENT_URL_UAT_BASE;
        } else if (environment == 1) {
            BASE_URL = PinePGConfigConstant.PINE_PG_PAYMENT_URL_PRODUCTION_BASE;
        } else if(environment==2){
            BASE_URL = PinePGConfigConstant.PINE_PG_PAYMENT_URL_TEST_SANDBOX_BASE;
        }
    }
    public static OkHttpClient getUnsafeOkHttpClient() {

    try {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient = okHttpClient.newBuilder()
                .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();

        return okHttpClient;
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }
    static Retrofit getRetrofitClient() {
        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            if (retrofit == null && BASE_URL != null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(getUnsafeOkHttpClient())
                        .build();
            }
            return retrofit;
        }
        catch (Exception ex)
        {

        }
        return  null;
    }
}

package com.otpreader.apiclient.HttpResponses;

import android.util.Base64;
import android.util.Base64InputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class BankUrlDetailsResponseV2 {
    @SerializedName("url_data")
    @Expose
    private String lsBankUrlDetails;
    @SerializedName("response_code")
    @Expose
    private Integer iResponseCode;

    @SerializedName("response_message")
    @Expose
    private String strResponseMessage;
    public String getUrlDetails()
    {
        return lsBankUrlDetails;
    }
    private List<BankUrlDetailsHttpResponse> listBankUrlDetails;
    public List<BankUrlDetailsHttpResponse> getListBankUrlDetails() throws Exception {
        String base64Encoded=lsBankUrlDetails;
        String result=decompress(Base64.decode(base64Encoded,Base64.DEFAULT));
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        listBankUrlDetails = gson.fromJson(result, new TypeToken<List<BankUrlDetailsHttpResponse>>(){}.getType());
        return listBankUrlDetails;
    }


    public  String decompress(byte[] compressed) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(bis);
        BufferedReader br = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        gis.close();
        bis.close();
        return sb.toString();
    }



}
package com.otpreader.ServiceManagers;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SaveNetBankingUserName {
    private Context context;
    public SaveNetBankingUserName(Context context)
    {
        this.context=context;
    }
    private void WriteFileData(Map<String, String> map) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            File file = new File(context.getFilesDir(), "SaveUsernameGson.txt");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fileWriter);
            out.write(json);
            out.close();
        } catch (Exception e) {
            Log.e("Error", "Error Ocurring while Writing ");
            Log.e("error", String.valueOf(e.getStackTrace()));
            Log.e("error", e.getMessage());
        }
    }
    private String ReadFileData() {
        String gsonData = null;
        try {
            File file = new File(context.getFilesDir(), "SaveUsernameGson.txt");
            if(file.exists()) {
                FileReader fileReader = new FileReader(file);
                BufferedReader in = new BufferedReader(fileReader);
                gsonData = in.readLine();
            }
        } catch (Exception e) {
            Log.e("Error", "Error Occurring while Reading File ");
            Log.e("error", String.valueOf(e.getStackTrace()));
            Log.e("error", e.getMessage());
        }
        return gsonData;
    }

    public String GetValueOfNetbankingUsername(String Key) {
        String Novalue = "";
        String userName="";
        String gsonData = null;
        gsonData = ReadFileData();
        try {
            if (gsonData != null) {
                Gson gson = new Gson();
                Type stringStringMap = new TypeToken<Map<String, String>>() {
                }.getType();
                Map<String, String> uslVsUserIdMap = gson.fromJson(gsonData, stringStringMap);
                userName = uslVsUserIdMap.get(Key);
            }
            else
            {
                return  Novalue;
            }
        }catch (Exception e) {
            Log.e("IOError", "Error Ocurring while Reading File ");
            Log.e("error", String.valueOf(e.getStackTrace()));
            Log.e("error", e.getMessage());
        }
        return userName;
    }

    public boolean PutNetbankingUsernameDataInFile(HashMap<String, String> map_Usrname) {
        String gsonData = null;
        boolean status = false;
        Map<String, String> map_ReadData = null;
        gsonData = ReadFileData();
        try {
            if (gsonData != null) {
                Gson gson = new Gson();
                Type stringStringMap = new TypeToken<Map<String, String>>() {
                }.getType();
                map_ReadData = gson.fromJson(gsonData, stringStringMap);
                if (map_ReadData != null) {
                    String key = (String) map_Usrname.keySet().toArray()[0];
                    String value = (String) map_Usrname.values().toArray()[0];
                    map_ReadData.put(key,value);
                        WriteFileData(map_ReadData);
                        status = true;
                    }
            }
            else {
                WriteFileData(map_Usrname);
                status = true;
            }
        } catch (Exception e) {
            Log.e("Error", "Error Occurred while Writing to File ");
            Log.e("error", String.valueOf(e.getStackTrace()));
            Log.e("error", e.getMessage());
            status = false;
        }
        return status;
    }
}

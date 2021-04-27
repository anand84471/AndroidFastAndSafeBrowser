package com.otpreader.AppPermissionHandler;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.otpreader.Constants.PinePGConstants.MY_PERMISSIONS_REQUEST_RECEIVE_SMS;


public class SmsPermission extends AppCompatActivity {
    private boolean smsPermissionResult = false;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.RECEIVE_SMS
    };
    int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askForPermissions();
    }
    public boolean getSmsPermissionResult() {
        askForPermissions();
        return smsPermissionResult;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        //will check the requestCode
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS: {
                //check whether the length of grantResults is greater than 0 and is equal to PERMISSION_GRANTED
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Now broadcastreceiver will work in background
                    Toast.makeText(this, "Thank you for permitting!", Toast.LENGTH_LONG).show();
                    smsPermissionResult = true;
                } else {
                    Toast.makeText(this, "Well I can't do anything until you permit me", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void askForPermissions() {
        if (!hasPermissions(PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
    public boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
                else {
                    if(permission.equals(Manifest.permission.RECEIVE_SMS)&&ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED)
                    {
                        smsPermissionResult=true;
                    }
                }
            }
        }
        return true;
    }
}
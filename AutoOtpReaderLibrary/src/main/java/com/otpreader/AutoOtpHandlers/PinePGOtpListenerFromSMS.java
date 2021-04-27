package com.otpreader.AutoOtpHandlers;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import static com.otpreader.Constants.PinePGConfigConstant.SMS_RECEIVED;

public class PinePGOtpListenerFromSMS extends BroadcastReceiver {
    private static final String TAG = "PinePGAutoOtpReader";
    String msg, phoneNo = "";
    public static  String OTP = "";
    WrraperForOtp objOtpHandler=WrraperForOtp.getInstance();
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        new HandleSmsReceiver().execute(intent);
    }
    public String extractOTPFromSmsBody(String sms) {
        String[] nbs = sms.split("\\D+");
        if (nbs.length != 0) {
            for (String number : nbs) {
                if ((number.matches("^[0-9]+$"))) {
                    if(number.length()==6||number.length()==8) {
                        return number;
                    }
                }
            }
        }
        return null;
    }
    private class HandleSmsReceiver extends AsyncTask<Intent, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Intent... intents) {
            Log.i(TAG, "Intent Received: " + intents[0].getAction());
            if (intents[0].getAction() == SMS_RECEIVED) {
                //retrieves a map of extended data from the intent
                Bundle dataBundle = intents[0].getExtras();
                if (dataBundle != null) {
                    boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
                    //creating PDU(Protocol Data Unit) object which is a protocol for transferring message
                    Object[] mypdu = (Object[]) dataBundle.get("pdus");
                    final SmsMessage[] message = new SmsMessage[mypdu.length];

                    for (int i = 0; i < mypdu.length; i++) {
                        //for build versions >= API Level 23
                        if (isVersionM) {
                            String format = dataBundle.getString("format");
                            //From PDU we get all object and SmsMessage Object using following line of code
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i], format);
                            }
                        } else {
                            //<API level 23
                            message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i]);
                        }
                        msg = message[i].getMessageBody();
                        phoneNo = message[i].getOriginatingAddress();
                    }
                    try {
                        msg = extractOTPFromSmsBody(msg);
                        //System.out.println("````````````````THE OTP READ="+msg);
                        if (msg!=null&&msg.length() == 6||msg.length()==8) {
                            System.out.println("````````````````THE OTP READ="+msg);
                            OTP = msg;
                            objOtpHandler.setOTP(msg);
                            msg="";
                            //abortBroadcast();
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, "Broadcast receiver failed", ex);
                    }
                }
            }
            return null;
        }
    }
}

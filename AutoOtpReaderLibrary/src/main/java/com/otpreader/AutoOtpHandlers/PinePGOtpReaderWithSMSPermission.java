package com.otpreader.AutoOtpHandlers;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.otpreader.BottomSheetIntilization.PinePGBottomSheetBehaviourDefault;
import com.otpreader.BottomSheetIntilization.PinePGOtpBoxesSix;
import com.otpreader.R;
import com.otpreader.WrapperClass.SetInstancesAndValuesFromUser;
import static com.otpreader.Constants.PinePGConfigConstant.OTP_LENGTH_FOR_HDFC_NET_BANKING;
import static com.otpreader.Constants.PinePGConfigConstant.OTP_LENGTH_GENERAL;
import static com.otpreader.Constants.PinePGConfigConstant.OTP_SUBMISSION_TIME_IN_MILLISECONDS;
import static com.otpreader.Constants.PinePGConfigConstant.OTP_WAITING_TIME_IN_MILLISECONDS;
import static com.otpreader.Constants.PinePGConstants.WRONG_OTP_ENTERED;

public class PinePGOtpReaderWithSMSPermission extends PinePGOtpReader {
    View OTP_WAITING_LAYOUT = null, OTP_TIMEOUT_LAYOUT = null,NET_BANKING_PAYMENT_APPROVE_LAYOUT=null;
    TextView OTP_WAITING_COMMENT_TV = null;
    boolean waitingForOtp = true;
    boolean isOtpReceived = false;
    Button buttonRetry, buttonManuallyFill;
    PinePGOtpListenerFromSMS objPinePGOtpListenerFromSMS;
    private boolean isServiceFinished = false;
    public String OTP = "";
    private int otpWaitingCount=0;
    private BroadcastReceiver RECEIVER = null;
    WrraperForOtp objOtpHandler;
    public PinePGOtpReaderWithSMSPermission() {
        objPinePGOtpListenerFromSMS = new PinePGOtpListenerFromSMS();
        inPinePGOtpBoxes = new PinePGOtpBoxesSix();
        inPinePGBottomSheetBehaviour = new PinePGBottomSheetBehaviourDefault();
        objOtpHandler=WrraperForOtp.getInstance();
    }
    @Override
    public void bottomSheetConfig() {
        try {
            if (OTP_WAITING_LAYOUT == null) {
                OTP_WAITING_LAYOUT = LayoutInflater.from(SetInstancesAndValuesFromUser.getCtx()).inflate(R.layout.otp_waiting_layout, dynamicLinearLayout, false);
            }
            inPinePGBottomSheetBehaviour.setBottomSheetBehaviour(BOTTOM_SHEET, OTP_WAITING_LAYOUT);
            dynamicLinearLayout.removeAllViews();
            dynamicLinearLayout.addView(OTP_WAITING_LAYOUT);
            if (OTP_WAITING_COMMENT_TV == null) {
                OTP_WAITING_COMMENT_TV = OTP_WAITING_LAYOUT.findViewById(R.id.otpWaitingComment);
            }
        } catch (Exception e) {
            Log.e(TAG, "An exception has occurred while adding layout", e);
        }
    }
    @Override
    public void triggerOtpReader() {
        if (VIEW == null) {
            VIEW = LayoutInflater.from(SetInstancesAndValuesFromUser.getCtx()).inflate(R.layout.autootpreader, dynamicLinearLayout, false);
        }
        super.initializeOtpLayout();
        bottomSheetConfig();
        triggerOtpWaitingMethod();
    }
    @Override
    public String getOtp() {
        return OTP;
    }
    public void triggerAutoOtpReader(String smsMessageStr, String strMessageBody) {
        try {

            if(dynamicLinearLayout==null)
            {
                Log.i(TAG,"OTP reader can not be started");
                Log.e(TAG,"layout to attach bottom sheet is not found in the webview");
                return;
            }
            if(objBrowserSessionDTO!=null)
            {
                objBrowserSessionDTO.IsOtpDetected=true;
            }
            dynamicLinearLayout.removeAllViews();
            waitingForOtp = false;
            super.initializeBottomSheets();
            super.initializeBottomSheetBehaviour();
            dynamicLinearLayout.addView(VIEW);
            isOtpSubmitted = false;
            inPinePGBottomSheetBehaviour.setBottomSheetBehaviour(BOTTOM_SHEET, VIEW);
            inPinePGBottomSheetBehaviour.changeBottomSheetBehaviour(BottomSheetBehavior.STATE_EXPANDED);
            inPinePGOtpBoxes.getOtpComment().setText(R.string.PinePG_otp_function_note);
            inPinePGOtpBoxes.getOtpApproveButton().setText(R.string.PinePG_opt_approve_button_text);
            final TextView TIMER_TV = inPinePGOtpBoxes.getOtpTimeTv();
            inPinePGOtpBoxes.fillOtpInOtpBoxes(smsMessageStr);
            inPinePGOtpBoxes.changeOtpFieldsFocusableState(false);
            if(shouldHaltAutoOtpReader)
            {
                inPinePGOtpBoxes.getOtpComment().setText("");
                return;
            }
            if (TIMER_TV != null) {
                new CountDownTimer(OTP_SUBMISSION_TIME_IN_MILLISECONDS, 1000) {
                    public void onTick(long millisUntilFinished) {
                        if (isOtpSubmitted) {
                            TIMER_TV.setText("");
                            return;
                        }
                        Resources res = SetInstancesAndValuesFromUser.getCtx().getResources();
                        TIMER_TV.setText(String.format(res.getString(R.string.PinePG_seconds),""+millisUntilFinished / 1000) );
                    }
                    public void onFinish() {
                        if (VIEW == null||isServiceFinished) {
                            return;
                        }
                        TIMER_TV.setText("");
                        submitOtp(inPinePGOtpBoxes.getOtpApproveButton());
                    }
                }.start();
            }
        } catch (Exception e) {
            Log.w(TAG, "Automatic otp reader cannot be started", e);
        }
    }
    public void implementOtpTimeoutResponse1(View view) {
        {
            try {
                dynamicLinearLayout.removeAllViews();
                dynamicLinearLayout.addView(OTP_WAITING_LAYOUT);
                inPinePGBottomSheetBehaviour.setBottomSheetBehaviour(BOTTOM_SHEET, OTP_WAITING_LAYOUT);
                triggerOtpWaitingMethod();
            } catch (Exception e) {
                Log.e(TAG, "Binding method for one button method failed", e);
            }
        }
    }
    public void implementOtpTimeoutResponse2(View view) {
        {
            try {
                dynamicLinearLayout.removeAllViews();
                super.initializeBottomSheets();
                super.initializeBottomSheetBehaviour();
                dynamicLinearLayout.addView(VIEW);
                emptyOtpEditList();
                if(m_iOtpLength!=0){
                    inPinePGOtpBoxes.configForManualPayment(m_iOtpLength);
                    Log.i(TAG,"Wrong otp length set");
                }
                else
                {
                    inPinePGOtpBoxes.configForManualPayment(OTP_LENGTH_GENERAL);
                    Log.i(TAG,"Wrong otp length set");
                }
                inPinePGOtpBoxes.getOtpComment().setText(R.string.PinePG_otp_approve_comment);
            } catch (Exception e) {
                Log.e(TAG, "Binding method for one button method failed", e);
            }
        }
    }


    private void triggerOtpAlternateMethod() {
        try {
            if (isServiceFinished) {
                return;
            }
            if (OTP_TIMEOUT_LAYOUT == null) {
                OTP_TIMEOUT_LAYOUT = LayoutInflater.from(SetInstancesAndValuesFromUser.GetInstanceOfClientActivity()).inflate(R.layout.otp_time_out_layout, dynamicLinearLayout, false);
                //OTP_TIMEOUT_LAYOUT.setOnClickListener(this);
            }
            inPinePGBottomSheetBehaviour.setBottomSheetBehaviour(BOTTOM_SHEET, OTP_TIMEOUT_LAYOUT);
            if (OTP_TIMEOUT_LAYOUT != null) {
                dynamicLinearLayout.addView(OTP_TIMEOUT_LAYOUT);
            }
            buttonRetry = OTP_TIMEOUT_LAYOUT.findViewById(R.id.retryfor30);
            buttonRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    implementOtpTimeoutResponse1(v);
                }
            });

            buttonManuallyFill = OTP_TIMEOUT_LAYOUT.findViewById(R.id.manuallyfill);
            buttonManuallyFill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    implementOtpTimeoutResponse2(v);
                }
            });
            inPinePGBottomSheetBehaviour.changeBottomSheetBehaviour(BottomSheetBehavior.STATE_EXPANDED);
        } catch (Exception e) {
            Log.e(TAG, "An exception has occurred", e);
        }
    }

    private void triggerOtpWaitingMethod() {
        try {
            waitingForOtp = true;
            inPinePGBottomSheetBehaviour.changeBottomSheetBehaviour(BottomSheetBehavior.STATE_EXPANDED);
            startOtpWaitingTimer();
        } catch (Exception e) {
            Log.wtf(TAG, "OTP reader can not be started", e);
        }
    }

    private void startOtpWaitingTimer() {
        try {
            int count=0;
            if (OTP_WAITING_COMMENT_TV != null) {
                new CountDownTimer(OTP_WAITING_TIME_IN_MILLISECONDS, 1000) {
                    public void onTick(long millisUntilFinished) {
                        {
                            if (isOtpReceived) {
                                return;
                            }
                            otpWaitingCount++;
                            if(otpWaitingCount>5) {
                                String otp = objOtpHandler.getOtp();
                                if (otp.length() == OTP_LENGTH_GENERAL || otp.length() == OTP_LENGTH_FOR_HDFC_NET_BANKING) {
                                    isOtpReceived = true;
                                    isOtpDetected = true;
                                    triggerAutoOtpReader(otp, null);
                                    objOtpHandler.setOTP("");
                                }
                            }
                        }
                        Resources res = SetInstancesAndValuesFromUser.getCtx().getResources();
                        String text = String.format(res.getString(R.string.PinePG_opt_waiting_message), ""+millisUntilFinished / 1000);
                        OTP_WAITING_COMMENT_TV.setText(text);
                    }
                    public void onFinish() {
                        if(isServiceFinished){
                            return;
                        }
                        if (!waitingForOtp)
                            return;
                        if (isOtpReceived) {
                            return;
                        }
                        if (dynamicLinearLayout != null && OTP_WAITING_LAYOUT != null) {
                            dynamicLinearLayout.removeView(OTP_WAITING_LAYOUT);
                        }
                        triggerOtpAlternateMethod();
                    }
                }.start();
            }
        } catch (Exception e) {
            Log.e(TAG, "OOPS! An exception has occurred", e);
        }
    }
    public void setOtp(String otp) {
        this.OTP = otp;
    }

    public String otp() {
        return OTP;
    }
    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(RECEIVER, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }
    @Override
    public void wrongOtpHandler() {
        implementOtpTimeoutResponse2(buttonManuallyFill);
        inPinePGOtpBoxes.getOtpComment().setText(WRONG_OTP_ENTERED);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceFinished = true;
        OTP_TIMEOUT_LAYOUT = null;
        OTP_WAITING_LAYOUT = null;
        OTP_WAITING_COMMENT_TV = null;
        OTP = null;
        objOtpHandler.setOTP("");
    }
}

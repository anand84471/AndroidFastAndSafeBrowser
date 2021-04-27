package com.otpreader.AutoOtpHandlers;

import android.os.CountDownTimer;
import android.view.LayoutInflater;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.otpreader.AutoOtpHandlers.PinePGOtpReader;
import com.otpreader.BottomSheetIntilization.PinePGBottomSheetBehaviourDefault;
import com.otpreader.BottomSheetIntilization.PinePGOtpBoxesSix;
import com.otpreader.R;
import com.otpreader.WrapperClass.SetInstancesAndValuesFromUser;
import com.otpreader.interfaces.PinePGBottomSheetBehaviour;

public class PinePGOtpReaderWithoutSMSPermission extends PinePGOtpReader {

    public PinePGOtpReaderWithoutSMSPermission() {
        inPinePGOtpBoxes = new PinePGOtpBoxesSix();
        inPinePGBottomSheetBehaviour =  new PinePGBottomSheetBehaviourDefault();
    }

    private void initializeOtpContents() {
        super.initializeBottomSheetBehaviour();
        bottomSheetConfig();
        super.initializeBottomSheets();
    }


    @Override
    public void bottomSheetConfig() {
        try {

            inPinePGOtpBoxes.getOtpComment().setText(R.string.PinePG_otp_approve_comment);
        } catch (Exception e) {

        }
    }

    @Override
    public void triggerOtpReader() {
        try {
            if (VIEW == null) {
                VIEW = LayoutInflater.from(SetInstancesAndValuesFromUser.getCtx()).inflate(R.layout.autootpreader, dynamicLinearLayout, false);
            }
            super.initializeOtpLayout();
            new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
                    if (inPinePGOtpBoxes.getEditBoxList() != null) {
                        if (inPinePGOtpBoxes.getEditBoxList().size() == 8) {
                            initializeOtpContents();
                            inPinePGOtpBoxes.configForManualPayment(m_iOtpLength);
                            dynamicLinearLayout.removeAllViews();
                            dynamicLinearLayout.addView(VIEW);
                            inPinePGBottomSheetBehaviour.changeBottomSheetBehaviour(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }
                }
                public void onFinish() {

                }
            }.start();
        } catch (Exception e) {

        }
    }

    @Override
    public String getOtp() {
        return OTP;
    }

    @Override
    public void triggerAutoOtpReader(String msg, String sender) {
        return;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void wrongOtpHandler() {
        inPinePGOtpBoxes.getOtpComment().setText("Wrong OTP. Please try again.");
    }
}

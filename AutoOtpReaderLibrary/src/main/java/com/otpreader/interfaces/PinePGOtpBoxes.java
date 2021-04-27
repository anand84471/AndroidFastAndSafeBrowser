package com.otpreader.interfaces;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public interface PinePGOtpBoxes {
    ArrayList<EditText> generateOtpBoxes(View v);
    TextView getOtpTimeTv();
    Button getOtpApproveButton();
    TextView getOtpComment();
    void fillOtpInOtpBoxes(String OTP);
    ArrayList<EditText> getEditBoxList();
    ProgressBar getOtpSubmitProgreesBar();
    void setOtpStatus(int status);
    void changeOtpFieldsFocusableState(boolean status);
}

package com.otpreader.BottomSheetIntilization;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.otpreader.R;
import com.otpreader.interfaces.PinePGOtpBoxes;
import java.util.ArrayList;

public class PinePGOtpBoxesSix implements PinePGOtpBoxes {
    private LinearLayout OTP_EDIT_BOX_LAYOUT = null;
    private Button OTP_APPROVE_BUTTON = null;
    private TextView TIMER_TV = null, OTP_COMMENT_TV = null;
    private ArrayList<EditText> EDIT_BOX_LIST = null;
    private View VIEW;
    private EditText OTP_ET_1 = null, OTP_ET_2 = null, OTP_ET_3 = null, OTP_ET_4 = null, OTP_ET_5 = null, OTP_ET_6 = null,OTP_ET_7=null,OTP_ET_8;
    private ProgressBar otpSubmitProgressBar = null;
    private Integer sizeofOtp = 0;
    @Override
    public ArrayList<EditText> generateOtpBoxes(View bottom_sheet) {
        this.VIEW = bottom_sheet;
        if (VIEW != null) {
            LoadResources();
        }
        return EDIT_BOX_LIST;
    }

    protected ArrayList<EditText> LoadResources() {
        if (OTP_EDIT_BOX_LAYOUT == null) {
            OTP_EDIT_BOX_LAYOUT = VIEW.findViewById(R.id.OtpEditBox);
        }
        if (OTP_APPROVE_BUTTON == null) {
            OTP_APPROVE_BUTTON = (Button) VIEW.findViewById(R.id.SubmitButton).findViewById(R.id.submit);
        }
        if (TIMER_TV == null) {
            TIMER_TV = VIEW.findViewById(R.id.box1);
        }
        if (OTP_COMMENT_TV == null) {
            OTP_COMMENT_TV = VIEW.findViewById(R.id.textView4);
        }
        if (otpSubmitProgressBar == null) {
            otpSubmitProgressBar = VIEW.findViewById(R.id.submitButtonProgressBar);
        }
        if (OTP_ET_1 == null && OTP_ET_2 == null && OTP_ET_3 == null && OTP_ET_4 == null && OTP_ET_5 == null && OTP_ET_6 == null) {
            OTP_ET_1 = VIEW.findViewById(R.id.editTextOne);
            OTP_ET_1.setTransformationMethod(null);
            OTP_ET_2 = VIEW.findViewById(R.id.editTexttwo);
            OTP_ET_2.setTransformationMethod(null);
            OTP_ET_3 = VIEW.findViewById(R.id.editTextthree);
            OTP_ET_3.setTransformationMethod(null);
            OTP_ET_4 = VIEW.findViewById(R.id.editTextfour);
            OTP_ET_4.setTransformationMethod(null);
            OTP_ET_5 = VIEW.findViewById(R.id.editTextfive);
            OTP_ET_5.setTransformationMethod(null);
            OTP_ET_6 = VIEW.findViewById(R.id.editTextsix);
            OTP_ET_6.setTransformationMethod(null);
            OTP_ET_7 = VIEW.findViewById(R.id.editTextSeven);
            OTP_ET_7.setTransformationMethod(null);
            OTP_ET_8 = VIEW.findViewById(R.id.editTextEight);
            OTP_ET_8.setTransformationMethod(null);
            EDIT_BOX_LIST = new ArrayList<EditText>();
            EDIT_BOX_LIST.add(OTP_ET_1);
            EDIT_BOX_LIST.add(OTP_ET_2);
            EDIT_BOX_LIST.add(OTP_ET_3);
            EDIT_BOX_LIST.add(OTP_ET_4);
            EDIT_BOX_LIST.add(OTP_ET_5);
            EDIT_BOX_LIST.add(OTP_ET_6);
            EDIT_BOX_LIST.add(OTP_ET_7);
            EDIT_BOX_LIST.add(OTP_ET_8);
        }
        return EDIT_BOX_LIST;
    }

    public Button getOtpApproveButton() {
        return OTP_APPROVE_BUTTON;
    }

    public TextView getOtpTimeTv() {
        return TIMER_TV;
    }

    public TextView getOtpComment() {
        return OTP_COMMENT_TV;
    }
    public void configForManualPayment( int otpLength)
    {
        sizeofOtp=otpLength;
        for (int DIGITEXTRACT = 0; DIGITEXTRACT < sizeofOtp; DIGITEXTRACT++) {
            EDIT_BOX_LIST.get(DIGITEXTRACT).setVisibility(View.VISIBLE);
        }
    }
    public void fillOtpInOtpBoxes(String OTP) {
        for (int DIGITEXTRACT = 0; DIGITEXTRACT < OTP.length(); DIGITEXTRACT++) {
            EDIT_BOX_LIST.get(DIGITEXTRACT).setText(String.valueOf(OTP.charAt(DIGITEXTRACT)));
            EDIT_BOX_LIST.get(DIGITEXTRACT).setVisibility(View.VISIBLE);
            sizeofOtp++;
        }
    }
    @Override
    public ArrayList<EditText> getEditBoxList() {
        return EDIT_BOX_LIST;
    }

    @Override
    public ProgressBar getOtpSubmitProgreesBar() {
        return otpSubmitProgressBar;
    }

    public Integer getSizeOfOtp() {
        return sizeofOtp;
    }

    public void setOtpStatus(int status) {
        int color = color = Color.GRAY;
        switch (status) {
            case 1: {
                //wrong otp
                color = Color.RED;
                break;
            }
            case 2: {
                //right otp
                color = Color.GREEN;
                break;
            }
            case 3: {
                //otp typing in active
                color = Color.GRAY;
                break;
                //error
            }
            case 4: {
                //otp typing active
                color = Color.BLUE;
                break;
            }
        }
        for (int i = 0; i < sizeofOtp; i++) {
            EDIT_BOX_LIST.get(i).getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        }
    }
    @Override
    public void changeOtpFieldsFocusableState(boolean status)
    {
        for (int i = 0; i < sizeofOtp; i++) {
            EDIT_BOX_LIST.get(i).setFocusable(status);
        }
    }
}

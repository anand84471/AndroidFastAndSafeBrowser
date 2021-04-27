package com.otpreader.AutoOtpHandlers;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.otpreader.BottomSheetIntilization.PinePGOtpBoxesSix;
import com.otpreader.Constants.PinePGConstants;
import com.otpreader.PinePGDTOs.BrowserSessionDTO;
import com.otpreader.R;
import com.otpreader.ServiceManagers.ReportJsErrorService;
import com.otpreader.WrapperClass.SetInstancesAndValuesFromUser;
import com.otpreader.interfaces.PinePGBottomSheetBehaviour;
import com.otpreader.interfaces.PinePGJavaScriptInterface;
import com.otpreader.pgwebview.EvaluateJsOnWebView;
import java.util.ArrayList;
import static com.otpreader.Constants.PinePGConfigConstant.MAX_ALLOWED_OTP_FAIL;
import static com.otpreader.Constants.PinePGConstants.OTP_SUBMISSION_STATUS_FAIL;
import static com.otpreader.Constants.PinePGConstants.OTP_SUBMISSION_STATUS_SUCCESS;

public abstract class PinePGOtpReader extends AppCompatActivity {
    public final String TAG = "PinePGOtpReader";
    private ArrayList<EditText> otpEditBoxes = null;
    protected View VIEW = null;
    public PinePGBottomSheetBehaviour inPinePGBottomSheetBehaviour;
    private String JS_EDITBOX_CODE = null;
    public String OTP = null;
    public PinePGOtpBoxesSix inPinePGOtpBoxes;
    protected boolean isOtpSubmitted = false;
    public LinearLayout dynamicLinearLayout = null;
    public View BOTTOM_SHEET = null;
    ViewGroup NET_BANKING_PAYMENT_APPROVE_LAYOUT=null;
    public abstract void bottomSheetConfig();
    WebView objWebView = null;
    int noOfTimesOtpSubmitted = 0;
    protected int m_iOtpLength=0;
    protected boolean isEnabledOneClickNetBankingTxn=false,isOtpDetected;
    private TextView netBankingAutoSubmissionMessage;
    PinePGJavaScriptInterface objPinePGJavaScriptInterface;
    private EvaluateJsOnWebView objEvaluateJsOnWebView;
    CheckBox rememberIdCheckBox;
    private String jsCodeToSubmitOtp="";
    private String jsCodeToSubmitNetBankingPage;
    private boolean isEnabledRememberMeService=false;
    boolean shouldHaltAutoOtpReader;
    boolean shouldUseGenericAutoOtpSubmissionCode=true;
    BrowserSessionDTO objBrowserSessionDTO;
    private ReportJsErrorService objReportJsErrorService;
    public void initializeBottomSheetBehaviour() {
        inPinePGBottomSheetBehaviour.setBottomSheetBehaviour(BOTTOM_SHEET, VIEW);
    }
    public void initializeBottomSheets(View bottom_sheet, LinearLayout dynamicLayout, WebView webView) {
        this.BOTTOM_SHEET = bottom_sheet;
        this.dynamicLinearLayout = dynamicLayout;
        objWebView = webView;
    }
    protected void initializeOtpLayout() {
        otpEditBoxes = inPinePGOtpBoxes.generateOtpBoxes(VIEW);
    }
    private void initializeOtpEditBoxes() {
        otpEditBoxes = inPinePGOtpBoxes.getEditBoxList();
        if (otpEditBoxes != null) {
            if (otpEditBoxes.size() > 0) {
                int size = otpEditBoxes.size();
                for (int i = 0; i < size - 1; i++) {
                    //textChangeListener(otpEditBoxes.get(i), otpEditBoxes.get(i + 1));
                    setSoftInputMethod(otpEditBoxes.get(i));
                }
            }
            setTextChangeListener(otpEditBoxes, inPinePGOtpBoxes.getOtpApproveButton());
        }
    }
    public void setJsCodeToSubmitOtp(String jsCode)
    {
        this.jsCodeToSubmitOtp=jsCode;
    }
    private String extractOtp() {
        if (otpEditBoxes == null) {
            otpEditBoxes = inPinePGOtpBoxes.getEditBoxList();
        } else {
            String otp = "";
            int size = otpEditBoxes.size();
            if (otpEditBoxes != null && size > 0) {
                for (int i = 0; i < size; i++) {
                    if(otpEditBoxes.get(i).getVisibility()==View.VISIBLE) {
                        otp += otpEditBoxes.get(i).getText().toString();
                    }
                }
            }
            OTP = otp;
            return otp;
        }
        return null;
    }
    private void textChangeListenerForward(final EditText text1, final EditText text2) {
        text1.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (count == 1) {
                            text1.clearFocus();
                            text1.setFocusableInTouchMode(true);
                            text2.setFocusableInTouchMode(true);
                            text2.requestFocus();
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                }
        );
    }
    private void textChangeListenerBack(final EditText text1, final EditText text2) {
        text1.addTextChangedListener(
                new TextWatcher() {
                    private int previousLength;
                    private boolean backSpace;
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        previousLength = s.length();
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        backSpace = previousLength >= s.length();
                        if (backSpace) {
                                text1.clearFocus();
                                text1.setFocusableInTouchMode(true);
                                text2.setFocusableInTouchMode(true);
                                text2.requestFocus();
                        }
                    }
                }
        );
    }
    private void setFocusChangeListenerBack(final EditText text1, final EditText text2) {
        text1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus&&text2.getText().length()==0)
                {
                    text2.requestFocus();
                }
            }
        });
    }
    public void emptyOtpEditList() {
        emptyEditLists(otpEditBoxes);
        otpEditBoxes.get(0).requestFocus();
    }
    public void setOtpComment(String comment) {
        inPinePGOtpBoxes.getOtpComment().setText(comment);
    }
    private void emptyEditLists(ArrayList<EditText> array) {
        for (int i = 0; i < array.size(); i++) {
            array.get(i).setText("");
        }
    }
    private void setSoftInputMethod(final EditText editText1) {
        editText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(editText1.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                editText1.setTranslationY(0f);
                return false;
            }
        });
    }
    private void setTextChangeListener(ArrayList<EditText> array, View v) {
        try {
            int size = array.size();
            for (int i = 0; i < size - 1; i++) {
                setSoftInputMethod(array.get(i));
                textChangeListenerForward(array.get(i), array.get(i + 1));
                setFocusChangeListener(array.get(i));
            }
            for (int i = size - 1; i > 0; i--) {
                textChangeListenerBack(array.get(i), array.get(i - 1));
                setFocusChangeListenerBack(array.get(i), array.get(i - 1));
            }
            array.get(0).requestFocus();
            setFocusChangeListener(array.get(size - 1));
            setSoftInputMethod(array.get(size - 1));
        } catch (Exception e) {
            Log.w(TAG, "An exception has occurred", e);
        }
    }
    public abstract void triggerOtpReader();
    public  void setOtpLength(int OtpLength){
        this.m_iOtpLength=OtpLength;
    }
    protected void initializeBottomSheets() {
        try {
            if (VIEW == null) {
                VIEW = LayoutInflater.from(SetInstancesAndValuesFromUser.getCtx()).inflate(R.layout.autootpreader, dynamicLinearLayout, false);
            }
            initializeOtpEditBoxes();
            setOnClick();
        } catch (Exception e) {
            Log.e(TAG, "Can't initialize otp bottom sheet", e);
        }
    }
    public void setOnClick() {
        try {
            inPinePGOtpBoxes.getOtpApproveButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitOtp(v);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Failed to bind method for OTP submit button", e);
        }
    }
    public void setFocusChangeListener(EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    inPinePGOtpBoxes.setOtpStatus(2);
                }
            }
        });
    }
    public abstract String getOtp();
    public boolean getIsOtpDetected()
    {
        return isOtpDetected;
    }
    public String getJsCodeWithExceptionHandling(String jsCode)
    {
        return "function returnError(){try{"+jsCode+"}catch(ex){return 'error '+ex.message;}}returnError();";
    }
    public void submitOtp(View view) {
        try {
            String JS_CODE_TO_SUBIMT_OTP=jsCodeToSubmitOtp;
            String otp = extractOtp();
            if (otp!=null&&otp.length() != inPinePGOtpBoxes.getSizeOfOtp()) {
                inPinePGOtpBoxes.setOtpStatus(OTP_SUBMISSION_STATUS_FAIL);
                inPinePGOtpBoxes.changeOtpFieldsFocusableState(true);
                return;
            }
            inPinePGBottomSheetBehaviour.changeBottomSheetBehaviour(BottomSheetBehavior.STATE_HIDDEN);
            if (noOfTimesOtpSubmitted == MAX_ALLOWED_OTP_FAIL) {
                inPinePGOtpBoxes.getOtpComment().setText(R.string.PinePG_otp_submission_limit_exeeded);
                noOfTimesOtpSubmitted += 1;
                return;
            }
            if (noOfTimesOtpSubmitted > MAX_ALLOWED_OTP_FAIL) {
                dynamicLinearLayout.removeView(VIEW);
                return;
            }
            noOfTimesOtpSubmitted += 1;
            inPinePGOtpBoxes.getOtpSubmitProgreesBar().setVisibility(View.VISIBLE);
            if (shouldUseGenericAutoOtpSubmissionCode) {
                //otp="";
                if(this.jsCodeToSubmitOtp==null||this.jsCodeToSubmitOtp=="")
                {
                    JS_EDITBOX_CODE = "javascript:" +
                            "var x = document.forms.length; if(x==1) { var form_id=document.forms[0]; var otp_field; try " +
                            "{ var input_fields; input_fields=form_id.getElementsByTagName('input');" +
                            " for (i = 0; i < input_fields.length ;i++) { if((input_fields[i].getAttribute('type') != 'hidden'))" +
                            " { if((input_fields[i].getAttribute('type') == 'password')||(input_fields[i].getAttribute('type') == 'number')" +
                            "||(input_fields[i].getAttribute('type') == 'text')||(input_fields[i].getAttribute('type') == 'tel')) " +
                            "{ if(!(input_fields[i].style.display=='none')&&!(input_fields[i].offsetHeight ==0 && " +
                            "input_fields[i].offsetWidth ==0)) { otp_field=input_fields[i]; break; } } } } " +
                            "otp_field.value='" + otp + "'; var submit_buttons; var form_submit_button; " +
                            "submit_buttons=form_id.getElementsByTagName('button'); for(i=0;i<submit_buttons.length;i++) {" +
                            " if(submit_buttons[i].getAttribute('type')=='submit') { form_submit_button=submit_buttons[i];" +
                            " form_submit_button.click(); break; } } form_id.submit(); } catch(exception) {throw 'error';} }"
                    ;
                }
                else if(objWebView.getUrl().equals("https://netbanking.hdfcbank.com/netbanking/epientry")){
                    JS_EDITBOX_CODE="document.getElementById(\"fldOtpToken\").value='"+otp+"';"+this.jsCodeToSubmitOtp;
                }
                else if(objWebView.getUrl().equals("https://secure5.arcot.com/acspage/cap?RID=49329&VAA=A")){
                    JS_EDITBOX_CODE="document.getElementById(\"otpentrypin\").value='"+otp+"';"+this.jsCodeToSubmitOtp;
                }
                else {
                    JS_EDITBOX_CODE = "javascript:" +
                            "var x = document.forms.length; if(x==1) { var form_id=document.forms[0]; var otp_field; try " +
                            "{ var input_fields; input_fields=form_id.getElementsByTagName('input');" +
                            " for (i = 0; i < input_fields.length ;i++) { if((input_fields[i].getAttribute('type') != 'hidden'))" +
                            " { if((input_fields[i].getAttribute('type') == 'password')||(input_fields[i].getAttribute('type') == 'number')" +
                            "||(input_fields[i].getAttribute('type') == 'text')||(input_fields[i].getAttribute('type') == 'tel')) " +
                            "{ if(!(input_fields[i].style.display=='none')&&!(input_fields[i].offsetHeight ==0 && " +
                            "input_fields[i].offsetWidth ==0)) { otp_field=input_fields[i]; break; } } } } " +
                            "otp_field.value='" + otp + "';" ;
                    JS_EDITBOX_CODE+=jsCodeToSubmitOtp;
                    JS_EDITBOX_CODE+="try{setOtp('"+otp+"');\n" +
                            "   }catch(ex){}";
                    JS_EDITBOX_CODE+="}catch(exception) {throw 'error';} }";
                }
                if (objWebView != null) {
                    objWebView.evaluateJavascript(getJsCodeWithExceptionHandling(JS_EDITBOX_CODE), new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            if (value!=null&& !value.equals("") && !value.equals("null")) {
                                reportJsError(JS_EDITBOX_CODE,value,PinePGConstants.JS_CODE_TYPE_OTP_PAGE_SUBMISSION_JS_CODE);
                                inPinePGOtpBoxes.getOtpSubmitProgreesBar().setVisibility(View.GONE);
                                inPinePGOtpBoxes.getOtpComment().setText(R.string.PinePG_otp_submission_error);
                                inPinePGOtpBoxes.setOtpStatus(OTP_SUBMISSION_STATUS_FAIL);
                            }
                        }
                    });
                }
            }
            else{
                JS_CODE_TO_SUBIMT_OTP+="submitOtp("+otp+");";
                if (objWebView != null) {
                    final String finalJS_CODE_TO_SUBIMT_OTP = JS_CODE_TO_SUBIMT_OTP;
                    objWebView.evaluateJavascript(getJsCodeWithExceptionHandling(JS_CODE_TO_SUBIMT_OTP), new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            if (value!=null &&!value.equals("")&&!value.equals("null")) {
                                reportJsError(finalJS_CODE_TO_SUBIMT_OTP,value,PinePGConstants.JS_CODE_TYPE_OTP_PAGE_SUBMISSION_JS_CODE);
                                inPinePGOtpBoxes.getOtpSubmitProgreesBar().setVisibility(View.GONE);
                                inPinePGOtpBoxes.getOtpComment().setText(R.string.PinePG_otp_submission_error);
                                inPinePGOtpBoxes.setOtpStatus(OTP_SUBMISSION_STATUS_FAIL);
                            }
                        }
                    });
                }
            }
            isOtpSubmitted = true;
            inPinePGOtpBoxes.setOtpStatus(OTP_SUBMISSION_STATUS_SUCCESS);
            emptyOtpEditList();
        } catch (Exception e) {
            Log.e(TAG, "OTP submission failed", e);
        }
    }

    public boolean getIsOtpSubmitted()
    {
        return this.isOtpSubmitted;
    }
    public abstract void wrongOtpHandler();

    public abstract void triggerAutoOtpReader(String msg, String sender);

    public void initializeNetBankingLayouts()
    {
        dynamicLinearLayout.removeAllViews();
        NET_BANKING_PAYMENT_APPROVE_LAYOUT = (ViewGroup) LayoutInflater.from(SetInstancesAndValuesFromUser.GetInstanceOfClientActivity()).inflate(R.layout.net_banking_approve_layout, dynamicLinearLayout, false);
        inPinePGBottomSheetBehaviour.setBottomSheetBehaviour(BOTTOM_SHEET, NET_BANKING_PAYMENT_APPROVE_LAYOUT);
        inPinePGBottomSheetBehaviour.changeBottomSheetBehaviour(BottomSheetBehavior.STATE_EXPANDED);
        dynamicLinearLayout.addView(NET_BANKING_PAYMENT_APPROVE_LAYOUT);
        objEvaluateJsOnWebView=new EvaluateJsOnWebView(objWebView,2);
        rememberIdCheckBox=(CheckBox)NET_BANKING_PAYMENT_APPROVE_LAYOUT.findViewById(R.id.rememberUserid);
        if(rememberIdCheckBox!=null&&rememberIdCheckBox.isChecked()){
            rememberIdCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!isChecked)
                    {
                        objPinePGJavaScriptInterface.stopRememberMeService();
                    }
                    else {
                        objPinePGJavaScriptInterface.startRememberMeService();
                    }
                }
            });
        }
    }
    private void reportJsError(String jsCode,String error,int JsCodeTypeId)
    {
        if(objReportJsErrorService==null)
        {
            objReportJsErrorService=new ReportJsErrorService(2);
        }
        objReportJsErrorService.reportJsErrorHasOccurred(objWebView.getUrl(),error ,jsCode,"", JsCodeTypeId);
    }
    public void triggerNetBankingSubmission(final String jsCode, final String jsCodeToGetUserIdFromPage,
                                             boolean ShouldStartOtpReader){
        try {

            if(isEnabledOneClickNetBankingTxn) {
                objWebView.evaluateJavascript(jsCode, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        if(value!=null &&!value.equals("")&&!value.equals("null")){
                            reportJsError(jsCode,value,PinePGConstants.JS_CODE_TYPE_PAGE_NET_BANKING_PAGE_SUBMISSION_JS_CODE);
                        }
                        if(objPinePGJavaScriptInterface.getShouldStartOtpReader())
                        {
                            setOtpLength(objPinePGJavaScriptInterface.getOtpLength());
                            triggerOtpReader();
                        }
                    }
                });
                if(!jsCodeToGetUserIdFromPage.isEmpty())
                {
                    objEvaluateJsOnWebView.getNetBankingUserIdFromPage(jsCodeToGetUserIdFromPage);
                }
                else {
                    if(rememberIdCheckBox!=null&&NET_BANKING_PAYMENT_APPROVE_LAYOUT.findViewById(R.id.rememberUserIdSection)!=null ){
                        NET_BANKING_PAYMENT_APPROVE_LAYOUT.findViewById(R.id.rememberUserIdSection).setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                        rememberIdCheckBox.setVisibility(View.GONE);
                        NET_BANKING_PAYMENT_APPROVE_LAYOUT.removeView(findViewById(R.id.rememberUserIdSection));
                        rememberIdCheckBox=null;
                    }
                }
                return;
            }
            NET_BANKING_PAYMENT_APPROVE_LAYOUT.findViewById(R.id.SubmitButtonNetBanking).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            objWebView.evaluateJavascript(getJsCodeWithExceptionHandling(jsCode), new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    if(value!=null&&!value.equals("")&&!value.equals("null"))
                                    {
                                        reportJsError(jsCode,value,PinePGConstants.JS_CODE_TYPE_PAGE_NET_BANKING_PAGE_SUBMISSION_JS_CODE);
                                    }
                                    if(objPinePGJavaScriptInterface.getShouldStartOtpReader())
                                    {
                                        setOtpLength(objPinePGJavaScriptInterface.getOtpLength());
                                        triggerOtpReader();
                                    }
                                }
                            });
                        }
                    }
            );
            NET_BANKING_PAYMENT_APPROVE_LAYOUT.findViewById(R.id.autoNetBanking).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isEnabledOneClickNetBankingTxn=true;
                            objWebView.evaluateJavascript(getJsCodeWithExceptionHandling(jsCode), new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {
                                    if(value!=null&&!value.equals("")&&!value.equals("null"))
                                    {
                                        reportJsError(jsCode,value,PinePGConstants.JS_CODE_TYPE_PAGE_NET_BANKING_PAGE_SUBMISSION_JS_CODE);
                                    }
                                }
                            });
                            inPinePGBottomSheetBehaviour.changeBottomSheetBehaviour(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    }
            );
            if(!jsCodeToGetUserIdFromPage.isEmpty())
            {
                objEvaluateJsOnWebView.getNetBankingUserIdFromPage(jsCodeToGetUserIdFromPage);
            }
            else {
                if(rememberIdCheckBox!=null&&NET_BANKING_PAYMENT_APPROVE_LAYOUT.findViewById(R.id.rememberUserIdSection)!=null ){
                    NET_BANKING_PAYMENT_APPROVE_LAYOUT.findViewById(R.id.rememberUserIdSection).setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    rememberIdCheckBox.setVisibility(View.GONE);
                    NET_BANKING_PAYMENT_APPROVE_LAYOUT.removeView(findViewById(R.id.rememberUserIdSection));
                    rememberIdCheckBox=null;
                }
            }
        }
        catch (Exception Ex)
        {
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        inPinePGOtpBoxes = null;
        inPinePGBottomSheetBehaviour = null;
        otpEditBoxes = null;
        JS_EDITBOX_CODE = null;
        OTP = null;
        VIEW = null;
    }
    public void setJavaScriptInterface(PinePGJavaScriptInterface objPinePGJavaScriptInterface)
    {
        this.objPinePGJavaScriptInterface=objPinePGJavaScriptInterface;
    }
    public void removeAllViews() {
        try {
            if(dynamicLinearLayout!=null) {
                dynamicLinearLayout.removeAllViews();
            }
            dynamicLinearLayout = null;
            onDestroy();
        } catch (Exception e) {
        }
    }



    public void setBrowserSessionDTO(BrowserSessionDTO objBrowserSessionDTO)
    {
        this.objBrowserSessionDTO=objBrowserSessionDTO;
    }
    public void setShouldHaltAutoOtpReader(boolean ShouldHaltAutoOtpReader)
    {
        this.shouldHaltAutoOtpReader=ShouldHaltAutoOtpReader;
    }
    public void setShouldUseGenericAutoOtpSubmissionCode(boolean ShouldUseGenericAutoOtpSubmissionCode)
    {
        this.shouldUseGenericAutoOtpSubmissionCode=ShouldUseGenericAutoOtpSubmissionCode;
    }
}

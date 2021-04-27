package com.otpreader.AutoOtpHandlers;
public  class WrraperForOtp {
    public String OTP = "";
    private static volatile WrraperForOtp objWrraperForOtp;
    private WrraperForOtp() {
    }
    public static WrraperForOtp getInstance() {
        if (objWrraperForOtp == null) {
            synchronized (WrraperForOtp.class) {
                if (objWrraperForOtp == null) {
                    objWrraperForOtp = new WrraperForOtp();
                }
            }
        }
        return objWrraperForOtp;
    }
    public String getOtp() {
        String otp=OTP;
        OTP="";
        return otp;
    }
    public void setOTP(String otp) {
        this.OTP = otp;
    }
}

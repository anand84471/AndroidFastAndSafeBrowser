package com.otpreader.WrapperClass;
import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
public class SetInstancesAndValuesFromUser {
    private static Activity obj_Activity;
    private static Context ctx;
    public static void PassInstanceOfMainActivity(AppCompatActivity objActivity) {
        if (objActivity != null) {
            obj_Activity = objActivity;
        }
    }
    public static void SetContext(Context context)
    {
        ctx=context;
    }
    public static Context getCtx(){return ctx;}
    public static Context GetInstanceOfClientActivity() {
        return ctx;
    }
}

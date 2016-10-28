package com.cfbb.android;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;

import com.cfbb.android.features.authentication.LoginActivity;

/**
 * @author MrChang45
 * @time 2016/10/26
 * @desc
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    public LoginActivityTest(Class<LoginActivity> activityClass) {
        super(activityClass);
    }

    public LoginActivityTest(){
        super(LoginActivity.class);
    }
    private Button btn_login;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        LoginActivity activity=getActivity();
        btn_login = (Button) activity.findViewById(R.id.btn_ok);
    }


    public void testlogin()
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btn_login.performClick();
            }
        });

    }
}

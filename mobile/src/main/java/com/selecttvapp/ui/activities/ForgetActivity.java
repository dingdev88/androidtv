package com.selecttvapp.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.network.common.AppFonts;
import com.demo.network.dialogs.AppDialogClickListiner;
import com.demo.network.dialogs.AppDialogUserActions;
import com.demo.network.dialogs.AppUtil;
import com.selecttvapp.BuildConfig;
import com.selecttvapp.R;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.network.JSONRPCAPI;

import org.json.JSONObject;


public class ForgetActivity extends FragmentActivity implements View.OnClickListener {

    private Context mContext;
    private EditText txtEmailAddress;
    private Button btnContinue;
    private String email;
    private JSONObject m_jsonArrayForget;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_forget_password);
        mContext = this;

        initComponent();
        setOnClickListiners();
        setFonts();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.FORGET_PASSWORD_SCREEN);
    }

    private void setFonts() {
        Typeface roboto = Typeface.createFromAsset(getAssets(), AppFonts.ROBOTO);
        txtEmailAddress.setTypeface(roboto);

        Typeface arial = Typeface.createFromAsset(getAssets(), AppFonts.ARIALREGULAR);
        btnContinue.setTypeface(arial);

        Typeface halvitaca = Typeface.createFromAsset(getAssets(), AppFonts.HELVETICA);
        ((TextView) findViewById(R.id.ui_forget_lab_login_into)).setTypeface(halvitaca);
    }

    private void setOnClickListiners() {
        btnContinue.setOnClickListener(this);
    }

    private void initComponent() {
        txtEmailAddress = (EditText) findViewById(R.id.ui_forget_etxt_email);
        txtEmailAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    onUserActionForgetPassword();
                }
                return false;
            }
        });
        btnContinue = (Button) findViewById(R.id.ui_forget_btn_continue);
    }

    private void onUserActionForgetPassword() {
        txtEmailAddress.setError(null);
        AppUtil.hideKeyBoard(mContext, txtEmailAddress);

        String szEmail = txtEmailAddress.getText().toString();
        if (szEmail.length() == 0) {
            txtEmailAddress.setError("Input Email Address");
            return;
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+";
        if (!szEmail.matches(emailPattern)) {
            txtEmailAddress.setError("Invalid email address");
            return;
        }
        email = szEmail;
        new ForgetPasswordAPI().execute();
    }

    @Override
    public void onClick(View view) {
        if (view == btnContinue) {
            onUserActionForgetPassword();
        }
    }

    private void showDialogToUser() {
        AppUtil.showDialog(mContext,
                "Password Reset Email Sent",
                "An email containing your password reset link has been sent to your verified email address.",
                "OK", null,
                new AppDialogClickListiner() {
                    @Override
                    public void onDialogClick(AppDialogUserActions which) {
                        if (which == AppDialogUserActions.OK) {
                            showLoginScreen();
                        }
                    }
                }
        );
    }

    private void showLoginScreen() {
        ForgetActivity.this.finish();
    }

    private class ForgetPasswordAPI extends AsyncTask<Object, Object, Object> {
        @Override
        protected void onPreExecute() {
            try {
                progressDialog = ProgressDialog.show(mContext, "Please wait", "Processing...", false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
//            arvig = "arvig-freecast-com";
//            broadviewlauncher = "broadview-live";
//            centurylink= "centurylinkdemo-freecast-com";
//            endeavor= "endeavor-freecast-com";
//            bucktv= "2bucktv-freecast-com";
//            evolution= "evolution-freecast-com";
//            etv = "etvanywhere-net";
//            smartcity = "smartcity-freecast-com";
//            wisetv= "wisetv-freecast-com";
//            rabbittvplus = "rabbittvplus-freecast-com";
//            hughesnet= "hughesnet-freecast-com";
//            klicktv= "selecttv-freecast-com";
//            mohawk = "mohawk-freecast-com";
//            demo= "demo-freecast-com";
//            mcg= "mcg-freecast-com";
//            selecttv = "selecttv-freecast-com";

//            String brand_slug = "";
//            if (getPackageName().equalsIgnoreCase("com.selecttvapp"))
//                brand_slug = "selecttv-freecast-com";
//            if (getPackageName().equalsIgnoreCase(BuildConfig.RABBITTVPLUS_PACKAGE))
//                brand_slug = "rabbittvplus-freecast-com";
            m_jsonArrayForget = JSONRPCAPI.getForget(email, BuildConfig.BRAND_SLUG);
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            try {
                if (m_jsonArrayForget == null) {
                    Toast.makeText(ForgetActivity.this, "Invalid email information", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    try {
                        if (m_jsonArrayForget.has("message")) {
                            String messaage = m_jsonArrayForget.getString("message");
                            AppUtil.showDialog(mContext,
                                    "Password Reset Email Sent",
                                    messaage,
                                    "OK", null,
                                    new AppDialogClickListiner() {
                                        @Override
                                        public void onDialogClick(AppDialogUserActions which) {
                                            if (which == AppDialogUserActions.OK) {
                                                showLoginScreen();
                                            }
                                        }
                                    }
                            );
                        } else {
                            showDialogToUser();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showDialogToUser();
                    }
                    Log.d("forgot pswd::::", ":::" + m_jsonArrayForget);
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        }
    }
}

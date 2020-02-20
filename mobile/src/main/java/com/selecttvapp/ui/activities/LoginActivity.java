package com.selecttvapp.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.network.dialogs.AppUtil;
import com.selecttvapp.BuildConfig;
import com.selecttvapp.R;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.common.InternetConnection;
import com.selecttvapp.common.PreferenceManager;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.common.Utils;
import com.selecttvapp.presentation.activities.PresenterLogin;
import com.selecttvapp.presentation.views.ViewLoginListener;

//import static com.arvig.R.string.need_help_url;


public class LoginActivity extends FragmentActivity implements View.OnClickListener, ViewLoginListener {
    private PresenterLogin presenter;
    private Context mContext;
    private Button btnLogin;
    private EditText editUsername, editPassword;
    private TextView btnForgetPassword, btnCreateAccount, btnNeedHelp;
    private TextView labelLoginInto;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = new PresenterLogin(this);
        if (presenter.isUserAlreadyLoggedIn(this))
            presenter.moveToNextScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_login);
        mContext = this;

        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editPassword.setTypeface(Typeface.DEFAULT);
        btnForgetPassword = (TextView) findViewById(R.id.btnForgetPassword);
        btnCreateAccount = (TextView) findViewById(R.id.btnCreateAccount);
        btnNeedHelp = (TextView) findViewById(R.id.btnNeedHelp);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        labelLoginInto = (TextView) findViewById(R.id.labelLoginInto);
        //used to give focus to the user email
        Utils.requestfocus(editUsername);

        /*set click listener*/
        btnLogin.setOnClickListener(this);
        btnForgetPassword.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);
        btnNeedHelp.setOnClickListener(this);

        /*set font styles*/
        presenter.setFont(FontHelper.ROBOTO, editUsername, editPassword);
        presenter.setFont(FontHelper.ARIALREGULAR, btnLogin);
        presenter.setFont(FontHelper.OPENSANS, btnCreateAccount);
        presenter.setFont(FontHelper.HELVETICA, labelLoginInto);
        presenter.setFont(FontHelper.HELVETICA_BOLD, btnForgetPassword);

        if (getPackageName().equals("com.selecttvapp")) {
            btnNeedHelp.setText("Create your account");
            btnNeedHelp.setCompoundDrawables(null, null, null, null);
        }

        editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    loginValidation();
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.LOGIN_SCREEN);
    }


    @Override
    public void onClick(View view) {
        if (view == btnCreateAccount) {
            showCreateAccountScreen();
        } else if (view == btnLogin) {
            loginValidation();
        } else if (view == btnForgetPassword) {
            startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
        } else if (view == btnNeedHelp) {
            if (getPackageName().equalsIgnoreCase(BuildConfig.SELECTTV_PACKAGE))
                showCreateAccountScreen();
            else
                showNeedHelp(getResources().getString(R.string.need_help_url));
//                presenter.getNeedHelpURI(this, getString(R.string.need_help_url1));
        }
    }

    @Override
    public void onLoginSuccess() {
        presenter.loadUserDetails();
        presenter.moveToNextScreen(this);
    }

    @Override
    public void onLoginFailed(String failedMessage) {
        Toast.makeText(this, failedMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNeedHelp(String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            if (i != null) {
                i.setData(Uri.parse(url));
                System.out.println("bsbsb" + url);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*validate username and password*/
    private void loginValidation() {
        PreferenceManager.setFirstLogin(true);
        AppUtil.hideKeyBoard(mContext, editPassword);
        editUsername.setError(null);
        editPassword.setError(null);

        String szEmail = editUsername.getText().toString();
        String szPassword = editPassword.getText().toString();
        if (szEmail.length() == 0) {
            editUsername.setError("Input Email Address!!!");
            return;
        }else if(!Utilities.validateEmail(szEmail)){
            editUsername.setError("Input Valid Email Address!!!");
            return;
        }
        if (szPassword.length() == 0) {
            editPassword.setError("Input Password");
            return;
        }
        username = szEmail;
        password = szPassword;
        if (InternetConnection.isConnected(this))
            presenter.userLogin(this, username, password);
        else Toast.makeText(this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
    }


    private void showAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Need Help?")
                .setMessage("")
                .setPositiveButton("Customer Support", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showCreateAccountScreen();
                    }
                })
                .setNegativeButton("Billing", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void showCreateAccountScreen() {
        try {
            String url = getString(R.string.register_url);
            Intent i = new Intent(Intent.ACTION_VIEW);
            if (i != null) {
                i.setData(Uri.parse(url));
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "No Application found to perform this action", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

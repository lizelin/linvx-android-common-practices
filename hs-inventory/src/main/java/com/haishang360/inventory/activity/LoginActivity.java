package com.haishang360.inventory.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haishang360.inventory.R;
import com.haishang360.inventory.engine.AppHelper;
import com.haishang360.inventory.engine.Constants;
import com.haishang360.inventory.engine.HttpTask;

import net.linvx.android.libs.http.LnxHttpResponse;
import net.linvx.android.libs.utils.AppUtils;
import net.linvx.android.libs.utils.NumberUtils;
import net.linvx.android.libs.utils.SharedPrefUtils;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends HsBaseActivity implements HttpTask.HttpResponseHandler{

    // UI references.
    @Bind(R.id.account)
    AutoCompleteTextView mAccountView;
    @Bind(R.id.password)
    EditText mPasswordView;
    @Bind(R.id.login_form)
     View mLoginFormView;
    @Bind(R.id.sign_in_button)
     Button mAccountSignInButton;

    @OnClick(R.id.sign_in_button)
    public void onClick(View view) {
        attemptLogin();
    }

    @OnEditorAction(R.id.password)
    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
            attemptLogin();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mAccountView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String account = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        } else if (!isEmailValid(account)) {
            mAccountView.setError(getString(R.string.error_invalid_account));
            focusView = mAccountView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            String postData = String.format("useraccount=ssss", account, password);
            String postData = String.format("useraccount=%s&password=%s", account, password);
            new HttpTask(Constants.URL_SERVER_PREFIX+"login",
                    postData,
                    this, this, Constants.TID_LOGIN).exec();
        }
    }

    private boolean isEmailValid(String account) {
        //TODO: Replace this with your own logic
        return NumberUtils.isNumeric(account);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 4;
    }


    @Override
    public void handleResponse(LnxHttpResponse response) {
        if (!AppHelper.preParseResponse(this, response))
            return;
        JSONObject json = (JSONObject) response.getExtraData(Constants.HTTP_EXTRA_DATA_JSON_KEY);
        AppUtils.showMsg(this, json.optString("message"));
        SharedPrefUtils.saveField(this, Constants.CURR_USER_ACCOUNT, mAccountView.getText().toString());
        this.finish();
    }
}


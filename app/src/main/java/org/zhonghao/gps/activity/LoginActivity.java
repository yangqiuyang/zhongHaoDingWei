package org.zhonghao.gps.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zhonghao.gps.R;
import org.zhonghao.gps.application.MyActivity;
import org.zhonghao.gps.application.MyApplication;
import org.zhonghao.gps.biz.ServelBiz;
import org.zhonghao.gps.entity.LoginResponse;
import org.zhonghao.gps.entity.MyEvent;
import org.zhonghao.gps.entity.NameDates;
import org.zhonghao.gps.entity.ResponseUserinfo;
import org.zhonghao.gps.entity.WarningResponseData;
import org.zhonghao.gps.utils.ISendData;
import org.zhonghao.gps.utils.MyJsonResponse;
import org.zhonghao.gps.utils.ProgressUtils;
import org.zhonghao.gps.utils.Urls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static org.zhonghao.gps.application.MyApplication.nameDates;
import static org.zhonghao.gps.application.MyApplication.warnResponseData;

/**
 * A login screen that offers login via user_name/password.
 */
public class LoginActivity extends MyActivity implements LoaderCallbacks<Cursor> {



    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsername;
    private EditText mPasswordView;
    private View mLoginFormView;
    public ResponseUserinfo responseUserinfo = null;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass, automatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_login);
        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //d
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        // Set up the login form.
        mUsername = (AutoCompleteTextView) findViewById(R.id.user_name);
        mPasswordView = (EditText) findViewById(R.id.password);
        //编辑完后，点击软键盘的返回键才会触发
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                //IME_NULL enter键
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button confirm = (Button) findViewById(R.id.btn_login);
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemeber = pref.getBoolean("remember", false);//判断是否记录密码
        boolean isAuto = pref.getBoolean("automatic", false);//判断是否自动登录
        if (isRemeber) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            mUsername.setText(account);
            mPasswordView.setText(password);
        }
        rememberPass = (CheckBox) findViewById(R.id.cb_remember);
        automatic = (CheckBox) findViewById(R.id.automatic);
        rememberPass.setChecked(isRemeber);

        if (isAuto){
            attemptLogin();
        }
        automatic.setChecked(isAuto);

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid user_name, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsername.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsername.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        // Check for a valid user_name address.
        if (TextUtils.isEmpty(username)) {
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsername.setError(getString(R.string.error_invalid_email));
            focusView = mUsername;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            editor = pref.edit();
            if (automatic.isChecked()) {
                editor = pref.edit();
                editor.putBoolean("automatic", true);
            } else {
                editor = pref.edit();
                editor.putBoolean("automatic", false);
            }
            if (rememberPass.isChecked()) {
                editor.putBoolean("remember", true);
                editor.putString("account", username);
                editor.putString("password", password);
            } else {
                editor.clear();
            }
            editor.commit();
            ProgressUtils.showProgress(this);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return username != null;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only user_name addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary user_name addresses first. Note that there won't be
                // a primary user_name address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUsername.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    LoginResponse loginResponse;//登录请求的数据
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        UserLoginTask(String username, String password) {
            nameDates.setPassword(password);//密码
            nameDates.setUsername(username);//姓名
            ArrayList list = new ArrayList<NameDates>();
            list.add(nameDates);
            responseUserinfo = new ResponseUserinfo();//上传数据
            responseUserinfo.setUserinfo(list);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
               // initWarning();
                 loginResponse = ServelBiz.LoginBiz(responseUserinfo, LoginActivity.this);
                 return loginResponse.getState();
            } catch (Exception e) {
                return false;
            }


            // TODO: register the new account here.
        }

        //获取设备信息,登录成功后，页面跳转
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                //initWarning();
                jumpIntent();

            } else {
                /*if (MyApplication.responseState) {
                    mPasswordView.setError("用户名或密码不正确");
                    mPasswordView.requestFocus();
                } else {*/
                    Toast.makeText(LoginActivity.this, "与服务器暂时连接不上，请休息下再试哦~", Toast.LENGTH_SHORT).show();
                    ProgressUtils.hideProgress();//隐藏进度条
               /* }*/

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    private void jumpIntent() {
        Intent intent = new Intent(LoginActivity.this, MapActivity.class);
        startActivity(intent);
        ProgressUtils.hideProgress();//隐藏进度条
        finish();
    }

}


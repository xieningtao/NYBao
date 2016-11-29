package com.ny.nybao;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.basesmartframe.baseui.BaseActivity;
import com.maxleap.LogInCallback;
import com.maxleap.MLUser;
import com.maxleap.MLUserManager;
import com.maxleap.exception.MLException;
import com.maxleap.im.DataHandler;
import com.maxleap.im.MLParrot;
import com.maxleap.im.ParrotException;
import com.sf.loglib.L;
import com.sf.utils.baseutil.SFToast;
import com.sflib.CustomView.baseview.EditTextClearDroidView;

/**
 * Created by NetEase on 2016/11/29 0029.
 */

public class NYLoginActivity extends BaseActivity {

    private Button mLoginBt;
    private EditTextClearDroidView mUserName;
    private EditTextClearDroidView mPwd;
    private TextView mRegisterTv;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ny_login_activity);
        initView();
    }

    private void initView() {
        mLoginBt = (Button) findViewById(R.id.login_bt);
        mUserName = (EditTextClearDroidView) findViewById(R.id.login_atv);
        mPwd = (EditTextClearDroidView) findViewById(R.id.pwd_cdv);
        mRegisterTv = (TextView) findViewById(R.id.register_tv);

        mLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mUserName.getEditText().getText())) {
                    SFToast.showToast(R.string.user_name_input_hint);
                    return;
                }

                if (TextUtils.isEmpty(mPwd.getEditText().getText())) {
                    SFToast.showToast(R.string.pwd_input_hint);
                    return;
                }
                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(NYLoginActivity.this);
                }
                mProgressDialog.show();
                String userName = mUserName.getEditText().getText().toString();
                String pwd = mPwd.getEditText().getText().toString();
                doLogin(userName, pwd);
            }
        });
        mRegisterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NYLoginActivity.this, NYRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mPwd.getEditText().setHint(R.string.pwd_input_hint);
        mPwd.getEditText().addTextChangedListener(mTextWatcher);
    }

    private void doLogin(final String userName, final String pwd) {
        MLUserManager.logInInBackground(userName, pwd, new LogInCallback() {
            public void done(MLUser user, MLException e) {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (user != null) {
                    // 登录成功
                    SFToast.showToast(R.string.login_success_tip);
                    doChatLogin(userName, pwd);
                } else {
                    // 登录失败
                    SFToast.showToast(R.string.login_fail_tip);
                    L.error(TAG, "doLogin exception: " + e);
                }
            }
        });
    }

    private void doChatLogin(String username, String password) {
        MLParrot parrot = MLParrot.getInstance();
        parrot.initWithMLUser(SFApp.APP_ID, SFApp.APP_ID_KEY, username, password);
        parrot.login(new DataHandler<String>() {
            @Override
            public void onSuccess(String id) {
                L.info(TAG, "doChatLogin.onSuccess id: " + id);
            }

            @Override
            public void onError(ParrotException e) {
                L.error(TAG, "doChatLogin.onError exception: " + e);
            }
        });
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateLoginState();
        }
    };

    private void updateLoginState() {
        if (!TextUtils.isEmpty(mUserName.getEditText().getText()) && !TextUtils.isEmpty(mPwd.getEditText().getText())) {
            mLoginBt.setEnabled(true);
        } else {
            mLoginBt.setEnabled(false);
        }
    }

}

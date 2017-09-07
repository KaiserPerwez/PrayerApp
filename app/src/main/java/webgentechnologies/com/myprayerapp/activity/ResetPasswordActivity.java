package webgentechnologies.com.myprayerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;
import webgentechnologies.com.myprayerapp.networking.VolleyUtils;

//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

public class ResetPasswordActivity extends AppCompatActivity {
    Button m_btn_resetPwd;
    String m_verify_mode, txt_password_change;
    Context m_ctx;
    EditText txt_resetPwd, txt_resetPwd_retype;
    TextView tv_pwd_chk;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        setCustomDesign();
        txt_resetPwd = (EditText) findViewById(R.id.txt_resetPwd);
        txt_resetPwd_retype = (EditText) findViewById(R.id.txt_resetPwd_retype);
        tv_pwd_chk = (TextView) findViewById(R.id.tv_pwd_chk);
        m_btn_resetPwd = (Button) findViewById(R.id.btn_resetPwd);
        // btn_resetPwd.setEnabled(false);

        txt_resetPwd_retype.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txt_resetPwd.getText().toString().equals(txt_resetPwd_retype.getText().toString())) {
                    tv_pwd_chk.setText("Password matched successfully");
                    //  _userSingletonModelClass.setTxt_change_pswd(txt_resetPwd_retype.getText().toString());
                    txt_password_change = txt_resetPwd_retype.getText().toString();
                    // btn_resetPwd.setEnabled(true);
                } else {
                    tv_pwd_chk.setText("Incorrect password");
                    //  btn_resetPwd.setEnabled(false);


                }
            }
        });
        setCustomClickListeners();
    }

    private void setCustomDesign() {
        m_ctx = ResetPasswordActivity.this;
        m_verify_mode = this.getIntent().getStringExtra("verify_mode");

        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        ((TextView) findViewById(R.id.tv_resetPwd)).setTypeface(regular_font);
        if (m_verify_mode.equals("change_pwd")) {
            ((TextView) findViewById(R.id.tv_resetPwd)).setText("Change Password");
            ((TextView) findViewById(R.id.btn_resetPwd)).setText("Change Password");
        } else {
            ((TextView) findViewById(R.id.tv_resetPwd)).setText("Reset Password");
            ((TextView) findViewById(R.id.btn_resetPwd)).setText("Reset Password");
        }


        ((TextView) findViewById(R.id.txt_resetPwd)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_resetPwd_retype)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.btn_resetPwd)).setTypeface(regular_font);

    }

    private void setCustomClickListeners() {
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                if (m_verify_mode.equals("change_pwd")) {
                    finish();
                } else {
                    Intent intent = new Intent(m_ctx, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //   btn_resetPwd = (Button) findViewById(R.id.btn_resetPwd);
        m_btn_resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
               /* if (verify_mode.equals("change_pwd")) {
                    Intent intent = new Intent(_ctx, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(_ctx, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }*/
                if (txt_resetPwd.getText().toString().length() > 0 && txt_resetPwd_retype.getText().toString().length() > 0 && txt_resetPwd.getText().toString().equals(txt_resetPwd_retype.getText().toString())) {
                    txt_password_change = txt_resetPwd_retype.getText().toString();
                    resetPassword();
                } else if (txt_resetPwd.getText().toString().length() == 0 && txt_resetPwd_retype.getText().toString().length() == 0) {
                    txt_resetPwd.setError("Fields cann't be empty");
                    txt_resetPwd_retype.setError("Fields cann't be empty");
                } else if (txt_resetPwd.getText().toString().length() > 0 && txt_resetPwd_retype.getText().toString().length() == 0)
                    txt_resetPwd_retype.setError("Fields cann't be empty");
                else if (txt_resetPwd.getText().toString().length() == 0 && txt_resetPwd_retype.getText().toString().length() > 0)
                    txt_resetPwd.setError("Fields cann't be empty");

            }
        });
    }

    /*
*Volley code to reset password
*/
    public void resetPassword() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_CHANGE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ResetPasswordActivity.this, response, Toast.LENGTH_LONG).show();
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");
                    if (status.equals("true")) {
                        Intent intent = new Intent(m_ctx, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(ResetPasswordActivity.this, "Error!Try again", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ResetPasswordActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("password", txt_password_change);
                params.put("user_id", userclass.getTxt_user_login_id());
                params.put("access_token", userclass.getTxt_user_access_token());
                params.put("device_id", "245");
                params.put("device_type", "Android");

                return params;
            }
        };
        VolleyUtils.getInstance(ResetPasswordActivity.this).addToRequestQueue(stringRequest);//
    }
//----------Volley code to reset password ends------------
void hideSoftKeyboard() {
    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
}
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideSoftKeyboard();
        return super.onTouchEvent(event);
    }
}


package webgentechnologies.com.myprayerapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import webgentechnologies.com.myprayerapp.networking.UrlConstants;
import webgentechnologies.com.myprayerapp.networking.VolleyUtils;

//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

public class ForgotPasswordTwoActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_resetPwd;
    String verify_mode, txt_password_change;
    Context _ctx;
    EditText txt_resetPwd, txt_resetPwd_retype;
    TextView tv_pwd_chk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        setCustomDesign();
        txt_resetPwd = (EditText) findViewById(R.id.txt_resetPwd);
        txt_resetPwd_retype = (EditText) findViewById(R.id.txt_resetPwd_retype);
        tv_pwd_chk = (TextView) findViewById(R.id.tv_pwd_chk);

        btn_resetPwd = (Button) findViewById(R.id.btn_resetPwd);
        btn_resetPwd.setOnClickListener(this);

        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(this);
    }

    private void setCustomDesign() {
        _ctx = ForgotPasswordTwoActivity.this;
        verify_mode = "change_pwd";//this.getIntent().getStringExtra("verify_mode");

        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        ((TextView) findViewById(R.id.tv_resetPwd)).setTypeface(regular_font);
        if (verify_mode.equals("change_pwd")) {
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

    void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideSoftKeyboard();
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        hideSoftKeyboard();
        int id = v.getId();
        switch (id) {
            case R.id.btn_resetPwd:
                String pass1 = txt_resetPwd.getText().toString();
                String pass2 = txt_resetPwd_retype.getText().toString();

                if (pass1.length() == 0) {
                    txt_resetPwd.setError("Password can't be empty");
                    return;
                }
                if (pass2.toString().length() == 0) {
                    txt_resetPwd_retype.setError("Re-typed Password can't be empty");
                    return;
                }
                if (pass1.length() != pass2.length()) {
                    Toast.makeText(_ctx, "Passwords were of different length", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass1.equals(pass2)) {
                    Toast.makeText(_ctx, "Passwords Matched.Resetting..", Toast.LENGTH_SHORT).show();
                    txt_password_change = txt_resetPwd_retype.getText().toString();
                    resetPassword();
                }
                break;
            case R.id.imageButtonPrev:
                if (verify_mode.equals("change_pwd")) {
                    finish();
                } else {
                    Intent intent = new Intent(_ctx, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    /*
*Volley code to reset password
*/
    public void resetPassword() {
        final ProgressDialog _progressDialog = new ProgressDialog(_ctx, ProgressDialog.STYLE_SPINNER);
        _progressDialog.setMessage("Checking...");
        _progressDialog.setCancelable(false);
        _progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_CHANGE_PASSWORD_FOR_FORGOT_PASSWORD_USERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(_ctx, response, Toast.LENGTH_LONG).show();
                if (_progressDialog.isShowing())
                    _progressDialog.cancel();
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");
                    if (status.equals("true")) {
                        Intent intent = new Intent(_ctx, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(_ctx, "Error!Try again", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(_ctx, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (_progressDialog.isShowing())
                    _progressDialog.cancel();
                Toast.makeText(_ctx, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("password", txt_password_change);
                params.put("email", ForgotPasswordOneActivity._txt_verifyEmail);
                params.put("device_id", "245");
                params.put("device_type", "Android");

                return params;
            }
        };
        VolleyUtils.getInstance(_ctx).addToRequestQueue(stringRequest);//
    }
}


package com.wgt.myprayerapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.wgt.myprayerapp.R;
import com.wgt.myprayerapp.Utils.CustomUtils;
import com.wgt.myprayerapp.Utils.ValidatorUtils;
import com.wgt.myprayerapp.model.UserSingletonModelClass;
import com.wgt.myprayerapp.networking.UrlConstants;
import com.wgt.myprayerapp.networking.VolleyUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordOneActivity extends AppCompatActivity implements View.OnClickListener {
    static String _txt_verifyEmail;
    Context _ctx;
    Button btn_getOtp;
    TextView btn_backToLogin;
    EditText txt_verifyEmail, txt_otp;
    UserSingletonModelClass _userSingletonModelClass = UserSingletonModelClass.get_userSingletonModelClass();
    String txt_otp_from_email;
    Button btn_verify;
    ProgressDialog _progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setCustomDesign();
        btn_getOtp = findViewById(R.id.btn_getOtp);
        btn_backToLogin = findViewById(R.id.tv_backToLogin);
        txt_verifyEmail = findViewById(R.id.txt_verifyEmail);
        btn_getOtp.setOnClickListener(this);
        btn_backToLogin.setOnClickListener(this);
    }

    private void setCustomDesign() {
        _ctx = ForgotPasswordOneActivity.this;
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface semiBold_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-SemiBold.ttf");

        ((TextView) findViewById(R.id.tv_forgotPwd)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_verifyEmail)).setTypeface(semiBold_font);
        ((TextView) findViewById(R.id.btn_getOtp)).setTypeface(semiBold_font);
        ((TextView) findViewById(R.id.tv_backToLogin)).setTypeface(regular_font);
    }

    @Override
    public void onClick(View v) {
        hideSoftKeyboard();
        int item = v.getId();
        switch (item) {
            case R.id.btn_getOtp:
                _txt_verifyEmail = txt_verifyEmail.getText().toString();
                txt_verifyEmail.setError(null);
                if (_txt_verifyEmail.length() == 0) {
                    txt_verifyEmail.setError("Email can't be empty");
                    return;
                }
                if (!ValidatorUtils.isValidEmail(_txt_verifyEmail)) {
                    txt_verifyEmail.setError("INVALID Email");
                    return;
                }
                getOtpAtEmail();
                break;
            case R.id.tv_backToLogin:
                finish();
        }
    }

    /*
   *Volley code for Getting otp to email
    */
    public void getOtpAtEmail() {
        _progressDialog = new ProgressDialog(_ctx, ProgressDialog.STYLE_SPINNER);
        _progressDialog.setMessage("Checking...");
        _progressDialog.setCancelable(false);
        _progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_OTP_FORGOT_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (_progressDialog.isShowing())
                    _progressDialog.cancel();
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                        LayoutInflater li = LayoutInflater.from(_ctx);
                        View promptsView = li.inflate(R.layout.verify_email_dialog, null);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_ctx);
                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(promptsView);
                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false);
                        // create alert dialog
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                        txt_otp = promptsView.findViewById(R.id.txt_otp);
                        btn_verify = promptsView.findViewById(R.id.btn_verify);
                        Button btn_back = promptsView.findViewById(R.id.btn_back);
                        TextView tv_email_dialog_title = promptsView.findViewById(R.id.tv_email_dialog_title);
                        tv_email_dialog_title.setText("Verify OTP");
                        btn_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                im.hideSoftInputFromWindow(alertDialog.getWindow().getDecorView().getWindowToken(), 0);
                                alertDialog.dismiss();
                            }
                        });
                        btn_verify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                im.hideSoftInputFromWindow(alertDialog.getWindow().getDecorView().getWindowToken(), 0);
                                txt_otp.clearFocus();
                                btn_verify.requestFocus();
                                txt_otp_from_email = txt_otp.getText().toString();
                                if (txt_otp_from_email.length() == 0) {
                                    txt_otp.setError("OTP can't be empty");
                                } else {
                                    _progressDialog.show();
                                    verifyOtp(alertDialog);
                                    //alertDialog.dismiss();
                                }
                            }
                        });
                    } else
                        txt_verifyEmail.setError("Email not found at our database");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(_ctx, "Err:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (_progressDialog.isShowing())
                    _progressDialog.cancel();
                Toast.makeText(_ctx, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", _txt_verifyEmail);
                params.put("device_id", "245");
                params.put("device_type", "Android");

                return params;
            }
        };

        VolleyUtils.getInstance(this).addToRequestQueue(stringRequest);
    }
//----------Volley code for Getting otp to email ends------------


    /*
    Volley code for verifying otp
     */
    public void verifyOtp(final AlertDialog alertDialog) {
        hideSoftKeyboard();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_OTP_VERIFY_FORGOTPASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (_progressDialog.isShowing())
                    _progressDialog.cancel();
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                        alertDialog.dismiss();
                        startActivity(new Intent(_ctx, ForgotPasswordTwoActivity.class));
                    } else {
                        CustomUtils.alert(_ctx, "ERROR", "Incorrect otp...please verify again");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (_progressDialog.isShowing())
                    _progressDialog.cancel();
                Toast.makeText(_ctx, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", txt_otp_from_email);
                params.put("email", _txt_verifyEmail);
                params.put("device_id", "245");
                params.put("device_type", "Android");
                return params;
            }
        };
        VolleyUtils.getInstance(this).addToRequestQueue(stringRequest);
    }

    //------------------Volley code for verifying otp ends---------------------
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
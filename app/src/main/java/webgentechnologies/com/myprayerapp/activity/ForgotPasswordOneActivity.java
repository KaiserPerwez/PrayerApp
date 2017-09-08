package webgentechnologies.com.myprayerapp.activity;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.Utils.ValidatorUtils;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;
import webgentechnologies.com.myprayerapp.networking.VolleyUtils;

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
        btn_getOtp = (Button) findViewById(R.id.btn_getOtp);
        btn_backToLogin = (TextView) findViewById(R.id.tv_backToLogin);
        txt_verifyEmail = (EditText) findViewById(R.id.txt_verifyEmail);
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
                        txt_otp = (EditText) promptsView.findViewById(R.id.txt_otp);
                        btn_verify = (Button) promptsView.findViewById(R.id.btn_verify);
                        Button btn_back = (Button) promptsView.findViewById(R.id.btn_back);
                        btn_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideSoftKeyboard();
                                alertDialog.dismiss();
                            }
                        });
                        btn_verify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideSoftKeyboard();
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
                    Toast.makeText(_ctx, "Err:" + e.getMessage(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(_ctx, "success", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(_ctx, "Incorrect otp...please verify again", Toast.LENGTH_LONG).show();
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
                Toast.makeText(_ctx, error.toString(), Toast.LENGTH_LONG).show();
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
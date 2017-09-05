package webgentechnologies.com.myprayerapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    Context m_ctx;
    Button m_btn_getOtp;
    TextView m_btn_backToLogin;
    EditText m_txt_verifyEmail;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setCustomDesign();
        // setCustomClickListeners();
        m_btn_getOtp = (Button) findViewById(R.id.btn_getOtp);
        m_btn_backToLogin = (TextView) findViewById(R.id.tv_backToLogin);
        m_txt_verifyEmail = (EditText) findViewById(R.id.txt_verifyEmail);
        m_btn_getOtp.setOnClickListener(this);
        m_btn_backToLogin.setOnClickListener(this);
    }

    private void setCustomClickListeners() {
        ((TextView) findViewById(R.id.tv_backToLogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        m_btn_getOtp = (Button) findViewById(R.id.btn_getOtp);
        m_btn_getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(m_ctx);
                View promptsView = li.inflate(R.layout.verify_email_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        m_ctx);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false);
                // create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();

                Button btn_verify = (Button) promptsView.findViewById(R.id.btn_verify);
                Button btn_back = (Button) promptsView.findViewById(R.id.btn_back);
                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                btn_verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(m_ctx, ResetPasswordActivity.class);
                        intent.putExtra("verify_mode", "forgot_pwd");
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void setCustomDesign() {
        m_ctx = ForgotPasswordActivity.this;
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface semiBold_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-SemiBold.ttf");

        ((TextView) findViewById(R.id.tv_forgotPwd)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_verifyEmail)).setTypeface(semiBold_font);
        ((TextView) findViewById(R.id.btn_getOtp)).setTypeface(semiBold_font);
        ((TextView) findViewById(R.id.tv_backToLogin)).setTypeface(regular_font);
    }

    @Override
    public void onClick(View v) {
        int item = v.getId();
        switch (item) {
            case R.id.btn_getOtp:
                // get prompts.xml view
                userclass.setTxt_email_for_forgot_password(m_txt_verifyEmail.getText().toString());
                getOtptoemail();
                LayoutInflater li = LayoutInflater.from(m_ctx);
                View promptsView = li.inflate(R.layout.verify_email_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        m_ctx);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false);
                // create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
                final EditText txt_otp = (EditText) promptsView.findViewById(R.id.txt_otp);
                Button btn_verify = (Button) promptsView.findViewById(R.id.btn_verify);
                Button btn_back = (Button) promptsView.findViewById(R.id.btn_back);
                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                btn_verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userclass.setTxt_otp_from_email(txt_otp.getText().toString());
                        Intent intent = new Intent(m_ctx, ResetPasswordActivity.class);
                        intent.putExtra("verify_mode", "forgot_pwd");
                        startActivity(intent);
                    }
                });
                break;
            case R.id.tv_backToLogin:
                finish();
        }
    }

    /*
   *Volley code for Getting otp to email
    */
    public void getOtptoemail() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_OTP_IN_USER_EMAIL_FORGOT_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ForgotPasswordActivity.this, response, Toast.LENGTH_LONG).show();
              /*  try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                       // startActivity(new Intent(ForgotPasswordActivity.this, HomeActivity.class));
                        JSONObject jobdata = job.getJSONObject("data");
                        userclass.setTxt_user_login_id(jobdata.getString("id"));
                        userclass.setTxt_user_access_token(jobdata.getString("accessToken"));
                        userclass.setTxt_temp_user_login_email(jobdata.getString("email"));
                        userclass.setTxt_fcbk_lgn_email(userclass.getTxt_temp_user_login_email());
                    } else
                        Toast.makeText(getApplicationContext(), "Incorrect email_id or password", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
              /*  params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL, email);*/
                params.put("email", userclass.getTxt_email_for_forgot_password());
                params.put("device_id", "245");
                params.put("device_type", "Android");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
//----------Volley code for Getting otp to email ends------------


    /*
    Volley code for verifying otp
     */
    public void verifyOtp() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_OTP_IN_FORGOT_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ForgotPasswordActivity.this, response, Toast.LENGTH_LONG).show();
              /*  try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                       // startActivity(new Intent(ForgotPasswordActivity.this, HomeActivity.class));
                        JSONObject jobdata = job.getJSONObject("data");
                        userclass.setTxt_user_login_id(jobdata.getString("id"));
                        userclass.setTxt_user_access_token(jobdata.getString("accessToken"));
                        userclass.setTxt_temp_user_login_email(jobdata.getString("email"));
                        userclass.setTxt_fcbk_lgn_email(userclass.getTxt_temp_user_login_email());
                    } else
                        Toast.makeText(getApplicationContext(), "Incorrect email_id or password", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
              /*  params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL, email);*/
                params.put("otp", "");
                params.put("email", userclass.getTxt_email_for_forgot_password());
                params.put("device_id", "245");
                params.put("device_type", "Android");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //------------------Volley code for verifying otp ends---------------------
}

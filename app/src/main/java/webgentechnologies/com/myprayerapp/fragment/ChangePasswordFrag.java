package webgentechnologies.com.myprayerapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import webgentechnologies.com.myprayerapp.activity.ResetPasswordActivity;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;
import webgentechnologies.com.myprayerapp.networking.VolleyUtils;

//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

public class ChangePasswordFrag extends Fragment {

    View rootView;
    Context m_ctx;
    TextView tv_user_emailid;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();
    String txt_otp_verify;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_change_password, container, false);
        tv_user_emailid = (TextView) rootView.findViewById(R.id.tv_usermail);
        setCustomDesign();
        setCustomClickListeners();
        tv_user_emailid.setText(userclass.getTxt_email());

        return rootView;
    }

    private void setCustomDesign() {
        m_ctx = rootView.getContext();
        Typeface regular_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface semiBold_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-SemiBold.ttf");

        ((TextView) rootView.findViewById(R.id.tv_forgotPwd)).setTypeface(regular_font);
        ((TextView) rootView.findViewById(R.id.tv_usermail)).setTypeface(semiBold_font);
        ((TextView) rootView.findViewById(R.id.btn_getOtp)).setTypeface(semiBold_font);
    }

    private void setCustomClickListeners() {
        Button btn_getOtp = (Button) rootView.findViewById(R.id.btn_getOtp);
        btn_getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Volley method calling for getting otp to email
                hideSoftKeyboard();
                getOtpToemailTochngpswd();
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
                final EditText txt_otp = (EditText) promptsView.findViewById(R.id.txt_otp);
                Button btn_verify = (Button) promptsView.findViewById(R.id.btn_verify);
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
                        alertDialog.dismiss();
                        txt_otp_verify = txt_otp.getText().toString();
                        //Volley method to verify otp is calling
                        verifyOtp();
                   /*     Intent intent = new Intent(m_ctx, ResetPasswordActivity.class);
                        intent.putExtra("verify_mode", "change_pwd");
                        startActivity(intent);*/
                    }
                });
            }
        });
    }

    /*
 *Volley code for Getting otp to email for password reset
  */
    public void getOtpToemailTochngpswd() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_OTP_FOR_CHANGE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
              /*  try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                       // startActivity(new Intent(ForgotPasswordOneActivity.this, HomeActivity.class));
                        JSONObject jobdata = job.getJSONObject("data");
                        _userSingletonModelClass.setTxt_user_login_id(jobdata.getString("id"));
                        _userSingletonModelClass.setTxt_user_access_token(jobdata.getString("accessToken"));
                        _userSingletonModelClass.setTxt_temp_user_login_email(jobdata.getString("email"));
                        _userSingletonModelClass.setTxt_fcbk_login_and_normal_login_email(_userSingletonModelClass.getTxt_temp_user_login_email());
                    } else
                        Toast.makeText(getApplicationContext(), "Incorrect email_id or password", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
              /*  params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL, email);*/
               // params.put("email", _userSingletonModelClass.getTxt_temp_user_login_email());
                params.put("email", userclass.getTxt_email());
                params.put("user_id", userclass.getTxt_user_login_id());
                params.put("access_token", userclass.getTxt_user_access_token());
                params.put("device_id", "245");
                params.put("device_type", "Android");

                return params;
            }
        };
        VolleyUtils.getInstance(getContext()).addToRequestQueue(stringRequest);//
    }
//----------Volley code for Getting otp to email for password change ends------------


    /*
*Volley code to verify otp
*/
    public void verifyOtp() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_OTP_RECEIVED_FROM_EMAIL_VERIFY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");
                    if (status.equals("true")) {
                        Intent intent = new Intent(m_ctx, ResetPasswordActivity.class);
                        intent.putExtra("verify_mode", "change_pwd");
                        startActivity(intent);
                    } else
                        Toast.makeText(getActivity().getApplicationContext(), "Check otp you have entered", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", txt_otp_verify);
                params.put("user_id", userclass.getTxt_user_login_id());
                params.put("access_token", userclass.getTxt_user_access_token());
                params.put("device_id", "245");
                params.put("device_type", "Android");

                return params;
            }
        };
        VolleyUtils.getInstance(getContext()).addToRequestQueue(stringRequest);//
    }
//----------Volley code to verify otp ends------------
void hideSoftKeyboard() {
    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
}
}

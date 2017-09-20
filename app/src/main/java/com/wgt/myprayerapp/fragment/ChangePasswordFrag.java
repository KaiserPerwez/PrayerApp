package com.wgt.myprayerapp.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.wgt.myprayerapp.R;
import com.wgt.myprayerapp.Utils.CustomUtils;
import com.wgt.myprayerapp.activity.ResetPasswordActivity;
import com.wgt.myprayerapp.model.UserSingletonModelClass;
import com.wgt.myprayerapp.networking.UrlConstants;
import com.wgt.myprayerapp.networking.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

public class ChangePasswordFrag extends Fragment implements View.OnTouchListener {

    View rootView;
    Context m_ctx;
    TextView tv_user_emailid;
    UserSingletonModelClass _userSingletonModelClass = UserSingletonModelClass.get_userSingletonModelClass();
    String txt_otp_verify;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_change_password, container, false);
        rootView.setOnTouchListener(this);//to detect touch on non-views

        progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);

        tv_user_emailid = (TextView) rootView.findViewById(R.id.tv_usermail);
        setCustomDesign();
        setCustomClickListeners();
        tv_user_emailid.setText(_userSingletonModelClass.getTxt_email());

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
                if (progressDialog != null)
                    progressDialog.show();

                getOtpToEmailToChangePwd();
            }
        });
    }

    /*
 *Volley code for Getting otp to email for password reset
  */
    public void getOtpToEmailToChangePwd() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_OTP_CHANGE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();

                JSONObject json_obj = null;
                try {
                    json_obj = new JSONObject(response);
                    String status = json_obj.getString("status");

                    if (status.equals("true")) {
                        //CustomUtils.alert(m_ctx,"Message","OTP has been sent to your email");
                        // get prompts.xml view
                        LayoutInflater li = LayoutInflater.from(m_ctx);
                        View promptsView = li.inflate(R.layout.verify_email_dialog, null);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(m_ctx);
                        alertDialogBuilder.setView(promptsView);// set prompts.xml to alertdialog builder

                        alertDialogBuilder.setCancelable(false);// set dialog message
                        final AlertDialog alertDialog = alertDialogBuilder.create();// create alert dialog
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
                                InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                im.hideSoftInputFromWindow(alertDialog.getWindow().getDecorView().getWindowToken(), 0);

                                txt_otp_verify = txt_otp.getText().toString();
                                txt_otp.setError(null);
                                if (txt_otp_verify.length() == 0) {
                                    txt_otp.setError("OTP must contain at least 4 characters");
                                    return;
                                }

                                if (progressDialog != null)
                                    progressDialog.show();
                                //Volley method to verify otp is calling

                                verifyOtp();
                            }
                        });
                    } else {
                        CustomUtils.alert(m_ctx, "Message", "Error while sending OTP.Please try again");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", _userSingletonModelClass.getTxt_email());
                params.put("user_id", _userSingletonModelClass.getTxt_user_login_id());
                params.put("access_token", _userSingletonModelClass.getTxt_user_access_token());
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_OTP_VERIFY_EMAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();

                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");
                    if (status.equals("true")) {
                        CustomUtils.alert(m_ctx, "Message", "Password changed successfully");
                        Intent intent = new Intent(m_ctx, ResetPasswordActivity.class);
                        intent.putExtra("verify_mode", "change_pwd");
                        startActivity(intent);
                    } else
                        CustomUtils.alert(m_ctx, "Error", "Incorrect OTP");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", txt_otp_verify);
                params.put("user_id", _userSingletonModelClass.getTxt_user_login_id());
                params.put("access_token", _userSingletonModelClass.getTxt_user_access_token());
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideSoftKeyboard();
        return false;
    }
}

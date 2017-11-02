package com.wgt.myprayerapp.activity;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.wgt.myprayerapp.R;
import com.wgt.myprayerapp.model.UserSingletonModelClass;
import com.wgt.myprayerapp.networking.UrlConstants;
import com.wgt.myprayerapp.networking.VolleyUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

public class RegnFourActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_signUp;
    Context _ctx;
    EditText txt_password, txt_password_retype;
    UserSingletonModelClass _userSingletonModelClass = UserSingletonModelClass.get_userSingletonModelClass();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regn_four);
        _ctx = RegnFourActivity.this;

        txt_password = (EditText) findViewById(R.id.txt_reg_pwd);
        txt_password_retype = (EditText) findViewById(R.id.txt_password_retype);
        setCustomDesign();
        btn_signUp = (Button) findViewById(R.id.btn_signUp);
        btn_signUp.setOnClickListener(this);
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(this);
        ImageView imageButtonPrevArrow = (ImageView) findViewById(R.id.imageButtonPrevArrow);
        imageButtonPrevArrow.setOnClickListener(this);

        TextView tv_AlreadyRegd = (TextView) findViewById(R.id.tv_AlreadyRegd);
        tv_AlreadyRegd.setOnClickListener(this);
        progressDialog = new ProgressDialog(_ctx, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Registering...");
    }

    private void setCustomDesign() {
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface semiBold_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-SemiBold.ttf");
        ((TextView) findViewById(R.id.tv_regn_four)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_regn_step4)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_reg_pwd)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_password_retype)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.btn_signUp)).setTypeface(semiBold_font);
        ((TextView) findViewById(R.id.tv_AlreadyRegd)).setTypeface(regular_font);
    }

    @Override
    public void onClick(View v) {
        hideSoftKeyboard();
        int item = v.getId();
        switch (item) {
            case R.id.btn_signUp:
                String password1 = txt_password.getText().toString();
                String password2 = txt_password_retype.getText().toString();
                if (password1.length() < 8) {
                    txt_password.requestFocus();
                    txt_password.setError("At least 8 characters required");
                    return;
                }
                if (password2.length() == 0) {
                    txt_password_retype.requestFocus();
                    txt_password_retype.setError("Retyped-Password can't be empty");
                    return;
                }
                if (!password2.equals(password1)) {
                    Toast.makeText(_ctx, "Password Mis-matched", Toast.LENGTH_SHORT).show();
                    return;
                }

                _userSingletonModelClass.setTxt_pswd(password1);
                registerUser();
                break;
            case R.id.imageButtonPrev:
            case R.id.imageButtonPrevArrow:
                finish();
                break;
            case R.id.tv_AlreadyRegd:
                Intent intent = new Intent(_ctx, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

        }
    }

    //Volley code for registration...
    public void registerUser() {
        progressDialog.show();
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_REGISTER_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");
                    if (status.equals("true")) {
                        Intent intent = new Intent(_ctx, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(_ctx, "Couldn't Register.Please try again.May be user already exists", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(_ctx, "Couldn't update.Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(_ctx, error.toString(), Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing())
                    progressDialog.cancel();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", _userSingletonModelClass.getTxt_fname());
                params.put("last_name", _userSingletonModelClass.getTxt_lname());
                params.put("email", _userSingletonModelClass.getTxt_email());
                params.put("country_id", _userSingletonModelClass.getTxt_country_id());
                params.put("country_name", _userSingletonModelClass.getTxt_country());
                params.put("address1", _userSingletonModelClass.getTxt_addr1());
                params.put("address2", _userSingletonModelClass.getTxt_addr2());
                params.put("city", _userSingletonModelClass.getTxt_city());
                params.put("state_id", _userSingletonModelClass.getTxt_state_id());
                params.put("state_name", _userSingletonModelClass.getTxt_state_name());
                params.put("phone", _userSingletonModelClass.getTxt_phone());
                params.put("church_name", _userSingletonModelClass.getChurch_id());

                String str = "";
                for (String s :
                        _userSingletonModelClass.getList_classes_attended()) {
                    str += (s + ";");
                }
                str = str.substring(0, str.length() - 1);
                params.put("classes", str);
                params.put("mission_trip", _userSingletonModelClass.getTxt_mission_trip_countries());
                params.put("mission_trip_status", _userSingletonModelClass.getTxt_mission_trip_participation_status());
                params.put("mission_concept", _userSingletonModelClass.getTxt_newto_mission());
                params.put("password", _userSingletonModelClass.getTxt_pswd());
                params.put("device_id", _userSingletonModelClass.getDevice_id());
                params.put("device_type", _userSingletonModelClass.getDevice_type());
                params.put("reg_type", _userSingletonModelClass.getReg_type());
                return params;
            }
        };
        VolleyUtils.getInstance(_ctx).addToRequestQueue(stringRequest);
    }
    //-----------Volley code for registration ends------------------

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



package webgentechnologies.com.myprayerapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;
import webgentechnologies.com.myprayerapp.networking.VolleyUtils;

public class RegnFourActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_signUp;
    Context ctx;
    EditText txt_password, txt_password_retype;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();
    static String password1, password2;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regn_four);
        txt_password = (EditText) findViewById(R.id.txt_reg_pwd);
        txt_password_retype = (EditText) findViewById(R.id.txt_password_retype);
        setCustomDesign();
        // setCustomClickListeners();
        btn_signUp = (Button) findViewById(R.id.btn_signUp);
        btn_signUp.setOnClickListener(this);
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(this);
        TextView tv_AlreadyRegd = (TextView) findViewById(R.id.tv_AlreadyRegd);
        tv_AlreadyRegd.setOnClickListener(this);
        progressDialog = new ProgressDialog(RegnFourActivity.this, ProgressDialog.STYLE_SPINNER);
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
        int item = v.getId();
        switch (item) {
            case R.id.btn_signUp:
                String password1 = txt_password.getText().toString();
                String password2 = txt_password_retype.getText().toString();
                if (password1.length() == 0)
                {
                    txt_password.setError("Password can't be empty");
                    return;
                }
                if (password2.length() == 0)
                {
                    txt_password_retype.setError("Retyped-Password can't be empty");
                    return;
                }
                if (!password2.equals(password1)) {
                    Toast.makeText(RegnFourActivity.this, "Password Mis-matched", Toast.LENGTH_SHORT).show();
                    return;
                }

                userclass.setTxt_pswd(password1);
                String str=userclass.toString();
                registerUser();
                break;
            case R.id.imageButtonPrev:
                finish();
                break;
            case R.id.tv_AlreadyRegd:
                Intent intent = new Intent(RegnFourActivity.this, LoginActivity.class);
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
                    String status=job.getString("status");
                    if(status.equals("true"))
                    {
                        Intent intent = new Intent(RegnFourActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(RegnFourActivity.this, "Couldn't Register.Please try again.May be user already exists", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(RegnFourActivity.this, "Couldn't update.Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegnFourActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                if (progressDialog.isShowing())
                    progressDialog.cancel();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
              /*  params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                param
                s.put(KEY_EMAIL, email);*/
                params.put("first_name", userclass.getTxt_fname());
                params.put("last_name", userclass.getTxt_lname());
                params.put("email", userclass.getTxt_email());
                params.put("country_id", userclass.getTxt_country_id());
                params.put("country_name", userclass.getTxt_country());
                params.put("address1", userclass.getTxt_addr1());
                params.put("address2", userclass.getTxt_addr2());
                params.put("city", userclass.getTxt_city());
                params.put("state_id", userclass.getTxt_state_id());
                params.put("state_name", userclass.getTxt_state_name());
                params.put("phone", userclass.getTxt_phone());
                params.put("church_name", userclass.getChurch_name());

                String str="";
                for (String s :
                        userclass.getList_classes_attended()) {
                    str += (s+";");
                }
                str=str.substring(0,str.length()-1);
                params.put("classes", str);
                params.put("mission_trip", userclass.getTxt_mission_trip_countries());
                params.put("mission_trip_status", userclass.getTxt_mission_trip_participation_status());
                params.put("mission_concept", userclass.getTxt_newto_mission());
                params.put("password", userclass.getTxt_pswd());
                params.put("device_id", userclass.getDevice_id());
                params.put("device_type", userclass.getDevice_type());
                params.put("reg_type",userclass.getReg_type());
                return params;
            }

//            private Map<String, String> checkParams(Map<String, String> map) {
//                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
//                while (it.hasNext()) {
//                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
//                    if (pairs.getValue() == null) {
//                        map.put(pairs.getKey(), "");
//                    }
//                }
//                return map;
//
//            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
//        VolleyUtils.getInstance(RegnFourActivity.this).addToRequestQueue(stringRequest);
    }
    //-----------Volley code for registration ends------------------
}



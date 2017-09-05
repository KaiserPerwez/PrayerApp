package webgentechnologies.com.myprayerapp.activity;

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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;

public class RegnFourActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_signUp;
    EditText txt_password, txt_password_retype;
    TextView txt_pwdcheck;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regn_four);
        txt_password = (EditText) findViewById(R.id.txt_reg_pwd);
        txt_password_retype = (EditText) findViewById(R.id.txt_password_retype);
        txt_pwdcheck = (TextView) findViewById(R.id.txt_pwdcheck);
        Toast.makeText(getApplicationContext(), userclass.getTxt_mission_trip(), Toast.LENGTH_LONG).show();
        btn_signUp = (Button) findViewById(R.id.btn_signUp);
        btn_signUp.setOnClickListener(this);
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(this);
        TextView tv_AlreadyRegd = (TextView) findViewById(R.id.tv_AlreadyRegd);
        tv_AlreadyRegd.setOnClickListener(this);

        setCustomDesign();
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
                if (passwordMatched()) {
                    txt_pwdcheck.setVisibility(View.GONE);
                    //registerUser();
                } else
                    txt_pwdcheck.setVisibility(View.VISIBLE);
                break;
            case R.id.imageButtonPrev:
                finish();
                break;
            case R.id.tv_AlreadyRegd:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

        }
    }

    private boolean passwordMatched() {
        String password1 = txt_password.getText().toString();
        String password2 = txt_password_retype.getText().toString();
        if (password2.equals(password1)) {
            userclass.setTxt_pswd(password2);
            return true;
        }
        return false;
    }

    //Volley code for registration...
    public void registerUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_REGISTER_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(RegnFourActivity.this, response, Toast.LENGTH_LONG).show();
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
                params.put("first_name", userclass.getTxt_fname());
                params.put("last_name", userclass.getTxt_lname());
                params.put("email", userclass.getTxt_email());
                params.put("country_id", userclass.getTxt_country_id1());
                params.put("country_name", userclass.getTxt_country());
                params.put("address1", userclass.getTxt_addr1());
                params.put("address2", userclass.getTxt_addr2());
                params.put("city", userclass.getTxt_city());
                params.put("state_id", userclass.getTxt_state_id());
                params.put("state_name", userclass.getTxt_state_name());
                params.put("phone", userclass.getTxt_phone());
                params.put("church_name", userclass.getChurch_name());
                params.put("classes", userclass.getTxt_classes_attended());
                params.put("mission_trip", userclass.getTxt_mission_trip());
                params.put("mission_concept", userclass.getTxt_newto_mission());
                params.put("password", userclass.getTxt_pswd());
                params.put("device_id", "245");
                params.put("device_type", "Android");
                params.put("reg_type", "Normal");
               /* params.put("first_name", "abc");
                params.put("last_name", "abg");
                params.put("email", "hag");
                params.put("country_id", "12");
                params.put("country_name", "sf");
                params.put("address1", "seefs");
                params.put("address2", "aef");
                params.put("city", "df");
                params.put("state_id", "234");
                params.put("state_name", "seef");
                params.put("phone", "234");
                params.put("church_name", "zdf");
                params.put("classes", "zdfaa");
                params.put("mission_trip", "sassa");
                params.put("mission_concept", "0");
                params.put("password", "srgsg");
                params.put("device_id", "245");
                params.put("device_type", "Android");
                params.put("reg_type", "Normal");*/
                return params;
            }

            private Map<String, String> checkEmptyParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //-----------Volley code for registration ends------------------
}



package com.wgt.myprayerapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.wgt.myprayerapp.R;
import com.wgt.myprayerapp.model.CountryModel;
import com.wgt.myprayerapp.model.UserSingletonModelClass;
import com.wgt.myprayerapp.networking.UrlConstants;
import com.wgt.myprayerapp.networking.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

public class EditThreeActivity extends AppCompatActivity implements View.OnClickListener {

    static String txt_mission_trip;
    Button btn_editProfile;
    Context _ctx;
    int[] i = {0};
    CheckBox txt_chk_new_to_mission;
    UserSingletonModelClass _userSingletonModelClass = UserSingletonModelClass.get_userSingletonModelClass();
    ProgressDialog progressDialog;
    Spinner spinner_country;
    String new_to_mission = "0", participation_status, country_mission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regn_three);
        _ctx = EditThreeActivity.this;

        setCustomDesign();
        //New code...
        txt_chk_new_to_mission = (CheckBox) findViewById(R.id.chk_new_to_mission);
        txt_chk_new_to_mission.setOnClickListener(this);

        FrameLayout imageButtonNext = (FrameLayout) findViewById(R.id.imageButtonNext);
        imageButtonNext.setVisibility(View.GONE);
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(this);
        ImageView imageButtonPrevArrow = (ImageView) findViewById(R.id.imageButtonPrevArrow);
        imageButtonPrevArrow.setOnClickListener(this);

        RelativeLayout toggle_switch_rLayoutOuter = (RelativeLayout) findViewById(R.id.toggle_switch_rLayoutOuter);
        RelativeLayout toggle_switch_rLayoutInner = (RelativeLayout) findViewById(R.id.toggle_switch_rLayoutInner);
        TextView toggle_switch_text = (TextView) findViewById(R.id.toggle_switch_text);
        ImageButton toggle_switch_btn = (ImageButton) findViewById(R.id.toggle_switch_btn);
        toggle_switch_rLayoutOuter.setOnClickListener(this);
        toggle_switch_rLayoutInner.setOnClickListener(this);
        toggle_switch_text.setOnClickListener(this);
        toggle_switch_btn.setOnClickListener(this);
        toggle_switch_btn.setOnClickListener(this);
        toggleYesNo(i[0]++);
        btn_editProfile = (Button) findViewById(R.id.btn_editProfile);
        btn_editProfile.setOnClickListener(this);
        progressDialog = new ProgressDialog(_ctx, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Updating data...");

        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        addItemsOnCountrySpinner();

        if (_userSingletonModelClass.getTxt_mission_trip_participation_status().toUpperCase().equals("YES")) {
        } else {
            toggleYesNo(i[0]++);//select NO
        }


        if (_userSingletonModelClass.getTxt_newto_mission().equals("1")) {
            txt_chk_new_to_mission.setChecked(true);
            new_to_mission = "1";
        }
    }

    private void toggleYesNo(int i) {
        final RelativeLayout toggle_switch_rLayout = (RelativeLayout) findViewById(R.id.toggle_switch_rLayoutInner);
        if (i % 2 == 0) {
            toggle_switch_rLayout.setGravity(Gravity.RIGHT | Gravity.CENTER);
            findViewById(R.id.tv_YES).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_NO).setVisibility(View.GONE);
            participation_status = "YES";
        } else {
            toggle_switch_rLayout.setGravity(Gravity.LEFT | Gravity.CENTER);
            findViewById(R.id.tv_YES).setVisibility(View.GONE);
            findViewById(R.id.tv_NO).setVisibility(View.VISIBLE);
            participation_status = "NO";
        }
    }

    private void setCustomDesign() {
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        ((TextView) findViewById(R.id.tv_regn_three)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_regn_three)).setText("Edit Profile");
//        TextView tv= (TextView) findViewById(R.id.tv_regn_three);
//        tv.setText(_userSingletonModelClass.getList_classes_attended().toString());
        ((TextView) findViewById(R.id.tv_regn_step3)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_regn_step3)).setText("Step (3/3)");
        ((TextView) findViewById(R.id.tv_participated)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.toggle_switch_text)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_YES)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_NO)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_new_to_mission)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.btn_editProfile)).setTypeface(regular_font);
        findViewById(R.id.btn_editProfile).setVisibility(View.VISIBLE);
        //  final CheckBox chk = (CheckBox) findViewById(R.id.chk);
        //chk.setTextSize((int) (chk.getTextSize()/0.85));
        //((TextView)findViewById(R.id.tv_regn_two)).setTypeface(regular_font);


    }

    private void addItemsOnCountrySpinner() {
        final ProgressDialog progressDialog = new ProgressDialog(_ctx, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Fetching Country list...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(UrlConstants._URL_MISSION_COUNTRY_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals(true) || status.equals("true"))
                        json_to_spinnerCountry(response);
                    else
                        Toast.makeText(_ctx, "No country loaded", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                Toast.makeText(_ctx, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        VolleyUtils.getInstance(_ctx).addToRequestQueue(stringRequest);
    }

    public void json_to_spinnerCountry(String response_str) {
        List<CountryModel> countryModelList = new ArrayList<>();
        try {
            JSONObject jsonObjectResponse = new JSONObject(response_str);
            JSONArray jsonArrayCountryList = jsonObjectResponse.getJSONArray("data");
            for (int i = 0; i < jsonArrayCountryList.length(); i++) {
                JSONObject jsonObject_country = jsonArrayCountryList.getJSONObject(i);
                CountryModel countryModel = new CountryModel();
                countryModel.setCountry_id(jsonObject_country.getString("id"));
                countryModel.setCountry_name(jsonObject_country.getString("name"));
                countryModel.setCountry_short_name(jsonObject_country.getString("sortname"));
                countryModelList.add(countryModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ArrayList<String> arrayList_country_name = new ArrayList<>();
        int pos_selected_country = 0;
        for (CountryModel temp_sModel :
                countryModelList) {
            if (temp_sModel.getCountry_name().equals(_userSingletonModelClass.getTxt_mission_trip_countries()))
                pos_selected_country = arrayList_country_name.size();

            arrayList_country_name.add(temp_sModel.getCountry_name());
        }
        spinner_country.setAdapter(new ArrayAdapter<String>(_ctx, android.R.layout.simple_spinner_dropdown_item, arrayList_country_name));
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_mission = arrayList_country_name.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_country.setSelection(pos_selected_country);
    }

    @Override
    public void onClick(View v) {
        int item = v.getId();
        // final int[] i = {0};

        switch (item) {
            case R.id.btn_editProfile:
               /* Intent intent = new Intent(RegnThreeActivity.this, RegnFourActivity.class);
                startActivity(intent);*/
                _userSingletonModelClass.setTxt_mission_trip_participation_status(participation_status);
                _userSingletonModelClass.setTxt_newto_mission(new_to_mission);
                _userSingletonModelClass.setTxt_mission_trip_countries(country_mission);

                editprofile();
                break;
            case R.id.imageButtonPrev:
            case R.id.imageButtonPrevArrow:
                finish();
                break;
            case R.id.toggle_switch_rLayoutOuter:
            case R.id.toggle_switch_rLayoutInner:
            case R.id.toggle_switch_text:
            case R.id.toggle_switch_btn:
                toggleYesNo(i[0]++);
                break;
            case R.id.chk_new_to_mission:
                if (txt_chk_new_to_mission.isChecked())
                    new_to_mission = "1";
                else
                    new_to_mission = "0";
                break;


        }
    }

    //------------Volley code for Update...----------------
    public void editprofile() {
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_EDIT_USER_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");
                    if (status.equals("true")) {
                        Intent intent = new Intent(_ctx, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(_ctx, "Couldn't update.Please try again", Toast.LENGTH_SHORT).show();
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
                params.put("device_id", "245");
                params.put("device_type", "Android");
                params.put("user_id", _userSingletonModelClass.getTxt_user_login_id());
                params.put("access_token", _userSingletonModelClass.getTxt_user_access_token());

                return params;
            }

        };
        VolleyUtils.getInstance(_ctx).addToRequestQueue(stringRequest);
    }
    //------------Volley code for update ends---------------
}

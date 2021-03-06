package com.wgt.myprayerapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

public class RegnThreeActivity extends AppCompatActivity implements View.OnClickListener {
    public int[] i = {0};
    Context _ctx;
    CheckBox txt_chk_new_to_mission;
    Spinner spinner_country;
    UserSingletonModelClass _userSingletonModelClass = UserSingletonModelClass.get_userSingletonModelClass();
    String new_to_mission = "0", participation_status = "YES", country_mission;
    //List<String> countryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regn_three);

        setCustomDesign();
        txt_chk_new_to_mission = (CheckBox) findViewById(R.id.chk_new_to_mission);
        txt_chk_new_to_mission.setOnClickListener(this);
        FrameLayout imageButtonNext = (FrameLayout) findViewById(R.id.imageButtonNext);
        imageButtonNext.setOnClickListener(this);
        ImageView imageButtonNextArrow = (ImageView) findViewById(R.id.imageButtonNextArrow);
        imageButtonNextArrow.setOnClickListener(this);
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(this);
        ImageView imageButtonPrevArrow = (ImageView) findViewById(R.id.imageButtonPrevArrow);
        imageButtonPrevArrow.setOnClickListener(this);


        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        addItemsOnCountrySpinner();

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
    }


    public void toggleYesNo(int i) {
        final RelativeLayout toggle_switch_rLayout = (RelativeLayout) findViewById(R.id.toggle_switch_rLayoutInner);
        if (i % 2 == 0) {
            toggle_switch_rLayout.setGravity(Gravity.RIGHT | Gravity.CENTER);
            findViewById(R.id.tv_YES).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_NO).setVisibility(View.GONE);
            participation_status = "YES";
        } else {
            toggle_switch_rLayout.setGravity(Gravity.LEFT | Gravity.CENTER);
            findViewById(R.id.tv_NO).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_YES).setVisibility(View.GONE);
            participation_status = "NO";
        }
    }

    private void setCustomDesign() {
        _ctx = RegnThreeActivity.this;
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        ((TextView) findViewById(R.id.tv_regn_three)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_regn_step3)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_participated)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.toggle_switch_text)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_YES)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_NO)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_new_to_mission)).setTypeface(regular_font);

    }

    public void addItemsOnCountrySpinner() {
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
                Toast.makeText(_ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        for (CountryModel temp_sModel :
                countryModelList) {
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
    }

    @Override
    public void onClick(View v) {
        hideSoftKeyboard();
        int item = v.getId();

        switch (item) {
            case R.id.imageButtonNext:
            case R.id.imageButtonNextArrow:
                _userSingletonModelClass.setTxt_mission_trip_participation_status(participation_status);
                _userSingletonModelClass.setTxt_newto_mission(new_to_mission);
                _userSingletonModelClass.setTxt_mission_trip_countries(country_mission);

                Intent intent = new Intent(RegnThreeActivity.this, RegnFourActivity.class);
                startActivity(intent);
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
                    //_userSingletonModelClass.setTxt_newto_mission("1");
                else
                    new_to_mission = "0";
                //_userSingletonModelClass.setTxt_newto_mission("0");
                break;
        }
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
}

package webgentechnologies.com.myprayerapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;
import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;
import webgentechnologies.com.myprayerapp.networking.VolleyUtils;

//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

public class EditThreeActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_editProfile;
    Context _ctx;
    int[] i = {0};
    static String txt_mission_trip;
    CheckBox txt_chk_new_to_mission;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();
    ProgressDialog progressDialog;

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
        // imageButtonNext.setOnClickListener(this);
        imageButtonNext.setVisibility(View.GONE);
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(this);
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

        String mission_trip_country = userclass.getTxt_mission_trip_countries();
        if (userclass.getTxt_mission_trip_participation_status().toUpperCase().equals("YES")) {
            Spinner spinner_countryYes = (Spinner) findViewById(R.id.spinner_countryYes);
            SpinnerAdapter spinnerAdapter = spinner_countryYes.getAdapter();
            for (int ii = 0; ii < spinnerAdapter.getCount(); ii++) {
                if (spinnerAdapter.getItem(ii).equals(mission_trip_country))
                    spinner_countryYes.setSelection(ii);
            }
        } else {
            toggleYesNo(i[0]++);//select NO
            Spinner spinner_countryNo = (Spinner) findViewById(R.id.spinner_countryNo);
            SpinnerAdapter spinnerAdapter = spinner_countryNo.getAdapter();
            for (int ii = 0; ii < spinnerAdapter.getCount(); ii++) {
                if (spinnerAdapter.getItem(ii).equals(mission_trip_country))
                    spinner_countryNo.setSelection(ii);
            }
        }
        if (userclass.getTxt_newto_mission().equals("1"))
            txt_chk_new_to_mission.setChecked(true);
    }

    private void toggleYesNo(int i) {
        final RelativeLayout toggle_switch_rLayout = (RelativeLayout) findViewById(R.id.toggle_switch_rLayoutInner);
        if (i % 2 == 0) {
            toggle_switch_rLayout.setGravity(Gravity.RIGHT | Gravity.CENTER);
            findViewById(R.id.relativeLayoutYes).setVisibility(View.VISIBLE);
            findViewById(R.id.relativeLayoutNo).setVisibility(View.GONE);
            participation_status = "YES";

            Spinner spinner_countryYes = (Spinner) findViewById(R.id.spinner_countryYes);
            spinner_countryYes.setSelection(0);
        } else {
            toggle_switch_rLayout.setGravity(Gravity.LEFT | Gravity.CENTER);
            findViewById(R.id.relativeLayoutNo).setVisibility(View.VISIBLE);
            findViewById(R.id.relativeLayoutYes).setVisibility(View.GONE);
            participation_status = "NO";

            Spinner spinner_countryNo = (Spinner) findViewById(R.id.spinner_countryNo);
            spinner_countryNo.setSelection(0);
        }
    }

    private void setCustomDesign() {
        addItemsOnCountrySpinner();
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
        ((TextView) findViewById(R.id.btn_editProfile)).setVisibility(View.VISIBLE);
        //  final CheckBox chk = (CheckBox) findViewById(R.id.chk);
        //chk.setTextSize((int) (chk.getTextSize()/0.85));
        //((TextView)findViewById(R.id.tv_regn_two)).setTypeface(regular_font);


    }

    private void addItemsOnCountrySpinner() {
        List<String> list = new ArrayList<String>();
        list.add("United States");

        Spinner spinner_countryYes = (Spinner) findViewById(R.id.spinner_countryYes);
        spinner_countryYes.setPrompt("Select country or region");

        HintSpinner<String> hintSpinnerYes = new HintSpinner<>(
                spinner_countryYes,
                // Default layout - You don't need to pass in any layout id, just your hint text and
                // your list data
                new HintAdapter<>(this, "Select country or region", list),
                new HintSpinner.Callback<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                        // Here you handle the on item selected event (this skips the hint selected event)
                        country_mission = itemAtPosition;
                    }
                });
        hintSpinnerYes.init();

        Spinner spinner_countryNo = (Spinner) findViewById(R.id.spinner_countryNo);
        spinner_countryNo.setPrompt("Select country or region");

        HintSpinner<String> hintSpinnerNo = new HintSpinner<>(
                spinner_countryNo,
                // Default layout - You don't need to pass in any layout id, just your hint text and
                // your list data
                new HintAdapter<>(this, "Select country or region", list),
                new HintSpinner.Callback<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                        // Here you handle the on item selected event (this skips the hint selected event)
                        country_mission = itemAtPosition;
                    }
                });
        hintSpinnerNo.init();
    }

    @Override
    public void onClick(View v) {
        int item = v.getId();
        // final int[] i = {0};

        switch (item) {
            case R.id.btn_editProfile:
               /* Intent intent = new Intent(RegnThreeActivity.this, RegnFourActivity.class);
                startActivity(intent);*/
                userclass.setTxt_mission_trip_participation_status(participation_status);
                userclass.setTxt_newto_mission(new_to_mission);
                userclass.setTxt_mission_trip_countries(country_mission);

                editprofile();
                break;
            case R.id.imageButtonPrev:
                finish();
                break;
            case R.id.toggle_switch_rLayoutOuter:
                toggleYesNo(i[0]++);
                break;
            case R.id.toggle_switch_rLayoutInner:
                toggleYesNo(i[0]++);
                break;
            case R.id.toggle_switch_text:
                toggleYesNo(i[0]++);
                break;
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
        progressDialog.show();
        progressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_EDIT_PROFILE, new Response.Listener<String>() {
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
                Toast.makeText(_ctx, error.toString(), Toast.LENGTH_LONG).show();
                if (progressDialog.isShowing())
                    progressDialog.cancel();
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
                params.put("country_id", userclass.getTxt_country_id());
                params.put("country_name", userclass.getTxt_country());
                params.put("address1", userclass.getTxt_addr1());
                params.put("address2", userclass.getTxt_addr2());
                params.put("city", userclass.getTxt_city());
                params.put("state_id", userclass.getTxt_state_id());
                params.put("state_name", userclass.getTxt_state_name());
                params.put("phone", userclass.getTxt_phone());
                params.put("church_name", userclass.getChurch_name());
                String str = "";
                for (String s :
                        userclass.getList_classes_attended()) {
                    str += (s + ";");
                }
                str = str.substring(0, str.length() - 1);
                params.put("classes", str);
                params.put("mission_trip", userclass.getTxt_mission_trip_countries());
                params.put("mission_trip_status", userclass.getTxt_mission_trip_participation_status());
                params.put("mission_concept", userclass.getTxt_newto_mission());
                params.put("device_id", "245");
                params.put("device_type", "Android");
                params.put("user_id", userclass.getTxt_user_login_id());
                params.put("access_token", userclass.getTxt_user_access_token());

                return params;
            }

        };
        VolleyUtils.getInstance(_ctx).addToRequestQueue(stringRequest);
    }
    //------------Volley code for update ends---------------
}

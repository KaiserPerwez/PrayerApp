package webgentechnologies.com.myprayerapp.activity;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;
import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;

public class EditThreeActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_editProfile;
    Context m_ctx;
    public int[] i = {0};
    public static String txt_mission_trip;
    CheckBox txt_chk_new_to_mission;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regn_three);

        setCustomDesign();
        Toast.makeText(getApplicationContext(), userclass.getTxt_editone_fname(), Toast.LENGTH_LONG).show();
     /*   FrameLayout imageButtonNext = (FrameLayout) findViewById(R.id.imageButtonNext);
        imageButtonNext.setVisibility(View.GONE);
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        RelativeLayout toggle_switch_rLayoutOuter = (RelativeLayout) findViewById(R.id.toggle_switch_rLayoutOuter);
        RelativeLayout toggle_switch_rLayoutInner = (RelativeLayout) findViewById(R.id.toggle_switch_rLayoutInner);
        TextView toggle_switch_text = (TextView) findViewById(R.id.toggle_switch_text);
        ImageButton toggle_switch_btn = (ImageButton) findViewById(R.id.toggle_switch_btn);
        final int[] i = {0};
        toggleYesNo(i[0]++);
        toggle_switch_rLayoutOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleYesNo(i[0]++);
            }
        });
        toggle_switch_rLayoutInner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleYesNo(i[0]++);
            }
        });
        toggle_switch_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleYesNo(i[0]++);
            }
        });
        toggle_switch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleYesNo(i[0]++);
            }
        });
        toggle_switch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleYesNo(i[0]++);
            }
        });

        btn_editProfile = (Button) findViewById(R.id.btn_editProfile);
        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditThreeActivity.this, HomeActivity.class));
            }
        });*/

        //New code...
        Toast.makeText(getApplicationContext(), userclass.getTxt_classes_attended() + "" + userclass.getTxt_classes_attended(), Toast.LENGTH_LONG).show();
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
    }

    private void toggleYesNo(int i) {
        final RelativeLayout toggle_switch_rLayout = (RelativeLayout) findViewById(R.id.toggle_switch_rLayoutInner);
        if (i % 2 == 0) {
            toggle_switch_rLayout.setGravity(Gravity.RIGHT | Gravity.CENTER);
            Toast.makeText(EditThreeActivity.this, "YES:" + i, Toast.LENGTH_SHORT).show();
            findViewById(R.id.relativeLayoutYes).setVisibility(View.VISIBLE);
            findViewById(R.id.relativeLayoutNo).setVisibility(View.GONE);
        } else {
            toggle_switch_rLayout.setGravity(Gravity.LEFT | Gravity.CENTER);
            Toast.makeText(EditThreeActivity.this, "NO:" + i, Toast.LENGTH_SHORT).show();
            findViewById(R.id.relativeLayoutNo).setVisibility(View.VISIBLE);
            findViewById(R.id.relativeLayoutYes).setVisibility(View.GONE);
        }
    }

    private void setCustomDesign() {
        addItemsOnCountrySpinner();
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        ((TextView) findViewById(R.id.tv_regn_three)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_regn_three)).setText("Edit Profile");
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
        list.add("California");
        list.add("Florida");
        list.add("Hawaii");
        list.add("Indiana");
        list.add("Mississippi");
        list.add("New Hampshire");
        list.add("New Jersey");
        list.add("New Mexico");
        list.add("New York");
        list.add("Texas");
        list.add("Washington");

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
                        txt_mission_trip = itemAtPosition.toString();
                        userclass.setTxt_mission_trip(txt_mission_trip);
                        userclass.setTxt_mission_trip_status("Yes");
                        userclass.setTxt_newto_mission("0");
                        Toast.makeText(getApplicationContext(), txt_mission_trip, Toast.LENGTH_LONG).show();
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
                        txt_mission_trip = itemAtPosition.toString();
                        userclass.setTxt_mission_trip(txt_mission_trip);
                        userclass.setTxt_mission_trip_status("No");
                        userclass.setTxt_newto_mission("0");
                        Toast.makeText(getApplicationContext(), txt_mission_trip, Toast.LENGTH_LONG).show();

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
                    userclass.setTxt_newto_mission("1");
                else
                    userclass.setTxt_newto_mission("0");
                break;


        }
    }

    //------------Volley code for Update...----------------
    public void editprofile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_EDIT_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(EditThreeActivity.this, response, Toast.LENGTH_LONG).show();
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
                params.put("first_name", userclass.getTxt_editone_fname());
                params.put("last_name", userclass.getTxt_editone_lname());
                //params.put("email", userclass.getTxt_editone_email());
                params.put("email", userclass.getTxt_fcbk_lgn_email());
                params.put("country_id", userclass.getTxt_editone_countryid1());
                params.put("country_name", userclass.getTxt_editone_country_name());
                params.put("address1", userclass.getTxt_editone_addr1());
                params.put("address2", userclass.getTxt_editone_addr2());
                params.put("city", userclass.getTxt_editone_city());
                params.put("state_id", userclass.getTxt_editone_state_id());
                params.put("state_name", userclass.getTxt_editone_state_name());
                params.put("phone", userclass.getTxt_editone_phone());
                params.put("church_name", userclass.getChurch_name());
                params.put("classes", userclass.getTxt_classes_attended());
                params.put("mission_trip", userclass.getTxt_mission_trip());
                params.put("mission_trip_status", userclass.getTxt_mission_trip_status());
                //  params.put("mission_concept", userclass.getTxt_newto_mission());
                params.put("mission_concept", userclass.getTxt_newto_mission());
                // params.put("mission_concept", "0");
                params.put("device_id", "245");
                params.put("device_type", "Android");
                params.put("user_id", userclass.getTxt_user_login_id());
                params.put("access_token", userclass.getTxt_user_access_token());
              /*  params.put("first_name", "abc");
                params.put("last_name", "abg");
                params.put("email", "abc@gmail.com");
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
                params.put("mission_trip_status","Yes");
                params.put("mission_concept", "0");
                params.put("device_id", "245");
                params.put("device_type", "Android");
                params.put("user_id", userclass.getTxt_user_login_id());
                params.put("access_token", userclass.getTxt_user_access_token());*/
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //------------Volley code for update ends---------------
}

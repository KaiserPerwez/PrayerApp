package webgentechnologies.com.myprayerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;
import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;

public class RegnThreeActivity extends AppCompatActivity implements View.OnClickListener {
    Context m_ctx;
    public int[] i = {0};
    CheckBox txt_chk_new_to_mission;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();
    String new_to_mission="0",participation_status="YES",country_mission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regn_three);

        setCustomDesign();
        // setCustomClickListeners();
        Toast.makeText(getApplicationContext(), userclass.getList_classes_attended() + "" , Toast.LENGTH_LONG).show();
        txt_chk_new_to_mission = (CheckBox) findViewById(R.id.chk_new_to_mission);
        txt_chk_new_to_mission.setOnClickListener(this);
        FrameLayout imageButtonNext = (FrameLayout) findViewById(R.id.imageButtonNext);
        imageButtonNext.setOnClickListener(this);
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

    }

    public void toggleYesNo(int i) {
        final RelativeLayout toggle_switch_rLayout = (RelativeLayout) findViewById(R.id.toggle_switch_rLayoutInner);
        if (i % 2 == 0) {
            toggle_switch_rLayout.setGravity(Gravity.RIGHT | Gravity.CENTER);
            Toast.makeText(RegnThreeActivity.this, "YES:" + i, Toast.LENGTH_SHORT).show();
            findViewById(R.id.relativeLayoutYes).setVisibility(View.VISIBLE);
            findViewById(R.id.relativeLayoutNo).setVisibility(View.GONE);
            participation_status="YES";

            Spinner spinner_countryYes = (Spinner) findViewById(R.id.spinner_countryYes);
            spinner_countryYes.setSelection(0);
        } else {
            toggle_switch_rLayout.setGravity(Gravity.LEFT | Gravity.CENTER);
            Toast.makeText(RegnThreeActivity.this, "NO:" + i, Toast.LENGTH_SHORT).show();
            findViewById(R.id.relativeLayoutNo).setVisibility(View.VISIBLE);
            findViewById(R.id.relativeLayoutYes).setVisibility(View.GONE);
            participation_status="NO";
            Spinner spinner_countryNo = (Spinner) findViewById(R.id.spinner_countryNo);
            spinner_countryNo.setSelection(0);
        }
    }

    private void setCustomDesign() {
        m_ctx = RegnThreeActivity.this;
        addItemsOnCountrySpinner();
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        ((TextView) findViewById(R.id.tv_regn_three)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_regn_step3)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_participated)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.toggle_switch_text)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_YES)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_NO)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_new_to_mission)).setTypeface(regular_font);

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
        hideSoftKeyboard();
        int item = v.getId();
        // final int[] i = {0};

        switch (item) {
            case R.id.imageButtonNext:
                userclass.setTxt_mission_trip_participation_status(participation_status);
                userclass.setTxt_newto_mission(new_to_mission);
                userclass.setTxt_mission_trip_countries(country_mission);
                String user=userclass.toString();
               // Toast.makeText(this,_userSingletonModelClass.toString(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegnThreeActivity.this, RegnFourActivity.class);
                startActivity(intent);
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

package webgentechnologies.com.myprayerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;
import webgentechnologies.com.myprayerapp.networking.VolleyUtils;

public class EditTwoActivity extends AppCompatActivity implements View.OnClickListener {
    Context _ctx;
    CheckBox chk_alpha, chk_perspective, chk_men, chk_beth_more, chk_cbs, chk_others;
    EditText txt_others;
    Spinner txt_churchname;
    static String txt_selected_church_name;
    UserSingletonModelClass _userSingletonModelClass = UserSingletonModelClass.get_userSingletonModelClass();
    ArrayList<String> _arrListChurches = new ArrayList<>();
    ArrayAdapter<String> _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_two);
        _ctx =EditTwoActivity.this;

        chk_alpha = (CheckBox) findViewById(R.id.chk_alpha);
        chk_perspective = (CheckBox) findViewById(R.id.chk_perspective);
        chk_men = (CheckBox) findViewById(R.id.chk_men);
        chk_beth_more = (CheckBox) findViewById(R.id.chk_beth_more);
        chk_cbs = (CheckBox) findViewById(R.id.chk_cbs);
        // chk_others=(CheckBox)findViewById(R.id.chk_others);
        txt_others = (EditText) findViewById(R.id.txt_others);
        txt_churchname = (Spinner) findViewById(R.id.txt_church_name);

        chk_alpha.setOnClickListener(this);
        chk_perspective.setOnClickListener(this);
        chk_men.setOnClickListener(this);
        chk_beth_more.setOnClickListener(this);
        chk_cbs.setOnClickListener(this);
        chk_others = (CheckBox) findViewById(R.id.chk_others);
        chk_others.setOnClickListener(this);

        FrameLayout imageButtonNext = (FrameLayout) findViewById(R.id.imageButtonNext);
        imageButtonNext.setOnClickListener(this);
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(this);

        setCustomDesign();
        //Calling volley method for spinner
        sendRequest();
        if (_userSingletonModelClass.getList_classes_attended().contains("Alpha")) {
            chk_alpha.setChecked(true);
        }
        if (_userSingletonModelClass.getList_classes_attended().contains("Perspective")) {
            chk_perspective.setChecked(true);
        }
        if (_userSingletonModelClass.getList_classes_attended().contains("Men's Fraternity")) {
            chk_men.setChecked(true);
        }
        if (_userSingletonModelClass.getList_classes_attended().contains("Beth More")) {
            chk_alpha.setChecked(true);
        }
        if (_userSingletonModelClass.getList_classes_attended().contains("CBS")) {
            chk_alpha.setChecked(true);
        }
        if(_userSingletonModelClass.getList_classes_attended().contains("OTHER")){
            chk_others.setChecked(true);
            findViewById(R.id.layout_txt_others).setVisibility(View.VISIBLE);
            int pos_other= _userSingletonModelClass.getList_classes_attended().indexOf("OTHER");
            txt_others.setText(_userSingletonModelClass.getList_classes_attended().get(pos_other+1).toString());
        }

    }

    private void setCustomDesign() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        ((TextView) findViewById(R.id.tv_regn_two)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_regn_two)).setText("Edit Profile");
        ((TextView) findViewById(R.id.tv_regn_step2)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_regn_step2)).setText("Step (2/3)");
//        ((TextView) findViewById(R.id.txt_church_name)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_header_class)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_alpha)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_perspective)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_men)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_beth_more)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_cbs)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_others)).setTypeface(regular_font);


    }

    @Override
    public void onClick(View v) {
        hideSoftKeyboard();
        int item = v.getId();
        switch (item) {
            case R.id.chk_others:
                LinearLayout layout = (LinearLayout) findViewById(R.id.layout_txt_others);
                if (chk_others.isChecked()) {
                    layout.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.GONE);
                }
                break;
            case R.id.imageButtonNext:
                List list = new ArrayList();
                if (chk_alpha.isChecked())
                    list.add("Alpha");
                if (chk_perspective.isChecked())
                    list.add("Perspective");
                if (chk_men.isChecked())
                    list.add("Men's Fraternity");
                if (chk_beth_more.isChecked())
                    list.add("Beth More");
                if (chk_cbs.isChecked())
                    list.add("CBS");
                if(chk_others.isChecked())
                {
                    list.add("OTHER");
                    list.add(txt_others.getText().toString());
                }
                _userSingletonModelClass.setList_classes_attended(list);
                _userSingletonModelClass.setChurch_name(txt_selected_church_name);
                Intent intent = new Intent(_ctx, EditThreeActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonPrev:
                finish();
                break;
        }
    }


    /*
    Volley code for spinner
     */
    public void sendRequest() {
        StringRequest stringRequestChurchList = new StringRequest(UrlConstants._URL_ALL_CHURCHES_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                churchList_Json(responseStr);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(_ctx, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        VolleyUtils.getInstance(_ctx).addToRequestQueue(stringRequestChurchList);

    }

    public void churchList_Json(String responseStr) {
        try {
            JSONObject jsonObject = new JSONObject(responseStr);
            JSONArray jsonArrayChurchList = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArrayChurchList.length(); i++) {
                JSONObject jsonObjectChurchName = jsonArrayChurchList.getJSONObject(i);
                String churchName = jsonObjectChurchName.getString("church_name");
                _arrListChurches.add(churchName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        _adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, _arrListChurches);
        txt_churchname.setAdapter(_adapter);
        txt_churchname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txt_selected_church_name= _adapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    //----------------Volley code for spinner ends----------------
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

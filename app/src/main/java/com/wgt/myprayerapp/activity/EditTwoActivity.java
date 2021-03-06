package com.wgt.myprayerapp.activity;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.wgt.myprayerapp.R;
import com.wgt.myprayerapp.model.UserSingletonModelClass;
import com.wgt.myprayerapp.networking.UrlConstants;
import com.wgt.myprayerapp.networking.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditTwoActivity extends AppCompatActivity implements View.OnClickListener {
    static String txt_selected_church_name;
    static String txt_selected_church_id;
    Context _ctx;
    CheckBox chk_alpha, chk_perspective, chk_men, chk_beth_more, chk_cbs, chk_others;
    EditText txt_others;
    Spinner txt_churchname;
    UserSingletonModelClass _userSingletonModelClass = UserSingletonModelClass.get_userSingletonModelClass();
    ArrayList<String> _arrListChurchName = new ArrayList<>();
    ArrayList<String> _arrListChurchId = new ArrayList<>();
    ArrayAdapter<String> _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_two);
        _ctx = EditTwoActivity.this;
       // load_ProfileDetails();
        chk_alpha = findViewById(R.id.chk_alpha);
        chk_perspective = findViewById(R.id.chk_perspective);
        chk_men = findViewById(R.id.chk_men);
        chk_beth_more = findViewById(R.id.chk_beth_more);
        chk_cbs = findViewById(R.id.chk_cbs);
        // chk_others=(CheckBox)findViewById(R.id.chk_others);
        txt_others = findViewById(R.id.txt_others);
        txt_churchname = findViewById(R.id.txt_church_name);

        chk_alpha.setOnClickListener(this);
        chk_perspective.setOnClickListener(this);
        chk_men.setOnClickListener(this);
        chk_beth_more.setOnClickListener(this);
        chk_cbs.setOnClickListener(this);
        chk_others = findViewById(R.id.chk_others);
        chk_others.setOnClickListener(this);


        FrameLayout imageButtonNext = findViewById(R.id.imageButtonNext);
        imageButtonNext.setOnClickListener(this);
        ImageView imageButtonNextArrow = findViewById(R.id.imageButtonNextArrow);
        imageButtonNextArrow.setOnClickListener(this);
        FrameLayout imageButtonPrev = findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(this);
        ImageView imageButtonPrevArrow = findViewById(R.id.imageButtonPrevArrow);
        imageButtonPrevArrow.setOnClickListener(this);


        setCustomDesign();
        //Calling volley method for spinner
        //sendRequest();
        if (_userSingletonModelClass.getList_classes_attended().contains("Alpha")) {
            chk_alpha.setChecked(true);
        } else {
            chk_alpha.setChecked(false);
        }
        if (_userSingletonModelClass.getList_classes_attended().contains("Perspective")) {
            chk_perspective.setChecked(true);
        } else {
            chk_perspective.setChecked(false);
        }
        if (_userSingletonModelClass.getList_classes_attended().contains("Men's Fraternity")) {
            chk_men.setChecked(true);
        } else {
            chk_men.setChecked(false);
        }
        if (_userSingletonModelClass.getList_classes_attended().contains("Beth Moore")) {
            chk_beth_more.setChecked(true);
        } else {
            chk_beth_more.setChecked(false);
        }
        if (_userSingletonModelClass.getList_classes_attended().contains("CBS")) {
            chk_cbs.setChecked(true);
        } else {
            chk_cbs.setChecked(false);
        }
        if (_userSingletonModelClass.getList_classes_attended().contains("OTHER")) {
            chk_others.setChecked(true);
            findViewById(R.id.layout_txt_others).setVisibility(View.VISIBLE);
            int pos_other = _userSingletonModelClass.getList_classes_attended().indexOf("OTHER");
            txt_others.setText(_userSingletonModelClass.getList_classes_attended().get(pos_other + 1).toString());
        }
        sendRequest();

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
                LinearLayout layout = findViewById(R.id.layout_txt_others);
                if (chk_others.isChecked()) {
                    layout.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.GONE);
                }
                break;
            case R.id.imageButtonNext:
            case R.id.imageButtonNextArrow:
                if (txt_selected_church_id.equals("-1")) {
                    Toast.makeText(_ctx, "Please select a church", Toast.LENGTH_SHORT).show();
                    return;
                }
                List list = new ArrayList();
                if (chk_alpha.isChecked())
                    list.add("Alpha");
                if (chk_perspective.isChecked())
                    list.add("Perspective");
                if (chk_men.isChecked())
                    list.add("Men's Fraternity");
                if (chk_beth_more.isChecked())
                    list.add("Beth Moore");
                if (chk_cbs.isChecked())
                    list.add("CBS");
                if (chk_others.isChecked()) {
                    list.add("OTHER");
                    list.add(txt_others.getText().toString());
                }
                if (list.size() == 0) {
                    Toast.makeText(this, "Please select at least one class", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (list.contains("OTHER") && (txt_others.getText().toString().length() <= 1)) {
                    Toast.makeText(this, "Please Enter a valid class-name for \"OTHERS\" category", Toast.LENGTH_SHORT).show();
                    return;
                }
                _userSingletonModelClass.setList_classes_attended(list);
                _userSingletonModelClass.setChurch_name(txt_selected_church_name);
                _userSingletonModelClass.setChurch_id(txt_selected_church_id);
                Intent intent = new Intent(_ctx, EditThreeActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonPrev:
            case R.id.imageButtonPrevArrow:
                finish();
                break;
        }
    }


    /*
    Volley code for spinner
     */
    public void sendRequest() {
        final ProgressDialog progressDialog = new ProgressDialog(_ctx, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Fetching list of classes...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequestChurchList = new StringRequest(UrlConstants._URL_CHURCH_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();

                churchList_Json(responseStr);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();

                Toast.makeText(_ctx, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleyUtils.getInstance(_ctx).addToRequestQueue(stringRequestChurchList);

    }

    public void churchList_Json(String responseStr) {
        int pos_churchNameOrId = 0;
        try {
            JSONObject jsonObject = new JSONObject(responseStr);
            JSONArray jsonArrayChurchList = jsonObject.getJSONArray("data");
            _arrListChurchName.add("Select Church");
            _arrListChurchId.add("-1");
            for (int i = 0; i < jsonArrayChurchList.length(); i++) {
                JSONObject jsonObjectChurchName = jsonArrayChurchList.getJSONObject(i);
                String churchName = jsonObjectChurchName.getString("church_name");
                String churchId = jsonObjectChurchName.getString("id");

                if (churchId.equals(_userSingletonModelClass.getChurch_id())) {
                    pos_churchNameOrId = _arrListChurchName.size();
                    _userSingletonModelClass.setChurch_name(churchName);
                }

                _arrListChurchId.add(churchId);
                _arrListChurchName.add(churchName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        _adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, _arrListChurchName);
        txt_churchname.setAdapter(_adapter);
        txt_churchname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txt_selected_church_name = _adapter.getItem(i).toString();
                txt_selected_church_id = _arrListChurchId.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        txt_churchname.setSelection(pos_churchNameOrId);
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

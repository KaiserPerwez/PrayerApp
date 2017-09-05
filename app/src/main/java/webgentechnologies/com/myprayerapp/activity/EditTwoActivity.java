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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;
import webgentechnologies.com.myprayerapp.networking.VolleyUtils;

public class EditTwoActivity extends AppCompatActivity implements View.OnClickListener {
    Context m_ctx;
    CheckBox chk_alpha, chk_perspective, chk_men, chk_beth_more, chk_cbs, chk_others;
    EditText txt_others;
    Spinner txt_churchname;
    static String txt_selected_church_name;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();
    ArrayList<String> arrListChurches = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_two);
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



        Toast.makeText(EditTwoActivity.this, userclass.getList_classes_attended().toString(), Toast.LENGTH_LONG).show();

        setCustomDesign();
        //Calling volley method for spinner
        sendRequest();
        if (userclass.getList_classes_attended().contains("Alpha")) {
            chk_alpha.setChecked(true);
        }
        if (userclass.getList_classes_attended().contains("Perspective")) {
            chk_perspective.setChecked(true);
        }
        if (userclass.getList_classes_attended().contains("Men's Fraternity")) {
            chk_men.setChecked(true);
        }
        if (userclass.getList_classes_attended().contains("Beth More")) {
            chk_alpha.setChecked(true);
        }
        if (userclass.getList_classes_attended().contains("CBS")) {
            chk_alpha.setChecked(true);
        }
        if(userclass.getList_classes_attended().contains("OTHER")){
            chk_others.setChecked(true);
            findViewById(R.id.layout_txt_others).setVisibility(View.VISIBLE);
            int pos_other=userclass.getList_classes_attended().indexOf("OTHER");
            txt_others.setText(userclass.getList_classes_attended().get(pos_other+1).toString());
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
                userclass.setList_classes_attended(list);
                userclass.setChurch_name(txt_selected_church_name);
                Intent intent = new Intent(EditTwoActivity.this, EditThreeActivity.class);
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
        // final ProgressDialog progressDialog = new ProgressDialog(m_ctx, ProgressDialog.STYLE_SPINNER);

        // progressDialog.setIndeterminate(true);


        StringRequest stringRequestChurchList = new StringRequest(UrlConstants._URL_ALL_CHURCHES_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                churchList_Json(responseStr);
                //str = responseStr;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditTwoActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        VolleyUtils.getInstance(EditTwoActivity.this).addToRequestQueue(stringRequestChurchList);

      /*  progressDialog.show(m_ctx, "", "Loading data...", true, true);
        if (str != null)
            progressDialog.dismiss();*/
        // Show User a progress dialog while waiting for Volley response

    }

    public void churchList_Json(String responseStr) {
        try {
            JSONObject jsonObject = new JSONObject(responseStr);
            JSONArray jsonArrayChurchList = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArrayChurchList.length(); i++) {
                JSONObject jsonObjectChurchName = jsonArrayChurchList.getJSONObject(i);
                String churchName = jsonObjectChurchName.getString("church_name");
                arrListChurches.add(churchName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrListChurches);
        txt_churchname.setAdapter(adapter);
        txt_churchname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txt_selected_church_name=adapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    //----------------Volley code for spinner ends----------------
}

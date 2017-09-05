package webgentechnologies.com.myprayerapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;
import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.UserLocationModel;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;

public class RegnOneActivity extends AppCompatActivity implements View.OnClickListener {
    Context m_ctx = RegnOneActivity.this;
    EditText txt_fname, txt_lname, txt_email, txt_addr1, txt_addr2, txt_state, txt_phone, txt_city;
    Spinner txt_country;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();
    /*
    *Adding variable for popup...
     */
    ListView lv_churches;
    ArrayList<String> arrListChurches = new ArrayList<>();
    ArrayAdapter<String> adapter;
    EditText txt_selected_church, txt_search_prayer;
    String selected_church_name;

    /*
    *Taking variables and arraylist for spinner
     */
    ArrayList<UserLocationModel> arrayList_country_details = new ArrayList<>();
    ArrayList<String> arraylist_country_name = new ArrayList<>();
    ArrayList<UserLocationModel> arraylist_state_details = new ArrayList<>();
    ArrayList<String> arrayList_state_name = new ArrayList<>();
    public static String txt_country_id1, txt_country_name, txt_country_sortname, txt_state_id, txt_state_name, txt_country_id2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regn_one);
        showPopUp();
        txt_fname = (EditText) findViewById(R.id.txt_fname);
        txt_lname = (EditText) findViewById(R.id.txt_lname);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_addr1 = (EditText) findViewById(R.id.txt_addr1);
        txt_addr2 = (EditText) findViewById(R.id.txt_addr2);
        txt_city = (EditText) findViewById(R.id.txt_state);
        txt_phone = (EditText) findViewById(R.id.txt_phone);


        setCustomDesign();
        sendrequest_to_spinner();
        //setCustomClickListeners();
        FrameLayout imageButtonNext = (FrameLayout) findViewById(R.id.imageButtonNext);
        imageButtonNext.setOnClickListener(this);
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(this);
    }

    public void settingvalues() {
        userclass.setTxt_fname(txt_fname.getText().toString());
        userclass.setTxt_lname(txt_lname.getText().toString());
        userclass.setTxt_email(txt_email.getText().toString());
        userclass.setTxt_addr1(txt_addr1.getText().toString());
        userclass.setTxt_addr2(txt_addr2.getText().toString());
        userclass.setTxt_city(txt_city.getText().toString());
        userclass.setTxt_phone(txt_phone.getText().toString());
    }

    private void setCustomDesign() {
        //  m_ctx = RegnOneActivity.this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // addItemsOnStateSpinner();
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        ((TextView) findViewById(R.id.tv_regn_one)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_regn_step1)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_fname)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_lname)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_email)).setTypeface(regular_font);

//        ((TextView)findViewById(R.id.txt_country)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_addr1)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_addr2)).setTypeface(regular_font);
        // ((TextView)findViewById(R.id.txt_city)).setTypeface(regular_font);

        ((TextView) findViewById(R.id.txt_state)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_phone)).setTypeface(regular_font);

    }

    private void addItemsOnStateSpinner() {
        final Spinner spinner_city = (Spinner) findViewById(R.id.spinner_city);
        //spinner_city.setPrompt("Choose State");
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
        final HintSpinner<String> hintSpinner = new HintSpinner<>(
                spinner_city,
                // Default layout - You don't need to pass in any layout id, just your hint text and
                // your list data
                new HintAdapter<>(this, "Select City*", list),
                new HintSpinner.Callback<String>() {
                    @Override
                    public void onItemSelected(int position, String itemAtPosition) {
                        // Here you handle the on item selected event (this skips the hint selected event)
                        String txt_selected_countryposition = itemAtPosition.toString();
                        //country = txt_selected_countryposition;
                        userclass.setTxt_country(txt_selected_countryposition);
                        Toast.makeText(getApplicationContext(), txt_selected_countryposition, Toast.LENGTH_LONG).show();
                    }

                });
        hintSpinner.init();
    }

    @Override
    public void onClick(View v) {
        int item = v.getId();
        switch (item) {
            case R.id.imageButtonNext:
                settingvalues();
                Intent intent = new Intent(m_ctx, RegnTwoActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonPrev:
                finish();
                break;
        }
    }

    /*
    *Popup code...
     */
    private void showPopUp() {

        LayoutInflater li = LayoutInflater.from(m_ctx);// get prompts.xml view
        final View promptsView = li.inflate(R.layout.add_church_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(m_ctx);// set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        // set dialog message
        alertDialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = alertDialogBuilder.create();// create alert dialog
        alertDialog.show();

        //Calling method of volley
        sendRequest();

        txt_selected_church = (EditText) promptsView.findViewById(R.id.txt_selected_churchname);
        txt_search_prayer = (EditText) promptsView.findViewById(R.id.search_Prayer);
        lv_churches = (ListView) promptsView.findViewById(R.id.church_lv);

        Button btn_submit = (Button) promptsView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                String txt_selected_church_name = txt_selected_church.getText().toString();
                userclass.setTxt_selected_church_name(txt_selected_church_name);


            }
        });
        Button btn_exit = (Button) promptsView.findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // RegnOneActivity.this.finish();
                finish();
            }
        });
    }

    //Popup code ends...
    /*
    Volley code for popup...
     */
    public void sendRequest() {
        // final ProgressDialog progressDialog = new ProgressDialog(m_ctx, ProgressDialog.STYLE_SPINNER);

        // progressDialog.setIndeterminate(true);
        final ProgressDialog progressDialog = new ProgressDialog(RegnOneActivity.this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequestChurchList = new StringRequest(UrlConstants._URL_ALL_CHURCHES_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                churchList_Json(responseStr);
                //str = responseStr;
                progressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegnOneActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.hide();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequestChurchList);
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

        adapter = new ArrayAdapter<String>(this, R.layout.dialog_row, R.id.txt_churchname_lst, arrListChurches);
        lv_churches.setAdapter(adapter);
        lv_churches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_church_name = adapter.getItem(position).toString();
                txt_selected_church.setText(selected_church_name);
            }
        });
        txt_search_prayer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lv_churches.setAdapter(adapter);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RegnOneActivity.this.adapter.getFilter().filter(s);
            }
        });
      /* lv_churches.setAdapter(new ChurchListViewAdapter());
        lv_churches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_church_name= arrListChurches.get(position);
                txt_selected_church.setText(selected_church_name);
            }
        });*/
    }
    //Volley code for popup ends...

    /*
    Volley code for spinner
     */
    public void sendrequest_to_spinner() {
        StringRequest stringRequest = new StringRequest(UrlConstants._URL_GET_COUNTRY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showjson_to_spinner(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showjson_to_spinner(String spinner_json) {
        try {
            UserLocationModel userLocationModel = new UserLocationModel();
            JSONObject jsonObject_Country = new JSONObject(spinner_json);
            JSONObject jsonObject_country = jsonObject_Country.getJSONObject("data");
            arraylist_country_name.add(jsonObject_country.getString("name"));
           /* arrayList_country_details.add(txt_editone_country_id1);
            arrayList_country_details.add(txt_editone_country_name);
            arrayList_country_details.add(txt_editone_country_sortname);*/
            userLocationModel.setTxt_country_id1(jsonObject_country.getString("id"));
            userLocationModel.setTxt_country(jsonObject_country.getString("name"));
            userLocationModel.setTxt_country_sortname(jsonObject_country.getString("sortname"));
            arrayList_country_details.add(userLocationModel);
            JSONArray jsonArray = jsonObject_country.getJSONArray("state");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject_state = jsonArray.getJSONObject(i);
                UserLocationModel userLocationModel1 = new UserLocationModel();
                arrayList_state_name.add(jsonObject_state.getString("name"));
               /* arraylist_state_details.add(txt_editone_state_id);
                arraylist_state_details.add(txt_editone_state_name);
                arraylist_state_details.add(txt_editone_country_id2);
                arrayList_state_name.add(txt_editone_state_name);*/
                userLocationModel1.setTxt_state_id(jsonObject_state.getString("id"));
                userLocationModel1.setTxt_state(jsonObject_state.getString("name"));
                userLocationModel1.setTxt_country_id2(jsonObject_state.getString("country_id"));
                arraylist_state_details.add(userLocationModel1);
                //  arraylist_country_name.add(j.getString("sortname"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Spinner spinner_country = (Spinner) findViewById(R.id.txt_country);
        spinner_country.setAdapter(new ArrayAdapter<String>(RegnOneActivity.this, android.R.layout.simple_spinner_dropdown_item, arraylist_country_name));
        Spinner spinner_city = (Spinner) findViewById(R.id.spinner_city);
        spinner_city.setAdapter(new ArrayAdapter<String>(RegnOneActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayList_state_name));
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txt_country_id1 = arrayList_country_details.get(position).getTxt_country_id1().toString();
                txt_country_name = arrayList_country_details.get(position).getTxt_country().toString();
                txt_country_sortname = arrayList_country_details.get(position).getTxt_country_sortname().toString();
                Toast.makeText(getApplicationContext(), txt_country_id1 + "/" + txt_country_name, Toast.LENGTH_LONG).show();
                userclass.setTxt_country_id1(txt_country_id1);
                userclass.setTxt_country(txt_country_name);
                userclass.setTxt_country_sortname(txt_country_sortname);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txt_state_id = arraylist_state_details.get(position).getTxt_state_id();
                txt_state_name = arraylist_state_details.get(position).getTxt_state();
                txt_country_id2 = arraylist_state_details.get(position).getTxt_country_id2();
                // Toast.makeText(getApplicationContext(),txt_editone_state_id+"/n"+txt_editone_state_name,Toast.LENGTH_LONG).show();
                userclass.setTxt_state_id(txt_state_id);
                userclass.setTxt_state_name(txt_state_name);
                userclass.setTxt_country_id2(txt_country_id2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}

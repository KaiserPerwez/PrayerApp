package webgentechnologies.com.myprayerapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.CountryModel;
import webgentechnologies.com.myprayerapp.model.StateModel;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;
import webgentechnologies.com.myprayerapp.networking.VolleyUtils;

public class RegnOneActivity extends AppCompatActivity implements View.OnClickListener {
    Context m_ctx = RegnOneActivity.this;
    EditText txt_fname, txt_lname, txt_email, txt_addr1, txt_addr2, txt_state, txt_phone, txt_city;
    Spinner spinner_country,spinner_state;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();
    /*
    *Adding variable for popup...
     */
    ListView lv_churches;
    ArrayList<String> arrListChurches = new ArrayList<>();
    ArrayAdapter<String> adapter;
    EditText txt_selected_church, txt_search_prayer;
    SharedPreferences sharedpreferences;
    static final String sharedpreference_key_churchname = "church name";
    static final String sharedpreferenceName = "pref_prayerApp";
    static String selected_church_name;

    /*
    *Taking variables and arraylist for spinner
     */
   // public static String txt_country_id1, txt_country_name, txt_country_sortname, txt_state_id, txt_state_name, txt_country_id2;
    public static String txt_country_id1, txt_country_name, txt_country_sortname, txt_state_id, txt_state_name, txt_country_id2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regn_one);
       /* sharedpreferences = getSharedPreferences(sharedpreferenceName, Context.MODE_PRIVATE);
        String sharedpreferencesString = sharedpreferences.getString(sharedpreference_key_churchname, null);*/
        //Toast.makeText(getApplicationContext(),sharedpreferencesString,Toast.LENGTH_LONG).show();
        // if (sharedpreferencesString == null) {
        // txt_selected_church.setText("Select the church from the given list");
        // showPopUp();
        //  }else {
              /* Intent intent = new Intent(RegnOneActivity.this, LoginActivity.class);
                startActivity(intent);
                RegnOneActivity.this.finish();
            showPopUp();
    }*/
        showPopUp();
        txt_fname = (EditText) findViewById(R.id.txt_fname);
        txt_lname = (EditText) findViewById(R.id.txt_lname);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_addr1 = (EditText) findViewById(R.id.txt_addr1);
        txt_addr2 = (EditText) findViewById(R.id.txt_addr2);
        txt_city = (EditText) findViewById(R.id.txt_city);
        txt_phone = (EditText) findViewById(R.id.txt_phone);


        setCustomDesign();
        userclass.setTxt_country_id("-1");//default
        userclass.setTxt_state_id("-1");//default

        sendrequest_to_spinner();
        //setCustomClickListeners();
        FrameLayout imageButtonNext = (FrameLayout) findViewById(R.id.imageButtonNext);
        imageButtonNext.setOnClickListener(this);
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(this);
    }

    public void settingvalues() {
//TODO:
        userclass.setTxt_fname(txt_fname.getText().toString());
        userclass.setTxt_lname(txt_lname.getText().toString());
        userclass.setTxt_email(txt_email.getText().toString());
        userclass.setTxt_addr1(txt_addr1.getText().toString());
        userclass.setTxt_addr2(txt_addr2.getText().toString());
        userclass.setTxt_city(txt_city.getText().toString());
        userclass.setTxt_phone(txt_phone.getText().toString());
    }

    private void setCustomDesign() {
        //  _ctx = RegnOneActivity.this;
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

        ((TextView) findViewById(R.id.txt_city)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_phone)).setTypeface(regular_font);

    }

    @Override
    public void onClick(View v) {
        hideSoftKeyboard();
        int item = v.getId();
        switch (item) {
            case R.id.imageButtonNext:
                settingvalues();
                if(userclass.getTxt_country_id().equals("-1"))
                {
                    Toast.makeText(RegnOneActivity.this, "Please select a country", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(userclass.getTxt_state_id().equals("-1"))
                {
                    Toast.makeText(RegnOneActivity.this, "Please select a State", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                hideSoftKeyboard();
                alertDialog.dismiss();

                String txt_selected_church_name = txt_selected_church.getText().toString();
              //  _userSingletonModelClass.setTxt_selected_church_name(txt_selected_church_name);
                userclass.setChurch_name(txt_selected_church_name);



            }
        });
        Button btn_exit = (Button) promptsView.findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
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
        // final ProgressDialog progressDialog = new ProgressDialog(_ctx, ProgressDialog.STYLE_SPINNER);

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
        VolleyUtils.getInstance(RegnOneActivity.this).addToRequestQueue(stringRequestChurchList);//
      /*  progressDialog.show(_ctx, "", "Loading data...", true, true);
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
                selected_church_name= _arrListChurches.get(position);
                txt_selected_church.setText(selected_church_name);
            }
        });*/
    }
    //Volley code for popup ends...

    /*
    Volley code for spinner
     */
    public void sendrequest_to_spinner() {
        StringRequest stringRequest = new StringRequest(UrlConstants._URL_GET_COUNTRY_LIST, new Response.Listener<String>() {
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
        VolleyUtils.getInstance(this).addToRequestQueue(stringRequest);
    }
    public void showjson_to_spinner(String response_str) {
        final CountryModel countryModel = new CountryModel();

        try {
            JSONObject jsonObject = new JSONObject(response_str);
            JSONObject jsonObject_country = jsonObject.getJSONObject("data");

            countryModel.setCountry_id(jsonObject_country.getString("id"));
            countryModel.setCountry_name(jsonObject_country.getString("name"));
            countryModel.setCountry_short_name(jsonObject_country.getString("sortname"));

            JSONArray jsonArrayStates = jsonObject_country.getJSONArray("state");
            for (int i = 0; i < jsonArrayStates.length(); i++) {
                JSONObject jsonObject_state = jsonArrayStates.getJSONObject(i);

                StateModel stateModel = new StateModel();
                stateModel.setState_id(jsonObject_state.getString("id"));
                stateModel.setState_name(jsonObject_state.getString("name"));
                stateModel.setState_country_id(jsonObject_state.getString("country_id"));

                countryModel.addStateModel(stateModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ArrayList<String> arraylist_country_name = new ArrayList<>();
        arraylist_country_name.add(countryModel.getCountry_name());

        ArrayList<String> arrayList_state_name = new ArrayList<>();
        for (StateModel temp_sModel :
                countryModel.getStateModelList()) {
            arrayList_state_name.add(temp_sModel.getState_name());
        }

        spinner_country= (Spinner) findViewById(R.id.spinner_country_name);
        spinner_country.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,arraylist_country_name ));
spinner_country.setSelection(0);

        spinner_state= (Spinner) findViewById(R.id.spinner_state);
        spinner_state.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_state_name));
spinner_state.setSelection(0);

        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txt_country_id1 = countryModel.getCountry_id();
                txt_country_name = countryModel.getCountry_name();
                txt_country_sortname = countryModel.getCountry_short_name();

//                Toast.makeText(getApplicationContext(), txt_country_id1 + "\n" + txt_country_name, Toast.LENGTH_LONG).show();
                userclass.setTxt_country_id(txt_country_id1);
                userclass.setTxt_country(txt_country_name);
                userclass.setTxt_country_sortname(txt_country_sortname);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txt_state_id = countryModel.getStateModelList().get(position).getState_id();
                txt_state_name = countryModel.getStateModelList().get(position).getState_name();
                txt_country_id2 = countryModel.getStateModelList().get(position).getState_country_id();
           //     Toast.makeText(getApplicationContext(), txt_state_id + "\n" + txt_state_name, Toast.LENGTH_LONG).show();
                userclass.setTxt_state_id(txt_state_id);
                userclass.setTxt_state_name(txt_state_name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


//-------------------Volley code for spinner ends--------------------------
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

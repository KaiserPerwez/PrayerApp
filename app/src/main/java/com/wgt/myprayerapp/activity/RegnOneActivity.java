package com.wgt.myprayerapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
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
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.wgt.myprayerapp.R;
import com.wgt.myprayerapp.Utils.ValidatorUtils;
import com.wgt.myprayerapp.model.CountryModel;
import com.wgt.myprayerapp.model.StateModel;
import com.wgt.myprayerapp.model.UserSingletonModelClass;
import com.wgt.myprayerapp.networking.UrlConstants;
import com.wgt.myprayerapp.networking.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

public class RegnOneActivity extends AppCompatActivity implements View.OnClickListener {
    Context _ctx;
    EditText txt_fname, txt_lname, txt_email, txt_addr1, txt_addr2, txt_state, txt_phone, txt_city;
    Spinner spinner_country, spinner_state;
    UserSingletonModelClass _userSingletonModelClass = UserSingletonModelClass.get_userSingletonModelClass();
    /*
    *Adding variable for popup...
     */
    ListView lv_churches;
    ArrayList<String> arrListChurches = new ArrayList<>();
    ArrayList<String> arrListChurchesId = new ArrayList<>();
    ArrayAdapter<String> adapter;
    EditText txt_selected_church, txt_search_church;
    String selected_church_name;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regn_one);
        _ctx = RegnOneActivity.this;
        showPopUp();
        txt_fname = (EditText) findViewById(R.id.txt_fname);
        txt_lname = (EditText) findViewById(R.id.txt_lname);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_addr1 = (EditText) findViewById(R.id.txt_addr1);
        txt_addr2 = (EditText) findViewById(R.id.txt_addr2);
        txt_city = (EditText) findViewById(R.id.txt_city);
        txt_phone = (EditText) findViewById(R.id.txt_phone);


        setCustomDesign();
        _userSingletonModelClass.setTxt_country_id("-1");//default
        _userSingletonModelClass.setTxt_state_id("-1");//default

        fetchCountriesFromServer();
        FrameLayout imageButtonNext = (FrameLayout) findViewById(R.id.imageButtonNext);
        imageButtonNext.setOnClickListener(this);
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(this);

        txt_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (txt_email.isFocused() || txt_email.getText().toString().length() == 0)
                    return;
                else if (!ValidatorUtils.isValidEmail(txt_email.getText().toString())) {
                    txt_email.setError("Email Format is invalid");
                    return;
                } else {
                    LayoutInflater li = LayoutInflater.from(_ctx);
                    final View promptsView = li.inflate(R.layout.verify_email_dialog, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_ctx);// set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);
                    // set dialog message
                    alertDialogBuilder.setCancelable(false);
                    alertDialog = alertDialogBuilder.create();// create alert dialog
                    alertDialog.show();

                    final EditText txt = (EditText) promptsView.findViewById(R.id.txt_otp);
                    txt.setHint("Re-Enter Email");
                    txt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                    Button btn_verify = (Button) promptsView.findViewById(R.id.btn_verify);
                    btn_verify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String email1 = txt_email.getText().toString();
                            String email2 = txt.getText().toString();
                            txt.setError(null);

                            if (email2.length() == 0)
                                txt.setError("Email can't be empty");
                            else if (!ValidatorUtils.isValidEmail(email2))
                                txt.setError("Email Format is invalid");
                            else if (!email1.equals(email2))
                                txt.setError("Emails didn't Match");
                            else
                                alertDialog.cancel();
                        }
                    });

                    Button btn_back = (Button) promptsView.findViewById(R.id.btn_back);
                    btn_back.setVisibility(View.GONE);
                }
            }
        });
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
                if (setFieldDataToUserSingletonObject()) {
                    Intent intent = new Intent(_ctx, RegnTwoActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.imageButtonPrev:
                finish();
                break;
        }
    }

    private boolean setFieldDataToUserSingletonObject() {
        txt_fname.setError(null);
        txt_lname.setError(null);
        txt_email.setError(null);
        txt_city.setError(null);

        if (txt_fname.getText().toString().length() == 0) {
            txt_fname.requestFocus();
            txt_fname.setError("First Name Field can't be empty");
            return false;
        }
        if (txt_lname.getText().toString().length() == 0) {
            txt_lname.requestFocus();
            txt_lname.setError("Last Name Field can't be empty");
            return false;
        }

        if (txt_email.getText().toString().length() == 0) {
            txt_email.requestFocus();
            txt_email.setError("Email Field can't be empty");
            return false;
        }
        if (!ValidatorUtils.isValidEmail(txt_email.getText().toString())) {
            txt_email.requestFocus();
            txt_email.setError("Invalid email format");
            return false;
        }

        if (txt_city.getText().toString().length() == 0) {
            txt_city.requestFocus();
            txt_city.setError("City Field can't be empty");
            return false;
        }

        _userSingletonModelClass.setTxt_fname(txt_fname.getText().toString());
        _userSingletonModelClass.setTxt_lname(txt_lname.getText().toString());
        _userSingletonModelClass.setTxt_email(txt_email.getText().toString());
        _userSingletonModelClass.setTxt_addr1(txt_addr1.getText().toString());
        _userSingletonModelClass.setTxt_addr2(txt_addr2.getText().toString());
        _userSingletonModelClass.setTxt_city(txt_city.getText().toString());
        _userSingletonModelClass.setTxt_phone(txt_phone.getText().toString());
        if (_userSingletonModelClass.getTxt_country_id().equals("-1")) {
            Toast.makeText(RegnOneActivity.this, "Please select a country", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (_userSingletonModelClass.getTxt_state_id().equals("-1")) {
            Toast.makeText(RegnOneActivity.this, "Please select a State", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /*
    *Popup code...
     */
    private void showPopUp() {

        LayoutInflater li = LayoutInflater.from(_ctx);// get prompts.xml view
        final View promptsView = li.inflate(R.layout.add_church_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_ctx);// set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        // set dialog message
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();// create alert dialog
        alertDialog.show();

        //Calling method of volley
        fetchChurchesFromServer();

        txt_selected_church = (EditText) promptsView.findViewById(R.id.txt_selected_churchname);
        txt_search_church = (EditText) promptsView.findViewById(R.id.search_church);
        txt_search_church.requestFocus();
        lv_churches = (ListView) promptsView.findViewById(R.id.church_lv);

        Button btn_submit = (Button) promptsView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_selected_church_name = txt_selected_church.getText().toString();
                if (txt_selected_church_name.length() == 0) {
                    Toast.makeText(_ctx, "Selected Church-name can't be empty", Toast.LENGTH_SHORT).show();
                    hideSoftKeyboard();
                    return;
                }

                hideSoftKeyboard();
                alertDialog.dismiss();
                String selectedChurchId = arrListChurchesId.get(arrListChurches.indexOf(txt_selected_church_name));
                _userSingletonModelClass.setChurch_id(selectedChurchId);
                _userSingletonModelClass.setChurch_name(txt_selected_church_name);


            }
        });
        Button btn_exit = (Button) promptsView.findViewById(R.id.btn_close);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                finish();
            }
        });
    }

    //Popup code ends...
    /*
    Volley code for popup...
     */
    public void fetchChurchesFromServer() {
        final ProgressDialog progressDialog = new ProgressDialog(RegnOneActivity.this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequestChurchList = new StringRequest(UrlConstants._URL_CHURCH_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String responseStr) {
                loadChurchesOnListView(responseStr);
                progressDialog.cancel();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegnOneActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        });
        VolleyUtils.getInstance(RegnOneActivity.this).addToRequestQueue(stringRequestChurchList);//
    }

    public void loadChurchesOnListView(String responseStr) {
        try {
            JSONObject jsonObject = new JSONObject(responseStr);
            JSONArray jsonArrayChurchList = jsonObject.getJSONArray("data");
            if (!arrListChurches.isEmpty()) {
                arrListChurches.clear();
                arrListChurchesId.clear();
            }

            for (int i = 0; i < jsonArrayChurchList.length(); i++) {
                JSONObject jsonObjectChurchName = jsonArrayChurchList.getJSONObject(i);
                String churchName = jsonObjectChurchName.getString("church_name");
                arrListChurches.add(churchName);
                String churchId = jsonObjectChurchName.getString("id");
                arrListChurchesId.add(churchId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this, R.layout.dialog_row, R.id.txt_churchname_lst, arrListChurches);
        lv_churches.setAdapter(adapter);
        lv_churches.invalidateViews();
        lv_churches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(alertDialog.getWindow().getDecorView().getWindowToken(), 0);
                selected_church_name = adapter.getItem(position);
                txt_selected_church.setText(selected_church_name);
                txt_search_church.clearFocus();
                txt_selected_church.requestFocus();
            }
        });
        txt_search_church.addTextChangedListener(new TextWatcher() {
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
    }
    //Volley code for popup ends...

    /*
    Volley code for spinner
     */
    public void fetchCountriesFromServer() {
        StringRequest stringRequest = new StringRequest(UrlConstants._URL_COUNTRY_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadCountriesOnSpinner(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleyUtils.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void loadCountriesOnSpinner(String response_str) {
        final CountryModel countryModel = new CountryModel();

        try {
            JSONObject jsonObject = new JSONObject(response_str);
            JSONObject jsonObject_country = jsonObject.getJSONObject("data");

            countryModel.setCountry_id(jsonObject_country.getString("id"));
            countryModel.setCountry_name(jsonObject_country.getString("name"));
            countryModel.setCountry_short_name(jsonObject_country.getString("sortname"));

            _userSingletonModelClass.setTxt_country_id(jsonObject_country.getString("id"));
            _userSingletonModelClass.setTxt_country(jsonObject_country.getString("name"));
            _userSingletonModelClass.setTxt_country_sortname(jsonObject_country.getString("sortname"));


            JSONArray jsonArrayStates = jsonObject_country.getJSONArray("state");
            for (int i = 0; i < jsonArrayStates.length(); i++) {
                JSONObject jsonObject_state = jsonArrayStates.getJSONObject(i);

                StateModel stateModel = new StateModel();
                stateModel.setState_id(jsonObject_state.getString("id"));
                stateModel.setState_name(jsonObject_state.getString("name"));
                stateModel.setState_country_id(jsonObject_state.getString("country_id"));

                countryModel.addStateModel(stateModel);
            }

            _userSingletonModelClass.setTxt_state_id(jsonArrayStates.getJSONObject(0).getString("id"));
            _userSingletonModelClass.setTxt_state_name(jsonArrayStates.getJSONObject(0).getString("name"));

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

        spinner_country = (Spinner) findViewById(R.id.spinner_country_name);
        spinner_country.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraylist_country_name));
        spinner_country.setSelection(0);

        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_state.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayList_state_name));
        spinner_state.setSelection(0);


        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String txt_country_id1 = countryModel.getCountry_id();
                String txt_country_name = countryModel.getCountry_name();
                String txt_country_sortname = countryModel.getCountry_short_name();

                _userSingletonModelClass.setTxt_country_id(txt_country_id1);
                _userSingletonModelClass.setTxt_country(txt_country_name);
                _userSingletonModelClass.setTxt_country_sortname(txt_country_sortname);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String txt_state_id = countryModel.getStateModelList().get(position).getState_id();
                String txt_state_name = countryModel.getStateModelList().get(position).getState_name();
                String txt_country_id2 = countryModel.getStateModelList().get(position).getState_country_id();

                _userSingletonModelClass.setTxt_state_id(txt_state_id);
                _userSingletonModelClass.setTxt_state_name(txt_state_name);
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

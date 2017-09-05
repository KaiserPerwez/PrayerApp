package webgentechnologies.com.myprayerapp.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.UserLocationModel;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;

/**
 * Created by Kaiser on 25-07-2017.
 */

public class EditOneFrag extends Fragment implements TextWatcher {

    View rootView;
    public static EditText txt_fname, txt_lname, txt_email, txt_addr1, txt_addr2, txt_city, txt_phone;
    Spinner spinner_state, spinner_country;

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
    ArrayList<UserLocationModel> arrayList_country_details = new ArrayList<>();
    ArrayList<String> arraylist_country_name = new ArrayList<>();
    ArrayList<UserLocationModel> arraylist_state_details = new ArrayList<>();
    ArrayList<String> arrayList_state_name = new ArrayList<>();
    public static String txt_editone_country_id1, txt_editone_country_name, txt_editone_country_sortname, txt_editone_state_id, txt_editone_state_name, txt_editone_country_id2;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_edit_one, container, false);
        txt_fname = (EditText) rootView.findViewById(R.id.txt_fname);
        txt_lname = (EditText) rootView.findViewById(R.id.txt_lname);
        txt_email = (EditText) rootView.findViewById(R.id.txt_email);
        txt_addr1 = (EditText) rootView.findViewById(R.id.txt_addr1);
        txt_addr2 = (EditText) rootView.findViewById(R.id.txt_addr2);
        txt_city = (EditText) rootView.findViewById(R.id.txt_city);
        txt_phone = (EditText) rootView.findViewById(R.id.txt_phone);
        txt_fname.setText(userclass.getTxt_fcbl_lgn_first_name());
        txt_lname.setText(userclass.getTxt_fcbk_lgn_last_name());
        txt_email.setText(userclass.getTxt_fcbk_lgn_email());
        spinner_state = (Spinner) rootView.findViewById(R.id.spinner_state);
        spinner_country = (Spinner) rootView.findViewById(R.id.txt_country);
        setCustomDesign();
        // addItemsOnStateSpinner();
        // setCustomClickListeners();
        showPopUp();
        sendrequest_to_spinner();

        /*
        As Textwatcher(addTextChangedListener would not be used further that's why I have commented below codes
         */
      /*  txt_fname.addTextChangedListener(this);
        txt_lname.addTextChangedListener(this);
        // txt_email.addTextChangedListener(this);
        txt_addr1.addTextChangedListener(this);
        txt_addr2.addTextChangedListener(this);
        txt_city.addTextChangedListener(this);
        txt_phone.addTextChangedListener(this);*/
        return rootView;
    }

    private void setCustomClickListeners() {
//
    }

    private void setCustomDesign() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Typeface regular_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");

        ((TextView) rootView.findViewById(R.id.tv_regn_one)).setTypeface(regular_font);
        ((TextView) rootView.findViewById(R.id.tv_regn_one)).setText("Edit Profile");
        ((TextView) rootView.findViewById(R.id.tv_regn_step1)).setTypeface(regular_font);
        ((TextView) rootView.findViewById(R.id.tv_regn_step1)).setText("Step (1/3)");
        ((TextView) rootView.findViewById(R.id.txt_fname)).setTypeface(regular_font);
        ((TextView) rootView.findViewById(R.id.txt_lname)).setTypeface(regular_font);
        //((TextView) rootView.findViewById(R.id.txt_email)).setTypeface(regular_font);

//        ((TextView)findViewById(R.id.txt_country)).setTypeface(regular_font);
        ((TextView) rootView.findViewById(R.id.txt_addr1)).setTypeface(regular_font);
        ((TextView) rootView.findViewById(R.id.txt_addr2)).setTypeface(regular_font);
        // ((TextView)findViewById(R.id.txt_city)).setTypeface(regular_font);

        ((TextView) rootView.findViewById(R.id.txt_city)).setTypeface(regular_font);
        ((TextView) rootView.findViewById(R.id.txt_phone)).setTypeface(regular_font);

    }


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
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void showjson_to_spinner(String spinner_json) {
        try {
            UserLocationModel userLocationModel = new UserLocationModel();
            JSONObject jsonObject_Country = new JSONObject(spinner_json);
            JSONObject jsonObject_country = jsonObject_Country.getJSONObject("data");
            arraylist_country_name.add(jsonObject_country.getString("name"));
            userLocationModel.setTxt_country_id1(jsonObject_country.getString("id"));
            userLocationModel.setTxt_country(jsonObject_country.getString("name"));
            userLocationModel.setTxt_country_sortname(jsonObject_country.getString("sortname"));
            arrayList_country_details.add(userLocationModel);
            JSONArray jsonArray = jsonObject_country.getJSONArray("state");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject_state = jsonArray.getJSONObject(i);
                UserLocationModel userLocationModel1 = new UserLocationModel();
                arrayList_state_name.add(jsonObject_state.getString("name"));
                userLocationModel1.setTxt_state_id(jsonObject_state.getString("id"));
                userLocationModel1.setTxt_state(jsonObject_state.getString("name"));
                userLocationModel1.setTxt_country_id2(jsonObject_state.getString("country_id"));
                arraylist_state_details.add(userLocationModel1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        spinner_country.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arraylist_country_name));
        spinner_state.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayList_state_name));
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txt_editone_country_id1 = arrayList_country_details.get(position).getTxt_country_id1().toString();
                txt_editone_country_name = arrayList_country_details.get(position).getTxt_country().toString();
                txt_editone_country_sortname = arrayList_country_details.get(position).getTxt_country_sortname().toString();
                Toast.makeText(getActivity(), txt_editone_country_id1 + "\n" + txt_editone_country_name, Toast.LENGTH_LONG).show();
                userclass.setTxt_editone_countryid1(txt_editone_country_id1);
                userclass.setTxt_editone_country_name(txt_editone_country_name);
                userclass.setTxt_editone_country_sortname(txt_editone_country_sortname);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txt_editone_state_id = arraylist_state_details.get(position).getTxt_state_id();
                txt_editone_state_name = arraylist_state_details.get(position).getTxt_state();
                txt_editone_country_id2 = arraylist_state_details.get(position).getTxt_country_id2();
                Toast.makeText(getActivity().getApplicationContext(), txt_editone_state_id + "\n" + txt_editone_state_name, Toast.LENGTH_LONG).show();
                userclass.setTxt_editone_state_id(txt_editone_state_id);
                userclass.setTxt_editone_state_name(txt_editone_state_name);
                userclass.setTxt_editone_country_id2(txt_editone_country_id2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //----Volley code for spinner ends---

    /*
    *Textwatcher code
     */

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == txt_fname.getEditableText()) {
            Toast.makeText(getActivity().getApplicationContext(), txt_fname.getText().toString(), Toast.LENGTH_LONG).show();
            userclass.setTxt_editone_fname(txt_fname.getText().toString());
        }
        if (s == txt_lname.getEditableText()) {
            userclass.setTxt_editone_lname(txt_lname.getText().toString());
            Toast.makeText(getActivity().getApplicationContext(), userclass.getTxt_editone_lname(), Toast.LENGTH_LONG).show();
        }
       /* if(s==txt_email.getEditableText())
            userclass.setTxt_editone_email(txt_email.getText().toString());*/
        if (s == txt_addr1.getEditableText())
            userclass.setTxt_editone_addr1(txt_addr1.getText().toString());
        if (s == txt_addr2.getEditableText())
            userclass.setTxt_editone_addr2(txt_addr2.getText().toString());
        if (s == txt_city.getEditableText())
            userclass.setTxt_editone_city(txt_city.getText().toString());
        if (s == txt_phone.getEditableText())
            userclass.setTxt_editone_phone(txt_phone.getText().toString());
    }
    //--------Textwatcher code ends---------------


    /*
   Volley code for popup...
    */
    public void sendRequest() {
        // final ProgressDialog progressDialog = new ProgressDialog(m_ctx, ProgressDialog.STYLE_SPINNER);

        // progressDialog.setIndeterminate(true);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
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
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.hide();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.dialog_row, R.id.txt_churchname_lst, arrListChurches);
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
                //  getActivity().this.adapter.getFilter().filter(s);
                // String text = txt_search_prayer.getText().toString().toLowerCase(Locale.getDefault());
                EditOneFrag.this.adapter.getFilter().filter(s);
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
  *Popup code...
   */
    private void showPopUp() {

        LayoutInflater li = LayoutInflater.from(getActivity());// get prompts.xml view
        final View promptsView = li.inflate(R.layout.add_church_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());// set prompts.xml to alertdialog builder
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
                getActivity().finish();
            }
        });
    }

    //Popup code ends...

}

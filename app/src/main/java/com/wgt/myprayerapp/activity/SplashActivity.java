package com.wgt.myprayerapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.wgt.myprayerapp.R;
import com.wgt.myprayerapp.Utils.PrefUtils;
import com.wgt.myprayerapp.model.UserSingletonModelClass;
import com.wgt.myprayerapp.networking.ConnectionDetector;
import com.wgt.myprayerapp.networking.UrlConstants;
import com.wgt.myprayerapp.networking.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    Context _ctx;
    UserSingletonModelClass _userSingletonModelClass = UserSingletonModelClass.get_userSingletonModelClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        _ctx = SplashActivity.this;

        if (!checkCompatibility()) {
            Toast.makeText(_ctx, "Tab Not supported", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_ctx);// set prompts.xml to alertdialog builder
            alertDialogBuilder.setTitle("Alert");
            alertDialogBuilder.setMessage("App not supported on Tablets. Please contact the developer.");
            // set dialog message
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("I Got it. Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    SplashActivity.this.finish();
                }
            });
            final AlertDialog alertDialog = alertDialogBuilder.create();// create alert dialog
            alertDialog.show();
        } else {
            if (ConnectionDetector.isConnectedToInternet(_ctx))
                loadDataFromPref();
            else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_ctx);// set prompts.xml to alertdialog builder
                alertDialogBuilder.setTitle("Alert");
                alertDialogBuilder.setMessage("No internet Connectivity.App will exit.");
                // set dialog message
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SplashActivity.this.finish();
                    }
                });
                final AlertDialog alertDialog = alertDialogBuilder.create();// create alert dialog
                alertDialog.show();
            }
        }
    }

    private boolean checkCompatibility() {
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
        if (diagonalInches >= 6.5) {
            // 6.5inch device or bigger
            return false;
        } else {
            return true;
        }
    }

    private void loadDataFromPref() {
        SharedPreferences prefs = getSharedPreferences(PrefUtils._PREF_PRAYER_APP, MODE_PRIVATE);
        String restoredText = prefs.getString(PrefUtils._PREF_KEY_LOGIN_ID, null);
        if (restoredText != null) {
            _userSingletonModelClass.setTxt_user_login_id(prefs.getString(PrefUtils._PREF_KEY_LOGIN_ID, ""));
            _userSingletonModelClass.setTxt_user_access_token(prefs.getString(PrefUtils._PREF_KEY_LOGIN_ACCESS_TOKEN, ""));
            load_ProfileDetails();
        } else {
            startActivity(new Intent(_ctx, LoginActivity.class));
            SplashActivity.this.finish();
        }
    }


    public void load_ProfileDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(SplashActivity.this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading data...");
        progressDialog.setCancelable(false);
        if (!(progressDialog.isShowing()))
            progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_USER_REGISTRATION_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                        JSONObject jobdata = job.getJSONObject("data");
                        String access_token = jobdata.getString("access_token");
                        _userSingletonModelClass.setTxt_user_access_token(access_token);
                        JSONArray userjsonarray = jobdata.getJSONArray("user");
                        for (int i = 0; i < userjsonarray.length(); i++) {
                            JSONObject jobuser = userjsonarray.getJSONObject(i);

                            _userSingletonModelClass.setTxt_user_login_id(jobuser.getString("id"));
                            _userSingletonModelClass.setTxt_fname(jobuser.getString("first_name"));
                            _userSingletonModelClass.setTxt_lname(jobuser.getString("last_name"));
                            _userSingletonModelClass.setTxt_email(jobuser.getString("email"));

                            _userSingletonModelClass.setTxt_country_id(jobuser.getString("country_id"));
                            _userSingletonModelClass.setTxt_country(jobuser.getString("country_name"));
                            _userSingletonModelClass.setTxt_addr1(jobuser.getString("address1"));
                            _userSingletonModelClass.setTxt_addr2(jobuser.getString("address2"));
                            _userSingletonModelClass.setTxt_city(jobuser.getString("city"));
                            _userSingletonModelClass.setTxt_state_id(jobuser.getString("state_id"));
                            _userSingletonModelClass.setTxt_state_name(jobuser.getString("state_name"));
                            _userSingletonModelClass.setTxt_phone(jobuser.getString("phone"));
                            _userSingletonModelClass.setChurch_id(jobuser.getString("church_id"));

                            String classes_attended[] = jobuser.getString("classes").split(";");
                            for (String str :
                                    classes_attended) {
                                if (str.length() > 0)
                                    _userSingletonModelClass.addClassesAttended(str);
                            }

                            _userSingletonModelClass.setTxt_mission_trip_countries(jobuser.getString("mission_trip"));
                            _userSingletonModelClass.setTxt_mission_trip_participation_status(jobuser.getString("mission_trip_status"));
                            _userSingletonModelClass.setTxt_newto_mission(jobuser.getString("mission_concept"));
                        }
                    } else if (status.equals("false")) {
                        Toast.makeText(_ctx, "NO/PARTIAL DATA FOUND", Toast.LENGTH_LONG).show();
                    }
                    startActivity(new Intent(_ctx, HomeActivity.class));
                    SplashActivity.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (progressDialog.isShowing())
                    progressDialog.cancel();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                Toast.makeText(_ctx, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", _userSingletonModelClass.getTxt_user_login_id());
                params.put("access_token", _userSingletonModelClass.getTxt_user_access_token());

                return params;
            }
        };
        VolleyUtils.getInstance(this).addToRequestQueue(stringRequest);
    }

}

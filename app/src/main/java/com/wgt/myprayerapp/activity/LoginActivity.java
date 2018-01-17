package com.wgt.myprayerapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.wgt.myprayerapp.R;
import com.wgt.myprayerapp.Utils.PrefUtils;
import com.wgt.myprayerapp.Utils.ValidatorUtils;
import com.wgt.myprayerapp.model.UserSingletonModelClass;
import com.wgt.myprayerapp.networking.UrlConstants;
import com.wgt.myprayerapp.networking.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModelClass userSingletonModelClass = UserSingletonModelClass.get_userSingletonModelClass();

    Context _ctx;
    Button _btn_login;
    EditText _txt_email, _txt_password;
    TextView _tv_forgot_pwd, _tv_signUp;
    ProgressDialog progressDialog;
    LinearLayout _linearLayout_btnFb;
    /*
    Facebook login variables
     */
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton loginButtonFB;
    /*
    *Facebook onSuccess code
     */

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            // Application code
                            try {
                                userSingletonModelClass.setTxt_fname(object.getString("first_name"));
                                userSingletonModelClass.setTxt_lname(object.getString("last_name"));
                                userSingletonModelClass.setTxt_email(object.getString("email"));
                                userSingletonModelClass.setReg_type("facebook");
                                register_FbUser();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Toast.makeText(_ctx, "User cancelled login", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(_ctx, "FB Login Error:" + error.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        _ctx = LoginActivity.this;

        _txt_email = (EditText) findViewById(R.id.txt_email);
        _txt_password = (EditText) findViewById(R.id.txt_password);
        _btn_login = (Button) findViewById(R.id.btn_login);
        _tv_forgot_pwd = (TextView) findViewById(R.id.tv_forgot_pwd);
        _tv_signUp = (TextView) findViewById(R.id.tv_signUp);
        _linearLayout_btnFb = (LinearLayout) findViewById(R.id.linearLayout_btnFb);


        _btn_login.setOnClickListener(this);
        _tv_forgot_pwd.setOnClickListener(this);
        _tv_signUp.setOnClickListener(this);
        _linearLayout_btnFb.setOnClickListener(this);
        setCustomDesign();

        progressDialog = new ProgressDialog(LoginActivity.this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }

        ///---------Facebook code ends inside onCreate()-------------
    }

    /*
    Facebook login override methods
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (accessTokenTracker != null)
            accessTokenTracker.stopTracking();
        if (profileTracker != null)
            profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
    }

    //-------Facebook login override method ends---------

    private void setCustomDesign() {
        _ctx = LoginActivity.this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface semiBold_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-SemiBold.ttf");

        ((TextView) findViewById(R.id.tv_hello)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_msg)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_email)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_password)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.btn_login)).setTypeface(semiBold_font);
        ((TextView) findViewById(R.id.tv_forgot_pwd)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_signUp)).setTypeface(regular_font);
        //  ((TextView) findViewById(R.id.tv_fb_login)).setTypeface(regular_font);
        //  ((TextView) findViewById(R.id.tv_fb)).setTypeface(semiBold_font);
    }

    @Override
    public void onClick(View v) {
        userSingletonModelClass.setReg_type("normal");
        hideSoftKeyboard();
        int item = v.getId();
        switch (item) {
            case R.id.btn_login:
                if (_txt_email.getText().toString().length() > 0 && _txt_password.getText().toString().length() > 0) {
                    if (!ValidatorUtils.isValidEmail(_txt_email.getText().toString())) {
                        _txt_email.setError("INVALID Email");
                        return;
                    }
                    userSingletonModelClass.setTxt_email(_txt_email.getText().toString());
                    userSingletonModelClass.setTxt_pswd(_txt_password.getText().toString());
                    login();
                } else {
                    if (_txt_email.getText().toString().length()==0)
                        _txt_email.setError("Email can't be blank");

                    if (_txt_password.getText().toString().length()==0)
                        _txt_password.setError("Password can't be blank");
                }
                break;
            case R.id.tv_forgot_pwd:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordOneActivity.class));
                break;
            case R.id.tv_signUp:
                startActivity(new Intent(LoginActivity.this, RegnOneActivity.class));
                break;
            case R.id.linearLayout_btnFb:
                userSingletonModelClass.setReg_type("facebook");
//                if (!(progressDialog.isShowing()))
//                    progressDialog.show();
                loginButtonFB = new LoginButton(this);
                callbackManager = CallbackManager.Factory.create();
                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

                    }
                };
                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                    }
                };
                accessTokenTracker.startTracking();
                profileTracker.startTracking();
                loginButtonFB.setReadPermissions(Arrays.asList("public_profile", "email"));
                loginButtonFB.setTextLocale(Locale.ENGLISH);
                loginButtonFB.registerCallback(callbackManager, callback);
                loginButtonFB.performClick();
//                if (progressDialog.isShowing())
//                    progressDialog.cancel();
                break;
        }
    }

    public void login() {
        if (!(progressDialog.isShowing()))
            progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_USER_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("true")) {
                        JSONObject jsonDataObject = jsonObject.getJSONObject("data");
                        String id = jsonDataObject.getString("id");
                        String accesstoken = jsonDataObject.getString("accessToken");
                        userSingletonModelClass.setTxt_user_login_id(id);
                        userSingletonModelClass.setTxt_user_access_token(accesstoken);

                        load_LoginDetails_OnPrefs();
                        load_ProfileDetails();
                    } else {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(_ctx, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    if (progressDialog.isShowing())
                        progressDialog.cancel();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                Toast.makeText(_ctx, "VolleyErr:" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", _txt_email.getText().toString());
                params.put("password", _txt_password.getText().toString());
                params.put("device_id", userSingletonModelClass.getDevice_id());
                params.put("device_type", userSingletonModelClass.getDevice_type());

                return params;
            }
        };
        VolleyUtils.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void load_LoginDetails_OnPrefs() {
        SharedPreferences.Editor editor = getSharedPreferences(PrefUtils._PREF_PRAYER_APP, MODE_PRIVATE).edit();
        editor.putString(PrefUtils._PREF_KEY_LOGIN_ID, userSingletonModelClass.getTxt_user_login_id());
        editor.putString(PrefUtils._PREF_KEY_LOGIN_ACCESS_TOKEN, userSingletonModelClass.getTxt_user_access_token());
        editor.apply();
    }

    public void register_FbUser() {
        if (!(progressDialog.isShowing()))
            progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_REGISTER_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                        login_FbUser();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                Toast.makeText(_ctx, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", userSingletonModelClass.getTxt_fname());
                params.put("last_name", userSingletonModelClass.getTxt_lname());
                params.put("email", userSingletonModelClass.getTxt_email());
                params.put("country_id", "");
                params.put("country_name", "");
                params.put("address1", "");
                params.put("address2", "");
                params.put("city", "");
                params.put("state_id", "");
                params.put("state_name", "");
                params.put("phone", "");
                params.put("church_name", "");
                params.put("classes", "");
                params.put("mission_trip", "");
                params.put("mission_concept", "");
                params.put("password", "");
                params.put("device_id", userSingletonModelClass.getDevice_id());
                params.put("device_type", userSingletonModelClass.getDevice_type());
                userSingletonModelClass.setReg_type("facebook");

                params.put("reg_type", userSingletonModelClass.getReg_type());
                return params;
            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;

            }
        };
        VolleyUtils.getInstance(this).addToRequestQueue(stringRequest);
    }

    /*
    *Volley code for login for facebook users
    * This is normal login fetched from api made in php.
    * This login() will be called after facebook login
     */
    public void login_FbUser() {
        if (!(progressDialog.isShowing()))
            progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_USER_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("true")) {
                        JSONObject jsonObject_data = jsonObject.getJSONObject("data");
                        userSingletonModelClass.setTxt_user_login_id(jsonObject_data.getString("id"));
                        userSingletonModelClass.setTxt_user_access_token(jsonObject_data.getString("accessToken"));
                        userSingletonModelClass.setTxt_fname(jsonObject_data.getString("firstName"));
                        userSingletonModelClass.setTxt_lname(jsonObject_data.getString("lastName"));
                        userSingletonModelClass.setTxt_email(jsonObject_data.getString("email"));
                        load_LoginDetails_OnPrefs();
                        load_ProfileDetails();


                    } else {
                        Toast.makeText(_ctx, "Error" + response, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                Toast.makeText(_ctx, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", userSingletonModelClass.getTxt_email());
                params.put("password", "");
                params.put("device_id", userSingletonModelClass.getDevice_id());
                params.put("device_type", userSingletonModelClass.getDevice_type());

                return params;
            }
        };
        VolleyUtils.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void load_ProfileDetails() {
        if (!(progressDialog.isShowing()))
            progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_USER_REGISTRATION_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                        {
                            JSONObject jobdata = job.getJSONObject("data");
                            String access_token = jobdata.getString("access_token");
                            userSingletonModelClass.setTxt_user_access_token(access_token);
                            JSONArray userjsonarray = jobdata.getJSONArray("user");
                            for (int i = 0; i < userjsonarray.length(); i++) {
                                JSONObject jobuser = userjsonarray.getJSONObject(i);
                                Log.e("&&&&&&","chruchnam :"+jobuser);

                                userSingletonModelClass.setTxt_user_login_id(jobuser.getString("id"));
                                userSingletonModelClass.setTxt_fname(jobuser.getString("first_name"));
                                userSingletonModelClass.setTxt_lname(jobuser.getString("last_name"));
                                userSingletonModelClass.setTxt_email(jobuser.getString("email"));

                                userSingletonModelClass.setTxt_country_id(jobuser.getString("country_id"));
                                userSingletonModelClass.setTxt_country(jobuser.getString("country_name"));
                                userSingletonModelClass.setTxt_addr1(jobuser.getString("address1"));
                                userSingletonModelClass.setTxt_addr2(jobuser.getString("address2"));
                                userSingletonModelClass.setTxt_city(jobuser.getString("city"));
                                userSingletonModelClass.setTxt_state_id(jobuser.getString("state_id"));
                                userSingletonModelClass.setTxt_state_name(jobuser.getString("state_name"));
                                userSingletonModelClass.setTxt_phone(jobuser.getString("phone"));
                                userSingletonModelClass.setChurch_id(jobuser.getString("church_id"));
                                userSingletonModelClass.setChurch_name(jobuser.getString("church_id"));

                                String classes_attended[] = jobuser.getString("classes").split(";");
                                for (String str :
                                        classes_attended) {
                                    if (str.length() > 0)
                                        userSingletonModelClass.addClassesAttended(str);
                                }

                                userSingletonModelClass.setTxt_mission_trip_countries(jobuser.getString("mission_trip"));
                                userSingletonModelClass.setTxt_mission_trip_participation_status(jobuser.getString("mission_trip_status"));
                                userSingletonModelClass.setTxt_newto_mission(jobuser.getString("mission_concept"));
                            }
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            LoginActivity.this.finish();
                        }
                    } else if (status.equals("false")) {
                        Toast.makeText(LoginActivity.this, "Data could't be fetched.Please re-login", Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
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
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userSingletonModelClass.getTxt_user_login_id());
                params.put("access_token", userSingletonModelClass.getTxt_user_access_token());

                return params;
            }
        };
        VolleyUtils.getInstance(this).addToRequestQueue(stringRequest);
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

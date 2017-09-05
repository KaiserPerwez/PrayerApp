package webgentechnologies.com.myprayerapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;
import webgentechnologies.com.myprayerapp.networking.VolleyUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();

    Context m_ctx;
    Button m_btn_login;
    EditText txt_email, txt_password;

    /*
    Facebook login variables
     */
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton loginButtonFB;
    ProgressDialog progressDialog;
    LinearLayout m_linearLayout_btnFb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        m_ctx=LoginActivity.this;
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_password = (EditText) findViewById(R.id.txt_password);

        txt_email.setText("satabhisha.wgt@gmail.com");
        txt_password.setText("123");
        setCustomDesign();
        m_btn_login = (Button) findViewById(R.id.btn_login);
        m_btn_login.setOnClickListener(this);

        progressDialog = new ProgressDialog(LoginActivity.this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);

        /*
        *Code to get KeyHash for android app
         */
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
        //------------Code for KeyHash ends--------------------

        /*
        *Facebook Login code---
         */
        loginButtonFB = (LoginButton) findViewById(R.id.btnfb);

        //  aQuery = new AQuery(this);

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
        loginButtonFB.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginButtonFB.setTextLocale(Locale.ENGLISH);
        loginButtonFB.registerCallback(callbackManager, callback);
        //---------Facebook code ends inside onCreate()-------------
    }

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
                            Log.v("LoginActivity", response.toString());

                            // Application code
                            try {
                                String birthday = "";
                                if (object.has("birthday")) {
                                    birthday = object.getString("birthday"); // 01/31/1980 format
                                }

                           /*  String fnm = object.getString("first_name");
                             String lnm = object.getString("last_name");
                             String mail = object.getString("email");
                             String gender = object.getString("gender");
                             String fid = object.getString("id");*/
                                userclass.setTxt_fname(object.getString("first_name"));
                                userclass.setTxt_lname(object.getString("last_name"));
                                userclass.setTxt_email(object.getString("email"));
                                // tvdetails.setText("Name: "+fnm+" "+lnm+" \n"+"Email: "+mail+" \n"+"Gender: "+gender+" \n"+"ID: "+fid+" \n"+"Birth Date: "+birthday);
                                // aQuery.id(ivpic).image("https://graph.facebook.com/" + fid + "/picture?type=large");
                                //https://graph.facebook.com/143990709444026/picture?type=large
                                //  Log.d("aswwww","https://graph.facebook.com/"+fid+"/picture?type=large");
                           /*  Intent i=new Intent(MainActivity.this,ActivitySecond.class);
                             startActivity(i);*/
                           userclass.setReg_type("facebook");
                                registerFcbkUser();
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

        }

        @Override
        public void onError(FacebookException error) {

        }
    };
    //Facebook Login onSuccess code ends---


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
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
    }

    //-------Facebook login override method ends---------

    private void setCustomDesign() {
        m_ctx = LoginActivity.this;
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
        hideSoftKeyboard();
        int item = v.getId();
        switch (item) {
            case R.id.btn_login:

                if (txt_email.getText().toString().length() > 0 && txt_password.getText().toString().length() > 0) {
                    userclass.setTxt_email(txt_email.getText().toString());
                    userclass.setTxt_pswd(txt_password.getText().toString());
                    login();
                } else {
                    if (txt_email.getText().toString().length() == 0)
                        txt_email.setError("Email can't be blank");

                    if (txt_password.getText().toString().length() == 0)
                        txt_email.setError("Password can't be blank");
                }
                break;
            case R.id.tv_forgot_pwd:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordOneActivity.class));
                break;
            case R.id.tv_signUp:
                userclass.setReg_type("normal");
                startActivity(new Intent(LoginActivity.this, RegnOneActivity.class));
                break;
        }
    }


    /*
     *Volley code for login
      */
    public void login() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_USER_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                        JSONObject jobdata = job.getJSONObject("data");
                        userclass.setTxt_user_login_id(jobdata.getString("id"));
                        userclass.setTxt_user_access_token(jobdata.getString("accessToken"));
                        userclass.setTxt_email(jobdata.getString("email"));
                        userclass.setTxt_fname(jobdata.getString("firstName"));
                        userclass.setTxt_lname(jobdata.getString("lastName"));
                        loadProfiledetails();
                    } else {
                        if (progressDialog.isShowing())
                            progressDialog.cancel();
                        Toast.makeText(m_ctx, "Incorrect email_id or password", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (progressDialog.isShowing())
                        progressDialog.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                Toast.makeText(m_ctx, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", txt_email.getText().toString());
                params.put("password", txt_password.getText().toString());
                params.put("device_id", userclass.getDevice_id());
                params.put("device_type", userclass.getDevice_type());

                return params;
            }
        };
        VolleyUtils.getInstance(this).addToRequestQueue(stringRequest);
    }
//----------Volley code for login ends------------

    //Volley code to register for facebook users in the api i.e made in php...
    public void registerFcbkUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_REGISTER_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                        //  startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        loginforfcbkusers();
                    } else if (status.equals("false")) {
                        Toast.makeText(m_ctx, "Already registered,now edit your profile", Toast.LENGTH_LONG).show();
                        // startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        loginforfcbkusers();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(m_ctx, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
              /*  params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL, email);*/
                params.put("first_name", userclass.getTxt_fname());
                params.put("last_name", userclass.getTxt_lname());
                params.put("email", userclass.getTxt_email());
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
                params.put("device_id",userclass.getDevice_id());
                params.put("device_type", userclass.getDevice_type());
                userclass.setReg_type("Facebook");
                params.put("reg_type", userclass.getReg_type());
                return params;
            }
            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;

            }
        };
        VolleyUtils.getInstance(this).addToRequestQueue(stringRequest);
    }
    //-----------Volley code for registration ends------------------


    /*
    *Volley code for login for facebook users
    * This is normal login fetched from api made in php.
    * This login() will be called after facebook login
     */
    public void loginforfcbkusers() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_USER_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                        JSONObject jobdata = job.getJSONObject("data");
                        userclass.setTxt_user_login_id(jobdata.getString("id"));
                        userclass.setTxt_user_access_token(jobdata.getString("accessToken"));
                        userclass.setTxt_fname(jobdata.getString("firstName"));
                        userclass.setTxt_lname(jobdata.getString("lastName"));
                        userclass.setTxt_email(jobdata.getString("email"));
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        loadProfiledetails();
                    } else {
                        Toast.makeText(m_ctx, "Error"+response.toString(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
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
                Toast.makeText(m_ctx, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", userclass.getTxt_email());
                params.put("password", "");
                params.put("device_id", userclass.getDevice_id());
                params.put("device_type", userclass.getDevice_type());

                return params;
            }
        };
        VolleyUtils.getInstance(this).addToRequestQueue(stringRequest);
    }

    //-----------Volley code for login for facebook users ends-----------


    /*
Volley code to get registration details
 */
    public void loadProfiledetails() {
        if (!(progressDialog.isShowing()))
            progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_GET_REGISTRATION_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                        {
                            JSONObject jobdata = job.getJSONObject("data");
                            String access_token = jobdata.getString("access_token");
                            userclass.setTxt_user_access_token(access_token);
                            JSONArray userjsonarray = jobdata.getJSONArray("user");
                            for (int i = 0; i < userjsonarray.length(); i++) {
                                JSONObject jobuser = userjsonarray.getJSONObject(i);

                                userclass.setTxt_user_login_id(jobuser.getString("id"));
                                userclass.setTxt_fname(jobuser.getString("first_name"));
                                userclass.setTxt_lname(jobuser.getString("last_name"));
                                userclass.setTxt_email(jobuser.getString("email"));

                                userclass.setTxt_country_id(jobuser.getString("country_id"));
                                userclass.setTxt_country(jobuser.getString("country_name"));
                                userclass.setTxt_addr1(jobuser.getString("address1"));
                                userclass.setTxt_addr2(jobuser.getString("address2"));
                                userclass.setTxt_city(jobuser.getString("city"));
                                userclass.setTxt_state_id(jobuser.getString("state_id"));
                                userclass.setTxt_state_name(jobuser.getString("state_name"));
                                userclass.setTxt_phone(jobuser.getString("phone"));
                                userclass.setChurch_id(jobuser.getString("church_id"));

                                String classes_attended[] = jobuser.getString("classes").split(";");
                                for (String str :
                                        classes_attended) {
                                    if (str.length() > 0)
                                        userclass.addClassesAttended(str);
                                }

                                userclass.setTxt_mission_trip_countries(jobuser.getString("mission_trip"));
                                userclass.setTxt_mission_trip_participation_status(jobuser.getString("mission_trip_status"));
                                userclass.setTxt_newto_mission(jobuser.getString("mission_concept"));
                            }
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            LoginActivity.this.finish();
                        }
                    } else if (status.equals("false")) {
                        Toast.makeText(LoginActivity.this, "Already registered,Please edit your profile", Toast.LENGTH_LONG).show();
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
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userclass.getTxt_user_login_id());
                params.put("access_token", userclass.getTxt_user_access_token());

                return params;
            }
        };
        VolleyUtils.getInstance(this).addToRequestQueue(stringRequest);
    }

    //--------------------    Volley code to get registration details ends-------------------------
void hideSoftKeyboard(){
    /* code to show keyboard on startup.this code is not working.*/
    InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow((null==getCurrentFocus())?null:getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideSoftKeyboard();
        return super.onTouchEvent(event);
    }
}

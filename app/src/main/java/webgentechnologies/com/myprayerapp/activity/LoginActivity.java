package webgentechnologies.com.myprayerapp.activity;

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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();

    Context m_ctx;
    Button m_btn_login;
    EditText _txt_email, _txt_password;

    /*
    Facebook login variables
     */
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;

    LinearLayout m_linearLayout_btnFb;
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
                                Log.d("Facebook id:", object.getString("id"));
                                String birthday = "";
                                if (object.has("birthday")) {
                                    birthday = object.getString("birthday"); // 01/31/1980 format
                                }

                           /*  String fnm = object.getString("first_name");
                             String lnm = object.getString("last_name");
                             String mail = object.getString("email");
                             String gender = object.getString("gender");
                             String fid = object.getString("id");*/
                                userclass.setTxt_fcbl_lgn_first_name(object.getString("first_name"));
                                userclass.setTxt_fcbk_lgn_last_name(object.getString("last_name"));
                                userclass.setTxt_fcbk_lgn_email(object.getString("email"));
                                userclass.setTxt_fcbk_lgn_gender(object.getString("gender"));
                                userclass.setTxt_fcbk_lgn_fcbkid(object.getString("id"));
                                // tvdetails.setText("Name: "+fnm+" "+lnm+" \n"+"Email: "+mail+" \n"+"Gender: "+gender+" \n"+"ID: "+fid+" \n"+"Birth Date: "+birthday);
                                // aQuery.id(ivpic).image("https://graph.facebook.com/" + fid + "/picture?type=large");
                                //https://graph.facebook.com/143990709444026/picture?type=large
                                //  Log.d("aswwww","https://graph.facebook.com/"+fid+"/picture?type=large");
                           /*  Intent i=new Intent(MainActivity.this,ActivitySecond.class);
                             startActivity(i);*/
                                registerFcbkUser();
                                Toast.makeText(getApplicationContext(), userclass.getTxt_fcbk_lgn_email(), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
            /*
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
            request.setParameters(parameters);
            */
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _txt_email = (EditText) findViewById(R.id.txt_email);
        _txt_password = (EditText) findViewById(R.id.txt_password);
        setCustomDesign();
        setCustomClickListeners();
        m_btn_login = (Button) findViewById(R.id.btn_login);
        m_btn_login.setOnClickListener(this);


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
        //  m_linearLayout_btnFb = (LinearLayout) findViewById(R.id.linearLayout_btnFb);
        loginButton = (LoginButton) findViewById(R.id.btnfb);

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
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, callback);
        //---------Facebook code ends inside onCreate()-------------
    }
    private void setCustomClickListeners() {
        ((TextView) findViewById(R.id.tv_forgot_pwd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        ((TextView) findViewById(R.id.tv_signUp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegnOneActivity.class);
                startActivity(intent);
            }
        });

       /* m_linearLayout_btnFb = (LinearLayout) findViewById(R.id.linearLayout_btnFb);
        m_linearLayout_btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(m_ctx, HomeActivity.class));
                LoginActivity.this.finish();
            }
        });*/

       /* m_btn_login = (Button) findViewById(R.id.btn_login);
        m_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(m_ctx, HomeActivity.class));
                LoginActivity.this.finish();
            }
        });*/
    }
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
        int item = v.getId();
        switch (item) {
            case R.id.btn_login:

                if (_txt_email.getText().toString().length() > 0 && _txt_password.getText().toString().length() > 0) {
                    userclass.setTxt_login_email_id(_txt_email.getText().toString());
                    userclass.setTxt_login_pwd(_txt_password.getText().toString());
                    login();
                } else if (_txt_email.getText().toString().length() == 0 && _txt_password.getText().toString().length() == 0) {
                    _txt_email.setError("Fields cann't be blank");
                    _txt_password.setError("Fields cann't be blank");
                } else if (_txt_email.getText().toString().length() == 0 && _txt_password.getText().toString().length() > 0)
                    _txt_email.setError("Fields cann't be blank");
                else if (_txt_email.getText().toString().length() > 0 && _txt_password.getText().toString().length() == 0)
                    _txt_password.setError("Fields cann't be blank");

                break;
        }
    }


    /*
     *Volley code for login
      */
    public void login() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_USER_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        JSONObject jobdata = job.getJSONObject("data");
                        userclass.setTxt_user_login_id(jobdata.getString("id"));
                        userclass.setTxt_user_access_token(jobdata.getString("accessToken"));
                        userclass.setTxt_temp_user_login_email(jobdata.getString("email"));
                        userclass.setTxt_fcbk_lgn_email(userclass.getTxt_temp_user_login_email());
                    } else
                        Toast.makeText(getApplicationContext(), "Incorrect email_id or password", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
              /*  params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL, email);*/
                params.put("email", userclass.getTxt_login_email_id());
                params.put("password", userclass.getTxt_login_pwd());
                params.put("device_id", "245");
                params.put("device_type", "Android");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
//----------Volley code for login ends------------

    //Volley code for registration...
    public void registerFcbkUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_REGISTER_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true"))
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    else {
                        Toast.makeText(getApplicationContext(), "Already registered,now edit your profile", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
              /*  params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL, email);*/
                params.put("first_name", userclass.getTxt_fcbl_lgn_first_name());
                params.put("last_name", userclass.getTxt_fcbk_lgn_last_name());
                params.put("email", userclass.getTxt_fcbk_lgn_email());
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
                params.put("device_id", "245");
                params.put("device_type", "Android");
                params.put("reg_type", "Facebook");
               /* params.put("first_name", "abc");
                params.put("last_name", "abg");
                params.put("email", "hag");
                params.put("country_id", "12");
                params.put("country_name", "sf");
                params.put("address1", "seefs");
                params.put("address2", "aef");
                params.put("city", "df");
                params.put("state_id", "234");
                params.put("state_name", "seef");
                params.put("phone", "234");
                params.put("church_name", "zdf");
                params.put("classes", "zdfaa");
                params.put("mission_trip", "sassa");
                params.put("mission_concept", "0");
                params.put("password", "srgsg");
                params.put("device_id", "245");
                params.put("device_type", "Android");
                params.put("reg_type", "Normal");*/
                return params;
            }

            ;

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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //-----------Volley code for registration ends------------------
}

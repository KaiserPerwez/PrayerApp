package com.wgt.myprayerapp.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.wgt.myprayerapp.R;
import com.wgt.myprayerapp.Utils.ValidatorUtils;
import com.wgt.myprayerapp.model.PostPrayerModelClass;
import com.wgt.myprayerapp.model.UserSingletonModelClass;
import com.wgt.myprayerapp.networking.UrlConstants;
import com.wgt.myprayerapp.networking.VolleyUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Kaiser on 25-07-2017.
 */

public class PostPrayerTextFrag extends Fragment implements View.OnClickListener, View.OnTouchListener {
    View rootView;
    TextView txt_overflow;
    ImageView img_overflow;
    int[] i = {0};
    UserSingletonModelClass _userSingletonModelClass = UserSingletonModelClass.get_userSingletonModelClass();
    PostPrayerModelClass postPrayerModelClass = new PostPrayerModelClass();
    EditText txtPrayer;
    ProgressDialog progressDialog;
    PopupMenu popup;
    private String receiver_email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_post_prayer_text, container, false);
        rootView.setOnTouchListener(this);//to detect touch on non-views
        setCustomDesign();

        txtPrayer = (EditText) rootView.findViewById(R.id.txtPrayer);

        RelativeLayout toggle_switch_rLayoutOuter = (RelativeLayout) rootView.findViewById(R.id.toggle_switch_rLayoutOuter);
        RelativeLayout toggle_switch_rLayoutInner = (RelativeLayout) rootView.findViewById(R.id.toggle_switch_rLayoutInner);
        TextView toggle_switch_text = (TextView) rootView.findViewById(R.id.toggle_switch_text);
        ImageButton toggle_switch_btn = (ImageButton) rootView.findViewById(R.id.toggle_switch_btn);
        toggleYesNo(i[0]++);
        toggle_switch_rLayoutOuter.setOnClickListener(this);
        toggle_switch_rLayoutInner.setOnClickListener(this);
        toggle_switch_text.setOnClickListener(this);
        toggle_switch_btn.setOnClickListener(this);
        toggle_switch_btn.setOnClickListener(this);

        txt_overflow = (TextView) rootView.findViewById(R.id.txt_overflow);
        txt_overflow.setOnClickListener(this);
        img_overflow = (ImageView) rootView.findViewById(R.id.img_overflow);
        img_overflow.setOnClickListener(this);
        Button btn_prayer = (Button) rootView.findViewById(R.id.btn_post_prayer);
        btn_prayer.setOnClickListener(this);
        postPrayerModelClass.setPost_priority("Medium");

        //Creating the instance of PopupMenu
        popup = new PopupMenu(rootView.getContext(), rootView.findViewById(R.id.img_overflow));
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.post_priority_menu, popup.getMenu());
        popup.getMenu().getItem(1).setChecked(true);
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                postPrayerModelClass.setPost_priority(item.getTitle().toString());
                item.setChecked(true);
                return true;
            }
        });
        return rootView;
    }

    private void setCustomDesign() {
    }

    private void toggleYesNo(int i) {
        final RelativeLayout toggle_switch_rLayout = (RelativeLayout) rootView.findViewById(R.id.toggle_switch_rLayoutInner);
        TextView tv_OR = (TextView) rootView.findViewById(R.id.tv_OR);
        LinearLayout linearLayout_btnFb = (LinearLayout) rootView.findViewById(R.id.linearLayout_btnFb);
        if (i % 2 == 0) {
            toggle_switch_rLayout.setGravity(Gravity.RIGHT | Gravity.CENTER);
            tv_OR.setVisibility(View.GONE);
            linearLayout_btnFb.setVisibility(View.GONE);
            postPrayerModelClass.setAccessibility("PRIVATE");
        } else {
            toggle_switch_rLayout.setGravity(Gravity.LEFT | Gravity.CENTER);
            tv_OR.setVisibility(View.VISIBLE);
            linearLayout_btnFb.setVisibility(View.VISIBLE);
            postPrayerModelClass.setAccessibility("PUBLIC");

        }
    }

    @Override
    public void onClick(View v) {
        hideSoftKeyboard();
        int item = v.getId();
        switch (item) {
            case R.id.toggle_switch_rLayoutOuter:
            case R.id.toggle_switch_rLayoutInner:
            case R.id.toggle_switch_text:
            case R.id.toggle_switch_btn:
                toggleYesNo(i[0]++);
                break;
            case R.id.txt_overflow:
            case R.id.img_overflow:
                popup.show();//showing popup menu
                break;
            case R.id.btn_post_prayer:
                if (txtPrayer.getText().length() <= 10) {
                    txtPrayer.setError("Minimum 10 characters required for your prayer description.");
                    return;
                } else {
                    LayoutInflater li = LayoutInflater.from(getContext());
                    final View promptsView = li.inflate(R.layout.verify_email_dialog, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());// set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);
                    // set dialog message
                    alertDialogBuilder.setCancelable(false);
                    final AlertDialog alertDialog = alertDialogBuilder.create();// create alert dialog
                    alertDialog.show();

                    final TextView txt_title = (TextView) promptsView.findViewById(R.id.tv_email_dialog_title);
                    txt_title.setText("Enter email id of Church Admin");
                    final EditText txt = (EditText) promptsView.findViewById(R.id.txt_otp);
                    txt.setHint("Enter Email");
                    txt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                    Button btn_verify = (Button) promptsView.findViewById(R.id.btn_verify);
                    btn_verify.setText("Submit");
                    btn_verify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            receiver_email = txt.getText().toString();
                            txt.setError(null);
                            if (receiver_email.length() == 0)
                                txt.setError("Email can't be empty");
                            else if (!ValidatorUtils.isValidEmail(receiver_email))
                                txt.setError("Email Format is invalid");
                            else {
                                alertDialog.cancel();
                                postTextPrayer();
                            }
                        }
                    });

                    Button btn_back = (Button) promptsView.findViewById(R.id.btn_back);
                    btn_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                        }
                    });
                }
                break;

        }
    }

    /*
       *Volley code for posting text prayer
        */
    public void postTextPrayer() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Posting Prayer...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_POST_TEXT_PRAYER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                        Toast.makeText(getActivity(), "Data posted successfully", Toast.LENGTH_LONG).show();
                        txtPrayer.setText("");
                    } else
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", _userSingletonModelClass.getTxt_user_login_id());
                params.put("sender_name", _userSingletonModelClass.getTxt_fname() + " " + _userSingletonModelClass.getTxt_lname());
                params.put("sender_email", _userSingletonModelClass.getTxt_email());
                params.put("receiver_email", receiver_email);
                params.put("post_content", txtPrayer.getText().toString());
                params.put("post_description", txtPrayer.getText().toString());
                params.put("accessibility", postPrayerModelClass.getAccessibility());
                params.put("post_type", "Text");
                params.put("post_priority", postPrayerModelClass.getPost_priority());
                params.put("sender_access_token", _userSingletonModelClass.getTxt_user_access_token());
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yy h:mm a");
                String formattedDate1 = df1.format(c.getTime());
                params.put("created_date", formattedDate1);


                return params;
            }
        };
        VolleyUtils.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    //----------Volley code for posting text prayer ends------------

    void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideSoftKeyboard();
        return false;
    }
}


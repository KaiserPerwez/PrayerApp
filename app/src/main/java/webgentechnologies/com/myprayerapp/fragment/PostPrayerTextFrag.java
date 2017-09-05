package webgentechnologies.com.myprayerapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.activity.HomeActivity;
import webgentechnologies.com.myprayerapp.model.PostPrayerModelClass;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;
import webgentechnologies.com.myprayerapp.networking.VolleyUtils;

/**
 * Created by Kaiser on 25-07-2017.
 */

public class PostPrayerTextFrag extends Fragment implements View.OnClickListener {
    View rootView;
    TextView txt_overflow;
    ImageView img_overflow;
    int[] i = {0};
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();
    PostPrayerModelClass postPrayerModelClass=new PostPrayerModelClass();
    EditText txtPrayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_post_prayer_text, container, false);
        setCustomDesign();
        // setCustomClickListeners();

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

        Log.d("TEXT DATA:",userclass.toString());

        return rootView;
    }

    private void setCustomDesign() {
    }

    private void setCustomClickListeners() {
        RelativeLayout toggle_switch_rLayoutOuter = (RelativeLayout) rootView.findViewById(R.id.toggle_switch_rLayoutOuter);
        RelativeLayout toggle_switch_rLayoutInner = (RelativeLayout) rootView.findViewById(R.id.toggle_switch_rLayoutInner);
        TextView toggle_switch_text = (TextView) rootView.findViewById(R.id.toggle_switch_text);
        ImageButton toggle_switch_btn = (ImageButton) rootView.findViewById(R.id.toggle_switch_btn);
        final int[] i = {0};
        toggleYesNo(i[0]++);
        toggle_switch_rLayoutOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleYesNo(i[0]++);
            }
        });
        toggle_switch_rLayoutInner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleYesNo(i[0]++);
            }
        });
        toggle_switch_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleYesNo(i[0]++);
            }
        });
        toggle_switch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleYesNo(i[0]++);
            }
        });
        toggle_switch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleYesNo(i[0]++);
            }
        });

        final TextView txt_overflow = (TextView) rootView.findViewById(R.id.txt_overflow);
        txt_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPriorityPopUp();
            }
        });
        final ImageView img_overflow = (ImageView) rootView.findViewById(R.id.img_overflow);
        img_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPriorityPopUp();
            }
        });
    }

    private void showPriorityPopUp() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(rootView.getContext(), rootView.findViewById(R.id.img_overflow));
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.post_priority_menu, popup.getMenu());
        popup.getMenu().getItem(2).setChecked(true);
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(rootView.getContext(), "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                postPrayerModelClass.setPost_priority(item.getTitle().toString());
                return true;
            }
        });


        popup.show();//showing popup menu
    }

    private void toggleYesNo(int i) {
        final RelativeLayout toggle_switch_rLayout = (RelativeLayout) rootView.findViewById(R.id.toggle_switch_rLayoutInner);
        TextView tv_OR = (TextView) rootView.findViewById(R.id.tv_OR);
        LinearLayout linearLayout_btnFb = (LinearLayout) rootView.findViewById(R.id.linearLayout_btnFb);
        if (i % 2 == 0) {
            toggle_switch_rLayout.setGravity(Gravity.RIGHT | Gravity.CENTER);
            tv_OR.setVisibility(View.GONE);
            linearLayout_btnFb.setVisibility(View.GONE);
            Toast.makeText(rootView.getContext(), "PRIVATE:" + i, Toast.LENGTH_SHORT).show();
            postPrayerModelClass.setAccessibility("PRIVATE");
        } else {
            toggle_switch_rLayout.setGravity(Gravity.LEFT | Gravity.CENTER);
            tv_OR.setVisibility(View.VISIBLE);
            linearLayout_btnFb.setVisibility(View.VISIBLE);
            Toast.makeText(rootView.getContext(), "PUBLIC:" + i, Toast.LENGTH_SHORT).show();
            postPrayerModelClass.setAccessibility("PUBLIC");

        }
    }

    @Override
    public void onClick(View v) {
        int item = v.getId();
        switch (item) {
            case R.id.toggle_switch_rLayoutOuter:
            case R.id.toggle_switch_rLayoutInner:
            case R.id.toggle_switch_text:
            case R.id.toggle_switch_btn:
                toggleYesNo(i[0]++);
                break;
            case R.id.txt_overflow:
                showPriorityPopUp();
                break;
            case R.id.img_overflow:
                showPriorityPopUp();
                break;
            case R.id.btn_post_prayer:
              //  userclass.setTxt_post_content_textfrag(txtPrayer.getText().toString());
               // userclass.setTxt_post_description_textfrag(txtPrayer.getText().toString());
                posttextprayer();
                break;

        }
    }

    /*
       *Volley code for posting text prayer
        */
    public void posttextprayer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_POST_TEXT_PRAYER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                        startActivity(new Intent(getActivity(), HomeActivity.class));
                        Toast.makeText(getActivity(), "Data posted successfully", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
              /*  params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL, email);*/
                params.put("user_id", userclass.getTxt_user_login_id());
                params.put("sender_name", userclass.getTxt_fname() + " " + userclass.getTxt_lname());
                params.put("sender_email", userclass.getTxt_email());
                params.put("receiver_email", "satabhisha.wgt@gmail.com");
                params.put("post_content",txtPrayer.getText().toString());
                params.put("post_description", txtPrayer.getText().toString());
                params.put("accessibility", postPrayerModelClass.getAccessibility());
                params.put("post_type", "Text");
                params.put("post_priority",postPrayerModelClass.getPost_priority());
                params.put("sender_access_token", userclass.getTxt_user_access_token());
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yy h:mm a");
                String formattedDate1 = df1.format(c.getTime());
                params.put("created_date", formattedDate1);


                return params;
            }
        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(stringRequest);
        VolleyUtils.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
//----------Volley code for posting text prayer ends------------
}


package com.wgt.myprayerapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.wgt.myprayerapp.R;
import com.wgt.myprayerapp.activity.HomeActivity;
import com.wgt.myprayerapp.adapters.ListViewPrayerListAdapter;
import com.wgt.myprayerapp.model.PostPrayerModelClass;
import com.wgt.myprayerapp.model.UserSingletonModelClass;
import com.wgt.myprayerapp.networking.UrlConstants;
import com.wgt.myprayerapp.networking.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kaiser on 28-07-2017.
 */

public class SearchPrayerFrag extends Fragment implements View.OnClickListener, View.OnTouchListener {
    View rootView;
    ProgressDialog progressDialog;
    UserSingletonModelClass _userSingletonModelClass;
    List<PostPrayerModelClass> _postPrayerModelClassList = new ArrayList<>();
    PopupMenu popup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_search_prayer, container, false);
        rootView.setOnTouchListener(this);//to detect touch on non-views
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final TextView txt_overflow = (TextView) rootView.findViewById(R.id.txt_overflow);
        txt_overflow.setOnClickListener(this);
        final ImageView img_overflow = (ImageView) rootView.findViewById(R.id.img_overflow);
        img_overflow.setOnClickListener(this);
        final FrameLayout frame_overflow = (FrameLayout) rootView.findViewById(R.id.frame_overflow);
        frame_overflow.setOnClickListener(this);

        //Creating the instance of PopupMenu
        popup = new PopupMenu(rootView.getContext(), rootView.findViewById(R.id.img_overflow));
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.sort_by_menu, popup.getMenu());
        popup.getMenu().getItem(0).setChecked(true);

        _userSingletonModelClass = UserSingletonModelClass.get_userSingletonModelClass();
        loadAllPrayersFromDatabase();

        final EditText txt_search = (EditText) rootView.findViewById(R.id.search_Prayer);
        txt_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (txt_search.getRight() - txt_search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        hideSoftKeyboard();
                        List<PostPrayerModelClass> postPrayerModelClassList = new ArrayList<PostPrayerModelClass>();
                        for (PostPrayerModelClass postPrayerModelClass :
                                _postPrayerModelClassList) {
                            if (postPrayerModelClass.getPost_description().toLowerCase().contains(txt_search.getText().toString().toLowerCase()))
                                postPrayerModelClassList.add(postPrayerModelClass);
                            if (postPrayerModelClass.getPost_type().toLowerCase().contains(txt_search.getText().toString().toLowerCase()))
                                postPrayerModelClassList.add(postPrayerModelClass);
//                            if (postPrayerModelClass.getAnswered_status().contains(txt_search.getText()))
//                                postPrayerModelClassList.add(postPrayerModelClass);
                            if (postPrayerModelClass.getPost_priority().toLowerCase().contains(txt_search.getText().toString().toLowerCase()))
                                postPrayerModelClassList.add(postPrayerModelClass);
                            if (postPrayerModelClass.getAccessibility().toLowerCase().contains(txt_search.getText().toString().toLowerCase()))
                                postPrayerModelClassList.add(postPrayerModelClass);
                        }
                        ListViewPrayerListAdapter listViewPrayerListAdapter = new ListViewPrayerListAdapter((HomeActivity) getActivity(), postPrayerModelClassList);
                        ListView lv_prayer_list = (ListView) rootView.findViewById(R.id.lv_prayer_list);
                        lv_prayer_list.setAdapter(listViewPrayerListAdapter);
                        lv_prayer_list.invalidateViews();

                        if (postPrayerModelClassList.size() == 0)
                            Toast.makeText(getContext(), "NO PRAYER FOUND", Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < popup.getMenu().size(); i++) {
                            popup.getMenu().getItem(i).setChecked(false);
                        }
                        popup.getMenu().clear();
                        popup.getMenuInflater().inflate(R.menu.sort_by_menu, popup.getMenu());
                        txt_search.clearFocus();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void loadAllPrayersFromDatabase() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Your Prayer list");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_VIEW_ALL_PRAYER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                        JSONArray data_arr = job.getJSONArray("data");
                        JSONObject data_post_object = null;
                        PostPrayerModelClass postPrayerModelClass = null;
                        for (int i = 0; i < data_arr.length(); i++) {
                            data_post_object = data_arr.getJSONObject(i);
                            postPrayerModelClass = new PostPrayerModelClass();
                            postPrayerModelClass.setId(data_post_object.getString("id"));
                            postPrayerModelClass.setUser_id(data_post_object.getString("user_id"));
                            postPrayerModelClass.setSender_name(data_post_object.getString("sender_name"));
                            postPrayerModelClass.setSender_email(data_post_object.getString("sender_email"));
                            postPrayerModelClass.setReceiver_email(data_post_object.getString("receiver_email"));
                            postPrayerModelClass.setPost_content(data_post_object.getString("post_content"));
                            postPrayerModelClass.setPost_description(data_post_object.getString("post_description"));
                            postPrayerModelClass.setAccessibility(data_post_object.getString("accessibility"));
                            postPrayerModelClass.setPost_type(data_post_object.getString("post_type"));
                            postPrayerModelClass.setPost_priority(data_post_object.getString("post_priority"));
                            postPrayerModelClass.setAnswered_status(data_post_object.getString("answered_status"));
                            postPrayerModelClass.setSender_access_token(data_post_object.getString("sender_access_token"));
                            try {
                                postPrayerModelClass.setUpdated_date(data_post_object.getString("updation_date"));
                            } catch (Exception e) {
                                postPrayerModelClass.setCreated_date(data_post_object.getString("created_date"));
                            }
                            String str = postPrayerModelClass.toString();
                            _postPrayerModelClassList.add(postPrayerModelClass);
                        }
                        loadAllPrayersIntoListView();
                    } else {
                        Toast.makeText(getContext(), "No Prayer Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity._context, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing())
                    progressDialog.cancel();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", _userSingletonModelClass.getTxt_user_login_id());
                params.put("sender_access_token", _userSingletonModelClass.getTxt_user_access_token());
                return params;
            }
        };
        VolleyUtils.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void loadAllPrayersIntoListView() {
        if (_postPrayerModelClassList.size() == 0) {
            Toast.makeText(getContext(), "NO PRAYER FOUND", Toast.LENGTH_SHORT).show();
            return;
        }

        List<PostPrayerModelClass> postPrayerModelClassList = new ArrayList<PostPrayerModelClass>();
        for (PostPrayerModelClass postPrayerModelClass :
                _postPrayerModelClassList) {
            if (postPrayerModelClass.getAnswered_status().equals("1"))
                postPrayerModelClassList.add(postPrayerModelClass);
        }
        if (postPrayerModelClassList.size() == 0) {
            Toast.makeText(getContext(), "NO ANSWERED PRAYER FOUND", Toast.LENGTH_SHORT).show();
            return;
        }

        ListViewPrayerListAdapter listViewPrayerListAdapter = new ListViewPrayerListAdapter((HomeActivity) getActivity(), postPrayerModelClassList);
        ListView lv_prayer_list = (ListView) rootView.findViewById(R.id.lv_prayer_list);
        lv_prayer_list.setAdapter(listViewPrayerListAdapter);


    }


    private void showSortPopUp() {
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(true);
                ((EditText) rootView.findViewById(R.id.search_Prayer)).setText("");
                List<PostPrayerModelClass> postPrayerModelClassList = new ArrayList<PostPrayerModelClass>();
                if (item.getTitle().equals("Answered")) {
                    for (PostPrayerModelClass postPrayerModelClass :
                            _postPrayerModelClassList) {
                        if (postPrayerModelClass.getAnswered_status().equals("1"))
                            postPrayerModelClassList.add(postPrayerModelClass);

                    }
                } else if (item.getTitle().equals("UnAnswered")) {
                    for (PostPrayerModelClass postPrayerModelClass :
                            _postPrayerModelClassList) {
                        if (postPrayerModelClass.getAnswered_status().equals("0"))
                            postPrayerModelClassList.add(postPrayerModelClass);
                    }
                }
// else if (item.getTitle().equals("Text")) {
//                    for (PostPrayerModelClass postPrayerModelClass :
//                            _postPrayerModelClassList) {
//                        if (postPrayerModelClass.getPost_type().toUpperCase().equals("TEXT"))
//                            postPrayerModelClassList.add(postPrayerModelClass);
//                    }
//                } else if (item.getTitle().equals("Audio")) {
//                    for (PostPrayerModelClass postPrayerModelClass :
//                            _postPrayerModelClassList) {
//                        if (postPrayerModelClass.getPost_type().toUpperCase().equals("AUDIO"))
//                            postPrayerModelClassList.add(postPrayerModelClass);
//                    }
//                } else if (item.getTitle().equals("Video")) {
//                    for (PostPrayerModelClass postPrayerModelClass :
//                            _postPrayerModelClassList) {
//                        if (postPrayerModelClass.getPost_type().toUpperCase().equals("VIDEO"))
//                            postPrayerModelClassList.add(postPrayerModelClass);
//                    }
//                }

                ListViewPrayerListAdapter listViewPrayerListAdapter = new ListViewPrayerListAdapter((HomeActivity) getActivity(), postPrayerModelClassList);
                ListView lv_prayer_list = (ListView) rootView.findViewById(R.id.lv_prayer_list);
                lv_prayer_list.setAdapter(listViewPrayerListAdapter);
                lv_prayer_list.invalidateViews();

                if (postPrayerModelClassList.size() == 0)
                    Toast.makeText(getContext(), "NO PRAYER FOUND", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();//showing popup menu
    }

    @Override
    public void onClick(View v) {
        hideSoftKeyboard();
        int id = v.getId();
        switch (id) {
            case R.id.txt_overflow:
            case R.id.img_overflow:
            case R.id.frame_overflow:
                showSortPopUp();
                break;

        }
    }

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

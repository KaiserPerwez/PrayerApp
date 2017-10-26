package webgentechnologies.com.myprayerapp.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.activity.HomeActivity;
import webgentechnologies.com.myprayerapp.model.PostPrayerModelClass;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;
import webgentechnologies.com.myprayerapp.networking.VolleyUtils;

import static webgentechnologies.com.myprayerapp.R.id.chk_answered;

/**
 * Created by Kaiser on 01-09-2017.
 */

public class ListViewPrayerListAdapter extends BaseAdapter {
    Activity activity;
    List<PostPrayerModelClass> list_prayers_posted;
    LayoutInflater inflater;

    public ListViewPrayerListAdapter(HomeActivity activity, List<PostPrayerModelClass> list_prayers_posted) {
        super();
        this.activity = activity;
        Collections.sort(list_prayers_posted, new Comparator<PostPrayerModelClass>() {
            @Override
            public int compare(PostPrayerModelClass p1, PostPrayerModelClass p2) {
                int diff = Integer.parseInt(p2.getId()) - Integer.parseInt(p1.getId());//descending order
                return diff;
            }
        });
        this.list_prayers_posted = list_prayers_posted;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return list_prayers_posted.size();
    }

    @Override
    //public PostPrayerModelClass getItem(int position) {
    public Object getItem(int position) {
        return list_prayers_posted.get(position);
    }

    @Override
    public long getItemId(int position) {
        //TODO:return prayer-post id here
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.search_prayer_list_item_overview, parent, false);

        final Holder holder = new Holder();
        holder.tv_timestamp = (TextView) convertView.findViewById(R.id.tv_post_timestamp);
        holder.post_type = (TextView) convertView.findViewById(R.id.post_type);
        holder.post_type_icon = (ImageView) convertView.findViewById(R.id.post_type_icon);
        holder.post_description = (TextView) convertView.findViewById(R.id.post_description);

        holder.post_content = (TextView) convertView.findViewById(R.id.post_content);
        holder.linearLayout_play = (LinearLayout) convertView.findViewById(R.id.linearLayout_play);
        holder.tv_play = (TextView) convertView.findViewById(R.id.tv_play);
        holder.chk_answered = (CheckBox) convertView.findViewById(chk_answered);
        holder.answered_date = (TextView) convertView.findViewById(R.id.answered_date);

        final PostPrayerModelClass postPrayerModelClass = list_prayers_posted.get(position);
        holder.postPrayerModelClass = postPrayerModelClass;

        holder.tv_timestamp.setText(postPrayerModelClass.getCreated_date());

//        if (postPrayerModelClass.getUpdated_date() != null && postPrayerModelClass.getUpdated_date().length() > 0)
//            holder.tv_timestamp.setText(postPrayerModelClass.getUpdated_date());
//        else

        final String post_type = postPrayerModelClass.getPost_type();
        holder.post_type.setText(post_type);
        if (post_type.toLowerCase().equals("audio")) {
            holder.post_type_icon.setImageResource(R.drawable.prayer_list_audio_icon);
            holder.linearLayout_play.setVisibility(View.VISIBLE);
            holder.tv_play.setText("PLAY AUDIO");
        } else if (post_type.toLowerCase().equals("video")) {
            holder.post_type_icon.setImageResource(R.drawable.prayer_list_video_icon);
            holder.linearLayout_play.setVisibility(View.VISIBLE);
            holder.tv_play.setText("PLAY VIDEO");
        } else if (post_type.toLowerCase().equals("text")) {
            holder.post_type_icon.setImageResource(R.drawable.prayer_list_text_icon);
            holder.linearLayout_play.setVisibility(View.GONE);
        } else
            holder.post_type_icon.setImageResource(R.drawable.cross);

        holder.post_description.setText(postPrayerModelClass.getPost_description());
        holder.post_content.setText(postPrayerModelClass.getPost_content());
        holder.post_content.setVisibility(View.GONE);

        if (postPrayerModelClass.getAnswered_status().equals("1")) {
            holder.chk_answered.setChecked(true);
            holder.chk_answered.setEnabled(false);
            holder.answered_date.setText(postPrayerModelClass.getUpdated_date());
        } else {
            holder.chk_answered.setChecked(false);
        }

        ImageView img_overflow_details = (ImageView) convertView.findViewById(R.id.img_overflow_details);
        final View finalConvertView = convertView;
        final int[] details_visible = {0};
        img_overflow_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView(finalConvertView, ++details_visible[0]);
            }
        });

        FrameLayout frameLayout_overflow_details = (FrameLayout) convertView.findViewById(R.id.frameLayout_overflow_details);
        frameLayout_overflow_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleView(finalConvertView, ++details_visible[0]);
            }
        });

        holder.linearLayout_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                if (postPrayerModelClass.getPost_type().toUpperCase().equals("AUDIO"))
                    i.setDataAndType(Uri.parse(holder.post_content.getText().toString()), "audio/*");
                else if (postPrayerModelClass.getPost_type().toUpperCase().equals("VIDEO"))
                    i.setDataAndType(Uri.parse(holder.post_content.getText().toString()), "video/*");
                HomeActivity._context.startActivity(i);
            }
        });
        holder.tv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                if (postPrayerModelClass.getPost_type().toUpperCase().equals("AUDIO"))
                    i.setDataAndType(Uri.parse(holder.post_content.getText().toString()), "audio/*");
                else if (postPrayerModelClass.getPost_type().toUpperCase().equals("VIDEO"))
                    i.setDataAndType(Uri.parse(holder.post_content.getText().toString()), "video/*");
                HomeActivity._context.startActivity(i);
            }
        });
        holder.chk_answered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call to update
                holder.chk_answered.setChecked(false);
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(HomeActivity._context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(HomeActivity._context);
                }
                builder.setTitle("Alert")
                        .setMessage("Are you sure the prayer has been answered?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                updateAnsweredPrayer(holder.postPrayerModelClass);
                                Toast.makeText(HomeActivity._context, "Confirmed", Toast.LENGTH_SHORT).show();
                                holder.chk_answered.setChecked(true);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(HomeActivity._context, "Dismissed", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                holder.chk_answered.setChecked(false);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        return convertView;
    }

    void toggleView(View finalConvertView, int n) {
        View layout_prayer_details = (View) finalConvertView.findViewById(R.id.layout_prayer_details);
        LinearLayout linearLayout_searchPrayerOverview = (LinearLayout) finalConvertView.findViewById(R.id.linearLayout_searchPrayerOverview);
        AbsListView.LayoutParams lp = (AbsListView.LayoutParams) linearLayout_searchPrayerOverview.getLayoutParams();

        lp.height = (n % 2 == 0) ? 160 : ViewGroup.LayoutParams.WRAP_CONTENT;
        if (layout_prayer_details.getVisibility() == View.GONE)
            layout_prayer_details.setVisibility(View.VISIBLE);
        else
            layout_prayer_details.setVisibility(View.GONE);
    }

    public void updateAnsweredPrayer(final PostPrayerModelClass postPrayerModelClass) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstants._URL_UPDATE_PRAYER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job = new JSONObject(response);
                    String status = job.getString("status");

                    if (status.equals("true")) {
                        Toast.makeText(HomeActivity._context, "Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HomeActivity._context, HomeActivity.class);
                        intent.putExtra("choice_id", "R.id.nav_searchPrayer");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        HomeActivity._context.startActivity(intent);
                        //((Activity)(HomeActivity._context)).finish();
                    } else {
                        Toast.makeText(HomeActivity._context, "Updation failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity._context, "Exception:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity._context, "Error:" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", postPrayerModelClass.getUser_id());
                params.put("prayer_id", postPrayerModelClass.getId());
                params.put("sender_access_token", postPrayerModelClass.getSender_access_token());

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yy h:mm a");
                String formattedDate1 = df1.format(c.getTime());
                params.put("updated_date", formattedDate1);

                return params;
            }
        };
        VolleyUtils.getInstance(HomeActivity._context).addToRequestQueue(stringRequest);
    }

    class Holder {
        TextView tv_timestamp;
        TextView post_type;
        ImageView post_type_icon;
        TextView post_description;

        TextView post_content;
        LinearLayout linearLayout_play;
        TextView tv_play;
        CheckBox chk_answered;
        TextView answered_date;
        PostPrayerModelClass postPrayerModelClass;
    }
}

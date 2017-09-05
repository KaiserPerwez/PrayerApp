package webgentechnologies.com.myprayerapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import webgentechnologies.com.myprayerapp.R;

/**
 * Created by Kaiser on 25-07-2017.
 */

public class PostPrayerVideoFrag extends Fragment {
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootView = inflater.inflate(R.layout.frag_post_prayer_video, container, false);
        setCustomDesign();
        setCustomClickListeners();
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

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(rootView.getContext(), "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
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
        } else {
            toggle_switch_rLayout.setGravity(Gravity.LEFT | Gravity.CENTER);
            tv_OR.setVisibility(View.VISIBLE);
            linearLayout_btnFb.setVisibility(View.VISIBLE);
            Toast.makeText(rootView.getContext(), "PUBLIC:" + i, Toast.LENGTH_SHORT).show();
        }
    }
}


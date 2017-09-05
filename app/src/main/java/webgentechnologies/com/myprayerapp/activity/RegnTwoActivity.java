package webgentechnologies.com.myprayerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;


public class RegnTwoActivity extends AppCompatActivity implements View.OnClickListener {
    Context m_ctx;
    CheckBox chk_alpha, chk_perspective, chk_men, chk_beth_more, chk_cbs, chk_others;
    EditText txt_others, txt_churchname;
    static String others;
    SharedPreferences sharedpreferences;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regn_two);
        chk_alpha = (CheckBox) findViewById(R.id.chk_alpha);
        chk_perspective = (CheckBox) findViewById(R.id.chk_perspective);
        chk_men = (CheckBox) findViewById(R.id.chk_men);
        chk_beth_more = (CheckBox) findViewById(R.id.chk_beth_more);
        chk_cbs = (CheckBox) findViewById(R.id.chk_cbs);
        // chk_others=(CheckBox)findViewById(R.id.chk_others);
        txt_others = (EditText) findViewById(R.id.txt_others);
        txt_churchname = (EditText) findViewById(R.id.txt_church_name);
        chk_alpha.setOnClickListener(this);
        chk_perspective.setOnClickListener(this);
        chk_men.setOnClickListener(this);
        chk_beth_more.setOnClickListener(this);
        chk_cbs.setOnClickListener(this);
        //Calling sharedpreference to set churchname value...
       /* sharedpreferences = getSharedPreferences(sharedpreferenceName, Context.MODE_PRIVATE);
        String sharedpreferencesString = sharedpreferences.getString(sharedpreference_key_churchname, null);*/
        txt_churchname.setText(userclass.getChurch_name());

        setCustomDesign();
        //setCustomClickListeners();
        chk_others = (CheckBox) findViewById(R.id.chk_others);
        chk_others.setOnClickListener(this);
        FrameLayout imageButtonNext = (FrameLayout) findViewById(R.id.imageButtonNext);
        imageButtonNext.setOnClickListener(this);
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(this);
    }

   /* private void setCustomClickListeners() {
        final CheckBox chk_others = (CheckBox) findViewById(R.id.chk_others);
        chk_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.layout_txt_others);
                if (chk_others.isChecked()) {
                    layout.setVisibility(View.VISIBLE);
                    userclass.setTxt_others(txt_others.getText().toString());
                } else
                    layout.setVisibility(View.GONE);

            }
        });

        FrameLayout imageButtonNext = (FrameLayout) findViewById(R.id.imageButtonNext);
        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegnTwoActivity.this, RegnThreeActivity.class);
                startActivity(intent);
            }
        });
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }*/

    private void setCustomDesign() {
        m_ctx = RegnTwoActivity.this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        ((TextView) findViewById(R.id.tv_regn_two)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_regn_step2)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_church_name)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.tv_header_class)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_alpha)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_perspective)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_men)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_beth_more)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_cbs)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.chk_others)).setTypeface(regular_font);


    }

    List list = new ArrayList();

    @Override
    public void onClick(View v) {
        int item = v.getId();
        switch (item) {
            case R.id.chk_others:
                LinearLayout layout = (LinearLayout) findViewById(R.id.layout_txt_others);
                if (chk_others.isChecked()) {
                    layout.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.GONE);
                }
                break;
            case R.id.imageButtonNext:
                List list = new ArrayList();
                if (chk_alpha.isChecked())
                    list.add("Alpha");
                if (chk_perspective.isChecked())
                    list.add("Perspective");
                if (chk_men.isChecked())
                    list.add("Men's Fraternity");
                if (chk_beth_more.isChecked())
                    list.add("Beth More");
                if (chk_cbs.isChecked())
                    list.add("CBS");
                if(chk_others.isChecked())
                {
                    list.add("OTHER");
                    list.add(txt_others.getText().toString());
                }
                if(list.size()==0)
                {
                    Toast.makeText(this,"Please select at least one class",Toast.LENGTH_LONG).show();
                    return;
                }
                if(list.contains("OTHER")&&(txt_others.getText().toString().length()<=1))
                {
                    Toast.makeText(this,"Please Enter a valid class-name for \"OTHERS\" category",Toast.LENGTH_LONG).show();
                    return;
                }
                userclass.setList_classes_attended(list);
                userclass.setChurch_name(txt_churchname.getText().toString());
                String user=userclass.toString();
               // Toast.makeText(this,userclass.toString(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegnTwoActivity.this, RegnThreeActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonPrev:
                finish();
                break;
        }
    }
}

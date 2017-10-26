package webgentechnologies.com.myprayerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
    Context _ctx;
    CheckBox chk_alpha, chk_perspective, chk_men, chk_beth_more, chk_cbs, chk_others;
    EditText txt_others, txt_churchname;
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

        txt_others = (EditText) findViewById(R.id.txt_others);
        txt_churchname = (EditText) findViewById(R.id.txt_church_name);
        chk_alpha.setOnClickListener(this);
        chk_perspective.setOnClickListener(this);
        chk_men.setOnClickListener(this);
        chk_beth_more.setOnClickListener(this);
        chk_cbs.setOnClickListener(this);

        txt_churchname.setText(userclass.getChurch_name());

        setCustomDesign();
        chk_others = (CheckBox) findViewById(R.id.chk_others);
        chk_others.setOnClickListener(this);
        FrameLayout imageButtonNext = (FrameLayout) findViewById(R.id.imageButtonNext);
        imageButtonNext.setOnClickListener(this);
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(this);
    }

    private void setCustomDesign() {
        _ctx = RegnTwoActivity.this;
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

    @Override
    public void onClick(View v) {
        hideSoftKeyboard();
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
                List<String> list = new ArrayList();
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
                Intent intent = new Intent(RegnTwoActivity.this, RegnThreeActivity.class);
                startActivity(intent);
                break;
            case R.id.imageButtonPrev:
                finish();
                break;
        }
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

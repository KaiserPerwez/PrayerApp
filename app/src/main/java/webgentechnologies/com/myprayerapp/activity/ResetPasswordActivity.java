package webgentechnologies.com.myprayerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import webgentechnologies.com.myprayerapp.R;

public class ResetPasswordActivity extends AppCompatActivity {
    Button m_btn_resetPwd;
    String m_verify_mode;
    Context m_ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        setCustomDesign();
        setCustomClickListeners();
    }

    private void setCustomDesign() {
        m_ctx = ResetPasswordActivity.this;
        m_verify_mode = this.getIntent().getStringExtra("verify_mode");

        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        ((TextView) findViewById(R.id.tv_resetPwd)).setTypeface(regular_font);
        if (m_verify_mode.equals("change_pwd")) {
            ((TextView) findViewById(R.id.tv_resetPwd)).setText("Change Password");
            ((TextView) findViewById(R.id.btn_resetPwd)).setText("Change Password");
        } else {
            ((TextView) findViewById(R.id.tv_resetPwd)).setText("Reset Password");
            ((TextView) findViewById(R.id.btn_resetPwd)).setText("Reset Password");
        }


        ((TextView) findViewById(R.id.txt_resetPwd)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.txt_resetPwd_retype)).setTypeface(regular_font);
        ((TextView) findViewById(R.id.btn_resetPwd)).setTypeface(regular_font);

    }

    private void setCustomClickListeners() {
        FrameLayout imageButtonPrev = (FrameLayout) findViewById(R.id.imageButtonPrev);
        imageButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m_verify_mode.equals("change_pwd")) {
                    finish();
                } else {
                    Intent intent = new Intent(m_ctx, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        m_btn_resetPwd = (Button) findViewById(R.id.btn_resetPwd);
        m_btn_resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m_verify_mode.equals("change_pwd")) {
                    Intent intent = new Intent(m_ctx, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(m_ctx, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}

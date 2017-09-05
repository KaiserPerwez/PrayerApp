package webgentechnologies.com.myprayerapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import webgentechnologies.com.myprayerapp.R;

public class SplashActivity extends AppCompatActivity {
    Context m_ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        m_ctx = SplashActivity.this;
        checkCompatibility();
    }

    private void checkCompatibility() {
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
        if (diagonalInches >= 6.5) {
            // 6.5inch device or bigger
            Toast.makeText(m_ctx, "Tab Not supported", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(m_ctx);// set prompts.xml to alertdialog builder
            alertDialogBuilder.setTitle("Alert");
            alertDialogBuilder.setMessage("App not supported on Tablets. Please contact the developer.");
            // set dialog message
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("I Got it. Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    SplashActivity.this.finish();
                }
            });
            final AlertDialog alertDialog = alertDialogBuilder.create();// create alert dialog
            alertDialog.show();
        } else {

            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
    }


}

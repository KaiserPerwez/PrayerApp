package webgentechnologies.com.myprayerapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.activity.ResetPasswordActivity;

public class ChangePasswordFrag extends Fragment {

    View rootView;
    Context m_ctx;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_change_password, container, false);
        setCustomDesign();
        setCustomClickListeners();
        return rootView;
    }

    private void setCustomDesign() {
        m_ctx = rootView.getContext();
        Typeface regular_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface semiBold_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-SemiBold.ttf");

        ((TextView) rootView.findViewById(R.id.tv_forgotPwd)).setTypeface(regular_font);
        ((TextView) rootView.findViewById(R.id.tv_usermail)).setTypeface(semiBold_font);
        ((TextView) rootView.findViewById(R.id.btn_getOtp)).setTypeface(semiBold_font);
    }

    private void setCustomClickListeners() {
        Button btn_getOtp = (Button) rootView.findViewById(R.id.btn_getOtp);
        btn_getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(m_ctx);
                View promptsView = li.inflate(R.layout.verify_email_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        m_ctx);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false);
                // create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();

                Button btn_verify = (Button) promptsView.findViewById(R.id.btn_verify);
                Button btn_back = (Button) promptsView.findViewById(R.id.btn_back);
                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                btn_verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        Intent intent = new Intent(m_ctx, ResetPasswordActivity.class);
                        intent.putExtra("verify_mode", "change_pwd");
                        startActivity(intent);
                    }
                });
            }
        });
    }
}

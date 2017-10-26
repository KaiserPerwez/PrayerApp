package webgentechnologies.com.myprayerapp.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Kaiser on 15-09-2017.
 */

public class CustomUtils {
    public static void alert(Context _ctx, String title, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_ctx);// set prompts.xml to alertdialog builder
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();// create alert dialog
        alertDialog.show();

    }
}

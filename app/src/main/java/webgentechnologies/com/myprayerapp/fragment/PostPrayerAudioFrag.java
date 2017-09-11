package webgentechnologies.com.myprayerapp.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import webgentechnologies.com.myprayerapp.R;
import webgentechnologies.com.myprayerapp.Utils.FileUtils;
import webgentechnologies.com.myprayerapp.Utils.ValidatorUtils;
import webgentechnologies.com.myprayerapp.model.PostPrayerModelClass;
import webgentechnologies.com.myprayerapp.model.UserSingletonModelClass;
import webgentechnologies.com.myprayerapp.networking.AndroidMultiPartEntity;
import webgentechnologies.com.myprayerapp.networking.UrlConstants;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;

/**
 * Created by Kaiser on 25-07-2017.
 */

public class PostPrayerAudioFrag extends Fragment implements View.OnClickListener {
    public static final int RequestPermissionCode = 1;
    View rootView;
    ImageView audio_record;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    UserSingletonModelClass userclass = UserSingletonModelClass.get_userSingletonModelClass();
    FileUtils fileUtils = new FileUtils();
    PostPrayerModelClass postPrayerModelClass = new PostPrayerModelClass();
    TextView txt_overflow;
    ImageView img_overflow;
    int[] i = {0};
    EditText txt_Prayer;
    Button btn_post_prayer;
    long totalSize = 0;
    String receiver_email;

    TextView audio_timer;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    boolean recording_status = false;
    private ProgressDialog progressDialog;
    private Uri fileUri; // file url to store image/video
    private long startHTime = 0L;
    private Handler customHandler = new Handler();
    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startHTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            if (audio_timer != null)
                audio_timer.setText("" + String.format("%02d", mins) + "m:"
                        + String.format("%02d", secs) + "s");
            customHandler.postDelayed(this, 0);
        }

    };

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                FileUtils.FILE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
              /*  Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");*/
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
       /* if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } */
        if (type == MEDIA_TYPE_AUDIO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "AUDIO_" + timeStamp + ".mp3");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frag_post_prayer_audio, container, false);
        txt_Prayer = (EditText) rootView.findViewById(R.id.txt_Prayer);
        audio_timer = (TextView) rootView.findViewById(R.id.audio_timer);
        audio_record = (ImageView) rootView.findViewById(R.id.audio_record);
        audio_record.setOnClickListener(this);

        progressDialog = new ProgressDialog(getContext(), ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);

        setCustomDesign();
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
        btn_post_prayer = (Button) rootView.findViewById(R.id.btn_post_prayer);
        btn_post_prayer.setOnClickListener(this);
        postPrayerModelClass.setPost_priority("Medium");
        return rootView;
    }

    private void resetTimer() {
        startHTime = 0L;
        customHandler = new Handler();
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedTime = 0L;
    }

    private void setCustomDesign() {
    }

    private void showPriorityPopUp() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(rootView.getContext(), rootView.findViewById(R.id.img_overflow));
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.post_priority_menu, popup.getMenu());
        popup.getMenu().getItem(1).setChecked(true);
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
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
            postPrayerModelClass.setAccessibility("PRIVATE");

        } else {
            toggle_switch_rLayout.setGravity(Gravity.LEFT | Gravity.CENTER);
            tv_OR.setVisibility(View.VISIBLE);
            linearLayout_btnFb.setVisibility(View.VISIBLE);
            postPrayerModelClass.setAccessibility("PUBLIC");

        }
    }

    //------------------Code for audio recording----------------
    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(fileUri.getPath());
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getActivity(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        Toast.makeText(getContext(), "Checking record permission", Toast.LENGTH_SHORT).show();
        int result = ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onClick(View v) {
        hideSoftKeyboard();
        int item = v.getId();
        switch (item) {
            case R.id.audio_record:
                if (!recording_status) {
                    if (checkPermission()) {
                        Toast.makeText(getContext(), "Record permission Granted.", Toast.LENGTH_SHORT).show();
                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_AUDIO);
                        fileUtils.setAudio_filepath(fileUri.getPath());
                        MediaRecorderReady();
                        try {
                            audio_record.setBackgroundResource(R.drawable.red_button);
                            resetTimer();
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                            recording_status = true;
                            startHTime = SystemClock.uptimeMillis();
                            customHandler.postDelayed(updateTimerThread, 0);
                            Toast.makeText(getContext(), "Recording...", Toast.LENGTH_SHORT).show();
                            btn_post_prayer.setEnabled(false);
                        } catch (IllegalStateException e) {
                            btn_post_prayer.setEnabled(true);
                            Toast.makeText(getContext(), "Err:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } catch (IOException e) {
                            btn_post_prayer.setEnabled(true);
                            Toast.makeText(getContext(), "Err:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        btn_post_prayer.setEnabled(true);
                        Toast.makeText(getContext(), "Record permission failed.Please try again.", Toast.LENGTH_LONG).show();
                        requestPermission();
                    }
                } else {
                    try {
                        mediaRecorder.stop();
                        recording_status = false;
                        audio_record.setBackgroundResource(R.drawable.mic);
                        timeSwapBuff += timeInMilliseconds;
                        customHandler.removeCallbacks(updateTimerThread);
                        Toast.makeText(getActivity(), "Recording Completed", Toast.LENGTH_LONG).show();
                        btn_post_prayer.setEnabled(true);
                    } catch (Exception e) {
                        btn_post_prayer.setEnabled(true);
                        Toast.makeText(getContext(), "Couldn't stop.Please try stopping again. Err:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }


                break;
            case R.id.toggle_switch_rLayoutOuter:
            case R.id.toggle_switch_rLayoutInner:
            case R.id.toggle_switch_text:
            case R.id.toggle_switch_btn:
                toggleYesNo(i[0]++);
                break;
            case R.id.txt_overflow:
            case R.id.img_overflow:
                showPriorityPopUp();
                break;
            case R.id.btn_post_prayer:
                if (txt_Prayer.getText().length() < 10) {
                    txt_Prayer.setError("Minimum 10 characters required for your prayer description.");
                    return;
                } else {
                    LayoutInflater li = LayoutInflater.from(getContext());
                    final View promptsView = li.inflate(R.layout.verify_email_dialog, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());// set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);
                    // set dialog message
                    alertDialogBuilder.setCancelable(false);
                    final AlertDialog alertDialog = alertDialogBuilder.create();// create alert dialog
                    alertDialog.show();

                    final TextView txt_title = (TextView) promptsView.findViewById(R.id.tv_email_dialog_title);
                    txt_title.setText("Enter email id of Church Admin");
                    txt_title.setTextSize(15);
                    final EditText txt = (EditText) promptsView.findViewById(R.id.txt_otp);
                    txt.setHint("Enter Email");
                    txt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                    Button btn_verify = (Button) promptsView.findViewById(R.id.btn_verify);
                    btn_verify.setText("Submit");
                    btn_verify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            receiver_email = txt.getText().toString();
                            txt.setError(null);

                            if (receiver_email.length() == 0)
                                txt.setError("Email can't be empty");
                            else if (!ValidatorUtils.isValidEmail(receiver_email))
                                txt.setError("Email Format is invalid");
                            else {
                                alertDialog.cancel();
                                new UploadAudioFileToServer().execute();
                            }
                        }
                    });

                    Button btn_back = (Button) promptsView.findViewById(R.id.btn_back);
                    btn_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                        }
                    });
                }
                break;

        }
    }

    /**
     * Method to show alert dialog
     */

    //------------------Json upload code ends------------------
    void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Creating file uri to store image/video
     */
    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Uploading the file to server
     */
    private class UploadAudioFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            if (progressDialog != null)
                progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(UrlConstants._URL_POST_AUDIO_PRAYER);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                entity.addPart("user_id", new StringBody(userclass.getTxt_user_login_id()));
                entity.addPart("sender_name", new StringBody(userclass.getTxt_fname() + " " + userclass.getTxt_lname()));
                entity.addPart("sender_email", new StringBody(userclass.getTxt_email()));
                entity.addPart("receiver_email", new StringBody(receiver_email));
                File sourceFile = new File(fileUtils.getAudio_filepath());

                // Adding file data to http body
                entity.addPart("audiofile", new FileBody(sourceFile));

                entity.addPart("post_description", new StringBody(txt_Prayer.getText().toString()));
                entity.addPart("accessibility", new StringBody(postPrayerModelClass.getAccessibility()));
                entity.addPart("post_type", new StringBody("Audio"));
                entity.addPart("post_priority", new StringBody(postPrayerModelClass.getPost_priority()));
                entity.addPart("user_access_token", new StringBody(userclass.getTxt_user_access_token()));
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yy h:mm a");
                String formattedDate1 = df1.format(c.getTime());
                entity.addPart("created_date", new StringBody(formattedDate1));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = "Err:" + e.toString();
            } catch (IOException e) {
                responseString = "Err:" + e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.cancel();
            if (result.startsWith("Err"))
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getActivity(), "Upload Completed", Toast.LENGTH_LONG).show();

            audio_timer.setText("");
            txt_Prayer.setText("");
            super.onPostExecute(result);
        }

    }

}



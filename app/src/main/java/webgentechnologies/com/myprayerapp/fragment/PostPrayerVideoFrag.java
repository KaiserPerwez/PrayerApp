package webgentechnologies.com.myprayerapp.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
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
import org.w3c.dom.Text;

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

/**
 * Created by Kaiser on 25-07-2017.
 */

public class PostPrayerVideoFrag extends Fragment implements View.OnClickListener {
    public static final int MEDIA_TYPE_VIDEO = 2;
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    static String filepath = null;
    View rootView;
    ImageView img_record_video;
    TextView txt_overflow;
    ImageView img_overflow;
    int[] i = {0};
    EditText txtPrayer;
    long totalSize = 0;
    Button btn_prayer;
    String receiver_email;
    ProgressDialog progressDialog;
    UserSingletonModelClass _userSingletonModelClass = UserSingletonModelClass.get_userSingletonModelClass();
    PostPrayerModelClass postPrayerModelClass = new PostPrayerModelClass();
    FileUtils fileUtils = new FileUtils();
    private Uri fileUri; // file url to store image/video

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
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
        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_post_prayer_video, container, false);
        img_record_video = (ImageView) rootView.findViewById(R.id.img_record_video);
        img_record_video.setOnClickListener(this);
        setCustomDesign();
        progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);

        txtPrayer = (EditText) rootView.findViewById(R.id.txt_Prayer);

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
        btn_prayer = (Button) rootView.findViewById(R.id.btn_post_prayer);
        btn_prayer.setOnClickListener(this);
        postPrayerModelClass.setPost_priority("Medium");

        return rootView;
    }

    private void setCustomDesign() {
    }

    private void showPriorityPopUp() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(rootView.getContext(), rootView.findViewById(R.id.img_overflow));
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.post_priority_menu, popup.getMenu());

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
    //----Checking device camera code ends--------

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    //-------Launching camera app to record video ends---------

    /**
     * Launching camera app to record video
     */
    private void recordVideo() {

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        filepath=fileUri.getPath();
        fileUtils.setVideo_filepath(filepath);

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the video file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            fileUri = savedInstanceState.getParcelable("file_uri");
            filepath = fileUri.getPath();
        }

    }

    //-----------Receiving activity result method will be called after closing the camera ends----------------------
    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Receiving activity result method will be called after closing the camera
     * *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {

                // video successfully recorded
                Toast.makeText(getActivity(),
                        "Video successfully recorded", Toast.LENGTH_SHORT)
                        .show();
                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filepath,
                        MediaStore.Images.Thumbnails.MINI_KIND);
                ImageView img_record_video_preview = (ImageView) rootView.findViewById(R.id.img_record_video_preview);
                img_record_video_preview.setImageBitmap(thumbnail);

            } else if (resultCode == getActivity().RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getActivity(),"User cancelled video recording", Toast.LENGTH_SHORT).show();
                filepath=null;

            } else {
                // failed to record video
                Toast.makeText(getActivity(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * Creating file uri to store image/video
     */
    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @Override
    public void onClick(View view) {
        hideSoftKeyboard();
        int item = view.getId();
        switch (item) {
            case R.id.toggle_switch_rLayoutOuter:
            case R.id.toggle_switch_rLayoutInner:
            case R.id.toggle_switch_text:
            case R.id.toggle_switch_btn:
                toggleYesNo(i[0]++);
                break;
            case R.id.txt_overflow:
                showPriorityPopUp();
                break;
            case R.id.img_overflow:
                showPriorityPopUp();
                break;
            case R.id.img_record_video:
                if (!isDeviceSupportCamera()) {
                    Toast.makeText(getActivity(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getActivity(), "Opening Camera", Toast.LENGTH_LONG).show();
                recordVideo();
                break;
            case R.id.btn_post_prayer:
                File sourceFile = null;
                try {
                    sourceFile= new File(fileUtils.getVideo_filepath());
                }
                catch (Exception e){
                    //Toast.makeText(getContext(), ""+e.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(), "Err:Please record a video before Uploading", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(filepath==null || sourceFile.length()==0){
                    Toast.makeText(getContext(), "Please record a video before Uploading", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (txtPrayer.getText().length() <= 10) {
                    txtPrayer.requestFocus();
                    txtPrayer.setError("Minimum 10 characters required for your prayer description.");
                    return;
                } else {
                    txtPrayer.setError(null);
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
                                new UploadFileToServer().execute();
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

    void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus()) ? null : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Uploading the file to server
     */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            if (progressDialog != null)
                progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            //  progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            //  progressBar.setProgress(progress[0]);

            // updating percentage value
            // txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(UrlConstants._URL_POST_VIDEO_PRAYER);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                entity.addPart("user_id", new StringBody(_userSingletonModelClass.getTxt_user_login_id()));
                entity.addPart("sender_name", new StringBody(_userSingletonModelClass.getTxt_fname() + " " + _userSingletonModelClass.getTxt_lname()));
                entity.addPart("sender_email", new StringBody(_userSingletonModelClass.getTxt_email()));
                entity.addPart("receiver_email", new StringBody(receiver_email));
                File sourceFile = new File(fileUtils.getVideo_filepath());

                // Adding file data to http body
                entity.addPart("file", new FileBody(sourceFile));

                entity.addPart("post_description", new StringBody(txtPrayer.getText().toString()));
                entity.addPart("accessibility", new StringBody(postPrayerModelClass.getAccessibility()));
                entity.addPart("post_type", new StringBody("Video"));
                entity.addPart("post_priority", new StringBody(postPrayerModelClass.getPost_priority()));
                entity.addPart("user_access_token", new StringBody(_userSingletonModelClass.getTxt_user_access_token()));
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df1 = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate1 = df1.format(c.getTime());
                entity.addPart("created_date", new StringBody(formattedDate1));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
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

            txtPrayer.setText("");
            super.onPostExecute(result);
        }

    }
}




package webgentechnologies.com.myprayerapp.Utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Satabhisha on 28-08-2017.
 */

public class FileUtils {
    public static final String FILE_DIRECTORY_NAME = "AndroidFileUpload";
    String video_filepath;
    String audio_filepath;

    //--------------------------------Getter---------------------------------------

    public static String getMediaStorageDir() {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                FileUtils.FILE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return (String) Environment.DIRECTORY_PICTURES + File.separator + FileUtils.FILE_DIRECTORY_NAME;
    }

    public String getVideo_filepath() {
        return video_filepath;
    }
    //--------------------------------Setter----------------------------------------

    public void setVideo_filepath(String video_filepath) {
        this.video_filepath = video_filepath;
    }

    public String getAudio_filepath() {
        return audio_filepath;
    }

    public void setAudio_filepath(String audio_filepath) {
        this.audio_filepath = audio_filepath;
    }
}

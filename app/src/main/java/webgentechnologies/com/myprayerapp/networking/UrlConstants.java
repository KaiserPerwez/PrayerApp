package webgentechnologies.com.myprayerapp.networking;

/**
 * Created by Satabhisha on 17-08-2017.
 */

public class UrlConstants {
    public static String _BASE_URL="http://50.62.31.191/~prayerapp/api/public/index.php/api/";

    public static String _URL_CHURCH_LIST = _BASE_URL + "allchurches";
    public static String _URL_COUNTRY_LIST = _BASE_URL + "getCountry";
    public static String _URL_MISSION_COUNTRY_LIST = _BASE_URL + "allCountry";

    public static String _URL_USER_LOGIN = _BASE_URL+"login";
    public static String _URL_REGISTER_USER = _BASE_URL+"registration";
    public static String _URL_USER_REGISTRATION_DETAILS = _BASE_URL + "getProfile";
    public static String _URL_EDIT_USER_PROFILE = _BASE_URL + "editProfile";

    public static String _URL_OTP_FORGOT_PASSWORD = _BASE_URL + "forgot-password";
    public static String _URL_OTP_CHANGE_PASSWORD = _BASE_URL + "sendOTP";
    public static String _URL_OTP_VERIFY_EMAIL = _BASE_URL + "verifyOTP";
    public static String _URL_OTP_VERIFY_FORGOTPASSWORD = _BASE_URL + "otp-verify";

    public static String _URL_CHANGE_PASSWORD =_BASE_URL+"changePassword";
    public static String _URL_CHANGE_PASSWORD_FOR_FORGOT_PASSWORD_USERS = _BASE_URL+"reset-password";

    public static String _URL_POST_TEXT_PRAYER = _BASE_URL+"post-prayer";
    public static String _URL_POST_VIDEO_PRAYER=_BASE_URL+"videoprayer";
    public static String _URL_POST_AUDIO_PRAYER=_BASE_URL+"audioprayer";

    public static String _URL_VIEW_ALL_PRAYER=_BASE_URL+"viewprayer";
    public static String _URL_UPDATE_PRAYER=_BASE_URL+"prayerstatus";


}

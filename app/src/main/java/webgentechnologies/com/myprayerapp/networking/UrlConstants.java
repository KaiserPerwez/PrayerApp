package webgentechnologies.com.myprayerapp.networking;

/**
 * Created by Satabhisha on 17-08-2017.
 */

public class UrlConstants {
    static String _URL_BASE="http://50.62.31.191/~prayerapp/api/public/index.php/api";
    public static String _URL_ALL_CHURCHES_LIST = _URL_BASE+"/allchurches";
    public static String _URL_REGISTER_USER = _URL_BASE+"/registration";
    public static String _URL_GET_COUNTRY = _URL_BASE+"/getCountry";
    public static String _URL_USER_LOGIN = _URL_BASE+"/login";
    public static String _URL_EDIT_PROFILE = _URL_BASE+"/editProfile";
    public static String _URL_OTP_IN_USER_EMAIL_FORGOT_PASSWORD = _URL_BASE+"/forgot-password";
    public static String _URL_OTP_IN_FORGOT_PASSWORD = _URL_BASE+"/otp-verify";

    public static String _URL_OTP_FOR_CHANGE_PASSWORD= _URL_BASE+"/sendOTP";
    public static String _URL_OTP_RECEIVED_FROM_EMAIL_VERIFY= _URL_BASE+"/verifyOTP";
    public static String _URL_CHANGE_PASSWORD= _URL_BASE+"/changePassword";
    public static String _URL_OTP_RECEIVED_FOR_FORGOTPASSWORD_VERIFY= _URL_BASE+"/otp-verify";
}

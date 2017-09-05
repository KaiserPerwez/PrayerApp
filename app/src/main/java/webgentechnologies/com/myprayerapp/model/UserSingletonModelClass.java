package webgentechnologies.com.myprayerapp.model;

/**
 * Created by Satabhisha on 16-08-2017.
 */

public class UserSingletonModelClass {


    private static UserSingletonModelClass _userSingletonModelClass = null;

    private UserSingletonModelClass() {
        //to avoid instantiation,it is declared as private
    }//why private:

    public static UserSingletonModelClass get_userSingletonModelClass() {
        if (_userSingletonModelClass == null)
            _userSingletonModelClass = new UserSingletonModelClass();
        return _userSingletonModelClass;
    }


    /*
    *RegnOneActivity variables
     */
    String txt_fname;
    String txt_lname;
    String txt_email;
    String txt_addr1;
    String txt_addr2;
    String txt_state_id;
    String txt_state;
    String txt_state_name;
    String txt_country_id2;
    String txt_phone;
    String txt_country_id1;
    String txt_country;
    String txt_country_sortname;
    String txt_selected_church_name;
    String txt_city;
    /*
    *RegnTwoActivity variables
     */
    String txt_classes_attended;
    String txt_others;
    String church_name;

    /*
    RegnThreeActivity variables
     */
    String txt_mission_trip;
    String txt_newto_mission;
    String txt_mission_trip_status; //txt_mission_trip_status extra field is added at editprofile else all fields are almost same for editprofile

    /*
    RegnFourActivity variables
     */
    String txt_pswd;

    /*
    LoginAcyivity variables
     */
    String txt_login_email_id;
    String txt_login_pwd;

    /*
    EditOneFrag variables
     */
    String txt_editone_fname;
    String txt_editone_lname;
    String txt_editone_email;
    String txt_editone_addr1;
    String txt_editone_addr2;
    String txt_editone_city;
    String txt_editone_phone;
    String txt_editone_countryid1;
    String txt_editone_country_name;
    String txt_editone_country_sortname;
    String txt_editone_state_id;
    String txt_editone_state_name;
    String txt_editone_country_id2;

    /*
    LoginActivity variables
     */
    String txt_user_login_id;
    String txt_user_access_token;
    String txt_temp_user_login_email;
    /*
    *Facebook Login variables
     */
    String txt_fcbl_lgn_first_name;
    String txt_fcbk_lgn_last_name;
    String txt_fcbk_lgn_email;
    String txt_fcbk_lgn_gender;
    String txt_fcbk_lgn_fcbkid;

    /*
    *ForgotPasswordActivity variables
     */
    String txt_email_for_forgot_password;
    String txt_otp_from_email;

//-----------------------getters----------------------


    public String getTxt_fname() {
        return txt_fname;
    }

    public String getTxt_lname() {
        return txt_lname;
    }

    public String getTxt_email() {
        return txt_email;
    }

    public String getTxt_addr1() {
        return txt_addr1;
    }

    public String getTxt_addr2() {
        return txt_addr2;
    }

    public String getTxt_state() {
        return txt_state;
    }

    public String getTxt_phone() {
        return txt_phone;
    }

    public String getTxt_country() {
        return txt_country;
    }

    public String getTxt_classes_attended() {
        return txt_classes_attended;
    }

    public String getTxt_others() {
        return txt_others;
    }

    public String getChurch_name() {
        return church_name;
    }

    public String getTxt_mission_trip() {
        return txt_mission_trip;
    }

    public String getTxt_newto_mission() {
        return txt_newto_mission;
    }

    public String getTxt_pswd() {
        return txt_pswd;
    }

    public String getTxt_selected_church_name() {
        return txt_selected_church_name;
    }

    public String getTxt_state_id() {
        return txt_state_id;
    }

    public String getTxt_state_name() {
        return txt_state_name;
    }

    public String getTxt_country_id2() {
        return txt_country_id2;
    }

    public String getTxt_country_id1() {
        return txt_country_id1;
    }

    public String getTxt_country_sortname() {
        return txt_country_sortname;
    }

    public String getTxt_city() {
        return txt_city;
    }

    public String getTxt_login_email_id() {
        return txt_login_email_id;
    }

    public String getTxt_login_pwd() {
        return txt_login_pwd;
    }

    public String getTxt_editone_fname() {
        return txt_editone_fname;
    }

    public String getTxt_editone_lname() {
        return txt_editone_lname;
    }

    public String getTxt_editone_email() {
        return txt_editone_email;
    }

    public String getTxt_editone_addr1() {
        return txt_editone_addr1;
    }

    public String getTxt_editone_addr2() {
        return txt_editone_addr2;
    }

    public String getTxt_editone_city() {
        return txt_editone_city;
    }

    public String getTxt_editone_phone() {
        return txt_editone_phone;
    }

    public String getTxt_editone_countryid1() {
        return txt_editone_countryid1;
    }

    public String getTxt_editone_country_name() {
        return txt_editone_country_name;
    }

    public String getTxt_editone_country_sortname() {
        return txt_editone_country_sortname;
    }

    public String getTxt_editone_state_id() {
        return txt_editone_state_id;
    }

    public String getTxt_editone_state_name() {
        return txt_editone_state_name;
    }

    public String getTxt_editone_country_id2() {
        return txt_editone_country_id2;
    }

    public String getTxt_user_login_id() {
        return txt_user_login_id;
    }

    public String getTxt_user_access_token() {
        return txt_user_access_token;
    }

    public String getTxt_mission_trip_status() {
        return txt_mission_trip_status;
    }

    public String getTxt_fcbl_lgn_first_name() {
        return txt_fcbl_lgn_first_name;
    }

    public String getTxt_fcbk_lgn_last_name() {
        return txt_fcbk_lgn_last_name;
    }

    public String getTxt_fcbk_lgn_email() {
        return txt_fcbk_lgn_email;
    }

    public String getTxt_fcbk_lgn_gender() {
        return txt_fcbk_lgn_gender;
    }

    public String getTxt_fcbk_lgn_fcbkid() {
        return txt_fcbk_lgn_fcbkid;
    }

    public String getTxt_temp_user_login_email() {
        return txt_temp_user_login_email;
    }

    public String getTxt_email_for_forgot_password() {
        return txt_email_for_forgot_password;
    }

    public String getTxt_otp_from_email() {
        return txt_otp_from_email;
    }
    //-----------------------setters----------------------


    public void setTxt_fname(String txt_fname) {
        this.txt_fname = txt_fname;
    }

    public void setTxt_lname(String txt_lname) {
        this.txt_lname = txt_lname;
    }

    public void setTxt_email(String txt_email) {
        this.txt_email = txt_email;
    }

    public void setTxt_addr1(String txt_addr1) {
        this.txt_addr1 = txt_addr1;
    }

    public void setTxt_addr2(String txt_addr2) {
        this.txt_addr2 = txt_addr2;
    }

    public void setTxt_state(String txt_state) {
        this.txt_state = txt_state;
    }

    public void setTxt_phone(String txt_phone) {
        this.txt_phone = txt_phone;
    }

    public void setTxt_country(String txt_country) {
        this.txt_country = txt_country;
    }

    public void setTxt_classes_attended(String txt_classes_attended) {
        this.txt_classes_attended = txt_classes_attended;
    }

    public void setTxt_others(String txt_others) {
        this.txt_others = txt_others;
    }

    public void setChurch_name(String church_name) {
        this.church_name = church_name;
    }

    public void setTxt_mission_trip(String txt_mission_trip) {
        this.txt_mission_trip = txt_mission_trip;
    }

    public void setTxt_newto_mission(String txt_newto_mission) {
        this.txt_newto_mission = txt_newto_mission;
    }

    public void setTxt_pswd(String txt_pswd) {
        this.txt_pswd = txt_pswd;
    }

    public void setTxt_state_id(String txt_state_id) {
        this.txt_state_id = txt_state_id;
    }

    public void setTxt_state_name(String txt_state_name) {
        this.txt_state_name = txt_state_name;
    }

    public void setTxt_country_id2(String txt_country_id2) {
        this.txt_country_id2 = txt_country_id2;
    }

    public void setTxt_country_id1(String txt_country_id1) {
        this.txt_country_id1 = txt_country_id1;
    }

    public void setTxt_country_sortname(String txt_country_sortname) {
        this.txt_country_sortname = txt_country_sortname;
    }

    public void setTxt_selected_church_name(String txt_selected_church_name) {
        this.txt_selected_church_name = txt_selected_church_name;

    }

    public void setTxt_city(String txt_city) {
        this.txt_city = txt_city;
    }

    public void setTxt_login_email_id(String txt_login_email_id) {
        this.txt_login_email_id = txt_login_email_id;
    }

    public void setTxt_login_pwd(String txt_login_pwd) {
        this.txt_login_pwd = txt_login_pwd;
    }

    public void setTxt_editone_fname(String txt_editone_fname) {
        this.txt_editone_fname = txt_editone_fname;
    }

    public void setTxt_editone_lname(String txt_editone_lname) {
        this.txt_editone_lname = txt_editone_lname;
    }

    public void setTxt_editone_email(String txt_editone_email) {
        this.txt_editone_email = txt_editone_email;
    }

    public void setTxt_editone_addr1(String txt_editone_addr1) {
        this.txt_editone_addr1 = txt_editone_addr1;
    }

    public void setTxt_editone_addr2(String txt_editone_addr2) {
        this.txt_editone_addr2 = txt_editone_addr2;
    }

    public void setTxt_editone_city(String txt_editone_city) {
        this.txt_editone_city = txt_editone_city;
    }

    public void setTxt_editone_phone(String txt_editone_phone) {
        this.txt_editone_phone = txt_editone_phone;
    }

    public void setTxt_editone_countryid1(String txt_editone_countryid1) {
        this.txt_editone_countryid1 = txt_editone_countryid1;
    }

    public void setTxt_editone_country_name(String txt_editone_country_name) {
        this.txt_editone_country_name = txt_editone_country_name;
    }

    public void setTxt_editone_country_sortname(String txt_editone_country_sortname) {
        this.txt_editone_country_sortname = txt_editone_country_sortname;
    }

    public void setTxt_editone_state_id(String txt_editone_state_id) {
        this.txt_editone_state_id = txt_editone_state_id;
    }

    public void setTxt_editone_state_name(String txt_editone_state_name) {
        this.txt_editone_state_name = txt_editone_state_name;
    }

    public void setTxt_editone_country_id2(String txt_editone_country_id2) {
        this.txt_editone_country_id2 = txt_editone_country_id2;
    }

    public void setTxt_user_login_id(String txt_user_login_id) {
        this.txt_user_login_id = txt_user_login_id;
    }

    public void setTxt_user_access_token(String txt_user_access_token) {
        this.txt_user_access_token = txt_user_access_token;
    }

    public void setTxt_mission_trip_status(String txt_mission_trip_status) {
        this.txt_mission_trip_status = txt_mission_trip_status;
    }

    public void setTxt_fcbl_lgn_first_name(String txt_fcbl_lgn_first_name) {
        this.txt_fcbl_lgn_first_name = txt_fcbl_lgn_first_name;
    }

    public void setTxt_fcbk_lgn_last_name(String txt_fcbk_lgn_last_name) {
        this.txt_fcbk_lgn_last_name = txt_fcbk_lgn_last_name;
    }

    public void setTxt_fcbk_lgn_email(String txt_fcbk_lgn_email) {
        this.txt_fcbk_lgn_email = txt_fcbk_lgn_email;
    }

    public void setTxt_fcbk_lgn_gender(String txt_fcbk_lgn_gender) {
        this.txt_fcbk_lgn_gender = txt_fcbk_lgn_gender;
    }

    public void setTxt_fcbk_lgn_fcbkid(String txt_fcbk_lgn_fcbkid) {
        this.txt_fcbk_lgn_fcbkid = txt_fcbk_lgn_fcbkid;
    }

    public void setTxt_temp_user_login_email(String txt_temp_user_login_email) {
        this.txt_temp_user_login_email = txt_temp_user_login_email;
    }

    public void setTxt_email_for_forgot_password(String txt_email_for_forgot_password) {
        this.txt_email_for_forgot_password = txt_email_for_forgot_password;
    }

    public void setTxt_otp_from_email(String txt_otp_from_email) {
        this.txt_otp_from_email = txt_otp_from_email;
    }
}

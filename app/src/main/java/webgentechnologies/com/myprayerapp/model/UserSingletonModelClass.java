package webgentechnologies.com.myprayerapp.model;

import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Satabhisha on 16-08-2017.
 */

public class UserSingletonModelClass {


    private static UserSingletonModelClass _userSingletonModelClass = null;
    /*
    *RegnOneActivity variables
     */
    String txt_fname;
    String txt_lname;
    String txt_email;
    String txt_addr1;
    String txt_addr2;
    String txt_phone;
    String txt_state_id;
    String txt_state_name;
    String txt_country_id;
    String txt_country;
    String txt_country_sortname;
    String txt_city;
    /*
    *RegnTwoActivity variables
     */
    List<String> list_classes_attended = new ArrayList<>();
    String church_name;
    String church_id;
    /*
    RegnThreeActivity variables
     */
    String txt_mission_trip_countries;
    String txt_newto_mission;
    String txt_mission_trip_participation_status; //txt_mission_trip_participation_status extra field is added at editprofile else all fields are almost same for editprofile
    /*
    RegnFourActivity variables
     */
    String txt_pswd;
    /*
    LoginActivity variables
     */
    String txt_user_login_id;
    String txt_user_access_token;
    //-----------extras--------------//
    String device_id;
    String device_type;
    String reg_type; //"normal" or "facebook"

    private UserSingletonModelClass() {
        //to avoid instantiation,it is declared as private
    }//why private:

    public static UserSingletonModelClass get_userSingletonModelClass() {
        if (_userSingletonModelClass == null)
            _userSingletonModelClass = new UserSingletonModelClass();
        return _userSingletonModelClass;
    }

    @Override
    public String toString() {
        return "UserSingletonModelClass{" +
                "txt_fname='" + txt_fname + '\'' +
                ", txt_lname='" + txt_lname + '\'' +
                ", txt_email='" + txt_email + '\'' +
                ", txt_addr1='" + txt_addr1 + '\'' +
                ", txt_addr2='" + txt_addr2 + '\'' +
                ", txt_phone='" + txt_phone + '\'' +
                ", txt_state_id='" + txt_state_id + '\'' +
                ", txt_state_name='" + txt_state_name + '\'' +
                ", txt_country_id='" + txt_country_id + '\'' +
                ", txt_country_name='" + txt_country + '\'' +
                ", txt_country_short_name='" + txt_country_sortname + '\'' +
                // ", txt_selected_church_name='" + txt_selected_church_name + '\'' +
                ", txt_city='" + txt_city + '\'' +
                ", list_classes_attended='" + list_classes_attended + '\'' +
                ", church_name='" + church_name + '\'' +
                ", txt_mission_trip_countries='" + txt_mission_trip_countries + '\'' +
                ", txt_newto_mission='" + txt_newto_mission + '\'' +
                ", txt_mission_trip_participation_status='" + txt_mission_trip_participation_status + '\'' +
                ", txt_pswd='" + txt_pswd + '\'' +
                ", txt_user_login_id='" + txt_user_login_id + '\'' +
                ", txt_user_access_token='" + txt_user_access_token + '\'' +
                '}';
    }

    public String getChurch_id() {
        return church_id;
    }

    public void setChurch_id(String church_id) {
        this.church_id = church_id;
    }

//    String txt_fcbk_login_and_normal_login_email;
//    String txt_fcbk_lgn_fcbkid;

//-----------------------getters----------------------

    public String getTxt_fname() {
        return txt_fname;
    }

    public void setTxt_fname(String txt_fname) {
        this.txt_fname = txt_fname;
    }

    public String getTxt_lname() {
        return txt_lname;
    }

    public void setTxt_lname(String txt_lname) {
        this.txt_lname = txt_lname;
    }

    public String getTxt_email() {
        return txt_email;
    }

    public void setTxt_email(String txt_email) {
        this.txt_email = txt_email;
    }

    public String getTxt_addr1() {
        return txt_addr1;
    }

    public void setTxt_addr1(String txt_addr1) {
        this.txt_addr1 = txt_addr1;
    }

    public String getTxt_addr2() {
        return txt_addr2;
    }

    public void setTxt_addr2(String txt_addr2) {
        this.txt_addr2 = txt_addr2;
    }

    public String getTxt_state_id() {
        return txt_state_id;
    }

   /* public String getTxt_selected_church_name() {
        return txt_selected_church_name;
    }*/

    public void setTxt_state_id(String txt_state_id) {
        this.txt_state_id = txt_state_id;
    }

    public String getTxt_state_name() {
        return txt_state_name;
    }

    public void setTxt_state_name(String txt_state_name) {
        this.txt_state_name = txt_state_name;
    }

    public String getTxt_phone() {
        return txt_phone;
    }

    public void setTxt_phone(String txt_phone) {
        this.txt_phone = txt_phone;
    }

    public String getTxt_country_id() {
        return txt_country_id;
    }

    public void setTxt_country_id(String txt_country_id) {
        this.txt_country_id = txt_country_id;
    }

    public String getTxt_country() {
        return txt_country;
    }

    public void setTxt_country(String txt_country) {
        this.txt_country = txt_country;
    }

    public String getTxt_country_sortname() {
        return txt_country_sortname;
    }

    public void setTxt_country_sortname(String txt_country_sortname) {
        this.txt_country_sortname = txt_country_sortname;
    }

    public String getTxt_city() {
        return txt_city;
    }

    //
//    public String getTxt_fcbk_login_and_normal_login_email() {
//        return txt_fcbk_login_and_normal_login_email;
//    }
//
//    public String getTxt_fcbk_lgn_fcbkid() {
//        return txt_fcbk_lgn_fcbkid;
//    }

    //-----------------------setters----------------------

    public void setTxt_city(String txt_city) {
        this.txt_city = txt_city;
    }

    public String getChurch_name() {
        return church_name;
    }

    public void setChurch_name(String church_name) {
        this.church_name = church_name;
    }

    public String getTxt_mission_trip_countries() {
        return txt_mission_trip_countries;
    }

    public void setTxt_mission_trip_countries(String txt_mission_trip_countries) {
        this.txt_mission_trip_countries = txt_mission_trip_countries;
    }

    public String getTxt_newto_mission() {
        return txt_newto_mission;
    }

    public void setTxt_newto_mission(String txt_newto_mission) {
        this.txt_newto_mission = txt_newto_mission;
    }

    public String getTxt_mission_trip_participation_status() {
        return txt_mission_trip_participation_status;
    }

    public void setTxt_mission_trip_participation_status(String txt_mission_trip_participation_status) {
        this.txt_mission_trip_participation_status = txt_mission_trip_participation_status;
    }

    public String getTxt_pswd() {
        return txt_pswd;
    }

    public void setTxt_pswd(String txt_pswd) {
        this.txt_pswd = txt_pswd;
    }

  /*  public void setTxt_selected_church_name(String txt_selected_church_name) {
        this.txt_selected_church_name = txt_selected_church_name;
    }*/

    public String getTxt_user_login_id() {
        return txt_user_login_id;
    }

    public void setTxt_user_login_id(String txt_user_login_id) {
        this.txt_user_login_id = txt_user_login_id;
    }

    public String getTxt_user_access_token() {
        return txt_user_access_token;
    }

    public void setTxt_user_access_token(String txt_user_access_token) {
        this.txt_user_access_token = txt_user_access_token;
    }

    public List<String> getList_classes_attended() {
        return list_classes_attended;
    }

    public void setList_classes_attended(List<String> list_classes_attended) {
        getList_classes_attended().clear();
        this.list_classes_attended = list_classes_attended;
    }

    public String getDevice_id() {
        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return device_id;
    }

    public String getDevice_type() {
        device_type = "Android";
        return device_type;
    }

    public String getReg_type() {
        return reg_type;
    }

    public void setReg_type(String reg_type) {
        this.reg_type = reg_type;
    }

    public void addClassesAttended(String str) {
        if (getList_classes_attended().size() > 0 && getList_classes_attended().contains(str))
            return;

        getList_classes_attended().add(str);
    }

    public void destroyUser() {
        _userSingletonModelClass = null;
    }

    //    public void setTxt_fcbk_login_and_normal_login_email(String txt_fcbk_login_and_normal_login_email) {
//        this.txt_fcbk_login_and_normal_login_email = txt_fcbk_login_and_normal_login_email;
//    }
//
//    public void setTxt_fcbk_lgn_fcbkid(String txt_fcbk_lgn_fcbkid) {
//        this.txt_fcbk_lgn_fcbkid = txt_fcbk_lgn_fcbkid;
//    }
}

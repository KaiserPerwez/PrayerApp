package webgentechnologies.com.myprayerapp.model;

/**
 * Created by Satabhisha on 21-08-2017.
 */

public class UserLocationModel {
    String txt_state_id;
    String txt_state;
    String txt_state_name;
    String txt_country_id2;
    String txt_country_id1;
    String txt_country;
    String txt_country_sortname;

    //---------------------------------getters---------------------------------

    public String getTxt_state_id() {
        return txt_state_id;
    }

    public String getTxt_state() {
        return txt_state;
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

    public String getTxt_country() {
        return txt_country;
    }

    public String getTxt_country_sortname() {
        return txt_country_sortname;
    }

    //----------------------Setters----------------------------

    public void setTxt_state_id(String txt_state_id) {
        this.txt_state_id = txt_state_id;
    }

    public void setTxt_state(String txt_state) {
        this.txt_state = txt_state;
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

    public void setTxt_country(String txt_country) {
        this.txt_country = txt_country;
    }

    public void setTxt_country_sortname(String txt_country_sortname) {
        this.txt_country_sortname = txt_country_sortname;
    }
}

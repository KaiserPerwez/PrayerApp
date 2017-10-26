package com.wgt.myprayerapp.Utils;

/**
 * Created by Kaiser on 07-09-2017.
 */

public class ValidatorUtils {
    public static boolean isValidEmail(String email) {
        int pos_AtSign = email.indexOf('@');
        int pos_last_AtSign = email.lastIndexOf('@');
        int pos_dot = email.lastIndexOf('.');
        boolean only_one_AtSign_present = (pos_last_AtSign == pos_AtSign); //pos_last_AtSign==pos_AtSign signifies that email has only one '@' symbol
        int num_of_letters_between_AtSign_n_dot = pos_dot - pos_AtSign;
        boolean doesnt_end_with_dot = pos_dot < email.length() - 1;
        if (pos_AtSign > 0 && num_of_letters_between_AtSign_n_dot > 2 && doesnt_end_with_dot && only_one_AtSign_present)
            return true;
        return false;
    }
}

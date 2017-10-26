package com.wgt.myprayerapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Satabhisha on 21-08-2017.
 */

public class CountryModel {

    String country_id;
    String country_name;
    String country_short_name;
    List<StateModel> stateModelList = new ArrayList<>();


    //---------------------------------getters---------------------------------


    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    //----------------------Setters----------------------------

    public String getCountry_short_name() {
        return country_short_name;
    }

    public void setCountry_short_name(String country_short_name) {
        this.country_short_name = country_short_name;
    }

    public List<StateModel> getStateModelList() {
        return stateModelList;
    }

    public void setStateModelList(List<StateModel> stateModelList) {
        this.stateModelList = stateModelList;
    }

    public void addStateModel(StateModel stateModel) {
        getStateModelList().add(stateModel);
    }
}

package com.yukti.dataone.model;

import java.util.ArrayList;

/**
 * Created by Creadigol on 2/17/2016.
 */
public class WebPicsModel
{
    private String carid;
    private String creation_date;

    public ArrayList<String> getImagepath() {
        return imagepath;
    }

    public void setImagepath(ArrayList<String> imagepath) {
        this.imagepath = imagepath;
    }

    public ArrayList<String> imagepath=new ArrayList<>();
    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

}

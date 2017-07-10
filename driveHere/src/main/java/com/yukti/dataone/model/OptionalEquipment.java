package com.yukti.dataone.model;

import java.util.ArrayList;

/**
 * Created by admin on 1/18/2016.
 */
public class OptionalEquipment
{
    public String category;
    public ArrayList<OptionsC> options;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<OptionsC> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<OptionsC> options) {
        this.options = options;
    }
}

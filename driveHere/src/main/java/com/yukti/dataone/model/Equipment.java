package com.yukti.dataone.model;

import java.util.ArrayList;

public class Equipment
{
    public String name;
    public ArrayList<Values> values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Values> getValues() {
        return values;
    }

    public void setValues(ArrayList<Values> values) {
        this.values = values;
    }
}

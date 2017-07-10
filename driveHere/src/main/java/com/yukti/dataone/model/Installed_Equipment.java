package com.yukti.dataone.model;

import java.util.ArrayList;

/**
 * Created by admin on 1/18/2016.
 */
public class Installed_Equipment
{
    public String category;
    public ArrayList<Equipment> equipment;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(ArrayList<Equipment> equipment) {
        this.equipment = equipment;
    }
}

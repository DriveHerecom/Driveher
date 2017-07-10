package com.yukti.dataone.model;

import java.util.ArrayList;

/**
 * Created by admin on 1/18/2016.
 */
public class Specifications
{
    public ArrayList<Specs> specifications;
    public String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<Specs> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(ArrayList<Specs> specifications) {
        this.specifications = specifications;
    }
}

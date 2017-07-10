package com.yukti.utils;


public class DrawerItem {

    private String catName, title, catId,description;
    private int imgResID;
    
    public DrawerItem(String catName, int imgResID ,String description) {
        setCatName(catName);
        setCatId("0");
        setImgResID(imgResID);
        setDescription(description);
        setTitle(null);
    }

    public DrawerItem(String catName, String catId) {
        setCatName(catName);
        setCatId(catId);
        setImgResID(0);
        setTitle(null);
    }

    public String getCatName() {
        return catName;
    }

    public String getTitle() {
        return title;
    }

    public String getCatId() {
        return catId;
    }

    public int getImgResID() {
        return imgResID;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

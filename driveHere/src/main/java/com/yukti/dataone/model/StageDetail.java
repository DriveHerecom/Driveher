package com.yukti.dataone.model;

public class StageDetail
{
    String stage;
    int total;

    public String getStageName() {
        return stage;
    }

    public void setStageName(String stageName) {
        this.stage = stageName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
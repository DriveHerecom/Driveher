package com.yukti.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchContract implements Serializable
{
    public String status_code,message;
    public ArrayList<SearchContractData> contracts;
}

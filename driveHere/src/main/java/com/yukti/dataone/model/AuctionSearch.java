package com.yukti.dataone.model;

import com.yukti.driveherenew.search.CarInventory;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 2/5/2016.
 */

public class AuctionSearch implements Serializable
{
    public String status_code, message;
    public int carDetailCount;
    public ArrayList<CarInventory> carDetail;
}

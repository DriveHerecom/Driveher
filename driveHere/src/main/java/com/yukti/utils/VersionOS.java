package com.yukti.utils;

import android.os.Build;

/**
 * Created by prashant on 23/5/16.
 */
public class VersionOS
{

    public boolean checkVersion()
    {
        if(Build.VERSION.SDK_INT>=23)
        {
           return true;
        }
        return false;
    }

}
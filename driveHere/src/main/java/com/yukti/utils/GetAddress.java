package com.yukti.utils;

import android.content.Context;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by prashant on 22/9/16.
 */
public class GetAddress {

    Geocoder geocoder;
    List<android.location.Address> addresses;

    String Address = "";

    public GetAddress() {

    }

    public String getAddressUsingLatLong(Context context, Double lat, Double lng) {
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            if (addresses.get(0).getAddressLine(0) != null)
                Address = Address + addresses.get(0).getAddressLine(0) + " ";

            if (addresses.get(0).getAddressLine(1) != null)
                Address = Address + addresses.get(0).getAddressLine(1) + " ";
            else {
                if (addresses.get(0).getLocality() != null)
                    Address = Address + addresses.get(0).getLocality() + " ";

                if (addresses.get(0).getAdminArea() != null)
                    Address = Address + addresses.get(0).getAdminArea() + " ";

                if (addresses.get(0).getPostalCode() != null)
                    Address = Address + addresses.get(0).getPostalCode() + " ";
            }

            if (addresses.get(0).getAddressLine(2) != null)
                Address = Address + addresses.get(0).getAddressLine(2) + " ";
            else if (addresses.get(0).getCountryName() != null)
                Address = Address + addresses.get(0).getCountryName() + " ";

        }
        return Address;
    }
}

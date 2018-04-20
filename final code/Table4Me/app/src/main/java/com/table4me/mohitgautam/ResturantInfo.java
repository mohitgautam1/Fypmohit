package com.table4me.mohitgautam;

/**
 * Created by Ali Lal Din on 22/03/2018.
 */

public class ResturantInfo
{
    private String RestName,RestType;
    private Double Lat,Lon;

    public ResturantInfo()
    {
        // Empty Default Constructor Required
    }



    public String getRestName() {
        return RestName;
    }

    public String getRestType()
    {
        return RestType;
    }

    public void setRestName(String restName) {
        RestName = restName;
    }

    public void setRestType(String restType) {
        RestType = restType;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public void setLon(Double lon) {
        Lon = lon;
    }

    public Double getLat() {
        return Lat;
    }


    public Double getLon() {
        return Lon;
    }
}

package com.table4me.mohitgautam;

/**
 * Created by Ali Lal Din on 08/04/2018.
 */

public class myRestaurantsClass
{
    private String ResType,ResImage,ResName,ResTags,OwnerID;
    private Double Lat,Lon;

    public myRestaurantsClass()
    {
        // Empty Public Constructor Required By Firebase-UI Database Library
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }

    public String getResType() {
        return ResType;
    }

    public void setResType(String resType) {
        ResType = resType;
    }

    public String getResImage() {
        return ResImage;
    }

    public void setResImage(String resImage) {
        ResImage = resImage;
    }

    public String getResName() {
        return ResName;
    }

    public void setResName(String resName) {
        ResName = resName;
    }

    public String getResTags() {
        return ResTags;
    }

    public void setResTags(String resTags) {
        ResTags = resTags;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLon() {
        return Lon;
    }

    public void setLon(Double lon) {
        Lon = lon;
    }
}

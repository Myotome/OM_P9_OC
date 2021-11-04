
package com.openclassrooms.realestatemanager.model.geocodingPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Viewport {

    @SerializedName("northeast")
    @Expose
    private Northeast__1 northeast;
    @SerializedName("southwest")
    @Expose
    private Southwest__1 southwest;

    public Northeast__1 getNortheast() {
        return northeast;
    }

    public void setNortheast(Northeast__1 northeast) {
        this.northeast = northeast;
    }

    public Southwest__1 getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Southwest__1 southwest) {
        this.southwest = southwest;
    }

}

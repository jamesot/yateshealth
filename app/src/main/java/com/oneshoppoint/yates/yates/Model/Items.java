package com.oneshoppoint.yates.yates.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by stephineosoro on 03/06/16.
 */

public class Items implements Serializable {

    private String title, thumbnailUrl;
    private String TheID;
    private String price;
    private String description;

    public Items() {
    }

    public Items(String title, String thumbnailUrl, String TheID, String price,
                 String description) {

        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.TheID = TheID;
        this.price = price;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTheID() {
        return TheID;
    }

    public void setTheID(String theID) {
        this.TheID = theID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String Price) {
        this.price = Price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


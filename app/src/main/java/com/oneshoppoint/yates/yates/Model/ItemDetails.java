package com.oneshoppoint.yates.yates.Model;


        import android.widget.ImageView;
        import android.widget.ProgressBar;

/**
 * Created by stephineosoro on 03/06/16.
 */


public class ItemDetails {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImageNumber() {
        return imageNumber;
    }
    public void setImageNumber(int imageNumber) {
        this.imageNumber = imageNumber;
    }

    public String getID() {
        return ID;
    }
    public void setID (String ID) {
        this.ID = ID;
    }

    public ProgressBar getPb() {
        return pb;
    }
    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }

    public String getURL() {
        return url;
    }
    public void setURL(String url) {
        this.url = url;
    }

    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }
    public void setTotal(String Total) {
        this.total = Total;
    }

    public String getEmail() {        return email;  }
    public void setEmail(String email) {     this.email = email;    }

    public String getPaybill() {        return paybill;  }
    public void setPaybill(String pbill) {     this.paybill = pbill;    }

    private String name;
    private String total;
    private String quantity;
    private String itemDescription;
    private String price;
    private String url;
    private int imageNumber;
    private String ID;
    private ImageView img;
    private ProgressBar pb;
    private String email;
    private String paybill;

}

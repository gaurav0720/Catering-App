package com.example.cateringapp;

public class Home {

    String itemName, itemPrice, itemOPrice, itemAvailability, itemPhoto;

    public Home() {
    }

    public Home(String itemName, String itemPrice, String itemOPrice, String itemAvailability, String itemPhoto) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemOPrice = itemOPrice;
        this.itemAvailability = itemAvailability;
        this.itemPhoto = itemPhoto;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemOPrice() {
        return itemOPrice;
    }

    public void setItemOPrice(String itemOPrice) {
        this.itemOPrice = itemOPrice;
    }

    public String getItemAvailability() {
        return itemAvailability;
    }

    public void setItemAvailability(String itemAvailability) {
        this.itemAvailability = itemAvailability;
    }

    public String getItemPhoto() {
        return itemPhoto;
    }

    public void setItemPhoto(String itemPhoto) {
        this.itemPhoto = itemPhoto;
    }
}

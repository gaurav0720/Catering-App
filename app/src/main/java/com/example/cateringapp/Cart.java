package com.example.cateringapp;

public class Cart {

    private String itemCount,itemImage, itemName, itemOPrice, itemPrice, itemAmount;

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemOPrice() {
        return itemOPrice;
    }

    public void setItemOPrice(String itemOPrice) {
        this.itemOPrice = itemOPrice;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
    }

    public Cart(String itemCount, String itemImage, String itemName, String itemOPrice, String itemPrice, String itemAmount) {
        this.itemCount = itemCount;
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemOPrice = itemOPrice;
        this.itemPrice = itemPrice;
        this.itemAmount = itemAmount;
    }

    public Cart() {
    }
}

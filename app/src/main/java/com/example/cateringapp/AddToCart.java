package com.example.cateringapp;

public class AddToCart {

    String itemName, itemOPrice, itemPrice, itemImage, itemCount, itemAmount;

    public AddToCart() {
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

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemCount() {
        return itemCount;
    }

    public String getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public AddToCart(String itemName, String itemOPrice, String itemPrice, String itemImage, String itemCount, String itemAmount) {
        this.itemName = itemName;
        this.itemOPrice = itemOPrice;
        this.itemPrice = itemPrice;
        this.itemImage = itemImage;
        this.itemCount = itemCount;
        this.itemAmount = itemAmount;
    }
}

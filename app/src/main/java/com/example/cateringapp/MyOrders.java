package com.example.cateringapp;

import java.util.ArrayList;

public class MyOrders {

    private String orderName;
    private ArrayList<String> orderItems;
    private ArrayList<String> orderItemCounts;
    private ArrayList<String> orderItemCountsAmount;
    private String totalAmount;
    private String orderBy;

    public MyOrders() {
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public MyOrders(String orderName, ArrayList<String> orderItems, ArrayList<String> orderItemCounts,
                    ArrayList<String> orderItemCountsAmount, String totalAmount, String orderBy) {
        this.orderName = orderName;
        this.orderItems = orderItems;
        this.orderItemCounts = orderItemCounts;
        this.orderItemCountsAmount = orderItemCountsAmount;
        this.totalAmount = totalAmount;
        this.orderBy = orderBy;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public ArrayList<String> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<String> orderItems) {
        this.orderItems = orderItems;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<String> getOrderItemCounts() {
        return orderItemCounts;
    }

    public void setOrderItemCounts(ArrayList<String> orderItemCounts) {
        this.orderItemCounts = orderItemCounts;
    }

    public ArrayList<String> getOrderItemCountsAmount() {
        return orderItemCountsAmount;
    }

    public void setOrderItemCountsAmount(ArrayList<String> orderItemCountsAmount) {
        this.orderItemCountsAmount = orderItemCountsAmount;
    }
}

package com.example.rest;

public class Package {
    private int id;
    private String name;
    private int period;
    private double price;
    private int dataAmount;
    private int minAmount;
    private int smsAmount;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getPeriod() { return period; }
    public void setPeriod(int period) { this.period = period; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getDataAmount() { return dataAmount; }
    public void setDataAmount(int dataAmount) { this.dataAmount = dataAmount; }
    public int getMinAmount() { return minAmount; }
    public void setMinAmount(int minAmount) { this.minAmount = minAmount; }
    public int getSmsAmount() { return smsAmount; }
    public void setSmsAmount(int smsAmount) { this.smsAmount = smsAmount; }
}

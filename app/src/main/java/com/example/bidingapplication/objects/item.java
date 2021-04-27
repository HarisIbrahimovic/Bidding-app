package com.example.bidingapplication.objects;

public class item {
    private String name;
    private String desc;
    private String imageUrl;
    private String price;
    private String username;
    private String id;
    private String bestBid;

    public String getBestBid() {
        return bestBid;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}

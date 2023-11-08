package com.example.rfid;


import android.os.Build;

import java.util.Base64;

public class Tag {
    private String tagID;
    private String RSSI;
    private String distance;
//    private String orderID;
    public Tag(String tagID, String RSSI){
        this.tagID = tagID;
        this.RSSI= RSSI;
//        this.orderID = orderID;
    }
    public Tag(String tagID, String RSSI, String distance){
        this.tagID = tagID;
        this.RSSI= RSSI;
        this.distance = distance;
    }
    public String getTagID(){
        return tagID;
    }
    public String getRSSI(){
        return RSSI;
    }
    public String getDistance() {return distance;}

//    public String getOrderID(){
//        return orderID;
//    }

}

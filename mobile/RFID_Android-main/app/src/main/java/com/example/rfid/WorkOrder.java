package com.example.rfid;

import android.os.Build;

import java.time.LocalDateTime;
import java.util.Base64;

public class WorkOrder {
    private String workOrderID;
    private String Date;

    public WorkOrder(String workOrderID, String Date){
        this.workOrderID=workOrderID;
        this.Date = Date;
    }

    public String getWorkOrderID(){
        return workOrderID;
    }

    public String getDate(){
        return Date;
    }


}

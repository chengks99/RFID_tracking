package com.example.rfid;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

public class WorkOrder implements Parcelable, Serializable{
    private String workOrderID;
    private String Date;
    private String uniqueId;

    public WorkOrder(String workOrderID, String Date){
        this.workOrderID=workOrderID;
        this.Date = Date;
        this.uniqueId = workOrderID + Date;
    }
    public String getUniqueId() {
        return uniqueId;
    }

    public String getWorkOrderID(){
        return workOrderID;
    }

    public String getDate(){
        return Date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(workOrderID);
        parcel.writeString(Date);
    }
    public static final Parcelable.Creator<WorkOrder> CREATOR = new Parcelable.Creator<WorkOrder>() {
        public WorkOrder createFromParcel(Parcel in) {
            return new WorkOrder(in);
        }

        public WorkOrder[] newArray(int size) {
            return new WorkOrder[size];
        }
    };

    private WorkOrder(Parcel in) {
        workOrderID = in.readString();
        Date = in.readString();
    }
}

package com.example.rfid;
import java.util.Comparator;

public class WorkOrderComparator implements Comparator<WorkOrder> {
    @Override
    public int compare(WorkOrder order1, WorkOrder order2) {
        // Compare WorkOrder objects based on their dates
        return order2.getDate().compareTo(order1.getDate());
    }
}
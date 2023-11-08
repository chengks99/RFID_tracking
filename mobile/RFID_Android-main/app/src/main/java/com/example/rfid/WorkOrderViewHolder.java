package com.example.rfid;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkOrderViewHolder extends RecyclerView.ViewHolder {
    TextView workOrderId;
    TextView date_time;
    public WorkOrderViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView){
        super(itemView);
        workOrderId = itemView.findViewById(R.id.historyWorkOrderID);
        date_time = itemView.findViewById(R.id.historyDateTime);

    }
}

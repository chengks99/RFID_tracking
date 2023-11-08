package com.example.rfid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorkOrderRecyclerAdapter extends RecyclerView.Adapter<WorkOrderViewHolder>{
    Context context;
    List<WorkOrder> items;

    public WorkOrderRecyclerAdapter(Context context, List<WorkOrder> items){
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public WorkOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkOrderViewHolder(LayoutInflater.from(context).inflate(R.layout.history_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WorkOrderViewHolder holder, int position) {
        holder.workOrderId.setText(items.get(position).getWorkOrderID());
        holder.date_time.setText(items.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

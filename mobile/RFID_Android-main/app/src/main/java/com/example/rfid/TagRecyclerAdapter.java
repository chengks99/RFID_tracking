package com.example.rfid;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Base64;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class TagRecyclerAdapter extends RecyclerView.Adapter<TagRecyclerAdapter.TagViewHolder> implements Filterable {

    private Context context;
//    private List<String> tags;
    private List<Tag> tags;
    private List<Tag> tagListFull;
    public static double n=0.21299;

    public void updateTagListFull(List<Tag> newTags) {
        tagListFull.clear();
        tagListFull.addAll(newTags);
    }

    class TagViewHolder extends RecyclerView.ViewHolder {
        TextView RSSI;
        TextView tagID;
        TextView distance;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagID = itemView.findViewById(R.id.orderText);
            RSSI = itemView.findViewById(R.id.rssiVal);
            distance = itemView.findViewById(R.id.distance);
        }
    }


    public TagRecyclerAdapter(List<Tag> tags){
        this.context = context;
        this.tags = tags;
        this.tagListFull= new ArrayList<>(tags);
    }
//    public TagRecyclerAdapter(Context context, List<String> tag) {
//        this.context = context;
//        this.tags = tags;
//        this.tagFilteredList = new ArrayList<>(tags);
//    }
public static List<String> findUniqueTags(List<Tag> tags) {
    List<String> uniqueTags = new ArrayList<>();
    Set<String> seenTags = new HashSet<>();

    for (Tag tag : tags) {
        String tagID = tag.getTagID();
        if (!seenTags.contains(tagID)) {
            uniqueTags.add(tagID);
            seenTags.add(tagID);
        }
    }

    return uniqueTags;
}
    public static double calculateAvgRSSI(List<Tag> tags, String tagID){
        double sum=0;
        int count=0;
//        System.out.println("tag size: "+String.valueOf(tags.size()));
        for (Tag tag: tags){
            if (tag.getTagID().equals(tagID)){
                sum += Integer.valueOf(tag.getRSSI());
                count +=1;
            }
        }
//        System.out.println("count: "+String.valueOf(count));
        if (count !=0){
        return sum/count;}
        else{
            return 0;
        }
    }
    public static double distanceAlgorithm3(List<Tag> tags,Double avgRSSI, String tagID){
//        double avgRSSI = calculateAvgRSSI(tags,tagID);
        double rssiRatio = (double) avgRSSI/-51;
        Log.d("distance Algorithm rssi At 1m (defualt):", "-51");
        Log.d("distance Algorithm received params (default): ", "0.21299");
        double squareRoot = Math.pow(rssiRatio,1/n);
        double result = squareRoot * 1;
//        distances.putValue(tagID, result);
        return result;
    }

    public static double distanceAlgorithm3(List<Tag> tags, Double avgRSSI, String tagID,Double receivedParams,Double rssiAt1m){
//        double avgRSSI = calculateAvgRSSI(tags,tagID);
        double rssiRatio = (double) avgRSSI/rssiAt1m;
        Log.d("distance Algorithm rssi At 1m:", String.valueOf(rssiAt1m));
        Log.d("distance Algorithm received params: ", String.valueOf(receivedParams));
        double squareRoot = Math.pow(rssiRatio,1/receivedParams);
        double result = squareRoot * 1;
//        distances.putValue(tagID, result);
        return result;
    }
    public int CountUniqueTags(List<Tag> tags){
        List<String> uniqueTags = new ArrayList<>();
        Integer countUnique=0;
        for (Tag tag : tags){
            if (!uniqueTags.contains(tag.getTagID())){
                uniqueTags.add(tag.getTagID());
                countUnique++;
            }

    };
        return countUnique;}

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_view_, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        Tag tag = tags.get(position);
        holder.tagID.setText(tag.getTagID());
        holder.RSSI.setText(tag.getRSSI());
        holder.distance.setText(tag.getDistance());
//        holder.RSSI.
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }


    @Override
    public Filter getFilter(){
        return tagFilter;

    }
    private Filter tagFilter = new Filter(){
        @RequiresApi(api = Build.VERSION_CODES.O)
        protected FilterResults performFiltering(CharSequence constraint){
            List<Tag> filteredList = new ArrayList<>();
            System.out.println("tagListFull:" + tagListFull);
            if (constraint == null || constraint.length() ==0){
                filteredList.addAll(tagListFull);
            }
            else{
                String filterPattern = constraint.toString().trim().toLowerCase(Locale.getDefault());
                System.out.println("filter pattern:"+ filterPattern);
                for (Tag tag : tagListFull){
                    System.out.println("tagID:"+tag.getTagID());
                    System.out.println("tagID lower case"+ tag.getTagID().toLowerCase(Locale.getDefault()));
                    if (tag.getTagID().toLowerCase(Locale.getDefault()).contains(filterPattern)){
                        filteredList.add(tag);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values= filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results){
            tags.clear();
            tags.addAll((List<Tag>) results.values);
            notifyDataSetChanged();

        }
    };
}
package com.example.rfid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {
    public ArrayList<WorkOrder> receivedWorkOrders= new ArrayList<WorkOrder>();
    public ArrayList<WorkOrder> items= new ArrayList<WorkOrder>();
    Button clearHistoryBtn;
    SharedPreferences sp;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        sp = getSharedPreferences("WorkOrderPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        clearHistoryBtn = findViewById(R.id.clearHistorybtn);

        recyclerView = findViewById(R.id.recyclerView);

        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle != null) {
            receivedWorkOrders = receivedBundle.getParcelableArrayList("workOrders");
//            items.clear(); // Clear the items list

            if (receivedWorkOrders != null) {
                // Save into shared preferences with unique keys
                for (int i = 0; i < receivedWorkOrders.size(); i++) {
                    WorkOrder item = receivedWorkOrders.get(i);
                    try {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        objectOutputStream.writeObject(item);

                        String workOrderString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//                        long timestamp = System.currentTimeMillis();
//                        String key = "workOrderKey" + (getSPSize(sp)+i);// Use a unique key for each work order
                        String key = "workOrderKey"+ item.getDate();
                        System.out.println("workOrderKey:" + key);
                        editor.putString(key, workOrderString);
                        editor.apply();

//                        items.add(item); // Add the work order to the items list
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        items.clear();
        // Get all the keys from SharedPreferences
        Map<String, ?> allEntries = sp.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();

            if (key.startsWith("workOrderKey")) { // Filter by your work order keys
                // Retrieve the serialized WorkOrder string
                String workOrderString = sp.getString(key, null);

                // Deserialize the string into a WorkOrder object
                if (workOrderString != null) {
                    byte[] bytes = Base64.decode(workOrderString, Base64.DEFAULT);
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                    try {
                        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                        WorkOrder workOrder = (WorkOrder) objectInputStream.readObject();
                        items.add(workOrder);

//                        if (!items.contains(workOrder)) {
//                            items.add(workOrder); // Add the work order to the items list if not already present
//                        }
                    } catch (ClassNotFoundException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        Collections.sort(items, new WorkOrderComparator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new WorkOrderRecyclerAdapter(getApplicationContext(), items));
    }

    private int getSPSize(SharedPreferences sp){
        Map<String, ?> allEntries = sp.getAll();
        int count=0;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("workOrderKey")) {
                count+=1;
            }
            }
    return count;
    }

    public void clearSP(View view){
        sp = getSharedPreferences("WorkOrderPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        items.clear();
        recyclerView.getAdapter().notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.history:
                Intent intent = new Intent(this,HistoryActivity.class);
                startActivity(intent);
                finish();
                return true;
//            case R.id.settings:
//                intent = new Intent(this,SettingsActivity.class);
//                startActivity(intent);
//                finish();
//                return true;
            case R.id.home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
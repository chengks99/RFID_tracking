package com.example.rfid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rfid.ui.home.HomeFragment;
import com.example.rfid.ui.home.HomeViewModel;
import com.example.rfid.ui.locate.LocateTagFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rfid.databinding.ActivityMainBinding;
import com.zebra.rfid.api3.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RFIDHandler.ResponseHandlerInterface {
    private Button clearButton;
//    private Button submitButton;
//    private TextView statusText;
    public TextView rfidStatus;
    public TextView totalReads;
    public TextView uniqueTags;
    private TextView workOrderSubmitText;
//    private EditText editTextWorkOrderID;
    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 100;
    RFIDHandler rfidHandler;
    RecyclerView recyclerView;
    TagRecyclerAdapter tagRecyclerAdapter;
    List <Tag> tags = new ArrayList<>();
    List<Tag> totalTag = new ArrayList<>();
    List<Tag> sb = new ArrayList<>();
    private SearchView searchView;
    DecimalFormat decimalFormat = new DecimalFormat("#.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rfidHandler = new RFIDHandler();

//        TextViews
//        statusText = findViewById(R.id.status_text);
        clearButton = findViewById(R.id.clearButton);
//        submitButton = findViewById(R.id.submitButton);
        rfidStatus = findViewById(R.id.rfidStatus);
        totalReads = findViewById(R.id.totalReadsCount);
        uniqueTags = findViewById(R.id.uniqueTagsCount);

//        EditText
//        editTextWorkOrderID = findViewById(R.id.editTextWorkOrderID);

//        textrfid =  findViewById(R.id.textRFID);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchView);


        tagRecyclerAdapter = new TagRecyclerAdapter(tags);
        recyclerView.setAdapter(tagRecyclerAdapter);

        searchView.clearFocus();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                MainActivity.this.arrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//              MainActivity.this.tagRecyclerAdapter.getFilter().filter(newText);
                tagRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT},
                        BLUETOOTH_PERMISSION_REQUEST_CODE);
            }else{
                rfidHandler.onCreate(this);
            }

        }else{
            rfidHandler.onCreate(this);
        }

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String result = rfidHandler.Defaults();
//                statusText.setText(result);
                totalTag.clear();
                sb.clear();
                tags.clear();
                totalReads.setText("0");
                uniqueTags.setText("0");
                tagRecyclerAdapter.notifyDataSetChanged();
            }
        });
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
                return true;
            case R.id.findLocation:
                intent = new Intent(this,LocateTagActivity.class);
                startActivity(intent);
                return true;
            case R.id.home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    public void onFindWorkOrder(View view){
//        try{
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            String workID = editTextWorkOrderID.getText().toString();
//            //Update the digest with the input bytes
//            md.update(workID.getBytes());
//
//            //Calculate the MD5 hash
//            byte[] hashBytes = md.digest();
//            //Convert the hash bytes to a hexadecimal string
//            StringBuilder hexString = new StringBuilder();
//            for (byte b : hashBytes) {
//                String hex = String.format("%02x", b);
//                hexString.append(hex);
//            }
//            String md5hash = hexString.toString();
//            System.out.println("MD5 Hash: " + md5hash);
//
//            String result_tagID = String.format("%s%s",md5hash.substring(0,md5hash.length()-workID.length()),workID);
//            //Create Work Order object
//            DateTimeFormatter dtf = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//                LocalDateTime now = LocalDateTime.now();
//                String currentDate= dtf.format(now);
//                WorkOrder workOrder = new WorkOrder(workID, currentDate);
//                System.out.println(dtf.format(now));
//            }
//
//
//            //Pass the value to locate tag activity
//            Intent intent = new Intent(this,LocateTagActivity.class);
//            intent.putExtra("Work Order ID",result_tagID);
//            startActivity(intent);
//        } catch (NoSuchAlgorithmException e){
//            e.printStackTrace();
//        }
//    }



    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                rfidHandler.onCreate(this);
            }
            else {
                Toast.makeText(this, "Bluetooth Permissions not granted", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
        rfidHandler.onPause();
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        String status = rfidHandler.onResume();
        rfidStatus.setText(status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rfidHandler.onDestroy();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void handleTagdata(TagData[] tagData) {
//        final List<Tag> sb = new ArrayList<>();
        synchronized (sb){
        for (int index = 0; index < tagData.length; index++) {
//            String orderID;
//            orderID = Base64.getEncoder().encodeToString(tagData[index].getTagID().getBytes());
            Tag newTag = new Tag(tagData[index].getTagID(),String.valueOf(tagData[index].getPeakRSSI()));
            sb.add(newTag);
            if (sb.size() > 15) {
                sb.remove(0); // Remove the oldest tag
            }
//            tags.add(tagData[index].getTagID()+"\n");
        }

        System.out.println("sb length:"+ sb.size());
        //Make a copy of the 'tags' list to avoid concurrent modification exception
        List<Tag> tagsCopy = new ArrayList<>(sb);
        if (sb.size()==15){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                tags.addAll(sb);

                totalTag.addAll(sb);
                totalReads.setText(String.valueOf(totalTag.size()));
                List<String> uniqueTagsList = TagRecyclerAdapter.findUniqueTags(tagsCopy);
                List<Tag> finalTagsList = new ArrayList<>();
                for (String tagID: uniqueTagsList){
                    System.out.println("tagID: "+tagID);
                    String formattedNumberRSSI = decimalFormat.format(TagRecyclerAdapter.calculateAvgRSSI(tagsCopy,tagID));
//                    double avgRSSI = Double.parseDouble(formattedNumberRSSI);
//                    System.out.println("avg RSSI:" + String.valueOf(avgRSSI));
                    String formattedNumberDistance = decimalFormat.format(TagRecyclerAdapter.distanceAlgorithm2(tagsCopy,tagID));
//                    double avgDistance = Double.parseDouble(formattedNumberDistance);
                    Tag uniqueTag = new Tag(tagID, formattedNumberRSSI,formattedNumberDistance);
                    finalTagsList.add(uniqueTag);
                }

                tags.clear();
                tags.addAll(finalTagsList);
                uniqueTags.setText(String.valueOf(tagRecyclerAdapter.CountUniqueTags(tags)));
                tagRecyclerAdapter.notifyDataSetChanged();
                tagRecyclerAdapter.updateTagListFull(tags);


                    }

        }
                );}
    }}
    @Override
    public void handleTriggerPress(boolean pressed) {
        if (pressed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            rfidHandler.performInventory();
        } else
            rfidHandler.stopInventory();
    }
}
package com.example.rfid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.zebra.rfid.api3.*;

import com.example.rfid.databinding.ActivityLocateTagBinding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocateTagActivity extends AppCompatActivity implements RFIDHandler.ResponseHandlerInterface{

    private AppBarConfiguration appBarConfiguration;
    private ActivityLocateTagBinding binding;
    public TextView ReaderConnectionText;
    private TextView tagID;
    private TextView rssiToDistance;
    private String receivedValue;
    private RFIDHandler rfidHandler;
    private EditText editTextWorkOrderID;
    private Button submitButton;
    List <String> tags = new ArrayList<>();
    ArrayAdapter<String> tagArrayAdapter;
    AutoCompleteTextView autoCompleteTextView;
    private String itemSelected;
    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 100;
    private List<Tag> sb = new ArrayList<>();
    ProgressBar progressBarCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_locate_tag);

        progressBarCustom = (ProgressBar) findViewById(R.id.progressBar);
        progressBarCustom.setProgress(0);

        ReaderConnectionText = findViewById(R.id.ReaderConnectionText);
        rssiToDistance = findViewById(R.id.rssiToDistance);
        tagID = findViewById(R.id.tagID);
        editTextWorkOrderID = findViewById(R.id.editTextWorkOrderID);
        submitButton = findViewById(R.id.submitButton);

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        tagArrayAdapter= new ArrayAdapter<String>(this,R.layout.drop_down_menu_item,tags);
        autoCompleteTextView.setAdapter(tagArrayAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,View view, int i,long l){
                itemSelected = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(LocateTagActivity.this, "Tag: " + itemSelected, Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = getIntent();
        if (intent.hasExtra("Work Order ID")){
            receivedValue = intent.getStringExtra("Work Order ID");
            tagID.setText(receivedValue);

        }


        rfidHandler = new RFIDHandler();
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

    }
    public void onFindWorkOrder(View view){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            String workID = editTextWorkOrderID.getText().toString();
            //Update the digest with the input bytes
            md.update(workID.getBytes());

            //Calculate the MD5 hash
            byte[] hashBytes = md.digest();
            //Convert the hash bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }
            String md5hash = hexString.toString();
            System.out.println("MD5 Hash: " + md5hash);

            String result_tagID = String.format("%s%s",md5hash.substring(0,md5hash.length()-workID.length()),workID);
            //Create Work Order object
            DateTimeFormatter dtf = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String currentDate= dtf.format(now);
                WorkOrder workOrder = new WorkOrder(workID, currentDate);
                System.out.println(dtf.format(now));
            }
            receivedValue = result_tagID;
            tagID.setText(receivedValue);


            //Pass the value to locate tag activity
//            Intent intent = new Intent(this,LocateTagActivity.class);
//            intent.putExtra("Work Order ID",result_tagID);
//            startActivity(intent);
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
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
        ReaderConnectionText.setText(status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rfidHandler.onDestroy();
    }

    private List<String> findUniqueTags(List<Tag>tags){
        List<String> uniqueTags = new ArrayList<>();
        for (Tag tag : tags){
            if (!uniqueTags.contains(tag.getTagID())){
                System.out.println("unique tag id: "+tag.getTagID());
                uniqueTags.add(tag.getTagID());}
        }
        return uniqueTags;
    }
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void handleTagdata(TagData[] tagData) {
//        final StringBuilder sb = new StringBuilder();
        synchronized (sb){
        String search_tagID = "";
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        for (int index = 0; index < tagData.length; index++) {

            Tag newTag = new Tag(tagData[index].getTagID(),String.valueOf(tagData[index].getPeakRSSI()));
            String tagID_lc = tagData[index].getTagID().toLowerCase(Locale.getDefault());
            sb.add(newTag);
            if (sb.size() > 15) {
                sb.remove(0); // Remove the oldest tag
            }
            if (receivedValue !=null){
                if (tagID_lc.contains(receivedValue.toLowerCase(Locale.getDefault()))){
                    search_tagID= tagData[index].getTagID();
                }}
            if (itemSelected != null){
                if (tagID_lc.contains(itemSelected.toLowerCase(Locale.getDefault()))){
                    search_tagID = tagData[index].getTagID();}}

        }
        System.out.println("sb length:"+ sb.size());
        final String finalTagID = search_tagID;
        System.out.println("final search tag id: "+ finalTagID);
        List<Tag> tagsCopy = new ArrayList<>(sb);
        if (sb.size()==15){
                String finalSearch_tagID = search_tagID;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                        tags.clear();
                        tags.addAll(TagRecyclerAdapter.findUniqueTags(tagsCopy));
                        tagArrayAdapter.notifyDataSetChanged();
                        Double doubleDistance = TagRecyclerAdapter.distanceAlgorithm2(tagsCopy,finalSearch_tagID);
                        String formattedNumberDistance = decimalFormat.format(doubleDistance);
                        tagID.setText(finalSearch_tagID);
                        if (Double.isNaN(TagRecyclerAdapter.distanceAlgorithm2(tagsCopy,finalSearch_tagID))){
                            rssiToDistance.setText("N/A");
                        }
                        else{
                            rssiToDistance.setText(formattedNumberDistance+"m");
                            Integer Percentage = (int) Math.round(((1-Double.parseDouble(formattedNumberDistance))/1)*100);
                            progressBarCustom.setProgress(Percentage);
                            if (doubleDistance < 0.15){
                                rfidHandler.beepReader();
                            }
                        }}
                     catch (com.zebra.rfid.api3.OperationFailureException e) {
                        // Handle OperationFailureException here
                        e.printStackTrace();
                    } catch (com.zebra.rfid.api3.InvalidUsageException e) {
                        // Handle InvalidUsageException here
                        e.printStackTrace();
                    }}

        });
        }}
    }
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
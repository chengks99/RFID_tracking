package com.example.rfid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.zebra.rfid.api3.*;

import com.example.rfid.databinding.ActivityMainBinding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RFIDHandler.ResponseHandlerInterface{

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    public TextView ReaderConnectionText;
    private TextView tagID;
    private TextView rssiValue;
    private TextView rssiToDistance;
    private String receivedValue;
    static Double receivedParams;
    static Double receivedRSSI1meter;
    static Double receivedRSSI50cm;
    private RFIDHandler rfidHandler;
    private EditText editTextWorkOrderID;
    private ImageView circleView;
    private Button submitButton;
    private Button fiftySaveButton;
    private Button meterSaveButton;
    private TextView tagStatusMsg;
    private Bundle senderBundle = new Bundle();
    private Bundle workOrderBundle = new Bundle();
    List <String> tags = new ArrayList<>();
    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 100;
    private List<Tag> sb = new ArrayList<>();
//    ProgressBar progressBarCustom;
    //    private beepMultiThread beepThread;
    private MediaPlayer mp;
    private SoundPool soundPool;
    private boolean isPlaying = false;
    private boolean isEntered = false;
    private int sbMaxLength=200;
    private boolean isTriggerPressed = false;
    final int[] notFoundCount = {0};
    private List<Double> rssiWindow = new ArrayList<Double>();
    public ArrayList<WorkOrder> items= new ArrayList<WorkOrder>();
    private static final int RFID_STATUS_CHECK_INTERVAL = 5000;
    private InputMethodManager inputManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        inputManager =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

//        progressBarCustom = (ProgressBar) findViewById(R.id.progressBar);
//        progressBarCustom.setProgress(0);

        ReaderConnectionText = findViewById(R.id.ReaderConnectionText);
        rssiToDistance = findViewById(R.id.rssiToDistance);
        tagStatusMsg = findViewById(R.id.tagStatusMsg);
        tagID = findViewById(R.id.tagID);
        rssiValue = findViewById(R.id.rssiValue);
        editTextWorkOrderID = findViewById(R.id.editTextWorkOrderID);
        submitButton = findViewById(R.id.submitButton);
        fiftySaveButton = findViewById(R.id.fiftySaveBtn);
        meterSaveButton = findViewById(R.id.meterSaveBtn);
        circleView = findViewById(R.id.CircleView);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            receivedParams = bundle.getDouble("n");
            receivedRSSI1meter = bundle.getDouble("rssi1Meter");
            System.out.println("received Params: "+ String.valueOf(receivedParams));
            System.out.println("received rssi at 1 meter: "+ String.valueOf(receivedRSSI1meter));
            Toast.makeText(this, "n set to: "+String.valueOf(receivedParams), Toast.LENGTH_LONG).show();
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
        startRFIDStatusCheckTimer();
    }

    private void startRFIDStatusCheckTimer() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("Checking rfid status", String.valueOf(rfidHandler.isReaderConnected()));
                checkRFIDStatus();
                // Repeat the check after the specified interval
                startRFIDStatusCheckTimer();
            }
        }, RFID_STATUS_CHECK_INTERVAL);
    }

    public void onFindWorkOrder(View view){
        try{
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
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
                items.add(workOrder);
                System.out.println(dtf.format(now));

                workOrderBundle.putParcelableArrayList("workOrders",items);


            }

            receivedValue = result_tagID;
//        receivedValue = workID;
        tagID.setText(receivedValue);
        isEntered= true;

    } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
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
                if (!workOrderBundle.isEmpty()){
                    intent.putExtras(workOrderBundle);
                }
                startActivity(intent);
                finish();
                return true;
            case R.id.settings:
                intent = new Intent(this,SettingsActivity.class);
                if (!senderBundle.isEmpty()){
                    intent.putExtras(senderBundle);
                }
                startActivity(intent);
                finish();
                return true;
            case R.id.home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnSave50cmButtonPressed(View view){
        if (rssiValue.getText() != ""){
            String rssiNumbersOnly = String.valueOf(rssiValue.getText());
            String numericPart = rssiNumbersOnly.replaceAll("[^0-9.-]", "");
            receivedRSSI50cm = Double.parseDouble(numericPart);
            Toast.makeText(this, "Saved 50cm RSSI: "+ receivedRSSI50cm.toString(), Toast.LENGTH_SHORT).show();}
        calculateN();
    }

    public void OnSave1mButtonPressed(View view){
        if (rssiValue.getText()!=""){
            String rssiNumbersOnly = String.valueOf(rssiValue.getText());
            String numericPart = rssiNumbersOnly.replaceAll("[^0-9.-]", "");
            receivedRSSI1meter = Double.parseDouble(numericPart);
            Toast.makeText(this, "Saved 1m RSSI: "+ receivedRSSI1meter.toString(),Toast.LENGTH_SHORT).show();
        }
        calculateN();
    }

    private double calculateN(){
        double n=0.21299;
        if (receivedRSSI1meter == null && receivedRSSI50cm != null) {
            n = Math.log10(receivedRSSI50cm / -51) / Math.log10(0.5);
            receivedRSSI1meter = -51.0;

        } else if (receivedRSSI50cm == null && receivedRSSI1meter != null) {
            n = Math.log10(-44 / receivedRSSI1meter) / Math.log10(0.5);
            receivedRSSI50cm = -44.0;
        } else if (receivedRSSI1meter != null && receivedRSSI50cm != null) {
            System.out.println("received RSSI at 50cm: " + String.valueOf(receivedRSSI50cm));
            System.out.println("received RSSI at 1m: " + String.valueOf(receivedRSSI1meter));
            n = Math.log10(receivedRSSI50cm / receivedRSSI1meter) / Math.log10(0.5);}
        Toast.makeText(this, "n set to: " + String.valueOf(n), Toast.LENGTH_SHORT).show();
        receivedParams = n;
        senderBundle.putDouble("rssi1Meter",receivedRSSI1meter*-1);
        senderBundle.putDouble("rssi50cm", receivedRSSI50cm*-1);
//        bundle.putDouble("n",n);
//        Log.d("Calculated N:",String.valueOf(n));
//        Intent senderIntent = new Intent(this, SettingsActivity.class);
//        senderIntent.putExtras(senderBundle);
        return n;
    }


    private void playMPSound(boolean isFast){
        if (!isPlaying && isTriggerPressed) {
            if (isFast){
                mp = MediaPlayer.create(this, R.raw.fast_beep);}
            else{
                mp = MediaPlayer.create(this, R.raw.medium_beep2);
            }
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                    isPlaying = false;
                }
            });
            mp.start();

            isPlaying = true;
        }
    }

    private void stopBeepSound() {
        if (mp != null && isPlaying) {
//            mp.stop();
            mp.release();
            isPlaying = false;
        }
        if (soundPool!=null && isPlaying){
//            soundPool.stop();
            soundPool.release();
            isPlaying = false;
        }
    }

    private double calculateAvgRSSIWindow(List<Double> rssiWindow){
        double sum=0;
        int count=0;
        for (Double rssi:rssiWindow){
            sum += rssi;
            count+=1;
        }
        return sum/count;
    }

    private void addToRSSIWindow(List<Double> rssiWindow, Double newRSSI){
        if (rssiWindow.size()==10){
            System.out.println("removing old rssi:"+String.valueOf(rssiWindow.get(0)));
            rssiWindow.remove(0);
            rssiWindow.add(newRSSI);
        }
        else{
            rssiWindow.add(newRSSI);
        }
    }

    private void checkRFIDStatus() {
        boolean isReaderConnected = false;

        try {
            // Attempt to get the RFID connection status
            isReaderConnected = rfidHandler.isReaderConnected();

            boolean finalIsReaderConnected = isReaderConnected;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!finalIsReaderConnected) {
                        ReaderConnectionText.setText("RFID Reader not connected");
                        // Add any other UI updates or actions you want when the reader is not connected
                    }
                }
            });
        } catch (Exception e) {
            Log.e("RFIDStatusCheck", "Error checking RFID status", e);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ReaderConnectionText.setText("Error! Please check the battery");
                }
            });
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void handleTagdata(TagData[] tagData) {
//        final StringBuilder sb = new StringBuilder();
//        List<Tag> sb = new ArrayList<>();
        synchronized (sb){
//            checkRFIDStatus();
            String search_tagID = "";
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            for (int index = 0; index < tagData.length; index++) {
                if (tagData[index].getTagID() != null){
                    Tag newTag = new Tag(tagData[index].getTagID(),String.valueOf(tagData[index].getPeakRSSI()));
                    String tagID_lc = tagData[index].getTagID().toLowerCase(Locale.getDefault());
                    sb.add(newTag);
                    if (sb.size() > sbMaxLength) {
                        sb.remove(0);// Remove the oldest tag
    //                    sb.subList(0,10).clear();
                    }
                    if (receivedValue !=null){
                        if (tagID_lc.contains(receivedValue.toLowerCase(Locale.getDefault()))){
                            search_tagID= tagData[index].getTagID();
                        }}

            }}
            System.out.println("sb length:"+ sb.size());
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .build();
//
//            soundPool = createSoundPool();
            List<Tag> tagsCopy = new ArrayList<>(sb);

//            if (sb.size()==sbMaxLength){
            String finalSearch_tagID = search_tagID;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        tags.clear();
//                        tags.addAll(TagRecyclerAdapter.findUniqueTags(tagsCopy));
//                        tagArrayAdapter.notifyDataSetChanged();
                        if (!finalSearch_tagID.equals("")){
                            notFoundCount[0] = 0;
                            System.out.println("final search tag id: "+ finalSearch_tagID);
                            Double rssi =TagRecyclerAdapter.calculateAvgRSSI(tagsCopy,finalSearch_tagID);
                            System.out.println("RSSI adding to window: "+String.valueOf(rssi));
                            addToRSSIWindow(rssiWindow,rssi);
                            circleView.setImageResource(R.drawable.check);
                            tagStatusMsg.setText("Tag Found");
                            Double avgRSSI = calculateAvgRSSIWindow(rssiWindow);
                            Double doubleDistance;
                            if (receivedParams!=null){
                                doubleDistance = TagRecyclerAdapter.distanceAlgorithm3(tagsCopy,avgRSSI, finalSearch_tagID,receivedParams,receivedRSSI1meter);
                                System.out.println("received Params is " + String.valueOf(receivedParams));
                            }
                            else{
                                doubleDistance= TagRecyclerAdapter.distanceAlgorithm3(tagsCopy,avgRSSI, finalSearch_tagID);}
//                            Double doubleDistance = TagRecyclerAdapter.avgDistanceWindow(finalSearch_tagID);
                            String formattedNumberDistance = decimalFormat.format(doubleDistance);
                            tagID.setText(finalSearch_tagID);
                            if (!Double.isNaN(avgRSSI)){
                                rssiValue.setText("RSSI:"+decimalFormat.format(avgRSSI));}
                            if (Double.isNaN(doubleDistance)){
                                rssiToDistance.setText("N/A");
                            }
                            if (!formattedNumberDistance.equals("-99.99")){
                                rssiToDistance.setText(formattedNumberDistance);
//                                Integer Percentage = (int) Math.round(((1-doubleDistance/3.0)*100));
//                                Log.d("percentage:",String.valueOf(Percentage));

//                                progressBarCustom.setProgress(Percentage);
                                if (doubleDistance<0.5){
                                    playMPSound(true);
//                                    playBeepSound(soundPool,1.5f,100);
                                }
                                else{
                                    playMPSound(false);
//                                    playBeepSound(soundPool,0.5f, 200);
                                }
                        }}
                        else{
                            Log.d("notFoundCount:",String.valueOf(notFoundCount[0]));

                            if (notFoundCount[0]>=50){
                                Log.e("Not found","Tag not found 50 times");
                            rssiToDistance.setText("");
                            rssiValue.setText("");
                            circleView.setImageResource(R.drawable.delete);
                            tagStatusMsg.setText("Tag not detected! Please adjust direction or move");
//                            progressBarCustom.setProgress(0);
                            notFoundCount[0]=0;
                            }
                            else{
                                notFoundCount[0] +=1;
                            }
                        }
                    }

                });
//            }
        }
    }
    @Override
    public void handleTriggerPress(boolean pressed) {
        isTriggerPressed = pressed;
        if (pressed && isEntered) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            rfidHandler.performInventory();
        } else{
            rfidHandler.stopInventory();
            stopBeepSound();}
    }
}
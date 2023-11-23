package com.example.rfid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    TextView rssiAt1m;
    TextView rssiAt50cm;
    public static double rssi1Meter=51;
    public static double rssi50=44;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        rssiAt1m = findViewById(R.id.rssi1Meter);
        rssiAt50cm = findViewById(R.id.rssi50cm);
//        rssiAt1m.setText(String.valueOf(rssi1Meter));
//        rssiAt50cm.setText(String.valueOf(rssi50));

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            rssi1Meter = bundle.getDouble("rssi1Meter");
            rssi50 = bundle.getDouble("rssi50cm");
            rssiAt1m.setText(String.valueOf(rssi1Meter));
            rssiAt50cm.setText(String.valueOf(rssi50));}
        else{
            rssiAt1m.setText(String.valueOf(rssi1Meter));
            rssiAt50cm.setText(String.valueOf(rssi50));
//            System.out.println("received Params: "+ String.valueOf(receivedParams));
//            System.out.println("received rssi at 1 meter: "+ String.valueOf(receivedRSSI1meter));
//            Toast.makeText(this, "n set to: "+String.valueOf(receivedParams), Toast.LENGTH_LONG).show();
        }
    }

    public void calculateN(View view){
        String negRSSI1Meter = "-"+rssiAt1m.getText().toString();
        String negRSSI50 = "-"+rssiAt50cm.getText().toString();
        rssi1Meter = Double.parseDouble(rssiAt1m.getText().toString());
        rssi50 = Double.parseDouble(rssiAt50cm.getText().toString());
        double n = Math.log10(rssi50/rssi1Meter)/Math.log10(0.5);
        rssiAt1m.setText(String.valueOf(rssi1Meter));
        rssiAt50cm.setText(String.valueOf(rssi50));

//        return n;
        Bundle bundle = new Bundle();
        bundle.putDouble("rssi1Meter",Double.parseDouble(negRSSI1Meter));
        bundle.putDouble("n",n);
        Log.d("Calculated N:",String.valueOf(n));
        Intent senderIntent = new Intent(this, MainActivity.class);
        senderIntent.putExtras(bundle);
        startActivity(senderIntent);
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
        rssiAt1m.setText(String.valueOf(rssi1Meter));
        rssiAt50cm.setText(String.valueOf(rssi50));
    }
}
package com.example.rfid;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.zebra.rfid.api3.ACCESS_OPERATION_CODE;
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS;
import com.zebra.rfid.api3.Antennas;
import com.zebra.rfid.api3.BEEPER_VOLUME;
import com.zebra.rfid.api3.ENUM_TRANSPORT;
import com.zebra.rfid.api3.ENUM_TRIGGER_MODE;
import com.zebra.rfid.api3.HANDHELD_TRIGGER_EVENT_TYPE;
import com.zebra.rfid.api3.INVENTORY_STATE;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.ReaderManagement;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.api3.RfidEventsListener;
import com.zebra.rfid.api3.RfidReadEvents;
import com.zebra.rfid.api3.RfidStatusEvents;
import com.zebra.rfid.api3.SESSION;
import com.zebra.rfid.api3.SL_FLAG;
import com.zebra.rfid.api3.START_TRIGGER_TYPE;
import com.zebra.rfid.api3.STATUS_EVENT_TYPE;
import com.zebra.rfid.api3.STOP_TRIGGER_TYPE;
import com.zebra.rfid.api3.TagData;
import com.zebra.rfid.api3.TagDataArray;
import com.zebra.rfid.api3.TriggerInfo;
import android.media.AudioAttributes;

import java.io.Serializable;
import java.util.ArrayList;

public class RFIDHandler implements Readers.RFIDReaderEventHandler {

    final static String TAG = "RFID_SAMPLE";
    // RFID Reader
    private static Readers readers;
    private static Readers locateTagReaders;
    private static ArrayList<ReaderDevice> availableRFIDReaderList;
    private static ReaderDevice readerDevice;
    private static RFIDReader reader;
    private EventHandler eventHandler;
    // UI and context
    TextView textView;
    private MainActivity context;
//    private LocateTagActivity locateTagcontext;
    // general
    private int MAX_POWER = 300;
    private int MAX_SENSITIVTY;
    private ReaderManagement readerManager;

    // In case of RFD8500 change reader name with intended device below from list of paired RFD8500
    String readerName = "RFD4031-G10B700-JP";

    void onCreate(MainActivity activity) {
        // application context
        context = activity;
        // Status UI
        textView = activity.ReaderConnectionText;
        readerManager = new ReaderManagement();
        InitSDK();

    }

//    void onCreate(LocateTagActivity activity){
//        locateTagcontext = activity;
//        textView= activity.ReaderConnectionText;
//        InitSDK();
//    }


    public boolean isReaderConnected() {
        if (reader != null && reader.isConnected())
            return true;
        else {
            Log.d(TAG, "reader is not connected");
//            connectReader();
            return false;
        }
    }

    //
    //  Activity life cycle behavior
    //

    String onResume() {
        return connect();
    }

    void onPause() {
        disconnect();
    }

    void onDestroy() {
        dispose();
    }



    //
    // RFID SDK
    //
    private void InitSDK() {
        Log.d(TAG, "InitSDK");
        if (readers == null) {
            new CreateInstanceTask().execute();
        } else
            connectReader();
    }

    // Enumerates SDK based on host device
    private class CreateInstanceTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "CreateInstanceTask");
            InvalidUsageException invalidUsageException = null;
            try {
                // Based on support available on the host device, choose the reader type
                if (context != null) {
                    readers = new Readers(context, ENUM_TRANSPORT.ALL);
                }
                try {
                    availableRFIDReaderList = readers.GetAvailableRFIDReaderList();
                } catch (InvalidUsageException e) {
                    e.printStackTrace();
                }
                if (invalidUsageException != null) {
                    readers.Dispose();
                    readers = null;
                    if (readers == null) {
                        readers = new Readers(context, ENUM_TRANSPORT.BLUETOOTH);
                    }
                }
                return null;
            } finally {

            }
        }
//        protected Void doInBackground(Void... voids) {
//            Log.d(TAG, "CreateInstanceTask");
//            // Based on support available on host device choose the reader type
//            InvalidUsageException invalidUsageException = null;
//            if (context!=null){
//                readers = new Readers(context, ENUM_TRANSPORT.ALL);
//            }
////            if (locateTagcontext!=null){
////                locateTagReaders = new Readers(locateTagcontext, ENUM_TRANSPORT.ALL);}
//            try {
//                availableRFIDReaderList = readers.GetAvailableRFIDReaderList();
//
//            } catch (InvalidUsageException e) {
//                e.printStackTrace();
//            }
//            if (invalidUsageException != null) {
//                readers.Dispose();
//                readers = null;
//                if (readers == null) {
//                    readers = new Readers(context, ENUM_TRANSPORT.BLUETOOTH);
//                }
//            }
//            return null;
//        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            connectReader();
        }
    }
    private synchronized void connectReader(){
        if(!isReaderConnected()){
            new ConnectionTask().execute();
        }
    }

    private class ConnectionTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Log.d(TAG, "ConnectionTask");
            GetAvailableReader();
            if (reader != null){
//                try {
//                    readerManager.getReaderInfo();
//                } catch (InvalidUsageException e) {
//                    throw new RuntimeException(e);
//                } catch (OperationFailureException e) {
//                    throw new RuntimeException(e);
//                }
                return connect();
            }
            return "Failed to find or connect reader";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            textView.setText(result);
        }
    }

    private synchronized void GetAvailableReader() {
        Log.d(TAG, "GetAvailableReader");
        if (readers != null) {
            readers.attach(this);
            try {
                if (readers.GetAvailableRFIDReaderList() != null) {
                    availableRFIDReaderList = readers.GetAvailableRFIDReaderList();
                    if (availableRFIDReaderList.size() != 0) {
                        // if single reader is available then connect it
                        if (availableRFIDReaderList.size() == 1) {
                            readerDevice = availableRFIDReaderList.get(0);
                            reader = readerDevice.getRFIDReader();
                        } else {
                            // search reader specified by name
                            for (ReaderDevice device : availableRFIDReaderList) {
                                if (device.getName().equals(readerName)) {
                                    readerDevice = device;
                                    reader = readerDevice.getRFIDReader();
                                }
                            }
                        }
                    }
                }
            }catch (InvalidUsageException ie){

            }
        }
    }

    // handler for receiving reader appearance events
    @Override
    public void RFIDReaderAppeared(ReaderDevice readerDevice) {
        Log.d(TAG, "RFIDReaderAppeared " + readerDevice.getName());
        connectReader();
    }

    @Override
    public void RFIDReaderDisappeared(ReaderDevice readerDevice) {
        Log.d(TAG, "RFIDReaderDisappeared " + readerDevice.getName());
        if (readerDevice.getName().equals(reader.getHostName()))
            disconnect();
    }

    public String GetHostName(){
        if (reader!=null){
            return "Connected: " + reader.getHostName();
        }
        else{
            return "Error finding reader";
        }
    }

    private synchronized String connect() {
        if (reader != null) {
            Log.d(TAG, "connect " + reader.getHostName());
            try {
                if (!reader.isConnected()) {
                    // Establish connection to the RFID Reader
                    reader.connect();
                    ConfigureReader();
                    if (reader !=null){
                    if(reader.isConnected()){
                        return "Connected: " + reader.getHostName();
                    }}
                }
            } catch (InvalidUsageException e) {
                e.printStackTrace();
            } catch (OperationFailureException e) {
                e.printStackTrace();
                Log.d(TAG, "OperationFailureException " + e.getVendorMessage());
                String des = e.getResults().toString();
                if (e instanceof OperationFailureException) {
                    OperationFailureException operationFailureException = (OperationFailureException) e;
                    if (operationFailureException.getStatusDescription().equals("Response timeout")) {
                        // API command timeout occurred, dispose of the reader and reconnect
                        dispose();
                        connectReader();
                    }
                }
                return "Connection failed" + e.getVendorMessage() + " " + des;
            }
        }
        return "";
    }


    private void ConfigureReader() {
//        Log.d(TAG, "ConfigureReader " + reader.getHostName());
        if (reader.isConnected()) {
            TriggerInfo triggerInfo = new TriggerInfo();
            //handheld trigger part
            triggerInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
            triggerInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE);
            try {
                // receive events from reader
                if (eventHandler == null)
                    eventHandler = new EventHandler();
                reader.Events.addEventsListener(eventHandler);
                // HH event
                // handheld trigger part
                reader.Events.setHandheldEvent(true);
                // tag event with tag data
                reader.Events.setTagReadEvent(true);
                reader.Events.setAttachTagDataWithReadEvent(false);
                // set trigger mode as rfid so scanner beam will not come
                reader.Config.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE, true);
                // set start and stop triggers
                reader.Config.setStartTrigger(triggerInfo.StartTrigger);
                reader.Config.setStopTrigger(triggerInfo.StopTrigger);
                Log.e("reader time out",String.valueOf(reader.getTimeout()));
                //set beeper volume
                reader.Config.setBeeperVolume(BEEPER_VOLUME.QUIET_BEEP);

                // power levels are index based so maximum power supported get the last one
                MAX_POWER = reader.ReaderCapabilities.getTransmitPowerLevelValues().length - 1;
                MAX_SENSITIVTY =-65;
                // set antenna configurations
                Antennas.AntennaRfConfig config = reader.Config.Antennas.getAntennaRfConfig(1);
                config.setTransmitPowerIndex(MAX_POWER);
                config.setrfModeTableIndex(0);
                config.setTari(0);
                reader.Config.Antennas.setAntennaRfConfig(1, config);
                // Set the singulation control
                Antennas.SingulationControl s1_singulationControl = reader.Config.Antennas.getSingulationControl(1);
                s1_singulationControl.setSession(SESSION.SESSION_S0);
                s1_singulationControl.Action.setInventoryState(INVENTORY_STATE.INVENTORY_STATE_A);
                s1_singulationControl.Action.setSLFlag(SL_FLAG.SL_ALL);
                reader.Config.Antennas.setSingulationControl(1, s1_singulationControl);
                // delete any prefilters
                reader.Actions.PreFilters.deleteAll();
                //
                //Print out values
                System.out.println("transmit power: "+config.getTransmitPowerIndex());
                System.out.println("sensitivty: " + config.getReceiveSensitivityIndex());

            } catch (InvalidUsageException | OperationFailureException e) {
                e.printStackTrace();
                if (e instanceof OperationFailureException) {
                    OperationFailureException operationFailureException = (OperationFailureException) e;
                    Log.e("operationFailureException.getStatusDescription()", operationFailureException.getStatusDescription());
                    if (operationFailureException.getStatusDescription().equals("RFID_API_COMMAND_TIMEOUT")) {
                        // API command timeout occurred, dispose of the reader and reconnect
                        Log.e("Dispose and connect after api timeout","success");
                        dispose();
                        connectReader();
                    }
                }
            }
        }
    }

    private synchronized void disconnect() {
        Log.d(TAG, "disconnect " + reader);
        try {
            if (reader != null) {
                reader.Events.removeEventsListener(eventHandler);
                reader.disconnect();
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("Disconnected");
                    }

                });
//                locateTagcontext.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        textView.setText("Disconnected");
//                    }
//
//                });
            }
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void dispose() {
        try {
            if (readers != null) {
                reader = null;
                readers.Dispose();
                readers = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void performInventory() {
        // check reader connection
        if (!isReaderConnected())
            return;
        try {
            reader.Actions.Inventory.perform();
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
            OperationFailureException operationFailureException = (OperationFailureException) e;
            Log.e("operationFailureException.getStatusDescription()", operationFailureException.getStatusDescription());
            if (operationFailureException.getStatusDescription().equals("RFID_API_COMMAND_TIMEOUT")) {
                // API command timeout occurred, dispose of the reader and reconnect
                Log.e("Dispose and connect after api timeout","success");
                dispose();
                connectReader();
            }
        }
    }

    synchronized void stopInventory() {
        // check reader connection
        if (!isReaderConnected())
            return;
        try {
            reader.Actions.Inventory.stop();
        } catch (InvalidUsageException e) {
            e.printStackTrace();
        } catch (OperationFailureException e) {
            e.printStackTrace();
            OperationFailureException operationFailureException = (OperationFailureException) e;
            Log.e("operationFailureException.getStatusDescription()", operationFailureException.getStatusDescription());
            if (operationFailureException.getStatusDescription().equals("RFID_API_COMMAND_TIMEOUT")) {
                // API command timeout occurred, dispose of the reader and reconnect
                Log.e("Dispose and connect after api timeout","success");
                dispose();
                connectReader();
            }
        }
    }

    // Read/Status Notify handler
    // Implement the RfidEventsLister class to receive event notifications
    public class EventHandler implements RfidEventsListener {
        // Read Event Notification
        public void eventReadNotify(RfidReadEvents e) {
            // Recommended to use new method getReadTagsEx for better performance in case of large tag population
//            TagDataArray listTags = reader.Actions.getReadTagsEx(100);
//            TagData[] myTags = listTags.getTags();
            TagData[] myTags= reader.Actions.getReadTags(100);
            if (myTags != null) {
                for (int index = 0; index < myTags.length; index++) {
                    Log.d(TAG, "Tag ID " + myTags[index].getTagID());

                    myTags[index].getPeakRSSI();
                    if (myTags[index].getOpCode() == ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ &&
                            myTags[index].getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS) {
                        if (myTags[index].getMemoryBankData().length() > 0) {
                            Log.d(TAG, " Mem Bank Data " + myTags[index].getMemoryBankData());
                        }
                    }
                    if (myTags[index].isContainsLocationInfo()) {
                        short dist = myTags[index].LocationInfo.getRelativeDistance();
                        Log.d(TAG, "Tag relative distance " + dist);
                    }

                }
                // possibly if operation was invoked from async task and still busy
                // handle tag data responses on parallel thread thus THREAD_POOL_EXECUTOR
                new AsyncDataUpdate().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myTags);
            }
        }



        // Status Event Notification
        public void eventStatusNotify(RfidStatusEvents rfidStatusEvents) {
            Log.d(TAG, "Status Notification: " + rfidStatusEvents.StatusEventData.getStatusEventType());
            if (rfidStatusEvents.StatusEventData.getStatusEventType() == STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT) {
                if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            if (context!=null){
                                context.handleTriggerPress(true);}
//                            if (locateTagcontext != null) {
//                                locateTagcontext.handleTriggerPress(true);
//                            }
                            return null;
                        }
                    }.execute();
                }
                if (rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            if (context != null){
                             context.handleTriggerPress(false);}
//                            if (locateTagcontext != null) {
//                                locateTagcontext.handleTriggerPress(false);
//                            }
                            return null;
                        }
                    }.execute();
                }
            }
        }
    }

    private class AsyncDataUpdate extends AsyncTask<TagData[], Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(TagData[]... params) {
            if (context!=null){
                context.handleTagdata(params[0]);}
//            if (locateTagcontext!=null){
//                locateTagcontext.handleTagdata(params[0]);
//            }
            return null;
        }
    }

    interface ResponseHandlerInterface {
        void handleTagdata(TagData[] tagData);

        void handleTriggerPress(boolean pressed);
        //void handleStatusEvents(Events.StatusEventData eventData);
    }

}
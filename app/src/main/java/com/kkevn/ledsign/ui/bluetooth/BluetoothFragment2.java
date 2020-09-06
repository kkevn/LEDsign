package com.kkevn.ledsign.ui.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kkevn.ledsign.R;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class BluetoothFragment2 extends Fragment {

    // GUI Components
    private TextView mBluetoothStatus;
    private TextView mReadBuffer;
    private Button mScanBtn;
    private Button mOffBtn;
    private Button mListPairedDevicesBtn;
    private Button mDiscoverBtn;
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayAdapter<String> mBTArrayAdapter;
    private ListView mDevicesListView;
    private CheckBox mLED1;
    ConnectedThread btt = null;
    BluetoothDevice mmDevice;
    String bt_address;
    boolean isBtConnected = false;

    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    // #defines for identifying shared types between calling functions
    final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status


    // https://github.com/bauerjj/Android-Simple-Bluetooth-Example/blob/master/app/src/main/java/com/mcuhq/simplebluetooth/MainActivity.java
    // http://mcuhq.com/27/simple-android-bluetooth-application-with-arduino-example
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        mBluetoothStatus = (TextView) root.findViewById(R.id.bluetoothStatus);
        mReadBuffer = (TextView) root.findViewById(R.id.readBuffer);
        mScanBtn = (Button) root.findViewById(R.id.scan);
        mOffBtn = (Button) root.findViewById(R.id.off);
        mDiscoverBtn = (Button) root.findViewById(R.id.discover);
        mListPairedDevicesBtn = (Button) root.findViewById(R.id.PairedBtn);
        mLED1 = (CheckBox) root.findViewById(R.id.checkboxLED1);

        mBTArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio

        mDevicesListView = (ListView) root.findViewById(R.id.devicesListView);
        mDevicesListView.setAdapter(mBTArrayAdapter); // assign model to view
        mDevicesListView.setOnItemClickListener(mDeviceClickListener);

        /*try {
            if (!mBTAdapter.isEnabled()) {
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
            } else {
                initiateBluetoothProcess();
            }
        } catch (NullPointerException npe) {
            Log.e("BluetoothFragment2", "error finding bluetooth adapter", npe);
        }*/

        try {
            if (mBTArrayAdapter == null) {
                // Device does not support Bluetooth
                mBluetoothStatus.setText("Status: Bluetooth not found");
                Toast.makeText(getContext(), "< Missing BT2 Support >", Toast.LENGTH_SHORT).show();
            } else if (!mBTAdapter.isEnabled()) {
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
            }
        } catch (NullPointerException npe) {
            Log.e("BluetoothFragment2", "error finding bluetooth adapter", npe);
        }



            mLED1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //if(mConnectedThread != null) //First check to make sure thread created
                    //    mConnectedThread.write("1");
                    if (mBTSocket.isConnected()) {
                        //btt.write("1".getBytes());
                        try {
                            if (mLED1.isChecked()) {
                                mBTSocket.getOutputStream().write("1".getBytes());
                                //ct2.write("1".getBytes());
                            }
                            else {
                                mBTSocket.getOutputStream().write("0".getBytes());
                                //ct2.write("0".getBytes());
                            }
                        } catch (Exception ioe) {
                            Log.e("BluetoothFragment2", "error writing to socket", ioe);
                        }
                        Toast.makeText(getContext(),"Wrote 1",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(),"Nothing",Toast.LENGTH_SHORT).show();
                    }
                }
            });


            mScanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bluetoothOn(v);
                }
            });

            mOffBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    bluetoothOff(v);
                }
            });

            mListPairedDevicesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    //listPairedDevices(v);
                    pairedDevicesList();
                }
            });

            mDiscoverBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //discover(v);
                }
            });

        return root;
    }

    private void pairedDevicesList() {
        try {
            mBTArrayAdapter.clear();
            mPairedDevices = mBTAdapter.getBondedDevices();
            //ArrayList list = new ArrayList();

            if (mBTAdapter.isEnabled() && mPairedDevices.size() > 0) {
                for(BluetoothDevice bt : mPairedDevices) {
                    mBTArrayAdapter.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
                    mmDevice = bt;
                }
            }
            else {
                Toast.makeText(getContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException npe) {
            Log.e("BluetoothFragment2", "error finding bluetooth adapter", npe);
        }


        //final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);
        //devicelist.setAdapter(adapter);
        //devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            /*if(!mBTAdapter.isEnabled()) {
                Toast.makeText(getContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
                return;
            }*/

            mBluetoothStatus.setText("Connecting...");
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            final String address = info.substring(info.length() - 17);
            //final String name = info.substring(0,info.length() - 17);

            bt_address = address;

            new ConnectBT().execute();

            // Spawn a new thread to avoid blocking the GUI one
            /*new Thread()
            {
                public void run() {
                    boolean fail = false;

                    BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                    try {
                        mBTSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }

                    // !!!
                    mBTAdapter.cancelDiscovery();


                    // Establish the Bluetooth socket connection.
                    try {
                        mBTSocket.connect();
                    } catch (IOException e) {
                        try {
                            fail = true;
                            mBTSocket.close();
                            mHandler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                        } catch (IOException e2) {
                            //insert code to deal with this
                            Toast.makeText(getContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(fail == false) {
                        mConnectedThread = new ConnectedThread(mBTSocket, mHandler);
                        mConnectedThread.start();

                        mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name).sendToTarget();
                    }
                }
            }.start();*/
        }
    };

    private void bluetoothOn(View view) {
        try {
            if (!mBTAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                mBluetoothStatus.setText("Bluetooth enabled");
                Toast.makeText(getContext(),"Bluetooth turned on",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(),"Bluetooth is already on", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException npe) {
            Log.e("BluetoothFragment", "error finding bluetooth adapter", npe);
        }
    }

    // Enter here after user selects "yes" or "no" to enabling radio
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent Data){
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                mBluetoothStatus.setText("Enabled");
                pairedDevicesList();
            }
            else
                mBluetoothStatus.setText("Disabled");
        }
    }

    private void bluetoothOff(View view){
        try {
            mBTAdapter.disable(); // turn off
            mBluetoothStatus.setText("Bluetooth disabled");
            Toast.makeText(getContext(),"Bluetooth turned Off", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException npe) {
            Log.e("BluetoothFragment", "error finding bluetooth adapter", npe);
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            //progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
            Toast.makeText(getContext(),"Connecting... pls wait", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (mBTSocket == null || !isBtConnected)
                {
                    mBTAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = mBTAdapter.getRemoteDevice(bt_address);//connects to the device's address and checks if it's available
                    mBTSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(BTMODULEUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBTSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                //msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                Toast.makeText(getContext(),"Connection Failed", Toast.LENGTH_SHORT).show();
                //finish();
            }
            else
            {
                //msg("Connected.");
                Toast.makeText(getContext(),"Connected.", Toast.LENGTH_SHORT).show();
                isBtConnected = true;
            }
            //progress.dismiss();
        }
    }
}